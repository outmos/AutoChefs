package be.ac.ulb.infof307.g06.database;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import be.ac.ulb.infof307.g06.model.Product;
import be.ac.ulb.infof307.g06.model.Recipe;
import be.ac.ulb.infof307.g06.model.ShoppingList;

public final class RecipeDataAccessor  {

	private DataAccessor dAccessor = DataAccessor.getInstance();
	private static final RecipeDataAccessor INSTANCE = new RecipeDataAccessor();
	private RecipeDataAccessor(){
	}
	public static RecipeDataAccessor getInstance(){
		return INSTANCE;
	}
	
	/**
	 * permet de recuperer les noms et le nombre de personnes associe des recettes
	 * dans la base de donnees
	 * @param userId : l'identifiant de l'utilisateur actuellement connecte
	 * @return la liste de recettes avec toutes les informations demandees
	 * @throws DataAccessorException 
	 */
	public List<Recipe> getRecipesNamesNbPersonInDB(int userId) throws DataAccessorException {
		List<Recipe> recipes = new ArrayList<>();
		Recipe r = null;
		Connection conn = DataAccessor.getConnection();
		PreparedStatement stRecipes = null;
		ResultSet rs = null;

		try {

			stRecipes = conn.prepareStatement("SELECT idrecipe, name, nb_person from recipe where iduser=?"); // requ�te
			stRecipes.setInt(1, userId);
			rs = stRecipes.executeQuery();

			while (rs.next()) {
				r = new Recipe(rs.getString(2));
				r.setId(rs.getInt(1));
				r.setNbPerson(rs.getInt(3));
				recipes.add(r);
			}
		} catch (SQLException e) {
			throw new DataAccessorException(DataAccessorException.WRONG_REQUEST);
		} finally {
			dAccessor.finallyHandler(stRecipes, conn);
		}
		return recipes;
	}
	
	/**
	 * permet de recuperer l'id des produits pour une liste de courses
	 * @param sl : une liste de courses
	 * @return une liste avec tous les identifiants des produits
	 * @throws DataAccessorException 
	 */
	private List<Integer> getIdProducts(ShoppingList sl) throws DataAccessorException {
		List<Integer> res = new ArrayList<Integer>();
		Connection connection = DataAccessor.getConnection();
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			for (Product p : sl.getProductsList()) {
				String req = "SELECT idproduct FROM product WHERE name='" + p.getName() + "'";
				statement = connection.prepareStatement(req);
				rs = statement.executeQuery();
				rs.next();
				res.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			throw new DataAccessorException(DataAccessorException.WRONG_REQUEST);
		} finally {
			dAccessor.finallyHandler(statement, connection);
		}

		return res;
	}
	
	/**
	 * rajoute une recette dans la base donnees avec 
	 * toutes ses donnees correspondantes
	 * @param recipe : la recette en question
	 * @param idUser : l'identifiant de l'utilisateur actuellement connecte, auquel appartiennent les recettes
	 */
	public boolean addRecipe(Recipe recipe, int idUser) throws SQLException {
		Connection connection = DataAccessor.getConnection();
		PreparedStatement stRecipe = null;
		ResultSet rs = null;
		connection.setAutoCommit(false);
		stRecipe = connection.prepareStatement("insert into recipe set name=?, iduser=?, operations=?, nb_person=?",
				Statement.RETURN_GENERATED_KEYS);
		stRecipe.setString(1, recipe.getName());
		stRecipe.setInt(2, idUser);
		stRecipe.setString(3, recipe.getOperations());
		stRecipe.setInt(4, recipe.getNbPerson());

		stRecipe.executeUpdate();
		connection.commit();
		int autoIncKeyFromApi = -1;
		rs = stRecipe.getGeneratedKeys();
		if (rs.next()) {
			autoIncKeyFromApi = rs.getInt(1);
		}
		recipe.setId(autoIncKeyFromApi);

		
		if (recipe.getIngredients()!=null){
			Statement statement = connection.createStatement();
			List<Integer> idList = getIdProducts(recipe.getIngredients());
			for (int i = 0; i < recipe.getIngredients().getProductsList().size(); ++i) {
				statement.addBatch("insert into map_recipe_product set recipe_id=" + recipe.getId() + ", product_id="
						+ idList.get(i) + ", quantity=" + recipe.getIngredients().getProductsList().get(i).getQuantity());
			}
			statement.executeBatch();
			statement.close();
		}
		
		
		connection.commit();
		connection.close();
		return true;
	}
	
	/**
	 * Remplit les donnees correspondantes d'une recette dans la base de donnees
	 * @param recipeID : l'identifiant de la recette a remplir
	 * @throws DataAccessorException 
	 */
	public Recipe fillRecipe(int recipeID) throws DataAccessorException {
		Recipe recipe = new Recipe(recipeID);
		Product product = null;
		Connection conn = DataAccessor.getConnection();
		PreparedStatement stRecipe = null;
		ResultSet rs = null;

		try {
			conn.setAutoCommit(false);
			stRecipe = conn.prepareStatement(
					"select r.idrecipe, r.operations, p.name, p.unit, m.quantity, r.nb_person, p.kcal, p.lipid, p.protein, p.glucid"
							+ " from recipe r, product p, map_recipe_product m "
							+ "where r.idrecipe = m.recipe_id and p.idproduct=m.product_id and r.idrecipe=?");
			stRecipe.setInt(1, recipeID);

			rs = stRecipe.executeQuery();

			int i = 0;
			while (rs.next()) {
				if (i == 0) {
					recipe.setOperations(rs.getString(2));
					recipe.setNbPerson(rs.getInt(6));
				}
				product = new Product(rs.getString(3), rs.getString(4), rs.getDouble(5), rs.getDouble(7),
						rs.getDouble(9), rs.getDouble(8), rs.getDouble(10));
				
				recipe.addProduct(product);
				i++;
			}

		} catch (SQLException e) {
			throw new DataAccessorException(DataAccessorException.WRONG_REQUEST);
		} finally {
			dAccessor.finallyHandler(stRecipe, conn);
		}
		return recipe;
	}

	/**
	 * Supprime une recette dans la base de donnee
	 * @param recipeId : l'identifiant de la recette a supprimer
	 * @throws DataAccessorException 
	 */
	public void deleteRecipe(int recipeId) throws DataAccessorException {
		Connection conn = DataAccessor.getConnection();
		PreparedStatement stDeleteMap = null;
		PreparedStatement stDeleteRec = null;

		try {
			conn.setAutoCommit(false);
			stDeleteMap = conn.prepareStatement("delete from map_recipe_product where recipe_id=?");
			stDeleteMap.setInt(1, recipeId);
			stDeleteRec = conn.prepareStatement("delete from recipe where idrecipe=?");
			stDeleteRec.setInt(1, recipeId);

			stDeleteMap.addBatch();
			stDeleteRec.addBatch();
		} catch (SQLException e) {
			throw new DataAccessorException(DataAccessorException.WRONG_REQUEST);
		}
		
		try {
			stDeleteMap.executeBatch();
			stDeleteRec.executeBatch();

			conn.commit();
		}
		catch (SQLException e) {
			throw new DataAccessorException(DataAccessorException.NOT_IN_DB);
		}

		 finally {
			dAccessor.finallyHandler(stDeleteMap, conn);
			dAccessor.finallyHandler(stDeleteRec, conn);
		}
	}

}
