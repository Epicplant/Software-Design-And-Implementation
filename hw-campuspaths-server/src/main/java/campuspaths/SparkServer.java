/*
 * Copyright (C) 2023 Hal Perkins.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Winter Quarter 2023 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package campuspaths;

import campuspaths.utils.CORSFilter;
import com.google.gson.Gson;
import pathfinder.CampusMap;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import spark.*;

public class SparkServer {

    /**
     * The main method of the program. Hosts a spark server at port 4567 and holds 2 routes: campus-buildings and
       campus-path. Campus-buildings simply returns the results of CampusMap.buildingNames() and campus-path
       simply calls CampusMap.findShortestPath where the URL queries 'start' and 'end' represent
       the 'start' and 'end' parameters of findShortestPath.
     *
     * @param args The command line application arguments for this function
     */
    public static void main(String[] args) {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();
        // The above two lines help set up some settings that allow the
        // React application to make requests to the Spark server, even though it
        // comes from a different server.
        // You should leave these two lines at the very beginning of main().

        // Creates an instance of the CampusMap
        CampusMap campusBuildings = new CampusMap();

        // #1 Return names of the buildings for the dropdown menu to generate options
        Spark.get("/campus-buildings", new Route() {
            @Override
            public Object handle(Request request, Response response) {

                //Turns campusBuildings.buildingNames() into a json object and then returns it
                Gson gson = new Gson();
                String jsonResponse = gson.toJson(campusBuildings.buildingNames());
                return jsonResponse;
            }
        });



        //#2 Returns shortest path between two buildings in campus-path

        Spark.get("/campus-path", new Route() {
            @Override
            public Object handle(Request request, Response response) {

                // Creates route queries start and end
                String start = request.queryParams("start");
                String end = request.queryParams("end");

                // If the queries start and end aren't present end the server
                if(start == null || end == null) {
                    Spark.halt(400, "must have start building and an end building");
                }

                // Creates a new shortestPath
                Path<Point> shortestPath = null;

                try {
                    // Uses Dijkstra's to find the shortest path between two buildings
                    shortestPath = campusBuildings.findShortestPath(start, end);
                } catch(IllegalArgumentException exc) {
                    // If either of the buildings we are finding a path between don't exist, then
                    // an illegal arggument is thrown so we halt the spark process.
                    Spark.halt(400, "start and end must be valid building names");
                }

                // Converts the path object to JSON.
                Gson gson = new Gson();
                String jsonResponse = gson.toJson(shortestPath);
                return jsonResponse;
            }
        });
    }
}
