package io.github.progark.Client.Views.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import io.github.progark.Main;

public class NavBar {

    private final Texture navBarTexture;
    private final Texture transparentTexture;

    public NavBar(Stage stage, Main main) {
        navBarTexture = new Texture(Gdx.files.internal("navBar2.png"));
        transparentTexture = new Texture(Gdx.files.internal("Transparent.png"));

        // Nav bar image
        Image navBar = new Image(navBarTexture);
        navBar.setSize(Gdx.graphics.getWidth(), 300);
        navBar.setPosition(0, 0);
        stage.addActor(navBar);

        float iconSize = 150;
        float yPos = 75;

        // Home button
        ImageButton homeBtn = makeButton();
        homeBtn.setPosition(Gdx.graphics.getWidth() * 0.05f, yPos);
        homeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.useHomeController();
            }
        });
        stage.addActor(homeBtn);

        // Leaderboard button
        ImageButton leaderboardBtn = makeButton();
        leaderboardBtn.setPosition(Gdx.graphics.getWidth() * 0.30f, yPos);
        leaderboardBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.useLeaderBoardController();
            }
        });
        stage.addActor(leaderboardBtn);

        // User button
        ImageButton userBtn = makeButton();
        userBtn.setPosition(Gdx.graphics.getWidth() * 0.53f, yPos);
        userBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.useUserController();
            }
        });
        stage.addActor(userBtn);

        // Settings button
        ImageButton settingsBtn = makeButton();
        settingsBtn.setPosition(Gdx.graphics.getWidth() * 0.76f, yPos);
        settingsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.useSettingsController();
            }
        });
        stage.addActor(settingsBtn);
    }

    private ImageButton makeButton() {
        ImageButton btn = new ImageButton(new TextureRegionDrawable(new TextureRegion(transparentTexture)));
        btn.setSize(150, 150);
        return btn;
    }

    public void dispose() {
        navBarTexture.dispose();
        transparentTexture.dispose();
    }
}
