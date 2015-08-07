package edu.cooper;

import edu.cooper.Tests.Tests;
import java.util.ArrayList;
import java.util.HashMap;

public class EventManager {

    double time;
    HashMap<String, Road> roadmap;
    IndexMinPQ<Agent> eventManager;
    boolean started;
    boolean finished;

    public EventManager() {
        roadmap = ConfigReader.initializeRoadNetwork("config/roadnet.csv");
        eventManager = ConfigReader.initializeRoutes("config/routelist.csv", roadmap, 30);
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
        Agent first = em.eventManager.minKey();
        int i=0;
        for(; !em.finished; i++) em.step();
        double total = 0;
        int n = 0;
        for(Agent a : em.roadmap.get("end").agentList) {
            total += a.getTotalTime();
            n += 1;
        }
        
        System.out.println(i + " iterations. " + n + " agents. " + total/n + " average time.");
    }
}
