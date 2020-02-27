package be.ac.ulb.infof307.g06.controllers;

import java.net.URL;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import be.ac.ulb.infof307.g06.database.DataAccessorException;
import be.ac.ulb.infof307.g06.database.ParserProduct;
import be.ac.ulb.infof307.g06.database.ParseProductException;
import be.ac.ulb.infof307.g06.database.ShopProductDataAccessor;
import be.ac.ulb.infof307.g06.model.Product;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.scene.control.TextField;
import be.ac.ulb.infof307.g06.model.Shop;
import be.ac.ulb.infof307.g06.utils.Constants;

/**
 * Class qui gere la vue permettant de ajouter/modifier les produits 
 * dans un magasin.
 */
public class AddProductController extends SceneManager implements Initializable {
	int shopId = Constants.NO_SHOPID;
	ShopProductDataAccessor spda = ShopProductDataAccessor.getInstance();
	ParserProduct parser = null;
	
	@FXML
    private Button backButton;
	
	@FXML
	protected Label userName;
	
	@FXML
    private Button browserButton;
	
	@FXML
    private Button chargeFileButton;
	
	@FXML
    private Button saveButton;
	
	@FXML
	protected ComboBox<String> productsComboBox;
	
	@FXML
	private ComboBox<String> shopsComboBox;
	
	@FXML
	protected TextField pathTextField;
	
	@FXML
	protected TextField priceTextField;
	

	
	/*
	 * Initialise la liste des produits et la liste des magasins qui vont servir pour les menu deroulants
	 * Elle hérite de la fonction initialize de java.fxml.Initializable
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	public void initialize(URL url, ResourceBundle rb){
		userName.setText(SceneManager.getUserName());	
		
		List<Product> productsInDB = null;
		List<Shop> shopsInDB = null;
		try {
			productsInDB = spda.getProductsList();
			shopsInDB = spda.getShopsList();
		} catch (DataAccessorException e) {
			printError(e.getMessage());
		}
		initIngredient(productsInDB);
		initShop(shopsInDB);
		
		TextFields.bindAutoCompletion(productsComboBox.getEditor(), productsComboBox.getItems());
		TextFields.bindAutoCompletion(shopsComboBox.getEditor(), shopsComboBox.getItems());

	}
	
	
	
	/**
	 * Methode initialisant le menu des magasins
	 * @param shopsInDB
	 */
	private void initShop(List<Shop> shopsInDB) {
		ObservableList<String> list = FXCollections.observableArrayList();
        for(int i=0; i<shopsInDB.size(); i++){
            list.add(shopsInDB.get(i).getName());
        } 
        
        shopsComboBox.setItems(list);
	}
	
	/**
	 * Methode initialisant le menu des ingredients
	 * @param productsInDB
	 */
	protected void initIngredient(List<Product> productsInDB) {
		ObservableList<String> list = FXCollections.observableArrayList();
        for(int i=0; i<productsInDB.size(); i++){
            list.add(productsInDB.get(i).getName());
        } 
     
        productsComboBox.setItems(list);
	}

	/**
	 * Methode permettant la souvegarde de la modification/ajout 
	 * du produit.
	 * @param event
	 */
	public void save(ActionEvent event) {
		if(!(priceTextField.getText().length()>0 && productsComboBox.getSelectionModel().getSelectedItem()!=null)){
            printError("Entree invalide");
            return;
		}
        String productName = productsComboBox.getSelectionModel().getSelectedItem();
        String shopName = shopsComboBox.getSelectionModel().getSelectedItem();
        Double price;
        try{
        	price = Double.valueOf(priceTextField.getText());
        } catch (Exception e){
        	printError("Valeur incorrecte pour le prix");
        	return;
        }
        
        try{
        	int prodId = spda.getProdIdFromName(productName);
        	List<Integer> shopIds = spda.getShopIdFromName(shopName);
        	Product product = new Product(prodId, productName);
        	for (int i=0; i<shopIds.size();i++){
        		Shop shop = new Shop(shopIds.get(i));
        		saveOrModify(shop, product, price);
        	}
        } catch (DataAccessorException e) {
			printError(e.getMessage());
        } 
	}
	
	
	/**
	 * Methode qui effectue la sauvegarde/modification du produit selon
	 * son existance dans le magasin
	 * 
	 * @param shop
	 * @param product
	 * @param price
	 */
	protected void saveOrModify(Shop shop, Product product, double price) {
		try {
			if (spda.getShopPrice(shop, product) == -1) {
	   			spda.saveMapProdStore(shop, product, price);
	   			printInfo("Votre produit a ete sauvegarde");	
			}
			else {
				spda.changePriceForProdShop(shop, product, price);
				printInfo("Votre produit a ete modifie");
			}
		} catch (DataAccessorException e) {
				printError(e.getMessage());
		}
	}
	
	/**
	 * Methode pour choisir le fichier 
	 * contenant les modifications aux produits.
	 * @param event
	 */
	public void browserButtonPressed(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			pathTextField.setText(file.getAbsolutePath());
		}
	}
	
	/**
	 * Methode de la confirmation de l'upload 
	 * du file avec les modifications aux produits.
	 * @param event
	 */
	public void chargePressed(ActionEvent event) {
		parser = ParserProduct.getInstance();
		try{
			if (shopId == Constants.NO_SHOPID) {
				parser.parserFile(pathTextField.getText());
			}
			else {
				parser.parserFile(pathTextField.getText(), shopId);
			}
			printInfo("Base de donnee modifiee avec succes");
		} catch (FileNotFoundException e) {
			printError("Impossible de trouver le fichier");
		} catch (ParseProductException e) {
			printError(e.getMessage());
		} catch (Exception e){
			printError("Erreur d'insertion des produits");
		}
		
	}
}
