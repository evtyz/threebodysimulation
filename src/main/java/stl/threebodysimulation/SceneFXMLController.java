package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * This class is the FXML controller for the entire scene (or UI).
 */
public class SceneFXMLController implements Initializable {

    /**
     * The name of the directory that stores CSVs.
     */
    static final String CSV_DIRECTORY_NAME = "CSV";
    /**
     * The extension of CSV files.
     */
    static final String CSV_EXTENSION_NAME = ".csv";
    /**
     * The name of the directory that stores templates.
     */
    static final String SAVES_DIRECTORY_NAME = "Saves";
    /**
     * The extension of template files.
     */
    static final String SAVES_EXTENSION_NAME = ".3btemplate";
    /**
     * Default colors that each particle is initialized to.
     */
    private static final Color[] defaultColors = {Color.RED, Color.GREEN, Color.BLUE};
    /**
     * The separator between directory names. Depends on operating system. e.g. "/" vs "\\" vs others.
     */
    static String fileSeparator;
    /**
     * A String.format template for CSV files.
     */
    static String CSVFilePathTemplate;
    /**
     * A String.format template for save files.
     */
    static String SavesFilePathTemplate;
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
     * The ScrollPane UI element for the saves panel.
     */
    @FXML
    private ScrollPane savesScrollPane;
    /**
     * The controller for the saves panel UI.
     */
    @FXML
    private SavesPanelFXMLController savesPanelController;
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
     * Constructor used by the FXML loader.
     */
    public SceneFXMLController() {
        fileSeparator = System.getProperty("file.separator");
        CSVFilePathTemplate = CSV_DIRECTORY_NAME + fileSeparator + "%s" + CSV_EXTENSION_NAME;
        SavesFilePathTemplate = SAVES_DIRECTORY_NAME + fileSeparator + "%s" + SAVES_EXTENSION_NAME;
    }

    /**
     * Opens a new popup window with an error message
     *
     * @param message The error message to display.
     * @param parent  The window that the error popup must block.
     */
    static void openErrorWindow(PopupMessage message, Window parent) {
        openPopupWindow(
                "/stl/threebodysimulation/layouts/errorWindowLayout.fxml",
                message,
                parent,
                () -> {
                }
        );
    }

    /**
     * Opens a new popup window with an confirmation warning message
     *
     * @param message         The warning message to display.
     * @param parent          The window that the warning popup must block.
     * @param confirmListener The listener that will be called if the user confirms.
     */
    static void openWarningWindow(PopupMessage message, Window parent, Listener confirmListener) {
        openPopupWindow(
                "/stl/threebodysimulation/layouts/warningWindowLayout.fxml",
                message,
                parent,
                confirmListener
        );
    }

    /**
     * Opens up a new popup window.
     *
     * @param layoutName      The name of the layout file to be loaded.
     * @param message         The message of the popup.
     * @param parent          The parent window of the popup
     * @param confirmListener The listener that runs when the popup is confirmed (can be a new Listener)
     */
    static void openPopupWindow(String layoutName, PopupMessage message, Window parent, Listener confirmListener) {
        try {

            final Stage popupWindow = new Stage();
            // Load the correct message into the layout
            ErrorWindowFXMLController popupController = loadLayout(layoutName, node -> {
                Scene popupScene = new Scene((Parent) node);
                MainApp.setCSS(popupScene);
                popupWindow.setScene(popupScene);
            });


            popupController.setLabel(message.getMessage());
            popupController.setConfirmListener(confirmListener);

            popupWindow.initModality(Modality.APPLICATION_MODAL); // Blocks the parent window.
            popupWindow.initOwner(parent);

            MainApp.openWindow(popupWindow, popupController.getIcon(), message.getTitle());

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
     * Loads a layout.
     *
     * @param resourceName The name of the layout file to be loaded.
     * @param manager      A manager that displays the layout file in the desired way.
     * @param <Controller> The type of controller that manages the layout file.
     * @return The layout controller.
     * @throws IOException If the layout isn't found (should never happen.)
     */
    static <Controller> Controller loadLayout(String resourceName, NodeManager manager) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneFXMLController.class.getResource(resourceName));
        manager.manageNewNode(loader.load());
        return loader.getController();
    }

