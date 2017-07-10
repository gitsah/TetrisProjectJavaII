package Data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

/**
 * Save the Player's information into the LinkedList.
 * @author Sahand Milaninia, Haram Kwon, Cory Bakich
 */

public class PlayersLinkedList
{
	private LinkedList<Player> list = new LinkedList<Player>();
	
	public void addUser(String name, String password, int score, int level)
	{
		list.add(new Player(name, password, score, level));
	}
	
	
	
	public void addUser(String id, String password)
	{
		list.add(new Player(id, password, 0, 1));
	}
	
	public void saveData()
	{
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
	
	@SuppressWarnings("unchecked")
	public void readData()
	{
		try ( // Create an input stream for file object.dat
			      ObjectInputStream input =
			        new ObjectInputStream(new FileInputStream("TetrisUserInfo.dat"));
			    ) {
			
			list = (LinkedList<Player>)(input.readObject());
			input.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("File not Found");
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotfoundException");
		} catch (IOException e) {
			System.out.println("IOException");
		}
	}
	
	/**
	 * Must be deleted after using
	 */
	public void showData()
	{
		for(Player element : list)
		{
			System.out.println("id: " + element.getId());
			System.out.println("password: " + element.getPassword());
			System.out.println("Score: " + element.getScore());
			System.out.println("GetLeve: " + element.getLevel());
		}
	}
}
