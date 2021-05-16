package game;

import java.util.ArrayList;
import java.util.List;

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

    private enum SelectionPhase {
        SELECT_FROM,
        SELECT_TO;

        public SelectionPhase alter() {
            return switch (this) {
                case SELECT_FROM -> SELECT_TO;
                case SELECT_TO -> SELECT_FROM;
            };
        }
    }

    private SelectionPhase selectionPhase = SelectionPhase.SELECT_FROM;

    private List<Position> selectablePositions = new ArrayList<>();

    private Position selected;

    private GameModel model = new GameModel();

    @FXML
    private GridPane board;

    @FXML
    private void initialize() {
        createBoard();
        createPlayerDot();
        createOpponentDot();
        setSelectablePositions();
        showSelectablePositions();
        wallBuilder();
    }

    private void createBoard() {
        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare();
                board.add(square, j, i);
            }
        }
    }

    private StackPane createSquare() {
        var square = new StackPane();
        square.getStyleClass().add("square");
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }


    private void createPlayerDot() {
            model.positionProperty(0).addListener(this::dotPositionChange);
            var dot = createDot(Color.rgb(255,0,0));
            getSquare(model.getDotPosition(0)).getChildren().add(dot);

    }

    private void createOpponentDot() {
            model.positionProperty(1).addListener(this::dotPositionChange);
            var dot = createDot(Color.rgb(0,0,255));
            getSquare(model.getDotPosition(1)).getChildren().add(dot);
    }

    private Circle createDot(Color color) {
        var dot = new Circle(25); // Size of dot
        dot.setFill(color);
        return dot;
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        var position = new Position(row, col);
        Logger.debug("Click on square {}", position);
        handleClickOnSquare(position);
    }

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
                }
            }
        }
    }

    private void alterSelectionPhase() {
        selectionPhase = selectionPhase.alter();
        hideSelectablePositions();
        setSelectablePositions();
        showSelectablePositions();
    }

    private void selectPosition(Position position) {
        selected = position;
        showSelectedPosition();
    }

    private void showSelectedPosition() {
        var square = getSquare(selected);
        square.getStyleClass().add("selected");
    }

    private void deselectSelectedPosition() {
        hideSelectedPosition();
        selected = null;
    }

    private void hideSelectedPosition() {
        var square = getSquare(selected);
        square.getStyleClass().remove("selected");
    }

    private void setSelectablePositions() {
        selectablePositions.clear();
        switch (selectionPhase) {
            case SELECT_FROM -> selectablePositions.addAll(model.getDotPositions());
            case SELECT_TO -> {
                var dotNumber = model.getDotNumber(selected).getAsInt();
                for (var direction : model.getValidMoves(dotNumber)) {
                    selectablePositions.add(selected.moveTo(direction));
                }
            }
        }
    }

    private void showSelectablePositions() {
        for (var selectablePosition : selectablePositions) {
            var square = getSquare(selectablePosition);
            square.getStyleClass().add("selectable");
        }
    }

    private void hideSelectablePositions() {
        for (var selectablePosition : selectablePositions) {
            var square = getSquare(selectablePosition);
            square.getStyleClass().remove("selectable");
        }
    }

    private StackPane getSquare(Position position) {
        for (var child : board.getChildren()) {
            if (GridPane.getRowIndex(child) == position.row() && GridPane.getColumnIndex(child) == position.col()) {
                return (StackPane) child;
            }
        }
        throw new AssertionError();
    }

    private void dotPositionChange(ObservableValue<? extends Position> observable, Position oldPosition, Position newPosition) {
        Logger.debug("Move: {} -> {}", oldPosition, newPosition);
        StackPane oldSquare = getSquare(oldPosition);
        StackPane newSquare = getSquare(newPosition);
        newSquare.getChildren().addAll(oldSquare.getChildren());
        oldSquare.getChildren().clear();
        if(model.getDotPosition(0).equals(model.getDotPosition(1))) {
            Logger.info("Congratulations, you won!");
        }
    }


    private void wallBuilder() {
        showWall13(); //OK
        showWall22();
        showWall31();
        showWall34(); //OK
        showWall43();
        showWall53(); //OK
        showWall56(); //OK
        showWall60();
        showWall63(); //OK
    }
    

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