package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Set;

// This class represents the FXML controller of the information shown for each object during the simulation, in the top right corner.
public class InfoFXMLController {

    // Declare our UI elements
    @FXML
    private Label objectLabel;

    @FXML
    private Label positionInfo;

    @FXML
    private Label velocityInfo;

    @FXML
    private Label accelerationInfo;

    @FXML
    public Circle objectCircle;

    private static DecimalFormat STANDARD_2_DIGIT =  new DecimalFormat("#####,##0.##");
    private static DecimalFormat STANDARD_5_DIGIT = new DecimalFormat("#####,##0.#####");

    private final int MAX_STANDARD_2_DIGIT_LENGTH = 10;
    private final int MAX_STANDARD_5_DIGIT_LENGTH = 7;

    // Declare some other useful variables
    private int id;

    private HashMap<String, Label> packagedLabels;

    public InfoFXMLController() {
        // An empty constructor for FXML to use. This is mandatory for the UI to work properly.
        // Additionally, we initialize id to -1, as a sentinel to show that we haven't fully set-up the UI yet.
        id = -1;
    }

    void setup(int id) {
        // Set the label to be "Object {id}"
        // INPUT:
        // id : int, the ID of the object. (e.g. object 1, 2, or 3)
        // USAGE:
        // >> setup(1) would cause objectLabel to show "Object 1".
        objectLabel.setText("Object " + id);
        this.id = id;
        packagedLabels = new HashMap<>();
        packagedLabels.put("position", positionInfo);
        packagedLabels.put("velocity", velocityInfo);
        packagedLabels.put("acceleration", accelerationInfo);

        STANDARD_2_DIGIT.setMaximumIntegerDigits(MAX_STANDARD_2_DIGIT_LENGTH);
        STANDARD_5_DIGIT.setMaximumIntegerDigits(MAX_STANDARD_5_DIGIT_LENGTH);
    }

    public void updateFromParticle(Particle particle) {
        // This method takes the Particle object and maps information onto corresponding UI labels.
        // INPUT:
        // particle : Particle, the Particle object that we are reading stats from.

        for (String key : packagedLabels.keySet()) { // TODO: Improve formatting so there is no overflow.
            double[] vector = particle.packagedInformation.get(key);
            String labelText;
            switch (InfoPanelFXMLController.chosenFormat) {
                case SCIENTIFIC_2:
                    labelText = String.format("[%.02e, %.02e]", vector[0], vector[1]);
                    break;
                case SCIENTIFIC_5:
                    labelText = String.format("[%.05e, %.05e]", vector[0], vector[1]);
                    break;
                case STANDARD_2:
                    labelText = String.format("[%s, %s]", formatNumberWithLimits(STANDARD_2_DIGIT, vector[0]), formatNumberWithLimits(STANDARD_2_DIGIT, vector[1]));
                    break;
                case STANDARD_5:
                    labelText = String.format("[%s, %s]", formatNumberWithLimits(STANDARD_5_DIGIT, vector[0]), formatNumberWithLimits(STANDARD_5_DIGIT, vector[1]));
                    break;
                default:
                    labelText = "N/A";
            }
            packagedLabels.get(key).setText(labelText);
        }
    }

    private static String formatNumberWithLimits(DecimalFormat format, double number) {
        if (Math.abs(number) >= Math.pow(10, format.getMaximumIntegerDigits())) {
            if (number > 0) {
                return "+Infinity";
            }
            return "-Infinity";
        }
        return format.format(number);
    }

    public void updateFromColor (Color color) {
            objectCircle.setFill(color);
    }
}
