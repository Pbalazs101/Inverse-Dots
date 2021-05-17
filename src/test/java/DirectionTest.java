
import game.model.PlayerDirection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DirectionTest {
    @Test
    void of() {
        assertSame(PlayerDirection.UP, PlayerDirection.of(-1, 0));
        assertSame(PlayerDirection.DOWN, PlayerDirection.of(1, 0));
        assertSame(PlayerDirection.LEFT, PlayerDirection.of(0, -1));
        assertSame(PlayerDirection.RIGHT, PlayerDirection.of(0, 1));
    }

    @Test
    void of_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> PlayerDirection.of(0, 0));
    }

}
