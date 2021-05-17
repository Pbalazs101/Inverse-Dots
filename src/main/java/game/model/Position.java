package game.model;


public record Position(int row, int col) {

    /**
     * Returns a new Position object calculated from given direction
     *
     * @param direction representing a specific change in x and y coordinates
     * @return Position object of state after the move occurred.
     */
    public Position moveTo(Direction direction){
        return new Position(row + direction.getRowChange(), col + direction.getColChange());
    }
}
