import javafx.animation.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
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
    private double scale;
    private int maxPatientId;

    private List<Hospital> hospitals;
    private final QuadTree qtree = new QuadTree();
    private ArrayList<Double> countryBorders;
    private DijkstrasAlgorithm dijkstrasAlgorithm;

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

    private void drawCountry(ArrayList<Double> countryBorders) {
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

    private void drawMap(List<Road> roads, List<Hospital> hospitals, List<Hospital> hospitalsAndIntersections, List<Monument> monuments) {
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

    private Circle drawPatient(double x, double y) {
        Circle patient = new Circle(x / scale + centerX, centerY - y / scale, 2, Color.PURPLE);
        pane.getChildren().add(patient);
        return patient;
    }

    private void calcScale(Area borders) {
        double scale = 1;
        boolean incrScale = false;
        while (Math.abs(borders.getxLeft() - borders.getxRight()) / scale > 464 || Math.abs(borders.getyUp() - borders.getyDown()) / scale > 344) {
            scale++;
            incrScale = true;
        }
        while ((Math.abs(borders.getxLeft() - borders.getxRight()) / scale < 100 || Math.abs(borders.getyUp() - borders.getyDown()) / scale < 100) && !incrScale) {
            scale /= 2;
        }
        this.scale = scale;
    }

    private void makeTransition(double scale, int duration, Patient p, Circle pInMap, Hospital nearestHospital, List<TranslateTransition> transitions) {
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.millis(duration));
        transition.setToX((nearestHospital.getX() - p.getX()) / scale);
        transition.setToY((-nearestHospital.getY() + p.getY()) / scale);
        transition.setNode(pInMap);
        transitions.add(transition);
    }

    public void handleButtonClick() {
        String hospPath = hospitalFile.getText();
        String patPath = patientFile.getText();
        Alert alert = setAlertStyle();

        InputFileReader reader;
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

        if (pane.getChildren() != null) {
            pane.getChildren().clear();
        }

        centerX = pane.getBoundsInLocal().getWidth() / 2;
        centerY = pane.getBoundsInLocal().getHeight() / 2;
        maxPatientId = patients.get(patients.size() - 1).getId();

        countryBorders = ConvexHull.convex_hull(hospitals, monuments);
        Area quadrant = QuadTree.calcQuadrant(countryBorders);
        calcScale(quadrant);
        qtree.fillTree(new ArrayList<>(hospitals), quadrant);
        dijkstrasAlgorithm = new DijkstrasAlgorithm(hospitalsAndIntersections);
        drawCountry(countryBorders);
        drawMap(roads, hospitals, hospitalsAndIntersections, monuments);

        Timeline timeLine = new Timeline();
        timeLine.pause();
        Collection<KeyFrame> frames = timeLine.getKeyFrames();
        int duration = (int) (MAX_MS_VALUE * animSlider.getValue());
        Duration frameTime = Duration.millis(duration);
        Duration frameGap = frameTime;

        for (Patient p : patients) {
            makeAnimation(frames, frameTime, p, duration);
            frameTime = frameTime.add(frameGap);
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

    private void makeAnimation(Collection<KeyFrame> frames, Duration frameTime, Patient p, int duration) {
        Point point = new Point(p.getX() + centerX, -p.getY() + centerY);

        frames.add(new KeyFrame(frameTime, e -> {
            boolean shouldPlay = true;
            Circle pInMap = drawPatient(p.getX(), p.getY());

            if (!ConvexHull.isInBorder(countryBorders, point)) {
                output.appendText("Patient " + p.getId() + " is outside the country.\n");
                FadeTransition fade = new FadeTransition();
                fade.setDuration(Duration.millis(duration));
                fade.setToValue(0);
                fade.setNode(pInMap);
                fade.play();
                return;
            }

            int nearestId = qtree.findNearest(p) - 1;
            Hospital nearestHospital = hospitals.get(nearestId);

            List<TranslateTransition> transitions = new ArrayList<>();
            SequentialTransition transition = new SequentialTransition();
            makeTransition(scale, duration, p, pInMap, nearestHospital, transitions);
            output.appendText("Patient " + p.getId() + " >> Hospital " + (nearestId + 1) + "\n");

            if (nearestHospital.getEmptyBeds() == 0) {
                Hospital nearestAndEmptyHospital = dijkstrasAlgorithm.getNearestEmpty(nearestHospital);

                if (nearestAndEmptyHospital != null) {
                    List<Hospital> path = nearestAndEmptyHospital.getShortestPath();

                    for (Hospital h : path) {
                        if (h.getId() != nearestHospital.getId()) {
                            makeTransition(scale, duration, p, pInMap, h, transitions);
                            output.appendText("Patient " + p.getId() + " >> Hospital " + h.getId() + "\n");
                        }
                    }

                    makeTransition(scale, duration, p, pInMap, nearestAndEmptyHospital, transitions);
                    nearestAndEmptyHospital.bringPatient();
                    output.appendText("Patient " + p.getId() + " >> Hospital " + nearestAndEmptyHospital.getId() + "\n");

                } else {
                    shouldPlay = false;
                    output.appendText("All hospitals are full! Patient " + p.getId() + " couldn't be transported to hospital! :(\n");
                }

                dijkstrasAlgorithm.clear();
            } else {
                nearestHospital.bringPatient();
            }

            if (shouldPlay) {
                transition.getChildren().addAll(transitions);
                transition.play();
            }
        }));
    }

    public void handleMouseClick(MouseEvent mouseEvent) {
        double x = mouseEvent.getX() - centerX;
        double y = -mouseEvent.getY() + centerY;

        Timeline timeLine = new Timeline();
        Collection<KeyFrame> frames = timeLine.getKeyFrames();
        int duration = (int) (MAX_MS_VALUE * animSlider.getValue());
        Duration frameTime = Duration.millis(duration);
        Patient p = new Patient(++maxPatientId, (int) x, (int) y);

        makeAnimation(frames, frameTime, p, duration);
        timeLine.setCycleCount(1);
        timeLine.play();
    }
}