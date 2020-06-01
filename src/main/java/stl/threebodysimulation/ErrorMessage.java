package stl.threebodysimulation;

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
    };

    public abstract String getTitle();

    public abstract String getMessage();
}
