package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Controller {

    public Button open = new Button();
    public Button save = new Button();
    public Button convert = new Button();

    public TextArea input = new TextArea();
    public TextArea errors = new TextArea();
    public TextArea output = new TextArea();

    private FileChooser chooser = new FileChooser();

    @FXML
    public void openFile(Event e) {
        String inputData = "";
        File file = chooser.showOpenDialog(open.getScene().getWindow());

        try {
            inputData = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException error) {
            //TODO: error case, file not found!
        }

        input.setText(inputData);
    }

    @FXML
    public void saveFile(Event e) {
        // TODO: save file as output.txt from output tab
        String outputData = output.getText();

    }

    @FXML
    public void convertFile(Event e) {
        // TODO: take content from input pane and send to conversion
    }
}
