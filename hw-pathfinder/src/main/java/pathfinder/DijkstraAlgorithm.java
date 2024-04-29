package pathfinder;

import graph.ChrisGraph;
import pathfinder.datastructures.Path;

import java.util.*;

/**
 * This class has the purpose of simulating Dijkstra's Algorithm on a ChrisGraph in order
   to find the path of the lowest value from a source node to a destination node. It is not an ADT.
 */
public class DijkstraAlgorithm {

    /**
     * This compares paths to each other so that they can be put in sorted order. Specifically compares 2 paths'
     * total costs to each other to determine which one is bigger and which one is smaller.
     */
    private static class PathComparator<E1> implements Comparator<Path<E1>> {

        /**
         * Returns a value indicating whether path p1 or path p2 is comparatively larger or smaller.
         * It does this by comparing the two paths' total costs.
         *
         * @param p1 The first path being compared
         * @param p2 The second path being compared
         * @requires p1 != null && p2 != null
         * @return Returns a negative int if p2 is bigger, 0 if p1 and p2 are the same,
        and a positive int if p1 is bigger
         */
        @Override
        public int compare(Path<E1> p1, Path<E1> p2) {

            Double costOne = p1.getCost();
            Double costTwo = p2.getCost();
            return costOne.compareTo(costTwo);

        }
    }



    /**
     * Uses Dijkstra's Algorithm to calculate the least cost path between 2 nodes storred in a ChrisGraph.
     *
     * @param <E1> Represents the data type that is representing the node abstraction
     * @param newGraph The graph that is being used to find a shortest path between nodes
     * @param start The start node that the path initially starts from
     * @param dest The dest node that the path initially wants to ends on
     * @throws IllegalArgumentException Throws if the inputted start nodes don't exist within the graph
       (including when null) or if the graph is itself null
     * @return Returns a path describing all edges we need to traverse to get from the start node to
       the dest node while also keeping track of the total cost to traverse this path. The returned
       path is the path from the start node to the dest node with the least cost.
     */
    public static <E1> Path<E1> lowestWeightPath(ChrisGraph<E1, Double> newGraph, E1 start, E1 dest) {

        //Throws and exception if any of the node params don't exist within the graph or the graph and/or nodes are null
        if(newGraph == null) {
            throw new IllegalArgumentException("The inputted graph was not a valid graph");
        } else if(!newGraph.graphContainsNode(start) && !newGraph.graphContainsNode(dest)) {
            throw new IllegalArgumentException(start + " and " + dest + " do not exist within the graph!");
        } else if(!newGraph.graphContainsNode(start)) {
            throw new IllegalArgumentException(start + " does not exist within the graph!");
        } else if (!newGraph.graphContainsNode(dest)) {
            throw new IllegalArgumentException(dest + " does not exist within the graph!");
        }

        // active = paths we are currently calculating
        PriorityQueue<Path<E1>> active = new PriorityQueue<>(new PathComparator<E1>());

        // finished = set of nodes for which we know the minimum-cost path from start.
        Set<E1> finished = new HashSet<>();

        //initializes the path from the first node to itself so that it is clear it costs 0 to arrive at the start node
        //from the start node
        Path<E1> initialPath = new Path<>(start);
        initialPath.extend(start, 0.0);
        active.add(initialPath);


        //while there are still active paths that could lead to shorter paths to various nodes keep running
        //{inv: All paths found so far are the shortest paths}
        while(!active.isEmpty()) {

            //The shortest cost path that we are currently analyzing as a viable option to reach nodes
            Path<E1> minPath = active.remove();
            E1 minDest = minPath.getEnd();

            //if the minimum path ends on the destination node then this has to have been the shortest path
            //that actually reaches the destination so return it
            if(minDest.equals(dest)) {
                return minPath;
            } else if(!finished.contains(minDest)) { //If a shortest path hasn't already been found for this node enter


                //Iterates through all of the children of minDes
                for(ChrisGraph.Node<E1, Double> edge : newGraph.nodesChildren(minDest)) {

                    //If the child is already part of the finished set then there is no point to make a new path to it
                    if(!finished.contains(edge.getNodeData())) {

                        Path<E1> newPath = minPath.extend(edge.getNodeData(), edge.getEdgeData());
                        active.add(newPath);

                    }

                }

                //add the minDest to finished since now that it has processed all of its children
                //and added them to active it is no longer needed to calculate minimum paths
                finished.add(minDest);
            }
        }

        // If the loop terminates, then no path exists from start to dest.
        // The implementation should indicate this to the client.

        return null;
    }

}
