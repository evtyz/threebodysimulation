package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


/**
 * This class is the FXML controller for the entire scene (or UI).
 */
public class SceneFXMLController implements Initializable {

    /**
     * Default colors that each particle is initialized to.
     */
    private static final Color[] defaultColors = {Color.RED, Color.BLUE, Color.GREEN};
    /**
     * The separator between directory names. Depends on operating system. e.g. "/" vs "\\" vs others.
     */
    static String fileSeparator;
    /**
     * The info display panel in the UI.
     */
    private InfoPanelFXMLController infoPanelController;
    /**
     * The settings panel in the UI.
     */
    private SettingsPanelFXMLController settingsPanelController;
    /**
     * The TabPane UI element that holds the tab view on the left.
     */
    @FXML
    private TabPane tabPane;
    /**
     * The Tab UI element for the settings panel.
     */
    @FXML
    private Tab settingsTab;
    /**
     * The VBox UI element for the saves panel.
     */
    @FXML
    private VBox savesBox;

    /**
     * The Button UI element that handles refreshing for new saves.
     */
    @FXML
    private Button refreshButton;

    /**
     * The Button UI element that handles opening the saves directory in a file explorer.
     */
    @FXML
    private Button browseButton;
    /**
     * The UI element that contains everything outside of the tabs to the left.
     */
    @FXML
    private BorderPane actionPane;
    /**
     * The visualization canvas and controls in the UI.
     */
    private CanvasPanelFXMLController canvasPanelController;
    /**
     * The entire UI scene, as a single element.
     */
    @FXML
    private BorderPane sceneLayout;

    /**
     * The filename of the template to be loaded.
     */
    private String templateName;

    /**
     * Constructor used by the FXML loader.
     */
    public SceneFXMLController() {
        fileSeparator = System.getProperty("file.separator");
    }

    /**
     * Opens a new popup window with an error message
     *
     * @param message The error message to display.
     * @param parent  The window that the error popup must block.
     */
    static void openErrorWindow(ErrorMessage message, Window parent) {
        openPopupWindow(
                new FXMLLoader(SceneFXMLController.class.getResource("/stl/threebodysimulation/layouts/errorWindowLayout.fxml")),
                message,
                parent,
                () -> { }
        );
    }

    /**
     * Opens a new popup window with an confirmation warning message
     *
     * @param message         The warning message to display.
     * @param parent          The window that the warning popup must block.
     * @param confirmListener The listener that will be called if the user confirms.
     */
    static void openWarningWindow(WarningMessage message, Window parent, Listener confirmListener) {
        openPopupWindow(
                new FXMLLoader(SceneFXMLController.class.getResource("/stl/threebodysimulation/layouts/warningWindowLayout.fxml")),
                message,
                parent,
                confirmListener
        );
    }

