
import game.model.PlayerDirection;
import game.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {
    Position position;


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

    @Test
    void getTarget() {
        assertPosition(-1, 0, position.moveTo(PlayerDirection.UP));
        assertPosition(0, 1, position.moveTo(PlayerDirection.RIGHT));
        assertPosition(1, 0, position.moveTo(PlayerDirection.DOWN));
        assertPosition(0, -1, position.moveTo(PlayerDirection.LEFT));
    }

    @Test
    void getUp() {
        assertPosition(-1, 0, position.moveTo(PlayerDirection.UP));
    }

    @Test
    void getRight() {
        assertPosition(0, 1, position.moveTo(PlayerDirection.RIGHT));
    }

    @Test
    void getDown() {
        assertPosition(1, 0, position.moveTo(PlayerDirection.DOWN));
    }

    @Test
    void getLeft() {
        assertPosition(0, -1, position.moveTo(PlayerDirection.LEFT));
    }



    @Test
    void testEquals() {
        assertTrue(position.equals(position));
        assertTrue(position.equals(new Position(position.row(), position.col())));
        assertFalse(position.equals(new Position(Integer.MIN_VALUE, position.col())));
        assertFalse(position.equals(new Position(position.row(), Integer.MAX_VALUE)));
        assertFalse(position.equals(new Position(Integer.MIN_VALUE, Integer.MAX_VALUE)));
        assertFalse(position.equals(null));
        assertFalse(position.equals("Some string"));
    }

    @Test
    void testHashCode() {
        assertTrue(position.hashCode() == position.hashCode());
        assertTrue(position.hashCode() == new Position(position.row(), position.col()).hashCode());
    }


    @Test
    void testToString() {
        assertEquals("Position[row=0, col=0]", position.toString());
    }
}
