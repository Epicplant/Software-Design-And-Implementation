package marvel;

import graph.ChrisGraph;

import java.util.*;

/**
 * This class has the dual purpose of both creating a graph for marvel heroes as well as finding
   the shortest path between 2 of them using BFS.
 */
public class MarvelPaths{


    /**
     * The main method of the program. Accepts user input to determine starting hero node and ending hero node,
       and then finds the shortest number of comic books one needs to traverse until they reach a
       comic with the other hero in it (path is lexicographically sorted).
     *
     * @param args The command line application arguments for this function
     */
     public static void main(String args[]) {

        Scanner input = new Scanner(System.in);
        boolean running = true;

        System.out.println("Welcome to the Marvel Hero Database!!!");

        //Runs as long as the user wants (until running is set to false by user input)
        while(running) {

            //Asks for starting node
            System.out.print("Please input the hero you wish to start searching from: ");
            String readInputStart = input.nextLine();
            System.out.println("Start hero is " + readInputStart);

            //Asks for the destination node
            System.out.print("Please input the hero you want to eventually reach: ");
            String readInputDest = input.nextLine();
            System.out.println("Dest hero is " + readInputDest);

            ChrisGraph<String, String> newGraph = loadGraph("marvel.csv");

            //Performs similar operation to marvelParser by constructing a path and turning it into a nice string
            try {

                //Asks for start node
                System.out.println("");
                List<ChrisGraph.Node<String, String>> path = shortestPathBetweenHeroes(newGraph, readInputStart, readInputDest);
                String printStatement = "";

                //initialized step counter
                int counter = 1;
                String parentNode = readInputStart;

                //if path is null then there doesn't exist a path between the start and dest node
                if (path == null) {
                    System.out.println(printStatement + "\n" + "no path found");
                } else {

                    //Loops through the path list until we reach the end, ultimately constructing a return string from it
                    for (ChrisGraph.Node<String, String> node : path) {
                        printStatement =  printStatement +  "\n" + "(**Step " + counter + "**)-[**Character " + parentNode + " is connected to " +
                                node.getNodeData() + " by the comic issue " + node.getNodeData() + "**]";
                        parentNode = node.getNodeData();
                        counter++;
                    }
                    System.out.println("The shortest path is as follows: " + printStatement);
                }

                System.out.println("");

            //If any nodes inputted don't exist an error is thrown and they are caught here
            } catch (IllegalArgumentException e) {
                System.out.println("Action Unable to be Taken: " + e.getMessage());
            }

            //If user wants to stop looking for connections they can here
            System.out.print("Would you like to keep searching for connections (y = yes, everything else = no)? ");
            String continueAnswer = input.nextLine();
            System.out.println("");

            if(!continueAnswer.toLowerCase().equals("y")) {
              running = false;
            }
        }
    }


    /**
     * Takes a .csv file and parses the information into a new ChrisGraph. The first item in a line of the csv is used
     * to create a node, with the second item (separated from the first with a comma) being used to create an undirected
     * edge (with the second items from its label) from itself to any other nodes that have the same edge label.
     *
     * @param fileName The name of the file which we will use to load the graph
     * @spec.requires The .csv file must be of a form where the name of a hero who appears in the comic book
       is written first, followed by a comma, and then followed by the comic book (edge connecting 2 superheroes together)
     * @throws IllegalArgumentException if the file doesn't exist, has an invalid name,
       or can't be read
     * @return Returns a ChrisGraph created from the CSV file that was inputted
    */
    public static ChrisGraph<String, String> loadGraph(String fileName) {

        //parses a file so all comics are keys and all associated heroes with a comic key are stored in a list
        HashMap<String, List<String>> parsedData = MarvelParser.parseData(fileName);
        ChrisGraph<String, String> newGraph = new ChrisGraph<>();

        for(String comicName : parsedData.keySet()) {

            List<String> comicRelatedHeroes = parsedData.get(comicName);

            //initialize all heroes as nodes
            for(int j = 0; j < comicRelatedHeroes.size(); j++) {
                newGraph.addNode(comicRelatedHeroes.get(j));
            }

            //For every single hero associated with a comic, create an undirected edge between every other hero
            //associated with the comic
            for(int i = 0; i < comicRelatedHeroes.size(); i++) {
                for(int j = i+1; j < comicRelatedHeroes.size(); j++) {

                    if(!comicRelatedHeroes.get(i).equals(comicRelatedHeroes.get(j))) {
                        newGraph.addEdge(comicRelatedHeroes.get(i), comicRelatedHeroes.get(j), comicName);
                        newGraph.addEdge(comicRelatedHeroes.get(j), comicRelatedHeroes.get(i), comicName);
                    }
                }
            }
        }

        return newGraph;
    }


