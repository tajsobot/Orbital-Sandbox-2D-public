package com.tajjulo.orbitalsandbox;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.security.Key;
import java.sql.Time;

public class Game extends ApplicationAdapter {

	private OrthographicCamera camera;
	private Texture buttonTexture;
	private ShapeRenderer shape;
	private SpriteBatch batch;
	private ExtendViewport viewport;

	private float deltaTime;

	
	@Override
	public void create () {
		shape = new ShapeRenderer();
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(500, 500, camera);
		viewport.getCamera().position.set(0,0,0);
	}

	@Override
	public void render () {
		deltaTime = Gdx.graphics.getDeltaTime();
		ScreenUtils.clear(0.5f,0.5f, 0.5f, 1 );
		viewport.apply();

		doInputsCamera();
		renderGrid();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		shape.dispose();
	}
	public void resize(int width, int height) {
		viewport.update(width, height);
	}


	//other methods

	//tiled na 100 * 100 (100m * 100m box) na 4 smeri
	public void renderGrid() {
		shape.setProjectionMatrix(viewport.getCamera().combined);
		shape.begin(ShapeRenderer.ShapeType.Line);
		int sqCount = 100;
		int sqHeight = 100;
		int sqWidth = 100;

		//desno gori
		shape.setColor(Color.WHITE);
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100; j++) {
				shape.rect(i * sqWidth, j * sqHeight, sqWidth, sqHeight);
			}
		}
		//levo gori
		shape.setColor(Color.GREEN);
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100; j++) {
				shape.rect(i * sqWidth - sqCount * sqWidth, j * sqHeight, sqWidth, sqHeight);
			}
		}
		//levo doli
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100; j++) {
				shape.setColor(Color.BLUE);
				shape.rect(i * sqWidth - sqCount * sqWidth, j * sqHeight - sqCount * sqHeight, sqWidth, sqHeight);
			}
		}
		//desmo doli
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100; j++) {
				shape.setColor(Color.ORANGE);
				shape.rect(i * sqWidth, j * sqHeight - sqCount * sqHeight, sqWidth, sqHeight);
			}
		}
		//naredi linje
		shape.setColor(Color.RED);
		shape.line(-sqWidth * sqCount + 1.0f,0,sqWidth * sqCount * 1.0f,0);
		shape.line(0, -sqHeight * sqCount,0, sqHeight * sqCount);

		shape.end();
	}

	public void doInputsCamera(){
		int cameraSpeed = 200;
		int cameraZoomSpeed = 1;

		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			camera.position.x += -cameraSpeed * deltaTime * camera.zoom;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)){
			camera.position.x += cameraSpeed * deltaTime * camera.zoom;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			camera.position.y += cameraSpeed * deltaTime * camera.zoom;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)){
			camera.position.y += -cameraSpeed * deltaTime * camera.zoom;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
			camera.position.setZero();
			camera.zoom = 1;
			System.out.printf("reset");
		}

		if(Gdx.input.isKeyPressed(Input.Keys.Q)){
			camera.zoom += cameraZoomSpeed * deltaTime * camera.zoom;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.E)){
			camera.zoom += -cameraZoomSpeed * deltaTime * camera.zoom;

		}


	}
}
