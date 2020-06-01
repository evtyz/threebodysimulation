package stl.threebodysimulation;

// This represents a Listener object that allows an instance to communicate with its owner.
interface Listener {
    // Method that is called when an instance of a class does something notable.
    void onEvent();
}
