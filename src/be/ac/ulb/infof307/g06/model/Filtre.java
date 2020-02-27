package be.ac.ulb.infof307.g06.model;

import java.util.ArrayList;
import java.util.List;

import be.ac.ulb.infof307.g06.database.DataAccessorException;
import be.ac.ulb.infof307.g06.database.FilterDataAccessor;
import be.ac.ulb.infof307.g06.database.ShopProductDataAccessor;

/**
 * This class handle the model part of the filters
 * @author Glyptodon
 *
 */
public class Filtre { // NOPMD by Glyptodon on 5/22/18 1:06 PM
	/**
	 * nom du shop recherche
	 */
	private String nameShop; // NOPMD by Glyptodon on 5/22/18 1:12 PM
	/**
	 * jour recherche
	 */
	private String day; // NOPMD by Glyptodon on 5/22/18 1:13 PM
	/**
	 * heure recherche
	 */
	private String hour; // NOPMD by Glyptodon on 5/22/18 1:13 PM
	/**
	 * bio characteristique recherche
	 */
	private Boolean bio ; // NOPMD by Glyptodon on 5/22/18 1:13 PM
	/**
	 * veg characteristique recherche
	 */
	private Boolean veg ; // NOPMD by Glyptodon on 5/22/18 1:13 PM
	/**
	 * vegan characteristique recherche
	 */
	private Boolean vegan  ; // NOPMD by Glyptodon on 5/22/18 1:13 PM
	/**
	 * local characteristique recherche
	 */
	private Boolean local ; // NOPMD by Glyptodon on 5/22/18 1:14 PM
	
	/**
	 * Constructeur
	 * @param nameShop
	 * @param day
	 * @param hour
	 * @param bio
	 * @param veg
	 * @param vegan
	 * @param local
	 */
	public Filtre(final String nameShop, final String day, final String hour,
			 final Boolean bio, final Boolean veg, final Boolean vegan, final Boolean local){
		this.nameShop=nameShop;
		this.day=day;
		this.hour=hour;
		this.bio=bio;
		this.veg=veg;
		this.vegan=vegan;
		this.local=local;
	}
	/**
	 * getter nameShop
	 * @return nameShop
	 */
	public String getNameShop(){
		return this.nameShop;
	}
	/**
	 * getter day
	 * @return day
	 */
	public String getDay(){
		return this.day;
	}
	/**
	 * getter hour
	 * @return hour
	 */
	public String getHour(){
		return this.hour;
	}
	/**
	 * getter bio
	 * @return bio
	 */
	public Boolean getBio(){
		return this.bio;
	}
	/**
	 * getter veg
	 * @return veg
	 */
	public Boolean getVeg(){
		return this.veg;
	}
	/**
	 * getter vegan
	 * @return vegan
	 */
	public Boolean getVegan(){
		return this.vegan;
	}
	/**
	 * getter local
	 * @return local
	 */
	public Boolean getLocal(){
		return this.local;
	}
	/**
	 * Private function that add one hour, day, name string to the string.
	 * @param nameValue
	 * @param valueToAdd
	 * @return String
	 */
	private String addToStr(final String nameValue, final String valueToAdd){
		if (valueToAdd != null){
			return nameValue+" "+valueToAdd+";"; // NOPMD by Glyptodon on 5/22/18 1:21 PM
		}
		return "";
	}
	/**
	 * Get correct string value
	 * @param value
	 * @return
	 */
	private String addToStrBoolean(final Boolean value){
		if (value){
			return "1;"; // NOPMD by Glyptodon on 5/22/18 1:21 PM
		}
		return	"0;";
		
	}
	
	/**
	 *  Renvoie la recherche faite par l'utilisateur
	 */
	public String getResearch(){
		
		String str=""; 
		str+=addToStr("name", nameShop); // NOPMD by Glyptodon on 5/22/18 1:22 PM
		str += addToStr("day", day); // NOPMD by Glyptodon on 5/22/18 1:22 PM
		str+= addToStr("hour", hour); // NOPMD by Glyptodon on 5/22/18 1:22 PM
		str += addToStrBoolean(bio); // NOPMD by Glyptodon on 5/22/18 1:22 PM
		str += addToStrBoolean(veg); // NOPMD by Glyptodon on 5/22/18 1:22 PM
		str += addToStrBoolean(vegan); // NOPMD by Glyptodon on 5/22/18 1:22 PM
		str += addToStrBoolean(local); // NOPMD by Glyptodon on 5/22/18 1:22 PM
		return str;
	}
	
	/**
	 * empty constructor pour createFiltreFromResearch 
	 */
	public Filtre() { //empty
	}
	
