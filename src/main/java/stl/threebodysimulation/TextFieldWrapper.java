package stl.threebodysimulation;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

import java.text.DecimalFormat;


/**
 * This class is a wrapper around a TextField that connects it with related tooltips, limits, and relevant methods.
 */
class TextFieldWrapper {

    /**
     * The DecimalFormat that the tooltip is in.
     */
    private static final DecimalFormat TOOLTIP_FORMAT = new DecimalFormat("###,##0.####");

    /**
     * The minimum value of the TextField.
     */
    private final double min;

    /**
     * The maximum value of the TextField.
     */
    private final double max;

    /**
     * Whether the minimum value is inclusive or not.
     */
    private final boolean allowBottomInclusive;

    /**
     * The TextField that is being wrapped.
     */
    private TextField subject;

    /**
     * The Tooltip that is being used with the TextField.
     */
    private Tooltip tooltip;

    /**
     * The regex format pattern that inputs must follow.
     */
    private String decimalPattern;

    /**
     * Whether the TextFieldWrapper is ready for simulation.
     */
    private boolean readiness;

    /**
     * @param subject The TextField to be managed.
     * @param tooltip The Tooltip of the TextField.
     * @param min The minimum value of the TextField.
     * @param max The maximum value of the TextField.
     * @param allowBottomInclusive Whether the minimum value of the TextField is valid or not.
     */
    TextFieldWrapper(TextField subject, Tooltip tooltip, double min, double max, boolean allowBottomInclusive) {

        // Initializes textfield and tooltip
        this.subject = subject;
        this.tooltip = tooltip;

        // Setup mins and maxes
        this.min = min;
        this.max = max;

        // Whether the max and min are valid or not
        this.allowBottomInclusive = allowBottomInclusive;


        // Checks if the minimum value allows negative numbers
        boolean allowNegative = min < 0;

        // Regexes modified from P. Jowko's implementation at https://stackoverflow.com/questions/40485521/javafx-textfield-validation-decimal-value.
        if (allowNegative) {
            decimalPattern = "[-]?[0-9]*(\\.[0-9]*)?";
        } else {
            decimalPattern = "[0-9]*(\\.[0-9]*)?";
        }

        limitToNumericalInput();
        attachInputValidator();

        // Set up the tooltip with correct text and settings
        setupTooltip();

        readiness = isValidInput();
    }

    /**
     * Initializes a tooltip with a label.
     */
    private void setupTooltip() {
        // Tooltips show up after 100 milliseconds of hovering
        tooltip.setShowDelay(new Duration(100));

        // Set the text of the tooltip to display the min and max.
        tooltip.setText(String.format("Must be between %s and %s", TOOLTIP_FORMAT.format(min), TOOLTIP_FORMAT.format(max)));
    }

    /**
     * Limits the TextField with a TextFormatter that enforces the regex pattern.
     */
    // Anonymous function modified from DVarga's solution at https://stackoverflow.com/questions/49918079/javafx-textfield-text-validation.
    private void limitToNumericalInput() {
        // Ensures that the text-field only accepts numerical inputs.
        subject.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches(decimalPattern)) ? change : null));
    }

    /**
     * Checks if the textfield included in the wrapper has a valid input or not.
     * @return True if valid, False if invalid.
     */
    private boolean isValidInput() {
        try {
            double value = Double.parseDouble(subject.getText());
            if (allowBottomInclusive) {
                return (min <= value && value <= max);
            }
            return (min < value && value <= max);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Highlights the TextField as incorrect with colouring.
     */
    private void highlightIncorrect() {
        subject.setStyle("-fx-border-color: #ff4444; -fx-background-color: #fff9f9;");
    }

    /**
     * Attaches a listener on the TextView to check if the input is within the min and max.
     */
    // Structure modified from Brendan's answer at https://stackoverflow.com/questions/16549296/how-perform-task-on-javafx-textfield-at-onfocus-and-outfocus
    private void attachInputValidator() {
        subject.focusedProperty().addListener((arg0, oldFocus, newFocus) -> {
            // If clicked away.
            if (!newFocus) {
                if (!isValidInput()) {
                    highlightIncorrect();
                    readiness = false;
                } else {
                    // Reset style
                    subject.setStyle("-fx-border-color: #cccccc; -fx-background-color: white;");
                    readiness = true;
                }
            } else {
                // Reset style
                subject.setStyle("-fx-border-color: #66afe9; -fx-background-color: white;");
            }
        });
    }

    /**
     * Disables or enables a TextField.
     * @param state Whether to enable or disable the TextField.
     */
    void changeState(boolean state) {
        if (state) {
            readiness = false;
            subject.setDisable(false);
            return;
        }
        readiness = true;
        subject.clear();
        subject.setStyle("-fx-border-color: #cccccc;");
        subject.setStyle("-fx-background-color: white;");
        subject.setDisable(true);
    }

    /**
     * Checks if the TextFieldWrapper is ready for simulation.
     * @return True if ready, False if not ready.
     */
    boolean isReady() {
        if (!readiness) {
            highlightIncorrect();
        }
        return readiness;
    }
}
