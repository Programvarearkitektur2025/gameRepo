package io.github.progark.Client.Views.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import io.github.progark.Client.Views.View;
import io.github.progark.Main;

    public class GameView extends View {
        private final Main game;
        //private final Stage stage;
        private final Skin skin;
        private final Texture backgroundTexture;

        public GameView(Main game){
            // Initialize variables
            super(); // This calls view constructor for standard initialization of view.
            this.game = game;

            // Initialize texture and skin here. This needs to be correct to planned UI
            backgroundTexture = new Texture(Gdx.files.internal("game_background.png"));
            skin = new Skin(Gdx.files.internal("uiskin.json"));
        }
        @Override
        public void enter() {
            super.enter();
        }

        @Override
        protected void initialize() {

        }

        @Override
        public void dispose() {
            // Clean up resources
            if (backgroundTexture != null) {
                backgroundTexture.dispose();
            }
            if (skin != null) {
                skin.dispose();
            }
            // Dispose of other resources
            super.dispose(); // Call parent's dispose to clean up stage and spriteBatch

        }
        /*
        @Override
        public void render(SpriteBatch sb) {

        }
        @Override
        public void update(float delta) {
        }
        @Override
        public void handleInput() {
        }
         */

    }
