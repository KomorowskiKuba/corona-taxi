import javafx.animation.*;
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
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class GraphicController extends Application {
    private final int MAX_MS_VALUE = 1000;
    private double centerX;
    private double centerY;

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

    @FXML
    private Button generateButton;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("graphicController.fxml")));
        Scene scene = new Scene(root, 1280, 720);

        stage.getIcons().add(new Image(GraphicController.class.getResourceAsStream("/images/icon.png")));
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
        stage.getIcons().add(new Image("images/icon.png"));

        return alert;
    }

    //Rysuje wielokąt na podstawie otrzymanych punktów.
    private void drawCountry(ArrayList<Double> countryBorders, double scale) {
        for (int i = 0; i < countryBorders.size(); i++) {
            double prevValue = countryBorders.get(i) / scale;
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
        country.setFill(Color.LIGHTGRAY);
        pane.getChildren().add(country);
    }

    //Umieszcza na mapie elementy.
    private void drawMap(List<Road> roads, List<Hospital> hospitals, List<Hospital> hospitalsAndIntersections, List<Monument> monuments, double scale) {
        Circle center = new Circle(centerX, centerY, 2, Color.BLACK);
        pane.getChildren().add(center);

        for (Road road : roads) {
            Hospital hospital1 = hospitalsAndIntersections.get(road.getFirstHospital().getId() - 1);
            Hospital hospital2 = hospitalsAndIntersections.get(road.getSecondHospital().getId() - 1);
            double x1 = hospital1.getX() / scale;
            double y1 = hospital1.getY() / scale;
            double x2 = hospital2.getX() / scale;
            double y2 = hospital2.getY() / scale;

            Line line = new Line(centerX + x1, centerY - y1, centerX + x2, centerY - y2);
            line.setStroke(Color.BLACK);
            pane.getChildren().add(line);
        }

        for (Hospital h : hospitals) {
            double x = h.getX() / scale;
            double y = h.getY() / scale;

            Circle hospital = new Circle(x + centerX, centerY - y, 4, Color.RED);
            pane.getChildren().add(hospital);
        }

        for (Monument m : monuments) {
            double x = m.getX() / scale;
            double y = m.getY() / scale;

            Circle monument = new Circle(x + centerX, centerY - y, 3, Color.GRAY);
            pane.getChildren().add(monument);
        }
    }

    private Circle drawPatient(double x, double y, double scale) {
        Circle patient = new Circle(x / scale + centerX, centerY - y / scale, 2, Color.PURPLE);
        pane.getChildren().add(patient);
        return patient;
    }

    private double calcScale(Area borders) {
        double scale = 1;
        while (Math.abs(borders.getxLeft() - borders.getxRight()) / scale > 464 || Math.abs(borders.getyUp() - borders.getyDown()) / scale > 344) {
            scale++;
        }
        while (Math.abs(borders.getxLeft() - borders.getxRight()) / scale < 100 || Math.abs(borders.getyUp() - borders.getyDown()) / scale < 100) {
            scale /= 2;
        }
        return scale;
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
        List<Hospital> hospitalsAndIntersections;
        try {
            reader = new InputFileReader(hospPath, false);
            hospitals = Objects.requireNonNull(reader).getHospitalList();
            monuments = Objects.requireNonNull(reader).getMonumentList();
            roads = Objects.requireNonNull(reader).getRoadList();
            hospitalsAndIntersections = Objects.requireNonNull(reader).getHospitalAndIntersectionList();
            reader = new InputFileReader(patPath, true);
            patients = Objects.requireNonNull(reader).getPatientList();
        } catch (FileNotFoundException e0) {
            alert.setTitle("Błędna nazwa pliku.");
            alert.setHeaderText("Błędna nazwa pliku!");
            alert.setContentText(e0.getMessage());
            alert.showAndWait();
            return;
        } catch (NumberFormatException e1) {
            alert.setTitle("Niepoprawne dane.");
            alert.setHeaderText("Niepoprawne dane!");
            alert.setContentText(e1.getMessage());
            alert.showAndWait();
            return;
        } catch (IllegalArgumentException e2) {
            alert.setTitle("Błędny format pliku.");
            alert.setHeaderText("Błędny format pliku!");
            alert.setContentText(e2.getMessage());
            alert.showAndWait();
            return;
        } catch (NullPointerException e3) {
            alert.setTitle("Brak danych w pliku.");
            alert.setHeaderText("Brak danych w pliku!");
            alert.setContentText(e3.getMessage());
            alert.showAndWait();
            return;
        }

        //Czyszczenie poprzedniej mapy
        if (pane.getChildren() != null) {
            pane.getChildren().clear();
        }

        centerX = pane.getBoundsInLocal().getWidth() / 2;
        centerY = pane.getBoundsInLocal().getHeight() / 2;

        ArrayList<Double> countryBorders = ConvexHull.convex_hull(hospitals, monuments);

        QuadTree qtree = new QuadTree();
        Area quadrant = qtree.calcQuadrant(countryBorders);
        double scale = calcScale(quadrant);

        drawCountry(countryBorders, scale);
        drawMap(roads, hospitals, hospitalsAndIntersections, monuments, scale);

        int duration = (int) (MAX_MS_VALUE * sliderValue);
        Timeline timeLine = new Timeline();
        timeLine.pause();
        Collection<KeyFrame> frames = timeLine.getKeyFrames();
        Duration frameGap = Duration.millis(duration);
        Duration frameTime = Duration.millis(duration);

        qtree.fillTree(new ArrayList<>(hospitals), quadrant);
        DijkstrasAlgorithm dijkstrasAlgorithm = new DijkstrasAlgorithm(hospitalsAndIntersections);

        for (Patient p : patients) {
            Point point = new Point(p.getX() + centerX, -p.getY() + centerY);

            frameTime = frameTime.add(frameGap);
            frames.add(new KeyFrame(frameTime, e -> {
                boolean shouldPlay = true;
                Circle pInMap = drawPatient(p.getX(), p.getY(), scale);

                if (!ConvexHull.isInBorder(countryBorders, point)) {
                    output.appendText("Patient " + p.getId() + " is outside the country.\n");
                    FadeTransition fade = new FadeTransition();
                    fade.setDuration(Duration.millis(duration));
                    fade.setToValue(0);
                    fade.setNode(pInMap);
                    fade.play();
                    return;
                }

                int nearestId = qtree.findNearest(p);
                Hospital nearestHospital = hospitals.get(nearestId);

                TranslateTransition transitionToNearest = new TranslateTransition();
                List<TranslateTransition> transitions = new ArrayList<>();
                SequentialTransition transition = new SequentialTransition();

                transitionToNearest.setDuration(Duration.millis(duration));
                transitionToNearest.setToX((double) (nearestHospital.getX() - p.getX()) / scale);
                transitionToNearest.setToY((double) (-nearestHospital.getY() + p.getY()) / scale);
                transitionToNearest.setNode(pInMap);
                transitions.add(transitionToNearest);

                if (nearestHospital.getEmptyBeds() == 0) {
                    Hospital nearestAndEmptyHospital = dijkstrasAlgorithm.getNearestEmpty(nearestHospital);

                    if (nearestAndEmptyHospital != null) {
                        List<Hospital> path = nearestAndEmptyHospital.getShortestPath();
                        TranslateTransition transitionToNext = new TranslateTransition();

                        for (Hospital h : path) {
                            if (h.getId() != nearestHospital.getId()) { //TODO: CHANGE FOR EQUALS MAYBE
                                transitionToNext.setDuration(Duration.millis(duration));
                                transitionToNext.setToX((double) (h.getX() - p.getX()) / scale);
                                transitionToNext.setToY((double) (-h.getY() + p.getY()) / scale);
                                transitionToNext.setNode(pInMap);
                                transitions.add(transitionToNext);

                                output.appendText("Patient " + p.getId() + " >> Hospital " + h.getId() + "\n");
                            }
                        }

                        TranslateTransition transitionToFinal = new TranslateTransition();

                        transitionToFinal.setDuration(Duration.millis(duration));
                        transitionToFinal.setToX((double) (nearestAndEmptyHospital.getX() - p.getX()) / scale);
                        transitionToFinal.setToY((double) (-nearestAndEmptyHospital.getY() + p.getY()) / scale);
                        transitionToFinal.setNode(pInMap);
                        transitions.add(transitionToFinal);

                        nearestAndEmptyHospital.bringPatient();

                        output.appendText("Patient " + p.getId() + " >> Hospital " + nearestAndEmptyHospital.getId() + "\n");


                    } else {
                        shouldPlay = false;
                        output.appendText("All hospitals are full! Patient " + p.getId() + " couldn't be transported to hospital! :(");
                    }
                } else {
                    output.appendText("Patient " + p.getId() + " >> Hospital " + (nearestId + 1) + "\n");
                    nearestHospital.bringPatient();
                }

                if (shouldPlay) {
                    transition.getChildren().addAll(transitions);
                    transition.play();
                }
            }));
        }

        timeLine.setCycleCount(1);
        timeLine.play();

        generateButton.setOnAction(e -> {
            timeLine.stop();
            try {
                handleButtonClick();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }
}