package be.ac.ulb.infof307.g06.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import be.ac.ulb.infof307.g06.database.DataAccessorException;
import be.ac.ulb.infof307.g06.database.RecipeDataAccessor;
import be.ac.ulb.infof307.g06.model.Recipe;
import be.ac.ulb.infof307.g06.model.RecipeList;
import be.ac.ulb.infof307.g06.model.ShoppingList;

/**
 * Affiche la page des plannifications des repas pour la semaine
 */
public class PlanificationController extends SceneManager implements Initializable {

	private static final List<String> week = Arrays.asList("lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi",
			"dimanche");
	private static final List<String> meals = Arrays.asList("petit dejeuner", "dejeuner", "diner");

	private ObservableList<String> recipeList;
	private RecipeList recipes;
	private List<Label> mealList;

	@FXML
	private Label userName;
	
	@FXML
    private Button backButton;

	@FXML
	private Label monday1, monday2, monday3;
	@FXML
	private Label tuesday1, tuesday2, tuesday3;
	@FXML
	private Label wednesday1, wednesday2, wednesday3;
	@FXML
	private Label thursday1, thursday2, thursday3;
	@FXML
	private Label friday1, friday2, friday3;
	@FXML
	private Label saturday1, saturday2, saturday3;
	@FXML
	private Label sunday1, sunday2, sunday3;

	@FXML
	private ComboBox<String> dayBox;
	@FXML
	private ComboBox<String> mealBox;
	@FXML
	private ComboBox<String> recipeBox;

	@FXML
	private Button confirmButton;

	@FXML
	private Button shoppingListButton;
	
	
	
	
	@Override
	/** 
	 * initialise la page de planification de recette pour une semaine
	 * */
	public void initialize(URL url, ResourceBundle rb) {
		userName.setText(SceneManager.getUserName());
		recipeList = FXCollections.observableArrayList();
		initDayBox();
		initMealBox();
		initRecipe();
		initMealList();

		TextFields.bindAutoCompletion(dayBox.getEditor(), dayBox.getItems());
		TextFields.bindAutoCompletion(mealBox.getEditor(), mealBox.getItems());
		TextFields.bindAutoCompletion(recipeBox.getEditor(), recipeBox.getItems());

	}

	/**
	 * initialise le menu qui permet a l'utilisateur de choisir le jour
	 * Pour changer la langue il faudrait changer la valeur des objets dans
	 * la liste PlanificationController.week
	 */
	private void initDayBox() {

		List<String> listU = new ArrayList<String>();
		ObservableList<String> listUnit = FXCollections.observableArrayList(listU);
		for(String day : PlanificationController.week) {
			listUnit.add(day);
		}
		dayBox.setItems(listUnit);
		dayBox.setValue(PlanificationController.week.get(0));

	}

	/**
	 * initialise le menu qui permet a l'utilisateur de choisir le repas
	 * Pour changer la langue il faudrait changer la valeur des objets dans
	 * la liste PlanificationController.meals
	 */
	private void initMealBox() {
		List<String> listU = new ArrayList<String>();
		ObservableList<String> listUnit = FXCollections.observableArrayList(listU);
		for(String meal : PlanificationController.meals) {
			listUnit.add(meal);
		}
		mealBox.setItems(listUnit);
		mealBox.setValue(PlanificationController.meals.get(0));

	}
	
	/** 
	 * charge et met les recettes dans les tranches horaires de la planification
	 * */
	private void initRecipe() {

		RecipeDataAccessor rda = RecipeDataAccessor.getInstance();
		try{
			List<Recipe> listrecipes = rda.getRecipesNamesNbPersonInDB(SceneManager.getUserID());

			for (Recipe r : listrecipes) {

				recipeList.add(r.getName());

			}
			recipes = new RecipeList(listrecipes);
			recipeBox.setItems(recipeList);
		} catch( DataAccessorException e){

		}
	}
	
	/** 
	 * initialise les labels
	 * */
	private void initMealList() {
		mealList = new ArrayList<Label>();
		mealList.add(monday1);
		mealList.add(monday2);
		mealList.add(monday3);
		mealList.add(tuesday1);
		mealList.add(tuesday2);
		mealList.add(tuesday3);
		mealList.add(wednesday1);
		mealList.add(wednesday2);
		mealList.add(wednesday3);
		mealList.add(thursday1);
		mealList.add(thursday2);
		mealList.add(thursday3);
		mealList.add(friday1);
		mealList.add(friday2);
		mealList.add(friday3);
		mealList.add(saturday1);
		mealList.add(saturday2);
		mealList.add(saturday3);
		mealList.add(sunday1);
		mealList.add(sunday2);
		mealList.add(sunday3);
	}
	
	/**
	 * Renvoie la position du label associe au repas donne par le jour et le repas selectionne
	 * */
	private int inputToInt() {
		return week.indexOf(dayBox.getSelectionModel().getSelectedItem())*3 + meals.indexOf(mealBox.getSelectionModel().getSelectedItem());
	}
	
	/**
	 * Ajoute le repas dans le tableau
	 * @param event
	 */
	public void confirmSelection(ActionEvent event) {

		if (dayBox.getSelectionModel().getSelectedItem() != null
				&& mealBox.getSelectionModel().getSelectedItem() != null
				&& recipeBox.getSelectionModel().getSelectedItem() != null) {
			mealList.get(inputToInt()).setText(recipeBox.getSelectionModel().getSelectedItem());
		}
	}

	/**
	 * 
	 * Methode qui renvoi la liste de course total par rapport à la planification 
	 * @throws DataAccessorException 
	 */
	private ShoppingList getTotalShoppingList() throws DataAccessorException {
		ShoppingList res = new ShoppingList();
		RecipeDataAccessor rda = RecipeDataAccessor.getInstance();
		for (Label l : mealList) {
			if (!l.getText().isEmpty()) {
				res.addList(rda.fillRecipe(recipes.searchId(l.getText())).getIngredients());
			}
		}
		return res;
	}


	
	/** 
	 * genere la liste de course a partir de la planification faites
	 * @param event 
	 * @throws IOException 
	 * */
	public void generateShoppingList(ActionEvent event) throws IOException {
		ShoppingList sl;
		try {
			sl = getTotalShoppingList();
		} catch (DataAccessorException e) {
			printError(e.getMessage());
			return;
		}
		if (sl.getProductsList().isEmpty()){
			printError("Aucune case remplie");
			return;
		}
		setNextView("/views/ShoppingListView.fxml", "Recette", event);
		FXMLLoader loader = getLoader();
		ShoppingListController controller = loader.getController();
		controller.setShoppingList(sl);
		loadView();
	}

	

}