    /**
     * Builds a custom FontIcon.
     *
     * @param iconTemplate The icon to be built.
     * @param color        The desired color of the icon.
     * @param size         The desired size of the icon.
     * @return The icon.
     */
    static FontIcon buildIcon(Material iconTemplate, Color color, int size) {
        FontIcon icon = new FontIcon(iconTemplate);
        icon.setIconColor(color);
        icon.setIconSize(size);
        return icon;
    }

    /**
     * Sets up a brand-new controller for the settings panel.
     *
     * @return The original form of a SettingsPanelFXMLController.
     * @throws IOException If the layout file isn't found. Should never happen.
     */
    private SettingsPanelFXMLController setupSettingsPanel() throws IOException {
        SettingsPanelFXMLController panelController = loadLayout(
                "/stl/threebodysimulation/layouts/settingsPanelLayout.fxml",
                node -> settingsTab.setContent(node));

        panelController.setup(); // Set up settings panel.
        panelController.setOnRunSimulationListener(() -> runSimulation(settingsPanelController.getSimulationSettings())); // Sets up what happens when simulation is run.
        panelController.setOnRunErrorListener(() -> openErrorWindow(FilenameUnspecificMessage.INPUT_ERROR, sceneLayout.getScene().getWindow())); // Sets up what happens when an error occurs.
        panelController.setOnSaveTemplateListener(() -> {
            savesPanelController.refreshSaves();
            tabPane.getSelectionModel().select(1);
        });
        return panelController;
    }

    /**
     * Sets up a brand-new controller for the info panel.
     *
     * @return The original form of an InfoPanelFXMLController.
     * @throws IOException If the layout file isn't found. Should never happen.
     */
    private InfoPanelFXMLController setupInfoPanel() throws IOException {
        InfoPanelFXMLController panelController = loadLayout(
                "/stl/threebodysimulation/layouts/infoPanelLayout.fxml",
                node -> actionPane.setTop(node)
        );
        panelController.setup();
        return panelController;
    }

    /**
     * Sets up a brand-new controller for the canvas panel.
     *
     * @return The original form of a CanvasPanelFXMLController.
     * @throws IOException If the layout file isn't found. Should never happen.
     */
    private CanvasPanelFXMLController setupCanvasPanel() throws IOException {
        CanvasPanelFXMLController panelController = loadLayout(
                "/stl/threebodysimulation/layouts/canvasPanelLayout.fxml",
                node -> actionPane.setCenter(node)
        );
        panelController.setup(); // Sets up canvas.
        panelController.setOnStopListener(() -> settingsPanelController.setActiveRunButton(true)); // Enables rerunning the simulation if it is stopped.
        return panelController;
    }

    /**
     * Sets up a brand-new controller for the saves panel.
     *
     * @return The original form of a SavesPanelFXMLController.
     * @throws IOException If the layout file isn't found. Should never happen.
     */
    private SavesPanelFXMLController setupSavesPanel() throws IOException {
        SavesPanelFXMLController panelController = loadLayout(
                "/stl/threebodysimulation/layouts/savesPanelLayout.fxml",
                node -> savesScrollPane.setContent(node)
        );

        panelController.setup();
        panelController.setOnLoadListener(() -> {
            settingsPanelController.loadTemplateName(panelController.getTemplateName());
            settingsPanelController.loadSettings(panelController.getSettings());
            tabPane.getSelectionModel().select(0);
        });
        return panelController;
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
            settingsPanelController = setupSettingsPanel();
            infoPanelController = setupInfoPanel();
            canvasPanelController = setupCanvasPanel();
            savesPanelController = setupSavesPanel();

        } catch (IOException ignored) {
            // This only happens when FXML files aren't found, which should never happen.
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
     * Passed in as a functional argument to explain how a node is displayed.
     */
    interface NodeManager {
        /**
         * Called when a node needs to be displayed.
         *
         * @param node The node that needs to be displayed.
         */
        void manageNewNode(Node node);
    }
}