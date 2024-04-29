package graph;

import java.util.*;

/**
 * This class is a mutable abstract representation of a graph utilizing nodes and edges. A graph in this context
 * consists of any number of nodes, with some or all of them being connected to others through edges. Depending on
 * the graph, these edges may have "weights" (a value associated with them such as length, time, etc.) and there may
 * be a path between any and all nodes. All nodes and edges are unique. The first generic type E1 associated with
 * this ADT describes the object that a node contains, and the second generic type E2 describes the object that an
 * edge contains.
 */
public class ChrisGraph<E1, E2> {

    private static final boolean DEBUG = false;

    private  Map<E1, Set<Node<E1, E2>>> graph;

    // Abstraction Function:
    // For any given ChrisGraph g, there is a map whose keys represent all nodes
    // and whose data values represent a list of their children. These lists of children
    // store both the data of each of the node's children as well as the edge label
    // connecting the parent and child within the node class. {n1 -> (n1's node children), ... ni -> (ni's node children)}
    //
    // Representation Invariant:
    // for(E1 i : graph.keySet)
    //      for(E1 j : graph.keySet)
    //          i != j
    // graph != null
    // for all i node datas and for all j nodes, i != null && j != null
    //
    // There are no duplicate node children of a parent node ( same edge label and same destination node)
    //
    // for all i nodes and for all j children i, j's nodeName is a node that already exists AKA
    //        for(E1 key : graph.keySet()) {
    //            for(Node<E1, E2> node : graph.get(key)) {
    //                graph.keySet().contains(node.getNodeName());
    //            }
    //        }




    /**
     * This class is an immutable abstract representation of a node found in a graph. Each node is given a piece of data as a classifier
     * and is given a weight value to determine the weighted edge between another node and itself. The first
     * generic type E1 associated with this ADT describes the object that a node contains, and the second generic
     * type E2 describes the object that an edge contains.
     */
    public static class Node<E1, E2> {

        private final E1 nodeData;
        private final E2 edgeData;

        // Abstraction Function:
        // For any given Node n, an incoming edge's label to that n is synonymous with edge data
        // and the data of the node being pointed to (n) is synonymous with nodeData
        //
        // Representation Invariant:
        // nodeData != null
        // (weight can be null since that means it has no edge)



        /**
         * Constructs a new Node object with an edge.
         *
         *
         * @param nodeData The data of the created node
         * @param edgeData The value of the edge connecting this node and another. If it is empty it is unweighted
         * @spec.effects Constructs a new Node that has a data and an edge value provided.
         * @spec.requires nodeName != null AND weight != null
         */

        public Node(E1 nodeData, E2 edgeData) {

            this.nodeData = nodeData;
            this.edgeData = edgeData;
            checkRep();

        }


        /**
         * Constructs a new Node object with no edge (used usually as a starting node).
         *
         *
         * @param nodeData The data of the created node
         * @spec.effects Constructs a new Node that with the given data a no incoming edge.
         * @spec.requires nodeData != null
         */
        public Node(E1 nodeData) {

            this.nodeData = nodeData;
            this.edgeData = null;
            checkRep();

        }

        /**
         * Throws an exception if the representation invariant is violated.
         */
        private void checkRep() {

            assert(nodeData != null) : "The data of this node was not initialized";

        }

        /**
         * Gets the data associated with this node
         *
         * @return The nodeData of this node.
         */
        public E1 getNodeData() {

            checkRep();
            E1 node = nodeData;
            checkRep();
            return node;
        }

        /**
         * Gets the data of the edge pointing towards this node
         *
         * @return Returns the edge data of this node.
         */
        public E2 getEdgeData() {
            checkRep();
            E2 weighter = edgeData;
            checkRep();
            return weighter;
        }

