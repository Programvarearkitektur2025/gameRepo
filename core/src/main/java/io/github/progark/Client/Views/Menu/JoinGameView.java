package io.github.progark.Client.Views.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

import io.github.progark.Client.Views.View;
import io.github.progark.Main;

public class JoinGameView extends View {
    private final Main game;
    private final Skin skin;
    private final Texture backgroundTexture;
    private final Texture joinButtonTexture;
    private final Image background;
    //private final BitmapFont bigFont;
    //private final BitmapFont smallFont;
    private TextField pinField;
    private Label statusLabel;

    public JoinGameView(Main game) {
        super();
        this.game = game;
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Load textures
        this.backgroundTexture = new Texture(Gdx.files.internal("Background_1.png"));
        this.joinButtonTexture = new Texture(Gdx.files.internal("JoinButton.png"));

        // Create background
        this.background = new Image(backgroundTexture);

        // Initialize fonts
        /*
        this.bigFont = new BitmapFont(Gdx.files.internal("fonts/big.fnt"));
        this.smallFont = new BitmapFont(Gdx.files.internal("fonts/small.fnt"));

         */
    }

    @Override
    protected void initialize() {
        // Set up background
        background.setFillParent(true);
        stage.addActor(background);

        // Create main table
        Table table = new Table();
        table.setFillParent(true);
        table.center().padTop(200);
        stage.addActor(table);

        // Create styles
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        //labelStyle.font = bigFont;
        labelStyle.fontColor = Color.BLACK;

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        //textFieldStyle.font = smallFont;
        textFieldStyle.fontColor = Color.BLACK;
        textFieldStyle.background = skin.getDrawable("textfield");
        textFieldStyle.cursor = skin.getDrawable("cursor");
        textFieldStyle.selection = skin.getDrawable("selection");

        // Create UI elements
        Label enterPinLabel = new Label("Enter game pin:", labelStyle);
        pinField = new TextField("", textFieldStyle);
        pinField.setMessageText("Game PIN");
        pinField.setAlignment(1); // center-aligned text

        // Create join button
        TextureRegionDrawable joinDrawable = new TextureRegionDrawable(joinButtonTexture);
        ImageButton joinButton = new ImageButton(joinDrawable);
        joinButton.getImage().setScaling(Scaling.stretch);
        joinButton.getImage().setSize(300, 60);
        joinButton.setSize(300, 60);
        joinButton.invalidateHierarchy();

        // Create status label
        statusLabel = new Label("", skin);
        statusLabel.setFontScale(1.5f);

        // Add button listener
        joinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String pin = pinField.getText();
                if (!pin.isEmpty()) {
                    // TODO: Implement game joining logic
                    System.out.println("Attempting to join game with PIN: " + pin);
                    statusLabel.setText("Joining game...");
                } else {
                    statusLabel.setText("Please enter a game PIN");
                }
            }
        });

        // Add UI elements to table
        table.add(enterPinLabel).padBottom(60).row();
        table.add(pinField).width(550).height(100).padBottom(60).row();
        table.add(joinButton).padBottom(20).row();
        table.add(statusLabel);
    }

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
        backgroundTexture.dispose();
        joinButtonTexture.dispose();
        //bigFont.dispose();
        //smallFont.dispose();
    }
}
