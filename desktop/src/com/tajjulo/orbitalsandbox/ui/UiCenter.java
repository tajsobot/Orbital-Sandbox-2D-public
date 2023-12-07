package com.tajjulo.orbitalsandbox.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.*;
import com.tajjulo.orbitalsandbox.game.PhysicsObject;
import com.tajjulo.orbitalsandbox.game.PhysicsSpace;
import sun.font.TextLabel;

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
    Label[] labels;
    String buttonPressID;
    OrthographicCamera camera;
    Viewport viewport;
    TextField textField;
    int clickPlanetIndex = -1;
    private Timer.Task changingTextTask;

    public UiCenter(PhysicsSpace space){
        this.space = space;
        camera = new OrthographicCamera();
        viewport = new ScreenViewport();
        stage = new Stage(viewport);

        font = new BitmapFont();
//      skin = new Skin();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
//      skin.addRegions(buttonAtlas);
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        table = new Table();
        table.setFillParent(true);

        // Create a Label with an empty string
        changingLabel = new Label("", new Label.LabelStyle(font, Color.WHITE));
        table.add(changingLabel).center();
        stage.addActor(table);

        // TextButton
        buttonTable = new Table();
        buttonTable.bottom();
        buttonPressID = "";

        button = createButton("< ", "timeDecrease", 5);
        buttonTable.add(button);

        button = createButton(" >", "timeIncrease", 5);
        buttonTable.add(button);

        button = createButton("Pause/Play", "buttonPause", 5);
        buttonTable.add(button);

        button = createButton("Toggle vectors", "buttonVectors", 5);
        buttonTable.add(button);

        button = createButton("Add random planet", "planetRandomAdder", 5);
        buttonTable.add(button);

        button = createButton("Add custom planet", "planetCustomAdder", 5);
        buttonTable.add(button);

        buttonTable.pad(5).bottom();
        buttonTable.setPosition(Gdx.graphics.getWidth()/2f, buttonTable.getY());

        stage.setViewport(viewport);
        stage.addActor(buttonTable);

        labelTable = new Table();

        scrollPane = new ScrollPane(labelTable);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false); // Disable horizontal scrolling

        // Set the position of the ScrollPane to the left center of the screen
        clickPlanetIndex = -1;
        labels = new Label[4];
        labels[0] = new Label("", new Label.LabelStyle(font, Color.WHITE));
        labels[0].setName("mass");
        labels[1] = new Label("", new Label.LabelStyle(font, Color.WHITE));
        labels[1].setName("speed");
        labels[2] = new Label("" , new Label.LabelStyle(font, Color.WHITE));
        labels[2].setName("acceleration");
        labels[3] = new Label("", new Label.LabelStyle(font, Color.WHITE));
        labels[3].setName("force");

        float scrollPaneHeight = (Gdx.graphics.getHeight() / 2f); // Set the height as needed
        scrollPane.setBounds(0, (Gdx.graphics.getHeight() - scrollPaneHeight) / 2f, Gdx.graphics.getWidth() / 2f, scrollPaneHeight);
        stage.addActor(scrollPane);

        for (Label leftLabel : labels) {
            labelTable.add(leftLabel).expandX().fillX().row();
            labelTable.pad(3);

            leftLabel.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("Label Clicked", leftLabel.getName());

                }
            });
        }

        //novo
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.setFillParent(true);
        verticalGroup.center();
        textField = new TextField("", skin);
        verticalGroup.addActor(textField);
        stage.addActor(verticalGroup);
    }

    private void printEnteredText() {
        String enteredText = textField.getText();
        System.out.println("Entered Text: " + enteredText);
    }

    public void doPlanetInfoLabels() {
        if(clickPlanetIndex >= 0 && space.getSize() > 0){
            PhysicsObject po = space.getObjectAtIndex(clickPlanetIndex);
            labels[0].setText("M = " + po.getMass() + " kg");
            labels[1].setText("V = " + (int)po.getVelocity().len() + " m/s");
            labels[2].setText("A = " + (int)po.getAcceleration().len() + " m/s2");
            labels[3].setText("Fg = " + (int)po.getForceForDrawing().len() + " N");

        }else {
            labels[0].setText("");
            labels[1].setText("");
            labels[2].setText("");
            labels[3].setText("");
        }
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

        if (changingTextTask != null) {
            changingTextTask.cancel();
        }
        changingTextTask = new Timer.Task() {
            @Override
            public void run() {
                changingLabel.setText("");
            }
        };
        //delay
        Timer.schedule(changingTextTask, 1.0f);
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

    public Stage getStage() {
        return stage;
    }

    public void updateResize(int width, int height){
        viewport.update(width, height, true);
        buttonTable.setPosition(width/2f, buttonTable.getY());
        float scrollPaneHeight = (Gdx.graphics.getHeight() / 10f); // Set the height as needed
        scrollPane.setBounds(0, (Gdx.graphics.getHeight() - scrollPaneHeight) / 2f, 200, scrollPaneHeight);
        stage.setDebugAll(true);

    }

    public void setPlanetClicked(int index){
        clickPlanetIndex = index;
    }

    public void toggleTextField(){

    }
}