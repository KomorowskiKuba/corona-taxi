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
    private static List<Hospital> hospitalAndIntersectionList;

    private static final int X_LIMIT = 100_000_000;
    private static final int Y_LIMIT = 100_000_000;
    private static final int ID_LIMIT = 100_000_000;
    private static final int ALL_BEDS_LIMIT = 1_000_000;
    private static final int FREE_BEDS_LIMIT = 1_000_000;
    private static final int DISTANCE_LIMIT = 10_000_000;

    public InputFileReader(String filePath, boolean readPatients) throws IllegalFormatException, FileNotFoundException, NullPointerException {
        hospitalList = new ArrayList<>();
        monumentList = new ArrayList<>();
        roadList = new ArrayList<>();
        patientList = new ArrayList<>();
        hospitalAndIntersectionList = new ArrayList<>();

        if (filePath.isEmpty()) {
            throw new IllegalArgumentException("Nie podano nazwy pliku wejsciowego!");
        }

        if (filePath.equals(".")) {
            throw new IllegalArgumentException("Błędna nazwa pliku wejściowego!");
        }

        if (checkFileFormat(filePath)) {
            readFromFile(filePath, readPatients);
        } else {
            throw new IllegalArgumentException("Bledny format pliku wejsciowego!\nPlik wejsciowy powinien byc zapisany w formacie \".txt\"");
        }

        if ((hospitalList.size() == 0 || monumentList.size() == 0 || roadList.size() == 0) && !readPatients) {
            throw new NullPointerException("W pliku " + filePath + " brakuje danych!");
        } else if (patientList.size() == 0 && readPatients) {
            throw new NullPointerException("W pliku " + filePath + " brakuje danych!");
        }

        initializeHospitalAndIntersectionList();

        Intersection intersection = new Intersection();

        intersection.findIntersections(hospitalList, hospitalAndIntersectionList, roadList);
    }

    private static void readFromFile(String filePath, boolean readPatients) throws FileNotFoundException {
        int lineNumber = 0;

        File inputFile = new File(filePath);
        Scanner scanner;
        String line;

        try {
            scanner = new Scanner(inputFile);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Nie odnaleziono pliku: " + filePath + "!\nPodaj prawidlowa nazwe!");
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
                    if (line.isEmpty()) {
                        continue;
                    }
                    if (line.charAt(0) == '#') {
                        break;
                    } else {
                        if (stringTokenizer.countTokens() == 3) {
                            try {
                                int id = Integer.parseInt(removeSpaces(stringTokenizer.nextToken()));
                                int x = Integer.parseInt(removeSpaces(stringTokenizer.nextToken()));
                                int y = Integer.parseInt(removeSpaces(stringTokenizer.nextToken()));

                                Patient newPatient = new Patient(id, x, y);

                                if (checkBasicParameters(newPatient) && !patientList.contains(newPatient)) {
                                    patientList.add(newPatient);
                                } else {
                                    throw new IllegalArgumentException("Pacjent nr: " + newPatient.getId() + " przekroczyl mozliwe limity danych lub znajduje sie juz w liscie pacjentow!");
                                }
                            } catch (NumberFormatException nfe) {
                                throw new NumberFormatException("Niepoprawne dane w pliku wejsciowym: " + filePath + " , linia: " + lineNumber + "!");
                            }
                        } else {
                            throw new IllegalArgumentException("Nieprawidlowa ilosc atrybutow w pliku wejsciowym: " + filePath + " ,w linii: " + lineNumber + "!\nPopraw strukture pliku!");
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("Nieprawidłowe dane wejsciowe!\nSprawdz poprawnosc danych w pliku wejsciowym!");
            }
        } else {
            if (checkIfLineIsAComment(line, hospitalsComment)) {
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    ++lineNumber;

                    StringTokenizer stringTokenizer = new StringTokenizer(line, delimiter);

                    if (line.isEmpty()) {
                        continue;
                    }
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

                                Hospital newHospital = new Hospital(id, name, x, y, amountOfTotalBeds, amountOfFreeBeds);

                                if (checkBasicParameters(newHospital) && !hospitalList.contains(newHospital) &&
                                        newHospital.getEmptyBeds() < FREE_BEDS_LIMIT && newHospital.getTotalBeds() < ALL_BEDS_LIMIT && newHospital.getEmptyBeds() <= newHospital.getTotalBeds()) {
                                    hospitalList.add(newHospital);
                                } else {
                                    throw new IllegalArgumentException("Szpital nr: " + newHospital.getId() + " przekroczyl mozliwe limity danych lub znajduje sie juz w liscie szpitali!");
                                }
                            } catch (NumberFormatException nfe) {
                                throw new NumberFormatException("Niepoprawne dane w pliku wejsciowym: " + filePath + " , linia: " + lineNumber + "!");
                            }
                        } else {
                            throw new IllegalArgumentException("Nieprawidlowa ilosc atrybutow w pliku wejsciowym: " + filePath + " ,w linii: " + lineNumber + "!\nPopraw strukture pliku!");
                        }
                    }
                }
                if (hospitalList.isEmpty()) {
                    throw new IllegalArgumentException("W pliku wejsciowym nie podano zadnych szpitali!");
                }
            } else {
                throw new IllegalArgumentException("Nieprawidłowe dane wejsciowe!\nSprawdz poprawnosc danych w pliku wejsciowym!");
            }

            if (checkIfLineIsAComment(line, monumentsComment)) {
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    ++lineNumber;

                    StringTokenizer stringTokenizer = new StringTokenizer(line, delimiter);

                    if (line.isEmpty()) {
                        continue;
                    }
                    if (line.charAt(0) == '#') {
                        break;
                    } else {
                        if (stringTokenizer.countTokens() == 4) {
                            try {
                                int id = Integer.parseInt(removeSpaces(stringTokenizer.nextToken()));
                                String name = removeSpaces(stringTokenizer.nextToken());
                                int x = Integer.parseInt(removeSpaces(stringTokenizer.nextToken()));
                                int y = Integer.parseInt(removeSpaces(stringTokenizer.nextToken()));

                                Monument newMonument = new Monument(id, name, x, y);

                                if (checkBasicParameters(newMonument) && !monumentList.contains(newMonument)) {
                                    monumentList.add(new Monument(id, name, x, y));
                                } else {
                                    throw new IllegalArgumentException("Monument nr: " + newMonument.getId() + " przekroczyl mozliwe limity danych lub znajduje sie juz w liscie monumentow!");
                                }
                            } catch (NumberFormatException nfe) {
                                throw new NumberFormatException("Niepoprawne dane w pliku wejsciowym: " + filePath + " , linia: " + lineNumber + "!");
                            }
                        } else {
                            throw new IllegalArgumentException("Nieprawidlowa ilosc atrybutow w pliku wejsciowym: " + filePath + " ,w linii: " + lineNumber + "!\nPopraw strukture pliku!");
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("Nieprawidłowe dane wejsciowe!\nSprawdz poprawnosc danych w pliku wejsciowym!");
            }

            if (checkIfLineIsAComment(line, roadsComment)) {
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    ++lineNumber;

                    StringTokenizer stringTokenizer = new StringTokenizer(line, delimiter);

                    if (line.isEmpty()) {
                        continue;
                    }
                    if (line.charAt(0) == '#') {
                        break;
                    } else {
                        if (stringTokenizer.countTokens() == 4) {
                            try {
                                int roadId = Integer.parseInt(removeSpaces(stringTokenizer.nextToken()));
                                int firstHospitalId = Integer.parseInt(removeSpaces(stringTokenizer.nextToken()));
                                int secondHospitalId = Integer.parseInt(removeSpaces(stringTokenizer.nextToken()));
                                int distance = Integer.parseInt(removeSpaces(stringTokenizer.nextToken()));

                                Hospital firstHospital = Hospital.findHospitalById(hospitalList, firstHospitalId);
                                Hospital secondHospital = Hospital.findHospitalById(hospitalList, secondHospitalId);

                                if (firstHospital != null && secondHospital != null) {
                                    Road newRoad = new Road(roadId, firstHospital, secondHospital, distance);

                                    if (newRoad.getId() < ID_LIMIT && distance < DISTANCE_LIMIT && !roadList.contains(newRoad)) {
                                        roadList.add(newRoad);
                                        firstHospital.addDestination(secondHospital, distance);
                                        secondHospital.addDestination(firstHospital, distance);
                                    } else {
                                        throw new IllegalArgumentException("Droga nr: " + newRoad.getId() + " przekroczyla mozliwe limity danych lub znajduje sie juz w liscie drog!");
                                    }
                                } else {
                                    throw new IllegalArgumentException("Szpitale: " + firstHospitalId + ", " + secondHospitalId + " nie istnieja!");
                                }
                            } catch (NumberFormatException nfe) {
                                throw new NumberFormatException("Niepoprawne dane w pliku wejsciowym: " + filePath + " , linia: " + lineNumber + "!");
                            }
                        } else {
                            throw new IllegalArgumentException("Nieprawidlowa ilosc atrybutow w pliku wejsciowym: " + filePath + ", w linii: " + lineNumber + "!\nPopraw strukture pliku!");
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("Nieprawidłowe dane wejsciowe!\nSprawdz poprawnosc danych w pliku wejsciowym!");
            }
        }
    }

    private static boolean checkBasicParameters(MapObject mapObject) {
        return mapObject.getX() < X_LIMIT && mapObject.getY() < Y_LIMIT && mapObject.getId() < ID_LIMIT;
    }

    private static boolean checkIfLineIsAComment(String line, String comment) {
        if (line.charAt(0) == '#') {
            return true;
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

    private boolean checkFileFormat(String fileName) {
        StringTokenizer stringTokenizer = new StringTokenizer(fileName, ".");
        List<String> tokens = new ArrayList<>();

        while (stringTokenizer.hasMoreTokens()) {
            tokens.add(stringTokenizer.nextToken());
        }

        return tokens.get(tokens.size() - 1).equals(acceptedFormat);
    }

    private void initializeHospitalAndIntersectionList() {
        hospitalAndIntersectionList.addAll(hospitalList);
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

    public List<Hospital> getHospitalAndIntersectionList() {
        return hospitalAndIntersectionList;
    }
}
