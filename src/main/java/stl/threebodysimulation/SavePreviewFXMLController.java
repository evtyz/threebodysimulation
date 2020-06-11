package stl.threebodysimulation;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.paint.Color;


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
    private Listener selectListener;

    /**
     * The listener that is called when the user deletes this preview.
     */
    private Listener deleteListener;

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
     * The delete button.
     */
    @FXML
    private FontAwesomeIconView deleteButton;

    /**
     * The select button.
     */
    @FXML
    private FontAwesomeIconView selectButton;

    /**
     * The constructor for the FXMLLoader.
     */
    public SavePreviewFXMLController() {
    }

    /**
     * Handles changing the opacity of a button depending on if it is hovered or not.
     *
     * @param button The button whose opacity is being changed.
     * @param state  True if it is being hovered, false if not.
     */
    static void hover(FontAwesomeIconView button, boolean state) {
        Color color;
        if (state) {
            color = Color.BLACK;
        } else {
            color = Color.SILVER;
        }
        button.setFill(color);
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
     * Sets the delete listener for when the user deletes the preview.
     *
     * @param deleteListener The listener to set.
     */
    void setDeleteListener(Listener deleteListener) {
        this.deleteListener = deleteListener;
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
     * Handles when the user starts hovering the select button.
     */
    public void startHoverSelect() {
        hover(selectButton, true);
    }

    /**
     * Handles when the user stops hovering the select button.
     */
    public void stopHoverSelect() {
        hover(selectButton, false);
    }

    /**
     * Called when the user clicks the select button.
     */
    public void select() {
        selectListener.onEvent();
    }

    /**
     * Handles when the user starts hovering the delete button.
     */
    public void startHoverDelete() {
        hover(deleteButton, true);
    }

    /**
     * Handles when the user stops hovering the delete button.
     */
    public void stopHoverDelete() {
        hover(deleteButton, false);
    }

    /**
     * Called when the user clicks the delete button.
     */
    public void delete() {
        deleteListener.onEvent();
    }

}
