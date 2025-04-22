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
/*
 * RoundController.java
 * This class is responsible for managing the game rounds.
 * It handles player interactions, answer submissions, and game state updates.
 * It also manages the view for displaying the game round.
 * The controller interacts with the GameService to perform operations related to game rounds.
 * It also handles the timer for each round and updates the game state accordingly.
 * The controller is responsible for merging round models and ensuring that the game state is consistent across all players.
 * The controller also handles the end of the round and awards points to the winning player.
 * The controller is responsible for managing the game view and updating it based on the game state.
 */
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

        roundIndex = (int) parentGameModel.getCurrentRound();


        Object roundRaw = parentGameModel.getGames().get(roundIndex);

        if (roundRaw instanceof RoundModel) {
            this.roundModel = (RoundModel) roundRaw;
        } else {
            System.err.println("⚠️ Unknown round data type: " + roundRaw.getClass().getSimpleName());
        }

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
/*
 * handleAnswerSubmission
 * This method handles the submission of answers by the players.
 * It checks if the answer is valid and if the player has already submitted an answer.
 * If the answer is valid, it updates the game state and notifies the view.
 * It also updates the scores and submitted answers for the current player.
 * The method uses the AuthService to get the logged-in username and checks if the answer is valid.
 * If the answer is valid, it updates the round model and notifies the view.
 */
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
/*
 * getCurrentPlayerAnswers
 * This method retrieves the current player's answers based on their username.
 * It checks if the username matches either player one or player two in the round model.
 */
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
/*
 * updateGameState
 * This method updates the game state based on the time elapsed since the last update.
 * It checks if the time is up and if the round has already been processed.
 * If the time is up and the round has not been processed, it fetches the logged-in username
 * and checks if the player has already completed the round.
 * If the player has not completed the round, it allows them to continue playing.
 * If the player has completed the round, it processes the end of the round and awards points to the winner.
 * It also updates the game view with the time remaining and shows the game over screen if necessary.
 */
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


/*
 * awardPointToRoundWinner
 * This method awards a point to the player who won the round.
 * It compares the scores of both players and updates their points accordingly.
 * If the scores are equal, it indicates a tie and no points are awarded.
 * It uses the parent game model to update the points for each player.
 */
    private void awardPointToRoundWinner() {
        int p1Score = roundModel.getPlayerOneScore();
        int p2Score = roundModel.getPlayerTwoScore();

        if (p1Score > p2Score) {
            Number currentPoints = parentGameModel.getPlayerOnePoints();
            parentGameModel.setPlayerOnePoints(currentPoints.intValue() + 1);

        } else if (p2Score > p1Score) {
            Number currentPoints = parentGameModel.getPlayerTwoPoints();
            parentGameModel.setPlayerTwoPoints(currentPoints.intValue() + 1);
        } else {
            System.out.println("🤝 Round is a tie. No points awarded.");
        }
    }

    public boolean isTimeUp() {
        return roundModel.isTimeUp();
    }

    public int getScore() {
        return roundModel.getPlayerOneScore(); 
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

        if (parentGameModel.isMultiplayer()) {

            boolean p1Completed = round.hasPlayerCompleted(round.playerOneUsername);
            boolean p2Completed = round.hasPlayerCompleted(round.playerTwoUsername);

            return p1Completed && p2Completed;
        }


        return round.hasPlayerCompleted(round.playerOneUsername);
    }
/*
 * endRoundEarly
 * This method is called to end the round early.
 * It fetches the logged-in username and marks the player as completed.
 * It also sets the time remaining to 0 and submits a default answer if the player has not answered yet.
 * It updates the game model with the new round state and checks if both players have played.
 * If both players have played, it awards points to the round winner.
 */
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

    private void mergeRoundModels(RoundModel latest, RoundModel local) {
        if (latest.getPlayerOneAnswers() != null) {
            for (Map.Entry<String, Integer> entry : latest.getPlayerOneAnswers().entrySet()) {
                if (!local.getPlayerOneAnswers().containsKey(entry.getKey())) {
                    local.getPlayerOneAnswers().put(entry.getKey(), entry.getValue());
                }
            }
        }

        if (latest.getPlayerTwoAnswers() != null) {
            for (Map.Entry<String, Integer> entry : latest.getPlayerTwoAnswers().entrySet()) {
                if (!local.getPlayerTwoAnswers().containsKey(entry.getKey())) {
                    local.getPlayerTwoAnswers().put(entry.getKey(), entry.getValue());
                }
            }
        }

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
