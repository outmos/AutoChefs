package be.ac.ulb.infof307.g06.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Recupere le mot de passe de la base de donnee dans un fichier
 * et decrypte. Si le mdp est incorrect ou si le fichier n existe pas
 * le mdp sera demand� a l utilisateur
 */
public final class CheckDatabaseId {
	private static final String PATH_FILE = "database_id.txt";
	private static final String KEY = "QfTjWmZq4t7w!z%C";
	private static final CheckDatabaseId INSTANCE = new CheckDatabaseId();
	private CheckDatabaseId(){
	}
	
	/**
	 * Renvoie l instance de CheckDatabaseId
	 * @return
	 */
	public static CheckDatabaseId getInstance(){
		return INSTANCE;
	}
	
	
	/**
	 * Decrypte le mdp stocke dans database_id.txt et le met dans
	 * DataAccessor.password
	 * @throws Exception
	 */
	private static void decrypt() throws Exception {

		File inputFile = new File(PATH_FILE);
		Key secretKey = new SecretKeySpec(KEY.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);

		FileInputStream inputStream = new FileInputStream(inputFile);
		byte[] inputBytes = new byte[(int) inputFile.length()];
		inputStream.read(inputBytes);

		byte[] outputBytes = cipher.doFinal(inputBytes);

		inputStream.close();
		DataAccessor.setPassword(new String(outputBytes));

		
	}

	/**
	 * Verifie si le mdp stocke dans database_id.txt est le bon
	 * 
	 * @return True si la connection a la base de donnee est reussi. Sinon False
	 */
	public static boolean checkId() {
		try {
			decrypt();
		} catch (Exception e) {
			return false;
		}
		return (DataAccessor.testConnection());
	}

	/**
	 * Sauvegarde, dans database_id.txt, une version encrypte du mdp donne par l
	 * utilisateur
	 * 
	 * @param pass : mot de passe
	 */
	public static void savePassword(String pass) throws Exception {
		Key secretKey = new SecretKeySpec(KEY.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);

		byte[] inputBytes = pass.getBytes("UTF-8");
		byte[] outputBytes = cipher.doFinal(inputBytes);

		File file = new File(PATH_FILE);

		if (file.exists()) {
			file.delete();
		}
		file.createNewFile();

		FileOutputStream outputStream = new FileOutputStream(file);
		outputStream.write(outputBytes);
		outputStream.flush();
		outputStream.close();

	}

}