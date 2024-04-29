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

package marvel.scriptTestRunner;

import graph.ChrisGraph;
import marvel.MarvelPaths;

import java.io.*;
import java.util.*;


/**
 * This class implements a testing driver which reads test scripts from
 * files for testing Graph, the Marvel parser, and your BFS algorithm.
 */
public class MarvelTestDriver {

    /**
     * String -> Graph: maps the names of graphs to the actual graph
     **/

    private final Map<String, ChrisGraph<String, String>> graphs = new HashMap<>();
    private final PrintWriter output;
    private final BufferedReader input;



    // Leave this constructor public
    public MarvelTestDriver(Reader r, Writer w) {
        // See GraphTestDriver as an example.
        input = new BufferedReader(r);
        output = new PrintWriter(w);

    }


    /**
     * @throws IOException if the input or output sources encounter an IOException
     * @spec.effects Executes the commands read from the input and writes results to the output
     **/
    // Leave this method public
    public void runTests() throws IOException {
        String inputLine;
        while((inputLine = input.readLine()) != null) {
            if((inputLine.trim().length() == 0) ||
                    (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            } else {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if(st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<>();
                    while(st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }

                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    private void executeCommand(String command, List<String> arguments) {
        try {
            switch(command) {
                case "CreateGraph":
                    createGraph(arguments);
                    break;
                case "AddNode":
                    addNode(arguments);
                    break;
                case "AddEdge":
                    addEdge(arguments);
                    break;
                case "ListNodes":
                    listNodes(arguments);
                    break;
                case "ListChildren":
                    listChildren(arguments);
                    break;
                case "LoadGraph":
                    loadGraph(arguments);
                    break;
                case "FindPath":
                    findPath(arguments);
                    break;
                default:
                    output.println("Unrecognized command: " + command);
                    break;
            }
        } catch(Exception e) {
            String formattedCommand = command;
            formattedCommand += arguments.stream().reduce("", (a, b) -> a + " " + b);
            output.println("Exception while running command: " + formattedCommand);
            e.printStackTrace(output);
        }
    }

    private void createGraph(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {

        graphs.put(graphName, new ChrisGraph<>());
        output.println("created graph " + graphName);
        // graphs.put(graphName, ___);
        // output.println(...);
    }

    private void addNode(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to AddNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {

        ChrisGraph<String, String> newGraph = graphs.get(graphName);
        newGraph.addNode(nodeName);
        output.println("added node " + nodeName + " to " + graphName);

        // ___ = graphs.get(graphName);
        // output.println(...);
    }

    private void addEdge(List<String> arguments) {
        if(arguments.size() != 4) {
            throw new CommandException("Bad arguments to AddEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        String edgeLabel = arguments.get(3);

        addEdge(graphName, parentName, childName, edgeLabel);
    }

    private void addEdge(String graphName, String parentName, String childName,
                         String edgeLabel) {

        ChrisGraph<String, String> newGraph = graphs.get(graphName);
        newGraph.addEdge(parentName, childName, edgeLabel);
        output.println("added edge " + edgeLabel + " from " + parentName +
                " to " + childName + " in " + graphName);
        // ___ = graphs.get(graphName);
        // output.println(...);
    }

    private void listNodes(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to ListNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {

        ChrisGraph<String, String> newGraph = graphs.get(graphName);

        List<String> nodes = newGraph.listAllNodes();
        Collections.sort(nodes);

        String printStatement = graphName + " contains:";

        for(String node : nodes) {
            printStatement = printStatement + " " + node;
        }

        output.println(printStatement);
        // ___ = graphs.get(graphName);
        // output.println(...);
    }

    private void listChildren(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to ListChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }


    //This compares nodes to each other so that they can be put in sorted order
    private static class NodeComparator<E1 extends Comparable<E1>, E2 extends Comparable<E2>> implements Comparator<ChrisGraph.Node<E1, E2>> {
        @Override
        public int compare(ChrisGraph.Node<E1, E2> n1, ChrisGraph.Node<E1, E2> n2) {

            int nameCompare = n1.getNodeData().compareTo(n2.getNodeData());
            if(nameCompare > 0) {
                return nameCompare;
            } else if(nameCompare < 0) {
                return nameCompare;
            } else {
                return n1.getEdgeData().compareTo(n2.getEdgeData());
            }
        }
    }

    private void listChildren(String graphName, String parentName) {

        ChrisGraph<String, String> newGraph = graphs.get(graphName);
        List<ChrisGraph.Node<String, String>> nodes = newGraph.nodesChildren(parentName);
        Collections.sort(nodes, new NodeComparator<>());

        String printStatement = "the children of " + parentName + " in " + graphName + " are:";

        for(ChrisGraph.Node<String, String> node : nodes) {
            printStatement = printStatement + " " + node.getNodeData() + "(" + node.getEdgeData() + ")";
        }
        output.println(printStatement);

        // ___ = graphs.get(graphName);
        // output.println(...);
    }


    private void loadGraph(List<String> arguments) {

        //throws exception if we have the incorrect arguments
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        //retrieves the parameters for loadGraph
        String graphName = arguments.get(0);
        String fileName = arguments.get(1);
        loadGraph(graphName, fileName);
    }

    private void loadGraph(String graphName, String fileName) {

        //Creates a new graph and initializes it
        ChrisGraph<String, String> newGraph = MarvelPaths.loadGraph(fileName);
        graphs.put(graphName, newGraph);
        output.println("loaded graph " + graphName);
        // graphs.put(graphName, ___);
        // output.println(...);
    }


    private void findPath(List<String> arguments) {

        //Throws an exception if we have the incorrect number of arguments
        if(arguments.size() != 3) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        //initialized the parameters for findPath
        String graphName = arguments.get(0);
        String startNode = arguments.get(1);
        String destNode = arguments.get(2);

        findPath(graphName, startNode, destNode);
    }

    private void findPath(String graphName, String startNode, String destNode) {

        ChrisGraph<String, String> newGraph = graphs.get(graphName);

        //If the start and/or dest node doesn't exist within the newGraph then say so instead of proceeding further
        if(!newGraph.graphContainsNode(startNode) && !newGraph.graphContainsNode(destNode)) {
            output.println("unknown: " + startNode + "\n" + "unknown: " + destNode);
        }else if(!newGraph.graphContainsNode(startNode)) {
            output.println("unknown: " + startNode);
        } else if(!newGraph.graphContainsNode(destNode)) {
            output.println("unknown: " + destNode);
        } else {

            //retrieves list pertaining to shortest path
            List<ChrisGraph.Node<String, String>> nodes = MarvelPaths.shortestPathBetweenHeroes(newGraph, startNode, destNode);


                String printStatement = "path from " + startNode + " to " + destNode + ":";
                String parentNode = startNode;

            //if nodes is null then there is no path
            if (nodes == null) {
                output.println(printStatement + "\n" + "no path found");
            } else {

                //constructs a string representing the path
                for (ChrisGraph.Node<String, String> node : nodes) {
                    printStatement =  printStatement + "\n" + parentNode + " to " + node.getNodeData() +
                            " via " + node.getEdgeData();
                    parentNode = node.getNodeData();
                }
                output.println(printStatement);
            }
        }
    }

    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }

        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }
}
