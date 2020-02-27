package be.ac.ulb.infof307.g06.database;

import java.sql.SQLException;

public class DataAccessorException extends SQLException {
	
	/**
	 * Classe pour les exceptions occurrant lors d'une interaction avec la DB
	 */
	
	public static final int DB_ACCESS = 1;
	public static final int WRONG_REQUEST = 2;
	public static final int ALREADY_IN_DB = 3;
	public static final int NOT_IN_DB = 4;
	

	private static final long serialVersionUID = 1L;

	public DataAccessorException(int errorId){
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
		case DB_ACCESS:
			tmp = "Impossible d acceder a la base de donnee";
			break;
		case WRONG_REQUEST:
			tmp = "Requete invalide";
			break;
		case ALREADY_IN_DB:
			tmp = "Objet deja sauvegarde";
			break;
		case NOT_IN_DB:
			tmp = "L'objet ne peut etre supprime: il n est pas dans la base de donnee";
			break;
		
		}
		return tmp;
	}
}
