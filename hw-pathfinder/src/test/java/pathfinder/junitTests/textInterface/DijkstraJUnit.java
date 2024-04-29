package pathfinder.junitTests.textInterface;

import graph.ChrisGraph;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import pathfinder.DijkstraAlgorithm;
import pathfinder.datastructures.Path;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertThrows;

/**
 * This class contains a set of test cases that can be used to test the implementation of the
   DijkstraAlgorithm Class.
 *
 */
public class DijkstraJUnit {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(10);

    //Tests that Dijkstra's algorithm works with a plethora of different objects as node data
    @Test
    public void correctGenericDijkstraBehavior() {

        ChrisGraph<Integer, Double> newGraph = new ChrisGraph<>();

        newGraph.addNode(1);
        newGraph.addNode(2);
        newGraph.addEdge(1, 2, 4.5);

        Path<Integer> shortestPath = DijkstraAlgorithm.lowestWeightPath(newGraph, 1, 2);
        assertTrue(shortestPath.getCost() == 4.5);

        ChrisGraph<Boolean, Double> newGraphTwo = new ChrisGraph<>();

        newGraphTwo.addNode(true);
        newGraphTwo.addNode(false);
        newGraphTwo.addEdge(true, false, 6.0);

        Path<Boolean> shortestPathTwo = DijkstraAlgorithm.lowestWeightPath(newGraphTwo, true, false);
        assertTrue(shortestPathTwo.getCost() == 6.0);

    }


    //Should return null when no path exists
    @Test
    public void correctReturnWhenNoPathExists() {

        ChrisGraph<String, Double> newGraph = new ChrisGraph<>();

        newGraph.addNode("A");
        newGraph.addNode("C");

        Path<String> shortestPath = DijkstraAlgorithm.lowestWeightPath(newGraph, "A", "C");
        assertNull(shortestPath);

    }

    //Should throw an exception if the graph or the nodes are null
    @Test
    public void correctlyThrowsExceptionForNullPaths() {
        ChrisGraph<String, Double> testGraph = null;
        assertThrows(IllegalArgumentException.class, () -> {
            DijkstraAlgorithm.lowestWeightPath(testGraph, "B", "A");
        });

        ChrisGraph<String, Double> newGraph = new ChrisGraph<String, Double>() ;
        assertThrows(IllegalArgumentException.class, () -> {
            DijkstraAlgorithm.lowestWeightPath(newGraph, null, "A");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            DijkstraAlgorithm.lowestWeightPath(newGraph, "A", null);
        });


    }

}
