package be.ac.ulb.infof307.g06.database;
import java.security.MessageDigest;import java.security.NoSuchAlgorithmException;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.bind.DatatypeConverter;

/**
 * Gere la connexion a un compte et la creation d'un nouveau compte.
 * Les password sont crypte dans la base de donnee
 */
public final class AccountManager {
	private static final AccountManager INSTANCE = new AccountManager();
	private AccountManager() {
	}
	
	/**
	 * Renvoie l instance d AccountManager
	 * @return
	 */
	public static AccountManager getInstance() {
        return INSTANCE;
    }

	/**
	 * Verifie si le nom d'utilisateur est correcte
	 * @param input : nom de l'utilisateur
	 * @throws CreateConnectAccountException 
	 */
	private void checkNickname(String input) throws CreateConnectAccountException {
		if (input.length() < 3 || input.length() > 40) {
			throw new CreateConnectAccountException(CreateConnectAccountException.LENGTH_PSEUDO);
		}
		char[] inputLst = input.toCharArray();
		for (int i = 0; i < inputLst.length; ++i) {
			if (!Character.isLetter(inputLst[i]) && !Character.isDigit(inputLst[i])) {
				throw new CreateConnectAccountException(CreateConnectAccountException.SPECIAL_CHAR_IN_PSEUDO);
			}
		}
	}

	/**
	 * Verifie si le mot de passe est correcte
	 * @param input : mot de passe 
	 * @throws CreateConnectAccountException
	 */
	private void checkPassword(String input) throws CreateConnectAccountException {
		char[] inputLst = input.toCharArray();
		for (int i = 0; i < inputLst.length; ++i) {
			if (inputLst[i] == ' ') {
				throw new CreateConnectAccountException(CreateConnectAccountException.SPACE_IN_PASSWORD);
			}
		}
		if (input.length() < 6) {
			throw new CreateConnectAccountException(CreateConnectAccountException.LENGTH_PASSWORD);
		}
		boolean letter = false;
		boolean digit = false;
		for (int i = 0; i < inputLst.length; ++i) {
			letter = Character.isLetter(inputLst[i]) || letter;
			digit = Character.isDigit(inputLst[i]) || digit;
			if (letter && digit)
				break;
		}
		if (!(letter && digit)) {
			throw new CreateConnectAccountException(CreateConnectAccountException.NO_DIGIT_OR_LETTER_IN_PSEUDO);
		}

	}

	/**
	 * Lors de la cration d'un comtpe, verifie si les 2 mots de passe sont les memes
	 * @param input1 : premier mot de passe
	 * @param input2 : deuxieme mot de passe
	 * @throws CreateConnectAccountException
	 */
	private void checkEqualsPasswords(String input1, String input2) throws CreateConnectAccountException {
		if (!input1.equals(input2)) {
			throw new CreateConnectAccountException(CreateConnectAccountException.DIFFERENTS_PASSWORDS);
		}
	}

	
	/**
	 * Verifie si le nom d'utilisateur et le mot de passe sont ok. Si oui : encode
	 * les données dans la base de donnee et le compte est cree. Si non : return
	 * un message d'erreur qui va etre affiche a l'ecran
	 * @param username : nom d'utilisateur
	 * @param password : premier mot de passe
	 * @param repassword : deuxieme mot de passe
	 * @throws CreateConnectAccountException
	 * @throws NoSuchAlgorithmException
	 * @throws SQLException
	 * @throws BatchUpdateException
	 */
	public void insertToDatabase(String username, String password, String repassword)
			throws CreateConnectAccountException, NoSuchAlgorithmException, SQLException {
		
		checkNickname(username);
		checkPassword(password);
		checkEqualsPasswords(password, repassword);
		saveUsernamePassword(username, password);
		
	}

	/**
	 * Creation d'un nouveau profil
	 * @param username : nom d'utilisateur
	 * @param password : mot de passe
	 * @throws NoSuchAlgorithmException
	 * @throws BatchUpdateException
	 * @throws SQLException
	 */
	public void saveUsernamePassword(String username, String password)
			throws NoSuchAlgorithmException, BatchUpdateException, SQLException {

		Connection conn = DataAccessor.getConnection(); 
		PreparedStatement stUsernamePassword = null; 
		try{
			conn.setAutoCommit(false); 
			stUsernamePassword = conn.prepareStatement("insert into user set username=?, password=?"); // la requete
			stUsernamePassword.setString(1, username); 
			MessageDigest md1 = MessageDigest.getInstance("SHA-256");
			md1.update(password.getBytes());
			stUsernamePassword.setString(2, DatatypeConverter.printHexBinary(md1.digest()).toUpperCase());

			stUsernamePassword.addBatch(); 
			stUsernamePassword.executeBatch(); 

			conn.commit();

		} finally {
			if (stUsernamePassword != null) {
				stUsernamePassword.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	/**
	 * Recois le nom d'utilisateur et le mot de passe et verifie si toutes les donnees passees sont correctes
	 * @param username : nom d'utilisateur
	 * @param password : mot de passe
	 * @return l'id du user dans la base de donnee
	 * @throws CreateConnectAccountException
	 * @throws DataAccessorException
	 */
	public int connect(String username, String password) throws CreateConnectAccountException, DataAccessorException {

		Connection conn = DataAccessor.getConnection();
		PreparedStatement stPassword = null;
		ResultSet rs = null;
		int idUser = 0;

		try {
			stPassword = conn.prepareStatement("SELECT password, iduser from user where username=?"); // requete
			stPassword.setString(1, username);
			rs = stPassword.executeQuery();
			if (!rs.next()) {
				throw new CreateConnectAccountException(CreateConnectAccountException.NON_EXISTANT_USER);
			}

			String dataPassword = rs.getString(1);
			if (!dataPassword.equals(password)) {
				throw new CreateConnectAccountException(CreateConnectAccountException.USER_OR_PASSWORD_INVALID);
			}

			idUser = rs.getInt(2);
		} 
		catch (SQLException e) {
			throw new CreateConnectAccountException(DataAccessorException.DB_ACCESS);
		} finally {
			if (stPassword != null){
				try {
					stPassword.close();
				} catch (SQLException e) {
				}
			}
			if (conn != null){
				try{
					conn.close();
				}catch (SQLException e){
				}
			}
		}
		return idUser;
	}
	
	/**
	 * securise la connection en cryptant le mot de passe
	 * @param nickname : nom d'utilisateur
	 * @param ppassword : mot de passe
	 * @return l'id du user dans la base de donnee
	 * @throws NoSuchAlgorithmException
	 * @throws CreateConnectAccountException
	 * @throws DataAccessorException
	 */
	public int securisationConnect(String nickname, String ppassword) throws NoSuchAlgorithmException, CreateConnectAccountException, DataAccessorException {
		MessageDigest md1 = MessageDigest.getInstance("SHA-256");
		md1.update(ppassword.getBytes());
		final String inputHashed = DatatypeConverter.printHexBinary(md1.digest()).toUpperCase();
		int iduser = connect(nickname, inputHashed);
		return iduser;
	}

}
