package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.material.Material;


/**
 * The FXMLController for the UI element that shows previews of saved templates.
 */
public class SavePreviewFXMLController extends DefaultSavePreviewFXMLController {

    /**
     * The listener that is called when the user deletes this preview.
     */
    private Listener deleteListener;

    /**
     * The delete button.
     */
    @FXML
    private Button deleteButton;


    /**
     * The constructor for the FXMLLoader.
     */
    public SavePreviewFXMLController() {
    }

    /**
     * Sets the delete listener for when the user deletes the preview.
     *
     * @param deleteListener The listener to set.
     */
    void setDeleteListener(Listener deleteListener) {
        this.deleteListener = deleteListener;
    }

    @Override
    void setSettings(SimulationSettings settings) {
        super.setSettings(settings);
        deleteButton.setGraphic(
                SceneFXMLController.buildIcon(Material.CLEAR, Color.WHITE, 20)
        );
    }


    /**
     * Called when the user clicks the delete button.
     */
    public void delete() {
        deleteListener.onEvent();
    }

}
