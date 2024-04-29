package graph.junitTests;
import graph.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * This class contains a set of test cases that can be used to test the implementation of the
 * ChrisGraph class.
 *
 * <p>
 */
public class ChrisGraphTest {


//Test node class


    //GetNodeWeight

        ///test that getNodeWeight gets the correct name when name is empty
        @Rule public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested
        @Test
        public void getEdgeEmptyCorrect() {
            ChrisGraph.Node<String, String> node = new ChrisGraph.Node<>("n1", "");
            assertEquals(node.getEdgeData(), "");
        }

        ///test that getNodeWeight gets the correct name when name is not empty
        @Test
        public void getEdgeDataFilledCorrect() {
            ChrisGraph.Node<String, String> node = new ChrisGraph.Node<>("n1", "20");
            assertEquals(node.getEdgeData(), "20");
        }

        //test that getNodeWeight fails when it is expected to
        @Test
        public void getEdgeDataFilledIncorrect() {
            ChrisGraph.Node<String, String> node = new ChrisGraph.Node<>("n1", "20");
            assertNotEquals(node.getEdgeData(), "15");
        }

    //GetNodeName

        ///test that getNodeName gets the correct name when the name is empty
        @Test
        public void getNodeDataEmptyCorrect() {
            ChrisGraph.Node<String, String> node = new ChrisGraph.Node<>("", "");
            assertEquals(node.getEdgeData(), "");
        }

        ///test that getNodeName gets the correct name when name is not is simply empty
        @Test
        public void getNodeDataFilledCorrect() {
            ChrisGraph.Node<String, String> node = new ChrisGraph.Node<>("n1", "20");
            assertEquals(node.getNodeData(), "n1");
        }

        //test that getNodeName fails when it is expected to
        @Test
        public void getNodeDataFilledIncorrect() {
            ChrisGraph.Node<String, String> node = new ChrisGraph.Node<>("n1", "20");
            assertNotEquals(node.getNodeData(), "n2");
        }


    //equals

        //test equals returns true when they are the same
        @Test
        public void nodesEqualEachOther() {
            ChrisGraph.Node<String, String> node = new ChrisGraph.Node<>("n1", "20");
            assertTrue(node.getNodeData().equals("n1") && node.getEdgeData().equals("20"));
        }

        //test equals returns false when they are not the same
        @Test
        public void nodesDoNotEqualEachOther() {
            ChrisGraph.Node<String, String> nodeOne = new ChrisGraph.Node<>("n1", "20");
            ChrisGraph.Node<String, String> nodeTwo = new ChrisGraph.Node<>("20", "n1");

            assertNotEquals(nodeOne,nodeTwo);

            nodeTwo = new ChrisGraph.Node<>("n1", "40");
            assertNotEquals(nodeOne,nodeTwo);

            nodeTwo = new ChrisGraph.Node<>("n2", "20");
            assertNotEquals(nodeOne,nodeTwo);
        }

        //hashCode
        //tests that the hashes of 2 equal nodes is the same
        @Test
        public void equalHashCodesWhenSame() {
            ChrisGraph.Node<String, String> nodeOne = new ChrisGraph.Node<>("n1", "20");
            ChrisGraph.Node<String, String> nodeTwo = new ChrisGraph.Node<>("n1", "20");
            assertEquals(nodeOne.hashCode(), nodeTwo.hashCode());
        }

        //tests that the hashes of 2 unequal nodes is not the same
        @Test
        public void notEqualHashCodesWhenNotSame() {
            ChrisGraph.Node<String, String> nodeOne = new ChrisGraph.Node<>("n1", "20");
            ChrisGraph.Node<String, String> nodeTwo = new ChrisGraph.Node<>("20", "n1");

            assertTrue(nodeOne.hashCode() != nodeTwo.hashCode());

            nodeTwo = new ChrisGraph.Node<>("n1", "40");
            assertTrue(nodeOne.hashCode() != nodeTwo.hashCode());

            nodeTwo = new ChrisGraph.Node<>("n2", "20");
            assertTrue(nodeOne.hashCode() != nodeTwo.hashCode());
        }

//nodes children section

    //Returns a correct list for a node that doesn't currently exist
    @Test
    public void DNENodeChildrenCorrect() {
        ChrisGraph<String, String> newGraph = new ChrisGraph<>();

        assertEquals(new ArrayList<>(), newGraph.nodesChildren("Does Not Exist"));
    }

    //Returns a correct list of a node with 0 children
    @Test
    public void zeroNodeChildrenCorrect() {
        ChrisGraph<String, String> newGraph = new ChrisGraph<>();

        newGraph.addNode("n1");
        assertEquals(new ArrayList<>(), newGraph.nodesChildren("n1"));
    }


