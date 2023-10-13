package com.tajjulo.orbitalsandbox;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlanetActor extends Actor {
    ShapeRenderer shape;
    PhysicsSpace space;
    public PlanetActor() {
        shape = new ShapeRenderer();
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
    public void setSpaceobject(PhysicsSpace space1){
        space = space1;
    }
}
