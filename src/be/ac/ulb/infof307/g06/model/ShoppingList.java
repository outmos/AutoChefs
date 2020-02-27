package be.ac.ulb.infof307.g06.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe pour les liste de course
 * @author Glyptodon
 *
 */

public class ShoppingList {
	/**
	 * list de produits dans la liste de course
	 */
	private List<Product> productsList;

	/**
	 * Constructeur 
	 */
	public ShoppingList() {
		productsList = new ArrayList<Product>();
	}
	
	/**
	 * setter de la liste de produits  
	 */
	public void setProductsList(final List<Product> newList) {
		productsList = newList;
	}
	
	/**
	 * cpy constructeur
	 */
	public ShoppingList(final ShoppingList shopList){
		this.productsList=shopList.getProductsList();
	}
	/**
	 * getter de la liste de produits  
	 */
	public List<Product> getProductsList() {
		final List<Product> newList= new ArrayList<Product>();
		for (final Product product:productsList){
			newList.add(new Product(product)); // NOPMD by Glyptodon on 5/22/18 12:49 PM
		}
		return newList;
	}
	
	/**
	 * getter du nombre de produits de la liste
	 */
	public int size() {
		return productsList.size();
	}
	
	/**
	 * getter d'un produit a l'indice i  
	 */
	public Product get(final int index) {
		return productsList.get(index);
	}
	
	/**
	 * supprime le produit de la liste a l'indice i  
	 */
	public boolean remove(final int index) {
		Boolean res = true;
		try {
			productsList.remove(index);
		}
		catch (IndexOutOfBoundsException e) {
			res = false;
		}
		return res;
	}
	
	/**
	 * ajoute un produit a la liste
	 */
	public void add(final Product product) {
		final int index = getIndexOf(product);
		if (index == -1) {
			productsList.add(product);
		}
		else {
			productsList.get(index).incQuantity(product.getQuantity()); // NOPMD by Glyptodon on 5/22/18 12:53 PM
		}
	}
	
	/**
	 * ajoute une liste de produits de type ShoppingList
	 */
	public void addList(final ShoppingList shoppingList) {
		for (final Product product : shoppingList.getProductsList()) {
			productsList.add(product);
		}
	}
	
	/**
	 * incremente la quantite d'un produit
	 */
	public void inc(final int index) {
		productsList.get(index).incQuantity(1); // NOPMD by Glyptodon on 5/22/18 12:54 PM
	}
	
	/**
	 * decremente la quantite d'un produit
	 */
	public void dec(final int index) {
		productsList.get(index).decQuantity(1); // NOPMD by Glyptodon on 5/22/18 12:56 PM
	}
	
	/**
	 * getter de l'indice dans la liste d'un produit
	 */
	public int getIndexOf(final Product product) {
		return getIndexOf(product.getName());
	}
	
	/**
	 * getter de l'indice dans la liste d'un produit a partir de son nom
	 */
	public int getIndexOf(final String product) {
		for (int i = 0; i < productsList.size(); ++i) {
			if (productsList.get(i).getName().equalsIgnoreCase(product)) { // NOPMD by Glyptodon on 5/22/18 12:57 PM
				return i; // NOPMD by Glyptodon on 5/22/18 12:58 PM
			}
		}
		return -1;
	}

	/**
	 * change la quantite d'un produit de la liste en
	 * le multipliant par une facteur
	 */
	public void changeQuantity(final double factor) {
		for (final Product product: productsList) {
			product.mulQuantity(factor);
		}
	}
	
}

