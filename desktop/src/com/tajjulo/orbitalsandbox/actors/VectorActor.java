package com.tajjulo.orbitalsandbox.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tajjulo.orbitalsandbox.game.PhysicsObject;
import com.tajjulo.orbitalsandbox.game.PhysicsSpace;

public class VectorActor extends Actor {
    private OrthographicCamera camera;
    private ShapeRenderer shape;
    private PhysicsSpace space;
    private boolean drawVelocity;
    private boolean drawAcceleration;
    private float width;
    private float vectorGirth = 20;
    private boolean vectorToggle;

    public VectorActor(PhysicsSpace space1) {
        shape = new ShapeRenderer();
        space = space1;
        width = 3f;
        drawVelocity = true;
        drawAcceleration = true;
        vectorToggle = false;
    }

    public VectorActor(PhysicsSpace space1, boolean drawVelocity, boolean drawAcceleration) {
        shape = new ShapeRenderer();
        space = space1;
        width = 3f;
        this.drawVelocity = drawVelocity;
        this.drawAcceleration = drawAcceleration;
        vectorToggle = false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(vectorToggle){
            camera = (OrthographicCamera) getStage().getCamera();
            vectorGirth = camera.zoom/2 + 10;
            if(drawVelocity)
                drawVelocityVectors(Color.SKY);
            if(drawAcceleration)
                drawAccelerationVectors(Color.GOLD, 1f);
        }
    }

    public void drawVelocityVectors(Color color){
        shape.setProjectionMatrix(getStage().getViewport().getCamera().combined);
        shape.begin(ShapeRenderer.ShapeType.Line);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shape.setColor(color);
        float squareToTriangleRatio = 0.8f;

        for (int i = 0; i < space.getSize(); i++) {

            float sqareLength = space.getObjectAtIndex(i).getVelocity().len() * squareToTriangleRatio;

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
            perpendicular1.mulAdd(perpendicular1, vectorGirth); //ob shaftu spodaj1
            perpendicular2.mulAdd(perpendicular2,vectorGirth); // ob shaftu spodaj2

            Vector2 awayPoint = new Vector2(space.getObjectAtIndex(i).getVelocity());

            float[] vertices = {100, 100, 200, 100, 150, 200};
            shape.polygon(vertices);
            shape.triangle(perpendicular1.x + posX, perpendicular1.y + posY,perpendicular2.x + posX, perpendicular2.y + posY, posX + vectorX, posY + vectorY);
        }
        shape.end();
    }

    private void drawAccelerationVectors(Color color, float scale) {
        shape.setProjectionMatrix(getStage().getViewport().getCamera().combined);
        shape.begin(ShapeRenderer.ShapeType.Line);
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

    public void toggleVectors(){
        vectorToggle = !vectorToggle;
    }
}