package be.ac.ulb.infof307.g06.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement; 

/** Classe de base pour toute classe qui devra se connecter a la base de donnees .
 *  Toutes les classes qui utilisent la connexion a la base de donnees devront heriter de cette classe
 */
public class DataAccessor {
	private static String password = null;
	private static final DataAccessor INSTANCE = new DataAccessor();
	protected DataAccessor(){
		
	}
	public static DataAccessor getInstance() {
        return INSTANCE;
    }
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();  
		} catch (Exception ex) {

		}
	}
	
	/**
	 * connexion a notre base de donnees
	 * @param s : mot de passe de la base de donnee
	 */
	public static void setPassword(String s) {
		password = s;
	}
	
	/**
	 * Renvoie un objet Connection pour notre DB
	 * @throws DataAccessorException
	 * @return java.sql.Connection : objet connection de la base de donnee
	 */
	protected static Connection getConnection() throws DataAccessorException { 
		Connection conn;
		try{
            conn=DriverManager.getConnection("jdbc:mysql://localhost/data?autoReconnect=true&useSSL=false", "root", password);
        }
        catch (SQLException e) {
        	throw new DataAccessorException(DataAccessorException.DB_ACCESS);
        }
		return conn;
	}
	/**
	 * Teste si DataAccessor.password est le bon mot de passe pour le user root de la base de donnees
	 * @return True si la connection a la base de donnee est reussi. Sinon False
	 */
	public static boolean testConnection() {
		try {
			DriverManager.getConnection("jdbc:mysql://localhost/data?autoReconnect=true&useSSL=false", "root", password);
			return true;
		}
		catch (SQLTimeoutException eto) {

			return false;
		}
		catch (SQLException e) {

			return false;
		}
	}
	
	/**
	 * ferme les connections et les PreparedStatement apres une requete
	 * @param st : bjet preparedStatement pour l'envoi d'instructions SQL parametrees � la base de donnees.
	 * @param conn : objet connection de la base de donnee
	 */
	protected void finallyHandler(PreparedStatement st, Connection conn){
		if (st != null){
			try {
				st.close();
			} catch (Exception ex) {
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
	}
	
	
	/**
	 * Supprime la base de donnee
	 * @return True si la base de donnees est supprimee. False sinon
	 * @throws SQLException
	 */
	public boolean dropDataBase() throws SQLException {
		boolean res =true;
		Connection conn = getConnection();
		
		Statement stmt = conn.createStatement();
	    String sql = "DROP SCHEMA data";
	    try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			res = false;
		}
	    return res;
		
		
	}
	
}