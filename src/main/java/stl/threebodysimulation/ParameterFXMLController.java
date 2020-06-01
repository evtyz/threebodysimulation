package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;

/**
 * A class that represents the FXML controller of each individual particle's input fields.
 */
public class ParameterFXMLController {

    /**
     * Maximum starting position, in km, of a particle, in both positive and negative directions.
     */
    private static final double MAX_ABS_STARTING_POSITION = 100000000;

    /**
     * Maximum starting velocity, in km/s, of a particle, in both positive and negative directions.
     */
    private static final double MAX_ABS_STARTING_VELOCITY = 10000;

    /**
     * Maximum starting mass, in earth units, of a particle. The minimum is zero.
     */
    private static final double MAX_MASS = 1000000;

    /**
     * The Label UI object that represents the object's text label, e.g. "Object 1"
     */
    @FXML
    private Label objectLabel;

    /**
     * The ColorPicker UI object, that represents the field where users can pick the particle's color.
     */
    @FXML
    private ColorPicker colorPickerField;

    /**
     * The TextField UI object where mass can be entered.
     */
    @FXML
    private TextField massField;

    /**
     * The Tooltip UI object that gives hints about mass limits.
     */
    @FXML
    private Tooltip massTooltip;

    /**
     * The TextField UI object where x-position can be entered.
     */
    @FXML
    private TextField xPositionField;

    /**
     * The Tooltip UI object that gives hints about x-position limits.
     */
    @FXML
    private Tooltip xPositionTooltip;

    /**
     * The TextField UI object where y-position can be entered.
     */
    @FXML
    private TextField yPositionField;

    /**
     * The Tooltip UI object that gives hints about y-position limits.
     */
    @FXML
    private Tooltip yPositionTooltip;

    /**
     * The TextField UI object where x-velocity can be entered.
     */
    @FXML
    private TextField xVelocityField;

    /**
     * The Tooltip UI object that gives hints about x-velocity limits.
     */
    @FXML
    private Tooltip xVelocityTooltip;

    /**
     * The TextField UI object where y-velocity can be entered.
     */
    @FXML
    private TextField yVelocityField;

    /**
     * The Tooltip UI object that gives hints about y-velocity limits.
     */
    @FXML
    private Tooltip yVelocityTooltip;

    /**
     * The array that contains wrappers for all the text fields for this particular particle.
     */
    private TextFieldWrapper[] allTextFields;

    /**
     * The id of the particle, one of {1, 2, 3}
     */
    private int id;

    /**
     * Constructor for FXML loader.
     */
    public ParameterFXMLController() {
    }

    /**
     * Sets up the initial state of the particle settings UI.
     * @param id The id of the particle that the settings represent.
     * @param color The color of the particle that the settings represent.
     */
    void setup(int id, Color color) {
        // Sets id
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

    /**
     * Checks if all inputs are valid.
     * @return Whether the object parameters are ready to be simulated
     */
    boolean isReady() {
        // By default, it is ready.
        boolean readiness = true;

        // All wrappers must be called so that they can highlight themselves if they are not ready.
        for (TextFieldWrapper textField : allTextFields) {
            if (!textField.isReady()) {
                readiness = false;
            }
        }
        return readiness;
    }

    /**
     * Packages user input into a particle object.
     * @return The particle object that the settings represent
     */
    Particle convertToParticle() {
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
