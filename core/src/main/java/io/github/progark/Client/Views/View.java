package io.github.progark.Client.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

public abstract class View {
    protected Stage stage;
    protected boolean isInitialized = false;

    protected View() {
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);
    }

    public void enter() {
        if (!isInitialized) {
            initialize();
            isInitialized = true;
        }
    }

    protected abstract void initialize();

    public void update(float delta) {
        if (stage != null) {
            stage.act(delta);
        }
    }

    public void render() {
        if (stage != null) {
            stage.draw();
        }
    }

    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
    }

    protected void handleInput() {
        // Default input handling
    }

    // Necessary?

    /*
    protected void handleInput() {
        if(error!=null && error.getButton().isPressed()) {
            error.getDialog().hide();
        }
    }

     */
}
