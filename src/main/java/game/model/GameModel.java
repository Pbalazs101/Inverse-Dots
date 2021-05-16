package game.model;

import javafx.beans.property.ObjectProperty;

import java.util.*;

public class GameModel {

    public static int BOARD_SIZE = 7;

    private final Dot [] dots;


    public GameModel() {
        this(new Dot(DotType.PLAYER, new Position(0, 4)),
                new Dot(DotType.OPPONENT, new Position(6, 2)));
    }

    public GameModel(Dot... dots) {
        checkDots(dots);
        this.dots = dots.clone();
    }

    private void checkDots(Dot[] dots) {
        var seen = new HashSet<Position>();
        for (var dot : dots) {
            if (! isOnBoard(dot.getPosition()) || seen.contains(dot.getPosition())) {
                throw new IllegalArgumentException();
            }
            seen.add(dot.getPosition());
        }
    }

    public int getDotCount() {
        return dots.length;
    }

    public DotType getDotType(int dotNumber) {
        return dots[dotNumber].getType();
    }

    public Position getDotPosition(int dotNumber) {
        return dots[dotNumber].getPosition();
    }

    public ObjectProperty<Position> positionProperty(int dotNumber) {
        return dots[dotNumber].positionProperty();
    }

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

    public Set<PlayerDirection> getValidMoves(int dotNumber) {
        EnumSet<PlayerDirection> validMoves = EnumSet.noneOf(PlayerDirection.class);
        for (var direction : PlayerDirection.values()) {
            if (isValidMove(dotNumber, direction)) {
                validMoves.add(direction);
            }
        }
        return validMoves;
    }

    public void move(int dotNumber, PlayerDirection direction) {
        dots[dotNumber].moveTo(direction);
    }

    public static boolean isOnBoard(Position position) {
        return 0 <= position.row() && position.row() < BOARD_SIZE
                && 0 <= position.col() && position.col() < BOARD_SIZE;
    }

    public List<Position> getDotPositions() {
        List<Position> positions = new ArrayList<>(dots.length);
        for (var dot : dots) {
            positions.add(dot.getPosition());
        }
        return positions;
    }

    public OptionalInt getDotNumber(Position position) {
        for (int i = 0; i < dots.length; i++) {
            if (dots[i].getPosition().equals(position)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

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
