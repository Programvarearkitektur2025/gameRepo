package io.github.progark.Server.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.progark.Server.Model.Game.GameModel;
import io.github.progark.Server.Model.Game.QuestionModel;
import io.github.progark.Server.Model.Game.RoundModel;
import io.github.progark.Server.Model.Login.UserModel;
import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;
/*
 * GameService.java
 * This class is responsible for managing game-related operations.
 * It handles loading game rounds, creating new games, and updating game states.
 * It interacts with the DatabaseManager to perform CRUD operations on game data.
 * The service also manages the game state and player interactions.
 * It provides methods to fetch relevant games, set new game rounds, and update the leaderboard.
 * The service is designed to be used by the GameController and other components of the application.
 */
public class GameService {

    private final DatabaseManager databaseManager;
    public GameService(DatabaseManager databaseManager){
        this.databaseManager = databaseManager;
    }

    /*
     * getRelevantGames
     * This method retrieves relevant games for a given user from the database.
     * It filters the games based on the user's username and returns a list of relevant games.
     * The method uses a callback to return the results asynchronously.
     */

    public void getRelevantGames(UserModel user, DataCallback callback) {
        String path = "lobbies";

        databaseManager.readData(path, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                List<Map<String, Object>> relevantGames = new ArrayList<>();

                if (data instanceof List<?>) {
                    List<?> gameList = (List<?>) data;

                    for (Object obj : gameList) {
                        if (obj instanceof Map) {
                            Map<String, Object> gameMap = (Map<String, Object>) obj;
                            Object lobbyCodeObj = gameMap.get("lobbyCode");

                            if (lobbyCodeObj instanceof String) {
                                String lobbyCode = (String) lobbyCodeObj;

                                try {
                                    GameModel game = GameModel.fromMap(lobbyCode, gameMap);

                                    if (game.getPlayerOne().equals(user.getUsername()) ||
                                        (game.getPlayerTwo() != null && game.getPlayerTwo().equals(user.getUsername()))) {

                                        Map<String, Object> gameEntry = new HashMap<>();
                                        gameEntry.put("gameId", lobbyCode);
                                        gameEntry.put("game", game);
                                        relevantGames.add(gameEntry);
                                    }
                                } catch (Exception e) {
                                    System.out.println("Failed to parse game with lobbyCode: " + lobbyCode + " — " + e.getMessage());
                                }
                            }
                        }
                    }

                    callback.onSuccess(relevantGames);
                } else {
                    callback.onFailure(new Exception("Invalid data format: Expected List<Map<String, Object>>, but got " + data.getClass().getSimpleName()));
                }
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

/*
 * setNewGameRounds
 * This method updates the game state in the database with new rounds.  
 * It retrieves the existing game data, updates the rounds and scores,
 * and writes the updated data back to the database.
 */
    public void setNewGameRounds(GameModel gameModel, List<RoundModel> newRounds) {
        String path = "lobbies/" + gameModel.getLobbyCode();

        databaseManager.readData(path, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (data instanceof Map) {
                    Map<String, Object> existingData = new HashMap<>((Map<String, Object>) data);

                    List<Map<String, Object>> roundMaps = new ArrayList<>();
                    for (RoundModel round : newRounds) {
                        roundMaps.add(round.toMap());
                    }

                    // 🧠 Save both the rounds AND updated game scores
                    existingData.put("games", roundMaps);
                    existingData.put("playerOnePoints", gameModel.getPlayerOnePoints());
                    existingData.put("playerTwoPoints", gameModel.getPlayerTwoPoints());

                    databaseManager.writeData(path, existingData);
                    System.out.println("✅ Game state saved successfully");
                } else {
                    System.err.println("⚠️ Unexpected data type when reading lobby: " + data.getClass().getSimpleName());
                }
            }

            @Override
            public void onFailure(Exception e) {
                System.err.println("❌ Failed to read existing lobby data: " + e.getMessage());
            }
        });
    }

/*
 * loadRoundsFromFirebase
 * This method loads game rounds from Firebase based on the specified difficulty level.
 * It retrieves questions from the database, filters them based on difficulty,
 * and creates RoundModel objects for the game.
 */
    public void loadRoundsFromFirebase(GameModel gameModel, int totalRounds, DataCallback callback) {
        String path = "questions";

        databaseManager.readData(path, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (!(data instanceof List<?>)) {
                    callback.onFailure(new Exception("Invalid Firestore data format."));
                    return;
                }

                List<?> documents = (List<?>) data;
                List<Map<String, Object>> validQuestions = new ArrayList<>();

                for (Object obj : documents) {
                    if (obj instanceof Map) {
                        Map<String, Object> docMap = (Map<String, Object>) obj;

                        Object diffObj = docMap.get("Difficulty");
                        Object questionObj = docMap.get("Question");
                        Object answersObj = docMap.get("Answers");

                        if (diffObj instanceof Number && questionObj instanceof String && answersObj instanceof Map) {
                            int difficulty = ((Number) diffObj).intValue();

                            if (difficulty == gameModel.getDifficulty()) {
                                validQuestions.add(docMap);
                            }
                        }
                    }
                }

                if (validQuestions.size() < totalRounds) {
                    callback.onFailure(new Exception("Not enough questions for difficulty " + gameModel.getDifficulty()));
                    return;
                }


                List<RoundModel> rounds = new ArrayList<>();
                Collections.shuffle(validQuestions);
                for (int i = 0; i < totalRounds; i++) {
                    Map<String, Object> questionDoc = validQuestions.get(i);

                    try {
                        String questionText = (String) questionDoc.get("Question");
                        int difficulty = ((Number) questionDoc.get("Difficulty")).intValue();

                        Map<String, Object> rawAnswers = (Map<String, Object>) questionDoc.get("Answers");
                        Map<String, Integer> answers = new HashMap<>();

                        for (Map.Entry<String, Object> entry : rawAnswers.entrySet()) {
                            answers.put(entry.getKey(), ((Number) entry.getValue()).intValue());
                        }

                        QuestionModel question = new QuestionModel(questionText, answers, difficulty);
                        RoundModel round = new RoundModel(question, gameModel.getPlayerOne(), gameModel.getPlayerTwo());
                        rounds.add(round);

                    } catch (Exception e) {
                        System.out.println("⚠️ Error parsing question: " + e.getMessage());
                    }
                }

                gameModel.setGames(rounds);
                setNewGameRounds(gameModel, rounds);
                callback.onSuccess(rounds);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void createQuestionsToFirebase(List<Map<String, Object>> questions) {
        for (Map<String, Object> q : questions) {
            databaseManager.writeQuestion("questions", q);
        }
    }

    public void updateLeaderBoard(GameModel gamemodel, AuthService authService){
        LeaderboardService leaderboardService = new LeaderboardService(databaseManager, authService);
        leaderboardService.updateLeaderBoard(gamemodel);
    }

    public void fetchCurrentRound(GameModel gameModel, DataCallback callback) {
        String path = "lobbies/" + gameModel.getLobbyCode();

        databaseManager.readData(path, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (!(data instanceof Map)) {
                    callback.onFailure(new Exception("Invalid data format: Expected Map."));
                    return;
                }

                Map<String, Object> lobbyData = (Map<String, Object>) data;
                Object gamesObj = lobbyData.get("games");

                if (!(gamesObj instanceof List)) {
                    callback.onFailure(new Exception("Missing or invalid 'games' list in lobby data."));
                    return;
                }

                List<?> gamesList = (List<?>) gamesObj;
                int currentIndex = (int) gameModel.getCurrentRound()-1;

                if (currentIndex < 0 || currentIndex >= gamesList.size()) {
                    callback.onFailure(new Exception("Current round index out of bounds."));
                    return;
                }

                Object roundDataObj = gamesList.get(currentIndex);

                if (!(roundDataObj instanceof Map)) {
                    callback.onFailure(new Exception("Invalid round format at index " + currentIndex));
                    return;
                }

                try {
                    Map<String, Object> roundData = (Map<String, Object>) roundDataObj;
                    RoundModel round = RoundModel.fromMap(roundData);
                    callback.onSuccess(round);
                } catch (Exception e) {
                    callback.onFailure(new Exception("Failed to parse RoundModel: " + e.getMessage()));
                }
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }
/*
 * loadExistingRoundsFromFirebase
 * This method loads existing rounds from Firebase for a given game model.
 * It retrieves the lobby data, checks for existing rounds,
 * and converts them into RoundModel objects.
 */
    public void loadExistingRoundsFromFirebase(GameModel gameModel, DataCallback callback) {
        String path = "lobbies/" + gameModel.getLobbyCode();

        databaseManager.readData(path, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (!(data instanceof Map)) {
                    callback.onFailure(new Exception("Invalid data format: Expected Map."));
                    return;
                }

                Map<String, Object> lobbyData = (Map<String, Object>) data;
                Object gamesObj = lobbyData.get("games");

                if (!(gamesObj instanceof List)) {
                    callback.onFailure(new Exception("Missing or invalid 'games' list in lobby data."));
                    return;
                }

                List<?> gameList = (List<?>) gamesObj;
                List<RoundModel> rounds = new ArrayList<>();

                for (Object obj : gameList) {
                    if (obj instanceof Map) {
                        try {
                            RoundModel round = RoundModel.fromMap((Map<String, Object>) obj);
                            rounds.add(round);
                        } catch (Exception e) {
                            System.err.println("⚠️ Failed to parse round: " + e.getMessage());
                        }
                    }
                }

                callback.onSuccess(rounds);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

}
