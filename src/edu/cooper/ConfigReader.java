package edu.cooper;

import java.io.*;
import java.util.*;

/**
 * Created by friedm3 on 8/6/15.
 */
public class ConfigReader {
    public static HashMap<String,Road> initializeRoadNetwork(String filename) {
        HashMap<String,Road> ht = new HashMap<>(); // each road contains the list of agents currently using that road
        ht.put("start",new Road("start",0,0)); // a place to put agents before they start on their journey
        ht.put("end",new Road("end",0,0)); // a place to put agents after they finish their journey
        try {
            Scanner s = new Scanner(new File(filename));
            s.useDelimiter("[,\\n]");
            s.nextLine();
            while(s.hasNext()) {
                String roadname = s.next() + s.next();
                float freeflowtraveltime = s.nextFloat();
                int capacity = s.nextInt();
                ht.put(roadname, new Road(roadname,capacity,freeflowtraveltime));
            }
            s.close();
        } catch (FileNotFoundException fe) {

        } catch (InputMismatchException im) {
            System.err.println("ConfigReader.initializeRoadNetwork: Please check that file '" + filename+ "' is in the correct place and has the correct format.");
            System.exit(-1);
        }
        return ht;
    }

    public static IndexMinPQ<Agent> initializeRoutes(String filename, HashMap<String,Road> agentHolder, double timewindow) {
        Random rng = new Random();

        //Delimiter used in CSV file
        final String DELIMITER = ",";
        try (BufferedReader fileReader = new BufferedReader(new FileReader(filename))){
            fileReader.readLine(); // skip first line

            String line = "";
            while ((line = fileReader.readLine()) != null) {
                //Get all tokens available in line
                String[] tokens = line.split(DELIMITER);
                int num_agents = Integer.parseInt(tokens[0]);
                String[] route = new String[tokens.length];
                route[0] = "start";
                route[route.length - 1] = "end";
                for (int i = 1; i < tokens.length - 1; i++) {
                    route[i] = tokens[i] + tokens[i + 1];
                }
                for(int i=0;i<num_agents;i++) {
                    double start_time = rng.nextDouble()*timewindow;
                    Agent a = new Agent(start_time,route);
                    agentHolder.get("start").addAgent(a);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("ConfigReader.initializeRoute: Check that file '" + filename + "' is in the correct place and has the correct format");
            System.exit(-1);
        }
        ArrayList<Agent> agents = agentHolder.get("start").agentList;
        IndexMinPQ<Agent> eventManager = new IndexMinPQ<>(agents.size());
        for(int i=0;i<agents.size();i++) {
            agents.get(i).ID = i;
            eventManager.insert(i,agents.get(i));
        }
        return eventManager;
    }
}
