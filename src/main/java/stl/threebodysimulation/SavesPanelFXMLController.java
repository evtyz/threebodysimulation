package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.kordamp.ikonli.material.Material;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The FXML Controller for the UI panel that displays saves.
 */
public class SavesPanelFXMLController {

    /**
     * A hashmap that stores the expanded/contracted state of the UI element for each template.
     */
    HashMap<String, Boolean> expandRecord;

    /**
     * Searchbar UI element.
     */
    @FXML
    private TextField searchField;

    /**
     * The VBox UI element for the saves panel with custom templates.
     */
    @FXML
    private VBox savesBox;

    /**
     * The VBox UI element for the saves panel with default templates.
     */
    @FXML
    private VBox defaultSavesBox;
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
     * The listener that is called when the user loads a new template.
     */
    private Listener onLoadListener;

    /**
     * The SimulationSettings about to be loaded in.
     */
    private SimulationSettings settings;

    /**
     * The filename of the template to be loaded.
     */
    private String templateName;

    /**
     * Sets up the controller for first use.
     */
    void setup() {
        expandRecord = new HashMap<>();
        refreshSaves();
        refreshButton.setGraphic(
                SceneFXMLController.buildIcon(Material.REFRESH, Color.valueOf("#555555"), 20)
        );

        browseButton.setGraphic(
                SceneFXMLController.buildIcon(Material.FOLDER, Color.WHITE, 20)
        );

        searchField.textProperty().addListener((observable, oldText, newText) -> refreshSaves(newText));
    }

    /**
     * Refreshes the save display for all saves.
     */
    public void refreshSaves() {
        refreshSaves("");
    }

    /**
     * Refreshes the saves display for saves that match the searchPrompt
     *
     * @param searchPrompt The search term entered by the user. If empty, this will display all files, irrespective of name.
     */
    @SuppressWarnings("ConstantConditions") // Already caught by a try-catch.
    public void refreshSaves(String searchPrompt) {
        try {
            savesBox.getChildren().clear();
        } catch (NullPointerException ignored) {
        }

        try {
            defaultSavesBox.getChildren().clear();
        } catch (NullPointerException ignored) {
        }

        boolean templatesFound = false;

        for (DefaultTemplates template : DefaultTemplates.values()) {
            String title = template.getTitle();

            try {
                if (! (searchPrompt.equals("") || title.toLowerCase().contains(searchPrompt.toLowerCase()))) {
                    continue;
                }

                templatesFound = true;

                DefaultSavePreviewFXMLController saveController = SceneFXMLController.loadLayout(
                        "/stl/threebodysimulation/layouts/defaultSavePreviewLayout.fxml",
                        node -> defaultSavesBox.getChildren().add(node)
                );

                if (expandRecord.containsKey(title)) {
                    saveController.setExpand(expandRecord.get(title));
                } else {
                    saveController.setExpand(false);
                    expandRecord.put(title, false);
                }

                SimulationSettings settings = template.getSettings();

                saveController.setSettings(settings);
                saveController.setTitle(title);
                saveController.setSelectListener(() -> {
                    templateName = title;
                    showLoadConfirmation(saveController.getSettings());
                });
                saveController.setOnExpandListener(() -> expandRecord.put(title, true));
                saveController.setOnContractListener(() -> expandRecord.put(title, false));

            } catch (IOException ignored) {} // Should never happen.
        }

        File saveDirectory = new File(SceneFXMLController.SAVES_DIRECTORY_NAME);
        //noinspection ResultOfMethodCallIgnored : As long as a directory exists, we don't care if mkdir created it.
        saveDirectory.mkdir(); // Create a directory if none exists.
        File[] filesList = saveDirectory.listFiles((dir, name) -> name.endsWith(SceneFXMLController.SAVES_EXTENSION_NAME));
        try {
            for (File saveFile : filesList) {
                String filename = saveFile.getName().substring(0, saveFile.getName().lastIndexOf("."));

                if (! (searchPrompt.equals("") || filename.toLowerCase().contains(searchPrompt.toLowerCase()))) {
                    continue;
                }
                // TODO Documentation
                // Reads a file and saves it as a node.
                ArrayList<String> serializedForm = new ArrayList<>();

                try {
                    for (CSVRecord record : CSVFormat.DEFAULT.parse(new FileReader(saveFile))) {
                        for (int index = 0; index < record.size(); index++) {
                            serializedForm.add(record.get(index));
                        }
                    }
                    templatesFound = true;
                } catch (IOException e) {
                    e.printStackTrace();
                    // Should never happen.
                }


                SimulationSettings settings = new SimulationSettings(serializedForm);

                SavePreviewFXMLController saveController = SceneFXMLController.loadLayout(
                        "/stl/threebodysimulation/layouts/savePreviewLayout.fxml",
                        node -> savesBox.getChildren().add(node)
                );

                if (expandRecord.containsKey(filename)) {
                    saveController.setExpand(expandRecord.get(filename));
                } else {
                    saveController.setExpand(false);
                    expandRecord.put(filename, false);
                }

                saveController.setSettings(settings);
                saveController.setTitle(filename);
                saveController.setSelectListener(() -> {
                    templateName = saveFile.getName().substring(0, saveFile.getName().lastIndexOf("."));
                    showLoadConfirmation(saveController.getSettings());
                });
                saveController.setDeleteListener(() -> showDeleteConfirmation(saveFile));
                saveController.setOnExpandListener(() -> expandRecord.put(filename, true));
                saveController.setOnContractListener(() -> expandRecord.put(filename, false));
            }
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }

        if (!templatesFound) {
            showNoSavesMessage();
        }
    }

    /**
     * Shows a message when no saves are found.
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
        SceneFXMLController.openWarningWindow(
                new FilenameSpecificMessage(FilenameSpecificMessage.Type.DELETE_CONFIRMATION, saveFile.getAbsolutePath()),
                savesBox.getScene().getWindow(),
                () -> {
                    if (!saveFile.delete()) {
                        SceneFXMLController.openErrorWindow(
                                new FilenameSpecificMessage(FilenameSpecificMessage.Type.DELETE_ERROR, saveFile.getAbsolutePath()),
                                savesBox.getScene().getWindow());
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
        this.settings = settings;
        SceneFXMLController.openWarningWindow(
                FilenameUnspecificMessage.LOAD_CONFIRMATION,
                savesBox.getScene().getWindow(),
                onLoadListener);
    }

    /**
     * Opens the saves directory in a file explorer.
     */
    public void openSavesDirectory() {
        DesktopAPI.openDirectory(SceneFXMLController.SAVES_DIRECTORY_NAME);
    }

    /**
     * Sets a listener for when the user loads a file.
     *
     * @param onLoadListener The listener.
     */
    void setOnLoadListener(Listener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    /**
     * Gets the SimulationSettings loaded by the user.
     *
     * @return The SimulationSettings loaded by the user.
     */
    SimulationSettings getSettings() {
        return settings;
    }

    /**
     * Gets the filename of the template loaded by the user.
     *
     * @return The filename.
     */
    String getTemplateName() {
        return templateName;
    }
}
