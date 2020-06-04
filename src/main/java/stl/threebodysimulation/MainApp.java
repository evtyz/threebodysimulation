package stl.threebodysimulation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 * This class launches the UI of the app.
 */
public class MainApp extends Application {
    /**
     * Starts the application.
     *
     * @param stage The window that the app will be run in. Supplied by the Application parent class.
     * @throws Exception An exception that occurs if the layout files are not found. Should never occur.
     */
    @Override
    public void start(Stage stage) throws Exception {
        // Set up the sceneLayout.fxml files and the CSS files. The inputs and outputs exist to implement the parent class Application.
        FXMLLoader appLoader = new FXMLLoader(getClass().getResource("/stl/threebodysimulation/layouts/sceneLayout.fxml"));
        Scene scene = new Scene(appLoader.load(), 1200, 900);

        // Load in CSS
        scene.getStylesheets().add(getClass().getResource("/stl/threebodysimulation/styles/bootstrap3.css").toExternalForm());

        // We don't want the window to be resizable, to save us the UI headache.
        stage.setResizable(false);

        // Icon of app
        stage.getIcons().add(new Image("/stl/threebodysimulation/icons/appIcon.png"));

        // Title of app
        stage.setTitle("Three-Body Simulation");

        // Stage the UI.
        stage.setScene(scene);
        stage.show();
    }

}