package io.github.progark.Client.Views.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.progark.Client.Controllers.CreateGameController;
import io.github.progark.Client.Views.View;
import io.github.progark.Main;

import java.util.Random;

public class CreateGameView extends View {
    private final Skin skin;
    private final Texture backgroundTexture;
    private final Texture startGameButtonTexture;
    private final Image background;
    private final BitmapFont font;

    private final CreateGameController createGameController;

    public CreateGameView(CreateGameController createGameController) {
        super();
        this.createGameController = createGameController;
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        this.backgroundTexture = new Texture(Gdx.files.internal("Background_1.png"));
        this.startGameButtonTexture = new Texture(Gdx.files.internal("StartGameButton.png"));
        this.background = new Image(backgroundTexture);

        // Font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 80;
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

        // Title label
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        Label titleLabel = new Label("Your game pin:", labelStyle);
        titleLabel.setAlignment(Align.center);

        // Random 4-digit pin
        String pin = String.format("%04d", new Random().nextInt(10000));
        Label pinLabel = new Label(pin, labelStyle);
        pinLabel.setAlignment(Align.center);

        // Start game button
        ImageButton startGameButton = new ImageButton(new TextureRegionDrawable(startGameButtonTexture));
        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Start game clicked");
                // game.setScreen(new YourNextScreen(game));
            }
        });

        // Layout
        table.add(titleLabel).padBottom(30).row();
        table.add(pinLabel).padBottom(80).row();
        table.add(startGameButton).width(600).height(180);
    }

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
        backgroundTexture.dispose();
        startGameButtonTexture.dispose();
        font.dispose();
    }
}
