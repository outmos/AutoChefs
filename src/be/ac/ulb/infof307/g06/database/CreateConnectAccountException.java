package be.ac.ulb.infof307.g06.database;


public class CreateConnectAccountException extends Exception{
	
	/**
	 * Classe pour les exceptions occurrant a la creation ou la connexion a un compte
	 */
	
	public static final int LENGTH_PSEUDO = 1;
	public static final int SPECIAL_CHAR_IN_PSEUDO = 2;
	public static final int SPACE_IN_PASSWORD = 3;
	public static final int LENGTH_PASSWORD = 4;
	public static final int NO_DIGIT_OR_LETTER_IN_PSEUDO = 5;
	public static final int DIFFERENTS_PASSWORDS = 6;
	public static final int NON_EXISTANT_USER = 7;
	public static final int USER_OR_PASSWORD_INVALID = 8;
	public static final int UNKNOWN_ERROR = 10;

	private static final long serialVersionUID = 1L;

	public CreateConnectAccountException(int errorId){
		super(idToMessage(errorId));
		
	}
	
	/**
	 * lie un errorID a un message d'erreur
	 * @param errorID : entier 
	 * @return
	 */
	private static String idToMessage(int errorID) {
		String tmp ="Default Message";
		switch(errorID) {
		case LENGTH_PSEUDO:
			tmp = "Le pseudo doit au moins contenir 3 caracteres et au plus 40";
			break;
		case SPECIAL_CHAR_IN_PSEUDO:
			tmp = "Le pseudo ne doit pas contenir de caracteres speciaux";
			break;
		case SPACE_IN_PASSWORD:
			tmp = "Le mot de passe ne doit pas contenir d'espace";
			break;
		case LENGTH_PASSWORD:
			tmp = "Le mot de passe doit contenir au moins 6 caracteres";
			break;
		case NO_DIGIT_OR_LETTER_IN_PSEUDO:
			tmp = "Le mot de passe doit contenir au moins un chiffre et une lettre";
			break;
		case DIFFERENTS_PASSWORDS:
			tmp = "Les deux mots de passe entres ne sont pas identiques";
			break;
		case NON_EXISTANT_USER:
			tmp = "Ce nom d'utilisateur n'existe pas";
			break;
		case USER_OR_PASSWORD_INVALID:
			tmp = "Le nom d'utilisateur et le mot de passe ne correspondent pas";
			break;
		case UNKNOWN_ERROR:
			tmp = "Erreur de recuperation de l'ID de l'utilisateur. Si l'erreur persiste veuillez contacter quelqu'un de competent";
			break;
		}
		return tmp;
	}
	
}