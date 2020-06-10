package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * The FXMLController for the UI element that shows previews of saved templates.
 */
public class SavePreviewFXMLController {

    /**
     * The settings that the preview is displaying.
     */
    private SimulationSettings settings;

    /**
     * The listener that is called when the user selects this preview.
     */
    private Listener clickListener;

    /**
     * The label that shows the filename of the settings.
     */
    @FXML
    private Label saveIDLabel;

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
     * The constructor for the FXMLLoader.
     */
    public SavePreviewFXMLController() {}

    /**
     * Sets the title of the preview.
     *
     * @param title The title.
     */
    void setTitle(String title) {
        saveIDLabel.setText(title);
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

        // Everything has 2 decimal places. TODO: Make sure things fit!
        timeskipLabel.setText(String.format("%.2f", settings.getSkip()));
        speedLabel.setText(String.format("%.2f", settings.getSpeed()));
        Particle[] particles = settings.getParticles();
        massLabel1.setText(String.format("%.2f", particles[0].getMass()));
        massLabel2.setText(String.format("%.2f", particles[1].getMass()));
        massLabel3.setText(String.format("%.2f", particles[2].getMass()));
    }

    /**
     * Sets the click listener for when the user selects the preview.
     *
     * @param listener The listener to set.
     */
    void setClickListener(Listener listener) {
        clickListener = listener;
    }

    /**
     * Retrieves the settings that the preview represents.
     *
     * @return The settings.
     */
    SimulationSettings getSettings() {
        return settings;
    }
}
