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
}
