/*
Henry Hu and Sahan Weeratunga
February 11, 2019
ICS4U - 01
This class is for everything pertaining to arrows.
*/

package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.audio.Sound;

public class arrow{
    //global variables that indicate whether the arrow would be on top or under an existing one
    public static int ABOVE = 1;
    public static int BELOW = 0;
    private int xPos=0,yPos=0;  //position
    private int mouse_x,mouse_y; //where the mouse is at the time of it's construction
    private int xSpeed=0,ySpeed=0; //speed will be calculated
    private float angle=0, power=0; //angle and power used will be calculated too
    private boolean stop; //false if the arrow is in flight, true if it's stopped
    private Vector2 frontPoint,backPoint; //the front and back points of the arrow
    private Sound bowRelease; //released sound
    private Sound arrowHit; //hit sound (only plays when it hits a player)

    public arrow(int xPos, int yPos, int mouse_x, int mouse_y, float power) {
        //needs an x and y position, the mouse distance relative to the player's position, scaled down, and the power given
        this.xPos = xPos;
        this.yPos = yPos;
        this.mouse_x = mouse_x;
        this.mouse_y = mouse_y;
        this.power = power;
        //calculates distance from (0,0) to mouse position, just used for scaling the arrow's flight down in the start
        double arcHypotenuse = Math.pow(Math.pow(mouse_x,2)+Math.pow(mouse_y,2),0.5);
        //first the arrow is scaled down, then it's stretched with power
        xSpeed = (int)(mouse_x/arcHypotenuse*(power/2));
        ySpeed = (int)(mouse_y/arcHypotenuse*(power/2));
        stop = false; //arrow will be flying
        bowRelease = Gdx.audio.newSound(Gdx.files.internal("core/assets/bowrelease.mp3"));
        arrowHit = Gdx.audio.newSound(Gdx.files.internal("core/assets/arrowhit.mp3"));
    }

    public arrow(arrow existing, int newX, int newY, int place){
        //using an existing arrow to create another (triple power-up)
        //needs the arrow it's based off of, the x and y, and whether it's on top or below
        xPos = newX;
        if (place == ABOVE) {
            yPos = newY+10; //y is a bit above
            xSpeed = existing.getXSpeed() + 10; //will go farther in the x direction
            ySpeed = existing.getYSpeed(); //y speed is the same
        }
        else if (place == BELOW){
            yPos = newY-10; //y is a bit below
            xSpeed = existing.getXSpeed() - 10; //will go less far in the x
            ySpeed = existing.getYSpeed(); //y speed is the same
        }
        stop = false; //arrow will be flying
    }

    public void setXSpeed(int newValue){ //sets a new x speed
        xSpeed = newValue;
    }
    public void setYSpeed(int newValue){ //sets a new y speed
        ySpeed = newValue;
    }
    public void setAngle(){ //changes the angle using x and y speed
        angle = (float)(Math.toDegrees(Math.atan2(ySpeed,xSpeed)));
    }
    public void setAngle(float newAngle){ //sets a new angle
        angle = newAngle;
    }
    public void setXPos(int newValue){ //sets a new x position
        xPos = newValue;
    }
    public void setYPos(int newValue){ //sets a new y position
        yPos = newValue;
    }
    public void setStop(){ //is called once the arrow has been stopped
        stop = true;
    }

    //returns the x and y
    public int getXPos(){
        return xPos;
    }
    public int getYPos(){
        return yPos;
    }
    //returns the speeds
    public int getXSpeed(){
        return xSpeed;
    }
    public int getYSpeed(){
        return ySpeed;
    }
    //returns the angle - in degrees
    public float getAngle(){
        return angle;
    }
    //returns the angle - in radians
    public double radiansAngle(){
        return Math.toRadians(angle);
    }

    //returns the front of the arrow using trigonometry
    public Vector2 getVectorF(){
        //the image is 50x13, first the centre of the arrow is taken, then trig is used to get to the front
        frontPoint = new Vector2(frontX(),frontY());
        return frontPoint;
    }
    public float frontX() { //gets and returns the x coordinate of the front of the arrow
        double frontX =  xPos + 25  + 25*(Math.cos(radiansAngle()));
        return (float)frontX;
    }
    public float frontY() { //gets and returns the y coordinate of the front of the arrow
        double frontY = yPos + 6.5 + 25*(Math.sin(radiansAngle()));
        return (float)frontY;
    }
    public boolean isStopped(){ //checks to see if the arrow is stopped
        return stop;
    }

    public void doing() { //releasing the arrow sound
        bowRelease.play(2f);
    }

    public void hitNoise() { //hitting a player sound
        try { //when the triple power up is hit and a player is hit, something goes wrong that was too demanding to fix
            arrowHit.play(2f);
        }
        catch (Exception e) {
        }
    }
}