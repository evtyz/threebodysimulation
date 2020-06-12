package stl.threebodysimulation;


/**
 * A class that represents error messages that can be shown in a popup.
 */
enum ErrorMessage implements PopupMessage {
    /**
     * An error that occurs when inputs for a simulation are incorrect.
     */
    INPUT_ERROR {
        @Override
        public String getTitle() {
            return "Parameter Error";
        }

        @Override
        public String getMessage() {
            return "The simulation cannot be run, because some parameters are not valid. Please input valid numbers, and then try again.";
        }
    },
    /**
     * An error that occurs if the user tries to save a template without incorrect parameters.
     */
    SAVE_ERROR {
        @Override
        public String getTitle() {
            return "Template Error";
        }

        @Override
        public String getMessage() {
            return "The template cannot be saved, because some parameters are not valid. Please provide valid inputs, and then try again.";
        }
    },
    /**
     * An error that occurs when an asymptote is detected in the simulation and it must stop.
     */
    ASYMPTOTE_ERROR {
        @Override
        public String getTitle() {
            return "Simulation Error";
        }

        @Override
        public String getMessage() {
            return "An asymptote has been detected, and the simulation has ceased. Please make sure your inputs do not lead to asymptotic behavior, and try again.";
        }
    },
    /**
     * An error that occurs when a double overflow occurs in a simulation and it must stop.
     */
    OVERFLOW_ERROR {
        @Override
        public String getTitle() {
            return "Simulation Error";
        }

        @Override
        public String getMessage() {
            return "One of the numbers being processed is too large to fit in a Java double. Please ensure that all your inputs are reasonably scaled.";
        }
    },
    /**
     * An error that occurs if a file is unable to be deleted.
     */
    DELETE_ERROR {
        @Override
        public String getTitle() {
            return "Delete File Error";
        }

        @Override
        public String getMessage() {
            return "The file cannot be deleted. It might be open in another window. Close all other instances of this app before trying again.";
        }
    },
    /**
     * An error that occurs when the user attempts to overwrite a CSV file that refuses to be edited.
     */
    OVERWRITE_ERROR {
        @Override
        public String getTitle() {
            return "Overwrite CSV Error";
        }
        @Override
        public String getMessage() {
            return "The CSV file you specified cannot be edited. It might be open in another window. Please close all other instances of this app and try again.";
        }
    },
    /**
     * An unknown error that occurs.
     */
    UNKNOWN_ERROR {
        @Override
        public String getTitle() {
            return "Simulation Error";
        }

        @Override
        public String getMessage() {
            return "An unknown error occurred during the simulation. Please let the developers know, and try your simulation again.";
        }
    }

}
