package com.zzzyt.as.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.zzzyt.as.AstroSimulator;
import com.zzzyt.as.desktop.input.DesktopHandler;
import com.zzzyt.as.desktop.input.DesktopProcessor;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.backgroundFPS=60;
		config.foregroundFPS=60;
		config.vSyncEnabled=false;
		config.title="Astro Simulator v0.0.0";
		config.addIcon("aslogo128x.png", FileType.Internal);
		config.addIcon("aslogo32x.png", FileType.Internal);
		config.addIcon("aslogo16x.png", FileType.Internal);
		AstroSimulator game=new AstroSimulator();
		DesktopHandler handler=new DesktopHandler();
		game.handler=handler;
		game.input.addProcessor(new DesktopProcessor());
		new LwjglApplication(game, config);
	}
}
