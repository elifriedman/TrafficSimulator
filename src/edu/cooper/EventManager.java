package edu.cooper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class EventManager {

    double time;
    HashMap<String, Road> roadmap;
    IndexMinPQ<Agent> eventManager;
    ArrayList<Agent> agentlist;
    ArrayList<Road[]> routelist;
    boolean started;
    boolean finished;

    public EventManager() {
        roadmap = ConfigReader.initializeRoadNetwork("config/roadnet.csv");
        routelist = ConfigReader.initializeRouteList("config/routelist.csv",roadmap);
        eventManager = ConfigReader.initializeAgents("config/agentlist.csv", roadmap, 30);
        agentlist = (ArrayList<Agent>) roadmap.get("start").agentList.clone();
        time = 0;
        started = false;
        finished = false;
    }

    public int[] initializeAgents(int[] routechoices) {
        int count=0;
        for(int i=0; i<routechoices.length; i++) {
            for(int j=0; j<routechoices[i]; j++) {
                agentlist.get(count+j).chooseRoute(routelist.get(i));
            }
            count += routechoices[i];
        }
        return routechoices;
    }
    public int[] initializeAgents() {
        int[] routechoices = new int[routelist.size()];
        Random rng = new Random();
        int total = agentlist.size();
        for(int i=0; i<routechoices.length-1; i++) {
            int r = rng.nextInt(total+1);
            routechoices[i] = r;
            total = total - r;
        }
        routechoices[routechoices.length-1] = total;

        int count=0;
        for(int i=0; i<routechoices.length; i++) {
            for(int j=0; j<routechoices[i]; j++) {
                agentlist.get(count+j).chooseRoute(routelist.get(i));
            }
            count += routechoices[i];
        }
        return routechoices;
    }
    public double averageTime() {
        int n = 0;
        double total = 0;
        for (Agent a : this.agentlist) {
            total += a.getTotalTime();
            n += 1;
        }
        return total / n;
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
        String filename = "learning_data";
        int N = 1000;
        try (BufferedWriter file = new BufferedWriter(new FileWriter(filename))) {
            for(int j=0; j<N; j++) {
                if(j%100==0) System.out.println(j);
                EventManager em = new EventManager();
                int[] choices = em.initializeAgents();
                while (!em.finished) em.step();
                String write = "";
                for (int i = 0; i < choices.length; i++) {
                    write += choices[i] + ",";
                }
                write += String.valueOf(em.averageTime()) + "\n";
                file.write(write);
            }
        } catch (Exception e) {}
    }

    public static void fn2() {
        EventManager em = new EventManager();
        em.initializeAgents();
        int i=0;
        while(!em.finished) {
            Agent a = em.step();
            if(em.agentlist.get(0).equals(a)) {
                System.out.println();
                System.out.print("("+a.currentRoad()+") ");
            }
            System.out.print(em.agentlist.get(0).distanceLeft() + " ");
        }
    }
    public static void fn1() {
        EventManager em = new EventManager();
        em.initializeAgents();
        int i = 0;
        String[] roads = {"SO", "SM","OP", "PM", "MN","PD", "ND"};
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
