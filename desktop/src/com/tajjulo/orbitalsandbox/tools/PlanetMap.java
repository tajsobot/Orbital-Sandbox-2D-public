package com.tajjulo.orbitalsandbox.tools;

import com.badlogic.gdx.math.Vector2;
import com.tajjulo.orbitalsandbox.game.PhysicsObject;
import com.tajjulo.orbitalsandbox.game.PhysicsSpace;

import java.io.*;
import java.util.ArrayList;

public class PlanetMap {

    //prefix za podatke, lahko kasneje dodajaš nove po potrebi
    private final String[] dataNames = {"posX","posY","mass","speedX","speedY","isStatic","density"};

    private String inputFileName;
    private String outputFileName;
    private ArrayList<float[]> list;
    private PhysicsSpace physicsSpace;

    public PlanetMap(){
        inputFileName = "inputMap.txt";
        outputFileName = "outputMap.txt";
        list = new ArrayList<float[]>();
    }

    public PlanetMap(String inputFileName, String outputFileName) {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
        list = new ArrayList<float[]>();
    }

    //metoda za
    private float[] lineToArray(String line){
        float[] arr = new float[dataNames.length];
        for (int i = 0; i < dataNames.length; i++) {
            //najde prvi in zadnji prefix
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
            arr[i] = Float.parseFloat(value);
        }
        return arr;
    }

    //iz physiscs space naredi txt file
    public void convertMapToText(PhysicsSpace physicsSpace1){
        physicsSpace = physicsSpace1;
        try {
            PrintWriter printWriter = new PrintWriter(outputFileName);
            for (int i = 0; i < physicsSpace.getSize(); i++) {
                PhysicsObject po = physicsSpace.getObjectAtIndex(i);
                //spremeni static v boolean vrednost
                int isStatic = 0;
                if(po.getIsStatic()) isStatic = 1;
                printWriter.println(
                        "{posX=\"" + po.getPosX() +
                                "\",posY=\""+ po.getPosY() +
                                "\",mass=\"" + po.getMass() +
                                "\",speedX=\"" + po.getVelocity().x +
                                "\",speedY=\"" + po.getVelocity().y +
                                "\",isStatic=\"" + isStatic +
                                "\",density=\"" + po.getDensity() + "\"}");
            }
            printWriter.close();
        } catch (IOException e){
            System.out.println(e);
        }
    }

    //iz txt file naredi physiscs space
    public PhysicsSpace convertTextToMap(){
        physicsSpace = new PhysicsSpace();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
            String line;
            while ((line = reader.readLine()) != null){
                list.add(lineToArray(line));
            }
            for (int i = 0; i < list.size(); i++) {
                float[] arr = list.get(i);
                physicsSpace.addObject(new PhysicsObject(arr[0], arr[1], (int)arr[2], new Vector2(arr[3], arr[4]),arr[5] != 0, (float)arr[6]));
            }
            return physicsSpace;
        } catch (Exception e){
            System.out.println(e);
            System.out.println("napaka pri branju, napačne ni pravih prefixov");
        }
        return physicsSpace;
    }

    @Override
    public String toString() {
        return "planetMap{" +
                "inputFileName='" + inputFileName + '\'' +
                ", outputFileName='" + outputFileName + '\'' +
                ", physicsSpace=" + physicsSpace.toString() +
                '}';
    }

    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public void setList(ArrayList<float[]> list) {
        this.list = list;
    }

    public void setPhysicsSpace(PhysicsSpace physicsSpace) {
        this.physicsSpace = physicsSpace;
    }

    public String getInputFileName() {
        return inputFileName;
    }

    public String getOutputFileName() {
        return outputFileName;

    }

    public ArrayList<float[]> getList() {
        return list;
    }

    public PhysicsSpace getPhysicsSpace() {
        return physicsSpace;
    }

    public String[] getDataNames() {
        return dataNames;
    }
}