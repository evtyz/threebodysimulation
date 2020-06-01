package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;

// This class represents the FXML controller of the editable parameters shown for each object before the simulation, on the left.
public class ParameterFXMLController {

    // Min and Max values for position, velocity, mass
    private static final double MAX_ABS_STARTING_POSITION = 100000000; // TODO: Calculate
    private static final double MAX_ABS_STARTING_VELOCITY = 10000; // TODO: Calculate
    private static final double MAX_MASS = 1000000; // TODO: Calculate

    // UI element declarations
    @FXML
    private Label objectLabel;
    @FXML
    private ColorPicker colorPickerField;
    @FXML
    private TextField massField;
    @FXML
    private Tooltip massTooltip;
    @FXML
    private TextField xPositionField;
    @FXML
    private Tooltip xPositionTooltip;
    @FXML
    private TextField yPositionField;
    @FXML
    private Tooltip yPositionTooltip;
    @FXML
    private TextField xVelocityField;
    @FXML
    private Tooltip xVelocityTooltip;
    @FXML
    private TextField yVelocityField;
    @FXML
    private Tooltip yVelocityTooltip;
    private TextFieldWrapper[] allTextFields;
    // Object ID e.g. Object 1, 2, or 3.
    private int id;

    // Empty constructor for use by FXML. Must exist.
    public ParameterFXMLController() {
    }

    void setup(int id, Color color) {
        // Sets the id of the object and changes the label to reflect this.
        // Also sets up each text-field to limit input correctly.
        // INPUT:
        // id: int, the id of the object (e.g. Object 1, 2, or 3)
        // USAGE:
        // >> setup(1) -> objectLabel shows "Object 1"
        objectLabel.setText("Object " + id);
        this.id = id;

        // Set up default color for this object
        colorPickerField.setValue(color);

        // Wrap all text-fields and related tooltips into cohesive TextFieldWrapper class with min and max values.
        allTextFields = new TextFieldWrapper[]{
                new TextFieldWrapper(massField, massTooltip, 0, MAX_MASS, false), // can't have zero mass
                new TextFieldWrapper(xPositionField, xPositionTooltip, -MAX_ABS_STARTING_POSITION, MAX_ABS_STARTING_POSITION, true),
                new TextFieldWrapper(yPositionField, yPositionTooltip, -MAX_ABS_STARTING_POSITION, MAX_ABS_STARTING_POSITION, true),
                new TextFieldWrapper(xVelocityField, xVelocityTooltip, -MAX_ABS_STARTING_VELOCITY, MAX_ABS_STARTING_VELOCITY, true),
                new TextFieldWrapper(yVelocityField, yVelocityTooltip, -MAX_ABS_STARTING_VELOCITY, MAX_ABS_STARTING_VELOCITY, true)
        };
    }

    boolean isReady() {
        // OUTPUT:
        // boolean, whether this object is ready to be simulated or not.

        boolean readiness = true;

        // All wrappers must be called so that they can highlight themselves if they are not ready.
        for (TextFieldWrapper textField : allTextFields) {
            if (textField.isReady()) {
                readiness = false;
            }
        }
        return readiness;
    }

    Particle convertToParticle() {
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
