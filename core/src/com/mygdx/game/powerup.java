/*
Henry Hu and Sahan Weeratunga
February 11, 2019
ICS4U - 01
This small class is for keeping track of power-ups and checking if they've been hit
*/

package com.mygdx.game;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class powerup {
    //global fields accessed in other classes
    public static int triple = 1;
    public static int heal = 2;
    //type isn't used, but at first we thought it was a good idea to include it
    private int type;
    //object's x and y position
    private int x,y;
    //object's circle
    private Circle circ;

    public powerup(int type, int xPos, int yPos){
        this.type = type;
        x = xPos;
        y = yPos;
        circ = new Circle(x,y,20); //20 as radius
    }

    public int getXPos(){
        return x;
    } //gets the x coordinate
    public int getYPos(){
        return y;
    } //gets the y coordinate
    public Circle getCirc(){
        return circ;
    } //gets the power-up's circle

    public boolean checkInside(Vector2 point){ //checks if given point is inside the power-up's circle
        if (getCirc().contains(point)){
            return true;
        }
        else{
            return false;
        }
    }

}