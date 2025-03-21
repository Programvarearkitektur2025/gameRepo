package io.github.progark.Client.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.function.Supplier;

import io.github.progark.Main;

public abstract class View {
    protected SpriteBatch spriteBatch;
    //protected Background background;
    protected Stage stage;
    //protected ErrorDialog error;


    protected View() {
        stage= new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);
        spriteBatch = new SpriteBatch();
        //background = new Background(new Texture(Gdx.files.internal("background_grey.png")),0,0, 5,true ,true);
    }
    public void setError(String message){
        /*
        if (error != null) {
            error.getButton().setChecked(true);
        }
        error = new ErrorDialog(message);
        error.getDialog().show(stage);
         */
    }

    protected void handleInput() {
        /*
        if(error!=null && error.getButton().isPressed()) {
            error.getDialog().hide();
        }
         */
    }

    public abstract void update(float dt);

    public void render(SpriteBatch sb) {
        /*
        sb.begin();
        background.drawGameObject(sb);
         */

    }

    public void dispose() {
        /*
        background.getImage().dispose();
        sb.dispose();
        stage.dispose();

         */
    }

    public void renderStage() {
        /*
        stage.draw();
        stage.act();

         */
    }

    public static void safeSetScreen(Main game, Supplier<Screen> screenSupplier) {
        Gdx.app.postRunnable(() -> game.setScreen(screenSupplier.get()));
    }
}
