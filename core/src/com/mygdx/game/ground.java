/*
Henry Hu and Sahan Weeratunga
February 11, 2019
ICS4U - 01
This class is for storing the ground's data and getting it's info
*/

package com.mygdx.game;

import java.util.*;

public class ground {
    private Random r = new Random(); //used to generate random segments
    //x coordinates already contain the two bottom corners, the right and left most points, and 100 pixels from those
    private ArrayList<Integer> segmentsX = new ArrayList<Integer>(Arrays.asList(0,0,100,924,1024,1024));
    //y coordinates start with the bottom left
    private ArrayList<Integer> segmentsY = new ArrayList<Integer>(Arrays.asList(0));
    //will combine x and y coordinates and be turned into a float array for creating a polygon
    private ArrayList<Integer> verticesAL = new ArrayList<Integer>();
    private int segmentNum; //random number of segments

    public ground(){
        segmentNum = r.nextInt(10)+5;
        for (int i=0; i<segmentNum; i++){ //for the number of segments
            segmentsX.add(r.nextInt(784)+120);
        }
        Collections.sort(segmentsX); //lowest to highest
        for (int i=0; i<segmentsX.size()-2; i++) { //loops through 2 less of the x coordinates size, since 2 values will
            //be manually added - the bottom left and right points
            if (i == 1) { //the second added y value will be the same as the first to create a platform
                segmentsY.add(segmentsY.get(1));
            }
            else if (i == segmentsX.size()-3) { //the last added y value will be the same as the previous, also for a platform
                segmentsY.add(segmentsY.get(segmentsY.size()-1));
            }
            else { //random numbers
                segmentsY.add(r.nextInt(300) + 10);
            }
        }
        segmentsY.add(0); //manually add the bottom right corner
        for (int i=0; i<segmentsX.size(); i++){ //adding the x and corresponding y values
            verticesAL.add(segmentsX.get(i));
            verticesAL.add(segmentsY.get(i));
        }
    }

    //all the x and y coordinates and will be used in the game class as their own fields
    public ArrayList<Integer> xCoords(){
        return segmentsX;
    }
    public ArrayList<Integer> yCoords(){
        return segmentsY;
    }

    public float[] polygonVertices(){ //creates and returns a float array for the x and y's, used for creating a polygon
        float[] verticesA = new float[verticesAL.size()]; //the returning float array
        int counter = 0; //for iterating through the x and y ArrayList
        for (int i=0; i<verticesA.length; i+=2){ //loops through the ArrayList by 2s
            verticesA[i] = segmentsX.get(counter);
            verticesA[i+1] = segmentsY.get(counter);
            counter++; //counter just goes up by 1 to access every single x and y
        }
        return verticesA;
    }
}