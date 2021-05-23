package game;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


public class LeaderboardViewController {
    @FXML
    private TableView tableView;

    @FXML
    private TableColumn<Score, String> name;

    @FXML
    private TableColumn<Score, String> steps;

    @FXML
    private void initialize() throws IOException {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        steps.setCellValueFactory(new PropertyValueFactory<>("steps"));
        List<Score> scores = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .readValue(LeaderboardViewController.class.getResourceAsStream("/scores.json"), new TypeReference<List<Score>>() {});
        ObservableList<Score> observableList = FXCollections.observableArrayList();
        observableList.addAll(scores);
        tableView.setItems(observableList);
    }

}

