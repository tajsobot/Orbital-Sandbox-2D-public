package com.tajjulo.orbitalsandbox;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class OrbitalSandbox extends Game {
    @Override
    public void create () {
        setScreen(new com.tajjulo.orbitalsandbox.game.Game(this));
        setScreen(new MainMenuScreen(this));

    }

    @Override
    public void render() {
        super.render();
    }
}