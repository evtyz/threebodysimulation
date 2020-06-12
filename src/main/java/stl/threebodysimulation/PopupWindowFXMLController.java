package stl.threebodysimulation;

import javafx.fxml.Initializable;
import javafx.scene.image.Image;

import java.net.URL;
import java.util.ResourceBundle;

abstract class PopupWindowFXMLController implements Initializable {
    /**
     * Initializes the window, according to the Application parent class. Empty.
     *
     * @param location  Not used.
     * @param resources Not used.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Sets the message of the popup window.
     *
     * @param label The message.
     */
    abstract void setLabel(String label);

    /**
     * Sets a listener for user's actions. By default, does nothing.
     *
     * @param confirmListener The listener that is set.
     */
    void setConfirmListener(Listener confirmListener) {

    }

    /**
     * Gets the icon used for the popup window.
     *
     * @return The image icon used for the popup window.
     */
    abstract Image getIcon();

}
