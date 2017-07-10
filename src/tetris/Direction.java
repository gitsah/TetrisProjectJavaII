
package tetris;

/**
 *
 * @author Sahand Milaninia, Haram Kwon, Cory Bakich
 */

//Tells us which direction a piece has moved, relative to the center of the piece.
public enum Direction {
    //sets 4 types of Direction values with x,y values
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0);

    int x;
    int y;
    
    //Constructer for the Direction
    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    //Returns for the previous Direction value
    public Direction prev() {
        int nextIndex = ordinal() - 1;

        if (nextIndex == -1) {
            nextIndex = Direction.values().length - 1;
        }

        return Direction.values()[nextIndex];
    }
    
    //returns the next Direction value
    public Direction next() {
        int nextIndex = ordinal() + 1;

        if (nextIndex == Direction.values().length) {
            nextIndex = 0;
        }

        return Direction.values()[nextIndex];
    }
}