	/**
	 *  Cree un filtre a partir de la recherche sauvegardee par l'utilisateur
	 */
	public static Filtre createFiltreFromResearch(final String research){
		
		String nameShop = null ;
		String day =  null ;
		String hour = null;
		Boolean bio = false; 
		Boolean veg = false ;
		Boolean vegan = false ;
		Boolean local  = false ;
		
		final int length = research.length()-1 ; 
		final int lengthCaracterisques = 7 ;  // NOPMD by Glyptodon on 5/22/18 1:31 PM
		final int indexCaracteristiques = length-8 ; // NOPMD by Glyptodon on 5/22/18 1:31 PM
		final String[] characteristiques = research.substring(indexCaracteristiques+1  , length).split(";") ; // NOPMD by Glyptodon on 5/22/18 1:31 PM
		String[] filters = null ; 
		if ( indexCaracteristiques  >  lengthCaracterisques) {
			 filters = research.substring(0,indexCaracteristiques ).split(";") ; // NOPMD by Glyptodon on 5/22/18 1:31 PM
		}
		
		bio = characteristiques[0].contentEquals("1"); // NOPMD by Glyptodon on 5/22/18 1:34 PM
		veg = characteristiques[1].contentEquals("1") ; // NOPMD by Glyptodon on 5/22/18 1:34 PM
		vegan = characteristiques[2].contentEquals("1"); // NOPMD by Glyptodon on 5/22/18 1:34 PM
		local = characteristiques[3].contentEquals("1"); // NOPMD by Glyptodon on 5/22/18 1:34 PM
		
		if (filters != null) {
			for (final String tmpStr : filters) {
				final String[] temp = tmpStr.split(" ") ; 
				if (temp[0].contentEquals("name")) {nameShop = temp[1] ; } // NOPMD by Glyptodon on 5/22/18 1:36 PM
				
				if (temp[0].contentEquals("day")) {day = temp[1] ;} // NOPMD by Glyptodon on 5/22/18 1:37 PM
				
				if (temp[0].contentEquals("hour")) {hour = temp[1] ; } // NOPMD by Glyptodon on 5/22/18 1:37 PM
			}
		}
		return new Filtre(nameShop, day, hour, bio, veg, vegan,local) ; 
	}
	
	/**
	 *  Renvoie les magasins qui correspondent à la recherche faite pour le user 
	 * @throws DataAccessorException 
	 */
	public List<Shop> createListShop() throws DataAccessorException{ // NOPMD by Glyptodon on 5/22/18 1:39 PM
    	final ArrayList<String> characteristics = new ArrayList<String>();
    	try{
			final ShopProductDataAccessor spda = ShopProductDataAccessor.getInstance();
			final FilterDataAccessor fda = FilterDataAccessor.getInstance();
			
			List<Shop> listShop = spda.getShopsList(); // NOPMD by Glyptodon on 5/22/18 1:40 PM
			int counter = 1;
			
			if( this.getDay()!=null && this.getHour()==null){
				List<Shop> dayResult = new ArrayList<Shop>();
				dayResult = fda.filterDay(spda.getShopsList(), this.getDay()); // NOPMD by Glyptodon on 5/22/18 1:40 PM
				listShop.addAll(dayResult); // NOPMD by Glyptodon on 5/22/18 1:40 PM
				counter++;
			}
			if(this.getNameShop()!=null && !this.getNameShop().contentEquals("")) {
				List<Shop> nameResult = new ArrayList<Shop>();
				try{
					nameResult = fda.selectShopsByName(this.getNameShop()) ; // NOPMD by Glyptodon on 5/22/18 1:42 PM
					listShop.addAll(nameResult); // NOPMD by Glyptodon on 5/22/18 1:42 PM
					counter++;
				} catch(Exception e){ // NOPMD by Glyptodon on 5/22/18 1:42 PM
		
				}
			}
			if ( this.getDay()!=null && !this.getDay().contentEquals("") &&  this.getHour()!=null) {
				
				final List<Shop> shops = spda.getShopsList();  // NOPMD by Glyptodon on 5/22/18 1:45 PM
				final List<Shop> openShop = fda.filterDayAndTime(shops,this.getHour(),this.getDay()); // NOPMD by Glyptodon on 5/22/18 1:45 PM
				listShop.addAll(openShop); // NOPMD by Glyptodon on 5/22/18 1:45 PM
				counter++;
			}
			if (this.getBio()) {
				characteristics.add("bio") ; 
			}
			if (this.getVeg()) {
				characteristics.add("veg") ; 
			}
			if (this.getVegan()) {
				characteristics.add("vegan") ; 
			}
			if (this.getLocal()) {
				characteristics.add("local") ; 
			}
			if ( !(characteristics.isEmpty()) ) {
				try{
					final List<Shop> listTmp = fda.selectShopsByCharacteristics(characteristics); // NOPMD by Glyptodon on 5/22/18 1:45 PM
					listShop.addAll(listTmp); // NOPMD by Glyptodon on 5/22/18 1:45 PM
					counter++;
				}catch(Exception e){ // NOPMD by Glyptodon on 5/22/18 1:45 PM
		
				}
			}
			listShop=combineListShop(listShop, counter);
			
			return listShop; // NOPMD by Glyptodon on 5/22/18 1:45 PM
    	} catch (DataAccessorException e){
    		return new ArrayList<Shop>();
    	}
    }
	
	/**
	 *  Renvoie la liste contenant les magasins en commun
	 *  en fonction de la recherche de l'utilisateur
	 */
	private List<Shop> combineListShop(final List<Shop> listShop, final int counter){
    	
    	final List<Shop> newListShop = new ArrayList<Shop>();
    	int tmpCounter=1;
    	for(int i=0; i<listShop.size(); i++){
    		tmpCounter=1;
    		for(int j=i+1; j<listShop.size(); j++){
    			if(listShop.get(i).getName().contentEquals(listShop.get(j).getName())){ // NOPMD by Glyptodon on 5/22/18 1:47 PM
    				tmpCounter++;
    			}
    		}
    		if(tmpCounter==counter){
				newListShop.add(listShop.get(i));
			}
    	}
    	return newListShop;
    }
	
}
