import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class HospitalTest {
    ArrayList<Hospital> hospitals = new ArrayList<>();

    @BeforeTest
    private void initialize() {
        hospitals.add(new Hospital(0, "Szpital Wojewódzki nr 997", 10, 10, 1000, 100));
        hospitals.add(new Hospital(1, "Krakowski Szpital Kliniczny", 100, 120, 999, 99));
        hospitals.add(new Hospital(2, "Pierwszy Szpital im. Prezesa RP", 120, 130, 99, 0));
    }

    @Test
    public void testFindHospitalById() {
        Hospital foundHospital = Hospital.findHospitalById(hospitals, 1);

        Assert.assertEquals(hospitals.get(1), foundHospital);
    }

    @Test
    public void testFindHospitalByCoordinates() {
        Hospital foundHospital = Hospital.findHospitalByCoordinates(hospitals, 10, 10);

        Assert.assertEquals(hospitals.get(0), foundHospital);
    }

    @Test
    public void testBringPatient() {
        int emptyBedsBefore = hospitals.get(0).getEmptyBeds();
        hospitals.get(0).bringPatient();
        int emptyBedsAfter = hospitals.get(0).getEmptyBeds();

        Assert.assertEquals(emptyBedsBefore - 1, emptyBedsAfter);
    }
}