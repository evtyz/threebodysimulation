package stl.threebodysimulation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 * This class launches the UI of the app.
 */
public class MainApp extends Application {
    /**
     * @param stage The window that the app will be run in. Supplied by the Application parent class.
     * @throws Exception An exception that occurs if the layout files are not found. Should never occur.
     */
    @Override
    public void start(Stage stage) throws Exception {
        // Set up the scene.fxml files and the CSS files. The inputs and outputs exist to implement the parent class Application.
        FXMLLoader appLoader = new FXMLLoader(getClass().getResource("/scene.fxml"));
        Parent root = appLoader.load();
        Scene scene = new Scene(root);

        // Load in CSS
        scene.getStylesheets().add(getClass().getResource("/bootstrap3.css").toExternalForm());

        // We don't want the window to be resizable, to save us the UI headache.
        stage.setResizable(false);

        // Icon of app
        stage.getIcons().add(new Image("/appIcon.png"));

        // Title of app
        stage.setTitle("Three-Body Simulation");

        // Stage the UI.
        stage.setScene(scene);
        stage.show();
    }

}