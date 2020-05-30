package stl.threebodysimulation;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    // This represents whether the TextField is ready to send its value for simulation.
    private boolean readiness;

    private final boolean allowBottomInclusive;

    // Constructor of object
    public TextFieldWrapper(TextField subject, Tooltip tooltip, double min, double max, boolean allowBottomInclusive) {

        // Initializes textfield and tooltip
        this.subject = subject;
        this.tooltip = tooltip;

        // Setup mins and maxes
        this.min = min;
        this.max = max;

        // Whether the max and min are valid or not
        this.allowBottomInclusive = allowBottomInclusive;

        readiness = false;

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
    private void limitToNumericalInput() {
        // Ensures that the text-field only accepts numerical inputs.
        subject.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches(decimalPattern)) ? change : null));
    }

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

    // Function that attaches a listener on the TextView to check if the input is within the min and max.
    // Structure modified from Brendan's answer at https://stackoverflow.com/questions/16549296/how-perform-task-on-javafx-textfield-at-onfocus-and-outfocus
    private void attachInputValidator() {
        subject.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldFocus, Boolean newFocus) {
                if (!newFocus) {
                    if (!isValidInput()) {
                        subject.setStyle("-fx-border-color: #ff4444; -fx-background-color: #fff9f9;");
                        readiness = false;
                    } else {
                        subject.setStyle("-fx-border-color: #cccccc;");
                        subject.setStyle("-fx-background-color: white;");
                        readiness = true;
                    }
                } else {
                    subject.setStyle("-fx-border-color: #66afe9");
                    subject.setStyle("-fx-background-color: white;");
                }
            }
        });
    }

    // Disables or enables a textfield.
    public void changeState(boolean state) {
        // INPUTS:
        // state: boolean, true if enabled, false if disabled.

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

    // returns readiness value
    public boolean isReady() {
        return readiness;
    }
}
