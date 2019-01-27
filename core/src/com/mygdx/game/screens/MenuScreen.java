package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.Pong;
import com.mygdx.game.managers.GameScreenManager;


public class MenuScreen extends AbstractScreen{

    OrthographicCamera cam;

    Texture ball = new Texture("Ball.png");
    Texture paddle = new Texture("PaddleWhite.png");

    Stage stage;
    TextButton button;
    TextButton.TextButtonStyle textButtonStyle;
    BitmapFont font1;
    BitmapFont font2;
    GlyphLayout title;
    GlyphLayout desc;
    String titleText;

    public MenuScreen(final Pong app) {
        super(app);

        this.cam = new OrthographicCamera();
        this.cam.setToOrtho(false, Pong.V_WIDTH, Pong.V_Height);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        font1 = new BitmapFont();;
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font1;
        textButtonStyle.fontColor = Color.BLACK;
        textButtonStyle.up = new TextureRegionDrawable(new TextureRegion(paddle));
        textButtonStyle.down = new TextureRegionDrawable(new TextureRegion(ball));
        button = new TextButton("PLAY", textButtonStyle);
        stage.addActor(button);
        addListener(button);

        button.setPosition(cam.viewportWidth / 2 - 50, cam.viewportHeight / 2 - 75);
        button.setSize(100, 50);

        font2 = new BitmapFont();
        font2.getData().setScale(5);
        title = new GlyphLayout();
        desc = new GlyphLayout();
        titleText = "PONG";
        desc.setText(font1, "First to 5 points wins");

    }

    @Override
    public void show() {
        title.setText(font2, titleText);
    }

    @Override
    public void update(float dt) {
        stage.act(dt);
        cam.update();
        app.batch.setProjectionMatrix(cam.combined);
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        stage.draw();
        app.batch.begin();
        font2.draw(app.batch, title, (cam.viewportWidth - title.width)/ 2, cam.viewportHeight - 145);
        font1.draw(app.batch, desc, (cam.viewportWidth - desc.width)/ 2, cam.viewportHeight - 400);
        app.batch.end();

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        super.dispose();
    }

    private void addListener(Actor btn) {
        btn.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                app.gsm.setScreen(GameScreenManager.STATE.PLAY);
            }
        });
    }

    public void setTitleText(String text) {
        titleText = text;
    }
}
