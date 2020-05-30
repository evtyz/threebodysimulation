package stl.threebodysimulation;

// This class sets up all possible number formats.
public enum NumberFormat {
    SCIENTIFIC_2 {
        public String toString() {
            return "Scientific, 2 decimal places";
        }
    },
    SCIENTIFIC_5 {
        public String toString() {
            return "Scientific, 5 decimal places";
        }
    },
    STANDARD_2 {
        public String toString() {
            return "Standard, 2 decimal places";
        }
    },
    STANDARD_5 {
        public String toString() {
            return "Standard, 5 decimal places";
        }
    }
}
