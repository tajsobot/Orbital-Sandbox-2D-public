package com.tajjulo.orbitalsandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.tajjulo.orbitalsandbox.game.Game;

import java.security.Key;

public class MainMenuScreen extends ScreenAdapter {
    OrbitalSandbox game;

    public MainMenuScreen (OrbitalSandbox game) {
        this.game = game;
    }

    public void update () {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            game.setScreen(new Game(game));
        }
    }

    public void draw () {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //TODO
    }

    @Override
    public void render (float delta) {
        update();
        draw();
    }

    @Override
    public void pause () {

    }
}