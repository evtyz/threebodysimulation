package stl.threebodysimulation;


/**
 * A class that represents error messages that can be shown in a popup.
 */
public enum ErrorMessage {
    /**
     * An error that occurs when inputs for a simulation are incorrect.
     */
    INPUT_ERROR {
        public String getTitle() {
            return "Parameter Error";
        }

        public String getMessage() {
            return "The simulation cannot be run, because some parameters are not valid. Please input valid numbers, and then try again.";
        }
    },
    /**
     * An error that occurs when an asymptote is detected in the simulation and it must stop.
     */
    ASYMPTOTE_ERROR {
        public String getTitle() {
            return "Simulation Error";
        }

        public String getMessage() {
            return "An asymptote has been detected, and the simulation has ceased. Please make sure your inputs do not lead to asymptotic behavior, and try again.";
        }
    },
    /**
     * An error that occurs when a double overflow occurs in a simulation and it must stop.
     */
    OVERFLOW_ERROR {
        public String getTitle() {
            return "Simulation Error";
        }

        public String getMessage() {
            return "One of the numbers being processed is too large to fit in a Java double. Please ensure that all your inputs are reasonably scaled.";
        }
    },
    /**
     * An unknown error that occurs.
     */
    UNKNOWN_ERROR {
        public String getTitle() {
            return "Simulation Error";
        }
        public String getMessage() {
            return "An unknown error occurred during the simulation. Please let the developers know, and try your simulation again.";
        }
    };

    /**
     * Gets the title of the error.
     * @return The title of the error, to be displayed in the window border.
     */
    public abstract String getTitle();

    /**
     * Gets the message of the error.
     * @return The message of the error, to be displayed in the body of the error popup.
     */
    public abstract String getMessage();
}
