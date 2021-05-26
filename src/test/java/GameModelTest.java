import game.model.*;
import javafx.geometry.Pos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameModelTest {

    Position position;
    GameModel gameModel;

    void assertPosition(int expectedRow, int expectedCol, Position position) {
        assertAll("position",
                () -> assertEquals(expectedRow, position.row()),
                () -> assertEquals(expectedCol, position.col())
        );
    }

    @BeforeEach
    void init() {
        position = new Position(0, 0);
    }

    @BeforeEach
    void initG() {
        gameModel = new GameModel(new Dot(DotType.RED, new Position(0, 4)),
                new Dot(DotType.BLUE, new Position(6, 2)));
    }

    @Test
    void isOnBoard() {
        assertTrue(gameModel.isOnBoard(new Position(0,0)));
        assertTrue(gameModel.isOnBoard(new Position(6,6)));
        assertFalse(gameModel.isOnBoard(new Position(-1,1)));
        assertFalse(gameModel.isOnBoard(new Position(-2,-5)));
        assertFalse(gameModel.isOnBoard(new Position(7,7)));
        assertFalse(gameModel.isOnBoard(new Position(6,7)));
    }

    @Test
    void getDotPosition() {
        assertPosition(0,4,gameModel.getDotPosition(0));
    }

    @Test
    void positionProperty() {
    }

    @Test
    void getValidMoves() {
        assertTrue(gameModel.getValidMoves().contains(PlayerDirection.DOWN));
        assertTrue(gameModel.getValidMoves().contains(PlayerDirection.RIGHT));
        assertFalse(gameModel.getValidMoves().contains(PlayerDirection.UP));
        assertTrue(gameModel.getValidMoves().contains(PlayerDirection.LEFT));

    }

    @Test
    void move() {
        gameModel.move(PlayerDirection.DOWN);
        assertPosition(1,4, gameModel.getDotPosition(0));
        assertPosition(6,2, gameModel.getDotPosition(1));
    }

    @Test
    void moveBlue() {
        gameModel.moveBlue(PlayerDirection.UP);
        assertPosition(5,2, gameModel.getDotPosition(1));
        assertPosition(0,4, gameModel.getDotPosition(0));
        gameModel.moveBlue(PlayerDirection.RIGHT);
        assertPosition(5,3, gameModel.getDotPosition(1));
        assertPosition(0,4, gameModel.getDotPosition(0));
        gameModel.moveBlue(PlayerDirection.LEFT);
        assertPosition(5,2, gameModel.getDotPosition(1));
        assertPosition(0,4, gameModel.getDotPosition(0));
        gameModel.moveBlue(PlayerDirection.DOWN);
        assertPosition(6,2, gameModel.getDotPosition(1));
        assertPosition(0,4, gameModel.getDotPosition(0));
    }

    @Test
    void isValidMove() {
        assertFalse(gameModel.isValidMove(PlayerDirection.UP));
        assertTrue(gameModel.isValidMove(PlayerDirection.DOWN));
        assertTrue(gameModel.isValidMove(PlayerDirection.RIGHT));
        assertTrue(gameModel.isValidMove(PlayerDirection.LEFT));
    }

    @Test
    void getDotPositions() {
    }

    @Test
    void testToString() {
        assertEquals("[REDPosition[row=0, col=4],BLUEPosition[row=6, col=2]]", gameModel.toString());
    }

    @Test
    void main() {
    }
}
