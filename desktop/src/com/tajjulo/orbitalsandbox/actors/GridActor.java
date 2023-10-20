package com.tajjulo.orbitalsandbox.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GridActor extends Actor {

    ShapeRenderer shape;
    public GridActor() {
        shape = new ShapeRenderer();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        renderGrid();
    }
    public void renderGrid(){
        shape.setProjectionMatrix(getStage().getViewport().getCamera().combined);
        shape.begin(ShapeRenderer.ShapeType.Line);
        int sqCount = 100;
        int sqHeight = 100;
        int sqWidth = 100;

        //desno gori
        shape.setColor(Color.WHITE);
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                shape.rect(i * sqWidth, j * sqHeight, sqWidth, sqHeight);
            }
        }
        //levo gori
        shape.setColor(Color.GREEN);
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                shape.rect(i * sqWidth - sqCount * sqWidth, j * sqHeight, sqWidth, sqHeight);
            }
        }
        //levo doli
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                shape.setColor(Color.BLUE);
                shape.rect(i * sqWidth - sqCount * sqWidth, j * sqHeight - sqCount * sqHeight, sqWidth, sqHeight);
            }
        }
        //desmo doli
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                shape.setColor(Color.ORANGE);
                shape.rect(i * sqWidth, j * sqHeight - sqCount * sqHeight, sqWidth, sqHeight);
            }
        }
        //naredi linje
        shape.setColor(Color.RED);
        shape.line(-sqWidth * sqCount + 1.0f,0,sqWidth * sqCount * 1.0f,0);
        shape.line(0, -sqHeight * sqCount,0, sqHeight * sqCount);

        shape.end();
    }
}
