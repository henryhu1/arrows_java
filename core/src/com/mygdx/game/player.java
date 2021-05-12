/*
Henry Hu and Sahan Weeratunga
February 11, 2019
ICS4U - 01
This class is for the player's fields and methods
*/

package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

public class player {
    private int posX,posY; //position
    private Rectangle playerRect; //its hit-box
    private int health=4; //starting health
    private int score=0; //starting score

    public player(int x, int y){
        posX = x;
        posY = y;
        //the image has some white space around it, so the hit-box is a little smaller than it
        playerRect = new Rectangle(x+9,y+7,60,130);
    }

    //where the arrow is shot from relative to the player, used mainly in the guide
    public int getArrowShotX() { //this doesn't get the actual shooting spot for player 2 but it's close enough
        return posX + 26;
    }
    public int getArrowShotY() {
        return posY + 65;
    }
    //getting the player's position
    public int getX(){
        return posX;
    }
    public int getY(){
        return posY;
    }
    public int getHealth(){return health;} //getting the player's health
    public Rectangle getPlayerRect(){return playerRect;} //getting the player's hit-box
    public int getScore(){
        return score;
    } //getting the player's score

    public void setY(int newY){ //only the y coordinate of the player will change in the game
        posY = newY;
        playerRect = new Rectangle(posX+9,posY+7,60,130); //which also changes it's rectangle
    }
    public void addHealth(int amount){ //adds health (can be positive or negative)
        health += amount;
        if (health>4){
            health = 4;
        }
    }
    public void setHealth(int amount) { //just used for resetting health
        health = amount;
    }
    public boolean checkDead(){ //checking if the player has more than 0 health
        if (health==0){
            return true;
        }
        else{
            return false;
        }
    }
    public void addPoints(int amount){ //add points whenever an arrow hits the opponent (just 1 point)
        score+=amount;
    }
}