package io.github.progark.Client.Views.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Align;

import java.util.Map;

import io.github.progark.Client.Views.View;
import io.github.progark.Client.Views.Components.NavBar;
import io.github.progark.Client.Controllers.LeaderboardController;
import io.github.progark.Server.Model.Game.LeaderboardModel;
import io.github.progark.Server.database.DataCallback;
/*
 * LeaderBoardView.java
 * This class is responsible for displaying the leaderboard view in the application.
 * It handles the rendering of the leaderboard, user scores, and navigation.
 * It also manages the loading of leaderboard data and user scores from the server.
 * The view interacts with the LeaderboardController to perform operations related to the leaderboard.
 */
public class LeaderBoardView extends View {

    private final Texture backgroundTexture, backButtonTexture, userTexture, pointsTexture,
        leaderboardHeader, trophyTexture, yourPointsTexture;
    private final LeaderboardController controller;
    private final BitmapFont font;
    private NavBar navBar;
    private final Skin skin;

    public LeaderBoardView(LeaderboardController controller) {
        super();
        this.controller = controller;

        this.backgroundTexture = new Texture(Gdx.files.internal("Background2.png"));
        this.backButtonTexture = new Texture(Gdx.files.internal("backButtonBlue.png"));
        this.userTexture = new Texture(Gdx.files.internal("User_Headline.png"));
        this.pointsTexture = new Texture(Gdx.files.internal("Points_Headline.png"));
        this.leaderboardHeader = new Texture(Gdx.files.internal("Leaderboard-Header.png"));
        this.trophyTexture = new Texture(Gdx.files.internal("Trophy.png"));
        this.yourPointsTexture = new Texture(Gdx.files.internal("Your_Points.png"));
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 70;
        font = generator.generateFont(parameter);
        generator.dispose();

        enter(); 
    }
/*
 * initialize
 * This method is responsible for initializing the leaderboard view.
 * It sets up the background, loads the leaderboard data, and displays the user's score.
 * It also creates the navigation bar and sets up the leaderboard header and trophy image.  
 */
    @Override
    protected void initialize() {
        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);
        controller.loadLeaderboard(new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (data instanceof LeaderboardModel) {
                    LeaderboardModel model = (LeaderboardModel) data;
                    Gdx.app.postRunnable(() -> {
                        populateLeaderboard(model);
                    });
                }
            }
            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });

        controller.loadOwnScore(new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                if (data instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String,Object> userData = (Map<String,Object>) data;
                    String username = (String) userData.get("username");
                    int score = (Integer) userData.get("score");

                    Gdx.app.postRunnable(() -> {
                        showUserScore(username, score); 
                    });
                }
            }
            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });

        navBar = new NavBar(stage, controller.getMain());

        Image header = new Image(leaderboardHeader);
        header.setSize(600, 100);
        header.setPosition(170, Gdx.graphics.getHeight() - 250);
        stage.addActor(header);

        Image trophy = new Image(trophyTexture);
        trophy.setSize(100, 100);
        trophy.setPosition(820, Gdx.graphics.getHeight() - 250);
        stage.addActor(trophy);

        Image userHeadline = new Image(userTexture);
        userHeadline.setSize(200, 70);
        userHeadline.setPosition(50, Gdx.graphics.getHeight() - 450);
        stage.addActor(userHeadline);

        Image pointsHeadline = new Image(pointsTexture);
        pointsHeadline.setSize(250, 70);
        pointsHeadline.setPosition(Gdx.graphics.getWidth() - 300, Gdx.graphics.getHeight() - 450);
        stage.addActor(pointsHeadline);

        Table usernamesTable = new Table();
        usernamesTable.top().padTop(20);
        Table pointsTable = new Table();
        pointsTable.top().padTop(20);

        usernamesTable.setPosition(50, Gdx.graphics.getHeight() - 480);
        pointsTable.setPosition(Gdx.graphics.getWidth() - 320, Gdx.graphics.getHeight() - 480);

        stage.addActor(usernamesTable);
        stage.addActor(pointsTable);

        Image yourPointsLabel = new Image(yourPointsTexture);
        yourPointsLabel.setSize(450, 70);
        yourPointsLabel.setPosition(50, Gdx.graphics.getHeight() - 1850);
        stage.addActor(yourPointsLabel);
    }

    /*
     * populateLeaderboard
     * This method populates the leaderboard with user scores.
     * It creates two tables: one for usernames and one for points.
     * It iterates through the leaderboard data and adds each user's name and score to the respective tables.
     * The tables are then added to the stage for rendering.
     */
    private void populateLeaderboard(LeaderboardModel model) {
        Map<String, Integer> leaderboard = model.getUserScore();

        Table usernamesTable = new Table();
        Table pointsTable = new Table();

        usernamesTable.top().padTop(20);
        pointsTable.top().padTop(20);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, skin.getColor("white"));

        for (Map.Entry<String, Integer> entry : leaderboard.entrySet()) {
            String uname = entry.getKey();
            int pts = entry.getValue();

            if (uname.length() > 15) {
                String shortname = uname.substring(0, 15) + "..";
                Label shortNameLabel = new Label(shortname, labelStyle);
                shortNameLabel.setAlignment(Align.left);
                usernamesTable.add(shortNameLabel).left().width(300).padLeft(300).padTop(20).row();
            } else {
                Label playerNameLabel = new Label(uname, labelStyle);
                playerNameLabel.setAlignment(Align.left);
                usernamesTable.add(playerNameLabel).left().width(300).padLeft(300).padTop(20).row();
            }

            Label pointsLabel = new Label(String.valueOf(pts), labelStyle);
            pointsLabel.setAlignment(Align.left);
            pointsTable.add(pointsLabel).left().width(200).padLeft(100).padTop(20).row();
        }

        usernamesTable.setPosition(50, Gdx.graphics.getHeight() - 480);
        pointsTable.setPosition(Gdx.graphics.getWidth() - 250, Gdx.graphics.getHeight() - 480);

        stage.addActor(usernamesTable);
        stage.addActor(pointsTable);
    }

/*
 * showUserScore
 * This method displays the user's score on the leaderboard.
 * It creates labels for the username and score, sets their positions,
 * and adds them to the stage for rendering.
 */
    private void showUserScore(String username, int score) {
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, skin.getColor("white"));

        Label myUserName = new Label(username, labelStyle);
        Label myPoints = new Label(score + "", labelStyle);
        myUserName.setAlignment(Align.left);
        myPoints.setAlignment(Align.left);

        myUserName.setPosition(50, Gdx.graphics.getHeight() - 2000);
        myPoints.setPosition(Gdx.graphics.getWidth() - 300, Gdx.graphics.getHeight() - 2000);

        stage.addActor(myUserName);
        stage.addActor(myPoints);
    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundTexture.dispose();
        backButtonTexture.dispose();
        userTexture.dispose();
        pointsTexture.dispose();
        leaderboardHeader.dispose();
        trophyTexture.dispose();
        yourPointsTexture.dispose();
        font.dispose();
        skin.dispose();
    }
}
