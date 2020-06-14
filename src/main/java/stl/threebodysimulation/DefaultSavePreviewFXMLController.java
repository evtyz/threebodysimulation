package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.material.Material;

/**
 * A UI element that shows an undeletable default template.
 */
public class DefaultSavePreviewFXMLController {
    /**
     * The settings that the preview is displaying.
     */
    private SimulationSettings settings;

    /**
     * The listener that is called when the user selects this preview.
     */
    private Listener selectListener;

    /**
     * The listener that is called when the title pane is expanded.
     */
    private Listener onExpandListener;

    /**
     * The listener that is called when the title pane is contracted.
     */
    private Listener onContractListener;


    /**
     * The label that shows the filename of the settings.
     */
    @FXML
    private TitledPane fileTitle;

    /**
     * The label that shows whether infinite simulation is enabled in the settings.
     */
    @FXML
    private Label infiniteLabel;

    /**
     * The label that shows the amount of timeskip from the settings.
     */
    @FXML
    private Label timeskipLabel;

    /**
     * The label that shows simulation speed from the settings.
     */
    @FXML
    private Label speedLabel;

    /**
     * The label that shows the mass of particle 1 from the settings.
     */
    @FXML
    private Label massLabel1;

    /**
     * The label that shows the mass of particle 2 from the settings.
     */
    @FXML
    private Label massLabel2;

    /**
     * The label that shows the mass of particle 3 from the settings.
     */
    @FXML
    private Label massLabel3;

    /**
     * The select button.
     */
    @FXML
    private Button selectButton;

    /**
     * For FXML loader.
     */
    public DefaultSavePreviewFXMLController() {

    }


    /**
     * Sets the title of the preview.
     *
     * @param title The title.
     */
    void setTitle(String title) {
        fileTitle.setText(title);
    }

    /**
     * Sets the click listener for when the user selects the preview.
     *
     * @param selectListener The listener to set.
     */
    void setSelectListener(Listener selectListener) {
        this.selectListener = selectListener;
    }


    /**
     * Sets the settings previewed.
     *
     * @param settings The settings.
     */
    void setSettings(SimulationSettings settings) {
        this.settings = settings;
        if (settings.getInfinite()) {
            infiniteLabel.setText("Yes");
        } else {
            infiniteLabel.setText("No");
        }

        selectButton.setGraphic(
                SceneFXMLController.buildIcon(Material.CHECK, Color.valueOf("#555555"), 20)
        );

        fileTitle.expandedProperty().addListener((obs, wasExpanded, isNowExpanded) -> {
            if (isNowExpanded) {
                onExpandListener.onEvent();
            } else {
                onContractListener.onEvent();
            }
        });

        // Everything has 2 decimal places. TODO: Make sure things fit!
        timeskipLabel.setText(String.format("%.2f", settings.getSkip()));
        speedLabel.setText(String.format("%.2f", settings.getSpeed()));
        Particle[] particles = settings.getParticles();
        massLabel1.setText(String.format("%.2f", particles[0].getMass()));
        massLabel2.setText(String.format("%.2f", particles[1].getMass()));
        massLabel3.setText(String.format("%.2f", particles[2].getMass()));
    }


    /**
     * Retrieves the settings that the preview represents.
     *
     * @return The settings.
     */
    SimulationSettings getSettings() {
        return settings;
    }


    /**
     * Sets the expanded/contracted state of the titled pane.
     *
     * @param expand True if expanded, false if contracted.
     */
    void setExpand(boolean expand) {
        fileTitle.setExpanded(expand);
    }

    /**
     * Called when the user clicks the select button.
     */
    public void select() {
        selectListener.onEvent();
    }


    /**
     * Sets the listener that is called when the titledpane expands.
     *
     * @param onExpandListener The listener.
     */
    void setOnExpandListener(Listener onExpandListener) {
        this.onExpandListener = onExpandListener;
    }

    /**
     * Sets the listener that is called when the titledpane contracts.
     *
     * @param onContractListener The listener.
     */
    void setOnContractListener(Listener onContractListener) {
        this.onContractListener = onContractListener;
    }
}
