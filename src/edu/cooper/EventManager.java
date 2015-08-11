package edu.cooper;

import java.util.ArrayList;
import java.util.HashMap;

public class EventManager {

    double time;
    HashMap<String, Road> roadmap;
    IndexMinPQ<Agent> eventManager;
    ArrayList<Agent> agentlist;
    boolean started;
    boolean finished;

    public EventManager() {
        roadmap = ConfigReader.initializeRoadNetwork("config/roadnet.csv");
        eventManager = ConfigReader.initializeRoutes("config/routelist.csv", roadmap, 30);
        agentlist = (ArrayList<Agent>) roadmap.get("start").agentList.clone();
        time = 0;
        started = false;
        finished = false;
    }

    public Agent step() {
        started = true;
        if (eventManager.isEmpty()) {
            finished = true;
            return null;
        }
        Agent event = eventManager.minKey();
        double now = event.getTime();
        time = now;
        Road current = event.currentRoad();
        Road next = event.nextRoad();

        // this agent is leaving the current road
        if (!event.finished() && !current.equals(roadmap.get("start"))) { // If this agent didn't just start travelling right now,...
            current.agentList.remove(event); // TODO : make 'remove' faster than O(N) !!
            for (Agent onPrevRoad : current.agentList) {
                onPrevRoad.update(now);
            }
        }

        // this agent is entering the next road
        if (!event.finished()) {
            next.addAgent(event);
            event.advance(now);
            for (Agent onNextRoad : next.agentList) {
                onNextRoad.update(now);
            }
        }

        return event;
    }

    public static void main(String[] args) {
//        EventManager em = new EventManager();
//        while(!em.finished) {
//            em.step();
//        }
        fn1();
    }

    public static void fn1() {
        EventManager em = new EventManager();
        int i = 0;
        String[] roads = {"SO", "OP", "PM", "MN", "ND"};
        for (; !em.finished; i++) {
            em.step();
            double now = ((double) Math.round(em.time * 1000)) / 1000;
            System.out.println("___" + i + "___"+now);
            for (String road : roads) {
                double tt = ((double) Math.round(em.roadmap.get(road).avg_vel() * 1000)) / 1000;
                System.out.print(road + "("+tt+"): ");
                for (Agent a : em.roadmap.get(road).agentList) {
                    double t = ((double) Math.round(a.getTime() * 1000.0)) / 1000;
                    System.out.print("(" + a.ID + ", " + t + ")\t");
                }
                System.out.println("");
            }
            System.out.println("");
        }
        double total = 0;
        int n = 0;
        for (Agent a : em.agentlist) {
            total += a.getTotalTime();
            n += 1;
        }
        System.out.println(i + " iterations. " + n + " agents. " + total / n + " average time.");
    }
}
