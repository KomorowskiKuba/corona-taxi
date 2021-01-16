import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class IntersectionTest {
    List<Hospital> hospitalList;
    List<Road> roadList;
    List<Hospital> intersectionList;
    Intersection intersection;

    @BeforeTest
    public void initialize() {
        intersection = new Intersection();
        hospitalList = new ArrayList<>();
        roadList = new ArrayList<>();
        intersectionList = new ArrayList<>();

        Hospital hospital1 = new Hospital(1, "Szpital Wojewódzki nr 997", 10, 10, 1000, 100);
        Hospital hospital2 = new Hospital(2, "Krakowski Szpital Kliniczny", 100, 120, 999, 99);
        Hospital hospital3 = new Hospital(3, "Pierwszy Szpital im. Prezesa RP", 120, 130, 99, 0);
        Hospital hospital4 = new Hospital(4, "Drugi Szpital im. Naczelnika RP", 10, 140, 70, 1);
        Hospital hospital5 = new Hospital(5, "Trzeci Szpital im. Króla RP", 140, 10, 996, 0);
        hospitalList.add(hospital1);
        hospitalList.add(hospital2);
        hospitalList.add(hospital3);
        hospitalList.add(hospital4);
        hospitalList.add(hospital5);

        Road road1 = new Road(1, hospital1, hospital2, 700);
        Road road2 = new Road(2, hospital1, hospital4, 550);
        Road road3 = new Road(3, hospital1, hospital5, 800);
        Road road4 = new Road(4, hospital2, hospital3, 300);
        Road road5 = new Road(5, hospital2, hospital4, 550);
        Road road6 = new Road(6, hospital3, hospital5, 600);
        Road road7 = new Road(7, hospital4, hospital5, 750);
        roadList.add(road1);
        roadList.add(road2);
        roadList.add(road3);
        roadList.add(road4);
        roadList.add(road5);
        roadList.add(road6);
        roadList.add(road7);

    }

    @Test
    public void testGetRoadByHospitals() {
        Road expected = roadList.get(0);

        Road result = intersection.getRoadByHospitals(roadList, hospitalList.get(0), hospitalList.get(1));

        Assert.assertEquals(result, expected);
    }

    @Test(priority = 1)
    public void testDoIntersect_shouldReturnTrue() {
        assertTrue(Intersection.doIntersect(intersection.createPoint(10, 10), intersection.createPoint(100, 100), intersection.createPoint(100, 10), intersection.createPoint(10, 100)));
    }

    @Test(priority = 1)
    public void testDoIntersect_shouldReturnFalse() {
        assertFalse(Intersection.doIntersect(intersection.createPoint(10, 10), intersection.createPoint(100, 100), intersection.createPoint(11, 10), intersection.createPoint(101, 100)));
    }

    @Test(priority = 1)
    public void testFindCrossPoint() {
        double[] expected = {55.0, 55.0};

        double[] result = Intersection.findCrossPoint(intersection.createPoint(10, 10), intersection.createPoint(100, 100), intersection.createPoint(100, 10), intersection.createPoint(10, 100));

        assertTrue(expected[0] == result[0] && expected[1] == result[1]);
    }

    @Test(priority = 1)
    public void testFindIntersections() {
        Hospital expected = new Hospital(6, "Intersection 1", 68.5, 81.5, 0, 0);
        Hospital result;

        intersection.findIntersections(hospitalList, intersectionList, roadList);
        result = intersectionList.get(intersectionList.size() - 1);

        Assert.assertTrue(expected.getId() == result.getId() && expected.getX() == result.getX() && expected.getY() == result.getY());
    }
}