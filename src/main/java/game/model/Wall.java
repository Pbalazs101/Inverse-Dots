package game.model;

import java.util.ArrayList;

public class Wall {
    private ArrayList walls;

    public Wall(Position pos1, Position pos2) {
        walls = new ArrayList();
        walls.add(pos1);
        walls.add(pos2);
    }


    public boolean containsAll(Position pos1, Position pos2) {
        return walls.contains(pos1) && walls.contains(pos2);
    }

}
