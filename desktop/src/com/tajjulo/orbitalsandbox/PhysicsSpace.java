package com.tajjulo.orbitalsandbox;

import java.util.ArrayList;

public class PhysicsSpace {

    private long constG;
    private long boundX;
    private long boundY;

    private ArrayList<PhysicsObject> objectList;

    public PhysicsSpace(){
        objectList = new ArrayList<PhysicsObject>();
        boundX = 100;
        boundY = 100;
    }
    public PhysicsSpace(long boundX1, long boundY1){
        boundX = boundX1;
        boundY = boundY1;
    }
    public void addObject(PhysicsObject object){
        objectList.add(object);
    }
    public void updateAll(float deltaTime){
        for (int i = 0; i < objectList.size(); i++) {
            for (int j = 0; j < objectList.size(); j++) {
                if(i != j){
                    objectList.get(i).updateGravity(objectList.get(j), deltaTime);
                }
            }
        }

        //kinematics + finish
        for (int i = 0; i < objectList.size(); i++) {
            objectList.get(i).updateKinematics(deltaTime);
            objectList.get(i).updateFinish();
        }

    }
}
