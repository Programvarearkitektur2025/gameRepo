package io.github.progark.Client.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import io.github.progark.Client.Controllers.LeaderboardController;
import io.github.progark.Server.Model.Game.LeaderboardModel;
import io.github.progark.Main;

import java.util.Map;

public class LeaderboardView extends View {
    private final Main game;

    // UI-ressurser
    private Skin skin;
    private Texture backgroundTexture;
    private Image background;
    private BitmapFont font;
    private LeaderboardController controller;



    private Label scoreboardLabel;

    public LeaderboardView(Main game) {
        super();
        this.game = game;

    }

    @Override
    protected void initialize() {

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        backgroundTexture = new Texture(Gdx.files.internal("Background_1.png"));
        font = new BitmapFont(Gdx.files.internal("fonts/small.fnt"));


        background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);


        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;
        style.fontColor = Color.WHITE;

        scoreboardLabel = new Label("Loading leaderboard...", style);


        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.center();
        table.add(scoreboardLabel).pad(20);
    }


    public void updateLeaderboard(LeaderboardModel model) {
        if (model == null || model.getUserScore() == null) {
            scoreboardLabel.setText("No data");
            return;
        }

        StringBuilder sb = new StringBuilder("LEADERBOARD\n\n");
        for (Map.Entry<String, Integer> entry : model.getUserScore().entrySet()) {
            String userId = entry.getKey();
            int score = entry.getValue();

            sb.append("User: ").append(userId).append(" | Score: ").append(score).append("\n");
        }
        scoreboardLabel.setText(sb.toString());
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void dispose() {
        super.dispose();
        if (skin != null) skin.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (font != null) font.dispose();
    }
}
