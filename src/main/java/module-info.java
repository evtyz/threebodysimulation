/**
 * An app built in JavaFX to graphically simulate the Three-Body Problem in classical mechanics.
 */
module threebodysimulation {
    requires javafx.controls;
    requires javafx.fxml;
    requires commons.math3;
    requires commons.csv;
    requires java.desktop;
    requires de.jensd.fx.glyphs.fontawesome;

    opens stl.threebodysimulation to javafx.fxml;

    exports stl.threebodysimulation;
}