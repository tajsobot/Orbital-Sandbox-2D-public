package com.tajjulo.orbitalsandbox;

import com.badlogic.gdx.Game;
import com.tajjulo.orbitalsandbox.screens.GameScreen;
import com.tajjulo.orbitalsandbox.screens.MainMenuScreen;

public class OrbitalSandbox extends Game {
    @Override
    public void create () {
        setScreen(new GameScreen(this));
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }
}