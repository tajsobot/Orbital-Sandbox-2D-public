package com.tajjulo.orbitalsandbox.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.tajjulo.orbitalsandbox.actors.GridActor;
import com.tajjulo.orbitalsandbox.actors.PlanetActor;
import com.tajjulo.orbitalsandbox.actors.VectorActor;
import com.tajjulo.orbitalsandbox.ui.UiCenter;

import java.util.Random;

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

	private PhysicsObject object5;

	private Stage spaceStage;
	private Stage uiStage;

	private GridActor gridActor;
	private PlanetActor planetActor;
	private VectorActor vectorActor;

	private UiCenter uiCenter;

	@Override
	public void create () {

		camera = new OrthographicCamera();
		viewport = new ExtendViewport(500, 500, camera);
		viewport.getCamera().position.set(0,0,0);
		camera.zoom = 10;

		shape = new ShapeRenderer();

		space = new PhysicsSpace();
		object1 = new PhysicsObject(0,0,10000000, new Vector2(0,0),false,10);
		object2 = new PhysicsObject(5000,0,1000, new Vector2(0,1400), false,10);
		object3 = new PhysicsObject(9000,0,1000, new Vector2(0,1000), false,10);
		object4 = new PhysicsObject(12000,0,6000, new Vector2(0,700), false,10);
		object5 = new PhysicsObject(5000,5000,600000, new Vector2(-1500,500), false,10);

		space.addObject(object1);
		space.addObject(object2);
		space.addObject(object3);
		space.addObject(object4);
		space.addObject(object5);

		gridActor = new GridActor();
		planetActor = new PlanetActor(space);
		vectorActor = new VectorActor(space);

		spaceStage = new Stage(viewport);
		spaceStage.addActor(gridActor);
		spaceStage.addActor(planetActor);
		spaceStage.addActor(vectorActor);

		uiCenter = new UiCenter(space);
	}

	@Override
	//main loop
	public void render () {
		//inputs
		doInputsCamera();
		doInputsSimulation();
		doUiInputs();

		//draw
		ScreenUtils.clear(0.5f,0.5f, 0.5f, 1 );
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		spaceStage.draw();
		uiCenter.renderUi();

		//physics
		accumulator += Gdx.graphics.getDeltaTime();
		while (accumulator >= fixedTimeStep) {
			doPhysics(fixedTimeStep);
			accumulator -= fixedTimeStep;
		}
		viewport.apply();
	}

	@Override
	public void dispose () {
		shape.dispose();
	}

	public void resize(int width, int height) {
		viewport.update(width, height);
		camera.position.setZero();
		uiCenter.updatecamera(width, height);
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
			camera.zoom = 10;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.Q)){
			camera.zoom += cameraZoomSpeed * Gdx.graphics.getDeltaTime() * camera.zoom;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.E)){
			camera.zoom += -cameraZoomSpeed * Gdx.graphics.getDeltaTime() * camera.zoom;
		}
		// 1 - 10 number inputs
		for (int i = 0; i < Math.max(space.getSize(), 10) ; i++) {
			if(Gdx.input.isKeyPressed(8 + i)) { // num1 je int 8
				PhysicsObject targetObject = space.getObjectAtIndex(i);
				camera.position.set(targetObject.getPosX(), targetObject.getPosY(), 0);
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
			Gdx.app.exit();
		}
	}

	private int timeScaleSaved = 1;
	public void doInputsSimulation(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_ADD)){
			space.addTimeScale(1);
			uiCenter.setChangingText(Integer.toString(space.getTimeScale()) + "x");
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_SUBTRACT)){
			if(space.getTimeScale() > 1){
				space.addTimeScale(-1);
			}
			uiCenter.setChangingText(Integer.toString(space.getTimeScale()) + "x");
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_ENTER)){
			toggleTime();
		}
	}

	public void doUiInputs(){
		if(uiCenter.getButtonPressID().equals("buttonPause")){
			toggleTime();
		}
		if(uiCenter.getButtonPressID().equals("buttonVectors")){
			vectorActor.toggleVectors();
		}
		Random random = new Random();
		if(uiCenter.getButtonPressID().equals("planetAdder")){
			int randomNum1 = random.nextInt(10000) - 5000;
			int randomNum2 = random.nextInt(10000) - 5000;
			int randomNum3 = random.nextInt(10000);
			int randomNum4 = random.nextInt(3000) - 1500;
			int randomNum5 = random.nextInt(3000) - 1500;
			space.addObject(new PhysicsObject(randomNum1,randomNum2,randomNum3 * 40, new Vector2(randomNum4, randomNum5), false, 10));
		}
		if(uiCenter.getButtonPressID().equals("button1")){
			toggleTime();
		}
		uiCenter.setButtonPressID("");
	}

	public void toggleTime(){
		if(space.getTimeScale() == 0){
			space.setTimeScale(timeScaleSaved);
			uiCenter.setChangingText(Integer.toString(space.getTimeScale()) + "x");
		}
		else{
			timeScaleSaved = space.getTimeScale();
			space.setTimeScale(0);
			uiCenter.setChangingText("paused");
		}
	}
}