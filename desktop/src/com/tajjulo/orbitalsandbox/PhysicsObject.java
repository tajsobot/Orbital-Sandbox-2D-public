package com.tajjulo.orbitalsandbox;

import com.badlogic.gdx.math.Vector2;

import java.util.Vector;

public class PhysicsObject {

    private long posX;
    private long posY;

    private long mass;
    private Vector2 force;

    private Vector2 velocity;
    private Vector2 acceleration;

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
    }

    // setters, getters

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

    public long getPosX() {
        return posX;
    }

    public long getPosY() {
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
