package stl.threebodysimulation;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * This class is a wrapper around a TextField that connects it to a tooltip and ensures that inputs are valid.
 */
class TextFieldWrapper {
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

    /**
     * The prompt shown in the tooltip for the textfield.
     */
    String prompt;

    /**
     * The label next to the textfield, if applicable.
     */
    Label subjectLabel = new Label();

    /**
     * The regex format pattern that inputs must follow.
     */
    String regexPattern;

    /**
     * Constructor for a basic TextFieldWrapper that does not allow empty inputs.
     *
     * @param subject The TextField the wrapper manages.
     * @param tooltip The Tooltip that attaches to the TextField.
     * @param prompt  The prompt shown in the TextField's tooltip.
     */
    TextFieldWrapper(TextField subject, Tooltip tooltip, String prompt) {
        this.subject = subject;
        this.tooltip = tooltip;
        this.prompt = prompt;
        attachInputValidator();
        setupTooltip();
        limitInput();
        readiness = isValidInput();
        // Regex below from John Knoeller's answer at https://stackoverflow.com/questions/2338044/regex-letters-numbers-dashes-and-underscores.
        regexPattern = "([A-Za-z0-9\\-\\_]+)"; // Only accept these characters.
    }

    /**
     * Constructor for a TextFieldWrapper with a corresponding Label that can be disabled.
     *
     * @param subject      The TextField the wrapper manages.
     * @param tooltip      The Tooltip that attaches to the TextField.
     * @param prompt       The prompt shown in the TextField's tooltip.
     * @param subjectLabel The Label that corresponds to the TextField.
     */
    @SuppressWarnings("SameParameterValue") // In case other uses come up, this method remains parameterized.
    TextFieldWrapper(TextField subject, Tooltip tooltip, String prompt, Label subjectLabel) {
        this(subject, tooltip, prompt);
        this.subjectLabel = subjectLabel;
    }

    /**
     * Default constructor used by child classes.
     */
    TextFieldWrapper() {
    }


    /**
     * Limits the TextField with a TextFormatter that enforces the regex pattern.
     */
    // Anonymous function modified from DVarga's solution at https://stackoverflow.com/questions/49918079/javafx-textfield-text-validation.
    void limitInput() {
        // Ensures that the text-field only accepts numerical inputs.
        subject.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches(regexPattern) || change.getControlNewText().equals("")) {
                return change;
            }
            return null;
        }));
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
     *
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
     *
     * @param state Whether to enable or disable the TextField.
     */
    void changeState(boolean state) {
        if (state) {
            readiness = isValidInput();
            subject.setDisable(false);
            subjectLabel.setTextFill(Color.BLACK);
            return;
        }
        readiness = true;
        subject.clear();
        subject.setStyle("-fx-border-color: #cccccc;");
        subject.setStyle("-fx-background-color: white;");
        subject.setDisable(true);
        subjectLabel.setTextFill(Color.LIGHTGRAY);
    }

    /**
     * Set the text of the TextField.
     *
     * @param text The text that the TextField will be set to.
     */
    void setText(String text) {
        subject.setText(text);
        if (!isValidInput()) {
            highlightIncorrect();
            readiness = false;
        } else {
            subject.setStyle("-fx-border-color: #cccccc; -fx-background-color: white;");
            readiness = true;
        }
    }

    /**
     * Checks if the TextFieldWrapper is ready for simulation.
     *
     * @return True if ready, False if not ready.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted") // Inverted for clarity's sake.
    boolean isReady() {
        if (!readiness) {
            highlightIncorrect();
        }
        return readiness;
    }
}
