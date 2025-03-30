package io.github.progark.Client.Views.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import io.github.progark.Client.Controllers.TutorialController;
import io.github.progark.Client.Views.View;

public class TutorialView extends View {

    private Texture backgroundTexture;
    private Texture howToPlayTexture;
    private Texture backButtonTexture;

    private final TutorialController controller;

    public TutorialView(TutorialController controller) {
        super();
        this.controller = controller;

        backgroundTexture = new Texture(Gdx.files.internal("Background2.png"));
        howToPlayTexture = new Texture(Gdx.files.internal("HowToPlay.png"));
        backButtonTexture = new Texture(Gdx.files.internal("backButtonBlue.png"));

        enter();
    }

    @Override
    protected void initialize() {
        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

        Image howToPlay = new Image(howToPlayTexture);
        howToPlay.setSize(1000,1810);
        float x = (Gdx.graphics.getWidth() - howToPlay.getWidth()) / 2f;
        float y = (Gdx.graphics.getHeight() - howToPlay.getHeight()) / 2f + 50; // juster +100 etter ønske

        howToPlay.setPosition(x, y);
        stage.addActor(howToPlay);

        ImageButton backButton = new ImageButton(new TextureRegionDrawable(backButtonTexture));
        backButton.setSize(100, 100);
        backButton.setPosition(30, Gdx.graphics.getHeight() - 130);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.goBackToHome();
            }
        });

        stage.addActor(backButton);
    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundTexture.dispose();
        howToPlayTexture.dispose();
        backButtonTexture.dispose();
    }
}
