package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;

import java.io.IOException;

/**
 * This controller manages the entire info panel of info displays in the top right of the UI.
 */
public class InfoPanelFXMLController {

    /**
     * The chosen number format to display numbers with.
     */
    private static NumberFormat chosenFormat;

    @FXML
    private HBox panelBox;

    /**
     * An array that stores all info-display UI elements.
     */
    private InfoFXMLController[] infoControllers;

    /**
     * The particles that each info display takes properties from.
     */
    private Particle[] particles;

    /**
     * Constructor used by FXML loader.
     */
    public InfoPanelFXMLController() {
    }

    /**
     * @return The number format that displays should use.
     */
    static NumberFormat getNumberFormat() {
        return chosenFormat;
    }

    /**
     * Sets the info display NumberFormat to the given format.
     * @param format The NumberFormat chosen by the user.
     */
    static void setNumberFormat(NumberFormat format) {
        chosenFormat = format;
    }

    /**
     * The setup method that is called by the FXML loader.
     */
    void setup() {
        try {
            // initialize array
            infoControllers = new InfoFXMLController[3];
            // setup each individual controller with correct ids and colors.
            for (int id = 0; id < 3; id++) {
                FXMLLoader infoLoader = new FXMLLoader(getClass().getResource("/stl/threebodysimulation/particleInfoLayout.fxml"));
                Parent infoDisplay = infoLoader.load();
                infoControllers[id] = infoLoader.getController();
                panelBox.getChildren().add(2 * id, infoDisplay);
                infoControllers[id].setup(id + 1);
                infoControllers[id].updateFromColor(SceneFXMLController.getDefaultColors()[id]);
            }
        } catch (IOException ignored) {
            // Should never happen.
        }
    }

    /**
     * Sets the particle array whose information will be displayed.
     * @param particles The array whose information will be displayed.
     */
    void setParticles(Particle[] particles) {
        this.particles = particles;

        // Set up a listener inside each particle
        for (int i = 0; i < 3; i++) {
            setParticleListener(i);
            infoControllers[i].updateFromColor(particles[i].getColor());
        }
    }

    /**
     * Sets up a listener for property changes inside each particle, and links the listener to the correct info display.
     * @param index The id of both the particle and the info display. (object 1 has display 1, etc)
     */
    private void setParticleListener(int index) {
        particles[index].setInfoUpdateListener(() -> infoControllers[index].updateFromParticle(particles[index]));
    }
}
