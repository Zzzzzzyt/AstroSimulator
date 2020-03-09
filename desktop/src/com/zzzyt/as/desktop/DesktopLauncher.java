package com.zzzyt.as.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.zzzyt.as.AstroSimulator;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.backgroundFPS=30;
		config.foregroundFPS=30;
		config.title="Astro Simulator v0.0.0";
		config.addIcon("aslogo128x.png", FileType.Internal);
		config.addIcon("aslogo32x.png", FileType.Internal);
		config.addIcon("aslogo16x.png", FileType.Internal);
		new LwjglApplication(new AstroSimulator(), config);
	}
}
