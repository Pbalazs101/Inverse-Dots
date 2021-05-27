package game.model;

import java.util.ArrayList;

/**
 * Class for placing walls on board.
 */
public class Wall {
    private ArrayList walls;

    /**
     * Constructor for setting up physics of walls.
     * @param pos1
     * @param pos2
     */
    public Wall(Position pos1, Position pos2) {
        if (pos1.equals(pos2.moveTo(PlayerDirection.UP)) || pos1.equals(pos2.moveTo(PlayerDirection.DOWN)) || pos1.equals(pos2.moveTo(PlayerDirection.RIGHT)) || pos1.equals(pos2.moveTo(PlayerDirection.LEFT))) {
            walls = new ArrayList();
            walls.add(pos1);
            walls.add(pos2);
        }
        else{
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param pos1 .
     * @param pos2 .
     * @return Returns true when both squares are in the list.
     */
    public boolean containsAll(Position pos1, Position pos2) {
        return walls.contains(pos1) && walls.contains(pos2);
    }

}
