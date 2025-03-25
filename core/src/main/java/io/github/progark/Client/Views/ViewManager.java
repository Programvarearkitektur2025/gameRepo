package io.github.progark.Client.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ViewManager {
    private View currentView;
    private final SpriteBatch spriteBatch;

    public ViewManager() {
        spriteBatch = new SpriteBatch();
    }

    public void setView(View newView) {
        // Always use postRunnable to ensure view transitions happen on the main thread
        Gdx.app.postRunnable(() -> {
            if (currentView != null) {
                currentView.dispose();
            }
            currentView = newView;
            currentView.enter();
        });
    }

    public void setView(ViewSupplier viewSupplier) {
        // Create and set the view on the main thread
        Gdx.app.postRunnable(() -> {
            if (currentView != null) {
                currentView.dispose();
            }
            currentView = viewSupplier.create();
            currentView.enter();
        });
    }

    public void update(float delta) {
        if (currentView != null) {
            currentView.update(delta);
            currentView.handleInput();
        }
    }

    public void render() {
        if (currentView != null) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            currentView.render();
        }
    }

    public void dispose() {
        if (currentView != null) {
            currentView.dispose();
        }
        spriteBatch.dispose();
    }

    public View getCurrentView() {
        return currentView;
    }

    @FunctionalInterface
    public interface ViewSupplier {
        View create();
    }
}
