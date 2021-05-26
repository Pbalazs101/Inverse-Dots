package game.model;

import java.util.ArrayList;

public class Wall {
    private ArrayList walls;

    /**
     * Constructor for setting up physics of walls.
     * @param pos1
     * @param pos2
     */
    public Wall(Position pos1, Position pos2) {
        walls = new ArrayList();
        walls.add(pos1);
        walls.add(pos2);
    }


    public boolean containsAll(Position pos1, Position pos2) {
        return walls.contains(pos1) && walls.contains(pos2);
    }

}
