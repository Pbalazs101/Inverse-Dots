package game.model;

import javafx.beans.property.ObjectProperty;

import java.util.*;

public class GameModel {

    public static int BOARD_SIZE = 7;

    private final Dot [] dots;

    /**
     * Initiates a starting position with a PLAYER and OPPONENT.
     */
    public GameModel() {
        this(new Dot(DotType.PLAYER, new Position(0, 4)),
                new Dot(DotType.OPPONENT, new Position(6, 2)));
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
     * @param dotNumber .
     * @param direction .
     * @return Boolean according to validity of move.
     */

    public boolean isValidMove(int dotNumber, PlayerDirection direction) {
        if (dotNumber < 0 || dotNumber >= dots.length) {
            throw new IllegalArgumentException();
        }
        Position newPosition = dots[dotNumber].getPosition().moveTo(direction);
        if (! isOnBoard(newPosition)) {
            return false;
        }
        for (var dot : dots) {
            if (dot.getPosition().equals(newPosition)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns every possible movement of given dot number.
     * @param dotNumber .
     * @return EnumSet containing valid moves.
     */

    public Set<PlayerDirection> getValidMoves(int dotNumber) {
        EnumSet<PlayerDirection> validMoves = EnumSet.noneOf(PlayerDirection.class);
        for (var direction : PlayerDirection.values()) {
            if (isValidMove(dotNumber, direction)) {
                validMoves.add(direction);
            }
        }
        return validMoves;
    }

    /**
     * Moves the given dot according to the given direction.
     * @param dotNumber .
     * @param direction .
     */
    public void move(int dotNumber, PlayerDirection direction) {
        dots[dotNumber].moveTo(direction);
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

    public static void main(String[] args) {
        GameModel model = new GameModel();
        System.out.println(model);
    }


}
