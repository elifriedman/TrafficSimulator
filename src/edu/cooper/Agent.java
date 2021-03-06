package edu.cooper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by friedm3 on 8/6/15.
 */
public class Agent implements Comparable<Agent> {
    private double journeyStart;
    private double current_time;
    private double velocity;
    private Double distRemaining;
    private Double t;
    private Road[] route;
    private int curPos;
    final int ID;
    private IndexMinPQ manager;


    public Agent(int ID, double t)  {
        this.journeyStart = t;
        this.current_time = t;
        this.distRemaining = 0.0;
        this.velocity = 0;
        this.t = t;
        curPos = 1;
        this.ID = ID;
        this.manager = null;
        this.route = null;
    }

    /**
     * Choose from one of the routes in routelist
     * @param routelist a list of possible routes from which to choose
     */
    public void chooseRoute(ArrayList<Road[]> routelist) {
        // TODO create a choice function
    }
    public void chooseRoute(Road[] route) {
        this.route = route;
    }
    public void setManager(IndexMinPQ manager) {
        if(this.manager==null) {
            this.manager = manager;
            this.manager.insert(ID, this);
        }
    }

    public double getTime() {
        double t = this.t;
        if (this.velocity > 0) {
            t = this.distRemaining / this.velocity + this.current_time;
        }
        return t;
    }
    public double getTotalTime() { return this.t - this.journeyStart; }
    public double distanceLeft() { return this.distRemaining; }

    public void advance(double current_time) {
        if(curPos < this.route.length) curPos++;
        if( !finished() ) {
            this.current_time = current_time;
            this.distRemaining = this.currentRoad().roadlength;
            this.velocity = this.currentRoad().avg_vel();
            this.t = this.getTime();
            if(manager != null && manager.contains(ID))
                manager.changeKey(ID, this);
        }
    }
    public void update(double current_time) {
        if(!finished()) {
            double dist_travelled = (current_time - this.current_time)*this.velocity;
            this.distRemaining = this.distRemaining - dist_travelled;
            this.distRemaining = this.distRemaining > 0 ? this.distRemaining : 0; // make sure we still have some travelling to do
            this.velocity = this.currentRoad().avg_vel();
            this.current_time = current_time;
            this.t = this.getTime();
            manager.changeKey(ID, this);
        }
    }
    
    public Road nextRoad() {
        if(this.route==null) {
            System.err.println("Please call agent.chooseRoute in order to initialize this agent with a route.");
            System.exit(-1);
        }
        if(finished()) return this.route[this.curPos-1];
        return this.route[this.curPos];
    }
    public Road currentRoad() {
        if (this.route==null) {
            System.err.println("Please call agent.chooseRoute in order to initialize this agent with a route.");
            System.exit(-1);
        }
        return this.route[this.curPos-1]; }
    

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
        String ret = "At: " + this.t.toString() + ", " + currentRoad().toString();
        if(curPos+1<route.length) ret += " --> " + nextRoad().toString();
        return ret;
    }

}
