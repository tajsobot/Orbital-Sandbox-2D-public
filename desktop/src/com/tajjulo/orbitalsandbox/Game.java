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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Game extends ApplicationAdapter {

	private OrthographicCamera camera;
	private Texture buttonTexture;
	private ShapeRenderer shape;
	private SpriteBatch batch;
	private ExtendViewport viewport;

	private float deltaTime;
	private boolean justResized = false ;

	private PhysicsSpace space;
	private PhysicsObject object1;
	private PhysicsObject object2;
	private PhysicsObject object3;

	@Override
	public void create () {
		shape = new ShapeRenderer();
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(500, 500, camera);
		viewport.getCamera().position.set(0,0,0);

		space = new PhysicsSpace();
		object1 = new PhysicsObject(-100,0,100000, new Vector2(0,4));
		object2 = new PhysicsObject(100,0,100000, new Vector2(0,-4));
		object3 = new PhysicsObject(50,100,10, new Vector2(5,-5));
		space.addObject(object1);
		space.addObject(object2);
		space.addObject(object3);

	}

	@Override
	//main loop
	public void render () {
		deltaTime = Gdx.graphics.getDeltaTime();

		ScreenUtils.clear(0.5f,0.5f, 0.5f, 1 );
		viewport.apply();

		doInputsCamera();

		renderGrid();
		renderTraces();
		renderObjects();

		doPhysics();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		shape.dispose();
	}
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

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

	public void renderObjects() {
		shape.begin(ShapeRenderer.ShapeType.Filled);
		shape.setColor(Color.BLACK);

		for (int i = 0; i < space.getSize(); i++) {

			PhysicsObject targetObject = space.getObjectAtIndex(i);
			shape.circle(space.getObjectAtIndex(i).getPosX(), space.getObjectAtIndex(i).getPosY(), 10);
		}
		shape.end();
	}
	public void renderTraces(){
		shape.begin(ShapeRenderer.ShapeType.Filled);
		shape.setColor(Color.DARK_GRAY);
		for (int i = 0; i < space.getSize(); i++) {
			for (int j = 0; j < space.getObjectAtIndex(i).getTraces().size(); j++) {
				float x = space.getObjectAtIndex(i).getTraces().get(j).x;
				float y = space.getObjectAtIndex(i).getTraces().get(j).y;
				shape.circle(x,y,2);
			}
		}
		shape.end();
	}

	public void doPhysics(){
		space.updateAll(Gdx.graphics.getDeltaTime());
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
		//smartes shit ever
		for (int i = 0; i < space.getSize(); i++) {
			if(Gdx.input.isKeyPressed(8 + i)) { // num1 je int 8
				PhysicsObject targetObject = space.getObjectAtIndex(i);
				camera.position.set(targetObject.getPosX(), targetObject.getPosY(), 0);
			}
		}
	}
}