    /**
     * Opens up a new popup window.
     *
     * @param windowLoader The loader that manages what type of popup the window will contain.
     * @param message The message of the popup.
     * @param parent The parent window of the popup
     * @param confirmListener The listener that runs when the popup is confirmed (can be a new Listener)
     */
    static void openPopupWindow(FXMLLoader windowLoader, PopupMessage message, Window parent, Listener confirmListener) {
        try {
            // Makes an FXML Loader and loads the fxml files
            Scene popupScene = new Scene(windowLoader.load());

            // Style the scenes
            popupScene.getStylesheets().add(SceneFXMLController.class.getResource("/stl/threebodysimulation/styles/bootstrap3.css").toExternalForm());

            // Load the correct message into the layout
            PopupWindowFXMLController popupController = windowLoader.getController();
            popupController.setLabel(message.getMessage());
            popupController.setConfirmListener(confirmListener);


            // Make a popup sceneLayout that blocks the main screen, and set icons and titles.
            final Stage popupWindow = new Stage();
            popupWindow.initModality(Modality.APPLICATION_MODAL); // Blocks the parent window.
            popupWindow.initOwner(parent);
            popupWindow.setResizable(false); // Not resizable
            popupWindow.getIcons().add(popupController.getIcon()); // Error icon.
            popupWindow.setTitle(message.getTitle());
            popupWindow.setScene(popupScene);
            popupWindow.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the default colors of particles.
     *
     * @return Default colors of all three particles.
     */
    static Color[] getDefaultColors() {
        return defaultColors;
    }


    /**
     * Sets up initial states of UI elements after the FXML loader is done linking. Called by the FXML loader.
     *
     * @param url            Unused. From parent class.
     * @param resourceBundle Unused. From parent class.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Load in settings panel
            FXMLLoader settingsPanelLoader = new FXMLLoader(getClass().getResource("/stl/threebodysimulation/layouts/settingsPanelLayout.fxml"));
            settingsTab.setContent(settingsPanelLoader.load());
            settingsPanelController = settingsPanelLoader.getController();
            settingsPanelController.setup(); // Set up settings panel.
            settingsPanelController.setOnOpenManualListener(this::openManual); // Sets up user manual button.
            settingsPanelController.setOnRunSimulationListener(() -> runSimulation(settingsPanelController.getSimulationSettings())); // Sets up what happens when simulation is run.
            settingsPanelController.setOnRunErrorListener(() -> openErrorWindow(ErrorMessage.INPUT_ERROR, sceneLayout.getScene().getWindow())); // Sets up what happens when an error occurs.
            settingsPanelController.setOnSaveTemplateListener(() -> {
                refreshSaves();
                tabPane.getSelectionModel().select(1);
            });

            // Load in info panel.
            FXMLLoader infoPanelLoader = new FXMLLoader(getClass().getResource("/stl/threebodysimulation/layouts/infoPanelLayout.fxml"));
            actionPane.setTop(infoPanelLoader.load());
            infoPanelController = infoPanelLoader.getController();
            infoPanelController.setup();

            // Load in canvas panel.
            FXMLLoader canvasPanelLoader = new FXMLLoader(getClass().getResource("/stl/threebodysimulation/layouts/canvasPanelLayout.fxml"));
            actionPane.setCenter(canvasPanelLoader.load());
            canvasPanelController = canvasPanelLoader.getController();
            canvasPanelController.setup(); // Sets up canvas.
            canvasPanelController.setOnStopListener(() -> settingsPanelController.setActiveRunButton(true)); // Enables rerunning the simulation if it is stopped.

            refreshSaves();

            refreshButton.setGraphic(
                    buildIcon(Material.REFRESH, Color.valueOf("#555555"), 20)
            );

            browseButton.setGraphic(
                    buildIcon(Material.FOLDER, Color.WHITE, 20)
            );

        } catch (IOException ignored) {
            // This only happens when FXML files aren't found, which should never happen.
        }
    }

    /**
     * Builds a custom FontIcon.
     *
     * @param iconTemplate The icon to be built.
     * @param color The desired color of the icon.
     * @param size The desired size of the icon.
     * @return The icon.
     */
    static FontIcon buildIcon(Material iconTemplate, Color color, int size) {
        FontIcon icon = new FontIcon(iconTemplate);
        icon.setIconColor(color);
        icon.setIconSize(size);
        return icon;
    }

    /**
     * Refreshes the saves display for new saves.
     */
    @SuppressWarnings("ConstantConditions") // Already caught by a try-catch.
    public void refreshSaves() {
        try {
            savesBox.getChildren().clear();
        } catch (NullPointerException ignored) {
        }
        File saveDirectory = new File("Saves");
        saveDirectory.mkdir(); // Create a directory if none exists.
        File[] filesList = saveDirectory.listFiles((dir, name) -> name.endsWith(".tbsettings"));
        try {
            if (filesList.length == 0) {
                showNoSavesMessage();
            } else {
                for (File saveFile : filesList) {
                    // TODO Documentation
                    // Reads a file and saves it as a node.
                    ArrayList<String> serializedForm = new ArrayList<>();

                    try {
                        for (CSVRecord record : CSVFormat.DEFAULT.parse(new FileReader(saveFile))) {
                            for (int index = 0; index < record.size(); index++) {
                                serializedForm.add(record.get(index));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        // Should never happen.
                    }


                    SimulationSettings settings = new SimulationSettings(serializedForm);
                    FXMLLoader saveLoader = new FXMLLoader(getClass().getResource("/stl/threebodysimulation/layouts/savePreviewLayout.fxml"));
                    savesBox.getChildren().add(saveLoader.load());
                    SavePreviewFXMLController saveController = saveLoader.getController();
                    saveController.setSettings(settings);
                    saveController.setTitle(saveFile.getName().substring(0, saveFile.getName().lastIndexOf(".")));
                    saveController.setSelectListener(() -> {
                        templateName = saveFile.getName().substring(0, saveFile.getName().lastIndexOf("."));
                        showLoadConfirmation(saveController.getSettings());
                    });
                    saveController.setDeleteListener(() -> showDeleteConfirmation(saveFile));

                }
            }
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
            showNoSavesMessage();
        }
    }

    /**
     * Shows a messsage when no saves are found.
     */
    private void showNoSavesMessage() {
        Label emptyMessage = new Label();
        emptyMessage.setText("No saves found.");
        emptyMessage.setPadding(new Insets(15, 15, 5, 15));
        savesBox.getChildren().add(0, emptyMessage);
    }

    /**
     * Shows a delete confirmation for a template file/
     *
     * @param saveFile The template file to be deleted.
     */
    private void showDeleteConfirmation(File saveFile) {
        openWarningWindow(
                new WarningMessage(WarningMessage.Type.DELETE_CONFIRMATION, saveFile.getAbsolutePath()),
                sceneLayout.getScene().getWindow(),
                () -> {
                    if (!saveFile.delete()) {
                        openErrorWindow(ErrorMessage.DELETE_ERROR, sceneLayout.getScene().getWindow());
                    }
                    refreshSaves();
                }
        );
    }

    /**
     * Shows a preview of loaded settings.
     *
     * @param settings Settings that are about to be loaded.
     */
    private void showLoadConfirmation(SimulationSettings settings) {
        openWarningWindow(
                new WarningMessage(WarningMessage.Type.LOAD_CONFIRMATION),
                sceneLayout.getScene().getWindow(),
                () -> {
                    settingsPanelController.loadTemplateName(templateName);
                    settingsPanelController.loadSettings(settings);
                    tabPane.getSelectionModel().select(0);
                });
    }


    /**
     * Opens the user manual.
     */
    private void openManual() {
        // Opens user manual popups
        try {
            // Load up a new window with user manual layout.
            final Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/stl/threebodysimulation/layouts/userManualLayout.fxml"));
            Scene scene = new Scene(root);
            // Enable css
            scene.getStylesheets().add(getClass().getResource("/stl/threebodysimulation/styles/bootstrap3.css").toExternalForm());
            // We don't want the sceneLayout to be resizable, to save us the UI headache.
            stage.setResizable(false);

            // Icon of app
            stage.getIcons().add(new Image("/stl/threebodysimulation/icons/appIcon.png"));

            // Title of app
            stage.setTitle("User Manual");

            // Stage the UI.
            stage.setScene(scene);
            stage.show();
        } catch (Exception ignored) { // In case the layout is not found. Should never happen.
        }
    }

    /**
     * Runs the simulation.
     *
     * @param settings Settings to run the simulation with.
     */
    private void runSimulation(SimulationSettings settings) {
        // Runs the simulation according to the given settings
        // Sets up info display.
        Particle[] particles = settings.getParticles();
        infoPanelController.setParticles(particles);
        InfoPanelFXMLController.setNumberFormat(settings.getNumberFormat());

        // Sets up visualization.
        canvasPanelController.setParticles(particles);
        canvasPanelController.runSimulation(settings);
    }

    /**
     * Opens the saves directory in a file explorer.
     */
    public void openSavesDirectory() {
        DesktopAPI.openDirectory("Saves");
    }
}