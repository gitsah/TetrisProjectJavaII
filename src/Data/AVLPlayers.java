package Data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

/**
 * This class provide functionality to load Player Data to AVL, and save the AVL to a file
 * named "TetrisUserInfo.dat." Furthermore, it passphrase the user password, so that in case
 * someone hack the file, user password will be keep safe.
 * 
 * @author Sahand Milaninia, Haram Kwon, Cory Bakich
 */
public class AVLPlayers
{
	//Create AVLTree for data saving.
	private AVLTree<Player> tree = new AVLTree<>();
	
	/**
	 * Add a new user for Tetris which will include id, password, score, and level.
	 * The password will be encrypted for security.
	 * 
	 * @param id id of the user.
	 * @param password the first password for user.
	 * @param score The score for user.
	 * @param level the first level of the suer.
	 */
	public void addUser(String id, String password, int score, int level)
	{
		tree.insert(new Player(id, password, score, level));
	}
	
	/**
	 * Add user in tree, and arrange by their name, and no duplicated id is allowed.
	 * Password will be encrypted for security.
	 * 
	 * @param id The user id.
	 * @param password The user password.
	 * @return Whether the user info is inserted successfully.
	 */
	public boolean addUser(String id, String password)
	{
		return tree.insert(new Player(id, password, 0, 1));
	}
	
	/**
	 * Delete the user if the user id exist, and the password is right.
	 * 
	 * @param id The id that you want to delete.
	 * @param password The password of the user according to your id.
	 * @return true if the deleting was successful, but if no user id found or password does not match, return false.
	 */
	public boolean deleteUser(String id, String password)
	{
		Player backupPlayer = searchByID(id);
		
		if(backupPlayer != null && backupPlayer.checkPassword(password))
		{
			return tree.delete(new Player(id, password, 0, 1));
		}
		return false;
	}
	
	/**
	 * Save data into "TetrisUserInfo.dat".
	 */
	public void saveData()
	{
		LinkedList<Player> list = new LinkedList<>();
		
		for(Player element : tree)
		{
			list.add(element);
		}
		
		try ( // Create an output stream for file object.dat
			      ObjectOutputStream output =
			        new ObjectOutputStream(new FileOutputStream("TetrisUserInfo.dat"));
			    ) {
			output.writeObject(list);
			output.close();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File not Found");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException");
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Read AVL tree to the binary file: "TetrisUserInfo.dat".
	 * Because it is saved with linked list, it will load with linked list, and convert it to
	 * AVL.
	 * first and dump to the AVL tree.
	 */
	@SuppressWarnings("unchecked")
	public void readData()
	{
		LinkedList<Player> list = new LinkedList<>();
		
		try ( // Create an input stream for file object.dat
			      ObjectInputStream input =
			        new ObjectInputStream(new FileInputStream("TetrisUserInfo.dat"));
			    ) {
			
			list = (LinkedList<Player>)(input.readObject());
			input.close();
			
			for(Player element : list)
			{
				tree.insert(element);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Read data failure, IOExcption happens");
			System.out.println("It must be your first time to launch the Application");
			System.out.println("From next time, this exception will not happen. :)");
			saveData();
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotfoundException");
		} catch (IOException e) {
			System.out.println("IOException happens");
		}
	}
	
	/**
	 * Show data in dictionary order. This is only for showing data for developers.
	 */
	public void showData()
	{
		tree.inorder();
	}
	
	/**
	 * Convert AVLTree Player to simple Array
	 * Waring: If you change something here, it will not take efect on the real AVLTree Player.
	 * @return Array of the Player.
	 */
	public Player[] toArray()
	{
		Player[] players = new Player[tree.size];
		
		int i=0;
		
		for(Player element : tree)
		{
			players[i++] = element;
		}
		
		return players.clone();
	}
	
	/**
	 * Search by the Players by their id, and if their id exist return the player
	 * object. If not exist, it will return 
	 * @param id
	 * @return Player if there is the id of the player, but if there is no
         * player, return null.
	 */
	public Player searchByID(String id)
	{
		return tree.search(new Player(id, "", 0, 0));
	}
}
