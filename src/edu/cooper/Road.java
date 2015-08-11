package edu.cooper;

import java.util.ArrayList;

/**
 * Created by friedm3 on 8/6/15.
 */
public class Road {
    ArrayList<Agent> agentList;
    String name;
    int capacity;
    double roadlength; // road length

    public Road(String name,int capacity, float roadlength) {
        this.name = name;
        this.capacity = capacity;
        this.roadlength = roadlength;
        agentList = new ArrayList<>();
    }

    public void addAgent(Agent a) {
        agentList.add(a);
    }

    public double cost() {
        if("start".equals(name) || "end".equals(name)) return 0;
        return Road.cost(this.roadlength, this.capacity, this.agentList.size());
    }

    public double avg_vel() {
        if("start".equals(name) || "end".equals(name)) return 0;
        double traveltime = Road.cost(this.roadlength, this.capacity, this.agentList.size());
        return this.roadlength/traveltime;
    }
    
    public int numCars() {
        return this.agentList.size();
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Road)) return false;
        Road oRoad = (Road) other;
        if (this.name.equals(oRoad.name) && this.capacity==oRoad.capacity && this.roadlength==oRoad.roadlength) return true;
        return false;
    }

    public static double alpha = .15;
    public static double beta = 4;
    public static double cost(double freeflowtraveltime, double capacity, int num_cars) {
        return freeflowtraveltime * ( 1 + Road.alpha*Math.pow(num_cars/capacity,Road.beta) );
    }
}
