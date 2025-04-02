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

import io.github.progark.Client.Controllers.LeaderBoardController;
import io.github.progark.Client.Views.View;
import io.github.progark.Client.Views.Components.NavBar;

public class LeaderBoardView extends View {

    private final Texture backgroundTexture, backButtonTexture, userTexture, pointsTexture, leaderboardHeader, trophyTexture, yourPointsTexture;
    private final LeaderBoardController controller;
    private final BitmapFont font;
    private NavBar navBar;
    private final Skin skin;


    public LeaderBoardView(LeaderBoardController controller) {
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

        // Font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 70;
        font = generator.generateFont(parameter);
        generator.dispose();

        enter();
    }

    @Override
    protected void initialize() {
        // Bakgrunn
        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

        // Tilbake knapp
        ImageButton backButton = new ImageButton(new TextureRegionDrawable(backButtonTexture));
        backButton.setSize(100, 100);
        backButton.setPosition(30, Gdx.graphics.getHeight() - 130);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.goBackHome();
            }
        });
        stage.addActor(backButton);

        // Navbar
        navBar = new NavBar(stage, controller.getMain());

        // Leaderboard header
        Image header = new Image(leaderboardHeader);
        header.setSize(600, 100);
        header.setPosition(170, Gdx.graphics.getHeight() - 250); // Center the header horizontally
        stage.addActor(header);

        // Trophy bilde
        Image trophy = new Image(trophyTexture);
        trophy.setSize(100, 100);
        trophy.setPosition(820, Gdx.graphics.getHeight() - 250); // Center the header horizontally
        stage.addActor(trophy);

        // User og Points Labels
        Image userHeadline = new Image(userTexture);
        userHeadline.setSize(200, 70);
        userHeadline.setPosition(50, Gdx.graphics.getHeight() - 450); // Position user label on the left
        stage.addActor(userHeadline);

        Image pointsHeadline = new Image(pointsTexture);
        pointsHeadline.setSize(250, 70);
        pointsHeadline.setPosition(Gdx.graphics.getWidth() - 300, Gdx.graphics.getHeight() - 450); // Position points label on the right
        stage.addActor(pointsHeadline);

        // Henter data fra controller
        Map<String, Integer> leaderboard = controller.getLeaderboard();

        // Lager tabeller for både username og points
        Table usernamesTable = new Table();
        usernamesTable.top().padTop(20); // Add space under the user label
        Table pointsTable = new Table();
        pointsTable.top().padTop(20); // Add space under the points label

        // Legg til alle 10 entries returnert i hashmapet fra queryen
        for (Map.Entry<String, Integer> entry : leaderboard.entrySet()) {
            // Usernames
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, skin.getColor("white"));
            Label playerNameLabel = new Label(entry.getKey(), labelStyle);
            playerNameLabel.setAlignment(Align.left);
            playerNameLabel.setWidth(200);
            usernamesTable.add(playerNameLabel).left().padLeft(250).padTop(20).row();

            // Points
            Label pointsLabel = new Label(entry.getValue().toString(), labelStyle);
            pointsLabel.setAlignment(Align.right);
            pointsTable.setWidth(20);
            pointsTable.add(pointsLabel).right().padLeft(100).padTop(20).row();
        }

        usernamesTable.setPosition(50, Gdx.graphics.getHeight() - 480);
        pointsTable.setPosition(Gdx.graphics.getWidth() - 320, Gdx.graphics.getHeight() - 480);

        // Your points label
        Image yourPointsLabel = new Image(yourPointsTexture);
        yourPointsLabel.setSize(450, 70);
        yourPointsLabel.setPosition(50, Gdx.graphics.getHeight() - 1850);
        stage.addActor(yourPointsLabel);

        // Den innloggede brukeren sine poeng
        // Henter bruker og poeng fra query i controller
        String loggedInUser = controller.getLoggedInUser();
        int userScore = controller.getLoggedInUserPoints();

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, skin.getColor("white"));

        // Username (logged in user)
        Label loggedInUserLabel = new Label(loggedInUser, labelStyle);
        loggedInUserLabel.setAlignment(Align.left);
        usernamesTable.add(loggedInUserLabel).left().padLeft(250).padTop(300).row();

        // Points (logged in user)
        Label loggedInUserScoreLabel = new Label(String.valueOf(userScore), labelStyle);
        loggedInUserScoreLabel.setAlignment(Align.right);
        pointsTable.add(loggedInUserScoreLabel).right().padLeft(250).padTop(300).row();

        stage.addActor(usernamesTable);
        stage.addActor(pointsTable);
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
