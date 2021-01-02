import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
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
    private BorderPane pane;

    @FXML
    private Polygon country;

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

    //Rysuje wielokąt na podstawie otrzymanych punktów.
    private void drawCountry(ArrayList<Double> countryBorders) {
        Bounds bounds = pane.getCenter().getBoundsInLocal();
        double centerX = bounds.getWidth() / 2;
        double centerY = bounds.getHeight() / 2;

        for (int i = 0; i < countryBorders.size(); i++) {
            double prevValue = countryBorders.get(i);
            double value;
            if (i % 2 == 0) {
                value = prevValue + centerX;
            } else {
                value = prevValue + centerY;
            }
            countryBorders.set(i, value);
        }

        country.getPoints().addAll(countryBorders);
        country.setFill(Color.AQUA);
        country.setVisible(true);
    }

    //Umieszcza na mapie elementy.
    private void drawMap(List<Road> roads, List<Hospital> hospitals, List<Monument> monuments) {
        Bounds bounds = pane.getCenter().getBoundsInLocal();
        double centerX = bounds.getWidth() / 2;
        double centerY = bounds.getHeight() / 2;
        Circle center = new Circle(centerX, centerY, 2, Color.BLACK);
        pane.getChildren().add(center);

        for (Road road : roads) {
            Hospital hospital1 = hospitals.get(road.getFirstHospitalId() - 1);
            Hospital hospital2 = hospitals.get(road.getSecondHospitalId() - 1);
            int x1 = hospital1.getX();
            int y1 = hospital1.getY();
            int x2 = hospital2.getX();
            int y2 = hospital2.getY();

            Line line = new Line(centerX + x1, centerY + y1, centerX + x2, centerY + y2);
            line.setStroke(Color.BLACK);
            pane.getChildren().add(line);
        }

        for (Hospital h : hospitals) {
            int x = h.getX();
            int y = h.getY();

            Circle hospital = new Circle(x + centerX, y + centerY, 4, Color.RED);
            pane.getChildren().add(hospital);
        }

        for (Monument m : monuments) {
            int x = m.getX();
            int y = m.getY();

            Circle monument = new Circle(x + centerX, y + centerY, 3, Color.YELLOWGREEN);
            pane.getChildren().add(monument);
        }
    }

    public void handleButtonClick() {
        String hospPath = hospitalFile.getText();
        String patPath = patientFile.getText();
        double sliderValue = animSlider.getValue(); //wartość opóźnienia w [s]

        Alert alert = setAlertStyle();
        InputFileReader fileReader = null;

        try {
            fileReader = new InputFileReader(hospPath, false);
        } catch (IllegalArgumentException e) {
            alert.setTitle("Błędny format pliku");
            alert.setHeaderText("Błędny format pliku!");
            alert.setContentText("Bledny format pliku wejsciowego!\nPlik wejsciowy powinien byc zapisany w formacie \".txt\"");
            alert.showAndWait();
        } catch (FileNotFoundException e1) {
            alert.setTitle("Błędna nazwa pliku");
            alert.setHeaderText("Błędna nazwa pliku!");
            alert.setContentText("Nie odnaleziono pliku: " + hospPath + "!\nPodaj prawidłową nazwę!");
            alert.showAndWait();
        }

        List<Hospital> hospitals = Objects.requireNonNull(fileReader).getHospitalList();
        List<Monument> monuments = Objects.requireNonNull(fileReader).getMonumentList();
        List<Road> roads = Objects.requireNonNull(fileReader).getRoadList();

        try {
            fileReader = new InputFileReader(patPath, true);
        } catch (IllegalArgumentException e) {
            alert.setTitle("Błędny format pliku");
            alert.setHeaderText("Błędny format pliku!");
            alert.setContentText("Bledny format pliku wejsciowego!\nPlik wejsciowy powinien byc zapisany w formacie \".txt\"");
            alert.showAndWait();
        } catch (FileNotFoundException e1) {
            alert.setTitle("Błędna nazwa pliku");
            alert.setHeaderText("Błędna nazwa pliku!");
            alert.setContentText("Nie odnaleziono pliku: " + patPath + "!\nPodaj prawidłową nazwę!");
            alert.showAndWait();
        }

        List<Patient> patients = Objects.requireNonNull(fileReader).getPatientList();

        output.appendText("Tu będą umieszczane komunikaty, gdy zostaną zaimplementowane algorytmy." +
                " Narazie podam dane szpitali z podanego pliku :) \n\n");
        for (Hospital h : hospitals) {
            output.appendText(h.toString() + "\n");
        }

        /*Tworzenie wielokąta.
        Tablica współrzędnych typu Double, otrzymana z convex hull algorithm, opisująca granice kraju*/
        ArrayList<Double> countryBorders = new ArrayList<Double>(
                Arrays.asList(200.0, 50.0,
                        400.0, 50.0,
                        450.0, 150.0,
                        400.0, 250.0,
                        200.0, 250.0,
                        150.0, 150.0
                ));
        drawCountry(countryBorders);
        drawMap(roads, hospitals, monuments);

        //TODO obsłużyć przypadek, gdy nie podano żadnego pliku (aktualnie wyrzuca błąd IndexOutOfBounds).
    }
}