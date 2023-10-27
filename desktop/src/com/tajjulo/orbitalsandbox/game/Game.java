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
		object2 = new PhysicsObject(1500,0,1000, new Vector2(-120,200), false);
		object3 = new PhysicsObject(1400,-1100,1000, new Vector2(200,100), false);
		object4 = new PhysicsObject(1000,-149,6000, new Vector2(200,-100), false);


		space.addObject(object1);
		space.addObject(object2);
		space.addObject(object3);
		space.addObject(object4);


		gridActor = new GridActor();
		planetActor = new PlanetActor(space);

		spaceStage = new Stage(viewport);
		spaceStage.addActor(gridActor);
		spaceStage.addActor(planetActor);
		uiLeft = new UiLeft();
	}

	@Override
	//main loop
	public void render () {
		viewport.apply();
		//inputs
		doInputsCamera();
		doInputsSimulation();
		//draw
		ScreenUtils.clear(0.5f,0.5f, 0.5f, 1 );
		spaceStage.draw();

		uiLeft.renderUi();

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

	public void doPhysics(float fixedDeltaTime){
		space.updateAll(fixedDeltaTime);
	}

	public void doInputsCamera(){
		int cameraSpeed = 200;
		int cameraZoomSpeed = 1;
		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			camera.position.x += -cameraSpeed * Gdx.graphics.getDeltaTime() * camera.zoom;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)){
			camera.position.x += cameraSpeed * Gdx.graphics.getDeltaTime() * camera.zoom;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			camera.position.y += cameraSpeed * Gdx.graphics.getDeltaTime() * camera.zoom;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)){
			camera.position.y += -cameraSpeed * Gdx.graphics.getDeltaTime() * camera.zoom;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
			camera.position.setZero();
			camera.zoom = 1;
			System.out.printf("reset");
		}
		if(Gdx.input.isKeyPressed(Input.Keys.Q)){
			camera.zoom += cameraZoomSpeed * Gdx.graphics.getDeltaTime() * camera.zoom;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.E)){
			camera.zoom += -cameraZoomSpeed * Gdx.graphics.getDeltaTime() * camera.zoom;
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
			toggleTime();
		}
	}
	public void toggleTime(){
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