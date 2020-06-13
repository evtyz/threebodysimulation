package stl.threebodysimulation;

/**
 * Represents all states that a simulation can be in.
 */
enum SimulationState {

    /**
     * A simulation is not running. A new simulation may begin.
     */
    INACTIVE,

    /**
     * A simulation is currently running. No new simulation may begin.
     */
    ACTIVE,

    /**
     * A simulation is paused. No new simulation may begin, but the current simulation may resume.
     */
    PAUSED
}
