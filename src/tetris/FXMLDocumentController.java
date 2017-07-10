/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import Data.AVLPlayers;
import Data.Player;

/**
 *
 * @author Sahand Milaninia, Haram Kwon, Cory Bakich
 */
public class FXMLDocumentController implements Initializable {
    
    //initializes all the FXML elements into usable variables
    @FXML
    private Button Back;
    @FXML
    private Button GuestLog;
    @FXML
    private Button listScore;
    @FXML
    private Button MainMenu;
    @FXML
    private Button PlayAgain;
    @FXML
    private Button Quit;
    @FXML
    private Button Register;
    @FXML
    private Button SignIn;
    @FXML
    private Button SignOut;
    @FXML
    private Button SignUp;
    @FXML
    private Button StartGame;
    @FXML
    private Label IncorrectPass;
    @FXML
    private Label Registering;
    @FXML
    private TableView<Player> Highscores;
    @FXML
    private TextField Username;
    @FXML
    private TextField ConfirmPassUp;
    @FXML
    private TextField UsernameUp;
    @FXML
    private TextField Password;
    @FXML
    private TextField PasswordUp;
    
    Tetris tetris = new Tetris();
    AVLPlayers data = Tetris.data;
    
    //handles actions for button presses within the FXML parts of our program
    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception{
        Parent root;
        Stage stage;
        
        //handles SignIn button clicked
        if(event.getSource()==SignIn){
            //Searches list of registered players for the username match
            Player player = data.searchByID(Username.getText());
            
            //if a player was found, and the password in the box matches the 
            //password in the list then it logs the player in loads next scene
            if(player != null && player.checkPassword(Password.getText())){
                Tetris.isGuest = false;
                Tetris.currentPlayer = player;
                //get reference to the button's stage
                stage=(Stage) SignIn.getScene().getWindow();
                //load up OTHER FXML document
                root = FXMLLoader.load(getClass().getResource("Highscores.fxml"));

                //create a new scene with root and set the stage
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            
            //UserIn.setText(Username.getText());
            }
            //if the username or password weren't correct, it notifies the user
            else
                IncorrectPass.setText("Incorrect Username or Password");
        }
        
        //handles SignUp button clicked
        if(event.getSource()==SignUp){
            //get reference to the button's stage
            stage=(Stage) SignUp.getScene().getWindow();
            //load up other FXML document
            root = FXMLLoader.load(getClass().getResource("Register.fxml"));
            
            //create a new scene with root and set the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        
        //handles Register button clicked
        if(event.getSource()==Register){
            //checks if both password and confirm password fields are the same
            if(PasswordUp.getText().compareTo(ConfirmPassUp.getText()) == 0){
            //checks if username is already in use, if it isn't it saves the new
            //username and password into the file
            if(data.addUser(UsernameUp.getText(), PasswordUp.getText())){
                data.saveData();
                Registering.setText("Registration successful, you can now sign in!");}
            else
                //lets user know if username was in use
                Registering.setText("Username is already in use");
            }
            else
                //lets user know if password do not match
                Registering.setText("Passwords do not match");
        }
        
        //handles StartGame button clicked
        if(event.getSource()==StartGame){
            //get reference to the button's stage
            stage=(Stage) StartGame.getScene().getWindow();
            
            //runs a method in Tetris that loads up the game in javafx
            tetris.switchBack(stage);
        }
        
        /**
         * Show list of scores and levels
         */
        if(event.getSource() == listScore)
        {
            final ObservableList<Player> data = 
                    FXCollections.observableArrayList(Tetris.data.toArray());
            
            Highscores.setEditable(true);
            Highscores.getColumns().clear();
            
            TableColumn nameCol = new TableColumn("User ID");
            nameCol.setMinWidth(70);
            nameCol.setCellValueFactory(
                    new PropertyValueFactory<Player, String>("id"));
            
            TableColumn scoreCol = new TableColumn("Score");
            scoreCol.setMinWidth(70);
            scoreCol.setCellValueFactory(
                    new PropertyValueFactory<Player, String>("score"));
            
            TableColumn levelCol = new TableColumn("level");
            levelCol.setMinWidth(70);
            levelCol.setCellValueFactory(
                    new PropertyValueFactory<Player, String>("level"));
            
            Highscores.setItems(data);
            Highscores.getColumns().addAll(nameCol, scoreCol, levelCol);
            
        }
        
        //handles GuestLog button clicked, no score saving
        if(event.getSource()==GuestLog){
            Tetris.isGuest = true;
            //get reference to the button's stage
            stage=(Stage) GuestLog.getScene().getWindow();
            //load up other FXML document
            root = FXMLLoader.load(getClass().getResource("Highscores.fxml"));
            
            //create a new scene with root and set the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
            //dotest("guest");
            //setName("Guest");
            //UserIn.setText("Guest"); //This isn't working for some reason
        }
        
        //handles SignOut button clicked
        if(event.getSource()==SignOut){
            //get reference to the button's stage
            stage=(Stage) SignOut.getScene().getWindow();
            //load up other FXML document
            root = FXMLLoader.load(getClass().getResource("MainSplash.fxml"));
            
            //create a new scene with root and set the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        
        //handles Back button clicked
        if(event.getSource()==Back){
            //get reference to the button's stage
            stage=(Stage) Back.getScene().getWindow();
            //load up other FXML document
            root = FXMLLoader.load(getClass().getResource("MainSplash.fxml"));
            
            //create a new scene with root and set the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        
        //handles PlayAgain button clicked
        if(event.getSource()==PlayAgain){
            //get reference to the button's stage
            stage=(Stage) PlayAgain.getScene().getWindow();
            
            //runs a method in Tetris that loads up the game in javafx
            tetris.switchBack(stage);
        }
        
        //handles MainMenu button clicked
        if(event.getSource()==MainMenu){
            //get reference to the button's stage
            stage=(Stage) MainMenu.getScene().getWindow();
            //load up other FXML document
            root = FXMLLoader.load(getClass().getResource("Highscores.fxml"));
            
            //create a new scene with root and set the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        
        //handles Quit button clicked
        if(event.getSource()==Quit){
            System.exit(0);
        }
    }
    
    //initializes the controller, loads the account data from the file into a list
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //loads player data from file into a list
        data.readData();
    }
}
