package game.model;

/**
 * Representing the four directions in which the player can move.
 */
public enum PlayerDirection implements Direction {

    UP(-1, 0),
    RIGHT(0, 1),
    DOWN(1, 0),
    LEFT(0, -1);

    private final int rowChange;
    private final int colChange;

    PlayerDirection(int rowChange, int colChange) {
        this.rowChange = rowChange;
        this.colChange = colChange;
    }

    @Override
    public int getRowChange() {
        return rowChange;
    }

    @Override
    public int getColChange() {
        return colChange;
    }

    /**
     *
     * @param rowChange .
     * @param colChange .
     * @return Direction object (The defined name of direction by recieving col and row change).
     */
    public static PlayerDirection of(int rowChange, int colChange) {
        for (var direction : values()) {
            if (direction.rowChange == rowChange && direction.colChange == colChange) {
                return direction;
            }
        }
        throw new IllegalArgumentException();
    }

    public static void main(String[] args) {
        System.out.println(of(1, -1));
    }

}