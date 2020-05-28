package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

// This class represents the FXML controller of the editable parameters shown for each object before the simulation, on the left.
public class ParameterFXMLController {

    // UI element declarations

    @FXML
    private Label objectLabel;

    @FXML
    private ColorPicker colorPickerField;

    @FXML
    private TextField massField;

    @FXML
    private TextField xPositionField;

    @FXML
    private TextField yPositionField;

    @FXML
    private TextField xVelocityField;

    @FXML
    private TextField yVelocityField;

    // Object ID e.g. Object 1, 2, or 3.
    private int id;

    // Empty constructor for use by FXML. Must exist.
    // Also initializes id to -1 so that other methods know that setup isn't finished yet.
    public ParameterFXMLController() {
        id = -1;
    }

    void setup(int id) {
        // Sets the id of the object and changes the label to reflect this.
        // INPUT:
        // id: int, the id of the object (e.g. Object 1, 2, or 3)
        // USAGE:
        // >> setup(1) -> objectLabel shows "Object 1"
        objectLabel.setText("Object " + id);
        this.id = id;
    }

    public boolean isValidObject() {
        // OUTPUT:
        // boolean, whether this object is ready to be simulated or not.
        //TODO: should return false if the object isn't ready to be simulated yet.
        return true;
    }

    public Particle convertToParticle() {
        // Converts parameters from inputs to a Particle object for simulation purposes.
        // OUTPUT:
        // Particle, the particle that represents the initial state of the object.
        return new Particle(
                Integer.parseInt(xPositionField.getText()),
                Integer.parseInt(yPositionField.getText()),
                Integer.parseInt(xVelocityField.getText()),
                Integer.parseInt(yVelocityField.getText()),
                Integer.parseInt(massField.getText()),
                id,
                colorPickerField.getValue());
    }
}
