package stl.threebodysimulation;

import javafx.scene.paint.Color;

/**
 * A list of default templates included with the app.
 */
enum DefaultTemplates {
    /**
     * An example of an asymmetrical orbit.
     */
    ASYMMETRICAL_ORBIT {
        @Override
        SimulationSettings getSettings() {
            return new SimulationSettings(
                    new Particle[]{
                            new Particle(
                                    100, 100, 30, 0, 1, 1, Color.RED
                            ),
                            new Particle(
                                    -100, -100, -30, 0, 1, 2, Color.GREEN
                            ),
                            new Particle(
                                    0, 0, 0, 0, 1, 3, Color.BLUE
                            )
                    },
                    true,
                    true,
                    false,
                    0,
                    1,
                    NumberFormat.ADAPTIVE,
                    ""
            );
        }

        @Override
        String getTitle() {
            return "Asymmetrical Orbit (Example)";
        }
    },
    /**
     * An example of a symmetrical orbit.
     */
    SYMMETRICAL_ORBIT {
        @Override
        SimulationSettings getSettings() {
            return new SimulationSettings(
                    new Particle[]{
                            new Particle(
                                    100, 0, 0, 71, 1, 1, Color.RED
                            ),
                            new Particle(
                                    -100, 0, 0, -71, 1, 2, Color.GREEN
                            ),
                            new Particle(
                                    0, 0, 0, 0, 1, 3, Color.BLUE
                            )
                    },
                    true,
                    true,
                    false,
                    0,
                    1,
                    NumberFormat.ADAPTIVE,
                    ""
            );
        }

        @Override
        String getTitle() {
            return "Symmetrical Orbit (Example)";
        }
    },
    /**
     * An example of a chaotic orbit.
     */
    CHAOTIC_ORBIT {
        SimulationSettings getSettings() {
            return new SimulationSettings(
                    new Particle[]{
                            new Particle(
                                    150, 50, -15, -30, 3, 1, Color.RED
                            ),
                            new Particle(
                                    50, -150, 5, 40, 3, 2, Color.GREEN
                            ),
                            new Particle(
                                    -200, -50, 10, -10, 3, 3, Color.BLUE
                            )
                    },
                    true,
                    true,
                    true,
                    0,
                    1,
                    NumberFormat.ADAPTIVE,
                    ""
            );
        }
        String getTitle() {
            return "Chaotic Orbit (Example)";
        }
    };

    /**
     * Gets the settings of the default template.
     *
     * @return The settings.
     */
    abstract SimulationSettings getSettings();

    /**
     * Gets the title of the default template.
     *
     * @return The title.
     */
    abstract String getTitle();
}
