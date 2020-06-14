package stl.threebodysimulation;

import javafx.scene.image.Image;

/**
 * The FXML controller for the confirmation dialog that appears when overwriting a file.
 */
public class WarningWindowFXMLController extends ErrorWindowFXMLController {

    /**
     * A listener that is called when the user confirms the warning.
     */
    private Listener confirmListener;

    /**
     * Method that is called when the user confirms the action.
     */
    public void confirm() {
        confirmListener.onEvent();
        closeWindow();
    }

    /**
     * Sets the listener object for confirmation.
     *
     * @param confirmListener The listener that will be called upon confirmation.
     */
    @Override
    void setConfirmListener(Listener confirmListener) {
        this.confirmListener = confirmListener;
    }

    /**
     * Gets the icon for the popup window.
     *
     * @return The icon.
     */
    @Override
    Image getIcon() {
        return new Image("/stl/threebodysimulation/icons/warningIcon.png");
    }
}
