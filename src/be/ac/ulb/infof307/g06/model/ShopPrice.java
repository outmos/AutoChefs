package be.ac.ulb.infof307.g06.model;

import javafx.scene.image.ImageView;

/**
 * Lien entre un prix et un shop.
 * @author Glyptodon
 *
 */
public class ShopPrice{
	/**
	 * prix total
	 */
	private double price;
	/**
	 * shop private variable
	 */
	final private Shop shop;

	/**
	 * Constructeur 
	 */
	public ShopPrice(final String name, final String newAddress, final double price) {
		shop = new Shop(name, newAddress);
		this.setPrice(price);
	}
	
	/**
	 * Constructeur
	 * @param shop
	 * @param price
	 */
	public ShopPrice(final Shop shop, final double price) {
		this.shop = new Shop(shop);
		this.setPrice(price);
	}
	

	/**
	 * getter du prix du shop
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * setter du prix shop
	 */
	public void setPrice(final double price) {
		this.price = price;
	}
	/**
	 * getter pour nom
	 * @return name du shop
	 */
	public String getName(){
		return shop.getName();
	}
	/**
	 * getter pour l'addresse
	 * @return address du shop
	 */
	public String getAddress(){
		return shop.getAddress();
	}
	/**
	 * set l'url du logo
	 * @param logoUrl
	 */
	public void setLogoUrl(final String logoUrl){
		shop.setLogoUrl(logoUrl);
	}
	/**
	 * get l'url du logo.
	 * @return logoUrl du shop
	 */
	public String getLogoUrl(){
		return shop.getLogoUrl();
	}
	/**
	 * set le logo
	 * @param image
	 */
	public void setLogo(final ImageView image){
		shop.setLogo(image);
	}
	/**
	 * get le logo de l'image
	 * @return 
	 * 
	 */
	public ImageView getLogo(){
		return shop.getLogo();
	}
	/**
	 * getter pour le shop
	 * @return shop
	 */
	public Shop getShop(){
		return new Shop(shop);
	}
}
