package edu.cooper;

/**
 * Created by friedm3 on 8/6/15.
 */
public class Agent implements Comparable<Agent> {
    private Double t;
    private String[] route;
    private int curPos;
    int ID;


    public Agent(double t,String[] route)  {
        this.t = new Double(t);
        this.route = route;
        curPos = 1;
    }

    public Double getTime() {
        return t;
    }
    public String nextRoad() {
        return this.route[this.curPos];
    }
    public String lastRoad() {
        return this.route[this.curPos-1];
    }
    public void advance() {
        if(curPos < route.length-1) curPos++;
    }

    public boolean finished() {
        return curPos >= route.length-1;
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
