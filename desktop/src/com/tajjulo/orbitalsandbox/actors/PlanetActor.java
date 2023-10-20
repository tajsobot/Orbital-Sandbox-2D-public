package com.tajjulo.orbitalsandbox.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tajjulo.orbitalsandbox.game.PhysicsObject;
import com.tajjulo.orbitalsandbox.game.PhysicsSpace;

public class PlanetActor extends Actor {
    ShapeRenderer shape;
    PhysicsSpace space;
    public PlanetActor(PhysicsSpace space1) {
        shape = new ShapeRenderer();
        space = space1;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //izrise planete
        shape.setProjectionMatrix(getStage().getViewport().getCamera().combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.BLACK);

        for (int i = 0; i < space.getSize(); i++) {

            PhysicsObject targetObject = space.getObjectAtIndex(i);
            shape.circle(space.getObjectAtIndex(i).getPosX(), space.getObjectAtIndex(i).getPosY(), 50);
        }
        shape.end();

        //izrise trace
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.DARK_GRAY);
        for (int i = 0; i < space.getSize(); i++) {
            for (int j = 0; j < space.getObjectAtIndex(i).getTraces().size(); j++) {
                float x = space.getObjectAtIndex(i).getTraces().get(j).x;
                float y = space.getObjectAtIndex(i).getTraces().get(j).y;
                shape.circle(x,y,10);
            }
        }
        shape.end();
    }
}