    //Returns a correct list for a graph that has a cycle
    @Test
    public void ChildrenHasCycleCorrect() {
        ChrisGraph<String, String> newGraph = new ChrisGraph<>();
        List<String> comparer = new ArrayList<>();

        newGraph.addNode("n1");

        newGraph.addEdge("n1", "n1", "0");
        comparer.add("n1");
        assertEquals(comparer, newGraph.nodesNeighbors("n1"));
    }



//nodes parents section

    //Returns a correct list for a node that doesn't exist
    @Test
    public void DNENodeParentCorrect() {
        ChrisGraph<String, String> newGraph = new ChrisGraph<>();

        assertEquals(new ArrayList<>(), newGraph.nodesParents("Does Not Exist"));
    }

    //Returns a correct list of an empty graph's nodes
    @Test
    public void zeroNodeParentCorrect() {
        ChrisGraph<String, String> newGraph = new ChrisGraph<>();

        newGraph.addNode("n1");
        assertEquals(new ArrayList<>(), newGraph.nodesParents("n1"));
    }

    //Returns a correct list of a graph with 1, 2, and 3 nodes
    @Test
    public void multipleNodeParentsCorrect() {
        ChrisGraph<String, String> newGraph = new ChrisGraph<>();

        newGraph.addNode("n1");
        newGraph.addNode("n2");
        newGraph.addNode("n3");
        newGraph.addNode("n4");

        List<String> comparer = new ArrayList<>();

        newGraph.addEdge("n2", "n1", "");
        comparer.add("n2");
        assertEquals(newGraph.nodesParents("n1"), comparer);

        newGraph.addEdge("n3", "n1", "");
        comparer.add("n3");
        assertEquals(newGraph.nodesParents("n1"), comparer);

        newGraph.addEdge("n4", "n1", "");
        comparer.add("n4");
        assertEquals(comparer, newGraph.nodesParents("n1"));

    }

    //Returns a correct list for a graph that has a cycle
    @Test
    public void ParentsHasCycleCorrect() {
        ChrisGraph<String, String> newGraph = new ChrisGraph<>();
        List<String> comparer = new ArrayList<>();

        newGraph.addNode("n1");

        newGraph.addEdge("n1", "n1", "0");
        comparer.add("n1");
        assertEquals(comparer, newGraph.nodesNeighbors("n1"));
    }


    //All nodes are unique test
    @Test
    public void NodeParentsAreUnique() {
        ChrisGraph<String, String> newGraph = new ChrisGraph<>();

        newGraph.addNode("6");
        newGraph.addNode("4");
        newGraph.addNode("1");
        newGraph.addNode("10");

        newGraph.addEdge("4", "6", "");
        newGraph.addEdge("1", "6", "");
        newGraph.addEdge("10", "6", "");
        newGraph.addEdge("10", "6", "");

        List<String> comparer = new ArrayList<>();
        comparer.add("1");
        comparer.add("4");
        comparer.add("10");

        assertEquals(comparer, newGraph.nodesParents("6"));
    }


//nodes neighbors section

    //Returns a correct list for a node that doesn't exist
    @Test
    public void DNENodeNeighborsCorrect() {
        ChrisGraph<String, String> newGraph = new ChrisGraph<>();

        assertEquals(new ArrayList<>(), newGraph.nodesNeighbors("Does Not Exist"));
    }

    //Returns a correct list of an isolated nodes neighbors
    @Test
    public void ZeroNodeNeighborsCorrect() {
        ChrisGraph<String, String> newGraph = new ChrisGraph<>();

        newGraph.addNode("n1");
        assertEquals(new ArrayList<>(), newGraph.nodesNeighbors("n1"));
    }


    //Returns a correct list for a node that has a cycle
    @Test
    public void NeighborsHasCycleCorrect() {
        ChrisGraph<String, String> newGraph = new ChrisGraph<>();
        List<String> comparer = new ArrayList<>();

        newGraph.addNode("n1");

        newGraph.addEdge("n1", "n1", "0");
        comparer.add("n1");
        assertEquals(comparer, newGraph.nodesNeighbors("n1"));
    }

    //All children and parents are unique
    @Test
    public void NodeNeighborsAreUnique() {
        ChrisGraph<String, String> newGraph = new ChrisGraph<>();

        newGraph.addNode("6");
        newGraph.addNode("4");
        newGraph.addNode("1");
        newGraph.addNode("10");

        newGraph.addEdge("6", "4", "");
        newGraph.addEdge("1", "6", "");
        newGraph.addEdge("10", "6", "");
        newGraph.addEdge("6", "10", "");

        List<String> comparer = new ArrayList<>();
        comparer.add("1");
        comparer.add("10");
        comparer.add("4");

        assertEquals(comparer, newGraph.nodesNeighbors("6"));
    }


//nodes indegree section

