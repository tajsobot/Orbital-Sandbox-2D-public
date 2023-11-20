package com.tajjulo.orbitalsandbox.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        super.draw(batch, parentAlpha);
        renderGrid();
    }
    public void renderGrid(){
        shape.setProjectionMatrix(getStage().getViewport().getCamera().combined);
        shape.begin(ShapeRenderer.ShapeType.Line);
        int sqCount = 100;
        int sqHeight = 1000;
        int sqWidth = 1000;

        //desno gori
        Color shadeColor = new Color(Color.WHITE);
        shadeColor.set(shadeColor.r, shadeColor.g,shadeColor.b, 0.3f);
        shape.setColor(shadeColor);
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                shape.rect(i * sqWidth, j * sqHeight, sqWidth, sqHeight);
            }
        }
        //levo gori
        shadeColor = new Color(Color.GREEN);
        shadeColor.set(shadeColor.r, shadeColor.g,shadeColor.b, 0.3f);
        shape.setColor(shadeColor);
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                shape.rect(i * sqWidth - sqCount * sqWidth, j * sqHeight, sqWidth, sqHeight);
            }
        }
        shadeColor = new Color(Color.BLUE);
        shadeColor.set(shadeColor.r, shadeColor.g,shadeColor.b, 0.3f);
        shape.setColor(shadeColor);
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                shape.rect(i * sqWidth - sqCount * sqWidth, j * sqHeight - sqCount * sqHeight, sqWidth, sqHeight);
            }
        }
        //desmo doli
        shadeColor = new Color(Color.ORANGE);
        shadeColor.set(shadeColor.r, shadeColor.g,shadeColor.b, 0.3f);
        shape.setColor(shadeColor);
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                shape.rect(i * sqWidth, j * sqHeight - sqCount * sqHeight, sqWidth, sqHeight);
            }
        }
        //naredi linje
        shadeColor = new Color(Color.RED);
        shadeColor.set(shadeColor.r, shadeColor.g,shadeColor.b, 0.5f);
        shape.setColor(shadeColor);
        shape.line(-sqWidth * sqCount + 1.0f,0,sqWidth * sqCount * 1.0f,0);
        shape.line(0, -sqHeight * sqCount,0, sqHeight * sqCount);

        shape.end();
    }
}
