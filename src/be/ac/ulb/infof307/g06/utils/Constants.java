package be.ac.ulb.infof307.g06.utils;
/**
 * Classe qui repertorie les constantes.
 * @author Glyptodon
 *
 */
public final class Constants {
	/**
	 * Instance de la classe
	 */
	private static Constants constant;
	/**
	 * NO_SHOPID constante
	 */
	public static final int NO_SHOPID = -1; //add product 
	/**
	 * PLUS constante
	 */
	public static final int PLUS = 1;		//ShoppingList
	/**
	 * MINUS constante
	 */
	public static final int MINUS = 0;		//ShoppingList 
	
	private Constants(){}
	/**
	 * Singleton implementation
	 * @return Constants
	 */
	public synchronized static Constants getInstance(){
		if(constant == null){
			constant= new Constants();
		}
		return constant;
	}
}
