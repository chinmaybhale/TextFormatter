/**
 * This class is responsible for getting errors from the Sanitize class
 * and outputting them in a readable way onto the errors tab in GUI.
 * There are 3 main kinds of errors, and the specifics will be displayed
 * in the message of the error along with the line number
 *
 * @author Chinmay Bhale
 */

package sample;

import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;

public class Errors {

    private static TitledPane errorPane;
    private static TextArea errors;

    /**
     * This method is only called upon by the controller class.
     * This method is used to initialize the errors TextArea and the errorPane TitledPane
     * by passing the control from the Controller class.
     *
     * @param error_pane is the reference to the errorPane TitledPane in the GUI
     * @param error is the reference to the errors TextArea in the GUI
     */
    public static void setErrorArea(TitledPane error_pane, TextArea error) {
        errorPane = error_pane;
        errors = error;
    }

    /**
     * This class is called when the parsed command does not exist or
     * is not completely recognized by the TextFormatter. To call it type
     * Errors.UnrecognizedCommandError("unknown command", 10)
     *
     * @param message This is the more specific message about the encountered error
     * @param line This is the line number on which the error is encountered
     */
    public static void UnrecognizedCommandError(String message, int line) {
        String err = "Line " + line + ": Unrecognized Command, " + message;
        errors.setText(buildErr(err));

        errorPane.setExpanded(true);
    }

    /**
     * This class is called when the parsed command is invalid. This means that it
     * is in parts or in whole completely wrong. TO call this method type
     * Errors.InvalidCommandError("invalid command", 10)
     *
     * @param message This is the more specific message about the encountered error
     * @param line This is the line number on which the error is encountered
     */
    public static void InvalidCommandError(String message, int line) {
        String err = "Line " + line + ": Invalid Command, " + message;
        errors.setText(buildErr(err));

        errorPane.setExpanded(true);
    }

    /**
     * This class is called when the length of the title exceeds the line length.
     * To call this method type Errors.TitleLengthOverflowError("length overflow, 10)
     *
     * @param message This is the more specific message about the encountered error
     * @param line This is the line number on which the error is encountered
     */
    public static void TitleLengthOverflowError(String message, int line) {
        String err = "Line " + line + ": Title Length Overflow, " + message;
        errors.setText(buildErr(err));

        errorPane.setExpanded(true);
    }

    private static String buildErr(String err) {
        String errList = errors.getText();
        return errList + "\n" + err;
    }

}