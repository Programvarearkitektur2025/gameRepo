package io.github.progark.Client.Controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.github.progark.Client.Views.Game.RoundView;
import io.github.progark.Main;
import io.github.progark.Server.Model.Game.GameModel;
import io.github.progark.Server.Model.Game.RoundModel;
import io.github.progark.Server.Service.AuthService;
import io.github.progark.Server.Service.GameService;
import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;

public class RoundController extends Controller {
    private GameService gameService;
    private RoundModel roundModel;
    private RoundView gameView;
    private GameModel parentGameModel;
    private Main main;
    private AuthService authService;
    private boolean roundAlreadyProcessed = false;
    private int roundIndex;


    public RoundController(GameModel gameModel, DatabaseManager databaseManager, Main main, AuthService authService) {
        this.gameService = new GameService(databaseManager);
        this.parentGameModel = gameModel;
        this.main = main;
        this.authService = authService;

        roundIndex = (int) parentGameModel.getCurrentRound() - 1;


        Object roundRaw = parentGameModel.getGames().get(roundIndex);

        if (roundRaw instanceof RoundModel) {
            this.roundModel = (RoundModel) roundRaw;
        } else {
            System.err.println("⚠️ Unknown round data type: " + roundRaw.getClass().getSimpleName());
        }

        // Ensure player usernames are set correctly
        if (roundModel.playerOneUsername == null) {
            roundModel.playerOneUsername = parentGameModel.getPlayerOne();
        }
        if (roundModel.playerTwoUsername == null) {
            roundModel.playerTwoUsername = parentGameModel.getPlayerTwo();
        }

        authService.getLoggedInUsername(new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                String username = (String) data;

                // ✅ Reset timer only for players who haven't played yet
                if (!roundModel.hasPlayerCompleted(username)) {
                    System.out.println("🔁 Resetting timer to 30 for " + username);
                    roundModel.setTimeRemaining(30);
                }
            }

            @Override
            public void onFailure(Exception e) {
                System.err.println("❌ Could not fetch user to check timer reset: " + e.getMessage());
            }
        });


        this.gameView = new RoundView(this);
        for (RoundModel round : parentGameModel.getGames()){
            System.out.println("Rounds constructor: " + round.getQuestion());
        }
        enter();
    }

    public void setGameView(RoundView gameView) {
        this.gameView = gameView;
    }

    public void handleAnswerSubmission(String input) {
        String answer = input.trim();

        authService.getLoggedInUsername(new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                String currentPlayer = (String) data;

                if (answer.isEmpty() || roundModel.hasAlreadySubmitted(currentPlayer, answer)) {
                    gameView.showMessage("Invalid answer. Please try again.");
                    return;
                }

                boolean success = submitAnswer(currentPlayer, answer);
                if (success) {
                    // 💡 Ensure the updated roundModel is saved back to the game list
                    parentGameModel.getGames().set(roundIndex, roundModel);

                    gameView.updateScore(getCurrentPlayerScore(currentPlayer));
                    gameView.updateSubmittedAnswers(getCurrentPlayerAnswers(currentPlayer));
                } else {
                    gameView.showMessage("Answer already submitted.");
                }
            }

            @Override
            public void onFailure(Exception e) {
                gameView.showMessage("Failed to get logged in user.");
                System.err.println("❌ Failed to fetch username: " + e.getMessage());
            }
        });
    }


    public boolean submitAnswer(String username, String answer) {
        return roundModel.submitAnswer(username, answer);
    }

    public Map<String, Integer> getSubmittedAnswers(String username) {
        return getCurrentPlayerAnswers(username);
    }

    public int getCurrentPlayerScore(String username) {
        if (username.equals(roundModel.playerOneUsername)) {
            return roundModel.getPlayerOneScore();
        } else if (username.equals(roundModel.playerTwoUsername)) {
            return roundModel.getPlayerTwoScore();
        } else {
            System.err.println("⚠️ Username not matched in round model.");
            return 0;
        }
    }

    public Map<String, Integer> getCurrentPlayerAnswers(String username) {
        if (username.equals(roundModel.playerOneUsername)) {
            return roundModel.getPlayerOneAnswers();
        } else if (username.equals(roundModel.playerTwoUsername)) {
            return roundModel.getPlayerTwoAnswers();
        } else {
            System.err.println("⚠️ Username not matched in round model.");
            return new HashMap<>();
        }
    }

    public void goToGame() {
        main.useGameController(parentGameModel);
    }

    public void updateGameState(float delta) {
        roundModel.updateTime(delta);
        gameView.updateTimeRemaining(roundModel.getTimeRemaining());

        if (roundModel.isTimeUp() && !roundAlreadyProcessed) {
            authService.getLoggedInUsername(new DataCallback() {
                @Override
                public void onSuccess(Object data) {
                    String username = (String) data;

                    // 🧠 Only process end-of-round logic if this player has already played
                    if (!roundModel.hasPlayerCompleted(username)) {
                        // Let the player stay and still play
                        return;
                    }

                    roundAlreadyProcessed = true;
                    gameView.showGameOver();
                    main.useGameController(parentGameModel);

                    for (RoundModel round : parentGameModel.getGames()) {
                        System.out.println("Round from endRoundEarly: " + round.getQuestion());
                    }

                    roundModel.markPlayerCompleted(username);
                    roundModel.setTimeRemaining(0);

                    if (Objects.equals(username, roundModel.playerOneUsername)) {
                        if (roundModel.getPlayerOneAnswers().isEmpty()) {
                            roundModel.submitAnswer(roundModel.playerOneUsername, "No answer");
                        }
                    }

                    if (Objects.equals(username, roundModel.playerTwoUsername)) {
                        if (roundModel.getPlayerTwoAnswers().isEmpty()) {
                            roundModel.submitAnswer(roundModel.playerTwoUsername, "No answer");
                        }
                    }

                    parentGameModel.getGames().set(roundIndex, roundModel);

                    if (bothPlayersHavePlayed(roundModel)) {
                        roundAlreadyProcessed = true;
                        awardPointToRoundWinner();
                    }

                    gameService.setNewGameRounds(parentGameModel, parentGameModel.getGames());
                }

                @Override
                public void onFailure(Exception e) {
                    System.err.println("❌ Failed to fetch username to mark player as completed: " + e.getMessage());
                    gameView.showMessage("Unable to end round. Failed to fetch current player.");
                }
            });
        }
    }



    private void awardPointToRoundWinner() {
        int p1Score = roundModel.getPlayerOneScore();
        int p2Score = roundModel.getPlayerTwoScore();

        if (p1Score > p2Score) {
            int currentPoints = parentGameModel.getPlayerOnePoints().intValue();
            parentGameModel.setPlayerOnePoints(currentPoints + 1);
        } else if (p2Score > p1Score) {
            int currentPoints = parentGameModel.getPlayerTwoPoints().intValue();
            parentGameModel.setPlayerTwoPoints(currentPoints + 1);
        } else {
            System.out.println("🤝 Round is a tie. No points awarded.");
        }
    }

    public boolean isTimeUp() {
        return roundModel.isTimeUp();
    }

    public int getScore() {
        return roundModel.getPlayerOneScore(); // Might be obsolete in multiplayer
    }

    public float getTimeRemaining() {
        return roundModel.getTimeRemaining();
    }

    public void updateTime(float delta) {
    }

    public String getQuestion() {
        return roundModel.getQuestion();
    }

    private boolean bothPlayersHavePlayed(RoundModel round) {
        // Check if both players have been marked as completed

        // For multiplayer, both players must have completed
        if (parentGameModel.isMultiplayer()) {

            boolean p1Completed = round.hasPlayerCompleted(round.playerOneUsername);
            boolean p2Completed = round.hasPlayerCompleted(round.playerTwoUsername);

            return p1Completed && p2Completed;
        }

        // For single player, only player one needs to complete

        return round.hasPlayerCompleted(round.playerOneUsername);
    }

    public void endRoundEarly() {
        for (RoundModel round: parentGameModel.getGames())
        {
            System.out.println("Round from endRoundEarly: " + round.getQuestion());
        }

        authService.getLoggedInUsername(new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                String username = (String) data;
                roundModel.markPlayerCompleted(username);
                roundModel.setTimeRemaining(0);


                if (Objects.equals(username, roundModel.playerOneUsername)) {
                    if (roundModel.getPlayerOneAnswers().isEmpty()) {
                        roundModel.submitAnswer(roundModel.playerOneUsername, "No answer");
                    }
                }

                if (Objects.equals(username, roundModel.playerTwoUsername)) {
                    if (roundModel.getPlayerTwoAnswers().isEmpty()) {
                        roundModel.submitAnswer(roundModel.playerTwoUsername, "No answer");
                    }
                }


                parentGameModel.getGames().set(roundIndex, roundModel);

                if (bothPlayersHavePlayed(roundModel)) {
                    roundAlreadyProcessed = true;

                    awardPointToRoundWinner();

                }

                gameService.setNewGameRounds(parentGameModel, parentGameModel.getGames());
            }

            @Override
            public void onFailure(Exception e) {
                System.err.println("❌ Failed to fetch username to mark player as completed: " + e.getMessage());
                gameView.showMessage("Unable to end round. Failed to fetch current player.");
            }
        });
    }

    /*
    private void fetchLatestRoundThenSave(Runnable onReadyToSave) {
        gameService.fetchCurrentRound(parentGameModel, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (data instanceof RoundModel) {
                    int roundIndex = parentGameModel.getCurrentRound().intValue();

                    mergeRoundModels((RoundModel) data, roundModel);
                    parentGameModel.getGames().set(roundIndex, roundModel);
                }
                onReadyToSave.run();
            }

            @Override
            public void onFailure(Exception e) {
                System.err.println("⚠️ Could not fetch latest round before saving: " + e.getMessage());
                // Proceed anyway (optional fallback)
                onReadyToSave.run();
            }
        });

    }

     */

    private void mergeRoundModels(RoundModel latest, RoundModel local) {
        // Merge player one's answers
        if (latest.getPlayerOneAnswers() != null) {
            for (Map.Entry<String, Integer> entry : latest.getPlayerOneAnswers().entrySet()) {
                if (!local.getPlayerOneAnswers().containsKey(entry.getKey())) {
                    local.getPlayerOneAnswers().put(entry.getKey(), entry.getValue());
                }
            }
        }

        // Merge player two's answers
        if (latest.getPlayerTwoAnswers() != null) {
            for (Map.Entry<String, Integer> entry : latest.getPlayerTwoAnswers().entrySet()) {
                if (!local.getPlayerTwoAnswers().containsKey(entry.getKey())) {
                    local.getPlayerTwoAnswers().put(entry.getKey(), entry.getValue());
                }
            }
        }

        // Update scores to reflect all answers
        int p1Score = 0;
        for (Integer value : local.getPlayerOneAnswers().values()) {
            p1Score += value;
        }
        local.setPlayerOneScore(p1Score);

        int p2Score = 0;
        for (Integer value : local.getPlayerTwoAnswers().values()) {
            p2Score += value;
        }
        local.setPlayerTwoScore(p2Score);

        // Preserve the time remaining from the local model
        local.setTimeRemaining(Math.min(latest.getTimeRemaining(), local.getTimeRemaining()));
    }


    @Override
    public void enter() {
        gameView.enter();
    }

    @Override
    public void update(float delta) {
        gameView.update(delta);
        gameView.render();
    }

    @Override
    public void dispose() {
        gameView.dispose();
    }
}
