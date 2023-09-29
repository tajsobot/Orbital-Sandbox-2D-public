package com.tajjulo.orbitalsandbox;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.tajjulo.orbitalsandbox.Game;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setIdleFPS(60);
		config.setForegroundFPS(200);
		config.setTitle("Orbital Sandbox");
		config.useVsync(false);
		new Lwjgl3Application(new Game(), config);
	}
}
