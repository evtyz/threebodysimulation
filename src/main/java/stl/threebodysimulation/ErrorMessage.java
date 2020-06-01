package stl.threebodysimulation;


// TODO: Documentation
public enum ErrorMessage {
    INPUT_ERROR {
        public String getTitle() {
            return "Parameter Error";
        }
        public String getMessage() {
            return "The simulation cannot be run, because some parameters are not valid. Please input valid numbers, and then try again.";
        }
    },
    ASYMPTOTE_ERROR {
        public String getTitle() {
            return "Simulation Error";
        }
        public String getMessage() {
            return "An asymptote has been detected, and the simulation has ceased. Please make sure your inputs do not lead to asymptotic behavior, and try again.";
        }
    },
    OVERFLOW_ERROR {
        public String getTitle() {
            return "Simulation Error";
        }
        public String getMessage() {
            return "One of the numbers being processed is too large to fit in a Java double. Please ensure that all your inputs are reasonably scaled.";
        }
    };

    public abstract String getTitle();

    public abstract String getMessage();
}
