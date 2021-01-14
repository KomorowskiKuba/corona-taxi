import org.junit.Before;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.*;

public class DijkstrasAlgorithmTest {
    List<Hospital> hospitals;

    @BeforeTest
    public void initiate() {
        Hospital node1 = new Hospital(1, "Hospital1", 1,1, 100, 0);
        Hospital node2 = new Hospital(2, "Hospital2", 2,2, 100, 0);
        Hospital node3 = new Hospital(3, "Hospital3", 3,3, 100, 0);
        Hospital node4 = new Hospital(4, "Hospital4", 4,4, 100, 0);
        Hospital node5 = new Hospital(5, "Hospital5", 5,5, 100, 0);
        Hospital node6 = new Hospital(6, "Hospital6", 6,6, 100, 100);

        node1.addDestination(node2, 10);
        node1.addDestination(node3, 15);
        node2.addDestination(node4, 12);
        node2.addDestination(node6, 15);
        node3.addDestination(node5, 10);
        node4.addDestination(node5, 2);
        node4.addDestination(node6, 1);
        node6.addDestination(node5, 5);

        hospitals = new ArrayList<>(List.of(node1, node2, node3, node4, node5, node6));
    }

    @Test
    public void getNearestAndEmpty_sampleDataGiven_nearestEmptyHospitalGot() {
        Hospital startingHospital = hospitals.get(0);
        Hospital nearestAndEmptyHospital = hospitals.get(5);
        DijkstrasAlgorithm dijkstrasAlgorithm = new DijkstrasAlgorithm(hospitals);

        Assert.assertEquals(dijkstrasAlgorithm.getNearestEmpty(startingHospital), nearestAndEmptyHospital);
    }

    @Test
    public void getNearestAndEmpty_sampleDataGiven_shortestPathGot() {
        Hospital startingHospital = hospitals.get(0);
        List<Hospital> shortestPath = new LinkedList<>(List.of(hospitals.get(0), hospitals.get(1), hospitals.get(3)));
        DijkstrasAlgorithm dijkstrasAlgorithm = new DijkstrasAlgorithm(hospitals);

        Assert.assertEquals(dijkstrasAlgorithm.getNearestEmpty(startingHospital).getShortestPath(), shortestPath);
    }

    @Test
    public void testClear() {
        DijkstrasAlgorithm dijkstrasAlgorithm = new DijkstrasAlgorithm(hospitals);
        Hospital startingHospital = hospitals.get(0);
        Hospital nearestAndEmpty = dijkstrasAlgorithm.getNearestEmpty(startingHospital);

        dijkstrasAlgorithm.clear();
        Graph graphAfterClearing = dijkstrasAlgorithm.getGraph();
        Set<Hospital> hospitalsAfterClearing = graphAfterClearing.getNodes();

        for (Hospital h : hospitalsAfterClearing) {
            Assert.assertTrue(h.getShortestPath().isEmpty());
        }
    }
}