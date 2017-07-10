/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import Data.AVLPlayers;
import Data.Player;

/**
 *
 * @author Sahand Milaninia, Haram Kwon, Cory Bakich
 */

public class Tetris extends Application {
    
    public static AVLPlayers data = new AVLPlayers();
    public static final int TILE_SIZE = 40;
    public static final int GRID_WIDTH = 15;
    public static final int GRID_HEIGHT = 20;

    private double time;
    private double timeDifficulty;
    private GraphicsContext g;

    private int[][] grid = new int[GRID_WIDTH][GRID_HEIGHT];
    
    private ArrayList<Tetrimino> original = new ArrayList<>();
    private ArrayList<Tetrimino> tetriminos = new ArrayList<>();

    private Tetrimino selected;

    private IntegerProperty score = new SimpleIntegerProperty(0);
    private DoubleProperty scoreRate = new SimpleDoubleProperty(50);
    private Label currentScore = new Label();
    private Label scoreWord = new Label("Score: ");
    
    private IntegerProperty level = new SimpleIntegerProperty(0);
    private Label currentLevel = new Label();
    private Label levelWord = new Label("Level: ");
    private Double levelUp = 0.0;
    
    private static Stage pStage;
    
    public static boolean isGuest = false;
    public static Player currentPlayer;
    //FXMLDocumentController controller = loader.getController();
    
