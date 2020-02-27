package be.ac.ulb.infof307.g06.controllers;

import java.io.IOException;

import be.ac.ulb.infof307.g06.database.NutritionalValueDataAccessor;
import be.ac.ulb.infof307.g06.model.Product;
import be.ac.ulb.infof307.g06.model.ShoppingList;
import be.ac.ulb.infof307.g06.utils.Constants;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;

/**
 * Gere l affiche d'une liste de course ainsi que les fonctionnalite liees a la modification de celle-ci:
 * ajouter un produit
 * retirer un produit
 * modifier la quantite d un produit
 */
public abstract class CourseListeController extends SceneManager{
	
	/** bouton "-" : decremente de 1 la quantite d'un produit selectionne 
	 * @param shoppinglist 
	 * @param tableView 
	 * @return l'index de l'element modife, -1 si la liste est vide
	 * @throws IOException */
    protected int minusPressed(ShoppingList shoppinglist, TableView<Product> tableView) throws IOException{
    	return touchPressed(shoppinglist,tableView,Constants.MINUS);
    }
    
    /** bouton "-" : incremente de 1 la quantite d'un produit selectionne 
	 * @param shoppinglist 
	 * @param tableView 
	 * @return l'index de l'element modife, -1 si la liste est vide
	 * @throws IOException */
    protected int plusPressed(ShoppingList shoppinglist, TableView<Product> tableView) throws IOException{
    	return touchPressed(shoppinglist,tableView,Constants.PLUS);
    }
    
    /**
     * Incremente ou decremente en fonction de "signe" la quantite du produit selectionne dans "tableView"
     * @param shoppinglist
     * @param tableView
     * @param signe
     * @return l'index de l'element modife, -1 si la liste est vide
     */
    protected int touchPressed(ShoppingList shoppinglist, TableView<Product> tableView,int signe){
		TableViewSelectionModel<Product> tmp = tableView.getSelectionModel();
		if (!tmp.isEmpty()) {
			int index = tmp.getSelectedIndex();
    		if (signe == Constants.PLUS)
    			shoppinglist.inc(index);
    		else
    			shoppinglist.dec(index);
    		return index;
		}
    	return -1;

    }
    
    /**
     * Renvoie la valeur de la quantite donnee en entree
     * @param textNumber
     * @return -1 si l'entree est invalide 
     */
    private double getInputQt(TextField textNumber) {
    	
    	if (textNumber.getText().length()<1)
    		return -1;
    	double qt;
    	try {
    		qt = Double.parseDouble(textNumber.getText());
    	}
    	catch (NumberFormatException e) {
    		qt = -1;
    	}
    	if (qt <= 0) {
    		qt = -1;
    	}
    	return qt;
    }
    
    /** Supprime l'element selectionne dans la liste de courses 
     * @param productList 
     * @param shoppinglist 
     * @param tableView */
    protected void deletePressed(ObservableList<Product> productList, ShoppingList shoppinglist, TableView<Product> tableView) {
    	if (productList.size()>0){
    		int tmp =tableView.getSelectionModel().getSelectedIndex();
    		shoppinglist.remove(tmp);
    		refresh(shoppinglist,productList);
    	}
    }
    
    /** Rajoute le produit selectionne avec sa quantite correspondante a la liste de course
	 * se trouvant dans le tableau
     * @param textNumber 
     * @param shoppinglist 
     * @param ingredientList 
     * @param productList */
    protected void addPressed(TextField textNumber, ShoppingList shoppinglist, ComboBox<String> ingredientList,ObservableList<Product> productList){
    	
    	double qt = getInputQt(textNumber);
    	String prodName = ingredientList.getSelectionModel().getSelectedItem();
        if (qt != -1 && prodName !=null){
        	NutritionalValueDataAccessor nvop = NutritionalValueDataAccessor.getInstance();
        	try{
        		shoppinglist.add( new Product(prodName, nvop.getUnitOfProduct(prodName),qt, nvop.getKcalOfProduct(prodName), nvop.getProteinOfProduct(prodName), nvop.getLipidOfProduct(prodName), nvop.getGlucidOfProduct(prodName)));
        	} catch(Exception e){

        	}
        	refresh(shoppinglist,productList);
        }
        else{
            printError("Entree Vide");
        }
    }
    
    /** Rafraichit la liste de produits de la liste de course 
     * @param shoppinglist 
     * @param productList */
    protected void refresh(ShoppingList shoppinglist, ObservableList<Product> productList) {
    	productList.clear();
    	for(Product p : shoppinglist.getProductsList()) {
    		productList.add(p);
    	}
    	
    }
}

