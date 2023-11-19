package com.tajjulo.orbitalsandbox.tools;

import com.badlogic.gdx.math.Vector2;
import com.tajjulo.orbitalsandbox.game.PhysicsObject;
import com.tajjulo.orbitalsandbox.game.PhysicsSpace;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

//  format:
//  {posX="0",posY="0",mass="1000",speedX="1000",speedY="1000",isStatic="0",density="10"}
public class PlanetMap {

    private final String[] dataNames = {"posX","posY","mass","speedX","speedY","isStatic","density"};

    private String inputFileName;
    private String outputFileName;

    private ArrayList<int[]> list;
    private PhysicsSpace physicsSpace;

    public PlanetMap(){
        inputFileName = "inputMap.txt";
        outputFileName = "outputMap.txt";
        list = new ArrayList<int[]>();
    }

    public PlanetMap(String inputFileName, String outputFileName) {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
        list = new ArrayList<int[]>();
    }

    private int[] lineToArray(String line){
        int[] arr = new int[dataNames.length];
        for (int i = 0; i < dataNames.length; i++) {
            int dataIndex = line.indexOf(dataNames[i]);
            int beginIndex = dataIndex + dataNames[i].length() + 2;
            int endIndex = 0;

            char currentChar;
            int count = 0;
            while((currentChar = line.charAt(beginIndex + count)) != '"') {
                endIndex = beginIndex + count + 1;
                count++;
            }

            String value = line.substring(beginIndex, endIndex);
            arr[i] = Integer.valueOf(value);
        }
        return arr;
    }

    //iz physiscs space naredi txt file
    public void convertMapToText(){
        //todo
    }

    //vrne physiscs space za simulacijo
    public PhysicsSpace convertTextToMap() throws Exception {
        physicsSpace = new PhysicsSpace();
        BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
        String line;
        while ((line = reader.readLine()) != null){
            list.add(lineToArray(line));
        }
        for (int i = 0; i < list.size(); i++) {
            int[] arr = list.get(i);
            physicsSpace.addObject(new PhysicsObject(arr[0], arr[1], arr[2], new Vector2(arr[3], arr[4]),arr[5] != 0, arr[6]));
        }
        return physicsSpace;
    }

    @Override
    public String toString() {
        return "planetMap{" +
                "inputFileName='" + inputFileName + '\'' +
                ", outputFileName='" + outputFileName + '\'' +
                ", physicsSpace=" + physicsSpace +
                ", list=" + list +
                '}';
    }

    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public String getInputFileName() {
        return inputFileName;
    }

    public String getOutputFileName() {
        return outputFileName;
    }
}
