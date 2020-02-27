package be.ac.ulb.infof307.g06.controllers;


import java.io.IOException;import java.net.URL;import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.Control;

import org.controlsfx.control.textfield.TextFields;

import be.ac.ulb.infof307.g06.database.DataAccessorException;
import be.ac.ulb.infof307.g06.database.NutritionalValueDataAccessor;
import be.ac.ulb.infof307.g06.database.ShopProductDataAccessor;
import be.ac.ulb.infof307.g06.model.Product;
import be.ac.ulb.infof307.g06.model.Shop;
import be.ac.ulb.infof307.g06.model.ShopPrice;
import be.ac.ulb.infof307.g06.model.ShoppingList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Controller pour la view de la liste des courses
 */
public class ShoppingListController extends CourseListeController implements Initializable{
   
	private ShopProductDataAccessor shopOp = ShopProductDataAccessor.getInstance();
	private NutritionalValueDataAccessor nvda = NutritionalValueDataAccessor.getInstance();
	
    private ObservableList<Product> productList;
    static private ShoppingList shoppinglist = new ShoppingList();

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
    private TableColumn<Product, Double> columnVN; 
     
    @FXML 
    private TableColumn<Product, Double> columnKcal; 
     
    @FXML 
    private TableColumn<Product, Double> columnCarbo; 
     
    @FXML 
    private TableColumn<Product, Double> columnLipid; 
     
    @FXML 
    private TableColumn<Product, Double> columnProtein;
   
    @FXML
    private Button deleteButton;
   
    @FXML
    private Button saveButton;
    
    @FXML
    private Button cheapestShopButton; 
    
    @FXML
    private Button nearestShopButton;
    
    @FXML
    private Button addNewButton;
    
    @FXML
    private Button priceButton;
    
    
    /**
     * Constructeur par defaut
     * @throws IOException
     */
    public ShoppingListController() throws IOException {}  
    
    /**
     * initialise les boutons et les tableaux de la page permettant 
	 * de faire sa liste de course. Initialise aussi le menu deroulant des quantite dont
	 * l'utilisateur a besoin quand il
     * veut ajouter un produit à la base de donnees.
	 */
    @Override
    public void initialize(URL url, ResourceBundle rb){
    	userName.setText(SceneManager.getUserName());
    	
    	productList = FXCollections.observableArrayList();
        //shoppinglist = new ShoppingList();
        try{
        	List<Product> produits = shopOp.getProductsList();
        	initIngredient(produits);
        } catch(DataAccessorException e){
        	printError(e.getMessage());
        }
        initQuantity();
        textNumber.setText("0");
        
        TextFields.bindAutoCompletion(ingredientList.getEditor(), ingredientList.getItems());
        
        columnProduct.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        columnQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        columnKcal.setCellValueFactory(new PropertyValueFactory<>("kcal")); 
        columnProtein.setCellValueFactory(new PropertyValueFactory<>("proteins")); 
        columnLipid.setCellValueFactory(new PropertyValueFactory<>("lipids")); 
        columnCarbo.setCellValueFactory(new PropertyValueFactory<>("glucids"));
        tableView.setItems(productList);
        refresh();
       
    }
    
    /**
	 * initialise la liste de shopping
     * @param newShoppingList 
	 */
    public void setShoppingList( ShoppingList newShoppingList) {
    	shoppinglist = newShoppingList;
    	refresh();
    }
    
    /**
	 * initialise les quantites des produits
	 */
    private void initQuantity(){
    	
        List<String> listU = new ArrayList<String>();
        ObservableList<String> listUnit = FXCollections.observableArrayList(listU);
        listUnit.add("Kg.");
        listUnit.add("L.");
        listUnit.add("Pcs.");
    }

    
    /**
     * Initialise tous les produits disponibles dans la base de
	 * donnee et les parametre dans le menu deroulant 
	 */
    private void initIngredient(List<Product> produits){
    	
        ObservableList<String> list = FXCollections.observableArrayList();
        for(int i=0; i<produits.size(); i++){
            list.add(produits.get(i).getName());
        }   
        ingredientList.setItems(list);
    }
    
    /** 
     * Bouton qui permet d'ajouter un produit dans la base
	 * de donnee
     * @param event 
     * @throws IOException 
	 */
    public void addDB(ActionEvent event) throws IOException{
        loadNextScene("/views/AddProductView.fxml","Ajouter un Produit", event);
    }
    
     
    
