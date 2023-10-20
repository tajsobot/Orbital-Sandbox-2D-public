package com.tajjulo.orbitalsandbox.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.tajjulo.orbitalsandbox.actors.GridActor;
import com.tajjulo.orbitalsandbox.actors.PlanetActor;
import com.tajjulo.orbitalsandbox.ui.UiLeft;

public class Game extends ApplicationAdapter {

	private ExtendViewport viewport;
	private OrthographicCamera camera;
	private ShapeRenderer shape;

	private float deltaTime;
	private float accumulator = 0f;
	private float fixedTimeStep = 1f / 500f; // 500 updates per second

	private PhysicsSpace space;
	private PhysicsObject object1;
	private PhysicsObject object2;
	private PhysicsObject object3;
	private PhysicsObject object4;

	private Stage spaceStage;
	private Stage uiStage;

	private GridActor gridActor;
	private PlanetActor planetActor;

	private UiLeft uiLeft;

	@Override
	public void create () {

		camera = new OrthographicCamera();
		viewport = new ExtendViewport(800, 500, camera);
		viewport.getCamera().position.set(0,0,0);

		shape = new ShapeRenderer();

		space = new PhysicsSpace();
		object1 = new PhysicsObject(0,0,100000, new Vector2(0,0),true);
		object2 = new PhysicsObject(500,0,1000, new Vector2(0,-400), false);
		object3 = new PhysicsObject(1000,0,1000, new Vector2(0,-200), false);

		space.addObject(object1);
		space.addObject(object2);
		space.addObject(object3);

		gridActor = new GridActor();
		planetActor = new PlanetActor(space);

		spaceStage = new Stage(viewport);
		spaceStage.addActor(gridActor);
		spaceStage.addActor(planetActor);
	}

	@Override
	//main loop
	public void render () {
		//inputs
		doInputsCamera();
		doInputsSimulation();
		//draw
		ScreenUtils.clear(0.5f,0.5f, 0.5f, 1 );
		spaceStage.draw();
		//physics
		accumulator += Gdx.graphics.getDeltaTime();
		while (accumulator >= fixedTimeStep) {
			// Update your physics here
			doPhysics(fixedTimeStep);
			accumulator -= fixedTimeStep;
		}
	}

	@Override
	public void dispose () {
		shape.dispose();
	}

	public void resize(int width, int height) {
		viewport.update(width, height);
		camera.position.setZero();
	}

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
			shape.circle(space.getObjectAtIndex(i).getPosX(), space.getObjectAtIndex(i).getPosY(), 50);
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
				shape.circle(x,y,10);
			}
		}
		shape.end();
	}

	public void doPhysics(float fixedDeltaTime){
		space.updateAll(fixedDeltaTime);
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
		// 1 - 10 number inputs
		for (int i = 0; i < space.getSize(); i++) {
			if(Gdx.input.isKeyPressed(8 + i)) { // num1 je int 8
				PhysicsObject targetObject = space.getObjectAtIndex(i);
				camera.position.set(targetObject.getPosX(), targetObject.getPosY(), 0);
			}
		}

	}

	private int timeScaleSaved = 1;
	public void doInputsSimulation(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_ADD)){
			space.addTimeScale(1);
			System.out.println("Time scale: " + space.getTimeScale());
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_SUBTRACT)){
			if(space.getTimeScale() > 1){
				space.addTimeScale(-1);
				System.out.println("Time scale: " + space.getTimeScale());
			}
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_ENTER)){
			if(space.getTimeScale() == 0){
				space.setTimeScale(timeScaleSaved);
				System.out.println("unpaused");
			}
			else{
				timeScaleSaved = space.getTimeScale();
				space.setTimeScale(0);
				System.out.println("paused");
			}
		}
	}
}