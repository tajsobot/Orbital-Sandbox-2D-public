package com.tajjulo.orbitalsandbox.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tajjulo.orbitalsandbox.game.PhysicsObject;
import com.tajjulo.orbitalsandbox.game.PhysicsSpace;

public class PlanetActor extends Actor {
    ShapeRenderer shape;
    PhysicsSpace space;
    OrthographicCamera camera;
    Color shadeColor;


    public PlanetActor(PhysicsSpace space1) {
        shape = new ShapeRenderer();
        space = space1;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        camera = (OrthographicCamera) getStage().getCamera();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        drawTraces();
        drawPlanets();
        drawPlanets();
        drawShadedPlanets();


    }

    public void drawTraces(){
        //izrise trace
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.DARK_GRAY);
        for (int i = 0; i < space.getSize(); i++) {
            for (int j = 0; j < space.getObjectAtIndex(i).getTraces().size(); j++) {
                float x = space.getObjectAtIndex(i).getTraces().get(j).x;
                float y = space.getObjectAtIndex(i).getTraces().get(j).y;
                shape.circle(x,y,5 );
            }
        }
        shape.end();
    }

    public void drawPlanets(){
        //izrise planete
        shape.setProjectionMatrix(getStage().getViewport().getCamera().combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.BLACK);

        for (int i = 0; i < space.getSize(); i++) {
            PhysicsObject targetObject = space.getObjectAtIndex(i);
            shape.circle(space.getObjectAtIndex(i).getPosX(), space.getObjectAtIndex(i).getPosY(), space.getObjectAtIndex(i).getPlanetRadius());
        }
        shape.end();
    }
    public void drawShadedPlanets(){

        shape.setProjectionMatrix(getStage().getViewport().getCamera().combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shadeColor = new Color(Color.BLACK);
        shadeColor.set(shadeColor.r, shadeColor.g,shadeColor.b, 0.2f);
        shape.setColor(shadeColor);

        for (int i = 0; i < space.getSize(); i++) {
            PhysicsObject targetObject = space.getObjectAtIndex(i);
            shape.circle(space.getObjectAtIndex(i).getPosX(), space.getObjectAtIndex(i).getPosY(), camera.zoom * 5);
        }
        shape.end();
    }

    public void setShadeColor(Color color, float shading) {
        shadeColor.set(color.r, color.g,color.b, shading);
    }
}
