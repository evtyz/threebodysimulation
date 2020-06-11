package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * A controller that manages each object's info display in the top right.
 */
public class InfoFXMLController {

    /**
     * The decimal format of a standard, 2 decimal place number.
     */
    private static final DecimalFormat STANDARD_2_DIGIT = new DecimalFormat("#####,##0.##");

    /**
     * The decimal format of a standard, 5 decimal place number.
     */
    private static final DecimalFormat STANDARD_5_DIGIT = new DecimalFormat("#####,##0.#####");

    /**
     * The maximum number of digits to the left of the decimal point in a standard 2 decimal place number.
     */
    private static final int MAX_STANDARD_2_DIGIT_LENGTH = 10;

    /**
     * The maximum number of digits to the left of the decimal point in a standard 5 decimal place number.
     */
    private static final int MAX_STANDARD_5_DIGIT_LENGTH = 7;

    /**
     * The TitledPane that contains the information and the title.
     */
    @FXML
    private TitledPane titleInfo;

    /**
     * The Label UI element that displays position info for an object.
     */
    @FXML
    private Label positionInfo;

    /**
     * The Label UI element that displays velocity info for an object.
     */
    @FXML
    private Label velocityInfo;

    /**
     * The Label UI element that displays acceleration info for an object.
     */
    @FXML
    private Label accelerationInfo;

    /**
     * A hashmap that allows for easy setting of info labels using String keys.
     */
    private HashMap<String, Label> packagedLabels;

    /**
     * An empty constructor used by the FXML loader.
     */
    public InfoFXMLController() {
    }

    /**
     * Formats a number into a string based on input DecimalFormat object.
     *
     * @param format The chosen format to show the number with.
     * @param number The number to format.
     * @return The formatted string that represents the number.
     */
    private static String formatNumberWithLimits(DecimalFormat format, double number) {
        // Check for overflow
        if (Math.abs(number) >= Math.pow(10, format.getMaximumIntegerDigits())) {
            if (number > 0) {
                return "+Infinity";
            }
            return "-Infinity";
        }
        // Otherwise, regularly format.
        return format.format(number);
    }

    /**
     * Sets up the info display at the start of the program.
     *
     * @param id the ID of the object. E.g. object 2 has id 2.
     */
    void setup(int id) {
        // Set the id of the display.
        titleInfo.setText("Object " + id);

        // Set up the hashmap with position, velocity, acceleration string keys.
        packagedLabels = new HashMap<>();
        packagedLabels.put("position", positionInfo);
        packagedLabels.put("velocity", velocityInfo);
        packagedLabels.put("acceleration", accelerationInfo);

        // Set up decimal formats with correct maximum digit limits.
        STANDARD_2_DIGIT.setMaximumIntegerDigits(MAX_STANDARD_2_DIGIT_LENGTH);
        STANDARD_5_DIGIT.setMaximumIntegerDigits(MAX_STANDARD_5_DIGIT_LENGTH);
    }

    /**
     * Updates the info display based on the state of a given particle.
     *
     * @param particle The particle that the display takes information from.
     */
    void updateFromParticle(Particle particle) {
        // Iterates through position, velocity, and then acceleration.
        for (String key : packagedLabels.keySet()) {
            // Get the vector.
            double[] vector = particle.getPackage().get(key);
            String labelText;
            // Display it according to the correct number format.
            switch (InfoPanelFXMLController.getNumberFormat()) {
                case ADAPTIVE:
                    labelText = String.format("[%.05g, %.05g]", vector[0], vector[1]);
                    break;
                case SCIENTIFIC_7:
                    labelText = String.format("[%.07e, %.07e]", vector[0], vector[1]);
                    break;
                case STANDARD_5:
                    labelText = String.format("[%s, %s]", formatNumberWithLimits(STANDARD_5_DIGIT, vector[0]), formatNumberWithLimits(STANDARD_5_DIGIT, vector[1]));
                    break;
                case STANDARD_2:
                    labelText = String.format("[%s, %s]", formatNumberWithLimits(STANDARD_2_DIGIT, vector[0]), formatNumberWithLimits(STANDARD_2_DIGIT, vector[1]));
                    break;
                default:
                    labelText = "N/A";
            }
            // Set the text of the specific label.
            packagedLabels.get(key).setText(labelText);
        }
    }

    /**
     * Changes the color of the circle next to the info display.
     *
     * @param color The color to update the display with.
     */
    void updateFromColor(Color color) {
        Circle colorCircle = new Circle(7);
        colorCircle.setFill(color);
        titleInfo.setGraphic(colorCircle);
    }
}
