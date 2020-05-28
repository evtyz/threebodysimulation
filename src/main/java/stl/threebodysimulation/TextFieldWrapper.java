package stl.threebodysimulation;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

import java.text.DecimalFormat;

// This class is a wrapper around a TextField that connects it with related tooltips, limits, and relevant methods.
public class TextFieldWrapper {
    private final double min;
    private final double max;
    // Instance variables
    private TextField subject;
    private Tooltip tooltip;
    // This controls what characters can be typed into the textbox.
    private String decimalPattern;

    // Constructor of object
    public TextFieldWrapper(TextField subject, Tooltip tooltip, double min, double max) {

        // Initializes textfield and tooltip
        this.subject = subject;
        this.tooltip = tooltip;

        // Checks if the minimum value allows negative numbers
        boolean allowNegative = min < 0;

        // Regexes modified from P. Jowko's implementation at https://stackoverflow.com/questions/40485521/javafx-textfield-validation-decimal-value.
        if (allowNegative) {
            decimalPattern = "[-]?[0-9]*(\\.[0-9]*)?";
        } else {
            decimalPattern = "[0-9]*(\\.[0-9]*)?";
        }
        limitToNumericalInput(allowNegative);

        // Setup mins and maxes
        this.min = min;
        this.max = max;

        // Set up the tooltip with correct text and settings
        setupTooltip();
    }

    private void setupTooltip() {
        // Void method that initializes a tooltip

        // This allows us to round to the correct number of decimal places
        DecimalFormat decimalFormat = new DecimalFormat("0.##");

        // Tooltips show up after 100 milliseconds of hovering
        tooltip.setShowDelay(new Duration(100));

        // Set the text of the tooltip to display the min and max.
        tooltip.setText(String.format("Number between %s and %s", decimalFormat.format(min), decimalFormat.format(max)));
    }

    // Anonymous function modified from DVarga's solution at https://stackoverflow.com/questions/49918079/javafx-textfield-text-validation.
    private void limitToNumericalInput(boolean allowNegative) {
        // Ensures that the text-field only accepts numerical inputs.
        // INPUT:
        // allowNegative: boolean, whether negative numbers are allowed in the textfield or not.
        subject.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches(decimalPattern)) ? change : null));
    }
}
