package edu.cooper;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by friedm3 on 8/6/15.
 */
public class Agent implements Comparable<Agent> {
    private double journeyStart;
    private Double roadStartT;
    private Double t;
    private final String[] route;
    private int curPos;
    final int ID;
    private IndexMinPQ manager;


    public Agent(int ID, double t,String[] route)  {
        this.journeyStart = t;
        this.roadStartT = t;
        this.t = t;
        this.route = route;
        curPos = 1;
        this.ID = ID;
        this.manager = null;
    }

    public void setManager(IndexMinPQ manager) {
        if(this.manager==null) {
            this.manager = manager;
            this.manager.insert(ID, this);
        }
    }
    public double getTime() { return this.t; }
    public double getTotalTime() { return this.t - this.journeyStart; }
    public double getRoadStartTime() { return this.roadStartT; }
    public void newEvent(double road_cost) { 
        if(road_cost >= 0) {
            this.roadStartT = this.t;
            this.t += road_cost;
            if(manager != null && manager.contains(ID)) 
                manager.changeKey(ID, this);
        }
        this.advance();
    }
    public void changeRoadCost(double road_cost, double current_time) {
        double new_arrival_time = this.roadStartT + road_cost;
        // make sure that our new travel time is in the future and not in the past.
        this.t = new_arrival_time > current_time ? new_arrival_time : current_time+0.1;
        if(!finished()) manager.changeKey(ID, this);
    }
    
    public String nextRoad() { 
        if(finished()) return this.route[this.curPos-1];
        return this.route[this.curPos]; 
    }
    public String currentRoad() { return this.route[this.curPos-1]; }
    
    public void advance() { if(curPos < route.length) curPos++; }
    public boolean finished() { 
        boolean finished = curPos >= route.length; // i.e. currentRoad() == "end"
        if(finished && manager.contains(ID)) {
            manager.delete(ID);
        }
        return finished; 
    }

    @Override
    public int compareTo(Agent a) {
        return this.t.compareTo(a.getTime());
    }

    @Override
    public String toString() {
        String ret = "At: " + this.t.toString() + ", " + route[curPos];
        if(curPos+1<route.length) ret += " --> " + route[curPos+1];
        return ret;
    }
}
