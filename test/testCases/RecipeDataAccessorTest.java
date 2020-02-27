package testCases;
import static org.junit.Assert.*;

import java.sql.SQLException;
import be.ac.ulb.infof307.g06.model.Recipe;
import be.ac.ulb.infof307.g06.database.DataAccessor;
import be.ac.ulb.infof307.g06.database.DataAccessorException;
import be.ac.ulb.infof307.g06.database.RecipeDataAccessor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RecipeDataAccessorTest {
	Recipe recipe;
	RecipeDataAccessor rda;

	@Before
	public void setUp() throws Exception {
        DataAccessor.setPassword("root");
		rda = RecipeDataAccessor.getInstance();
		recipe = new Recipe("addDelete", 0, "", 1);
	}

	@After
	public void tearDown() throws Exception {
		rda = null;
		recipe = null;
	}
	
	@Test
	public void getRecipesNamesNbPersonInDBTest(){
		try{
			assertEquals(2,(rda.getRecipesNamesNbPersonInDB(0)).size());
		} catch (DataAccessorException e){
			fail("Not expected error");
		}		
	}

	@Test
	public void addThenDeleteRecipeTest() {
		try{
			rda.addRecipe(recipe, 0);
		}catch (SQLException e){
			fail("Not expected error");
		}
		try{
			assertEquals(3,(rda.getRecipesNamesNbPersonInDB(0)).size());
			rda.deleteRecipe(recipe.getId());
			assertEquals(2,(rda.getRecipesNamesNbPersonInDB(0)).size());
		} catch (DataAccessorException e){
			fail("Not expected error");
		}
	}

	@Test
	public void fillRecipeTest() {
		Recipe recipe = null;
		try{
			recipe = rda.fillRecipe(999);
		}catch (DataAccessorException e){
			fail("Not expected error");
		}
		
		assertEquals(10,recipe.getNbPerson());
		assertEquals("test_operations",recipe.getOperations());
		
	}
	
}