    /** 
     * Calcul le prix total de la liste de course avec tous les produits
	 * selectionnee. Quitte la page pour le creation d'une liste de course
	 * et affiche la view "shops" avec le prix total et les informations 
	 * des magasins correspondant
     * @param event 
     * @throws IOException 
	 */
    public void priceTotal(ActionEvent event) throws IOException{
            
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ShopsView.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        ShopController controller = loader.getController();
       
        List<ShopPrice> lstShops;
		try {
			lstShops = getShopsForShoppingList();
		} catch (DataAccessorException e) {
			printError(e.getMessage());
			return;
		} 
       
        ObservableList<ShopPrice> shopList = FXCollections.observableArrayList(lstShops);
        controller.setShopList(shopList);
        stage.show();
        
    }
    
    
    /**
     * Affiche le magasin le moins cher, ou, le magasin le plus proche
     * pour une liste de course donnees.
     * @param event 
     * @throws IOException 
     */
    public void nearestOrCheapestShop(ActionEvent event) throws IOException{
    	
    	String id = ((Control)event.getSource()).getId();
    	FXMLLoader loader = new FXMLLoader();
    	Parent root;
    	Stage stage = new Stage();
    	
    	List<ShopPrice> lstShops;
    	List<Shop> s =  new ArrayList<>();
    	
    	try {
    		lstShops = getShopsForShoppingList();
		} catch (DataAccessorException e) {
			printError(e.getMessage());
			return;
		}
    	
    	try{
        	lstShops.get(0);
        }catch(IndexOutOfBoundsException e){
        	printError("Veuillez ajouter des produits à la liste !");
        	return;
        }
    	
    	switch(id){
	    	case("nearestShopButton"):
	    		for(ShopPrice shop : lstShops){
	    			s.add(shop.getShop());
	    		}
	    	
	    		loader.setLocation(getClass().getResource("/views/NearestShopView.fxml"));
		    	NearestShopController ctrl1 = new NearestShopController(s);
	    		loader.setController(ctrl1);
	    		root = loader.load();
	    		stage.setScene(new Scene(root));
	    		
	    		break;
	    		
	    	case("cheapestShopButton"):
	    		s.add(lstShops.get(0).getShop());
	    	
	    		loader.setLocation(getClass().getResource("/views/CheapestShopView.fxml"));
	    		CheapestShopController ctrl2 = new CheapestShopController(s);
	    		loader.setController(ctrl2);
	    		root = loader.load();
	    		stage.setScene(new Scene(root));
	    		
	    		break;
	    		
	    	default:
	    		return;
    	}
    	
    	stage.show();
    }
    
    
    
	/** 
	 * Renvoie tous les magasins qui contiennent tous les produits de la liste
	 * @throws DataAccessorException
	 */
    private List<ShopPrice> getShopsForShoppingList() throws DataAccessorException {
    	
    	List<Product> listProducts = new ArrayList<Product>();
        for (Product product : productList) {
            listProducts.add(product);
        }
        List<ShopPrice> lstShops = shopOp.getShopsForShopping(listProducts);
    	
        return lstShops ; 
    }

	/** 
	 * Après avoir selectionne un produit dans le menu deroulant, ouvre une
     * view qui affiche tous les magasins avec le prix du produit selectionne 
	 * @param event 
	 * @throws IOException 
     */
    public void checkPrice(ActionEvent event) throws IOException{
          
        if(!(textNumber.getText().length()>0 && ingredientList.getSelectionModel().getSelectedItem()!=null)){
            printError("Entree invalide");
        }
        else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ShopsView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            ShopController controller = loader.getController();
            try{
            	List<ShopPrice> shopz = shopOp.selectShopsWhereProdExists(new Product(ingredientList.getSelectionModel().getSelectedItem(), nvda.getUnitOfProduct(ingredientList.getSelectionModel().getSelectedItem())));
            	ObservableList<ShopPrice> slist = FXCollections.observableArrayList(shopz);
            	controller.setShopList(slist);
            } catch (Exception e){

            }
            stage.show();
        }
    }
    
    /**
     * bouton "-" : incremente de 1 la quantite d'un produit sélectionne
     * @param event 
     * @throws IOException 
     */
    public void plusPressed(ActionEvent event) throws IOException{
    	if (shoppinglist.size() != 0){
    		int tmp = super.plusPressed(shoppinglist, tableView);
    		if (tmp != -1)
    			refresh(tmp);
    	}

    }
    
    
    /**
     * bouton "-" : decremente de 1 la quantite d'un produit sélectionne
     * @param event 
     * @throws IOException 
     */
    public void minusPressed(ActionEvent event) throws IOException{
    	if (shoppinglist.size() != 0){
    		int tmp = super.minusPressed(shoppinglist, tableView);
    		if (tmp != -1)
    			refresh(tmp);
    	}

    }
    
    
    private void refresh(int index) {
    	productList.set(index, shoppinglist.get(index));
    }
    
    
    private void refresh() {
    	productList.clear();
    	for (int index = 0; index < shoppinglist.size(); ++index) {
        	productList.add(shoppinglist.get(index));
    	}
    }

    
    /** 
     * Rajoute le produit selectionne avec sa quantite correspondante 
     * a la liste de course se trouvant dans le tableau.
	 */
    public void addPressed(){
    	super.addPressed(textNumber, shoppinglist, ingredientList,productList);
    }
    
    
    /** 
     * Supprime l'element selectionne dans la liste de courses.
     */
    public void deletePressed() {	
    	super.deletePressed(productList, shoppinglist, tableView);
    }
}