    public Parent createContent() {
        //creates a Pane and a canvas with a grid based on predefined dimentions
        Pane root = new Pane();
        root.setPrefSize(GRID_WIDTH * TILE_SIZE, GRID_HEIGHT * TILE_SIZE);
        Canvas canvas = new Canvas(GRID_WIDTH * TILE_SIZE, GRID_HEIGHT * TILE_SIZE);
        g = canvas.getGraphicsContext2D();
        
        //creates an HBox for the top score bar
        HBox top = new HBox();
        currentScore.setPadding(new Insets(0,400,0,0));
        
        //sets font size for and binds the level properties
        levelWord.setStyle("-fx-font: 21 times;");
        currentLevel.setStyle("-fx-font: 21 times;");
        currentLevel.textProperty().bind(level.asString());
        
        //sets font size for and binds the score properties
        scoreWord.setStyle("-fx-font: 21 times;");
        currentScore.setStyle("-fx-font: 21 times;");
        currentScore.textProperty().bind(score.asString());
        
        //adds the score and level to the HBox and the canvas and HBox to the pane
        top.getChildren().addAll(scoreWord, currentScore, levelWord, currentLevel);
        root.getChildren().addAll(canvas, top);
        
        //Creates Tetris pieces from 1 by 1 pieces, this piece is a single square
        original.add(new Tetrimino(Color.BLUE, new Piece(0, Direction.DOWN)));
        
        //Creates 4 piece line
        original.add(new Tetrimino(Color.BLUE,
                new Piece(0, Direction.DOWN),
                new Piece(1, Direction.LEFT),
                new Piece(1, Direction.RIGHT),
                new Piece(2, Direction.RIGHT)
        ));
        
        //Creates the 3 way pointing shape thingy
        original.add(new Tetrimino(Color.RED,
                new Piece(0, Direction.DOWN),
                new Piece(1, Direction.LEFT),
                new Piece(1, Direction.RIGHT),
                new Piece(1, Direction.DOWN)
        ));
        
        /*
        //this thingy here would be a 2by2 square but not working
        original.add(new Tetrimino(Color.RED,
                new Piece(0, Direction.DOWN),
                new Piece(1, Direction.DOWNRIGHT),
                new Piece(1, Direction.RIGHT),
                new Piece(1, Direction.DOWN)
        ));/*
        //old thingy here, replaced
        /*original.add(new Tetrimino(Color.GREEN,
                new Piece(0, Direction.DOWN),
                new Piece(1, Direction.RIGHT),
                new Piece(2, Direction.RIGHT),
                new Piece(1, Direction.DOWN)
        ));*/
        
        //right and left facing 3 length lines with the side pointing ends
        original.add(new Tetrimino(Color.ORANGE,
                new Piece(0, Direction.DOWN),
                new Piece(1, Direction.DOWN),
                new Piece(2, Direction.DOWN),
                new Piece(1, Direction.RIGHT)
        ));
        original.add(new Tetrimino(Color.GREEN,
                new Piece(0, Direction.DOWN),
                new Piece(1, Direction.DOWN),
                new Piece(2, Direction.DOWN),
                new Piece(1, Direction.LEFT)
        ));
        
        //spawns the first piece
        spawn();
        
        //starts an AnimationTimer, used to move the piece down in the grid
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                //time counter that goes up every frame, gradually increases as
                //the game goes on from timeDifficulty
                time += 0.018 + timeDifficulty;
                timeDifficulty += 0.000012;
                scoreRate.setValue(scoreRate.getValue() * 1.0001);
                //levelUp counter goes up with each frame
                levelUp += 0.00084;
                
                //when the time counter hits 1 it updates the grid, moving the Tetrimino piece down
                if (time >= 1 && isValidState()) {
                    update();
                    render();
                    time = 0;
                }
                //when the level counter hits 1 it ups the current level by 1
                if (levelUp >= 1 && isValidState()) {
                    levelUp = 0.0;
                    level.setValue(level.getValue() + 1);
                }
                
            }
        };
        //starts the AnimationTimer
        timer.start();
            
        return root;
    }
    
    //moves the piece down 1 tile, used to move the pieve down on a timer
    private void update() {
        makeMove(p -> p.move(Direction.DOWN), p -> p.move(Direction.UP), true);
    }
    
    //renders the grid with all the current Tetriminos on it
    private void render() {
        g.clearRect(0, 0, GRID_WIDTH * TILE_SIZE, GRID_HEIGHT * TILE_SIZE);

        tetriminos.forEach(p -> p.draw(g));
    }
    
    //adds a piece to the grid
    private void placePiece(Piece piece) {
        grid[piece.x][piece.y]++;
    }
    
    //removes a piece from the grid
    private void removePiece(Piece piece) {
        grid[piece.x][piece.y]--;
    }
    
    //checks if the piece is off the grid
    private boolean isOffscreen(Piece piece) {
        return piece.x < 0 || piece.x >= GRID_WIDTH
                || piece.y < 0 || piece.y >= GRID_HEIGHT;
    }
    
    //used to move the Tetrimino pieces
    private void makeMove(Consumer<Tetrimino> onSuccess, Consumer<Tetrimino> onFail, boolean endMove) {
        selected.pieces.forEach(this::removePiece);
            
        onSuccess.accept(selected);
        
        //sets a boolean to the offscreen state on each piece
        boolean offscreen = selected.pieces.stream().anyMatch(this::isOffscreen);
        
        //checks to make sure the piece isn't offscreen
        if (!offscreen) {
            selected.pieces.forEach(this::placePiece);
        } else {
            onFail.accept(selected);

            selected.pieces.forEach(this::placePiece);

            if (endMove) {
                sweep();
            }

            return;
        }
        
        //checks if the piece is in a valid state
        if (!isValidState()) {
            selected.pieces.forEach(this::removePiece);

            onFail.accept(selected);

            selected.pieces.forEach(this::placePiece);

            if (endMove) {
                sweep();
            }
        }
    }
    
    //checks if the Tetrimino is in a valid state on the grid
    private boolean isValidState() {
        for (int y = 0; y < GRID_HEIGHT; y++) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                if (grid[x][y] > 1) {
                    return false;
                }
            }
        }

        return true;
    }

    //sweep method used to clear a row when all pieces line up
    private void sweep() {
        List<Integer> rows = sweepRows();
        //detatches the single square pieces from the grid for a cleared row,
        //and adds to the score
        rows.forEach(row -> {
            score.setValue(score.getValue() + scoreRate.getValue());
            
            for (int x = 0; x < GRID_WIDTH; x++) {
                for (Tetrimino tetrimino : tetriminos) {
                    tetrimino.detach(x, row);
                }   
                grid[x][row]--;
            }
        });

        //runs through the single square pieces in a cleared row and removes them
        rows.forEach(row -> {
            tetriminos.stream().forEach(tetrimino -> {
                tetrimino.pieces.stream()
                        .filter(piece -> piece.y < row)
                        .forEach(piece -> {
                            removePiece(piece);
                            piece.y++;
                            placePiece(piece);
                        });
            });
        });

        spawn();
    }
    
    //list for row to be sweeped
    private List<Integer> sweepRows() {
        List<Integer> rows = new ArrayList<>();

        outer:
        for (int y = 0; y < GRID_HEIGHT; y++) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                if (grid[x][y] != 1) {
                    continue outer;
                }
            }
            rows.add(y);
        }

        return rows;
    }
    
    //used by FXMLDocumentController to start the Tetris.createContent() stage
    public void switchBack(Stage stage) throws Exception{
        Scene scene = new Scene(createContent());
        
        stage.setScene(scene);
        scene.getStylesheets().add
         (Tetris.class.getResource("Style.css").toExternalForm());
        stage.show();
        
        //event handler that listens for a key press and does an action 
        scene.setOnKeyPressed(e -> {
            //does nothing if nothing is pressed
            if (null != e.getCode()) switch (e.getCode()) {
                case SPACE://handles space key press, rotates the piece
                    makeMove(p -> p.rotate(), p -> p.rotateBack(), false);
                    break;
                case UP://handles up key press, rotates the piece
                    makeMove(p -> p.rotate(), p -> p.rotateBack(), false);
                    break;
                case LEFT://handles left key press, moves one tile left
                    makeMove(p -> p.move(Direction.LEFT), p -> p.move(Direction.RIGHT), false);
                    break;
                case RIGHT://handles right key press, moves one tile right
                    makeMove(p -> p.move(Direction.RIGHT), p -> p.move(Direction.LEFT), false);
                    break;
                case DOWN://handles down key press, moves one tile down
                    makeMove(p -> p.move(Direction.DOWN), p -> p.move(Direction.UP), true);
                    break;
                default://handles anything else
                    break;
            }
            //renders the grid and the pieces in it
            render();
            
            });
    }
    //picks a random Tetrimino out of the list to spawn
    private void spawn() {
        Tetrimino tetrimino = original.get(new Random().nextInt(original.size())).copy();
        tetrimino.move(GRID_WIDTH / 2, 0);

        selected = tetrimino;

        tetriminos.add(tetrimino);
        tetrimino.pieces.forEach(this::placePiece);

        //if it can't spawn the piece in a valid state the game is over 
        if (!isValidState()) {
            try {
                //Stage stageTheLabelBelongs = (Stage) currentScore.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("GameOver.fxml"));
                Scene scene = new Scene(root);
                pStage.setScene(scene);
                pStage.show();
                //saves player score if they weren't a guest
                if(!isGuest){
                    if(currentPlayer.getScore()<score.getValue() 
                     && currentPlayer.getLevel()<level.getValue()){
                        currentPlayer.setScore(score.getValue());
                        currentPlayer.setLevel(level.getValue());
                    }
                    //saves the changes to player list to the player file
                    data.saveData();
                }
            } catch (IOException ex) {
                Logger.getLogger(Tetris.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //starts the program up onto the MainSplash FXML file
    @Override
    public void start(Stage stage) throws Exception {
        //stores this stage as the primary stage for future reference
        pStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("MainSplash.fxml"));
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    //main class for IDE
    public static void main(String[] args) {
        launch(args);
    }
}