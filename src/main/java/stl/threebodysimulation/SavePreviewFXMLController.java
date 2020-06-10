package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SavePreviewFXMLController {

    // TODO Everything

    private SimulationSettings settings;

    private Listener clickListener;

    @FXML
    private Label saveIDLabel;

    @FXML
    private Label infiniteLabel;

    @FXML
    private Label timeskipLabel;

    @FXML
    private Label speedLabel;

    @FXML
    private Label massLabel1;

    @FXML
    private Label massLabel2;

    @FXML
    private Label massLabel3;

    public SavePreviewFXMLController() {}

    void setTitle(String title) {
        saveIDLabel.setText(title);
    }

    void setSettings(SimulationSettings settings) {
        this.settings = settings;
        if (settings.getInfinite()) {
            infiniteLabel.setText("Yes");
        } else {
            infiniteLabel.setText("No");
        }

        timeskipLabel.setText(String.format("%.2f", settings.getSkip()));
        speedLabel.setText(String.format("%.2f", settings.getSpeed()));
        Particle[] particles = settings.getParticles();
        massLabel1.setText(String.format("%.2f", particles[0].getMass()));
        massLabel2.setText(String.format("%.2f", particles[1].getMass()));
        massLabel3.setText(String.format("%.2f", particles[2].getMass()));
    }

    void setClickListener(Listener listener) {
        clickListener = listener;
    }

    SimulationSettings getSettings() {
        return settings;
    }
}
