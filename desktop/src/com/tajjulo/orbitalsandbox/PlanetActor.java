package com.tajjulo.orbitalsandbox;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlanetActor extends Actor {
    ShapeRenderer shape;
    public PlanetActor() {
        shape = new ShapeRenderer();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.BLACK);

        for (int i = 0; i < space.getSize(); i++) {

            PhysicsObject targetObject = space.getObjectAtIndex(i);
            shape.circle(space.getObjectAtIndex(i).getPosX(), space.getObjectAtIndex(i).getPosY(), 50);
        }
        shape.end();
    }
}
