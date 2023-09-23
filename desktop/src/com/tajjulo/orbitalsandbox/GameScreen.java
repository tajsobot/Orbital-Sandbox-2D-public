package com.tajjulo.orbitalsandbox;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class GameScreen extends ScreenAdapter {

    private OrthographicCamera camera;
    private SpriteBatch batch;

    public GameScreen(OrthographicCamera camera){
        this.camera = camera;
    }

}
