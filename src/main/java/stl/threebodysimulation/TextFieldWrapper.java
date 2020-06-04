package stl.threebodysimulation;

import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class TextFieldWrapper {
    /**
     * The TextField that is being wrapped.
     */
    TextField subject;

    /**
     * The Tooltip that is being used with the TextField.
     */
    Tooltip tooltip;

    /**
     * Whether the LimitedTextFieldWrapper is ready for simulation.
     */
    boolean readiness;

    String prompt;

    /**
     * Constructor for a basic TextFieldWrapper that does not allow empty inputs.
     * @param subject The TextField the wrapper manages.
     * @param tooltip the Tooltip that attaches to the TextField.
     * @param prompt The prompt shown in the TextField's tooltip.
     */
    TextFieldWrapper(TextField subject, Tooltip tooltip, String prompt) {
        this.subject = subject;
        this.tooltip = tooltip;
        this.prompt = prompt;
        attachInputValidator();
        setupTooltip();
        readiness = isValidInput();
    }

    /**
     * Default constructor used by child classes.
     */
    TextFieldWrapper() {

    }

    /**
     * Initializes a tooltip with a label.
     */
    void setupTooltip() {
        // Tooltips show up after 100 milliseconds of hovering
        tooltip.setShowDelay(new Duration(100));

        // Set the text of the tooltip to display the min and max.
        tooltip.setText(prompt);
    }

    /**
     * Checks if the textfield included in the wrapper is not empty.
     * @return True if not empty, False if empty.
     */
    boolean isValidInput() {
        return (!subject.getText().equals(""));
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
    void attachInputValidator() {
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
