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
}
