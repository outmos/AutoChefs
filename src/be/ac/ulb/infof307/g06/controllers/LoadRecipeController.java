package be.ac.ulb.infof307.g06.controllers;
import java.io.IOException;import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import be.ac.ulb.infof307.g06.database.DataAccessorException;
import be.ac.ulb.infof307.g06.database.RecipeDataAccessor;
import be.ac.ulb.infof307.g06.model.Recipe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;

/** 
 *	Affiche la page pour charger une recette
 */
public class LoadRecipeController extends SceneManager implements Initializable  {
	
	private ObservableList<Recipe> rlist = FXCollections.observableArrayList();
	Recipe mainRecipe;
	
	@FXML
	private Label userName;
	
	@FXML
    private Button logoutButton;
	
	@FXML
    private Button backButton;
	
	@FXML
    private Button loadButton;
	
	@FXML 
	private TableView<Recipe> tableView;
	
	@FXML
    private TableColumn<Recipe, String> columnRecipeName;
	
	@FXML
    private TableColumn<Recipe,Integer> columnNbPerson;
	
	
	
	
	/** initialise la page de chargement de recette avec les recettes deja enregistrees
	 * @param url 
	 * @param rb 
	 * */
	public void initialize(URL url, ResourceBundle rb){
		mainRecipe = new Recipe();
		userName.setText(SceneManager.getUserName());
		RecipeDataAccessor rda = RecipeDataAccessor.getInstance();
		List<Recipe> ll = null;
		try {
			ll = rda.getRecipesNamesNbPersonInDB(SceneManager.getUserID());
		} catch (DataAccessorException e) {
			printError(e.getMessage());
		}
		for (Recipe r : ll) {
			rlist.add(r);
		}
		columnRecipeName.setCellValueFactory(new PropertyValueFactory<Recipe,String>("name"));
		columnNbPerson.setCellValueFactory(new PropertyValueFactory<Recipe,Integer>("nb_person"));
	    tableView.setItems(rlist);
	    
	}

	/** charge la recette selectionnee
	 * @param event 
	 * @throws IOException */
	public void loadRecipe(ActionEvent event) throws IOException  {
		int tmp = tableView.getSelectionModel().getSelectedIndex();
		RecipeDataAccessor rda = RecipeDataAccessor.getInstance();
		try{
			mainRecipe = rda.fillRecipe(rlist.get(tmp).getId());
		} catch(ArrayIndexOutOfBoundsException e){
			printError("Aucune recette selectionnee");
			return;
		} catch (DataAccessorException e) {
			printError(e.getMessage());
			return;
		}
		back(event);

	}
	
	
	@Override
	/** retourne a la page precedente (ici la page de creation/modification de recette)
	 * */
	public void back(ActionEvent event) throws IOException{
		setPreviousView(event);
		RecipeMainController controller = getLoader().getController();
    	controller.setRecipe(mainRecipe);
		loadView();

	}
	
	/**
	 * Setter pour la recette this.recipe
	 * @param recipe
	 */
	public void setRecipe(Recipe recipe){
		mainRecipe = recipe;
	}
	
	
	
}
