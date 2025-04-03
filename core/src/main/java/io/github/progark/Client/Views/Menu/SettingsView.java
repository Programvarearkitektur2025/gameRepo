package io.github.progark.Client.Views.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import io.github.progark.Client.Controllers.SettingsController;
import io.github.progark.Client.Views.Components.NavBar;
import io.github.progark.Client.Views.View;

public class SettingsView extends View {

    private Texture backgroundTexture;
    private Texture settingsHeaderTexture;

    private Texture jeopardyOn, jeopardyOff;
    private Texture edSheeranOn, edSheeranOff;
    private Texture noMusicOn, noMusicOff;
    private Texture fixYouOn, fixYouOff;
    private Texture giveYouUpOn, giveYouUpOff;

    private ImageButton jeopardyBtn, edSheeranBtn, noMusicBtn, fixYouBtn, giveYouUpBtn;

    private NavBar navBar;
    private final SettingsController controller;

    public SettingsView(SettingsController controller) {
        super();
        this.controller = controller;
        backgroundTexture = new Texture(Gdx.files.internal("Background2.png"));
        settingsHeaderTexture = new Texture(Gdx.files.internal("SettingsHeader.png"));
        loadAssets();
        enter();
    }

    private void loadAssets() {
        jeopardyOn = new Texture("JeopardyOn.png");
        jeopardyOff = new Texture("JeopardyOff.png");
        edSheeranOn = new Texture("EdSheeranOn.png");
        edSheeranOff = new Texture("EdSheeranOff.png");
        noMusicOn = new Texture("NoMusicOn.png");
        noMusicOff = new Texture("NoMusicOff.png");

        fixYouOn = new Texture("FixYouOn.png");
        fixYouOff = new Texture("FixYouOff.png");
        giveYouUpOn = new Texture("GiveYouUpOn.png");
        giveYouUpOff = new Texture("GiveYouUpOff.png");
    }

    @Override
    protected void initialize() {
        Image bg = new Image(backgroundTexture);
        bg.setFillParent(true);
        stage.addActor(bg);

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(100);
        stage.addActor(table);

        // Add Header
        Image header = new Image(settingsHeaderTexture);
        table.add(header).padBottom(200).width(900).height(245).row();

        // Buttons (initial states)
        jeopardyBtn = createButton(jeopardyOff, "jeopardy", "Jeopardy.mp3");
        edSheeranBtn = createButton(edSheeranOn, "ed", "ThinkingOutLoud.mp3"); // default on
        fixYouBtn = createButton(fixYouOff, "fixyou", "FixYou.mp3");
        giveYouUpBtn = createButton(giveYouUpOff, "giveyouup", "GiveYouUp.mp3");
        noMusicBtn = createButton(noMusicOff, "none", null);

        // Layout all buttons
        table.add(jeopardyBtn).pad(30).width(500).height(160).row();
        table.add(edSheeranBtn).pad(30).width(500).height(160).row();
        table.add(fixYouBtn).pad(30).width(500).height(160).row();
        table.add(giveYouUpBtn).pad(30).width(500).height(160).row();
        table.add(noMusicBtn).pad(30).width(500).height(160).row();

        // Default music if none playing
        if (!controller.getMain().getMusicManager().isPlaying()) {
            controller.getMain().getMusicManager().play("ThinkingOutLoud.mp3");
        }

        navBar = new NavBar(stage, controller.getMain());
        String currentTrack = controller.getMain().getMusicManager().getCurrentTrack();

        if (currentTrack == null || currentTrack.isEmpty()) {
            updateButtons("none");
        } else if (currentTrack.contains("Jeopardy")) {
            updateButtons("jeopardy");
        } else if (currentTrack.contains("ThinkingOutLoud")) {
            updateButtons("ed");
        } else if (currentTrack.contains("FixYou")) {
            updateButtons("fixyou");
        } else if (currentTrack.contains("GiveYouUp")) {
            updateButtons("giveyouup");
        }

    }

    private ImageButton createButton(Texture offTexture, String key, String file) {
        ImageButton button = new ImageButton(new TextureRegionDrawable(new TextureRegion(offTexture)));
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (file != null) {
                    controller.getMain().getMusicManager().play(file);
                } else {
                    controller.getMain().getMusicManager().stop();
                }
                updateButtons(key);
            }
        });
        return button;
    }

    private void updateButtons(String active) {
        // Jeopardy
        jeopardyBtn.getStyle().imageUp = new TextureRegionDrawable(
            new TextureRegion(active.equals("jeopardy") ? jeopardyOn : jeopardyOff)
        );

        // Ed Sheeran
        edSheeranBtn.getStyle().imageUp = new TextureRegionDrawable(
            new TextureRegion(active.equals("ed") ? edSheeranOn : edSheeranOff)
        );

        // Fix You
        fixYouBtn.getStyle().imageUp = new TextureRegionDrawable(
            new TextureRegion(active.equals("fixyou") ? fixYouOn : fixYouOff)
        );

        // Give You Up
        giveYouUpBtn.getStyle().imageUp = new TextureRegionDrawable(
            new TextureRegion(active.equals("giveyouup") ? giveYouUpOn : giveYouUpOff)
        );

        // No Music
        noMusicBtn.getStyle().imageUp = new TextureRegionDrawable(
            new TextureRegion(active.equals("none") ? noMusicOn : noMusicOff)
        );
    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundTexture.dispose();
        settingsHeaderTexture.dispose();

        jeopardyOn.dispose(); jeopardyOff.dispose();
        edSheeranOn.dispose(); edSheeranOff.dispose();
        fixYouOn.dispose(); fixYouOff.dispose();
        giveYouUpOn.dispose(); giveYouUpOff.dispose();
        noMusicOn.dispose(); noMusicOff.dispose();

        navBar.dispose();
    }
}