    /**
     * This compares nodes to each other so that they can be put in sorted order. Compares node datas first
     * and then, if those aren't the same, compares edge datas to break ties when determining which node is bigger.
     */
    private static class NodeComparator<E1 extends Comparable<E1>, E2 extends Comparable<E2>> implements Comparator<ChrisGraph.Node<E1, E2>> {

        /**
         * Returns a value indicating whether node n1 or node n2 is comparatively larger or smaller.
         * It first compares the node datas, and then depending on if that doesn't identify which one is bigger,
         * it then compares the edge data to break ties.
         *
         * @param n1 The first node being compared
         * @param n2 The second node being compared
         * @requires n1 != null && n2 != null
         * @return Returns a negative int if n2 is bigger, 0 if n1 and n2 are the same,
           and a positive int if n1 is bigger
         */
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

    /**
     * Uses Breadth-First Search to calculate the shortest path between 2 nodes. Children of a node are searched in
     * lexicographical order.
     *
     * @param newGraph The graph that is being used to find a shortest path between nodes
     * @param start Where the path initially starts from
     * @param dest The desired destination that the path leads to
     * @throws IllegalArgumentException Throws if the inputted start nodes don't exist within the graph (including when null)
               or if the graph is null itself
     * @return Returns a list of nodes describing all edges we need to take and all the childNodes we need to pass through
       in order to eventually reach the destination node.
     */
    public static List<ChrisGraph.Node<String, String>> shortestPathBetweenHeroes
    (ChrisGraph<String, String> newGraph, String start, String dest) {

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

        //Creates the queue that holds the children of every node process
        Queue<ChrisGraph.Node<String, String>> searchOrder = new LinkedList<>();
        searchOrder.add(new ChrisGraph.Node<>(start));

        //paths represents the shortest way to get to every single node
        //as a result, we'd want to access the parents list
        HashMap<String, List<ChrisGraph.Node<String, String>>> paths = new HashMap<>();
        paths.put(start, new ArrayList<>());

        //Keeps looping while there are still unvisited nodes that can be reached from start node
        //{inv: paths.get(node) is the shortest unweighted path to node for the current node in BFS}
        while(!searchOrder.isEmpty()) {

            ChrisGraph.Node<String, String> currNode = searchOrder.remove();

            //if the node is the destination then we have reached and we return the path to it
            if(currNode.getNodeData().equals(dest)) {

                return paths.get(currNode.getNodeData());

            } else {

                //Gets all the children of a node and sorts them lexicographically so they can be traversed in
                //the correct order
                List<ChrisGraph.Node<String, String>> edges = newGraph.nodesChildren(currNode.getNodeData());
                edges.sort(new NodeComparator<>());

                //Iterates through every child and if that child is unvisited then modify our map of paths
                //to include the shortest path to that node.
                for(ChrisGraph.Node<String, String> edge : edges) {
                    if(!paths.containsKey(edge.getNodeData())) {

                        List<ChrisGraph.Node<String, String>> newPath = new ArrayList<>(paths.get(currNode.getNodeData()));
                        newPath.add(edge);
                        paths.put(edge.getNodeData(), newPath);
                        searchOrder.add(edge);

                    }
                }
            }
        }

        //returns null if no path exists from start node to dest node
        return null;
    }
}
