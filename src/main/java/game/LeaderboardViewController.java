package game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.stage.Stage;


public class LeaderboardViewController {
    @FXML
    private TableView tableView;

    @FXML
    private TableColumn<Score, String> name;

    @FXML
    private TableColumn<Score, String> steps;

    @FXML
    private void initialize() throws IOException {
        File save = new File(System.getProperty("user.dir"),"scores.json");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        steps.setCellValueFactory(new PropertyValueFactory<>("steps"));
        if (save.isFile()) {
            List<Score> scores = new ObjectMapper()
                    .readValue(save, new TypeReference<List<Score>>() {
                    });
            if (scores == null) {
                scores = new ArrayList<>();
            }
            ObservableList<Score> observableList = FXCollections.observableArrayList();
            observableList.addAll(scores);
            tableView.setItems(observableList);
        }
    }

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("/Initial.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}

