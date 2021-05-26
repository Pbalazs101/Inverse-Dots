import game.model.GameModel;
import game.model.PlayerDirection;
import game.model.Position;
import org.junit.jupiter.api.Test;

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

    @Test
    void testIsValidMove() {
        assertTrue(gameModel.isOnBoard(new Position(0,0)));
        assertTrue(gameModel.isOnBoard(new Position(6,6)));
        assertFalse(gameModel.isOnBoard(new Position(-1,1)));
        assertFalse(gameModel.isOnBoard(new Position(-2,-5)));
        assertFalse(gameModel.isOnBoard(new Position(7,7)));
        assertFalse(gameModel.isOnBoard(new Position(6,7)));
    }

}
