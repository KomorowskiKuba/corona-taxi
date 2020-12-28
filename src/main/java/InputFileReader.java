import java.io.File;
import java.io.FileNotFoundException;
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

    public InputFileReader(String filePath, boolean readPatients) throws IllegalFormatException, FileNotFoundException {
        hospitalList = new ArrayList<>();
        monumentList = new ArrayList<>();
        roadList = new ArrayList<>();
        patientList = new ArrayList<>();

        if (checkFileFormat(filePath)) {
            readFromFile(filePath, readPatients);
        } else {
            throw new IllegalArgumentException("Bledny format pliku wejsciowego! Plik wejsciowy powinien byc zapisany w formacie \".txt\"");
        }
    }

    private static void readFromFile(String filePath, boolean readPatients) throws FileNotFoundException {
        File inputFile = new File(filePath);
        Scanner scanner;
        String line;
        int lineNumber = 0;

        try {
            scanner = new Scanner(inputFile);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Nie odnaleziono pliku: " + filePath +"! Podaj prawidlowa nazwe!");
        }

        if (scanner.hasNextLine()) {
            line = scanner.nextLine();
            ++lineNumber;
        } else {
            throw new IllegalArgumentException("Brak linii do wczytania!");
        }

        if (readPatients) {
            if (checkIfLineIsAComment(line, patientsComment)) {
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    ++lineNumber;
                    StringTokenizer stringTokenizer = new StringTokenizer(line, delimiter);
                    if (line.charAt(0) == '#') {
                        break;
                    } else {
                        if (stringTokenizer.countTokens() == 3) {
                            try {
                                int id = Integer.parseInt(removeSpaces(stringTokenizer.nextToken()));
                                int x = Integer.parseInt(removeSpaces(stringTokenizer.nextToken()));
                                int y = Integer.parseInt(removeSpaces(stringTokenizer.nextToken()));
                                patientList.add(new Patient(id, x, y));
                            } catch (NumberFormatException nfe) {
                                throw new NumberFormatException("Niepoprawne dane w pliku wejsciowym, linia: " + lineNumber + "!");
                            }
                        } else {
                            throw new IllegalArgumentException("Nieprawidlowa ilosc atrybutow w linii: " + lineNumber + "! Popraw strukture pliku!");
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("Nieprawidłowe dane wejsciowe! Sprawdz poprawnosc danych w pliku wejsciowym!");
            }
        } else {
            if (checkIfLineIsAComment(line, hospitalsComment)) {
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    ++lineNumber;
                    StringTokenizer stringTokenizer = new StringTokenizer(line, delimiter);

                    if (line.charAt(0) == '#') {
                        break;
                    } else {
                        if (stringTokenizer.countTokens() == 6) {
                            try {
                                int id = Integer.parseInt(removeSpaces(stringTokenizer.nextToken()));
                                String name = removeSpaces(stringTokenizer.nextToken());
                                int x = Integer.parseInt(removeSpaces(stringTokenizer.nextToken()));
                                int y = Integer.parseInt(removeSpaces(stringTokenizer.nextToken()));
                                int amountOfTotalBeds = Integer.parseInt(removeSpaces(stringTokenizer.nextToken()));
                                int amountOfFreeBeds = Integer.parseInt(removeSpaces(stringTokenizer.nextToken()));

                                hospitalList.add(new Hospital(id, name, x, y, amountOfTotalBeds, amountOfFreeBeds));
                            } catch (NumberFormatException nfe) {
                                throw new NumberFormatException("Niepoprawne dane w pliku wejsciowym, linia: " + lineNumber + "!");
                            }

                        } else {
                            throw new IllegalArgumentException("Nieprawidlowa ilosc atrybutow w linii: " + lineNumber + "! Popraw strukture pliku!");
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("Nieprawidłowe dane wejsciowe! Sprawdz poprawnosc danych w pliku wejsciowym!");
            }

            if (checkIfLineIsAComment(line, monumentsComment)) {
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    ++lineNumber;
                    StringTokenizer stringTokenizer = new StringTokenizer(line, delimiter);
                    if (line.charAt(0) == '#') {
                        break;
                    } else {
                        if (stringTokenizer.countTokens() == 4) {
                            try {
                                int id = Integer.parseInt(removeSpaces(stringTokenizer.nextToken()));
                                String name = removeSpaces(stringTokenizer.nextToken());
                                int x = Integer.parseInt(removeSpaces(stringTokenizer.nextToken()));
                                int y = Integer.parseInt(removeSpaces(stringTokenizer.nextToken()));

                                monumentList.add(new Monument(id, name, x, y));
                            } catch (NumberFormatException nfe) {
                                throw new NumberFormatException("Niepoprawne dane w pliku wejsciowym, linia: " + lineNumber + "!");
                            }
                        } else {
                            throw new IllegalArgumentException("Nieprawidlowa ilosc atrybutow w linii: " + lineNumber + "! Popraw strukture pliku!");
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("Nieprawidłowe dane wejsciowe! Sprawdz poprawnosc danych w pliku wejsciowym!");
            }

            if (checkIfLineIsAComment(line, roadsComment)) {
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    ++lineNumber;
                    StringTokenizer stringTokenizer = new StringTokenizer(line, delimiter);
                    if (line.charAt(0) == '#') {
                        break;
                    } else {
                        if (stringTokenizer.countTokens() == 4) {
                            try {
                                int roadId = Integer.parseInt(removeSpaces(stringTokenizer.nextToken()));
                                int firstHospitalId = Integer.parseInt(removeSpaces(stringTokenizer.nextToken()));
                                int secondHospitalId = Integer.parseInt(removeSpaces(stringTokenizer.nextToken()));
                                int distance = Integer.parseInt(removeSpaces(stringTokenizer.nextToken()));

                                roadList.add(new Road(roadId, firstHospitalId, secondHospitalId, distance));
                            } catch (NumberFormatException nfe) {
                                throw new NumberFormatException("Niepoprawne dane w pliku wejsciowym, linia: " + lineNumber + "!");
                            }
                        } else {
                            throw new IllegalArgumentException("Nieprawidlowa ilosc atrybutow w linii: " + lineNumber + "! Popraw strukture pliku!");
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("Nieprawidłowe dane wejsciowe! Sprawdz poprawnosc danych w pliku wejsciowym!");
            }
        }
    }

    private static boolean checkIfLineIsAComment(String line, String comment) {
        if (line.charAt(0) == '#') {
            if (line.equals(comment)) {
                return true;
            } else {
                throw new IllegalArgumentException("Linia z komenatrzem nie jest taka jak oczekiwano!");
            }
        } else {
            return false;
        }
    }

    private static String removeSpaces(String word) {
        String trimmedWord = word;

        if (word.charAt(0) == ' ') {
            trimmedWord = trimmedWord.stripLeading();
        }
        if (word.charAt(word.length() - 1) == ' ') {
            trimmedWord = trimmedWord.stripTrailing();
        }

        return trimmedWord;
    }

    public List<Hospital> getHospitalList() {
        return hospitalList;
    }

    public List<Monument> getMonumentList() {
        return monumentList;
    }

    public List<Road> getRoadList() {
        return roadList;
    }

    public List<Patient> getPatientList() {
        return patientList;
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
        String location2 = "src/data/sampleData.txt";
        InputFileReader ifr = null;

        try {
            ifr = new InputFileReader(location2, false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("HOSPITALS: ");
        for (Hospital h : Objects.requireNonNull(ifr).getHospitalList()) {
            System.out.println(h.toString());
        }
        System.out.println("===========================================");
        System.out.println("MONUMENTS: ");
        for (Monument m : Objects.requireNonNull(ifr).getMonumentList()) {
            System.out.println(m.toString());
        }
        System.out.println("===========================================");
        System.out.println("ROADS: ");
        for (Road r : Objects.requireNonNull(ifr).getRoadList()) {
            System.out.println(r.toString());
        }
        System.out.println("===========================================");
        System.out.println("PATIENTS: ");
        for (Patient p : Objects.requireNonNull(ifr).getPatientList()) {
            System.out.println(p.toString());
        }
        System.out.println("===========================================");

    }
}
