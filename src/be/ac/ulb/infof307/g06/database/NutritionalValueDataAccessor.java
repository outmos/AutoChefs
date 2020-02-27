package be.ac.ulb.infof307.g06.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class NutritionalValueDataAccessor {
	private static final NutritionalValueDataAccessor INSTANCE = new NutritionalValueDataAccessor();
	private DataAccessor dataAccessor = DataAccessor.getInstance();

	private NutritionalValueDataAccessor() {
	}

	public static NutritionalValueDataAccessor getInstance() {
		return INSTANCE;
	}


	/**
	 * Renvoie la valeur nutritionnelle d'un produit passee en parametre.
	 * @param prod : produit 
	 * @param row : valeur nutritionnelle d'un produit
	 * @return valeur nutritionnelle
	 * @throws DataAccessorException
	 */
	private String getRowOfProduct(String prod, String row) throws DataAccessorException {

		Connection conn = DataAccessor.getConnection();
		PreparedStatement stProdShops = null;
		ResultSet rs = null;

		try {
			stProdShops = conn.prepareStatement("SELECT p." + row + " FROM product p WHERE p.name=?"); // la requete
			stProdShops.setString(1, prod);
			rs = stProdShops.executeQuery();
			rs.next();
			return rs.getString(1);

		} catch (SQLException e) {
			throw new DataAccessorException(DataAccessorException.WRONG_REQUEST);
		} finally {
			dataAccessor.finallyHandler(stProdShops, conn);
		}

	}


	public String getUnitOfProduct(String prod) throws DataAccessorException {
		return getRowOfProduct(prod, "unit");
	}


	public double getKcalOfProduct(String prod) throws NumberFormatException, DataAccessorException {
		return Double.valueOf(getRowOfProduct(prod, "kcal"));
	}


	public double getLipidOfProduct(String prod) throws NumberFormatException, DataAccessorException {
		return Double.valueOf(getRowOfProduct(prod, "lipid"));
	}

	public double getProteinOfProduct(String prod) throws NumberFormatException, DataAccessorException {
		return Double.valueOf(getRowOfProduct(prod, "protein"));
	}


	public double getGlucidOfProduct(String prod) throws NumberFormatException, DataAccessorException {
		return Double.valueOf(getRowOfProduct(prod, "glucid"));
	}

}
