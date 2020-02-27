package be.ac.ulb.infof307.g06.model;

/**
 * classe pour les recettes
 * @author Glyptodon
 *
 */
public class Recipe {
	
	/**
	 * nom de la recette
	 */
	private String name ;
	/**
	 * id de la recette
	 */
	private int idRecipe ; // NOPMD by Glyptodon on 5/22/18 2:48 PM
	/**
	 * ingredients de la recette
	 */
	private ShoppingList ingredients;
	/**
	 * liste instruction
	 */
	private String operations;
	/**
	 * nombre de personne
	 */
	private int nbPerson;
	
	/**
	 * Constructeur 
	 */
	public Recipe(){
		ingredients = new ShoppingList();
		nbPerson = 1;
	}
	
	/**
	 * Constructeur 
	 * @param name
	 */
	public Recipe(final String name) {
		this.name = name;
		ingredients = new ShoppingList();
	}
	
	/**
	 * Constructeur
	 * @param name
	 * @param idRecipe
	 * @param operations
	 * @param nbPerson
	 */
	public Recipe(final String name,final int idRecipe,final String operations,final int nbPerson){
		this.name = name;
		this.idRecipe = idRecipe;
		this.operations = operations;
		this.nbPerson = nbPerson;
		ingredients = new ShoppingList();
	}
	
	/**
	 * Constructeur
	 * @param recipeID
	 */
	public Recipe(final int recipeID) {
		this.setId(recipeID);
		ingredients = new ShoppingList();
	}
	
	/**
	 * Copie constructeur
	 * @param tmpRecipe
	 */
	public Recipe(final Recipe tmpRecipe) {
		this.name = tmpRecipe.getName();
		this.idRecipe = tmpRecipe.getId();
		this.operations = tmpRecipe.getOperations();
		this.nbPerson = tmpRecipe.getNbPerson();
		ingredients = tmpRecipe.getIngredients();
	}

	/**
	 * getter des ingredients d'une recette
	 */
	public ShoppingList getIngredients() {
		return new ShoppingList(ingredients);
	}
	
	/**
	 * setter des ingredients d'une recette
	 * @param ingredients
	 */
	public void setIngredients(final ShoppingList ingredients) {
		this.ingredients = ingredients;
	}
	
	/**
	 * ajoute un ingredient a une recette
	 * @param product
	 */
	public void addProduct(final Product product){
		ingredients.add(product);
	}
	
	/**
	 * getter des operations d'une recette
	 */
	public String getOperations() {
		return operations;
	}
	
	/**
	 * setter des operations d'une recette
	 * @param operations
	 */
	public void setOperations(final String operations) {
		this.operations = operations;
	}
	
	/**
	 * getter du nom d'une recette
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * setter du nom d'une recette
	 * @param name
	 */
	public void setName(final String name) {
		this.name = name;
	}
	
	/**
	 * getter de l'id d'une recette
	 */
	public int getId() {
		return idRecipe;
	}
	
	/**
	 * setter de l'id d'une recette
	 * @param idRecipe
	 */
	public void setId(final int idRecipe) {
		this.idRecipe = idRecipe;
	}
	
	/**
	 * getter du nombre de personne pour une recette
	 */
	public int getNbPerson() {
		return nbPerson;
	}
	
	/**
	 * setter du nombre de personne pour une recette
	 * @param newNbPerson
	 */
	public void setNbPerson(final int newNbPerson) {
		nbPerson = newNbPerson;
	}
	
	/**
	 * change la quantite des produits d'une recette en fonction
	 * du nombre de personne
	 * @param newNbPerson
	 */
	public void changeQuantity(final int newNbPerson) {
		if (newNbPerson <= 0) {
			return ;
		}
		ingredients.changeQuantity((double) newNbPerson/nbPerson);
		setNbPerson(newNbPerson);
	}
	
}