package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Pong;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = Pong.TITLE;
		config.width = Pong.DESKTOP_WIDTH;
		config.height = Pong.DESKTOP_HEIGHT;
		config.backgroundFPS = Pong.FPS;
		config.foregroundFPS = Pong.FPS;
		new LwjglApplication(new Pong(), config);
	}
}
