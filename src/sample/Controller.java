/**
 * This is the Controller class for the GUI for the TextFormatter.
 * All actions to the backend are called from here
 *
 *
 * @author Chinmay Bhale
 */
package sample;

import com.sun.glass.ui.Pixels;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class Controller {

    public Button open = new Button();
    public Button save = new Button();
    public Button convert = new Button();

    public TabPane tabPane = new TabPane();

    public TextArea input = new TextArea();
    public TextArea errors = new TextArea();
    public TextArea output = new TextArea();

    public TitledPane errorPane = new TitledPane();

    private FileChooser chooser = new FileChooser();

    /**
     * This method is called upon when the open button is clicked
     * A file chooser opens up a dialog box for the user to browse the directories
     * for the file. The file is returned when the user clicks open in the dialog box.
     * The data is read as a String, and printed onto the input text area.
     *
     * @param e
     */
    @FXML
    public void openFile(Event e) {
        String inputData = "";
        File file = chooser.showOpenDialog(open.getScene().getWindow());

        try {
            inputData = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException error) {
            //TODO: error case, file not found!
        }

        input.setFont(Font.font("monospaced"));
        input.setText(inputData);

        Stage primary = (Stage) open.getScene().getWindow();
        primary.setTitle(file.getName());
    }

    /**
     * This method is called upon when the save button is clicked
     * A file chooser dialog box opens up and allows the user to browse the computer
     * to find a location to save the file. The user enters the name of the file, and saves the file.
     * The contents of the output text area are put into the file using a helper method
     *
     * @link createFile
     * @param e
     */
    @FXML
    public void saveFile(Event e) {
        String outputData = output.getText();

        File file = chooser.showSaveDialog(save.getScene().getWindow());

        if(file != null) {
            createFile(outputData, file);
        }
    }

    /**
     * This method is called upon when the convert button is clicked.
     * The contents of the input text area are taken and passed to the backend
     * for processing
     *
     * @param e
     */
    @FXML
    public void convertFile(Event e) {
        // TODO: take content from input pane and send to conversion
        errors.setText("");
        Errors.setErrorArea(errorPane, errors);

        String string = input.getText();
        Sanitize sanitizer = new Sanitize();
        Formatter formatter = new Formatter();

        string = sanitizer.sanitize(string);
        string = formatter.formatWrap(string);
        string = Formatter.lineLength(string);
        string = formatter.justification(string);
        string = formatter.indentation(string);
        string = formatter.doubleSpaces(string);
        string = formatter.formatTitle(string);
        string = Formatter.blankSpaces(string);
        string = Formatter.formatColumns(string);

        output.setFont(Font.font("monospaced"));
        output.setText(string);

        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        selectionModel.select(1);
    }

    /**
     * This is a helper method for saveFile(). It is used to create a file with the data in the
     * output text area
     *
     * @param outputData
     * @param file
     */
    private void createFile(String outputData, File file) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(outputData);
            writer.close();
        } catch (IOException e) {
            //TODO: deal with this error
        }
    }
}
