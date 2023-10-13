package com.tajjulo.orbitalsandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class UiActor extends Actor {
    ShapeRenderer shape;
    public UiActor() {
        shape = new ShapeRenderer();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setProjectionMatrix(getStage().getViewport().getCamera().combined);

        shape.setColor(Color.GOLD);
        shape.rect(0,10, 100,50);
        shape.end();
    }
}