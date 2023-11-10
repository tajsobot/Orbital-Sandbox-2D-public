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

    PhysicsSpace space;
    String textTimeScale;
    Timer timer;

    String buttonPressID;

    OrthographicCamera camera;
    Viewport viewport;


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

        Gdx.input.setInputProcessor(stage);

        // Add UI elements to the stage
        stage.setViewport(viewport);
        stage.addActor(table);
    }

    public void renderUi(){
        stage.draw();
        stage.act(Gdx.graphics.getDeltaTime());
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

        changingLabel.addAction(Actions.sequence(
            Actions.delay(0.5f),
            Actions.run(new Runnable() {
                @Override
                public void run() {
                    changingLabel.setText(""); // Set the text back to an empty string
                }
            })
        ));
    }
    public void updatecamera(int width, int height){
        viewport.update(width, height);
        }
}