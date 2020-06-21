package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.VPos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

// TODO documentation

public class UserManualFXMLController implements Initializable {

    @FXML
    TextFlow manualTextFlow;

    @FXML
    TextFlow creditsTextFlow;

    public UserManualFXMLController() {
    }

    private Text generateHeadingText(String s) {
        Text text = new Text();
        text.setFont(Font.font("System", FontWeight.BOLD, 20));
        text.setTextOrigin(VPos.CENTER);
        text.setText(s);
        text.setLineSpacing(20);
        return text;
    }

    private Text generateBodyText(String s) {
        Text text = new Text();
        text.setFont(Font.font("System", FontWeight.NORMAL, 16));
        text.setText(s);
        text.setLineSpacing(10);
        return text;
    }

    private Text generateSpace() {
        Text text = new Text();
        text.setFont(Font.font("System", 4));
        text.setText("\n");
        return text;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Displays the text

        Text[] manualTexts = new Text[] {
                generateHeadingText("HOW TO RUN A SIMULATION:\n"),
                generateSpace(),
                generateBodyText("1) Enter your simulation variables into the boxes under each simulation 'Object'. \n" +
                        "2) Fill in the checkboxes under 'Global Settings' according to your simulation requirements. \n" +
                        "3) If you have selected the option to 'Run Infinitely', set a Simulation Speed Multiplier (the factor by which the simulation speed is multiplied). \n" +
                        "4) If you would like the particles to be displayed after a certain time, set the Timeskip (the time elapsed before graphics are displayed). \n" +
                        "5) Set a preferred number format to display the physical values of the particles (optional). \n" +
                        "6) Click 'Run Simulation'.\n\n"),
                generateHeadingText("HOW TO USE A SIMULATION TEMPLATE:\n"),
                generateSpace(),
                generateBodyText("1) At the top of the window, click on the 'Scales' tab. \n" +
                        "2) Select the template that you would like to use. \n" +
                        "3) Click the white checkmark button in the dropdown menu for the template. \n" +
                        "4) Click 'Yes' on the template warning message if you wish to proceed. \n" +
                        "5) Click 'Run Simulation'. \n\n"),
                generateHeadingText("HOW TO SAVE YOUR OWN TEMPLATE: \n"),
                generateSpace(),
                generateBodyText("1) After inputting all the necessary variables, write the name of your template under 'Template ID'. \n" +
                        "2) Click the save icon. \n\n"),
                generateHeadingText("HOW TO SAVE YOUR DATA IN A CSV FILE:\n"),
                generateSpace(),
                generateBodyText("1) Before running the simulation, check the 'Save CSV' box. \n" +
                        "2) Enter the name of the new CSV file. \n" +
                        "3) Click the document icon beside the name box. You will now be given the file directory in which the CSV file is stored. \n" +
                        "4) Click 'Run Simulation'.\n\n")
        };

        Text[] creditTexts = new Text[] {
                generateHeadingText("CREATORS\n"),
                generateSpace(),
                generateBodyText("Evan Zheng\n" +
                        "Vladislav Aleinikov\n\n"),
                generateHeadingText("LIBRARIES AND TOOLS USED\n"),
                generateSpace(),
                generateBodyText(" - JavaFX for the GUI framework \n" +
                        " - JBootX for Bootstrap-like visual styling \n" +
                        " - Apache Commons Math for integrations of ordinary differential equations \n" +
                        " - Apache Commons CSV for logging statistics and templates into CSV files \n" +
                        " - GraalVM for native-image compilation of Java programs \n" +
                        " - Gluon Client-Maven-Plugin for GraalVM compatibility with JavaFX \n" +
                        " - Ikonli for UI icons\n\n")
        };

        for (Text text : manualTexts) {
            manualTextFlow.getChildren().add(text);
        }
        for (Text text : creditTexts) {
            creditsTextFlow.getChildren().add(text);
        }

    }
}
