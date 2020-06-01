package stl.threebodysimulation;

// This class sets up all possible number formats.
public enum NumberFormat {
    ADAPTIVE {
        public String toString() {
            return "Adaptive";
        }
    },
    SCIENTIFIC_7 {
        public String toString() {
            return "Scientific, 7 decimal places";
        }
    },
    STANDARD_5 {
        public String toString() {
            return "Standard, 5 decimal places";
        }
    },
    STANDARD_2 {
        public String toString() {
            return "Standard, 2 decimal places";
        }
    }

}
