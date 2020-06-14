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
    }; // TODO: more.

    /**
     * Gets the settings of the default template.
     *
     * @return The settings.
     */
    abstract SimulationSettings getSettings();

    /**
     * Gets the tilte of the default template.
     *
     * @return The title.
     */
    abstract String getTitle();
}
