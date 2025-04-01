package io.github.progark.Client.Views.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.Map;

import io.github.progark.Client.Controllers.LeaderboardController;
import io.github.progark.Client.Views.View;
import io.github.progark.Main;
import io.github.progark.Server.Model.Game.LeaderboardModel;

public class LeaderboardView extends View {
    private final Main game;
    private LeaderboardController controller;

    private Skin skin;
    private Texture buttonTexture;
    private Label scoreboardLabel;


    public LeaderboardView(Main game, LeaderboardController controller) {
        super();
        this.game = game;
        this.controller = controller;
    }

    @Override
    protected void initialize() {
        // Last inn skin og knapptekstur (erstatt "bigButton.png" med din knapptekstur)
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        buttonTexture = new Texture(Gdx.files.internal("bigButton.png"));

        // Rens skjermen med en fast bakgrunnsfarge (valgfritt)
        Gdx.gl.glClearColor(0, 0, 0, 1);

        // Sett opp et table for layout
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Lag en stor knapp med knappteksturen
        ImageButton leaderboardButton = new ImageButton(new TextureRegionDrawable(buttonTexture));
        leaderboardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Her kaller vi funksjoner i controlleren for testing – disse vil skrive ut til konsollen.
                System.out.println("Leaderboard-knapp trykket!");
                controller.loadLeaderboard();
                controller.incrementScoreFor("user123");
            }
        });

        // Legg knappen midtstilt i table
        table.center();
        table.add(leaderboardButton).expand().fill();
    }

    @Override
    public void update(float dt) {
        // Eventuell per-frame logikk (for øyeblikket tom)
    }

    @Override
    public void dispose() {
        super.dispose();
        if (skin != null) skin.dispose();
        if (buttonTexture != null) buttonTexture.dispose();
    }
    public void updateLeaderboard(LeaderboardModel model) {
        // eksempel:
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

}
