package sample;

import sample.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Controller controller = new Controller();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(controller);

        Parent root = FXMLLoader.load(getClass().getResource("index.fxml"));
        primaryStage.setTitle("Text Formatter");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
