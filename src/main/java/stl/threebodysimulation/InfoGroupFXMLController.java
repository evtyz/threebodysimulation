package stl.threebodysimulation;

import javafx.fxml.FXML;

public class InfoGroupFXMLController {

    @FXML
    private InfoFXMLController object1InfoController;

    @FXML
    private InfoFXMLController object2InfoController;

    @FXML
    private InfoFXMLController object3InfoController;

    public InfoGroupFXMLController() {}

    private InfoFXMLController[] infoControllers;

    public void setup() {
        infoControllers = new InfoFXMLController[] {object1InfoController, object2InfoController, object3InfoController};
        for (int i = 0; i < 3; i++) {
            infoControllers[i].setup((byte)(i+1));
        }
    }
}
