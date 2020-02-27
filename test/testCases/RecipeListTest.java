package testCases;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import be.ac.ulb.infof307.g06.model.Recipe;
import be.ac.ulb.infof307.g06.model.RecipeList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RecipeListTest {
    RecipeList recipeList;
    Recipe recipe;
	List<Recipe> recipes;

	@Before
	public void setUp() throws Exception {
		
        recipeList = new RecipeList();
        recipe = new Recipe("Tajine");
        recipes = new ArrayList<Recipe>();
        recipes.add(recipe);

        recipeList.setRecipes(recipes);
	}

	@After
	public void tearDown() throws Exception {
        recipeList = null;
		recipe = null;
	}

	@Test
	public void IsFoundIfReturnValueIsNotNegative(){
        // testing . . .
		assertEquals(recipe.getId(), recipeList.searchId(recipe.getName()));
	}
}
