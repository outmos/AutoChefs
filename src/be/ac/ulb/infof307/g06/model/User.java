package be.ac.ulb.infof307.g06.model;

/**
 * Classe de l'utilisateur
 * @author Glyptodon
 *
 */
public class User {
	/**
	 * Nom de l'utilisateur
	 */
	private String name;
	/**
	 * id de l'utilisateur
	 */
	final private int userId;
	
	/**
	 * Constructeur 
	 */
	public User(final int userId, final String name) {
		this.userId = userId;
		this.name = name;
	}

	/**
	 * getter du nom de l'utilisateur 
	 */
	public String getName() {
		return name;
	}

	/**
	 * setter du nom de l'utilisateur 
	 */
	public void setName(final String name) {
		this.name = name;
	}
	/**
	 * getter de l'id de l'utilisateur 
	 */
	public int getID() {
		return userId;
	}

}