        /**
         * Standard equality operation.
         *
         * @param obj the object to be compared to "this" for equality
         * @return true iff 'obj' is an instance of a Node and 'this' and 'obj' represent the same
         * object
         */
        @Override
        public boolean equals(Object obj) {

            checkRep();
            if(!(obj instanceof Node<?, ?>)) {

                checkRep();
                return false;
            }


            checkRep();

            Node<?, ?> node = (Node<?, ?>) obj;
            return (this.getNodeData().equals(node.getNodeData()) &&
                    this.getEdgeData().equals(node.getEdgeData()));

        }


        /**
         * Standard hashCode function.
         *
         * @return an int that all objects equal to this will also return.
         */
        @Override
        public int hashCode() {

            checkRep();
            int returner = 31 * this.getNodeData().hashCode() + this.getEdgeData().hashCode();
            checkRep();

            return returner;
        }

    }

    /**
     * Constructs a new ChrisGraph.
     *
     *
     * @spec.effects Constructs a new ChrisGraph that is currently empty.
     */
    public ChrisGraph() {
        graph = new HashMap<>();
        checkRep();
    }

    /**
     * Throws an exception if the representation invariant is violated.
     */
    private void checkRep() {

        assert(graph != null) : "The graph is not initialized";

        if(DEBUG) {
            for (E1 i : graph.keySet()) {
                assert (i != null);
                for (Node j : graph.get(i)) {
                    assert (j != null) : "A node in the graph is null";
                }
            }

            //Hashsets prevent duplicates so is nodes not having multiple instances
            //of the same child is implicitly checked

            Object[] keys = graph.keySet().toArray();

            for (int i = 0; i < keys.length; i++) {
                for (int j = i + 1; j < keys.length; j++) {
                    assert (keys[i] != keys[j]) : "There is a duplicate node";
                }
            }

            for (E1 key : graph.keySet()) {
                for (Node node : graph.get(key)) {
                    assert (graph.keySet().contains(node.getNodeData())) : "One of a nodes children is not a real node";
                }
            }
        }


    }

    /**
     * Adds a brand-new node (nodeData) to the graph (g)
     *
     *
     * @param nodeData The data representation of the newly added node
     * @spec.modifies this
     * @spec.effects If the node already exists, nothing is added to the graph. Otherwise,
     * the node becomes a new node within the graph.
     * Ex: g can be represented as [n1, n3, n4]. g:nodeData should be [n1, n3, n4, nodeData]
     * @spec.requires nodeName != null
     */
    public void addNode(E1 nodeData) {
        checkRep();
        if(!graph.keySet().contains(nodeData)) {
            graph.put(nodeData, new HashSet<>());
        }
        checkRep();
    }

    /**
     * Adds a brand-new edge (edgeData) between one node (parent) and another (nChild) to the graph.
     * If the edge data is null it is unweighted and if the edge already exists nothing happens.
     *
     *
     * @param parentNodeData The data of the parent of the edge (where the edge/arrow originates from)
     * @param childNodeData The data of the child of the edge (where the edge/arrow points to)
     * @param edgeData The data (label) of the edge from the parentNode to the childNode
     * @spec.modifies this
     * @spec.effects If one of the nodes provided doesn't exist or the edge already exists
     * then nothing happens. Otherwise, the edge is added
     * to the list of the parent node's children. Example (formatted as (edge destination, edge weight)):
     * parents' children == [(n2, e1), (n3,e2)]. [(n2, e1),
     * (n3, e2)]:(nChild, edgeData) becomes [(n2, e1), (n3, e2), (nChild, edgeData)].
     * @spec.requires parentNodeData != null AND childNodeData != null AND edgeData != null
     */
    public void addEdge(E1 parentNodeData, E1 childNodeData, E2 edgeData) {

        checkRep();
        Node<E1, E2> newEdge = new Node<>(childNodeData, edgeData);

        if(graph.containsKey(parentNodeData)
        && graph.containsKey(childNodeData)
        && !graph.get(parentNodeData).contains(newEdge)) {

            graph.get(parentNodeData).add(newEdge);
        }

        checkRep();
    }


