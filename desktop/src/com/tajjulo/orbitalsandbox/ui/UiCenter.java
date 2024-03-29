package com.tajjulo.orbitalsandbox.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.*;
import com.sun.org.apache.xpath.internal.operations.Or;
import com.tajjulo.orbitalsandbox.OrbitalSandbox;
import com.tajjulo.orbitalsandbox.game.PhysicsObject;
import com.tajjulo.orbitalsandbox.game.PhysicsSpace;
import com.tajjulo.orbitalsandbox.screens.GameScreen;
import com.tajjulo.orbitalsandbox.screens.MainMenuScreen;

import java.util.ArrayList;

public class UiCenter {
    OrbitalSandbox game;
    Stage stage;
    TextButton button;
    TextButton.TextButtonStyle textButtonStyle;
    BitmapFont font;
    Skin skin;
    TextureAtlas buttonAtlas;
    Table table;
    Table labelTableLeft;
    Table buttonTable;
    Label changingLabel;
    ScrollPane scrollPaneLeft;
    PhysicsSpace space;
    Label[] labels;
    ArrayList<Label> labelsRight;
    String buttonPressID;
    OrthographicCamera camera;
    Viewport viewport;
    TextField textField;
    int clickPlanetIndex = -1;
    boolean isInputingNumbers = false;
    private Timer.Task changingTextTask;
    //ne brisi pomembno za text filed
    VerticalGroup verticalGroup;

    String labelName;
    int savedTimescale;
    ScrollPane scrollPaneRight;
    Table labelTableRight;
    Label localLabelRight;

    ArrayList<Label> labelListRight;

