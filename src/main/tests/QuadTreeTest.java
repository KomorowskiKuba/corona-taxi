import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class QuadTreeTest {
    ArrayList<Hospital> hospitals = new ArrayList<>();
    ArrayList<Double> borders = new ArrayList<>();
    QuadTree qTree = new QuadTree();

    @BeforeTest
    private void initialize() {
        hospitals.add(new Hospital(0, "Szpital Wojewódzki nr 997", 0, 0, 1000, 100));
        hospitals.add(new Hospital(1, "Krakowski Szpital Kliniczny", 100, 100, 999, 99));
        hospitals.add(new Hospital(2, "Pierwszy Szpital im. Prezesa RP", 50, 50, 99, 0));

        for (Hospital h : hospitals) {
            borders.add(h.getX());
            borders.add(h.getY());
        }
    }

    @Test
    public void testCalcQuadrant() {
        Area expectedArea = new Area(0, 0, 100, 100);

        Area testedArea = QuadTree.calcQuadrant(borders);

        Assert.assertTrue(expectedArea.equals(testedArea));
    }

    @Test
    public void testFindNearest() {
        Patient p = new Patient(0, 10, 10);

        Area area = new Area(0, 0, 100, 100);
        qTree.fillTree(hospitals, area);
        int nearestId = qTree.findNearest(p);

        Assert.assertEquals(0, nearestId);
    }
}