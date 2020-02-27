package be.ac.ulb.infof307.g06.controllers;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import be.ac.ulb.infof307.g06.database.DataAccessorException;
import be.ac.ulb.infof307.g06.database.ShopProductDataAccessor;
import be.ac.ulb.infof307.g06.model.Product;
import be.ac.ulb.infof307.g06.model.Recipe;
import be.ac.ulb.infof307.g06.model.ShoppingList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/** Controller de la view qui affiche le prix totale apr�s avoir fait
 * une liste de course */
public class RecipeMainController extends CourseListeController implements Initializable{

	private ObservableList<Product> productList;
    private ShoppingList shoppingList;
    private Recipe recipe = null;

    private ShopProductDataAccessor spda =  ShopProductDataAccessor.getInstance();
    
    @FXML
    private Label userName;
    
    @FXML
    private ComboBox<String> ingredientList;
   
    @FXML
    private TextField textNumber;
    
    @FXML
    private Button backButton;
	
    @FXML
    private Button addButton;
    
    @FXML
    private TableView<Product> tableView;
   
    @FXML
    private TableColumn<Product, String> columnProduct;
   
    @FXML
    private TableColumn<Product, Integer> columnQuantity;
   
    @FXML
    private TableColumn<Product, String> columnUnit;
	
    @FXML
    private Button deleteButton;
	
	@FXML
    private Button saveButton;
	
	@FXML
    private Button loadRecipe;
	
	@FXML
	private Button shoppingListButton;
	
	@FXML
    private TextField numberOfPerson;
	
	@FXML
    private TextArea textArea;
	
	@FXML
    private Button resetButton;
	
	
	/**
	 * met en place la liste de shopping
	 * @param list 
	 */
	public void setShoppingList(ShoppingList list){
		shoppingList = list;
	}
	
	/**
	 * quand on a charger une recette deja cree, permet de charger
	 * et d'afficher les donnees sur la page
	 * @param rr 
	 */
	public void setRecipe(Recipe rr){
		recipe = rr;
    	super.refresh(shoppingList,productList);
    	productList = FXCollections.observableArrayList();
		
		initRecipe();
		try{
			List<Product> produits = spda.getProductsList();
			initIngredient(produits);
		} catch(DataAccessorException e){
			printError(e.getMessage());
		}
        textNumber.setText("0");
        
        TextFields.bindAutoCompletion(ingredientList.getEditor(), ingredientList.getItems()); 
        
        columnProduct.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        columnQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tableView.setItems(productList);

	}

	/**
	 * initialise les informations sur la page lie a une recette
	 */
	private void initRecipe() {
        shoppingList = recipe.getIngredients();
        textArea.setText(recipe.getOperations());
        numberOfPerson.setText(String.valueOf(recipe.getNbPerson()));
        refresh(shoppingList, productList);

	}
	
	@Override
	/**
	 * initilaise la page pour une recette
	 */
    public void initialize(URL url, ResourceBundle rb){
		userName.setText(SceneManager.getUserName());
		
		if(recipe == null){
			recipe = new Recipe();
		}

		productList = FXCollections.observableArrayList();
		initRecipe();           
        List<Product> produits=null;
		try {
			produits = spda.getProductsList();
		} catch (DataAccessorException e) {
			printError(e.getMessage());
		}
        initIngredient(produits);
        
        TextFields.bindAutoCompletion(ingredientList.getEditor(), ingredientList.getItems()); 
        
        columnProduct.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        columnQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tableView.setItems(productList);
    }
	
	
	/**Initialise tous les produits disponibles dans la base de
	 * donnee et les parametre dans le menu deroulant */
    private void initIngredient(List<Product> produits){
    	
        ObservableList<String> list = FXCollections.observableArrayList();
        for(int i=0; i<produits.size(); i++){
            list.add(produits.get(i).getName());
        }
        ingredientList.setItems(list);
    }
    
