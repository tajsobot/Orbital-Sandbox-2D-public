package com.tajjulo.orbitalsandbox.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tajjulo.orbitalsandbox.game.PhysicsObject;
import com.tajjulo.orbitalsandbox.game.PhysicsSpace;


public class VectorActor extends Actor {
    private ShapeRenderer shape;
    private PhysicsSpace space;

    private boolean drawVelocity;
    private boolean drawAcceleration;

    private float width;
    private float vectorGirth = 20;

    public VectorActor(PhysicsSpace space1) {
        shape = new ShapeRenderer();
        space = space1;
        width = 3f;
        drawVelocity = true;
        drawAcceleration = true;
    }

    public VectorActor(PhysicsSpace space1, boolean drawVelocity, boolean drawAcceleration) {
        shape = new ShapeRenderer();
        space = space1;
        width = 3f;
        this.drawVelocity = drawVelocity;
        this.drawAcceleration = drawAcceleration;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(drawVelocity)
            drawVelocityVectors(Color.SKY);
        if(drawAcceleration)
            drawAccelerationVectors(Color.GOLD, 1f);
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
            Vector2 perpendicular1 = new Vector2(vectorX, vectorY);
            Vector2 perpendicular2 = new Vector2(vectorX, vectorY);
            perpendicular1.rotateDeg(90);
            perpendicular2.rotateDeg(-90);
            perpendicular1.nor();
            perpendicular2.nor();
            perpendicular1.mulAdd(perpendicular1, vectorGirth);
            perpendicular2.mulAdd(perpendicular2,vectorGirth);
            shape.triangle(perpendicular1.x + posX, perpendicular1.y + posY,perpendicular2.x + posX, perpendicular2.y + posY, posX + vectorX, posY + vectorY);
        }
        shape.end();
    }

    private void drawAccelerationVectors(Color color, float scale) {
        shape.setProjectionMatrix(getStage().getViewport().getCamera().combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(color);

        for (int i = 0; i < space.getSize(); i++) {
            float posX = space.getObjectAtIndex(i).getPosX();
            float posY = space.getObjectAtIndex(i).getPosY();
            float vectorX = space.getObjectAtIndex(i).getAcceleration().x;
            float vectorY = space.getObjectAtIndex(i).getAcceleration().y;
            Vector2 perpendicular1 = new Vector2(vectorX, vectorY);
            Vector2 perpendicular2 = new Vector2(vectorX, vectorY);
            perpendicular1.rotateDeg(90);
            perpendicular2.rotateDeg(-90);
            perpendicular1.nor();
            perpendicular2.nor();
            perpendicular1.mulAdd(perpendicular1,vectorGirth);
            perpendicular2.mulAdd(perpendicular2,vectorGirth);
            shape.triangle(perpendicular1.x + posX, perpendicular1.y + posY,perpendicular2.x + posX, perpendicular2.y + posY, posX + vectorX, posY + vectorY);
        }
        shape.end();
    }

    public void setDrawAcceleration(boolean drawAcceleration) {
        this.drawAcceleration = drawAcceleration;
    }

    public void setDrawVelocity(boolean drawVelocity) {
        this.drawVelocity = drawVelocity;
    }
}
