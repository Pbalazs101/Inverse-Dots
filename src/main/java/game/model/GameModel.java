package game.model;

import javafx.beans.property.ObjectProperty;

import java.util.*;

public class GameModel {

    public static int BOARD_SIZE = 7;

    private final Dot [] dots;

    private final ArrayList<Wall> walls = new ArrayList<>();

    /**
     * Initiates a starting position with a RED and BLUE dots.
     */
    public GameModel() {
        this(new Dot(DotType.RED, new Position(0, 4)),
                new Dot(DotType.BLUE, new Position(6, 2)));
        createWallPhysics();
    }

    public GameModel(Dot... dots) {
        checkDots(dots);
        this.dots = dots.clone();
    }


    /**
     * Determines if a given state is valid.
     * @param dots .
     */
    private void checkDots(Dot[] dots) {
        var seen = new HashSet<Position>();
        for (var dot : dots) {
            if (! isOnBoard(dot.getPosition()) || seen.contains(dot.getPosition())) {
                throw new IllegalArgumentException();
            }
            seen.add(dot.getPosition());
        }
    }


    /**
     *
     * @param dotNumber .
     * @return Position object describing a dot with the given dotNumber.
     */

    public Position getDotPosition(int dotNumber) {
        return dots[dotNumber].getPosition();
    }

    /**
     *
     * @param dotNumber .
     * @return Given dot's position property.
     */
    public ObjectProperty<Position> positionProperty(int dotNumber) {
        return dots[dotNumber].positionProperty();
    }

    /**
     * Determines the validity of a move.
     * @param direction .
     * @return Boolean according to validity of move.
     */

    public boolean isValidMove(PlayerDirection direction) {

        var inverseDirectionRowChange = direction.getRowChange()*-1;
        var inverseDirectionColChange = direction.getColChange()*-1;

        var inverseDirection = PlayerDirection.of(inverseDirectionRowChange,inverseDirectionColChange);

        Position oldPosition = dots[0].getPosition();
        Position newPosition = dots[0].getPosition().moveTo(direction);
        Position oldPosition2 = dots[1].getPosition();
        Position newPosition2 = dots[1].getPosition().moveTo(inverseDirection);
        if (! isOnBoard(newPosition)) {
            return false;
        }
        for (var dot : dots) {
            if (dot.getPosition().equals(newPosition)) {
                return false;
            }
        }
        for (var w : walls){
            if (w.containsAll(oldPosition,newPosition)) {
                return false;
            }
        }
        for (var w : walls){
            if (w.containsAll(oldPosition2,newPosition2)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns every possible movement of given dot number.
     * @return EnumSet containing valid moves.
     */

    public Set<PlayerDirection> getValidMoves() {
        EnumSet<PlayerDirection> validMoves = EnumSet.noneOf(PlayerDirection.class);
        for (var direction : PlayerDirection.values()) {
            if (isValidMove(direction)) {
                validMoves.add(direction);
            }
        }
        return validMoves;
    }

    /**
     * Moves the given dot according to the given direction.
     * @param direction .
     */
    public void move(PlayerDirection direction) {
        dots[0].moveTo(direction);
    }

    /**
     * Moves the blue dot according to the given direction.
     * @param direction .
     */
    public void moveBlue(PlayerDirection direction) {
        dots[1].moveTo(direction);
    }

    /**
     * Returns a
     * @param position .
     * @return Boolean depending on whether the board contains the given position.
     */
    public static boolean isOnBoard(Position position) {
        return 0 <= position.row() && position.row() < BOARD_SIZE
                && 0 <= position.col() && position.col() < BOARD_SIZE;
    }

    /**
     * Returns occupied positions.
     * @return List containing positions occupied by any dot.
     */
    public List<Position> getDotPositions() {
        List<Position> positions = new ArrayList<>(dots.length);
        for (var dot : dots) {
            positions.add(dot.getPosition());
        }
        return positions;
    }

    /**
     * 
     * @return Object as represented by a string with a given format.
     */
    public String toString() {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (var dot : dots) {
            joiner.add(dot.toString());
        }
        return joiner.toString();
    }

    /**
     * Creating barriers between squares according the diagram.
     */
    private void createWallPhysics () {
        walls.add(new Wall(new Position(1,2),new Position(1,3)));
        walls.add(new Wall(new Position(0,3),new Position(1,3)));
        walls.add(new Wall(new Position(1,3),new Position(2,3)));

        walls.add(new Wall(new Position(2,1),new Position(2,2)));
        walls.add(new Wall(new Position(2,2),new Position(3,2)));

        walls.add(new Wall(new Position(3,1),new Position(4,1)));

        walls.add(new Wall(new Position(2,4),new Position(3,4)));
        walls.add(new Wall(new Position(3,3),new Position(3,4)));
        walls.add(new Wall(new Position(3,4),new Position(4,4)));

        walls.add(new Wall(new Position(4,2),new Position(4,3)));
        walls.add(new Wall(new Position(4,3),new Position(5,3)));

        walls.add(new Wall(new Position(5,5),new Position(5,6)));
        walls.add(new Wall(new Position(4,6),new Position(5,6)));

        walls.add(new Wall(new Position(6,0),new Position(6,1)));

        walls.add(new Wall(new Position(6,3),new Position(5,3)));
        walls.add(new Wall(new Position(6,2),new Position(6,3)));

    }
    /*
    public static void main(String[] args) {
        GameModel model = new GameModel();
        System.out.println(model);
    }
     */

}
