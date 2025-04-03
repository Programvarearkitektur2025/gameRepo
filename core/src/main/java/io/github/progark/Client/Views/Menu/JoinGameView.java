package io.github.progark.Client.Views.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import io.github.progark.Client.Controllers.JoinGameController;
import io.github.progark.Client.Views.View;
import io.github.progark.Main;

public class JoinGameView extends View {
    private final Skin skin;
    private final Texture backgroundTexture;
    private final Texture joinButtonTexture;
    private final Texture rectangleTexture;
    private final Texture backButtonTexture;
    private final Image background;
    private final BitmapFont font;
    private final JoinGameController controller;
    private TextField pinField;
    private Label statusLabel;

    public JoinGameView(JoinGameController controller) {
        super();
        this.controller = controller;
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        this.backgroundTexture = new Texture(Gdx.files.internal("Background2.png"));
        this.joinButtonTexture = new Texture(Gdx.files.internal("JoinButton.png"));
        this.rectangleTexture = new Texture(Gdx.files.internal("Rectangle.png"));
        this.backButtonTexture = new Texture(Gdx.files.internal("backButtonBlue.png"));
        this.background = new Image(backgroundTexture);

        // Font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 60;
        font = generator.generateFont(parameter);
        generator.dispose();
    }

    @Override
    protected void initialize() {
        background.setFillParent(true);
        stage.addActor(background);

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(200);
        stage.addActor(table);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        // Back Button
        ImageButton backButton = new ImageButton(new TextureRegionDrawable(backButtonTexture));
        backButton.setPosition(30, Gdx.graphics.getHeight() - 100); // Adjust position
        backButton.setSize(80, 80);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.goBackToHome();
            }
        });
        stage.addActor(backButton);

        // Bakgrunn for input
        Drawable inputBackground = new TextureRegionDrawable(rectangleTexture);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = Color.DARK_GRAY;
        textFieldStyle.background = inputBackground;
        textFieldStyle.cursor = skin.newDrawable("white", Color.GRAY);

        Label enterPinLabel = new Label("Enter game pin:", labelStyle);

        pinField = new TextField("", textFieldStyle);
        pinField.setMessageText("  Game PIN");
        pinField.setAlignment(Align.center);

        ImageButton joinButton = new ImageButton(new TextureRegionDrawable(joinButtonTexture));
        joinButton.getImage().setScaling(Scaling.stretch);
        joinButton.setSize(300, 120);

        joinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String pin = pinField.getText();
                if (!pin.isEmpty()) {
                    System.out.println("Attempting to join game with PIN: " + pin);
                    statusLabel.setText("Joining game...");
                    controller.joinLobby(pin);
                } else {
                    statusLabel.setText("Please enter a game PIN");
                }
            }
        });

        statusLabel = new Label("", new Label.LabelStyle(font, Color.WHITE));

        table.add(enterPinLabel).padBottom(60).row();
        table.add(pinField).width(550).height(100).padBottom(60).row();
        table.add(joinButton).padBottom(40).row();
        table.add(statusLabel);
    }

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
        backgroundTexture.dispose();
        joinButtonTexture.dispose();
        rectangleTexture.dispose();
        font.dispose();
    }
}
