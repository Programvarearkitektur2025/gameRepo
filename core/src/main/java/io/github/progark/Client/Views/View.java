package io.github.progark.Client.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

public abstract class View {
    protected Stage stage;
    protected boolean isInitialized = false;

    protected View() {
    }

    public void enter() {
        if (!isInitialized) {
            Gdx.app.postRunnable(() -> {
                stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
                Gdx.input.setInputProcessor(stage);
                initialize();
                isInitialized = true;
            });
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
            Gdx.gl.glClearColor(0.6588f, 0.7059f, 0.7059f, 1f); // #a8b4b4
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


            stage.draw();
        }
    }

    public void dispose() {
        if (stage != null) {
            stage.dispose();
            stage = null;
        }
        isInitialized = false;
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
