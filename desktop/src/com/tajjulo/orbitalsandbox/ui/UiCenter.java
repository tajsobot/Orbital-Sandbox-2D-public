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
import com.tajjulo.orbitalsandbox.game.PhysicsSpace;

public class UiCenter {
    Stage stage;
    TextButton button;
    TextButton.TextButtonStyle textButtonStyle;
    BitmapFont font;
    Skin skin;
    TextureAtlas buttonAtlas;
    Table table;
    Label changingLabel;
    Table labelTable;
    ScrollPane scrollPane;

    PhysicsSpace space;
    String textTimeScale;
    Timer timer;

    String buttonPressID;

    OrthographicCamera camera;
    Viewport viewport;

    private Timer.Task changingTextTask;

    public UiCenter(PhysicsSpace space){
        camera = new OrthographicCamera();
        viewport = new ScreenViewport();
        stage = new Stage(viewport);

        font = new BitmapFont();
        skin = new Skin();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        skin.addRegions(buttonAtlas);
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        table = new Table();
        table.setFillParent(true);

        // Create a Label with an empty string
        changingLabel = new Label("", new Label.LabelStyle(font, Color.WHITE));
        table.add(changingLabel).center();

        // TextButton
        button = new TextButton("pause/play", textButtonStyle);
        button.setName("buttonPause");
        button.setPosition(2,2);
        buttonPressID = "";
        button.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                buttonPressID = actor.getName();
            }
        });
        stage.addActor(button);

        button = new TextButton("toggle vectors", textButtonStyle);
        button.setPosition(2,20);
        button.setName("buttonVectors");
        buttonPressID = "";
        button.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                buttonPressID = actor.getName();
            }
        });
        stage.addActor(button);

        button = new TextButton("Add Random planet", textButtonStyle);
        button.setPosition(2,40);
        button.setName("planetAdder");
        buttonPressID = "";
        button.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                buttonPressID = actor.getName();
            }
        });
        stage.addActor(button);
        stage.setViewport(viewport);
        stage.addActor(table);

        labelTable = new Table();

        // Create a scroll pane and add the table to it
        scrollPane = new ScrollPane(labelTable);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        // Add the scroll pane to the stage
        stage.addActor(scrollPane);

        // Position the scroll pane on the middle-left side of the screen
        scrollPane.setPosition(0, stage.getHeight() / 2f - scrollPane.getHeight() / 2f);

        //todo
        addNewLabel("pep");
        addNewLabel("opek");
        addNewLabel("opek");
        addNewLabel("opek");
        addNewLabel("opek");
        addNewLabel("opek");
    }

    public void addNewLabel(String labelText) {
        // Create a new label with the provided text
        Label label = new Label(labelText,  new Label.LabelStyle(font, Color.WHITE));

        // Add qthe label to the table
        labelTable.add(label).expandX().fillX().padLeft(5).row();

        // Scroll to the bottom to show the latest label
        labelTable.layout();
        labelTable.invalidate();
        scrollPane.setScrollPercentY(1);
    }

    public void render(float delta) {
        // Update and render the stage
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    public void resize(int width, int height) {
        // Update the stage's viewport when the screen is resized
        stage.getViewport().update(width, height, true);
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

    public void updatecamera(int width, int height){
        viewport.update(width, height);
    }

    public Stage getStage() {
        return stage;
    }
}