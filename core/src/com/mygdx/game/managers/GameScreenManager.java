package com.mygdx.game.managers;

import com.mygdx.game.Pong;
import com.mygdx.game.screens.AbstractScreen;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.MenuScreen;

import java.util.HashMap;

public class GameScreenManager {

    private final Pong app;
    private HashMap<STATE, AbstractScreen> gameScreens;

    private String menuText = "PONG";

    public enum STATE {
        MAIN_MENU,
        PLAY,
        SETTINGS
    }

    public GameScreenManager(final Pong app) {
        this.app = app;
//        menuText = "PONG";

        initGameScreens();
        setScreen(STATE.MAIN_MENU);
        System.out.println(gameScreens);
    }

    private void initGameScreens() {
        this.gameScreens = new HashMap<STATE, AbstractScreen>();
        this.gameScreens.put(STATE.MAIN_MENU, new MenuScreen(app));
        this.gameScreens.put(STATE.PLAY, new GameScreen(app));
    }

    public void setScreen(STATE nextScreen) {
        app.setScreen(gameScreens.get(nextScreen));
    }

    public void dispose() {
        for(AbstractScreen screen : gameScreens.values()) {
            if(screen != null) {
                screen.dispose();
            }
        }
    }

    public MenuScreen getScreen() {

        return (MenuScreen) gameScreens.get(STATE.MAIN_MENU);
    }
}