    //Returns 0 if nodeName doesn't exist
    @Test
    public void DNENodeInDegree() {
        ChrisGraph<String, String> newGraph = new ChrisGraph<>();
        assertEquals(newGraph.nodesInDegree("n1"), 0);
    }

    //Returns the correct indegree for a node having 0 parents
    @Test
    public void ZeroNodeInDegree() {
        ChrisGraph<String, String> newGraph = new ChrisGraph<>();
        newGraph.addNode("n1");
        assertEquals(newGraph.nodesInDegree("n1"), 0);
    }

    // Returns the correct indegree for a node having 1, 2, and 3 other nodes pointing at it
    @Test
    public void DifferentNodeInDegree() {
        ChrisGraph<String, String> newGraph = new ChrisGraph<>();

        newGraph.addNode("n1");
        newGraph.addNode("n2");
        newGraph.addNode("n3");
        newGraph.addNode("n4");

        newGraph.addEdge("n4", "n1", "");
        assertEquals(newGraph.nodesInDegree("n1"), 1);

        newGraph.addEdge("n3", "n1", "");
        assertEquals(newGraph.nodesInDegree("n1"), 2);

        newGraph.addEdge("n2", "n1", "");
        assertEquals(newGraph.nodesInDegree("n1"), 3);

    }


//nodes outdegree section

    //Returns 0 if nodeName doesn't exist
    @Test
    public void DNENodeOutDegree() {
        ChrisGraph<String, String> newGraph = new ChrisGraph<>();
        assertEquals(newGraph.nodesOutDegree("n1"), 0);
    }

    //Returns the correct outdegree for a node having 0 children
    @Test
    public void ZeroNodeOutDegree() {
        ChrisGraph<String, String> newGraph = new ChrisGraph<>();
        newGraph.addNode("n1");
        assertEquals(newGraph.nodesOutDegree("n1"), 0);
    }

    //Returns the correct outdegree for a node having 1, 2, and 3 nodes it is pointing at
    @Test
    public void DifferentNodeOutDegree() {
        ChrisGraph<String, String> newGraph = new ChrisGraph<>();

        newGraph.addNode("n1");
        newGraph.addNode("n2");
        newGraph.addNode("n3");
        newGraph.addNode("n4");

        newGraph.addEdge("n1", "n4", "");
        assertEquals(newGraph.nodesOutDegree("n1"), 1);

        newGraph.addEdge("n1", "n3", "");
        assertEquals(newGraph.nodesOutDegree("n1"), 2);

        newGraph.addEdge("n1", "n2", "");
        assertEquals(newGraph.nodesOutDegree("n1"), 3);
    }


//add edge section

    //checks that if one of the nodes doesn't exist that an edge is never created
    @Test
    public void mismatchedNodesCorrect() {
        ChrisGraph<String, String> newGraph = new ChrisGraph<>();

        newGraph.addNode("n1");

        newGraph.addEdge("n1", "n3", "4");
        newGraph.addEdge("n3", "n1", "4");
        assertTrue(newGraph.nodesNeighbors("n1").isEmpty());
    }

    //checks that the weight between two existing nodes is set correctly
    @Test
    public void checkAddEdgeSetsCorrect() {
        ChrisGraph<String, String> newGraph = new ChrisGraph<>();

        newGraph.addNode("n1");
        newGraph.addNode("n2");

        newGraph.addEdge("n1", "n2", "4");
        assertEquals(newGraph.nodesChildren("n1").get(0).getEdgeData(), "4");
    }


//graph contains node section

    //checks that it returns true if the node exists
    @Test
    public void graphHasNodeCorrect() {

        ChrisGraph<String, String> newGraph = new ChrisGraph<>();

        newGraph.addNode("n1");
        assertTrue(newGraph.graphContainsNode("n1"));

    }

    //checks that it throws an exception if the graph is null
    @Test
    public void graphContainsCorreclyThrowsException() {

        ChrisGraph<String, String> testGraph = null;
        assertThrows(NullPointerException.class, () -> {
            testGraph.graphContainsNode("A");
        });

    }

    //checks that it returns false if the node doesn't exist
    @Test
    public void graphHasNodeFalse() {
        ChrisGraph<String, String> newGraph = new ChrisGraph<>();
        assertFalse(newGraph.graphContainsNode("n1"));
        newGraph.addNode("n1");
        assertFalse(newGraph.graphContainsNode("n2"));

    }


}
