package be.ac.ulb.infof307.g06.database;


public class ParseProductException extends Exception{
	
	/**
	 *  Classe pour les exceptions occurrant a la creation ou la connexion a un compte
	 */

	public static final int ERROR_IN_FILE = 1;
	public static final int INVALID_SHOP = 2;

	private static final long serialVersionUID = 1L;

	public  ParseProductException (String message){
		super(message);
	}

	public  ParseProductException (int errorId){
		super(idToMessage(errorId));
		
	}
	/**
	 * lie un errorID a un message d'erreur
	 * @param errorID : entier qui defini le type d'erreur
	 * @return message d'erreur
	 */
	private static String idToMessage(int errorID) {
		String tmp ="Default Message";
		switch(errorID) {
		case ERROR_IN_FILE:
			tmp = "Le fichier n'est pas valide";
			break;
		case INVALID_SHOP:
			tmp = "Le magasin n'existe pas";
			break;
		}
		return tmp;
	}
	
}