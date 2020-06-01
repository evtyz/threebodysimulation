package stl.threebodysimulation;

/**
 * This represents a Listener object that allows an instance to communicate with its owner.
 */
interface Listener {
    /**
     * A method that is called when an instance does something notable.
     */
    void onEvent();
}
