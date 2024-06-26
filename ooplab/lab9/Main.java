import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @FXML
    private TextField destinationInput;
    @FXML
    private TextField levelsInput;
    private static Director director;
    public Stage primaryStage = new Stage();

    public Stage getStage(){return primaryStage;}

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Middle-Earth Adventure");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Start.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    public void startGame() throws IOException {
        String destination = destinationInput.getText();
        String levels = levelsInput.getText();
        if (isValidInit(destination, levels)) {
            int levelNumber = Integer.parseInt(levels);
            director = new Director(levelNumber, destination);
            director.Gaming(primaryStage);
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("alert.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    @FXML
    public void Exit() {
        primaryStage.close();
        System.exit(1);
    }

    private boolean isValidInit(String destination, String levels) {
        try {
            int num = Integer.parseInt(levels);
            if (num <= 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        boolean isValid = (destination.length() >= 4 && destination.length() <= 16 && destination.matches("[a-zA-Z ]+")
                && !destination.startsWith(" ") && !destination.endsWith(" "));
        return isValid;
    }
}