package com.unshape.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.unshape.game.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Main(), config);
		
		config.samples = 16;
		config.resizable = false;
		config.title 	= "Unshape";
		config.width 	= 480;
		config.height 	= 640;
	}
}
