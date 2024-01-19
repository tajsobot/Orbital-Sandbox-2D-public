package com.tajjulo.orbitalsandbox.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.tajjulo.orbitalsandbox.OrbitalSandbox;
import com.tajjulo.orbitalsandbox.actors.GridActor;
import com.tajjulo.orbitalsandbox.actors.PlanetActor;
import com.tajjulo.orbitalsandbox.actors.VectorActor;
import com.tajjulo.orbitalsandbox.game.PhysicsObject;
import com.tajjulo.orbitalsandbox.game.PhysicsSpace;
import com.tajjulo.orbitalsandbox.tools.PlanetMap;
import com.tajjulo.orbitalsandbox.ui.UiCenter;

import java.util.Random;

//test
public class GameScreen extends ScreenAdapter implements InputProcessor {

	private OrbitalSandbox game;
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
	private int planetClickIndex;
	private String clickState;

	public GameScreen(OrbitalSandbox game) {
		this.game = game;

		camera = new OrthographicCamera();
		viewport = new ExtendViewport(500, 500, camera);
		viewport.getCamera().position.set(0,0,0);
		camera.zoom = 10;

		shape = new ShapeRenderer();
		space = new PhysicsSpace();

		//nastavi planete vun iz txt fila
		planetMap = new PlanetMap("input.txt", "output.txt");
		space = planetMap.convertTextToMap();

		planetMap.convertMapToText(space);

		gridActor = new GridActor();
		planetActor = new PlanetActor(space);
		vectorActor = new VectorActor(space);

		spaceStage = new Stage(viewport);
		spaceStage.addActor(gridActor);
		spaceStage.addActor(planetActor);
		spaceStage.addActor(vectorActor);

		uiCenter = new UiCenter(space);
		uiStage = uiCenter.getStage();
		planetClickIndex = -1;

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(uiStage);
		multiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(multiplexer);

		clickState = "select";
		rawCameraPosition = new Vector3(0,0,0);

	}

	public void render (float delta) {
		//inputs
		if(!uiCenter.isInputingNumbers()){
			doInputsCamera();
			doInputsSimulation();
		}
		doUiInputs();

		//draw
		ScreenUtils.clear(0.4f,0.4f, 0.4f, 1 );
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//physics
		accumulator += Gdx.graphics.getDeltaTime();
		// 500 updates per second
		float fixedTimeStep = 1f / 500f;
		while (accumulator >= fixedTimeStep) {
			int negTime = 1;
			if(space.getTimeScale() < 0){
				negTime = -1;
			}
			doPhysics(negTime * fixedTimeStep);
			accumulator -= fixedTimeStep;
		}
		viewport.apply();
		spaceStage.draw();
		uiStage.draw();
		uiCenter.doPlanetInfoLabels();

	}

	@Override
	public void dispose () {
		shape.dispose();
	}

	public void resize(int width, int height) {
		viewport.update(width, height);
		uiCenter.updateResize(width, height);
	}

	public void doPhysics(float fixedDeltaTime){
		space.updateAll(fixedDeltaTime);
	}

