package stl.threebodysimulation;

import javafx.fxml.FXML;

// Controller for particle info panel on top right
public class InfoPanelFXMLController {

    // UI element declarations (controllers represent each individual stat panel for objects)
    @FXML
    private InfoFXMLController object1InfoController;

    @FXML
    private InfoFXMLController object2InfoController;

    @FXML
    private InfoFXMLController object3InfoController;

    private InfoFXMLController[] infoControllers;

    // Default constructor for FXML
    public InfoPanelFXMLController() {
    }

    // Setup method called by scenecontroller
    public void setup() {
        // initialize array
        infoControllers = new InfoFXMLController[]{object1InfoController, object2InfoController, object3InfoController};
        // setup each individual controller with correct ids
        for (int id = 0; id < 3; id++) {
            infoControllers[id].setup((byte) (id + 1));
        }
    }
}
