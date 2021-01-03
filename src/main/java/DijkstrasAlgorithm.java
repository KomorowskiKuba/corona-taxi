import java.util.*;

class GraphNode {
    private final int id;
    private boolean isEmpty;
    private int distance = Integer.MAX_VALUE;
    private List<GraphNode> shortestPath = new LinkedList<>();
    private final Map<GraphNode, Integer> adjacentNodeMap = new HashMap<>();

    public GraphNode(int id, boolean isEmpty) {
        this.id = id;
        this.isEmpty = isEmpty;
    }

    public void addDestination(GraphNode destination, int distance) {
        adjacentNodeMap.put(destination, distance);
    }

    public int getDistance() {
        return distance;
    }

    public int getId() {
        return id;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public Map<GraphNode, Integer> getAdjacentNodeMap() {
        return adjacentNodeMap;
    }

    public List<GraphNode> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(List<GraphNode> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "GraphNode{" +
                "id=" + id +
                ", shortestPath=" + shortestPath +
                ", distance=" + distance +
                ", isEmpty=" + isEmpty +
                '}';
    }
}

class Graph {
    private final Set<GraphNode> nodeSet = new HashSet<>();

    public void addNode(GraphNode node) {
        nodeSet.add(node);
    }

    public Set<GraphNode> getNodes() {
        return nodeSet;
    }
}

public class DijkstrasAlgorithm {
    public static void calculateShortestPathFromNode(Graph graph, GraphNode node) {
        Set<GraphNode> settledNodes = new HashSet<>();
        Set<GraphNode> unsettledNodes = new HashSet<>();

        node.setDistance(0);
        unsettledNodes.add(node);

        while (unsettledNodes.size() != 0) {
            GraphNode currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);

            for (Map.Entry<GraphNode, Integer> pair : currentNode.getAdjacentNodeMap().entrySet()) {
                int edgeDistance = pair.getValue();
                GraphNode adjNode = pair.getKey();

                if (!settledNodes.contains(adjNode)) {
                    calculateMinDistance(adjNode, edgeDistance, currentNode);
                    unsettledNodes.add(adjNode);
                }
            }
            settledNodes.add(currentNode);
        }

    }

    private static GraphNode getLowestDistanceNode(Set<GraphNode> unsettledNodes) {
        GraphNode lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;

        for (GraphNode node : unsettledNodes) {
            if (node.getDistance() < lowestDistance) {
                lowestDistanceNode = node;
                lowestDistance = node.getDistance();
            }
        }

        return lowestDistanceNode;
    }

    private static void calculateMinDistance(GraphNode evaluationNode, int distance, GraphNode source) {
        int srcDistance = source.getDistance();

        if (srcDistance + distance < evaluationNode.getDistance()) {
            evaluationNode.setDistance(srcDistance + distance);
            LinkedList<GraphNode> shortestPath = new LinkedList<>(source.getShortestPath());
            shortestPath.add(source);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

    public static GraphNode getNearestEmpty(Graph graph, GraphNode node) {
        GraphNode nearestAndEmpty = null;
        int distance = Integer.MAX_VALUE;

        calculateShortestPathFromNode(graph, node);

        for (GraphNode gn : graph.getNodes()) {
            if (gn.getDistance() < distance && gn.getDistance() != 0 && gn.isEmpty()) {
                distance = gn.getDistance();
                nearestAndEmpty = gn;
            }
        }

        return nearestAndEmpty;
    }

    public static void main(String[] args) {
        Graph graph = new Graph();

        GraphNode node1 = new GraphNode(1, true);
        GraphNode node2 = new GraphNode(2, false);
        GraphNode node3 = new GraphNode(3, true);
        GraphNode node4 = new GraphNode(4, true);
        GraphNode node5 = new GraphNode(5, true);
        GraphNode node6 = new GraphNode(6, true);

        node1.addDestination(node2, 8);
        node1.addDestination(node3, 14);
        node2.addDestination(node4, 13);
        node2.addDestination(node6, 15);
        node3.addDestination(node5, 9);
        node4.addDestination(node5, 3);
        node4.addDestination(node6, 2);
        node6.addDestination(node5, 4);

        graph.addNode(node1);
        graph.addNode(node2);
        graph.addNode(node3);
        graph.addNode(node4);
        graph.addNode(node5);
        graph.addNode(node6);

        System.out.println(getNearestEmpty(graph, node1).toString());

        //DijkstrasAlgorithm.calculateShortestPathFromNode(graph, nodeA);

        //for (GraphNode n : graph.getNodes()) {
        //    System.out.println("ID: " + n.getId() + " " + n.getDistance() + " " + n.isEmpty());
            //System.out.println(n.toString());
        //}
    }
}
