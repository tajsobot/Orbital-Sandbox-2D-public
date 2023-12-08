package com.tajjulo.orbitalsandbox.game;

import com.tajjulo.orbitalsandbox.game.PhysicsObject;

import java.util.ArrayList;

public class PhysicsSpace {
    private int timeScale = 1;
    private boolean doCollisions = true;

    private ArrayList<PhysicsObject> objectList;

    public PhysicsSpace(){
        objectList = new ArrayList<PhysicsObject>();
    }

    public void addObject(PhysicsObject object){
        objectList.add(object);
    }

    public void updateAll(float deltaTime){
        for (int t = 0; t < Math.abs(timeScale); t++) {
            for (int i = 0; i < objectList.size(); i++) {
                for (int j = 0; j < objectList.size(); j++) {
                    if(i != j){
                        PhysicsObject obj1 = objectList.get(i);
                        PhysicsObject obj2 = objectList.get(j);
                        obj1.updateGravity(obj2, deltaTime);
                        if(doCollisions){

                            float distance = (float)Math.sqrt(Math.pow(obj1.getPosX() - obj2.getPosX(),2) + Math.pow(obj1.getPosY() - obj2.getPosY(),2));
                            float minCollisionDistance = obj1.getPlanetRadius() + obj2.getPlanetRadius();
                            if(distance < minCollisionDistance){
                                System.out.println(i + " "+ j);
                            }
                        }
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

    public PhysicsObject getObjectAtIndex(int i) {
        if(i < objectList.size()){
            return objectList.get(i);
        }
        return objectList.get(0);
    }

    public int getSize(){
        return objectList.size();
    }

    public void setTimeScale(int timeScale) {
        this.timeScale = timeScale;
    }

    public int getTimeScale() {
        return timeScale;
    }

    public ArrayList<PhysicsObject> getObjectList() {
        return objectList;
    }

    public void addTimeScale(int addValue){
        timeScale += addValue;
    }
    public void removeObject(int index){
        objectList.remove(index);
    }
    public void removeAllObjects(){
        for(int i = objectList.size() -1 ; i >= 0; i--) {
            objectList.remove(i);
        }
    }
}