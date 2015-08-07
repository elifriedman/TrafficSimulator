/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.cooper.Tests;
import edu.cooper.*;
/**
 *
 * @author EliFriedman
 */
public class Tests {
    public static void testAgent() {
        IndexMinPQ man = new IndexMinPQ(5);
        String[] route1 = {"start","AB","BC","CD","end"};
        String[] route2 = {"start","AB","BD","end"};
        String[] route3 = {"start","AD","end"};
        double[] start = {0, 3, 0.4, 2, 1};
        String[][] routes = {route1, route2, route1, route3, route2};
        Agent[] list = new Agent[5];
        for(int i=0;i<5;i++) {
            list[i] = new Agent(i,start[i],routes[i]); list[i].setManager(man);   
        }
        assert man.delMin() == 0;
        assert man.delMin() == 2;
        list[3].advance();
        boolean finished = list[3].finished();
        assert finished;
        assert man.contains(3)==false;
        
        list[4].newEvent(4);
        list[1].advance();list[1].advance();list[1].advance();
        assert list[1].finished();
        assert !list[4].finished();
        list[4].advance();list[4].advance();list[4].advance();
        assert list[4].finished();
        assert man.isEmpty();
    }
    
    public static void testinitRoutes(IndexMinPQ<Agent> eventManager) {
        assert eventManager.size()==26;
        assert eventManager.minKey().getTime()==0.0;
        assert eventManager.keyOf(25).getTime()==45;
        assert eventManager.keyOf(25).nextRoad().equals("SM");
        eventManager.keyOf(25).advance();
        assert eventManager.keyOf(25).nextRoad().equals("MP");
    }
}
