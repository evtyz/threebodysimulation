module threebodysimulation {
    requires javafx.controls;
    requires javafx.fxml;
    requires commons.math3;
    requires commons.csv;

    opens stl.threebodysimulation to javafx.fxml;

    exports stl.threebodysimulation;
}