package stl.threebodysimulation;

import javafx.fxml.FXML;

// Controller for particle info panel on top right
public class InfoPanelFXMLController {

    // UI element declarations (controllers represent each individual stat panel for objects)
    @FXML
    private InfoFXMLController object1InfoController;

    @FXML
    private InfoFXMLController object2InfoController;

    @FXML
    private InfoFXMLController object3InfoController;

    private InfoFXMLController[] infoControllers;

    private Particle[] particles;

    public static NumberFormat chosenFormat;

    // Default constructor for FXML
    public InfoPanelFXMLController() {
    }

    // Setup method called by scenecontroller
    public void setup() {
        // initialize array
        infoControllers = new InfoFXMLController[]{object1InfoController, object2InfoController, object3InfoController};
        // setup each individual controller with correct ids
        for (int id = 0; id < 3; id++) {
            infoControllers[id].setup(id + 1);
            infoControllers[id].updateFromColor(SceneFXMLController.defaultColors[id]);
        }
    }

    // Sets up the particles that the panel reads
    public void setParticles(Particle[] particles) {
        // INPUTS:
        // particles: Particle[3], the particles whose stats will be shown.

        this.particles = particles;

        // Set up a listener inside each particle
        for (int i = 0; i < 3; i++) {
            setParticleListener(i);
            infoControllers[i].updateFromColor(particles[i].color);
        }
    }

    // Sets up the particle listener for a single particle
    public void setParticleListener(int index) {
        particles[index].infoUpdateListener = new Listener() {
            @Override
            public void onEvent() {
                infoControllers[index].updateFromParticle(particles[index]);
            }
        };
    }

    // Sets the number format of the info panel.
    public void setNumberFormat(NumberFormat format) {
        chosenFormat = format;
    }
}
