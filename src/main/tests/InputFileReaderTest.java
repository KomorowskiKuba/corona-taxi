import org.testng.annotations.Test;
import org.testng.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class InputFileReaderTest {
    private final String nonExistentFilePath = "src/main/tests/test_data/non_existent_file1234.txt";
    private final String filePathWithWrongAmountOfArguments = "src/main/tests/test_data/wrong_amount_of_args_file.txt";
    private final String filePathWithWrongNumberFormats = "src/main/tests/test_data/wrong_formats_file.txt";

    private final int wrongAmountOfArgumentsLineNum = 2;
    private final int wrongNumberFormatsLineNum = 6;

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Nie podano nazwy pliku wejsciowego!")
    public void readFromFile_givenEmptyFilePath_IllegalArgumentExceptionThrown() throws FileNotFoundException {
        String filePath = "";
        boolean readPatients = false;

        InputFileReader inputFileReader = new InputFileReader(filePath, readPatients);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Bledny format pliku wejsciowego!\nPlik wejsciowy powinien byc zapisany w formacie \".txt\"")
    public void readFromFile_givenFileWithWrongExtension_IllegalArgumentExceptionThrown() throws FileNotFoundException {
        String filePath = "src/main/tests/test_data/wrong_format_file.jpg";
        boolean readPatients = false;

        InputFileReader inputFileReader = new InputFileReader(filePath, readPatients);
    }

    @Test(expectedExceptions = FileNotFoundException.class, expectedExceptionsMessageRegExp = "Nie odnaleziono pliku: " + nonExistentFilePath + "!\nPodaj prawidlowa nazwe!")
    public void readFromFile_givenNonExistentsFilePath_IllegalArgumentExceptionThrown() throws FileNotFoundException {
        boolean readPatients = false;

        InputFileReader inputFileReader = new InputFileReader(nonExistentFilePath, readPatients);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Nieprawidlowa ilosc atrybutow w pliku wejsciowym: " + filePathWithWrongAmountOfArguments + " ,w linii: " + wrongAmountOfArgumentsLineNum + "!\nPopraw strukture pliku!")
    public void readFromFile_givenFileWithWrongAmountOfArgumentsInLine_IllegalArgumentExceptionThrown() throws FileNotFoundException {
        boolean readPatients = false;

        InputFileReader inputFileReader = new InputFileReader(filePathWithWrongAmountOfArguments, readPatients);
    }

    @Test(expectedExceptions = NumberFormatException.class, expectedExceptionsMessageRegExp = "Niepoprawne dane w pliku wejsciowym: " + filePathWithWrongNumberFormats + " , linia: " + wrongNumberFormatsLineNum + "!")
    public void readFromFile_givenFileWrongNumberFormats_IllegalArgumentExceptionThrown() throws FileNotFoundException {
        boolean readPatients = false;

        InputFileReader inputFileReader = new InputFileReader(filePathWithWrongNumberFormats, readPatients);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "W pliku wejsciowym nie podano zadnych szpitali!")
    public void readFromFile_givenFileWithNoHospitals_IllegalArgumentExceptionThrown() throws FileNotFoundException {
        String missingDataFilePath = "src/main/tests/test_data/missing_hospitals_file.txt";
        boolean readPatients = false;

        InputFileReader inputFileReader = new InputFileReader(missingDataFilePath, readPatients);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Nieprawidłowe dane wejsciowe!\nSprawdz poprawnosc danych w pliku wejsciowym!")
    public void readFromFile_givenFileWithWrongComment_IllegalArgumentExceptionThrown() throws FileNotFoundException {
        String filePath = "src/main/tests/test_data/wrong_comments_file.txt";
        boolean readPatients = true;

        InputFileReader inputFileReader = new InputFileReader(filePath, readPatients);
    } //TODO: KODOWANIE GLUPIE

    @Test
    public void readFromFile_givenNormalFile_properListsGot() throws FileNotFoundException {
        String filePath = "src/main/tests/test_data/proper_data_file.txt";
        boolean readPatients = false;

        Hospital h1 = new Hospital(1, "Szpital Wojewódzki nr 997", 10, 10, 1000, 100);
        Hospital h2 = new Hospital(2, "Krakowski Szpital Kliniczny", 100, 120, 999, 99);
        Hospital h3 = new Hospital(3, "Pierwszy Szpital im. Prezesa RP", 120, 130, 99, 0);

        Monument m1 = new Monument(1, "Pomnik Wikipedii", -1, 50);
        Monument m2 = new Monument(2, "Pomnik Fryderyka Chopina", 110, 55);
        Monument m3 = new Monument(3, "Pomnik Anonimowego Przechodnia", 40, 70);

        Road r1 = new Road(1, h1, h2, 700);
        Road r2 = new Road(2, h2, h3, 550);
        Road r3 = new Road(3, h3, h1, 800);

        List<Hospital> hospitals = new ArrayList<>(List.of(h1, h2, h3));
        List<Monument> monuments = new ArrayList<>(List.of(m1, m2, m3));
        List<Road> roads = new ArrayList<>(List.of(r1, r2, r3));

        InputFileReader inputFileReader = new InputFileReader(filePath, readPatients);

        Assert.assertEquals(inputFileReader.getHospitalList(), hospitals);
        Assert.assertEquals(inputFileReader.getMonumentList(), monuments);
        Assert.assertEquals(inputFileReader.getRoadList(), roads);
    }
}