package game;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

    public class InitialViewController extends Application {

        public static String getName() {
            return name;
        }

        public static String name;

        @FXML
        private TextField nameTextField;

        @Override
        public void start(Stage stage) throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource("/Initial.fxml"));
            stage.setTitle("Inverse Dots");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }

        @FXML
        private void handlePlayButton(ActionEvent event) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("/ui.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }

        @FXML
        private void handleLeaderboardButton(ActionEvent event) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("/Leaderboard.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }

        @FXML
        public void handleTextFieldEditingDidEnd(ActionEvent event) {
            name = nameTextField.getText();

        }
    }

