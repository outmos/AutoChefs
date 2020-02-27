package be.ac.ulb.infof307.g06.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Liste de recettes
 * @author Glyptodon
 *
 */
public class RecipeList {
	/**
	 * Liste de recette
	 */
	private List<Recipe> recipes;

	/**
	 * Constructeur 
	 */
	public RecipeList() {
		recipes = new ArrayList<Recipe>();
	}
	
	/**
	 * Constructeur
	 * @param newRecipes
	 */
	public RecipeList(final List<Recipe> newRecipes) {
		recipes = newRecipes;
	}
	
	/**
	 * getter de la liste des recettes
	 */
	public List<Recipe> getRecipes() {
		final List<Recipe> newListRecipe = new ArrayList<Recipe>();
		for(final Recipe tmpRecipe:recipes){
			newListRecipe.add(new Recipe(tmpRecipe)); // NOPMD by Glyptodon on 5/22/18 3:04 PM
		}
		return newListRecipe;
	}

	/**
	 * setter de la liste des recettes
	 * @param recipes
	 */
	public void setRecipes(final List<Recipe> recipes) {
		this.recipes = recipes;
	}
	
	/**
	 * Renvoie l'id d'une recette a partir de son nom.
	 * Renvoie -1 si il n'y a pas de recette associee au nom
	 * @param name
	 */
	public int searchId(final String name) {
		
		for (final Recipe recipe : recipes) {
			if (recipe.getName().equals(name)) { // NOPMD by Glyptodon on 5/22/18 3:06 PM
				return recipe.getId(); // NOPMD by Glyptodon on 5/22/18 3:06 PM
			}
		}
		return -1;
	}
	
	
}
