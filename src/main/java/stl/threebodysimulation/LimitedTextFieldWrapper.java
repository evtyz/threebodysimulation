package stl.threebodysimulation;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;

import java.text.DecimalFormat;


/**
 * This class is a wrapper around a TextField that connects it with related tooltips, limits, and relevant methods.
 */
class LimitedTextFieldWrapper extends TextFieldWrapper {

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
     * The regex format pattern that inputs must follow.
     */
    private String decimalPattern;

    /**
     * @param subject The TextField to be managed.
     * @param tooltip The Tooltip of the TextField.
     * @param min The minimum value of the TextField.
     * @param max The maximum value of the TextField.
     * @param allowBottomInclusive Whether the minimum value of the TextField is valid or not.
     */
    LimitedTextFieldWrapper(TextField subject, Tooltip tooltip, double min, double max, boolean allowBottomInclusive) {

        // Initializes textfield and tooltip
        this.subject = subject;
        this.tooltip = tooltip;

        // Setup mins and maxes
        this.min = min;
        this.max = max;
        this.prompt = String.format("Must be between %s and %s", TOOLTIP_FORMAT.format(min), TOOLTIP_FORMAT.format(max));

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
     * Limits the TextField with a TextFormatter that enforces the regex pattern.
     */
    // Anonymous function modified from DVarga's solution at https://stackoverflow.com/questions/49918079/javafx-textfield-text-validation.
    private void limitToNumericalInput() {
        // Ensures that the text-field only accepts numerical inputs.
        subject.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches(decimalPattern)) ? change : null));
    }

    /**
     * Checks if the textfield included in the wrapper can be validly formatted into a double within the limits.
     * @return True if valid, False if invalid.
     */
    @Override
    boolean isValidInput() {
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
}
