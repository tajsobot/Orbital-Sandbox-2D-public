package com.tajjulo.orbitalsandbox.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.*;
import com.tajjulo.orbitalsandbox.game.PhysicsObject;
import com.tajjulo.orbitalsandbox.game.PhysicsSpace;

public class UiCenter {
    Stage stage;

    TextButton button;
    TextButton.TextButtonStyle textButtonStyle;
    BitmapFont font;
    Skin skin;
    TextureAtlas buttonAtlas;

    Table table;
    Table labelTable;
    Table buttonTable;
    Label changingLabel;
    ScrollPane scrollPane;

    PhysicsSpace space;
    String textTimeScale;
    Timer timer;

    Label[] labels;

    String buttonPressID;

    OrthographicCamera camera;
    Viewport viewport;

    PhysicsObject physicsObject;
    int clickPlanetIndex;

    private Timer.Task changingTextTask;

    public UiCenter(PhysicsSpace space){
        camera = new OrthographicCamera();
        viewport = new ScreenViewport();
        stage = new Stage(viewport);
        stage.setDebugAll(true);

        font = new BitmapFont();
        skin = new Skin();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        skin.addRegions(buttonAtlas);
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        table = new Table();
        table.setFillParent(true);

        // Create a Label with an empty string
        changingLabel = new Label("123", new Label.LabelStyle(font, Color.WHITE));
        table.add(changingLabel).center();
        stage.addActor(table);

        // TextButton
        buttonTable = new Table();
        buttonTable.bottom();
        buttonPressID = "";

        button = createButton("pause/play", "buttonPause", 5);
        button.pad(5);
        buttonTable.add(button);

        button = createButton("toggle vectors", "buttonVectors", 5);
        buttonTable.add(button);

        button = createButton("Add Random planet", "planetAdder", 5);
        buttonTable.add(button);

        buttonTable.pad(5);
        buttonTable.setPosition(500,0);

        stage.setViewport(viewport);
        stage.addActor(buttonTable);

        labelTable = new Table();

        // Create a scroll pane and add the table to it
        scrollPane = new ScrollPane(labelTable);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        // Add the scroll pane to the stage
        stage.addActor(scrollPane);

        // Position the scroll pane on the middle-left side of the screen
        scrollPane.setPosition(0, stage.getHeight() / 2f - scrollPane.getHeight() / 2f);

        clickPlanetIndex = -1;

        labels = new Label[3];
        Label label;
        labels[0] = new Label("mass: ",  new Label.LabelStyle(font, Color.WHITE));
        labels[1] = new Label("velocity X: ",  new Label.LabelStyle(font, Color.WHITE));
        labels[2] = new Label("velocity Y: " ,  new Label.LabelStyle(font, Color.WHITE));
        for (int i = 0; i < labels.length; i++) {
            labelTable.add(labels[i]);
        }
    }

    public void render(float delta) {
        // Update and render the stage
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        if(clickPlanetIndex > 0){
            doPlanetInfoLabels();
        }
    }

    public void resize(int width, int height) {
        // Update the stage's viewport when the screen is resized
        stage.getViewport().update(width, height, true);
    }
    public void doPlanetInfoLabels() {
        physicsObject = space.getObjectAtIndex(clickPlanetIndex);
        Label[] labels = new Label[3];
        Label label;
        labels[0] = new Label("mass: " + physicsObject.getMass(),  new Label.LabelStyle(font, Color.WHITE));
        labels[1] = new Label("velocity X: " + physicsObject.getVelocity().x,  new Label.LabelStyle(font, Color.WHITE));
        labels[2] = new Label("velocity Y: " + physicsObject.getVelocity().y,  new Label.LabelStyle(font, Color.WHITE));

    }
    public String getButtonPressID() {
        return buttonPressID;
    }

    public void setButtonPressID(String buttonPressID) {
        this.buttonPressID = buttonPressID;
    }

    public void setChangingText(String text) {
        changingLabel.setText(text);
        changingLabel.clearActions();

        // Cancel any existing task
        if (changingTextTask != null) {
            changingTextTask.cancel();
        }
        // Schedule a new task
        changingTextTask = new Timer.Task() {
            @Override
            public void run() {
                changingLabel.setText(""); // Set the text back to an empty string
            }
        };

        Timer.schedule(changingTextTask, 0.5f);
    }
    public TextButton createButton(String text, String buttonName, int padding){
        button = new TextButton(text, textButtonStyle);
        button.pad(padding);
        button.setName(buttonName);
        buttonPressID = "";
        button.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                buttonPressID = actor.getName();
            }
        });
        return button;
    }

    public void updatecamera(int width, int height){
        viewport.update(width, height);
    }

    public Stage getStage() {
        return stage;
    }

    public void setPlanetClicked(int index){
        clickPlanetIndex = index;
    }

}