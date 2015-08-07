package edu.cooper;

import java.util.ArrayList;

/**
 * Created by friedm3 on 8/6/15.
 */
public class Road {
    ArrayList<Agent> agentList;
    String name;
    int capacity;
    float fftt; // free flow travel time

    public Road(String name,int capacity, float freeflowtraveltime) {
        this.name = name;
        this.capacity = capacity;
        this.fftt = freeflowtraveltime;
        agentList = new ArrayList<>();
    }

    public void addAgent(Agent a) {
        agentList.add(a);
    }

    public double cost() {
        if("start".equals(name) || "end".equals(name)) return 0;
        return Road.cost(this.fftt,this.capacity,this.agentList.size());
    }
    
    public int numCars() {
        return this.agentList.size();
    }

    public static double alpha = .15;
    public static double beta = 4;
    public static double cost(double freeflowtraveltime, double capacity, int num_cars) {
        return freeflowtraveltime * ( 1 + Road.alpha*Math.pow(num_cars/capacity,Road.beta) );
    }
}
