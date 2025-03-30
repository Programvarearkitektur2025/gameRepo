package io.github.progark.Client.Views.Game;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import io.github.progark.Client.Controllers.GameController;
import io.github.progark.Client.Views.View;
import io.github.progark.Main;
import io.github.progark.Server.Model.Game.GameModel;
import io.github.progark.Server.Service.GameService;

public class GameView extends View {
        private final GameController gameController;
        private final GameModel gameModel;
        //private final Stage stage;
        private final Skin skin;
        // private final Texture backgroundTexture;

        public GameView(GameController gameController){
            super(); // This calls view constructor for standard initialization of view.
            this.gameController = gameController;
            this.gameModel = new GameModel();
            // Initialize texture and skin here. This needs to be correct to planned UI
            // backgroundTexture = new Texture(Gdx.files.internal("game_background.png"));
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

            // if (backgroundTexture != null) {
            //     backgroundTexture.dispose();
            // }
            // if (skin != null) {
            //     skin.dispose();
            // }
            // Dispose of other resources
            super.dispose(); // Call parent's dispose to clean up stage and spriteBatch

        }

        public void showMessage(String displayMessage){
            // Logic for displaying error message
            // Method should end with an call to the render method to render new UI
        }
        public void updateScore (int score){
            // Logic for displaying score
            // Method should end with an call to the render method to render new UI
        }

        public void updateSubmittedAnswers(Map<String,Integer> solution){
            // Logic for updating submitted answers
            // Method should end with an call to the render method to render new UI

        }
        public void updateTimeRemaining(float time){
        // Logic for updating the time remaining
        // Method should end with an call to the render method to render new UI
        }

        public void showGameOver(){
        // Logic for displaying that the game is over.
        // Method should end with an call to the render method to render new UI
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
