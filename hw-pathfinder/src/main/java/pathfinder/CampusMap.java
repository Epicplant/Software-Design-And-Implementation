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

package pathfinder;

import graph.ChrisGraph;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import pathfinder.parser.CampusBuilding;
import pathfinder.parser.CampusPath;
import pathfinder.parser.CampusPathsParser;

import java.util.*;

/**
 * This functions contains the functionality needed to run the pathfinder application behind
   the scenes
 */
public class CampusMap implements ModelAPI {

    //A graph where building locations are nodes and path distances between buildings
    //are edges
    private final ChrisGraph<Point, Double> buildingsGraph;

    //A map where the short version of building names are keys and the long versions are the values
    private final Map<String, String> buildingNames;

    private final boolean DEBUG = false;


    // Abstraction Function:
    // For any given CampusMap g, there is a map whose keys represent all UW buildings' abbreviated
    // names and whose data values represent that buildings full name. Additionally, there is a
    // ChrisGraph whose nodes represent the coordinate points of a building from the UW and
    // whose edges represented paths from each buildings' coordinates to other buildings' coordinates.
    //
    // Representation Invariant:
    // buildingNames != null
    // buildingsGraph != null
    //  for(CampusBuilding building : CampusPathsParser.parseCampusBuildings("campus_buildings.csv")) {
    //      buildingNames.get(building.getShortName()) == building.getLongName());
    //  }
    //
    // for(String building : buildingNames.keySet()) {
    //      buildingNames.get(building) != null;
    // }



    /**
     * Constructor for the CampusMap ADT.
     *
     * @spec.effects Initializes buildingsGraph and buildingNames so that buildingsGraph contains all the buildings from
     * campus_paths and so that buildingNames has all the names from campus_buildings
     */
    public CampusMap() {

        buildingsGraph = new ChrisGraph<>();

        List<CampusPath> paths = CampusPathsParser.parseCampusPaths("campus_paths.csv");

        //Create a graph where the nodes are all possible locations you can go and then adds
        //paths between all of them pertaining where they exist.
        for(CampusPath path : paths) {

            Point srcPoint = new Point(path.getX1(), path.getY1());
            Point destPoint = new Point(path.getX2(), path.getY2());

            buildingsGraph.addNode(srcPoint);
            buildingsGraph.addNode(destPoint);
            buildingsGraph.addEdge(srcPoint, destPoint, path.getDistance());
            buildingsGraph.addEdge(destPoint, srcPoint, path.getDistance());
        }


        buildingNames = new HashMap<>();

        for(CampusBuilding building : CampusPathsParser.parseCampusBuildings("campus_buildings.csv")) {
            buildingNames.put(building.getShortName(), building.getLongName());
        }

        checkRep();
    }


    /**
     * Throws an exception if the representation invariant is violated.
     */
    private void checkRep() {

        assert(buildingNames != null) : "The buildingNames map was not correctly initialized";
        assert(buildingsGraph != null) : "The buildings graph 'newGraph' was not correctly initialized";

        if(DEBUG) {

            for (CampusBuilding building : CampusPathsParser.parseCampusBuildings("campus_buildings.csv")) {
                assert (buildingNames.get(building.getShortName()).equals(building.getLongName()));
            }

            for (String building : buildingNames.keySet()) {
                assert (buildingNames.get(building) != null);
            }
        }
    }



    @Override
    public boolean shortNameExists(String shortName) {

        checkRep();

        boolean returner = buildingNames.containsKey(shortName);

        checkRep();

        return returner;
    }

    @Override
    public String longNameForShort(String shortName) {

        checkRep();

        if(!buildingNames.containsKey(shortName)) {
            throw new IllegalArgumentException("The short name provided does not exist in the map");
        } else {

            String returner = buildingNames.get(shortName);
            checkRep();
            return returner;
        }
    }

    @Override
    public Map<String, String> buildingNames() {
        checkRep();
        return buildingNames;
    }

    @Override
    public Path<Point> findShortestPath(String startShortName, String endShortName) {

        checkRep();
        //parse the campus path and building data into lists and create a graph that will hold them
        List<CampusBuilding> buildings = CampusPathsParser.parseCampusBuildings("campus_buildings.csv");

        Point startPoint = null;
        Point endPoint = null;

        //Find the coordinates of the start and end destination
        for(CampusBuilding building : buildings) {
            if(building.getShortName().equals(startShortName)) {
                startPoint = new Point(building.getX(), building.getY());
            }

            if(building.getShortName().equals(endShortName)) {
                endPoint = new Point(building.getX(), building.getY());
            }
        }

        //Throws exception if the params were null of if the params weren't actually buildings on campus
        if(startPoint == null || endPoint == null) {
            throw new IllegalArgumentException("One or both of the building names don't exist on the map");
        }


        //Runs Dijkstra's Algorithm and then returns the least cost path from start to dest.
        Path<Point> returner = DijkstraAlgorithm.lowestWeightPath(buildingsGraph, startPoint, endPoint);

        checkRep();
        return returner;

    }
}