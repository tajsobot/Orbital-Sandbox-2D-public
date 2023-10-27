package com.tajjulo.orbitalsandbox.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class UiLeft {
    TextButton.TextButtonStyle buttonStyle;
    Stage stage;
    public UiLeft(){
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        TextButton button = new TextButton("Click me!", buttonStyle);
    }
    public void renderUi(){
        stage.draw();
    }
}