package stl.threebodysimulation;

/**
 * A class that represents all possible number display formats.
 */
public enum NumberFormat {
    /**
     * Uses either standard or scientific, depending on the scale of the number.
     */
    ADAPTIVE {
        public String toString() {
            return "Adaptive";
        }
    },
    /**
     * Uses Scientific display with 7 decimal places.
     */
    SCIENTIFIC_7 {
        public String toString() {
            return "Scientific: 7 decimal places";
        }
    },
    /**
     * Uses regular display with 5 decimal places.
     */
    STANDARD_5 {
        public String toString() {
            return "Standard: 5 decimal places";
        }
    },
    /**
     * Uses regular display with 2 decimal places. Decreases precision, but increases the maximum number that can be displayed.
     */
    STANDARD_2 {
        public String toString() {
            return "Standard: 2 decimal places";
        }
    }

}
