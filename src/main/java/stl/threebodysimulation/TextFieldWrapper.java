package stl.threebodysimulation;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class TextFieldWrapper {
    private TextField subject;
    private final double min;
    private final double max;
    private String decimalPattern;

    public TextFieldWrapper(TextField subject, double min, double max) {
        this.subject = subject;
        boolean allowNegative = min < 0;

        // Regexes modified from P. Jowko's implementation at https://stackoverflow.com/questions/40485521/javafx-textfield-validation-decimal-value.
        if (allowNegative) {
            decimalPattern = "[-]?[0-9]*(\\.[0-9]*)?";
        } else {
            decimalPattern = "[0-9]*(\\.[0-9]*)?";
        }
        limitToNumericalInput(allowNegative);
        this.min = min;
        this.max = max;
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
