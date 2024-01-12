package com.tajjulo.orbitalsandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen extends ScreenAdapter {
    OrbitalSandbox game;
    int state;
    public GameScreen (OrbitalSandbox game) {
        this.game = game;
    }

    @Override
    public void render (float delta) {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0, 1, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void pause () {

    }
}