    public UiCenter(PhysicsSpace space){
        this.space = space;
        camera = new OrthographicCamera();
        viewport = new ScreenViewport();
        viewport.setWorldSize(10,10);
        stage = new Stage(viewport);

        font = new BitmapFont();
//      skin = new Skin();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
//      skin.addRegions(buttonAtlas);
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        table = new Table();
        table.setFillParent(true);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        verticalGroup = new VerticalGroup();
        verticalGroup.setFillParent(true);
        verticalGroup. setVisible(false);
        verticalGroup.center();
        textField = new TextField("", skin);
        verticalGroup.addActor(textField);
        stage.addActor(verticalGroup);

        labelListRight = new ArrayList<>();

        // Create a Label with an empty string
        changingLabel = new Label("", new Label.LabelStyle(font, Color.WHITE));
        table.add(changingLabel).center();
        stage.addActor(table);

        clickPlanetIndex = -1;

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

        button = createButton("Add custom planet", "planetCustomAdder", 5);
        buttonTable.add(button);

        button = createButton("Remove all", "removeAll", 5);
        buttonTable.add(button);
        button = createButton("random", "planetRandomAdder", 5);
        buttonTable.add(button);
        button = createButton("Main Menu", "mainMenu", 5);
        buttonTable.add(button);

        buttonTable.pad(5).bottom();
        buttonTable.setPosition(Gdx.graphics.getWidth()/2f, buttonTable.getY());

        stage.addActor(buttonTable);

        //table left
        labelTableLeft = new Table();

        scrollPaneLeft = new ScrollPane(labelTableLeft);
        scrollPaneLeft.setFadeScrollBars(false);
        scrollPaneLeft.setScrollingDisabled(true, false); // Disable horizontal scrolling

        clickPlanetIndex = -1;
        labels = new Label[6];
        labels[0] = new Label("", new Label.LabelStyle(font, Color.WHITE));
        labels[0].setName("name");
        labels[1] = new Label("", new Label.LabelStyle(font, Color.WHITE));
        labels[1].setName("mass");
        labels[2] = new Label("", new Label.LabelStyle(font, Color.WHITE));
        labels[2].setName("speed");
        labels[3] = new Label("" , new Label.LabelStyle(font, Color.WHITE));
        labels[3].setName("speedX");
        labels[4] = new Label("", new Label.LabelStyle(font, Color.WHITE));
        labels[4].setName("speedY");
        labels[5] = new Label("", new Label.LabelStyle(font, Color.WHITE));
        labels[5].setName("force");

        float scrollPaneHeightLeft = (Gdx.graphics.getHeight() / 2f); // Set the height as needed
        scrollPaneLeft.setBounds(0, (Gdx.graphics.getHeight() - scrollPaneHeightLeft) / 2f, Gdx.graphics.getWidth() / 2f, scrollPaneHeightLeft);
        stage.addActor(scrollPaneLeft);

        for (Label leftLabel : labels) {
            labelTableLeft.add(leftLabel).expandX().fillX().row();
            labelTableLeft.pad(3);

            leftLabel.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("Label Clicked", leftLabel.getName());
                    labelName = leftLabel.getName();
                    savedTimescale = space.getTimeScale();
                    space.setTimeScale(0);
                    isInputingNumbers = true;
                    verticalGroup.setVisible(true);
                    textField.setText("");
                }
            });
        }
        //table right
        labelTableRight = new Table();
        scrollPaneRight = new ScrollPane(labelTableRight);
        scrollPaneRight.setFadeScrollBars(false);
        scrollPaneRight.setScrollingDisabled(true, false); // Disable horizontal scrolling

        float scrollPaneHeightRight = (Gdx.graphics.getHeight() / 2f); // Set the height as needed
        scrollPaneRight.setBounds(0, (Gdx.graphics.getHeight() - scrollPaneHeightRight) / 2f, Gdx.graphics.getWidth() / 2f, scrollPaneHeightRight);
        stage.addActor(scrollPaneRight);

        //ostalo
        Label versionLabel = new Label("(Debug) Beta Version 0.1", new Label.LabelStyle(font, Color.WHITE));
        versionLabel.setPosition(10, 10);
        stage.addActor(versionLabel);

        updateResize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
    public void updateInputs(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && isInputingNumbers){
            int in = 0;
            isInputingNumbers = false;
            verticalGroup.setVisible(false);
            try {
                if(labelName.compareTo("name")==0){
                    space.getObjectAtIndex(clickPlanetIndex).setName(getEnteredText());
                } else{
                    in = Integer.parseInt(getEnteredText());
                }
                if(labelName.compareTo("mass")==0){
                    space.getObjectAtIndex(clickPlanetIndex).setMass(in);
                }
                if(labelName.compareTo("speed")==0){
                    Vector2 vect = space.getObjectAtIndex(clickPlanetIndex).getVelocity();
                    vect.nor();
                    vect.x = vect.x * in;
                    vect.y = vect.y * in;
                    space.getObjectAtIndex(clickPlanetIndex).setVelocity(vect);
                }
                if(labelName.compareTo("speedX")==0){
                    Vector2 vect = space.getObjectAtIndex(clickPlanetIndex).getVelocity();
                    vect.x = in;
                    space.getObjectAtIndex(clickPlanetIndex).setVelocity(vect);
                }
                if(labelName.compareTo("speedY")==0){
                    Vector2 vect = space.getObjectAtIndex(clickPlanetIndex).getVelocity();
                    vect.y = in;
                    space.getObjectAtIndex(clickPlanetIndex).setVelocity(vect);
                }
                space.getObjectAtIndex(clickPlanetIndex).updatePlanetRadius();
            }catch (Exception e){
                System.out.println("napacen vnos");
            }
            space.setTimeScale(savedTimescale);
        }
    }

    private String getEnteredText() {
        String enteredText = textField.getText();
        System.out.println("Entered Text: " + enteredText);
        return enteredText;
    }

    //ta metoda se perma updatja
    public void doPlanetInfoLabels() {
        //levi del
        if(clickPlanetIndex >= 0 && space.getSize() > 0){
            PhysicsObject po = space.getObjectAtIndex(clickPlanetIndex);
            labels[0].setText("name = " + po.getName());
            labels[1].setText("M = " + po.getMass() + " kg");
            labels[2].setText("V = " + (int)po.getVelocity().len() + " m/s");
            labels[3].setText("Vx = " + (int)po.getVelocity().x + " m/s");
            labels[4].setText("Vy = " + (int)po.getVelocity().y + " m/s");
            labels[5].setText("F = " + (int)po.getForceForDrawing().len() + " N");

        }else {
            labels[0].setText("");
            labels[1].setText("");
            labels[2].setText("");
            labels[3].setText("");
            labels[4].setText("");
            labels[5].setText("");
        }

        //desni del
        labelTableRight.clear();

        for (int i = 0; i < space.getSize(); i++) {
            localLabelRight = new Label(1+i + ": " + space.getObjectAtIndex(i).getName(), new Label.LabelStyle(font, Color.WHITE));
            localLabelRight.setName(i + "");
            labelTableRight.add(localLabelRight).expandX().fillX().row();
            labelTableRight.pad(3);
            localLabelRight.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("Label Clicked");
                }
            });
            labelListRight.add(localLabelRight);
        }
        float scrollPaneHeightRight = space.getSize()*20;
        scrollPaneRight.setBounds(Gdx.graphics.getWidth() - 200, (Gdx.graphics.getHeight() - scrollPaneHeightRight) / 2f, 200, scrollPaneHeightRight);

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
        float scrollPaneHeightLeft = (Gdx.graphics.getHeight() / 6f); // Set the height as needed
        scrollPaneLeft.setBounds(0, (Gdx.graphics.getHeight() - scrollPaneHeightLeft) / 2f, 200, scrollPaneHeightLeft);
        float scrollPaneHeightRight = space.getSize()*10;
        scrollPaneRight.setBounds(Gdx.graphics.getWidth() - 200, (Gdx.graphics.getHeight() - scrollPaneHeightRight) / 2f, 200, scrollPaneHeightRight);
    }

    public void setPlanetClicked(int index){
        clickPlanetIndex = index;
    }

    public boolean isInputingNumbers() {
        return isInputingNumbers;
    }
}