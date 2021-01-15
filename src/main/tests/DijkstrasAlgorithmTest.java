import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DijkstrasAlgorithmTest {
    private List<Hospital> hospitalsTestOne;
    private List<Hospital> hospitalsTestTwo;

    @BeforeTest
    public void initiateTestDataOne() {
        Hospital node1 = new Hospital(1, "Hospital1", 1, 1, 100, 0);
        Hospital node2 = new Hospital(2, "Hospital2", 2, 2, 100, 0);
        Hospital node3 = new Hospital(3, "Hospital3", 3, 3, 100, 0);
        Hospital node4 = new Hospital(4, "Hospital4", 4, 4, 100, 0);
        Hospital node5 = new Hospital(5, "Hospital5", 5, 5, 100, 0);
        Hospital node6 = new Hospital(6, "Hospital6", 6, 6, 100, 100);

        node1.addDestination(node2, 110);
        node1.addDestination(node3, 165);
        node2.addDestination(node4, 132);
        node2.addDestination(node6, 165);
        node3.addDestination(node5, 110);
        node4.addDestination(node5, 22);
        node4.addDestination(node6, 11);
        node6.addDestination(node5, 55);

        hospitalsTestOne = new ArrayList<>(List.of(node1, node2, node3, node4, node5, node6));
    }

    @BeforeTest
    public void initiatedTestDataTwo() {
        Hospital node1 = new Hospital(1, "Hospital1", 1, 1, 999, 0);
        Hospital node2 = new Hospital(2, "Hospital2", 2, 2, 999, 0);
        Hospital node3 = new Hospital(3, "Hospital3", 3, 3, 999, 0);
        Hospital node4 = new Hospital(4, "Hospital4", 4, 4, 999, 100);
        Hospital node5 = new Hospital(5, "Hospital5", 5, 5, 999, 0);

        node1.addDestination(node2, 700);
        node2.addDestination(node1, 700);
        node1.addDestination(node4, 550);
        node4.addDestination(node1, 550);
        node1.addDestination(node5, 800);
        node5.addDestination(node1, 800);
        node2.addDestination(node3, 300);
        node3.addDestination(node2, 300);
        node2.addDestination(node4, 550);
        node4.addDestination(node2, 550);
        node3.addDestination(node5, 600);
        node3.addDestination(node5, 600);
        node4.addDestination(node5, 750);
        node5.addDestination(node4, 750);

        hospitalsTestTwo = new ArrayList<>(List.of(node1, node2, node3, node4, node5));
    }

    @Test
    public void getNearestAndEmpty_sampleDataOneGiven_nearestEmptyHospitalGot() {
        Hospital startingHospital = hospitalsTestOne.get(0);
        Hospital nearestAndEmptyHospital = hospitalsTestOne.get(5);
        DijkstrasAlgorithm dijkstrasAlgorithm = new DijkstrasAlgorithm(hospitalsTestOne);

        Assert.assertEquals(dijkstrasAlgorithm.getNearestEmpty(startingHospital), nearestAndEmptyHospital);
    }

    @Test
    public void getNearestAndEmpty_sampleDataOneGiven_shortestPathGot() {
        Hospital startingHospital = hospitalsTestOne.get(0);
        List<Hospital> shortestPath = new LinkedList<>(List.of(hospitalsTestOne.get(0), hospitalsTestOne.get(1), hospitalsTestOne.get(3)));
        DijkstrasAlgorithm dijkstrasAlgorithm = new DijkstrasAlgorithm(hospitalsTestOne);

        Assert.assertEquals(dijkstrasAlgorithm.getNearestEmpty(startingHospital).getShortestPath(), shortestPath);
    }

    @Test
    public void getNearestAndEmpty_sampleDataTwoGiven_nearestEmptyHospitalGot() {
        Hospital startingHospital = hospitalsTestTwo.get(2);
        Hospital nearestAndEmptyHospital = hospitalsTestTwo.get(3);
        DijkstrasAlgorithm dijkstrasAlgorithm = new DijkstrasAlgorithm(hospitalsTestTwo);

        Assert.assertEquals(dijkstrasAlgorithm.getNearestEmpty(startingHospital), nearestAndEmptyHospital);
    }

    @Test
    public void getNearestAndEmpty_sampleDataTwoGiven_shortestPathGot() {
        Hospital startingHospital = hospitalsTestTwo.get(2);
        List<Hospital> shortestPath = new LinkedList<>(List.of(hospitalsTestTwo.get(2), hospitalsTestTwo.get(1)));
        DijkstrasAlgorithm dijkstrasAlgorithm = new DijkstrasAlgorithm(hospitalsTestTwo);

        Assert.assertEquals(dijkstrasAlgorithm.getNearestEmpty(startingHospital).getShortestPath(), shortestPath);
    }

    @Test
    public void testClear() {
        DijkstrasAlgorithm dijkstrasAlgorithm = new DijkstrasAlgorithm(hospitalsTestOne);
        Hospital startingHospital = hospitalsTestOne.get(0);
        Hospital nearestAndEmpty = dijkstrasAlgorithm.getNearestEmpty(startingHospital);

        dijkstrasAlgorithm.clear();

        Graph graphAfterClearing = dijkstrasAlgorithm.getGraph();
        Set<Hospital> hospitalsAfterClearing = graphAfterClearing.getNodes();

        for (Hospital h : hospitalsAfterClearing) {
            Assert.assertTrue(h.getShortestPath().isEmpty());
        }
    }
}