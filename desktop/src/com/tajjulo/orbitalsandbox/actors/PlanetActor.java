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

import java.util.List;

public class PlanetActor extends Actor {
    ShapeRenderer shape;
    PhysicsSpace space;
    OrthographicCamera camera;
    Color shadeColor;
    int clickPlanetIndex;

    public PlanetActor(PhysicsSpace space1) {
        shape = new ShapeRenderer();
        space = space1;
        clickPlanetIndex = -1;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        camera = (OrthographicCamera) getStage().getCamera();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        if(space.getSize() != 0){
            drawPlanets();
            drawShadedPlanets();
            drawOutline();
            drawTraces();
        }
    }

    public void drawTraces(){
        //izrise trace
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shadeColor = new Color(Color.BLACK);
        shadeColor.set(shadeColor.r, shadeColor.g,shadeColor.b, 0.4f);
        shape.setColor(shadeColor);
        float prevX = 0;
        float prevY = 0;
        float x = 0;
        float y = 0;

        for (int i = 0; i < space.getSize(); i++) {
            List<Vector2> traces = space.getObjectAtIndex(i).getTraces();

            for (int j = 0; j < traces.size(); j++) {
                x = traces.get(j).x;
                y = traces.get(j).y;
                if (j > 0) {
                    shape.rectLine(x, y, prevX, prevY, camera.zoom);
                }
                prevY = y;
                prevX = x;
            }
            prevY = 0;
            prevX = 0;
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

    public void drawOutline(){
        if(clickPlanetIndex >= 0){
            shape.setProjectionMatrix(getStage().getViewport().getCamera().combined);
            shape.begin(ShapeRenderer.ShapeType.Line);
            shape.setColor(Color.WHITE);
            float x = space.getObjectAtIndex(clickPlanetIndex).getPosX();
            float y = space.getObjectAtIndex(clickPlanetIndex).getPosY();
            float radius = space.getObjectAtIndex(clickPlanetIndex).getPlanetRadius();

            shape.circle(x,y,Math.max(radius, camera.zoom * 5));
            shape.end();
        }
    }

    public void setPlanetClicked(int index){
        clickPlanetIndex = index;
    }

    public void setShadeColor(Color color, float shading) {
        shadeColor.set(color.r, color.g,color.b, shading);
    }
}
