package Data;

import java.io.Serializable;

import AES.AES;
import java.io.Serializable;

/**
 * A Player object which will contains the Player id, password, score, and level of the user.
 * @author Sahand Milaninia, Haram Kwon, Cory Bakich
 */
 
public class Player implements Serializable, Comparable<Player>
{
	/**
	 * Make instance variable for user id, password, and score, and level.
	 */
	private String id;
	private String password;
	private int score;
	private int level;
	
	/**
	 * Construct the Player object with id, password, score, and level.
	 * @param id The id of the user.
	 * @param password The password of the user.
	 * @param score The score of the user.
	 * @param level The selve of the user you want to set.
	 */
	public Player (String id, String password, int score, int level)
	{
		this.id = id;
		this.password = AES.convertToString(password);
		this.score = score;
		this.level = level;
	}
	
	/**
	 * Get the id of the user
	 * @return User id
	 */
	public String getId()
	{
		return this.id;
	}
	
	/**
	 * Get Encrypted password 
	 * @return AES Encrypted password.
	 */
	public String getPassword()
	{
		return this.password;
	}
	
	/**
	 * Change the password of the user.
	 * @param newPassword The new password for the user.
	 */
	public void changePassword(String newPassword)
	{
		this.password = AES.convertToString(newPassword);
	}
	
	/**
	 * Check the password whether it is right or wrong.
	 * @param password the password that you want to check.
	 * @return return True if the password is correct, false if password is wrong.
	 */
	public boolean checkPassword(String password)
	{
		if(this.password.compareTo(AES.convertToString(password)) == 0)
			return true;
		
		return false;
	}
	
	/**
	 * Get the score of the user.
	 * @return The score of the user.
	 */
	public int getScore()
	{
		return this.score;
	}
	
	/**
	 * Set the score to the input score.
	 * @param score score that you want.
	 */
	public void setScore(int score)
	{
		this.score = score;
	}
	
	public void addScore(int score)
	{
		this.score += score;
	}
	
	/**
	 * Get the level of the user.
	 * @return the level of the user.
	 */
	public int getLevel()
	{
		return this.level;
	}
	
	public void leveUp()
	{
		this.level++;
	}
	
	public void setLevel(int level)
	{
		this.level = level;
	}

	/**
	 * Compare by their ID and password.
	 */
	@Override
	public int compareTo(Player o) {
		return this.id.compareTo(o.getId());
	}
	
	/**
	 * Show the user information.
	 */
	public void showData()
	{
		System.out.println("-------------------------------");
		System.out.println("User ID: " + id);
		System.out.println("User password: " + password);
		System.out.println("User Score: " + score);
		System.out.println("User level" + level);
	}
	

	/**
	 * Make User information to string.
	 */
	@Override
	public String toString()
	{
		return "-------------------------------\n"+
				"User ID: " + id + '\n' +
				"User password: " + password + '\n' +
				"User Score: " + score + '\n' +
				"User level: " + level + '\n';
	}
	
}