	Vector3 rawCameraPosition;
	public void doInputsCamera(){
		int cameraSpeed = 200;
		int cameraZoomSpeed = 1;

		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			rawCameraPosition.x += -cameraSpeed * Gdx.graphics.getDeltaTime() * camera.zoom;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)){
			rawCameraPosition.x += cameraSpeed * Gdx.graphics.getDeltaTime() * camera.zoom;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			rawCameraPosition.y += cameraSpeed * Gdx.graphics.getDeltaTime() * camera.zoom;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)){
			rawCameraPosition.y += -cameraSpeed * Gdx.graphics.getDeltaTime() * camera.zoom;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
			rawCameraPosition.setZero();
			camera.zoom = 100;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.Q)){
			camera.zoom += cameraZoomSpeed * Gdx.graphics.getDeltaTime() * camera.zoom;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.E)){
			camera.zoom += -cameraZoomSpeed * Gdx.graphics.getDeltaTime() * camera.zoom;
		}
		//camera smoothing
		camera.position.lerp(rawCameraPosition, 0.06f);

		// 1 - 10 number inputs
		for (int i = 0; i < Math.min(space.getSize(), 10) ; i++) {
			if(Gdx.input.isKeyPressed(8 + i)) { // num1 je int 8
				PhysicsObject targetObject = space.getObjectAtIndex(i);
				rawCameraPosition.set(targetObject.getPosX(), targetObject.getPosY(), 0);
				planetActor.setPlanetClicked(i);
				uiCenter.setPlanetClicked(i);
				planetClickIndex = i;
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
			Gdx.app.exit();
		}
	}

	private int timeScaleSaved = 1;
	public void doInputsSimulation(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_ADD)){
			if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
				increaseTime(10);
			}else increaseTime(1);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_SUBTRACT)){
			if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
				increaseTime(-10);
			}else increaseTime(-1);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_ENTER)){
			toggleTime();
		}
		if( Gdx.input.isKeyJustPressed(Input.Keys.FORWARD_DEL) ||  Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) ){
			if(planetClickIndex >= 0 && space.getSize() > 0){
				space.removeObject(planetClickIndex);
				planetClickIndex--;
				updatePlanetClickIndex();
			}
		}
	}

	public void doUiInputs(){
		uiCenter.updateInputs();
		if(uiCenter.getButtonPressID().equals("timeIncrease")){
			increaseTime(1);
		}
		if(uiCenter.getButtonPressID().equals("timeDecrease")){
			increaseTime(-1);
		}
		if(uiCenter.getButtonPressID().equals("buttonVectors")){
			vectorActor.toggleVectors();
		}
		if(uiCenter.getButtonPressID().equals("buttonPause")){
			toggleTime();
		}
		Random random = new Random();
		if(uiCenter.getButtonPressID().equals("planetRandomAdder")){
			int randomNum1 = random.nextInt(10000) - 5000;
			int randomNum2 = random.nextInt(10000) - 5000;
			int randomNum3 = random.nextInt(10000);
			int randomNum4 = random.nextInt(3000) - 1500;
			int randomNum5 = random.nextInt(3000) - 1500;
			space.addObject(new PhysicsObject(randomNum1,randomNum2,randomNum3 * 40, new Vector2(randomNum4, randomNum5), false, 0.1f));
		}
		if(uiCenter.getButtonPressID().equals("removeAll")){
			space.removeAllObjects();
		}
		if(uiCenter.getButtonPressID().equals("mainMenu")){
			game.setScreen(new MainMenuScreen(game));
		}


		if(uiCenter.getButtonPressID().equals("planetCustomAdder")){
			clickState = "adding";
			if(space.getTimeScale() > 0){
				toggleTime();
			}
			planetClickIndex = -1;
			updatePlanetClickIndex();
			uiCenter.setChangingText("Adding planet...");

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
	public void increaseTime(int a){
		space.addTimeScale(a);
		if(space.getTimeScale() == 0){
			uiCenter.setChangingText("paused");
		}else uiCenter.setChangingText(space.getTimeScale() + "x");
	}
	public void updatePlanetClickIndex(){
		planetActor.setPlanetClicked(planetClickIndex);
		uiCenter.setPlanetClicked(planetClickIndex);
	}
	@Override public boolean mouseMoved (int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}

	@Override public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		Vector3 clickPos = new Vector3(screenX, screenY, 0);
		camera.unproject(clickPos);

		if(clickState.equals("adding")){
			PhysicsObject p = new PhysicsObject((long)clickPos.x, (long)clickPos.y, 1, new Vector2(0,0), false, 0.1f);
			space.addObject(p);
			clickState = "select";
		}
		for (int i = 0; i < space.getSize(); i++) {
			float planetRadius = space.getObjectAtIndex(i).getPlanetRadius();
			float posX = space.getObjectAtIndex(i).getPosX();
			float posY = space.getObjectAtIndex(i).getPosY();

			if(Math.abs(clickPos.x - posX) < planetRadius && Math.abs(clickPos.y - posY) < planetRadius || Math.abs(clickPos.x - posX) < camera.zoom * 5 && Math.abs(clickPos.y - posY) < camera.zoom * 5){
				planetActor.setPlanetClicked(i);
				planetClickIndex = i;
				uiCenter.setPlanetClicked(i);
				uiCenter.doPlanetInfoLabels();
				break;
			}
			else{
				planetActor.setPlanetClicked(-1);
				uiCenter.setPlanetClicked(-1);
				planetClickIndex = -1;
			}

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