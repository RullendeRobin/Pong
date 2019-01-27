package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.managers.GameScreenManager;

public class Pong extends Game {

    //App vars
    public static String TITLE = "Pong";
    public static int DESKTOP_WIDTH = 720;
    public static int DESKTOP_HEIGHT = 420;
    public static int FPS = 60;

    //Game vars
    public static int V_WIDTH = 720;
    public static int V_Height = 420;

    //Managers
    public GameScreenManager gsm;
    public AssetManager assets;

    //Batches
    public SpriteBatch batch;
    public ShapeRenderer shapeBatch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeBatch = new ShapeRenderer();

        assets = new AssetManager();
        gsm = new GameScreenManager(this);
    }

    @Override
    public void render() {
        super.render();
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        shapeBatch.dispose();
        assets.dispose();
        gsm.dispose();
    }
}
