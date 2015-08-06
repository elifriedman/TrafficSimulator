package edu.cooper;

import java.util.ArrayList;
import java.util.HashMap;

public class EventManager {

    HashMap<String,Road> roadmap;
    IndexMinPQ<Agent> eventManager;

    public EventManager() {
        roadmap = ConfigReader.initializeRoadNetwork("config/roadnet.csv");
        eventManager = ConfigReader.initializeRoutes("config/routelist.csv", roadmap, 30);
    }

    public void step() {
        Agent event = eventManager.minKey();
        String last = event.lastRoad();
        String next = event.nextRoad();
        if(!last.equals("start")) { // If this agent didn't just start travelling right now,...
            ArrayList<Agent> lastList = roadmap.get(last).agentList;
            lastList.remove(event); // ! O(N) time to remove !!
            // cost stuff...
            for(int i=0; i<lastList.size(); i++ ) {
//                lastList.get(i).
            }
        }
    }
    public static void main(String[] args) {
        EventManager em = new EventManager();
    }
}