    private void refresh(int index) {
    	productList.set(index, shoppingList.get(index));
    }
    
    
    /** Rajoute le produit selectionne avec sa quantite correspondante a la liste de course
	 * se trouvant dans le tableau*/
    public void addPressed(){
    	super.addPressed(textNumber, shoppingList, ingredientList,productList);
    }
	
    /** Supprime l'element selectionne dans la liste de courses */
    public void deletePressed() {
    	super.deletePressed(productList, shoppingList, tableView);
    }
    
    /** bouton "+" : icremente de 1 la quantite d'un produit selectionne 
     * @param event 
     * @throws IOException 
     */
    public void plusPressed(ActionEvent event) throws IOException{
    	if (shoppingList.size() != 0 && tableView.getSelectionModel().getSelectedItem()!=null){
    		refresh(super.plusPressed(shoppingList, tableView));
    	}

    }
    
    
    /** 
     * bouton "-" : decremente de 1 la quantite d'un produit selectionne
     * @param event 
     * @throws IOException 
     */
    public void minusPressed(ActionEvent event) throws IOException{
    	if (shoppingList.size() != 0 && tableView.getSelectionModel().getSelectedItem()!=null){
    		refresh(super.minusPressed(shoppingList, tableView));	
    	}
    }
    
	/**
	 * reinitialise la page en enlevant toutes les donnees deja selectionne
	 */
    public void resetPressed() {
    	recipe = new Recipe();
    	initRecipe();
    }
	
	/**
	 * incremente le nombre de personnes pour la recette qui est en train
	 * d'etre creee
	 */
    public void plusPersonPressed() {
    	recipe.changeQuantity(Integer.valueOf(numberOfPerson.getText())+1);
    	refresh(shoppingList,productList);
    	numberOfPerson.setText(String.valueOf(recipe.getNbPerson()));

    }
    
	/**
	 *  decremente le nombre de personnes pour la recette qui est en train
	 * d'etre creee
	 */
    public void minusPersonPressed() {
    	recipe.changeQuantity(Integer.valueOf(numberOfPerson.getText())-1);
    	refresh(shoppingList,productList);
    	numberOfPerson.setText(String.valueOf(recipe.getNbPerson()));

    	
    }
    
	/**
	 * initialise la page de sauvegarde de la recette
	 * @param event 
	 * @throws IOException 
	 */
	public void saveRecipeInDB(ActionEvent event)throws IOException {
		int persons;
		try {
			persons = Integer.parseInt(numberOfPerson.getText());
		}
		catch (NumberFormatException e1)
		{
            printError("Nombre de personnes incorrect");
            return;
		}
		if (persons == 0) {
			printError("Nombre de personnes incorrect");
			return;
		}
		if(shoppingList.size()==0){
			printError("Il est impossible de sauvegarder une recette vide");
			return;
		}
		recipe.setNbPerson(persons);
		recipe.setIngredients(shoppingList);
		recipe.setOperations(textArea.getText());
		setNextView("/views/SaveRecipeView.fxml","Recette", event);
		FXMLLoader loader = getLoader();
		SaveRecipeController controller = loader.getController();
    	controller.setRecipe(recipe);
    	loadView();

	}
	
	/**
	 * Ouvre la fenetre shoppinglist avec les ingredients a acheter
	 * @param event
	 * @throws IOException
	 */
	public void generateShoppingList(ActionEvent event) throws IOException {
		setNextView("/views/ShoppingListView.fxml", "Recette", event);
		FXMLLoader loader = getLoader();
		ShoppingListController controller = loader.getController();
		controller.setShoppingList(shoppingList);
		loadView();
	}
	
	/**
	 * Quand on appuie sur charger, va sur la fenetre avec les recettes deja existantes 
	 * @param e 
	 * @throws IOException 
	 */
	public void loadRecipeInWindow(ActionEvent e) throws IOException{ 

		recipe.setOperations(textArea.getText());
		setNextView("/views/LoadRecipeView.fxml","Recette", e);
		LoadRecipeController controller = getLoader().getController();
    	controller.setRecipe(recipe);
    	
    	loadView();
	}
	
}
