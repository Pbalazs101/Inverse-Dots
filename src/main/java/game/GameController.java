package game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import org.tinylog.Logger;

import game.model.GameModel;
import game.model.PlayerDirection;
import game.model.Position;

public class GameController {

    /**
     * Enum class for defining selection phase
     */
    private enum SelectionPhase {
        SELECT_FROM,
        SELECT_TO;

        /**
         * Alternating between dot and destination selection.
         * @return current phase
         */
        public SelectionPhase alter() {
            return switch (this) {
                case SELECT_FROM -> SELECT_TO;
                case SELECT_TO -> SELECT_FROM;
            };
        }
    }

    private int numberOfSteps = 1;

    private SelectionPhase selectionPhase = SelectionPhase.SELECT_FROM;

    private List<Position> selectablePositions = new ArrayList<>();

    private Position selected;

    private GameModel model = new GameModel();

    private List<Score> scores2;

    @FXML
    private GridPane board;


    /**
     * Creating the model based on excercise 2.34.
     */
    @FXML
    private void initialize() {
        createBoard();
        createPlayerDot();
        createOpponentDot();
        setSelectablePositions();
        showSelectablePositions();
        wallBuilder();
    }

    /**
     * Setting up the board and creating squares.
     */
    private void createBoard() {
        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare();
                board.add(square, j, i);
            }
        }
    }

    /**
     * Creating squares as stackpane and adding event listener to square objects and initializing css classes.
     * @return new StackPane object.
     */
    private StackPane createSquare() {
        var square = new StackPane();
        square.getStyleClass().add("square");
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }

    /**
     * Creating PLAYER dot with attributes.
     */
    private void createPlayerDot() {
            model.positionProperty(0).addListener(this::dotPositionChange);
            var dot = createDot(Color.rgb(255,0,0));
            getSquare(model.getDotPosition(0)).getChildren().add(dot);

    }

    /**
     * Creating OPPONENT dot with attributes.
     */
    private void createOpponentDot() {
            model.positionProperty(1).addListener(this::dotPositionChange);
            var dot = createDot(Color.rgb(0,0,255));
            getSquare(model.getDotPosition(1)).getChildren().add(dot);
    }

    /**
     * Visually showing dot on board with set size.
     * @param color Color to fill.
     * @return Circle object.
     */
    private Circle createDot(Color color) {
        var dot = new Circle(25); // Size of dot
        dot.setFill(color);
        return dot;
    }

    /**
     * Driver code for mouse click event listener.
     * @param event .
     */
    @FXML
    private void handleMouseClick(MouseEvent event) {
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        var position = new Position(row, col);
        Logger.debug("Click on square {}", position);
        handleClickOnSquare(position);
    }

    /**
     * Handles clicks when position is about the get changed or we are about selecting dots.
     * @param position .
     */
    private void handleClickOnSquare(Position position) {
        switch (selectionPhase) {
            case SELECT_FROM -> {
                if (selectablePositions.contains(position)) {
                    selectPosition(position);
                    alterSelectionPhase();
                }
            }
            case SELECT_TO -> {
                if (selectablePositions.contains(position)) {
                    var direction = PlayerDirection.of(position.row() - selected.row(), position.col() - selected.col());
                    Logger.debug("Moving player dot {}",  direction);

                    var inverseDirectionRowChange = direction.getRowChange()*-1;
                    var inverseDirectionColChange = direction.getColChange()*-1;

                    var inverseDirection = PlayerDirection.of(inverseDirectionRowChange,inverseDirectionColChange);

                    model.move(0, direction);
                    model.move(1, inverseDirection);

                    deselectSelectedPosition();
                    alterSelectionPhase();
                    numberOfSteps += 1;

                }
            }
        }
    }

    /**
     * Function for alternating between the two possible selection phases and taking care of visual properties.
     */
    private void alterSelectionPhase() {
        selectionPhase = selectionPhase.alter();
        hideSelectablePositions();
        setSelectablePositions();
        showSelectablePositions();
    }

    /**
     * Recieves a position and sets it as the selected one.
     * @param position .
     */
    private void selectPosition(Position position) {
        selected = position;
        showSelectedPosition();
    }

    /**
     * Creating css classes for selected positions.
     */
    private void showSelectedPosition() {
        var square = getSquare(selected);
        square.getStyleClass().add("selected");
    }

    /**
     *Sets "selected" to null value.
     */
    private void deselectSelectedPosition() {
        hideSelectedPosition();
        selected = null;
    }

    /**
     * Removes css class in order to hide no longer selected positions.
     */
    private void hideSelectedPosition() {
        var square = getSquare(selected);
        square.getStyleClass().remove("selected");
    }

    /**
     * Sets the selectable positions for choosing dot or moving a selected dot.
     */
    private void setSelectablePositions() {
        selectablePositions.clear();
        switch (selectionPhase) {
            case SELECT_FROM -> selectablePositions.add(model.getDotPosition(0));
            case SELECT_TO -> {
                for (var direction : model.getValidMoves(0)) {
                    selectablePositions.add(selected.moveTo(direction));
                    Logger.info("Selectable moves:"+selectablePositions);
                    System.out.println(selectablePositions.get(0));
                    System.out.println();
                    System.out.println(direction.name());
                }
            }
        }
    }

    /**
     * Creating css classes for selectable positions.
     */
    private void showSelectablePositions() {
        for (var selectablePosition : selectablePositions) {
            var square = getSquare(selectablePosition);
            square.getStyleClass().add("selectable");
        }
    }

    /**
     * Removes css class in order to hide no longer selectable positions.
     */
    private void hideSelectablePositions() {
        for (var selectablePosition : selectablePositions) {
            var square = getSquare(selectablePosition);
            square.getStyleClass().remove("selectable");
        }
    }

    /**
     * Returns the square on the given position if a dot is present on it.
     * @param position .
     * @return Square object
     */
    private StackPane getSquare(Position position) {
        for (var child : board.getChildren()) {
            if (GridPane.getRowIndex(child) == position.row() && GridPane.getColumnIndex(child) == position.col()) {
                return (StackPane) child;
            }
        }
        throw new AssertionError();
    }

    /**
     * Removes children (dot) from and old position object after assigning them to a new one.
     * @param observable
     * @param oldPosition
     * @param newPosition
     */
    private void dotPositionChange(ObservableValue<? extends Position> observable, Position oldPosition, Position newPosition) {
        Logger.debug("Move: {} -> {}", oldPosition, newPosition);
        StackPane oldSquare = getSquare(oldPosition);
        StackPane newSquare = getSquare(newPosition);
        newSquare.getChildren().addAll(oldSquare.getChildren());
        oldSquare.getChildren().clear();
        endState();
    }

    /**
     * Logging when the game has ended.
     */
    private void endState() {
        List<Score> scores = new ArrayList<Score>();
        var playerName = "";
        if (InitialViewController.name != null) {
            playerName = InitialViewController.getName();
        } else {
            playerName = "Unnamed Player";
        }


        if(model.getDotPosition(0).equals(model.getDotPosition(1))) {
            try {
                scores2 = new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .readValue(LeaderboardViewController.class.getResourceAsStream("/scores.json"), new TypeReference<List<Score>>() {});
            } catch (Exception e) {
                System.out.println("Exception");
            }

            Logger.info("Congratulations, you won!");
            Logger.info("Name:"+playerName);
            Logger.info("Number of steps made: "+numberOfSteps);
            ObjectMapper objectMapper = new ObjectMapper();
            scores.add(new Score(playerName,String.valueOf(numberOfSteps)));
            try {
                objectMapper.writeValue(new File("target/scores2.json"), scores);

            } catch (IOException e) {
                e.printStackTrace();
            }



        }
    }


    /**
     * Calls each method to display walls according to figure number 39.
     */
    private void wallBuilder() {
        showWall13();
        showWall22();
        showWall31();
        showWall34();
        showWall43();
        showWall53();
        showWall56();
        showWall60();
        showWall63();
    }

    /**
     * Creates css class for every square that contains walls.
     */
    private void showWall13() {
            var square = getSquare(new Position(1,3));
            square.getStyleClass().add("wall13");
    }

    private void showWall22() {
        var square = getSquare(new Position(2,2));
        square.getStyleClass().add("wall22");
    }

    private void showWall31() {
        var square = getSquare(new Position(3,1));
        square.getStyleClass().add("wall31");
    }

    private void showWall34() {
        var square = getSquare(new Position(3,4));
        square.getStyleClass().add("wall34");
    }

    private void showWall43() {
        var square = getSquare(new Position(4,3));
        square.getStyleClass().add("wall43");
    }

    private void showWall53() {
        var square = getSquare(new Position(5,3));
        square.getStyleClass().add("wall53");
    }

    private void showWall56() {
        var square = getSquare(new Position(5,6));
        square.getStyleClass().add("wall56");
    }

    private void showWall60() {
        var square = getSquare(new Position(6,0));
        square.getStyleClass().add("wall60");
    }

    private void showWall63() {
        var square = getSquare(new Position(6,3));
        square.getStyleClass().add("wall63");
    }

}