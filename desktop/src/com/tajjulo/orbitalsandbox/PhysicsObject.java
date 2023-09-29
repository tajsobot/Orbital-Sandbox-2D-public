package com.tajjulo.orbitalsandbox;

import com.badlogic.gdx.math.Vector2;

import java.net.Socket;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Vector;

public class PhysicsObject {

    private final float G = 1000;
    private final float maxDeltaTime = 0.05f;

    private float posX;
    private float posY;

    private long mass;
    private Vector2 force;

    private Vector2 velocity;
    private Vector2 acceleration;

    private float minimalSimulationRadius = 10;
    private float timeElapsed;
    private int tracerFrequency; //in Hz
    private boolean doTraces = true;
    private int maxTracers = 100;
    private LinkedList<Vector2> traces;

    //constructors
    public PhysicsObject(){

        posY = 0;
        posX = 0;
        mass = 1;
        force = new Vector2(0,0);
        velocity = new Vector2(0,0);
        acceleration = new Vector2(0,0);
    }

    public PhysicsObject(long x, long y){
        posX = x;
        posY = y;
        mass = 1;
        force = new Vector2(0,0);
        velocity = new Vector2(0,0);
        acceleration = new Vector2(0,0);

        tracerFrequency = 10;
        traces = new LinkedList<Vector2>();
    }
    public PhysicsObject(long x, long y, int mass, Vector2 velocity){
        posX = x;
        posY = y;
        this.mass = mass;
        force = new Vector2(0,0);
        this.velocity = velocity;
        acceleration = new Vector2(0,0);

        tracerFrequency = 10;
        traces = new LinkedList<Vector2>();
    }

    public void updateKinematics(float deltaTime){
        acceleration.x = force.x / mass;
        acceleration.y = force.y / mass;
        velocity.x += acceleration.x * deltaTime;
        velocity.y += acceleration.y * deltaTime;
        posX += velocity.x * deltaTime;
        posY += velocity.y * deltaTime;

        if(doTraces){
            timeElapsed += deltaTime;
            if (timeElapsed >= 1/(tracerFrequency * 1.0f)){
                addPointToTracers(posX, posY);
                timeElapsed = 0;
            }
        }
    }

    public void updateGravity(PhysicsObject obj, float deltaTime){

        Vector2 dir = new Vector2(0,0);
        float distPow2 = (float) (Math.pow(obj.getPosX() - posX,2) + Math.pow(obj.getPosY() - posY,2));
        dir.x = obj.getPosX() - posX;
        dir.y = obj.getPosY() - posY;

        dir.nor();

        float forceScale = ((mass * obj.getMass())/distPow2) * G;
        Vector2 gravityForce = new Vector2(dir.x * forceScale, dir.y * forceScale);
        if (distPow2 > Math.pow(minimalSimulationRadius,2)){
            force.add(gravityForce);
        }
    }
    public void updateFinish(){
        force.set(new Vector2(0, 0));
    }
    // setters, getters
    public void addPointToTracers(float x, float y){
        traces.add(new Vector2(x,y));
        System.out.println(traces.size());
        if (traces.size() > maxTracers){
            traces.remove(0);
        }
    }

    public void setDoTraces(boolean doTraces) {
        this.doTraces = doTraces;
    }

    public void setTracerFrequency(int tracerFrequency) {
        this.tracerFrequency = tracerFrequency;
    }

    public int getTracerFrequency() {
        return tracerFrequency;
    }

    public LinkedList<Vector2> getTraces() {
        return traces;
    }

    public void setAcceleration(Vector2 acceleration) {
        this.acceleration = acceleration;
    }

    public void setForce(Vector2 force) {
        this.force = force;
    }

    public void setMass(long mass) {
        this.mass = mass;
    }

    public void setPosX(long posX) {
        this.posX = posX;
    }

    public void setPosY(long posY) {
        this.posY = posY;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public long getMass() {
        return mass;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public Vector2 getForce() {
        return force;
    }

    public Vector2 getVelocity() {
        return velocity;
    }
}