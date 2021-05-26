package game.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Dot {


    private final DotType type;
    private final ObjectProperty<Position> position = new SimpleObjectProperty<>();

    public Dot(DotType type, Position position) {
        this.type = type;
        this.position.set(position);
    }

    /**
     * Sets a new value for the Position object
     * @param direction .
     */
    public void moveTo(Direction direction) {
        Position newPosition = position.get().moveTo(direction);
        position.set(newPosition);
    }

    public ObjectProperty<Position> positionProperty() {
        return position;
    }

    /**
     *
     * @return Dot type and its position as a string
     */
    public String toString() {
        return type.toString() + position.get().toString();
    }


    /**
     *
     * @return Position object.
     */
    public Position getPosition() {
        return position.get();
    }


    public static void main(String[] args) {
        Dot dot = new Dot(DotType.RED, new Position(0, 0));
        dot.positionProperty().addListener((observableValue, oldPosition, newPosition) -> {
            System.out.printf("%s -> %s\n", oldPosition.toString(), newPosition.toString());
        });
        System.out.println(dot);
        dot.moveTo(PlayerDirection.DOWN);
        dot.moveTo(PlayerDirection.LEFT);
        System.out.println(dot);
    }

}
