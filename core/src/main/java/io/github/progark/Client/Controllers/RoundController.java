package io.github.progark.Client.Controllers;

import java.util.HashMap;
import java.util.Map;

import io.github.progark.Main;
import io.github.progark.Server.Model.Game.GameModel;
import io.github.progark.Server.Model.Game.RoundModel;
import io.github.progark.Server.Service.AuthService;
import io.github.progark.Server.Service.GameService;
import io.github.progark.Server.Service.RoundService;
import io.github.progark.Client.Views.Game.RoundView;
import io.github.progark.Server.Service.SolutionService;
import io.github.progark.Server.database.DataCallback;
import io.github.progark.Server.database.DatabaseManager;

public class RoundController extends Controller {
    private GameService gameService;
    private SolutionService solutionService;
    private RoundModel roundModel;
    private RoundView gameView;
    private GameModel parentGameModel;
    private Main main;
    private AuthService authService;
    private boolean roundAlreadyProcessed = false;


    public RoundController(GameModel gameModel, DatabaseManager databaseManager, Main main, AuthService authService) {
        this.solutionService = new SolutionService(databaseManager);
        this.gameService = new GameService(databaseManager);
        this.parentGameModel = gameModel;
        this.main = main;
        this.authService = authService;

        int roundIndex = parentGameModel.getCurrentRound().intValue() - 1;

        if (roundIndex >= 0 && roundIndex < parentGameModel.getGames().size()) {
            Object roundRaw = parentGameModel.getGames().get(roundIndex);

            if (roundRaw instanceof RoundModel) {
                this.roundModel = (RoundModel) roundRaw;
            } else if (roundRaw instanceof Map) {
                this.roundModel = RoundModel.fromMap((Map<String, Object>) roundRaw);
            } else {
                System.err.println("⚠️ Unknown round data type: " + roundRaw.getClass().getSimpleName());
            }
        } else {
            System.err.println("⚠️ Invalid round index: " + roundIndex);
        }

        this.gameView = new RoundView(this);
        enter();
    }

    public void setGameView(RoundView gameView) {
        this.gameView = gameView;
    }

    public void handleAnswerSubmission(String input) {
        String answer = input.trim();
        System.out.println("Checking answer: " + answer);

        authService.getLoggedInUsername(new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                String currentPlayer = (String) data;
                System.out.println("Current player: " + currentPlayer);

                if (answer.isEmpty() || roundModel.hasAlreadySubmitted(currentPlayer, answer)) {
                    gameView.showMessage("Invalid answer. Please try again.");
                    return;
                }

                boolean success = submitAnswer(currentPlayer, answer);
                if (success) {
                    // 💡 Ensure the updated roundModel is saved back to the game list
                    int roundIndex = parentGameModel.getCurrentRound().intValue() - 1;
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
        System.out.println("player one username: + " + roundModel.playerOneUsername);
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
            roundAlreadyProcessed = true;
            gameView.showGameOver();

            fetchLatestRoundThenSave(() -> {
                int roundIndex = parentGameModel.getCurrentRound().intValue() - 1;
                parentGameModel.getGames().set(roundIndex, roundModel);

                if (parentGameModel.isMultiplayer()) {
                    if (bothPlayersHavePlayed(roundModel)) {
                        System.out.println("🎯 Multiplayer round finished.");
                        awardPointToRoundWinner();
                        parentGameModel.setCurrentRound(parentGameModel.getCurrentRound().intValue() + 1);
                    } else {
                        gameService.setNewGameRounds(parentGameModel, parentGameModel.getGames());
                        System.out.println("✅ Partial state saved — waiting for the other player...");
                        return;
                    }
                } else {
                    parentGameModel.setPlayerOnePoints(parentGameModel.getPlayerOnePoints().intValue() + roundModel.getPlayerOneScore());
                    parentGameModel.setCurrentRound(parentGameModel.getCurrentRound().intValue() + 1);
                }

                gameService.setNewGameRounds(parentGameModel, parentGameModel.getGames());
                returnToGameView(roundModel);
            });
        }
    }


    private void awardPointToRoundWinner() {
        int p1Score = roundModel.getPlayerOneScore();
        int p2Score = roundModel.getPlayerTwoScore();

        if (p1Score > p2Score) {
            int currentPoints = parentGameModel.getPlayerOnePoints().intValue();
            parentGameModel.setPlayerOnePoints(currentPoints + 1);
            System.out.println("🏆 Player One wins the round. +1 point");
        } else if (p2Score > p1Score) {
            int currentPoints = parentGameModel.getPlayerTwoPoints().intValue();
            parentGameModel.setPlayerTwoPoints(currentPoints + 1);
            System.out.println("🏆 Player Two wins the round. +1 point");
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

    public void getQuestionByID(String ID) {
        solutionService.getQuestion(ID, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                System.out.println(data);
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("❌ Failed to fetch question: " + e.getMessage());
            }
        });
    }

    public String getQuestion() {
        return roundModel.getQuestion();
    }

    private boolean bothPlayersHavePlayed(RoundModel round) {
        return round.getPlayerOneAnswers() != null && !round.getPlayerOneAnswers().isEmpty() &&
            round.getPlayerTwoAnswers() != null && !round.getPlayerTwoAnswers().isEmpty();
    }


    public void returnToGameView(RoundModel roundModel) {
        main.useGameController(parentGameModel);
    }

    public void endRoundEarly() {
        if (roundAlreadyProcessed) return;

        authService.getLoggedInUsername(new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                String username = (String) data;
                roundModel.markPlayerCompleted(username);

                // Save the updated roundModel back to the parentGameModel
                int roundIndex = parentGameModel.getCurrentRound().intValue() - 1;
                parentGameModel.getGames().set(roundIndex, roundModel);
                roundModel.setTimeRemaining(0);

                updateGameState(0.01f);
            }

            @Override
            public void onFailure(Exception e) {
                System.err.println("❌ Failed to fetch username to mark player as completed: " + e.getMessage());
                gameView.showMessage("Unable to end round. Failed to fetch current player.");
            }
        });
    }

    private void fetchLatestRoundThenSave(Runnable onReadyToSave) {
        gameService.fetchCurrentRound(parentGameModel, new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (data instanceof RoundModel) {
                    int roundIndex = parentGameModel.getCurrentRound().intValue() - 1;

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

    private void mergeRoundModels(RoundModel latest, RoundModel local) {
        // Merge opponent's answers if they submitted something new
        if (latest.getPlayerOneAnswers() != null) {
            local.getPlayerOneAnswers().putAll(latest.getPlayerOneAnswers());
        }
        if (latest.getPlayerTwoAnswers() != null) {
            local.getPlayerTwoAnswers().putAll(latest.getPlayerTwoAnswers());
        }

        // Also carry over score if needed
        local.setPlayerOneScore(Math.max(latest.getPlayerOneScore(), local.getPlayerOneScore()));
        local.setPlayerTwoScore(Math.max(latest.getPlayerTwoScore(), local.getPlayerTwoScore()));
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
