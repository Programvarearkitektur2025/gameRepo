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

        // Laster ressurser
        this.backgroundTexture = new Texture(Gdx.files.internal("Background2.png"));
        this.backButtonTexture = new Texture(Gdx.files.internal("backButtonBlue.png"));
        this.userTexture = new Texture(Gdx.files.internal("User_Headline.png"));
        this.pointsTexture = new Texture(Gdx.files.internal("Points_Headline.png"));
        this.leaderboardHeader = new Texture(Gdx.files.internal("Leaderboard-Header.png"));
        this.trophyTexture = new Texture(Gdx.files.internal("Trophy.png"));
        this.yourPointsTexture = new Texture(Gdx.files.internal("Your_Points.png"));
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 70;
        font = generator.generateFont(parameter);
        generator.dispose();

        enter(); // kaller initialize()
    }

    @Override
    protected void initialize() {
        // Bakgrunn
        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

        // Laster scoreboard (top-10)
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

        // Laster innlogget brukers egen score
        controller.loadOwnScore(new DataCallback() {
            @Override
            public void onSuccess(Object data) {
                // Forventet at data er Map{ "username": <String>, "score": <Integer> }
                if (data instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String,Object> userData = (Map<String,Object>) data;
                    String username = (String) userData.get("username");
                    int score = (Integer) userData.get("score");

                    Gdx.app.postRunnable(() -> {
                        showUserScore(username, score); // Viser i bunnen
                    });
                }
            }
            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });

        // Tilbake-knapp
        ImageButton backButton = new ImageButton(new TextureRegionDrawable(backButtonTexture));
        backButton.setSize(100, 100);
        backButton.setPosition(30, Gdx.graphics.getHeight() - 130);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Eksempel på å gå tilbake eller noe slikt
                // controller.goBackHome();
            }
        });
        stage.addActor(backButton);

        // Navbar
        navBar = new NavBar(stage, controller.getMain());

        // Leaderboard header
        Image header = new Image(leaderboardHeader);
        header.setSize(600, 100);
        header.setPosition(170, Gdx.graphics.getHeight() - 250);
        stage.addActor(header);

        // Trophy bilde
        Image trophy = new Image(trophyTexture);
        trophy.setSize(100, 100);
        trophy.setPosition(820, Gdx.graphics.getHeight() - 250);
        stage.addActor(trophy);

        // User / Points overskrifter
        Image userHeadline = new Image(userTexture);
        userHeadline.setSize(200, 70);
        userHeadline.setPosition(50, Gdx.graphics.getHeight() - 450);
        stage.addActor(userHeadline);

        Image pointsHeadline = new Image(pointsTexture);
        pointsHeadline.setSize(250, 70);
        pointsHeadline.setPosition(Gdx.graphics.getWidth() - 300, Gdx.graphics.getHeight() - 450);
        stage.addActor(pointsHeadline);

        // Tabell for scoreboard (ikke fylt her, fylles i populateLeaderboard())
        Table usernamesTable = new Table();
        usernamesTable.top().padTop(20);
        Table pointsTable = new Table();
        pointsTable.top().padTop(20);

        usernamesTable.setPosition(50, Gdx.graphics.getHeight() - 480);
        pointsTable.setPosition(Gdx.graphics.getWidth() - 320, Gdx.graphics.getHeight() - 480);

        stage.addActor(usernamesTable);
        stage.addActor(pointsTable);

        // "Your points" label-bilde
        Image yourPointsLabel = new Image(yourPointsTexture);
        yourPointsLabel.setSize(450, 70);
        yourPointsLabel.setPosition(50, Gdx.graphics.getHeight() - 1850);
        stage.addActor(yourPointsLabel);
    }

    /**
     * Viser top-10 scoreboard i to kolonner (username -> points).
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

            Label playerNameLabel = new Label(uname, labelStyle);
            playerNameLabel.setAlignment(Align.left);
            usernamesTable.add(playerNameLabel).left().padLeft(250).padTop(20).row();

            Label pointsLabel = new Label(String.valueOf(pts), labelStyle);
            pointsLabel.setAlignment(Align.right);
            pointsTable.add(pointsLabel).right().padLeft(100).padTop(20).row();
        }

        // Plasser tabellene
        usernamesTable.setPosition(50, Gdx.graphics.getHeight() - 480);
        pointsTable.setPosition(Gdx.graphics.getWidth() - 320, Gdx.graphics.getHeight() - 480);

        // Legg til i stage
        stage.addActor(usernamesTable);
        stage.addActor(pointsTable);
    }

    /**
     * Viser brukerens egen score nederst på skjermen.
     */
    private void showUserScore(String username, int score) {
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, skin.getColor("white"));

        Label myScoreLabel = new Label(username + ": " + score, labelStyle);
        myScoreLabel.setAlignment(Align.left);


        float x = 650f;
        float y = Gdx.graphics.getHeight() - 1850;

        myScoreLabel.setPosition(x, y);

        stage.addActor(myScoreLabel);
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
