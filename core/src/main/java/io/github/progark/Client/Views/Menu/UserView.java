package io.github.progark.Client.Views.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import io.github.progark.Client.Controllers.UserController;
import io.github.progark.Client.Views.View;
import io.github.progark.Client.Views.Components.NavBar;
import io.github.progark.Server.Model.Login.UserModel;
/*
 * UserView.java
 * This class is responsible for displaying the user view in the application.
 * It handles the rendering of the user screen, including the background, profile icon, and logout button.
 * It also manages user interactions with the logout button.
 * The view interacts with the UserController to perform operations related to user management.
 */
public class UserView extends View {

    private final Texture backgroundTexture;
    private final Texture logoutButtonTexture;
    private final Texture personTexture;

    private final UserController controller;

    private NavBar navBar;

    private final BitmapFont usernameFont;
    private final BitmapFont emailFont;

    public UserView(UserController controller) {
        super();
        this.controller = controller;


        backgroundTexture = new Texture(Gdx.files.internal("Background2.png"));
        logoutButtonTexture = new Texture(Gdx.files.internal("logout.png"));
        personTexture = new Texture(Gdx.files.internal("Person.png"));

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter usernameParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
        usernameParams.size = 60;
        usernameFont = gen.generateFont(usernameParams);

        FreeTypeFontGenerator.FreeTypeFontParameter emailParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
        emailParams.size = 45;
        emailFont = gen.generateFont(emailParams);
        gen.dispose();

        enter();
    }

    @Override
    protected void initialize() {
        Image bg = new Image(backgroundTexture);
        bg.setFillParent(true);
        stage.addActor(bg);

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(200);
        stage.addActor(table);

        Image profileIcon = new Image(personTexture);
        table.add(profileIcon).size(400).padTop(150).row();


        ImageButton logoutBtn = new ImageButton(new TextureRegionDrawable(new TextureRegion(logoutButtonTexture)));
        logoutBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.logout();
            }
        });
        table.add(logoutBtn).width(600).height(260).padTop(400).row();

        navBar = new NavBar(stage, controller.getMain());
    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundTexture.dispose();
        logoutButtonTexture.dispose();
        personTexture.dispose();
        navBar.dispose();
        usernameFont.dispose();
        emailFont.dispose();
    }
}
