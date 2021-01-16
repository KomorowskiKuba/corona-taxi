import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ConvexHullTest {
    List<Hospital> hospitalList;
    List<Monument> monumentList;
    Double[] inputPoints1;
    Double[] inputPoints2;
    Double[] expectedOutputPoints1;
    Double[] expectedOutputPoints2;
    ArrayList<Double> borders;
    @BeforeTest
    public void initialize() throws FileNotFoundException {
        InputFileReader inputFileReader = new InputFileReader("src/data/sampleData.txt", false);
        hospitalList = Objects.requireNonNull(inputFileReader).getHospitalList();
        monumentList = Objects.requireNonNull(inputFileReader).getMonumentList();
        inputPoints1 = new Double[]{7.0, 7.0, 7.0, -7.0, -7.0, -7.0, -7.0, 7.0, 9.0, 0.0, -9.0, 0.0, 0.0, 9.0, 0.0, -9.0};
        expectedOutputPoints1 = new Double[]{-9.0, 0.0, -7.0, -7.0, 0.0, -9.0, 7.0, -7.0, 9.0, 0.0, 7.0, 7.0, 0.0, 9.0, -7.0, 7.0};
        inputPoints2 = new Double[]{7.0, 7.0, 7.0, -7.0, -7.0, -7.0, -7.0, 7.0, 9.0, 0.0, -9.0, 0.0, 0.0, 9.0, 0.0, -9.0, 0.0, 0.0, 1.0, 2.0, -2.0, 1.0, -1.0, -1.0, 3.0, 4.0, 4.0, 3.0, -5.0, 4.0, 6.0, 5.0};
        expectedOutputPoints2 = new Double[]{-9.0, 0.0, -7.0, -7.0, 0.0, -9.0, 7.0, -7.0, 9.0, 0.0, 7.0, 7.0, 0.0, 9.0, -7.0, 7.0};
        borders = ConvexHull.convex_hull(hospitalList,monumentList);
    }

    @Test
    public void testConvexHull1() {
        List<Double> expectedResult;
        List<Double> result = new ArrayList<>();
        List<Hospital> hospitalList = new ArrayList<>();
        List<Monument> monumentList = new ArrayList<>();


        for(int i = 0; i < inputPoints1.length / 2; i += 2){
            hospitalList.add(new Hospital(i + 1, "Hospital", inputPoints1[i], inputPoints1[i+1], 0, 0));
        }

        for(int i = inputPoints1.length / 2; i < inputPoints1.length; i += 2){
            monumentList.add(new Monument(i + 1, "Monument", inputPoints1[i], inputPoints1[i+1]));
        }

        expectedResult= new ArrayList<Double>(Arrays.asList(expectedOutputPoints1));
        result = ConvexHull.convex_hull(hospitalList, monumentList);

        Assert.assertEquals(result, expectedResult);
    }

    @Test
    public void testConvexHull2() {
        List<Double> expectedResult;
        List<Double> result = new ArrayList<>();
        List<Hospital> hospitalList = new ArrayList<>();
        List<Monument> monumentList = new ArrayList<>();


        for(int i = 0; i < inputPoints2.length / 2; i += 2){
            hospitalList.add(new Hospital(i + 1, "Hospital", inputPoints2[i], inputPoints2[i+1], 0, 0));
        }

        for(int i = inputPoints2.length / 2; i < inputPoints2.length; i += 2){
            monumentList.add(new Monument(i + 1, "Monument", inputPoints2[i], inputPoints2[i+1]));
        }

        expectedResult= new ArrayList<Double>(Arrays.asList(expectedOutputPoints2));
        result = ConvexHull.convex_hull(hospitalList, monumentList);

        Assert.assertEquals(result, expectedResult);
    }

    @Test
    public void testIsInBorder_shouldReturnTrue(){
        List<Point> patientList = new ArrayList<>();
        for (int i = 1; i <= 10; i++){
            patientList.add(new Point(i*11.0, i + 15.0));
        }

        for (int i = 0; i < 10; i++){
            Assert.assertEquals(ConvexHull.isInBorder(borders, patientList.get(i)), true);
        }
    }

    @Test
    public void testIsInBorder_shouldReturnFalse(){
        List<Point> patientList = new ArrayList<>();
        for (int i = 1; i <= 10; i++){
            patientList.add(new Point(i, i - 15.0));
        }

        for (int i = 0; i < 10; i++){
            Assert.assertEquals(ConvexHull.isInBorder(borders, patientList.get(i)), false);
        }
    }
}