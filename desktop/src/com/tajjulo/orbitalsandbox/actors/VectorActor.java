package com.tajjulo.orbitalsandbox.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tajjulo.orbitalsandbox.game.PhysicsObject;
import com.tajjulo.orbitalsandbox.game.PhysicsSpace;


public class VectorActor extends Actor {
    private ShapeRenderer shape;
    private PhysicsSpace space;

    private Float width;
    public VectorActor(PhysicsSpace space1) {
        shape = new ShapeRenderer();
        space = space1;

        width = 3f;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        drawVelocityVectors(Color.SKY);
        drawForceVectors(Color.FIREBRICK, 1f/500f);
    }

    public void drawVelocityVectors(Color color){
        shape.setProjectionMatrix(getStage().getViewport().getCamera().combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(color);

        for (int i = 0; i < space.getSize(); i++) {
            float posX = space.getObjectAtIndex(i).getPosX();
            float posY = space.getObjectAtIndex(i).getPosY();
            float vectorX = space.getObjectAtIndex(i).getVelocity().x;
            float vectorY = space.getObjectAtIndex(i).getVelocity().y;

            shape.rectLine(posX, posY, posX + vectorX,  posY + vectorY, width);
        }
        shape.end();
    }

    private void drawForceVectors(Color color, float scale) {
        shape.setProjectionMatrix(getStage().getViewport().getCamera().combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(color);

        for (int i = 0; i < space.getSize(); i++) {
            float posX = space.getObjectAtIndex(i).getPosX();
            float posY = space.getObjectAtIndex(i).getPosY();
            float vectorX = space.getObjectAtIndex(i).getForceForDrawing().x;
            float vectorY = space.getObjectAtIndex(i).getForceForDrawing().y;

            shape.rectLine(posX, posY, posX + vectorX * scale,  posY + vectorY * scale, width);
        }
        shape.end();
    }
}
