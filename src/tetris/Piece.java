package tetris;

/**
 *
 * @author Sahand Milaninia, Haram Kwon, Cory Bakich
 */

//class for the 1 by 1 pieces used to make Tetriminos
public class Piece {
    public int distance;
    public Direction direction;
    public Tetrimino parent;
    public int x;
    public int y;
    
    //constructer for the Piece
    public Piece(int distance, Direction direction) {
        this.distance = distance;
        this.direction = direction;
    }

    //sets the Piece's parent to a Tetrimino piece
    public void setParent(Tetrimino parent) {
        this.parent = parent;
        x = parent.x + distance * direction.x;
        y = parent.y + distance * direction.y;
    }
    
    //Sets the direction to put the Piece
    public void setDirection(Direction direction) {
        this.direction = direction;
        x = parent.x + distance * direction.x;
        y = parent.y + distance * direction.y;
    }
    
    //creates a copy of a Piece
    public Piece copy() {
        return new Piece(distance, direction);
    }
}
