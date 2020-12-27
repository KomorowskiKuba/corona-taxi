import java.util.*;

public class InputFileReader {
    private static final String hospitalsComment = "# Szpitale (id | nazwa | wsp. x | wsp. y | Liczba łóżek | Liczba wolnych łóżek)";
    private static final String monumentsComment = "# Obiekty (id | nazwa | wsp. x | wsp. y)";
    private static final String roadsComment = "# Drogi (id | id_szpitala | id_szpitala | odległość)";
    private static final String patientsComment = "# Pacjenci (id | wsp. x | wsp.y)";

    private static final String delimiter = "|";
    private static final String acceptedFormat = "txt";

    private static List<Hospital> hospitalList;
    private static List<Monument> monumentList;
    private static List<Road> roadList;
    private static List<Patient> patientList;

    public InputFileReader(String filePath) throws IllegalFormatException {
        hospitalList = new ArrayList<>();
        monumentList = new ArrayList<>();
        roadList = new ArrayList<>();
        patientList = new ArrayList<>();

        if (checkFileFormat(filePath)) {
            System.out.println("XD");
        } else {
            throw new IllegalArgumentException("Bledny format pliku wejsciowego! Plik wejsciowy powinien byc zapisany w formacie \".txt\"");
        }
    }

    private static void readFromFile(String filePath) {
        
    }

    private boolean checkFileFormat(String fileName) {
        StringTokenizer stringTokenizer = new StringTokenizer(fileName, ".");
        List<String> tokens = new ArrayList<>();

        while (stringTokenizer.hasMoreTokens()) {
            tokens.add(stringTokenizer.nextToken());
        }

        return tokens.get(tokens.size() - 1).equals(acceptedFormat);
    }

    public static void main(String[] args) {
        String location1 = "C:\\Users\\korni\\OneDrive\\Pulpit\\data.kot.cmdddd";
        String location2 = "sampleData.txt";
        InputFileReader ifr = new InputFileReader(location2);
    }
}
