package com.tajjulo.orbitalsandbox.game;

import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;

public class PhysicsObject {

    private final float G = 1000;
    private final float maxDeltaTime = 0.05f;
    private String name;
    private float posX;
    private float posY;

    private float mass;
    private Vector2 force;

    private Vector2 velocity;
    private Vector2 acceleration;

    private boolean isStatic;
    private float timeElapsed;
    private float minimalSimulationRadius = 20;
    private int tracerFrequency = 144; //in Hz
    private boolean doTraces = true;
    private int maxTracers = 500;
    private LinkedList<Vector2> traces;

    //za izrisovanje
    private Vector2 forceForDrawing;
    private float density; // dL*M
    private float planetRadius;
    private final float minimumPlanetRadius = 10;
    private boolean exertGravity;

    private boolean doCollisions = true;

    //constructors
    public PhysicsObject(){
        posY = 0;
        posX = 0;
        mass = 1;
        force = new Vector2(0,0);
        forceForDrawing = new Vector2(0,0);
        velocity = new Vector2(0,0);
        acceleration = new Vector2(0,0);
        this.isStatic = false;
        density = 1;
        updatePlanetRadius();
        name = "physics object";
    }

    public PhysicsObject(long x, long y){
        posX = x;
        posY = y;
        mass = 1;
        force = new Vector2(0,0);
        forceForDrawing = new Vector2(0,0);
        velocity = new Vector2(0,0);
        acceleration = new Vector2(0,0);
        traces = new LinkedList<Vector2>();
        this.isStatic = false;
        density = 1;
        updatePlanetRadius();
        name = "physics object";
    }

    public PhysicsObject(float x, float y, int mass, Vector2 velocity, boolean isStatic, float densety){
        posX = x;
        posY = y;
        this.mass = mass;
        force = new Vector2(0,0);
        forceForDrawing = new Vector2(0,0);
        this.velocity = velocity;
        acceleration = new Vector2(0,0);
        traces = new LinkedList<Vector2>();
        this.isStatic = isStatic;
        this.density = densety;
        updatePlanetRadius();
        name = "physics object";
        exertGravity = true;
    }

    public PhysicsObject(float x, float y, int mass, Vector2 velocity, boolean exertG){
        posX = x;
        posY = y;
        this.mass = mass;
        force = new Vector2(0,0);
        forceForDrawing = new Vector2(0,0);
        this.velocity = velocity;
        acceleration = new Vector2(0,0);
        traces = new LinkedList<Vector2>();
        this.isStatic = false;
        density = 1;
        updatePlanetRadius();
        name = "physics object";
        exertGravity = exertG;
    }

    public PhysicsObject(float x, float y, int mass, Vector2 velocity, boolean exertG, boolean doCollisions){
        posX = x;
        posY = y;
        this.mass = mass;
        force = new Vector2(0,0);
        forceForDrawing = new Vector2(0,0);
        this.velocity = velocity;
        acceleration = new Vector2(0,0);
        traces = new LinkedList<Vector2>();
        this.isStatic = false;
        density = 1;
        updatePlanetRadius();
        name = "physics object";
        exertGravity = exertG;
        this.doCollisions = doCollisions;
    }

    public void updateKinematics(float deltaTime){
        if(!isStatic) {
            forceForDrawing.set(force);
            acceleration.x = force.x / (float) mass;
            acceleration.y = force.y / (float) mass;
            velocity.x += acceleration.x * deltaTime;
            velocity.y += acceleration.y * deltaTime;
            posX += velocity.x * deltaTime;
            posY += velocity.y * deltaTime;
        }
        if(doTraces){
            timeElapsed += Math.abs(deltaTime);
            if (timeElapsed >= 1/(tracerFrequency * 1.0f)){
                addPointToTracers(posX, posY);
                timeElapsed = 0;
            }
        }

    }

    public void updateGravity(PhysicsObject obj, float deltaTime){
        if(exertGravity){

        }
        Vector2 dir = new Vector2(0,0);
        float distPow2 = (float) (Math.pow(obj.getPosX() - posX,2) + Math.pow(obj.getPosY() - posY,2));
        dir.x = obj.getPosX() - posX;
        dir.y = obj.getPosY() - posY;
        dir.nor();

        float forceScale = (((float)mass * (float)obj.getMass())/distPow2) * G;
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

    public void setMass(float mass) {
        this.mass = mass;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public float getMass() {
        return mass;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public boolean getIsStatic() {
        return isStatic;
    }

    public float getDensity() {
        return density;
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

    public Vector2 getForceForDrawing() {
        return forceForDrawing;
    }

    public float getPlanetRadius() {
        return planetRadius;
    }

    public void updatePlanetRadius(){
        planetRadius = 5f/(float)Math.cbrt((density * Math.PI* 3f)/(mass * 4f));
    }
    public void addVelocity(Vector2 v, float scalar){
        velocity.mulAdd(v , scalar);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getExertGravity() {
        return exertGravity;
    }

    public boolean isDoCollisions() {
        return doCollisions;
    }

    public void setDoCollisions(boolean doCollisions) {
        this.doCollisions = doCollisions;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }
}