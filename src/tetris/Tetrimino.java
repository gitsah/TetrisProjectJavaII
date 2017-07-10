package tetris;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static tetris.Tetris.TILE_SIZE;

/**
 *
 
 */

/*
*Class that is used to create the Tetrimino pieces by taking in any number of
*piece variables and a color. Contains many methods for using a Tetrimino piece.
* @author Sahand Milaninia, Haram Kwon, Cory Bakich
*/
public class Tetrimino {

    public int x;
    public int y;

    //Each block should have a seperate color.
    public Color color;
    
    //list of 1 tile Pieces, used to create Tetrimino pieces
    public List<Piece> pieces;
    
    //contructer for the Tetrimino pieces, takes a color and Pieces to make one
    public Tetrimino(Color color, Piece...pieces) {
        this.color = color;
        this.pieces = new ArrayList<>(Arrays.asList(pieces));

        for (Piece piece : this.pieces)
            piece.setParent(this);
    }

    //moves based on the x and y value given relative to the center of the piece
    public void move(int dx, int dy) {
        x += dx;
        y += dy;

        pieces.forEach(p -> {
            p.x += dx;
            p.y += dy;
        });
    }
    
    //calls a move command giving an x and y value based off a Direction value given
    public void move(Direction direction) {
        move(direction.x, direction.y);
    }
    
    //draws the piece
    public void draw(GraphicsContext g) {
        g.setFill(color);

        pieces.forEach(p -> g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE));
    }
    
    //counter rotates if piece would go off the screen
    public void rotateBack() {
        pieces.forEach(p -> p.setDirection(p.direction.prev()));
    }

    //Take each piece, set its direction to direction next.
    public void rotate() {
        pieces.forEach(p -> p.setDirection(p.direction.next()));
    }

    //Remove from the list, if the x and y are equal
    public void detach(int x, int y) {
        pieces.removeIf(p -> p.x == x && p.y == y);
    }
    
    //creates a copy of a Tetrimino piece
    public Tetrimino copy() {
        return new Tetrimino(color, pieces.stream()
                .map(Piece::copy)
                .collect(Collectors.toList())
                .toArray(new Piece[0]));
    }
}
