package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

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

    private TextFieldWrapper[] allTextFields;

    // Object ID e.g. Object 1, 2, or 3.
    private byte id;

    // Empty constructor for use by FXML. Must exist.
    // Also initializes id to -1 so that other methods know that setup isn't finished yet.
    public ParameterFXMLController() {
        id = -1;
    }

    public static final double MAX_ABS_STARTING_POSITION = 100000; // TODO: Calculate
    public static final double MAX_ABS_STARTING_VELOCITY = 100000; // TODO: Calculate
    public static final double MAX_MASS = 10000; // TODO: Calculate

    void setup(byte id, Color color) {
        // Sets the id of the object and changes the label to reflect this.
        // Also sets up each text-field to limit input correctly.
        // INPUT:
        // id: byte, the id of the object (e.g. Object 1, 2, or 3)
        // USAGE:
        // >> setup(1) -> objectLabel shows "Object 1"
        objectLabel.setText("Object " + id);
        this.id = id;

        colorPickerField.setValue(color);

        allTextFields = new TextFieldWrapper[] {
                new TextFieldWrapper(massField, 0, MAX_MASS),
                new TextFieldWrapper(xPositionField, -MAX_ABS_STARTING_POSITION, MAX_ABS_STARTING_POSITION),
                new TextFieldWrapper(yPositionField, -MAX_ABS_STARTING_POSITION, MAX_ABS_STARTING_POSITION),
                new TextFieldWrapper(xVelocityField, -MAX_ABS_STARTING_VELOCITY, MAX_ABS_STARTING_VELOCITY),
                new TextFieldWrapper(yVelocityField, -MAX_ABS_STARTING_VELOCITY, MAX_ABS_STARTING_VELOCITY)
        };
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
                Double.parseDouble(xPositionField.getText()),
                Double.parseDouble(yPositionField.getText()),
                Double.parseDouble(xVelocityField.getText()),
                Double.parseDouble(yVelocityField.getText()),
                Double.parseDouble(massField.getText()),
                id,
                colorPickerField.getValue());
    }
}
