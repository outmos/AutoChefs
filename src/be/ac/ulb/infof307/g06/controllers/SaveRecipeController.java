package be.ac.ulb.infof307.g06.controllers;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Affiche la page pour sauvegarder une recette
 */
public class SaveRecipeController extends SceneManager implements Initializable  {
	
	private RecipeDataAccessor rda = RecipeDataAccessor.getInstance(); 
	private ObservableList<Recipe> rlist;
	Recipe mainRecipe;

	
	@FXML
    private Label userName;
	
	@FXML
    private Button logoutButton;
	
	@FXML
    private Button backButton;
	
	@FXML
    private Button saveButton;
	
	@FXML
    private Button deleteButton;
	
	@FXML 
	private TableView<Recipe> tableView;
	
	@FXML
    private TableColumn<Recipe, String> columnRecipeName;
	
	@FXML
    private TableColumn<Recipe, Integer> columnNbPerson;	
	
	@FXML
    private TextField nameField;	
	
	/**
	 * Setter pour la recette this.recipe
	 * @param recipe
	 */
	public void setRecipe(Recipe recipe){
		mainRecipe = recipe;
	}
	
	/**
	 * initialise la page de sauvegarde d'une recette
	 * @param url 
	 * @param rb 
	 */
	public void initialize(URL url, ResourceBundle rb){
		mainRecipe = new Recipe();
		userName.setText(SceneManager.getUserName());
		
		rlist = FXCollections.observableArrayList();
		try {
			refresh();
		} catch (DataAccessorException e) {
			printError(e.getMessage());
			return;
		}
		columnRecipeName.setCellValueFactory(new PropertyValueFactory<Recipe,String>("name"));
		columnNbPerson.setCellValueFactory(new PropertyValueFactory<Recipe,Integer>("nb_person"));
	    tableView.setItems(rlist);

	}
	
	/**
	 * rafraichit la liste des recettes sauvegarder
	 * @throws DataAccessorException 
	 */
	private void refresh() throws DataAccessorException {
		rlist.clear();
		List<Recipe> ll = rda.getRecipesNamesNbPersonInDB(SceneManager.getUserID());
		for (Recipe r : ll) {
			rlist.add(r);
		}
	}

	/**
	 * sauvegarde la recette selectionne dans la base de donnee
	 */
	private void saveInDB() {
		boolean temp = false;
		while (! temp) {
			try {
				rda.addRecipe(mainRecipe, SceneManager.getUserID());
			}
			catch ( SQLException e) {
				printError("Impossible d'enregistrer cette recette");
			}
			temp = true;
		}
	}
	/**
	 * bouton sauvegarde d'une recette et verfifie si toutes les conditions
	 * sont bonnes
	 * @param event 
	 * @throws IOException 
	 */
	public void savePressed(ActionEvent event) throws IOException  {
		int tmp = tableView.getSelectionModel().getSelectedIndex();
		if (!nameField.getText().isEmpty()) {
			if (checkExistName(nameField.getText())) {
				printError("Nom deja pris");
			}
			else {
				mainRecipe.setName(nameField.getText());
				saveInDB();
				back(event);
			}
		}
		else {
			if (tmp == -1) {
				printError("Entrer un nom ou selectionner une recette a ecraser");
			}
			else {
				Alert alert = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment ecraser " + tableView.getSelectionModel().getSelectedItem().getName() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
				alert.showAndWait();
				
				if (alert.getResult() == ButtonType.YES) {
					String name = rlist.get(tmp).getName();
					try{
						rda.deleteRecipe(rlist.get(tmp).getId());
					} catch(Exception e){

					}
					mainRecipe.setName(name);
					saveInDB();
					back(event);
				}
			}
		}
	}
	
	/**
	 * supprime une recette et met a jour la liste des recettes
	 * @param event 
	 * @throws IOException 
	 */
	public void deletePressed(ActionEvent event) throws IOException {
		if (tableView.getSelectionModel().getSelectedItem()!=null){
			Alert alert = new Alert(AlertType.CONFIRMATION, "Supprimer " + tableView.getSelectionModel().getSelectedItem().getName() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
			alert.showAndWait();

			if (alert.getResult() == ButtonType.YES) {
				RecipeDataAccessor rda = RecipeDataAccessor.getInstance();
				int tmp = tableView.getSelectionModel().getSelectedIndex();
				try{
					rda.deleteRecipe(rlist.get(tmp).getId());
					refresh();
				} catch(DataAccessorException e){
					printError(e.getMessage());
				}
			}
		}
	}
	
	@Override
	/**
	 * Retourne a la page precedente
	 */
	public void back(ActionEvent event) throws IOException{
		setPreviousView(event);
		RecipeMainController controller = getLoader().getController();
    	controller.setRecipe(mainRecipe);
		loadView();
	}
	
	/**
	 * regarde dans la base de donnee si le nom de la recette
	 * a sauvegarde n'est pas deja dans la base de donnee
	 */
	private boolean checkExistName(String nn) {
		for (Recipe r : rlist) {
			if (r.getName().equals(nn)) {
				return true;
			}
		}
		
		return false;
	}
	
}
