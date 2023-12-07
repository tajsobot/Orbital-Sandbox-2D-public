package com.tajjulo.orbitalsandbox.tools;

import com.badlogic.gdx.math.Vector2;
import com.tajjulo.orbitalsandbox.game.PhysicsObject;
import com.tajjulo.orbitalsandbox.game.PhysicsSpace;

import java.io.*;
import java.util.ArrayList;

//  format:
//  {posX="0",posY="0",mass="1000",speedX="1000",speedY="1000",isStatic="0",density="10"}
public class PlanetMap {

    private final String[] dataNames = {"posX","posY","mass","speedX","speedY","isStatic","density"};

    private String inputFileName;
    private String outputFileName;

    private ArrayList<double[]> list;
    private PhysicsSpace physicsSpace;

    public PlanetMap(){
        inputFileName = "inputMap.txt";
        outputFileName = "outputMap.txt";
        list = new ArrayList<double[]>();
    }

    public PlanetMap(String inputFileName, String outputFileName) {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
        list = new ArrayList<double[]>();
    }

    private double[] lineToArray(String line){
        double[] arr = new double[dataNames.length];
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
            arr[i] = Double.parseDouble(value);
        }
        return arr;
    }

    //iz physiscs space naredi txt file
    public void convertMapToText(PhysicsSpace physicsSpace1) throws Exception {
        physicsSpace = physicsSpace1;
        PrintWriter printWriter = new PrintWriter(outputFileName);
        for (int i = 0; i < physicsSpace.getSize(); i++) {
            PhysicsObject po = physicsSpace.getObjectAtIndex(i);
            int isStatic = 0;
            if(po.getIsStatic()) isStatic = 1;
            printWriter.println(
                    "{posX=\"" + (int)po.getPosX() +
                    "\",posY=\""+ (int)po.getPosY() +
                    "\",mass=\"" + (int)po.getMass() +
                    "\",speedX=\"" + (int)po.getVelocity().x +
                    "\",speedY=\"" + (int)po.getVelocity().y +
                    "\",isStatic=\"" + (int)isStatic +
                    "\",density=\"" + (int)po.getDensity() + "\"}");
        }
        printWriter.close();
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
            double[] arr = list.get(i);
            physicsSpace.addObject(new PhysicsObject((long)arr[0], (long)arr[1], (int)arr[2], new Vector2((float)arr[3], (float)arr[4]),arr[5] != 0, (float)arr[6]));
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
