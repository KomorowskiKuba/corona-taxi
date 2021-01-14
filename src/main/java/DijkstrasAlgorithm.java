import java.util.*;

class Graph {
    private final Set<Hospital> nodeSet = new HashSet<>();

    public void addNode(Hospital node) {
        nodeSet.add(node);
    }

    public Set<Hospital> getNodes() {
        return nodeSet;
    }
}

public class DijkstrasAlgorithm {
    private final Graph graph;

    DijkstrasAlgorithm(List<Hospital> hospitals) {
        graph = createGraph(hospitals);
    }

    private Graph createGraph(List<Hospital> hospitals) {
        Graph graph = new Graph();

        for (Hospital h : hospitals) {
            graph.addNode(h);
        }

        return graph;
    }

    private static void calculateShortestPathsFromNode(Hospital node) {
        Set<Hospital> settledNodes = new HashSet<>();
        Set<Hospital> unsettledNodes = new HashSet<>();

        node.setDistance(0);
        unsettledNodes.add(node);

        while (unsettledNodes.size() != 0) {
            Hospital currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);

            for (Map.Entry<Hospital, Double> pair : currentNode.getAdjacentNodeMap().entrySet()) {
                double edgeDistance = pair.getValue();
                Hospital adjNode = pair.getKey();

                if (!settledNodes.contains(adjNode)) {
                    calculateMinDistance(adjNode, edgeDistance, currentNode);
                    unsettledNodes.add(adjNode);
                }
            }
            settledNodes.add(currentNode);
        }

    }

    private static Hospital getLowestDistanceNode(Set<Hospital> unsettledNodes) {
        Hospital lowestDistanceNode = null;
        double lowestDistance = Integer.MAX_VALUE;

        for (Hospital node : unsettledNodes) {
            if (node.getDistance() < lowestDistance) {
                lowestDistanceNode = node;
                lowestDistance = node.getDistance();
            }
        }

        return lowestDistanceNode;
    }

    private static void calculateMinDistance(Hospital evaluationNode, double distance, Hospital source) {
        double srcDistance = source.getDistance();

        if (srcDistance + distance < evaluationNode.getDistance()) {
            evaluationNode.setDistance(srcDistance + distance);
            LinkedList<Hospital> shortestPath = new LinkedList<>(source.getShortestPath());
            shortestPath.add(source);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

    public Hospital getNearestEmpty(Hospital node) {
        Hospital nearestAndEmpty = null;
        double distance = Integer.MAX_VALUE;

        calculateShortestPathsFromNode(node);

        for (Hospital n : graph.getNodes()) {
            if (n.getDistance() < distance && n.getEmptyBeds() > 0) {
                distance = n.getDistance();
                nearestAndEmpty = n;
            }
        }

        return nearestAndEmpty;
    }

    public void clear() {
        for (Hospital h : graph.getNodes()) {
            h.setDistance(Integer.MAX_VALUE);
            h.setShortestPath(new LinkedList<>());
        }
    }

    public Graph getGraph() {
        return graph;
    }

    public static void main(String[] args) {
        List<Hospital> hospitals = new ArrayList<>();

        Hospital node1 = new Hospital(1, "Hospital1", 1,1, 100, 0);
        Hospital node2 = new Hospital(2, "Hospital2", 2,2, 100, 0);
        Hospital node3 = new Hospital(3, "Hospital3", 3,3, 100, 0);
        Hospital node4 = new Hospital(4, "Hospital4", 4,4, 100, 0);
        Hospital node5 = new Hospital(5, "Hospital5", 5,5, 100, 100);
        Hospital node6 = new Hospital(6, "Hospital6", 6,6, 100, 100);


        node1.addDestination(node2, 10);
        node1.addDestination(node3, 15);
        node2.addDestination(node4, 12);
        node2.addDestination(node6, 15);
        node3.addDestination(node5, 10);
        node4.addDestination(node5, 2);
        node4.addDestination(node6, 1);
        node6.addDestination(node5, 5);

        hospitals.add(node1);
        hospitals.add(node2);
        hospitals.add(node3);
        hospitals.add(node4);
        hospitals.add(node5);
        hospitals.add(node6);

        DijkstrasAlgorithm dijkstrasAlgorithm = new DijkstrasAlgorithm(hospitals);

        System.out.println();
        Hospital n = dijkstrasAlgorithm.getNearestEmpty(node1);
        System.out.println(n.toString());
        System.out.println(n.getShortestPath().toString());
    }
}
