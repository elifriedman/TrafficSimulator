package edu.cooper;

import edu.cooper.Tests.Tests;
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
        String current = event.currentRoad();
        String next = event.nextRoad();

        // this agent is leaving the current road
        if (!event.finished() && !current.equals("start")) { // If this agent didn't just start travelling right now,...
            roadmap.get(current).agentList.remove(event); // TODO : make 'remove' faster than O(N) !!
            double newtt = roadmap.get(current).cost();
            for (Agent onPrevRoad : roadmap.get(current).agentList) {
                onPrevRoad.changeRoadCost(newtt, now);
            }
        }

        // this agent is entering the next road
        if (!event.finished()) {
            roadmap.get(next).addAgent(event);
            double newtt = roadmap.get(next).cost();
            event.newEvent(newtt); // put the agent back in the Priority Queue with the TT of the next road
            for (Agent onNextRoad : roadmap.get(next).agentList) {
                onNextRoad.changeRoadCost(newtt, now);
            }
        }

        return event;
    }

    public static void main(String[] args) {
        EventManager em = new EventManager();
        int i = 0;
        String[] roads = {"SO", "OP", "PM", "MN", "ND"};
        for (; !em.finished; i++) {
            em.step();
            double now = ((double) Math.round(em.time * 1000)) / 1000;
            System.out.println("___" + i + "___"+now);
            for (String road : roads) {
                double tt = ((double) Math.round(em.roadmap.get(road).cost() * 1000)) / 1000;
                System.out.print(road + "("+tt+"): ");
                for (Agent a : em.roadmap.get(road).agentList) {
                    double st = ((double) Math.round(a.getRoadStartTime() * 1000)) / 1000;
                    double t = ((double) Math.round(a.getTime() * 1000.0)) / 1000;
                    System.out.print("(" + a.ID + ", " + st + ", " + t + ")\t");
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
