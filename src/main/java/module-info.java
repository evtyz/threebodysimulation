module threebodysimulation {
    requires javafx.controls;
    requires javafx.fxml;
    requires commons.math3;

    opens stl.threebodysimulation to javafx.fxml;

    exports stl.threebodysimulation;
}