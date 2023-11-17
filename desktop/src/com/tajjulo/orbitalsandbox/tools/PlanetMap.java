package com.tajjulo.orbitalsandbox.tools;

import com.tajjulo.orbitalsandbox.game.PhysicsSpace;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

//  format:
//  {posX="0",posY="0",mass="1000",speedX="1000",speedY="1000",isStatic="0",density="10"}
public class PlanetMap {

    private final String[] dataNames = {"posX","posY","mass","speedX","speedY","isStatic","density" };

    private String inputFileName;
    private String outputFileName;

    private PhysicsSpace physicsSpace;
    private ArrayList<int[]> list;

    public void planetMap(){
        this.inputFileName = "C:\\Users\\K01-DM-04\\IdeaProjects\\Orbital-Sandbox-2D\\inputMap.txt";
        this.outputFileName = "outputMap.txt";
    }
    public void planetMap(String inputFileName, String outputFileName) {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
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
                System.out.print(currentChar + ", ");
                endIndex = beginIndex + count + 1;
                count++;
            }

            String value = line.substring(beginIndex, endIndex);
            arr[i] = Integer.valueOf(value);
        }
        return arr;
    }
    public void convertMapToText(){

    }
    public PhysicsSpace convertTextToMap() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\K01-DM-04\\IdeaProjects\\Orbital-Sandbox-2D\\inputMap.txt"));
        String line;
        while ((line = reader.readLine()) != null){
            list.add(lineToArray(line));
        }

        return null;
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
}
