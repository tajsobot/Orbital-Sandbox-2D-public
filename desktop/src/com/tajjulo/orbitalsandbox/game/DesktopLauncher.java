package com.tajjulo.orbitalsandbox.game;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.tajjulo.orbitalsandbox.OrbitalSandbox;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setIdleFPS(60);
		config.setForegroundFPS(144);
		config.setTitle("Orbital Sandbox");
		config.useVsync(true);
		config.setWindowSizeLimits(1024,768, 5000,5000);
		new Lwjgl3Application(new OrbitalSandbox(), config);
	}
}