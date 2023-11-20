package com.tajjulo.orbitalsandbox.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.tajjulo.orbitalsandbox.actors.GridActor;
import com.tajjulo.orbitalsandbox.actors.PlanetActor;
import com.tajjulo.orbitalsandbox.actors.VectorActor;
import com.tajjulo.orbitalsandbox.tools.PlanetMap;
import com.tajjulo.orbitalsandbox.ui.UiCenter;

import java.util.Random;

public class Game extends ApplicationAdapter implements InputProcessor {

	private ExtendViewport viewport;
	private OrthographicCamera camera;
	private ShapeRenderer shape;

	private float deltaTime;
	private float accumulator = 0f;

	private PhysicsSpace space;

	private Stage spaceStage;

	private GridActor gridActor;
	private PlanetActor planetActor;
	private VectorActor vectorActor;

	private UiCenter uiCenter;
	private Stage uiStage;

	PlanetMap planetMap;
	@Override
	public void create () {
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(500, 500, camera);
		viewport.getCamera().position.set(0,0,0);
		camera.zoom = 10;

		shape = new ShapeRenderer();
		space = new PhysicsSpace();

		//nastavi planete vun iz txt fila
		planetMap = new PlanetMap();
		try {
			space = planetMap.convertTextToMap();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		try {
			planetMap.convertMapToText(space);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		gridActor = new GridActor();
		planetActor = new PlanetActor(space);
		vectorActor = new VectorActor(space);

		spaceStage = new Stage(viewport);
		spaceStage.addActor(gridActor);
		spaceStage.addActor(planetActor);
		spaceStage.addActor(vectorActor);

		uiCenter = new UiCenter(space);

		uiStage = uiCenter.getStage();

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(uiStage);
		multiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(multiplexer);
	}
	@Override
	public void render () {
		//inputs
		doInputsCamera();
		doInputsSimulation();
		doUiInputs();

		//draw
		ScreenUtils.clear(0.4f,0.4f, 0.4f, 1 );
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		spaceStage.draw();
		uiStage.draw();

		//physics
		accumulator += Gdx.graphics.getDeltaTime();
		// 500 updates per second
		float fixedTimeStep = 1f / 500f;
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
			uiCenter.setChangingText(space.getTimeScale() + "x");
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_SUBTRACT)){
			if(space.getTimeScale() > 1){
				space.addTimeScale(-1);
			}else space.setTimeScale(1);
			uiCenter.setChangingText(space.getTimeScale() + "x");
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
			uiCenter.setChangingText(space.getTimeScale() + "x");
		}
		else{
			timeScaleSaved = space.getTimeScale();
			space.setTimeScale(0);
			uiCenter.setChangingText("paused");
		}
	}
	@Override public boolean mouseMoved (int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}

	@Override public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		for (int i = 0; i < space.getSize(); i++) {
			float planetRadius = space.getObjectAtIndex(i).getPlanetRadius();
			float posX = space.getObjectAtIndex(i).getPosX();
			float posY = space.getObjectAtIndex(i).getPosY();
			Vector3 clickPos = new Vector3(screenX, screenY, 0);
			camera.unproject(clickPos);

			if(Math.abs(clickPos.x - posX) < planetRadius && Math.abs(clickPos.y - posY) < planetRadius || Math.abs(clickPos.x - posX) < camera.zoom * 5 && Math.abs(clickPos.y - posY) < camera.zoom * 5){
				planetActor.setPlanetClicked(i);
				break;
			}
			else planetActor.setPlanetClicked(-1);

		}
		return true;
	}

	@Override public boolean touchDragged (int screenX, int screenY, int pointer) {
		return true;
	}

	@Override public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		return true;
	}

	@Override
	public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override public boolean keyDown (int keycode) {
		return false;
	}

	@Override public boolean keyUp (int keycode) {
		return false;
	}

	@Override public boolean keyTyped (char character) {
		return false;
	}

}