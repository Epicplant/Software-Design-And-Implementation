package marvel.junitTests;
import marvel.MarvelParser;
import marvel.MarvelPaths;
import graph.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This class contains a set of test cases that can be used to test the implementation of the
   MarvelPaths Class.
 *
 */
public class marvelJunitTests {

    @Rule public Timeout globalTimeout = Timeout.seconds(30);

    //Should throw an illegalArgumentException if one of the nodes in findshortestpath doesn't exist
    @Test
    public void correctlyThrowsExceptionForNonExistantNodes() {

        ChrisGraph<String, String> newGraph = MarvelPaths.loadGraph("basicHeroGraph.csv");
        assertThrows(IllegalArgumentException.class, () -> {
            MarvelPaths.shortestPathBetweenHeroes(newGraph, "nonexistant hero", "A");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            MarvelPaths.shortestPathBetweenHeroes(newGraph, "A", "nonexistant hero");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            MarvelPaths.shortestPathBetweenHeroes(newGraph, "nonexistant hero 1", "nonexistant hero");
        });

    }

    //Should return null when no path exists
    @Test
    public void correctReturnWhenNoPathExists() {

        ChrisGraph<String, String> newGraph = MarvelPaths.loadGraph("someUnconnectedHeroes.csv");
        assertEquals(null, MarvelPaths.shortestPathBetweenHeroes(newGraph, "A", "C"));

    }

    // Should throw an illegalArgumentException if a file does not exist within loadGraph
    @Test
    public void loadCorretlyThrowsExceptionForNonExistantFiles() {

        assertThrows(IllegalArgumentException.class, () -> {
            ChrisGraph<String, String> newGraph = MarvelPaths.loadGraph("nonexistant.csv");
        });

    }

    // Should throw an illegalArgumentException if a file does not exist within parseData
    @Test
    public void parseCorrectlyThrowsExceptionForNonExistantFiles() {

        assertThrows(IllegalArgumentException.class, () -> {
            HashMap<String, List<String>> test = MarvelParser.parseData("nonexistant.csv");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            HashMap<String, List<String>> test = MarvelParser.parseData(null);
        });

    }


    //Should throw an exception if the graph or the nodes are null
    @Test
    public void correctlyThrowsExceptionForNullPaths() {
        ChrisGraph<String, String> testGraph = null;
        assertThrows(IllegalArgumentException.class, () -> {
            MarvelPaths.shortestPathBetweenHeroes(testGraph, "B", "A");
        });

        ChrisGraph<String, String> newGraph = MarvelPaths.loadGraph("someUnconnectedHeroes.csv");
        assertThrows(IllegalArgumentException.class, () -> {
            MarvelPaths.shortestPathBetweenHeroes(newGraph, null, "A");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            MarvelPaths.shortestPathBetweenHeroes(newGraph, "A", null);
        });


    }

    //Test ALL Things that SHOULDN'T happen

}
