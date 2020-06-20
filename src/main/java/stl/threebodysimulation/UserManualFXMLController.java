package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // The user manual text.
        String body =
                " \n" +
                        " HOW TO RUN A SIMULATION: \n\n" +
                        " 1) Enter your simulation variables into the boxes under each simulation 'Object'. \n" +
                        " 2) Fill in the checkboxes under 'Global Settings' according to your simulation requirements. \n" +
                        " 3) If you have selected the option to 'Run Infinitely', set a Simulation Speed Multiplier (the factor by which the simulation speed is multiplied). \n" +
                        " 4) If you would like the particles to be displayed after a certain point in the simulation, set the Timeskip (the time elapsed before graphics are displayed). \n" +
                        " 5) Set a preferred number format to display the physical values of the particles (optional). \n" +
                        " 6) Click 'Run Simulation'. \n\n" +
                        " HOW TO USE A SIMULATION TEMPLATE: \n\n" +
                        " 1) At the top of the window, click on the 'Scales' tab. \n" +
                        " 2) Select the template that you would like to use. \n" +
                        " 3) Click the white checkmark button in the dropdown menu for the template. \n" +
                        " 4) Click 'Yes' on the template warning message if you wish to proceed. \n" +
                        " 5) Click 'Run Simulation'. \n\n" +
                        " HOW TO SAVE YOUR OWN TEMPLATE: \n\n" +
                        " 1) After inputting all the necessary variables, write the name of your template under 'Template ID'. \n" +
                        " 2) Click the save icon. \n\n" +
                        " HOW TO SAVE YOUR DATA IN A CSV FILE: \n\n" +
                        " 1) Before running the simulation, check the 'Save CSV' box. \n" +
                        " 2) Enter the name of the new CSV file. \n" +
                        " 3) Click the document icon beside the name box. You will now be given the file directory in which the CSV file is stored. \n" +
                        " 4) Click 'Run Simulation'.";

        String credits =
                " \n" +
                        " CREATORS \n\n" +
                        " Evan Zheng \n" +
                        " Vladislav Aleinikov \n\n" +
                        " LIBRARIES AND TOOLS USED \n\n" +
                        " - JavaFX for the GUI framework \n" +
                        " - JBootX for Bootstrap-like visual styling \n" +
                        " - Apache Commons Math for integrations of ordinary differential equations \n" +
                        " - Apache Commons CSV for logging statistics and templates into CSV files \n" +
                        " - GraalVM for native-image compilation of Java programs \n" +
                        " - Gluon Client-Maven-Plugin for GraalVM compatibility with JavaFX \n" +
                        " - Ikonli for UI icons ";

        // Sets the text objects
        Text manualText = new Text();
        Text creditsText = new Text();

        // Sets the font and line spacing
        manualText.setFont(Font.font("Verdana", 15));
        creditsText.setFont(Font.font("Verdana", 15));
        manualText.setLineSpacing(10);

        // Sets the text to be displayed
        manualText.setText(body);
        creditsText.setText(credits);

        // Displays the text
        manualTextFlow.getChildren().add(manualText);
        creditsTextFlow.getChildren().add(creditsText);


    }
}
