import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class GraphicController extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("graphicController.fxml")));
        Scene scene = new Scene(root, 1280, 720);

        stage.getIcons().add(new Image("file::icon.png")); //nie dziala z jakiegos powodu
        stage.setResizable(false);
        stage.setTitle("Corona-taxi");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
