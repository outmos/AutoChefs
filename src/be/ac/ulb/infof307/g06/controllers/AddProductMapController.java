package be.ac.ulb.infof307.g06.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import be.ac.ulb.infof307.g06.database.DataAccessorException;
import be.ac.ulb.infof307.g06.model.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import be.ac.ulb.infof307.g06.model.Shop;
import be.ac.ulb.infof307.g06.utils.BridgeSingleton;

/**
 * Class permettant de ajouter/modifier les produits 
 * dans un magasin en le selectionnant sur la carte.
 */
public class AddProductMapController extends AddProductController implements Initializable {
	
	
	
	BridgeSingleton bridge = BridgeSingleton.getInstance();
	
	@FXML
	protected Label userName;
	
	@FXML
    private Button saveButton;

    @FXML
    private ComboBox<String> productsComboBox;

    @FXML
    private TextField pathTextField;
    
    @FXML
    private Label indication;

    @FXML
    private TextField priceTextField;
	
	@FXML
    private Button chargeFileButton;
	
	@FXML
    private Button browserButton;
	
	/*
	 * Initialise la liste des produits qui vont servir pour les menu deroulants
	 * Elle hérite de la fonction initialize de java.fxml.Initializable
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	public void initialize(URL url, ResourceBundle rb){
		userName.setText(SceneManager.getUserName());	
		
		List<Product> productsInDB = null;
		
		try {
			productsInDB = spda.getProductsList();
			
		} catch (DataAccessorException e) {
			printError(e.getMessage());
		}
		initIngredient(productsInDB);
				
		TextFields.bindAutoCompletion(productsComboBox.getEditor(), productsComboBox.getItems());
		
		try {
			shopId = spda.getShopIdFromCoordinates(bridge.getConsideredShop().get(0), bridge.getConsideredShop().get(1));
		} catch (DataAccessorException e) {
			printError(e.getMessage());
		}
		
	}
	
	/**
	 * Methode permettant la souvegarde de la modification 
	 * du produit.
	 * @param event
	 */
	public void save(ActionEvent event) {
		if(!(priceTextField.getText().length()>0 && productsComboBox.getSelectionModel().getSelectedItem()!=null)){
            printError("Entree invalide");
        } else {
        	String productName = productsComboBox.getSelectionModel().getSelectedItem();
        	Double price = Double.valueOf(priceTextField.getText());
        	int prodId;
			try {
				prodId = spda.getProdIdFromName(productName);
	        	Product product = new Product(prodId, productName);
	        	Shop shop = new Shop(shopId);
	        	saveOrModify(shop, product, price);
			} catch (DataAccessorException e1) {
				printError(e1.getMessage());
			}
        }
	}
	
	

}
