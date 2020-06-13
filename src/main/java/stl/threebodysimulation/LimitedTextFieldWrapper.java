package stl.threebodysimulation;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
     * Basic constructor for a LimitedTextFieldWrapper that manages a TextField and related objects.
     *
     * @param subject              The TextField to be managed.
     * @param tooltip              The Tooltip of the TextField.
     * @param min                  The minimum value of the TextField.
     * @param max                  The maximum value of the TextField.
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
            regexPattern = "[-]?[0-9]*(\\.[0-9]*)?";
        } else {
            regexPattern = "[0-9]*(\\.[0-9]*)?";
        }

        limitInput();
        attachInputValidator();

        // Set up the tooltip with correct text and settings
        setupTooltip();

        readiness = isValidInput();
    }

    /**
     * Constructor for a LimitedTextFieldWrapper that manages a TextField and related objects, including a Label that can be disabled with the TextField.
     *
     * @param subject              The TextField to be managed.
     * @param tooltip              The Tooltip of the TextField.
     * @param min                  The minimum value of the TextField.
     * @param max                  The maximum value of the TextField.
     * @param allowBottomInclusive Whether the minimum value of the TextField is valid or not.
     * @param subjectLabel         The Label UI object that corresponds with the TextField.
     */
    @SuppressWarnings("SameParameterValue")
    // In case other use-cases come up, this constructor remains parameterized.
    LimitedTextFieldWrapper(TextField subject, Tooltip tooltip, double min, double max, boolean allowBottomInclusive, Label subjectLabel) {
        this(subject, tooltip, min, max, allowBottomInclusive);
        this.subjectLabel = subjectLabel;
    }

    /**
     * Checks if the textfield included in the wrapper can be validly formatted into a double within the limits.
     *
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
        } catch (NumberFormatException e) { // Not parsable as a double for whatever reason. Should never occur.
            return false;
        }
    }
}
