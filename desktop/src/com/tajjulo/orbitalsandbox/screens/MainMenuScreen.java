package com.tajjulo.orbitalsandbox.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.tajjulo.orbitalsandbox.OrbitalSandbox;
import jdk.internal.icu.impl.BMPSet;
import sun.java2d.pipe.ShapeSpanIterator;

import java.awt.*;

public class MainMenuScreen extends ScreenAdapter {
    OrbitalSandbox game;
    ShapeRenderer shape;
    Camera camera;

    public MainMenuScreen (OrbitalSandbox game) {
        this.game = game;
        shape = new ShapeRenderer();
        camera = new OrthographicCamera(500,500);

    }

    public void update () {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            game.setScreen(new GameScreen(game));
        }
    }

    public void draw () {

        int rectWidth = 200;
        Rectangle square1 = new Rectangle(-rectWidth/2,100,rectWidth,50);
        Rectangle square2 = new Rectangle(-rectWidth/2,0,rectWidth,50);
        Rectangle square3 = new Rectangle(-rectWidth/2,-100,rectWidth,50);


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shape.setProjectionMatrix(camera.combined);
        // Draw squares
        shape.begin(ShapeRenderer.ShapeType.Filled);

        shape.setColor(Color.RED);
        shape.rect(square1.x, square1.y, square1.width, square1.height);

        shape.setColor(Color.GREEN);
        shape.rect(square2.x, square2.y, square2.width, square2.height);

        shape.setColor(Color.BLUE);
        shape.rect(square3.x, square3.y, square3.width, square3.height);

        shape.end();
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if (square1.contains(touchPos.x, touchPos.y)) {
                game.setScreen(new GameScreen(game));
                System.out.println("Square 1 clicked!");
            }
            else if (square2.contains(touchPos.x, touchPos.y)) {
                // Action for square 2
                //todo
                System.out.println("Square 2 clicked!");
            }
            else if (square3.contains(touchPos.x, touchPos.y)) {
            // Action for square 3
            System.out.println("Square 3 clicked!");
            Gdx.app.exit();
            }
        }
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