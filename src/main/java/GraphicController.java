import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GraphicController extends Application {

    @FXML
    private Pane pane;

    @FXML
    private TextField hospitalFile;

    @FXML
    private TextField patientFile;

    @FXML
    private Slider animSlider;

    @FXML
    private TextArea output;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("graphicController.fxml")));
        Scene scene = new Scene(root, 1280, 720);
        //stage.getIcons().add(new Image("file::icon.png")); //nie dziala z jakiegos powodu

        //Tak już działa, ale obrazek musi być w folderze resources
        stage.getIcons().add(new Image(GraphicController.class.getResourceAsStream("/icon.png")));
        stage.setResizable(false);
        stage.setTitle("Corona-taxi");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private Alert setAlertStyle() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setGraphic(null);
        dialogPane.getStylesheets().add(getClass().getResource("alert.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.getIcons().add(new Image("icon.png"));

        return alert;
    }

    double centerX;
    double centerY;

    //Rysuje wielokąt na podstawie otrzymanych punktów.
    private void drawCountry(ArrayList<Double> countryBorders) {
        for (int i = 0; i < countryBorders.size(); i++) {
            double prevValue = countryBorders.get(i);
            double value;
            if (i % 2 == 0) {
                value = prevValue + centerX;
            } else {
                value = -prevValue + centerY;
            }
            countryBorders.set(i, value);
        }

        Polygon country = new Polygon();
        country.getPoints().addAll(countryBorders);
        country.setFill(Color.AQUA);
        pane.getChildren().add(country);
    }

    //Umieszcza na mapie elementy.
    private void drawMap(List<Road> roads, List<Hospital> hospitals, List<Monument> monuments) {
        Circle center = new Circle(centerX, centerY, 2, Color.BLACK);
        pane.getChildren().add(center);

        for (Road road : roads) {
            Hospital hospital1 = hospitals.get(road.getFirstHospitalId() - 1);
            Hospital hospital2 = hospitals.get(road.getSecondHospitalId() - 1);
            int x1 = hospital1.getX();
            int y1 = hospital1.getY();
            int x2 = hospital2.getX();
            int y2 = hospital2.getY();

            Line line = new Line(centerX + x1, centerY - y1, centerX + x2, centerY - y2);
            line.setStroke(Color.BLACK);
            pane.getChildren().add(line);
        }

        for (Hospital h : hospitals) {
            int x = h.getX();
            int y = h.getY();

            Circle hospital = new Circle(x + centerX, centerY - y, 4, Color.RED);
            pane.getChildren().add(hospital);
        }

        for (Monument m : monuments) {
            int x = m.getX();
            int y = m.getY();

            Circle monument = new Circle(x + centerX, centerY - y, 3, Color.GRAY);
            pane.getChildren().add(monument);
        }
    }

    private Circle drawPatient(double x, double y) {
        Circle patient = new Circle(x + centerX, centerY - y, 2, Color.YELLOW);
        pane.getChildren().add(patient);
        return patient;
    }

    public void handleButtonClick() {
        String hospPath = hospitalFile.getText();
        String patPath = patientFile.getText();
        double sliderValue = animSlider.getValue();
        Alert alert = setAlertStyle();

        InputFileReader reader;
        List<Hospital> hospitals;
        List<Monument> monuments;
        List<Road> roads;
        List<Patient> patients;

        try {
            reader = new InputFileReader(hospPath, false);
            hospitals = Objects.requireNonNull(reader).getHospitalList();
            monuments = Objects.requireNonNull(reader).getMonumentList();
            roads = Objects.requireNonNull(reader).getRoadList();

            reader = new InputFileReader(patPath, true);
            patients = Objects.requireNonNull(reader).getPatientList();
        } catch (FileNotFoundException e0) {
            alert.setTitle("Błędna nazwa pliku");
            alert.setHeaderText("Błędna nazwa pliku!");
            alert.setContentText(e0.getMessage());
            alert.showAndWait();
            return;
        } catch (NumberFormatException e1) {
            alert.setTitle("Niepoprawne dane");
            alert.setHeaderText("Niepoprawne dane!");
            alert.setContentText(e1.getMessage());
            alert.showAndWait();
            return;
        } catch (IllegalArgumentException e2) {
            alert.setTitle("Błędny format pliku");
            alert.setHeaderText("Błędny format pliku!");
            alert.setContentText(e2.getMessage());
            alert.showAndWait();
            return;
        }

        //Czyszczenie poprzedniej mapy
        if (pane.getChildren() != null) {
            pane.getChildren().clear();
        }

        centerX = pane.getBoundsInLocal().getWidth() / 2;
        centerY = pane.getBoundsInLocal().getHeight() / 2;

        /*Tworzenie wielokąta.
        Tablica współrzędnych typu Double, otrzymana z convex hull algorithm, opisująca granice kraju*/
        ArrayList<Double> countryBorders = new ArrayList<>(
                Arrays.asList(10.0, 10.0,
                        -1.0, 50.0,
                        10.0, 140.0,
                        120.0, 130.0,
                        140.0, 10.0
                ));

        drawCountry(countryBorders);
        drawMap(roads, hospitals, monuments);

        for (Patient p : patients) {
            Circle pInMap = drawPatient(p.getX(), p.getY());

            //TODO Zaimplementować w jakiś sposób przerwę (Thread.sleep() najpierw robi przerwę
            // dla wszystkich wywołań, a potem dopiero następuje rysuowanie wszystkego razem)

            //pane.getChildren().remove(pInMap);
        }

        for (Hospital h : hospitals) {
            output.appendText(h.toString() + "\n");
        }
    }
}