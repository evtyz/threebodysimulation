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

        setCSS(scene);
        stage.setScene(scene);

        openWindow(stage, new Image("/stl/threebodysimulation/icons/appIcon.png"), "Three-Body Simulation");
    }

    /**
     * Sets the CSS of a scene to the app's theme.
     *
     * @param scene The scene to be styled.
     */
    static void setCSS(Scene scene) {
        scene.getStylesheets().add(MainApp.class.getResource("/stl/threebodysimulation/styles/bootstrap3.css").toExternalForm());
    }

    /**
     * Opens a new window.
     *
     * @param stage The layout of the window.
     * @param icon The icon of the window.
     * @param title The title of the window.
     */
    static void openWindow(Stage stage, Image icon, String title) {
        stage.setResizable(false);
        stage.getIcons().add(icon);
        stage.setTitle(title);
        stage.show();
    }

}