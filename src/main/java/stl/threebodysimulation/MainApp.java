package stl.threebodysimulation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


// The "start" method of this class is called when we run the app.
public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Set up the scene.fxml files and the CSS files. The inputs and outputs exist to implement the parent class Application.
        Parent root = FXMLLoader.load(getClass().getResource("/scene.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/bootstrap3.css").toExternalForm());
        // We don't want the window to be resizable, to save us the UI headache.
        stage.setResizable(false);

        // Icon of app
        stage.getIcons().add(new Image("/icon.png"));

        // Title of app
        stage.setTitle("Three-Body Simulation");

        // Stage the UI.
        stage.setScene(scene);
        stage.show();
    }

}