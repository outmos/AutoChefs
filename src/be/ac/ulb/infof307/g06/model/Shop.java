package be.ac.ulb.infof307.g06.model;
import javafx.scene.image.ImageView;

/** Classe qui represente l'objet magasin */
public class Shop { // NOPMD by Glyptodon on 5/22/18 1:54 PM
	
	/**
	 * id du shop
	 */
	private int idShop ; // NOPMD by Glyptodon on 5/22/18 1:54 PM
	/**
	 * nom du shop
	 */
	private String name;
	/**
	 * address du shop
	 */
	private String address;
	/**
	 * url du logo du shop
	 */
	private String logoUrl;
	/**
	 * logo du shop
	 */
	private ImageView logo;
	/**
	 * city dans lequel le shop est
	 */
	private String city ; 
	/**
	 * String des characteristiques
	 */
	private String caracteristics ;
	/**
	 * horaire d'ouverture
	 */
	private String openingHours ;
	/**
	 * lat du shop
	 */
	private double lattitude ; // NOPMD by Glyptodon on 5/22/18 1:54 PM
	/**
	 * long du shop
	 */
	private double longitude ; // NOPMD by Glyptodon on 5/22/18 1:54 PM
	private final static String TIMES = "00:00-00:00;00:00-00:00;00:00-00:00;00:00-00:00;00:00-00:00;00:00-00:00;00:00-00:00"; // NOPMD by Glyptodon on 5/22/18 1:56 PM
	private final static String CARACTERISTICS = "False;False;False;False"; // NOPMD by Glyptodon on 5/22/18 1:56 PM
	
	/**
	 * constructeur vide
	 */
	public Shop(){//empty constructor
	}
	
	/**
	 * Constructeur de base
	 * @param name
	 * @param address
	 * @param logoUrl
	 * @param city
	 * @param caracteristics
	 * @param openingHours
	 * @param lattitude
	 * @param longitude
	 */
	public Shop(final String name, final String address, final String logoUrl, final String city, final String caracteristics,
			final String openingHours,  final double lattitude, final double  longitude) {
		super();
		
		this.name = name;
		this.address = address;
		this.logoUrl = logoUrl;
		this.city = city;
		this.caracteristics = caracteristics.isEmpty() ? CARACTERISTICS : caracteristics ;
		this.openingHours = openingHours.isEmpty() ? TIMES : openingHours ;
		this.lattitude = lattitude;
		this.longitude = longitude;
	}
	/**
	 * copy constructor
	 * @param shop
	 */
	public Shop(final Shop shop){
		this.name=shop.getName();
		this.address=shop.getAddress();
		this.logoUrl=shop.getLogoUrl();
		this.city=shop.getCity();
		this.caracteristics=shop.getCaracteristics();
		this.openingHours=shop.getOpeningHours();
		this.lattitude=shop.getLatitude();
		this.longitude=shop.getLongitude();
	}
	

	/**
	 * Constructeur
	 * @param name
	 * @param newAddress
	 */
	public Shop(final String name, final String newAddress) {
		this.name = name;
		this.address = newAddress;
	}
	
	/**
	 * Constructeur
	 * @param name
	 * @param newAddress
	 * @param logoUrl
	 */
	public Shop(final String name, final String newAddress, final String logoUrl) {
		this.name = name;
		this.address = newAddress;
		this.logoUrl = logoUrl;
	}
	
	/**
	 * Constructeur 
	 * @param name
	 */
	public Shop(final String name) {
		this.name = name;
		
	}
	
	/**
	 * Constructeur
	 * @param shopId
	 * @param name
	 */
	public Shop(final int shopId, final String name) {
		this.name = name;
		this.idShop = shopId;
		
	}

	/**
	 * Constructeur
	 * @param shopId
	 */
	public Shop(final int shopId) {
		this.idShop = shopId;
	}

	/**
	 * getter du nom d'un shop 
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * getter d'une adresse d'un shop 
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * getter de l'id d'un shop 
	 */
	public int getId(){
		return idShop ; 
	}

	/**
	 * setter de l'id d'un shop
	 * @param newId
	 */
	public void setId(final int newId) {
		idShop = newId ; 
	}
	
	/**
	 * setter du nom d'un shop
	 * @param newName 
	 */
	public void setName(final String newName){
		name = newName;
		
	}
	
	/**
	 * seter de l'adresse d'un shop
	 * @param newAddress 
	 */
	public void setAddress(final String newAddress) {
		address = newAddress;
	}
	
	/**
	 * getter de l'url logo d'un shop 
	 */
	public String getLogoUrl() {
		return logoUrl;
	}

	/**
	 * setter de l'url logo d'un shop
	 * @param logoUrl 
	 */
	public void setLogoUrl(final String logoUrl) {
		this.logoUrl = logoUrl;
	}
	
	/**
	 * getter du logo d'un shop
	 */
	public ImageView getLogo() {
		return logo;
	}

	/**
	 * sette du logo d'un shop
	 * @param item 
	 */
	public void setLogo(final ImageView item) {
		this.logo = item;
	}


	/**
	 * getter pour city
	 * @return the city
	 */
	public String getCity() {
		return city;
	}


	/**
	 * setter city
	 * @param city the city to set
	 */
	public void setCity(final String city) {
		this.city = city;
	}


	/**
	 * getter characteristique
	 * @return the caracteristics
	 */
	public String getCaracteristics() {
		return caracteristics;
	}


	/**
	 * @param caracteristics the caracteristics to set
	 */
	public void setCaracteristics(final String caracteristics) {
		this.caracteristics = caracteristics;
	}


	/**
	 * @return the openingHours
	 */
	public String getOpeningHours() {
		return openingHours;
	}


	/**
	 * @param openingHours the openingHours to set
	 */
	public void setOpeningHours(final String openingHours) {
		this.openingHours = openingHours;
	}


	/**
	 * @return the lattitude
	 */
	public double getLatitude() {
		return lattitude;
	}


	/**
	 * @param lattitude the lattitude to set
	 */
	public void setLattitude(final double lattitude) {
		this.lattitude = lattitude;
	}


	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}


	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(final double longitude) {
		this.longitude = longitude;
	}
	
	
	
}
