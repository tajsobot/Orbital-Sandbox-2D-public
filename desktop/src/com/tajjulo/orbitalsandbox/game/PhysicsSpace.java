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
                        //todo collisions
                        if(doCollisions){
                            float distance = (float)Math.sqrt(Math.pow(obj1.getPosX() - obj2.getPosX(),2) + Math.pow(obj1.getPosY() - obj2.getPosY(),2));
                            float minCollisionDistance = obj1.getPlanetRadius() + obj2.getPlanetRadius();
                            if(distance < minCollisionDistance){
                                if(obj1.getMass() >= obj2.getMass()){
                                    //keep momentum
                                    obj1.addVelocity(obj2.getVelocity(), obj2.getMass()/obj1.getMass());
                                    //mass
                                    obj1.setMass(obj1.getMass() + obj2.getMass());
                                    obj1.updatePlanetRadius();

                                    objectList.remove(obj2);

                                    float centerX = ((obj1.getMass()*obj1.getPosX()) + (obj2.getMass() * obj2.getPosX()))/ (obj1.getMass() + obj2.getMass());
                                    float centerY = ((obj1.getMass()*obj1.getPosY()) + (obj2.getMass() * obj2.getPosY()))/ (obj1.getMass() + obj2.getMass());
                                    obj1.setPosX(centerX);
                                    obj1.setPosY(centerY);
                                } else if(obj1.getMass() < obj2.getMass()){
                                    //momentum
                                    obj2.addVelocity(obj1.getVelocity(), obj1.getMass()/obj2.getMass());
                                    //mass
                                    obj2.setMass(obj1.getMass() + obj2.getMass());
                                    obj2.updatePlanetRadius();

                                    objectList.remove(obj1);
                                    float centerX = ((obj1.getMass()*obj1.getPosX()) + (obj2.getMass() * obj2.getPosX()))/ (obj1.getMass() + obj2.getMass());
                                    float centerY = ((obj1.getMass()*obj1.getPosY()) + (obj2.getMass() * obj2.getPosY()))/ (obj1.getMass() + obj2.getMass());
                                    obj2.setPosX(centerX);
                                    obj2.setPosY(centerY);
                                }
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
    private void doCollisions(){

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