    /**
     * Finds a list of unique children from a given parentNode.
     *
     *
     * @param nodeData The data of the node whose children we are finding
     * @spec.requires  nodeData != null
     * @return returns a list of all the nodes' children in node form (node_data, edge_data) and returns an empty
     * list if the node doesn't exist. All nodes are unique.
     * */
    public List<Node<E1, E2>> nodesChildren(E1 nodeData) {

        checkRep();
        Set<Node<E1, E2>> copy = graph.get(nodeData);
        if(copy != null) {
            checkRep();
            return new ArrayList<>(copy);
        }

        List<Node<E1, E2>> returner = new ArrayList<>();
        checkRep();
        return returner;
    }

    /**
     * Finds a list of unique parents corresponding to a given parentNode.
     *
     *
     * @param nodeData The data of the node whose parents we are finding
     * @spec.requires nodeData != null
     * @return returns a list of all the nodes' parents in string form; however, if the node doesn't
     * exist it merely returns an empty list. All string parent node datas are unique.
     */
    public List<E1> nodesParents(E1 nodeData) {

        checkRep();
        List<E1> returner = new ArrayList<>();

        for(E1 i : graph.keySet()) {
            for(Node j : graph.get(i)) {
                if(j.getNodeData().equals(nodeData)) {
                    returner.add(i);
                    break;
                }
            }
        }

        checkRep();
        return returner;
    }


    /**n
     * Finds a list of unique elements corresponding to the children and parents of a given parentNode.
     *
     *
     * @param nodeData The data of the node whose neighbors we are finding
     * @spec.requires nodeData != null
     * @return returns a list of all the unique nodes data's connected to the node through incoming edges
     * followed by the nodes connected by outgoing edges. If the node doesn't exist it returns an empty list.
     */
    public List<E1> nodesNeighbors(E1 nodeData) {

        checkRep();

        List<E1> returner = new ArrayList<>(this.nodesParents(nodeData));
        List<Node<E1, E2>> children = new ArrayList<>(this.nodesChildren(nodeData));

        for(Node<E1, E2> i : children) {
            if(!returner.contains(i.nodeData)) {

                returner.add(i.nodeData);
            }
        }

        checkRep();
        return returner;
    }

    /**
     * Finds the number of nodes that point edges towards this node
     *
     *
     * @param nodeData The data of the node whose indegree we are finding
     * @spec.requires nodeData != null
     * @return returns the indegree of the node (the number of nodes with edges pointing towards it).
     * It returns 0 if the node doesn't exist.
     */
    public int nodesInDegree(E1 nodeData) {

        checkRep();
        int returner = this.nodesParents(nodeData).size();
        checkRep();

        return returner;
    }

    /**
     * Finds the number of nodes that this node has edges pointing to
     *
     *
     * @param nodeData The data of the node whose outdegree we are finding
     * @spec.requires nodeData != null
     * @return returns the outdegree of the node (the number of nodes this node has edges pointing to). If the node
     * doesn't exist return 0.
     */
    public int nodesOutDegree(E1 nodeData) {

        checkRep();
        int returner = this.nodesChildren(nodeData).size();
        checkRep();

        return returner;
    }

    /**
     * Determines whether a given node exists in the graph or not
     *
     *
     * @param nodeData The data of the node who will hold the edge going from it to a separate node
     * @throws IllegalArgumentException Throws if the parameter is ever null
     * @return returns true if the given node exists in the graph or not
     */
    public boolean graphContainsNode(E1 nodeData) {

        checkRep();

        if(nodeData == null) {
            throw new IllegalArgumentException("The Node " + nodeData + " is null");
        }

        boolean returner = graph.containsKey(nodeData);
        checkRep();

        return returner;
    }


    /**
     * Finds all nodes that currently exist within the graph and puts them in list form
     *
     *
     * @return returns a list of all node datas in the graph (should be unique).
     */
    public List<E1> listAllNodes() {

        checkRep();
        List<E1> returner = new ArrayList<>(graph.keySet());
        checkRep();

        return returner;
    }
}