package com.tajjulo.orbitalsandbox.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.*;
import com.tajjulo.orbitalsandbox.game.Game;
import com.tajjulo.orbitalsandbox.game.PhysicsSpace;


import static com.badlogic.gdx.scenes.scene2d.ui.Table.Debug.table;

public class UiLeft {
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

    public UiLeft(PhysicsSpace space){
        this.space = space;

        stage = new Stage(new FitViewport(1024, 768));
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
        table.add(changingLabel).expand().center();

        // TextButton
        button = new TextButton("asdasda", textButtonStyle);
        button.setName("buttonPause");
        buttonPressID = "";
        button.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                buttonPressID = actor.getName();
            }
        });

        Gdx.input.setInputProcessor(stage);

        // Add UI elements to the stage
        stage.addActor(button);
        stage.addActor(table);
    }

    public void renderUi(){
        stage.draw();
        stage.setDebugAll(true);
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
}