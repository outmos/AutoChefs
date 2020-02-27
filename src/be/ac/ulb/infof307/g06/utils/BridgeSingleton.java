package be.ac.ulb.infof307.g06.utils;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import be.ac.ulb.infof307.g06.database.DataAccessorException;
import be.ac.ulb.infof307.g06.database.ShopProductDataAccessor;
import be.ac.ulb.infof307.g06.model.Shop;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

/**
 * @version 2.0
 * Class BridgeSingleton suit le Design Pattern Singleton.
 * Elle représente le pont entre le Java Core et la map en Javascript.
 * Toute opération en lien avec la map passe par cette class.
 */
public final class BridgeSingleton {
	/**
	 * Instance de la classe
	 */
	private static BridgeSingleton bridgeInstance;
	/**
	 * Coordonne geographique du magasin selectionne sur la map.
	 */
	private List<Double> consideredShop = new ArrayList<>();
	/**
	 * Objet javascript pour le bridge.
	 */
	private static JSObject jsObject;
	
	/**
	 * Container pour stocker des magasins utilises par le bridge
	 */
	private static List<Shop> shopContainer;
	
	private BridgeSingleton(){}
	
	/**
	 * Methode qui declare une nouvelle instance de BridgeSingleton.
	 * @return une instance du singleton Bridge.
	 */
	public synchronized static BridgeSingleton getInstance(){ // NOPMD by Glyptodon on 22/05/18 02:07
		if (bridgeInstance == null){
			bridgeInstance = new BridgeSingleton();
		}
		return bridgeInstance;
	}
	
	public void setShopContainer(final List<Shop> list){ // NOPMD by Glyptodon on 22/05/18 01:42
		BridgeSingleton.shopContainer = list;
	}
	
	/**
	 * Methode qui contient le magasin selectione sur la map.
	 * @return le shop selectione sur la map.
	 */
	public List<Double> getConsideredShop(){
		return consideredShop;
	}
	
	public void setConsideredShop(final List<Double> newConsideredList){ // NOPMD by Glyptodon on 22/05/18 01:42
		consideredShop = newConsideredList;
	}
	
	public void setJSObject(final JSObject new_js){ // NOPMD by Glyptodon on 22/05/18 01:42
		jsObject = new_js;
	}
	
	/**
	 * Methode permettant d'ajouter des produits a un magasin donne sur la map.
	 * @throws IOException
	 */
	public void addProducts() throws IOException{
		
		if (!consideredShop.isEmpty()){
			final Class<?> loaderClass = getClass();
			final URL url = loaderClass.getResource("/views/AddProductMapView.fxml"); // NOPMD by Glyptodon on 22/05/18 01:54
			final FXMLLoader loader = new FXMLLoader(url);
			
			final Parent root = loader.load();
			final Stage stage = new Stage();
	        stage.setScene(new Scene(root));
	        stage.show();
	     }
	        
       
	}
	
	/**
	 * Methode affichant le magasin le plus proche 
	 * de l'endroit sur lequel l'utilisateur clique sur la carte.
	 * @param origin
	 * @param mode
	 * @throws IOException 
	 */
	public void nearestShop(final String origin, final String mode) throws IOException{
		final ConcurrentHashMap<Shop, Integer> distances = new ConcurrentHashMap<>();
		Shop nearestShop;
		String destination = null;
		for(final Shop shop : shopContainer){
			destination = String.join(",", String.valueOf(shop.getLatitude()), String.valueOf(shop.getLongitude()));
			distances.put(shop, getFastestRoute(origin, destination, mode));
		}
		
		// recherche de la distance la plus petite. . .
		Entry<Shop, Integer> min = null;
		for (final Entry<Shop, Integer> entry : distances.entrySet()) {
		    if (min == null || min.getValue() > entry.getValue()) { // NOPMD by Glyptodon on 22/05/18 02:07
		        min = entry;
		    }
		}
		
		nearestShop = min.getKey(); // NOPMD by Glyptodon on 22/05/18 02:08
		
		final List<Shop> list = new ArrayList<>(Arrays.asList(nearestShop));
		removeMarkers();
		initializeMarkers(list);
		centerMapOnCoords(nearestShop.getLatitude(), nearestShop.getLongitude()); // NOPMD by Glyptodon on 22/05/18 02:08
	}

	/**
     * Trouve le plus court chemin en terme de distance,
     * entre deux points sur la carte.
     * @param coordinate1: Un string de format lat/long separes par une virgule.
     * @param coordinate2: Un string de format lat/long separes par une virgule.
     * @param mode : mode de transport utilise.
     * @return La distance du trajet en metres.
	 * @throws IOException 
     *
     */
    public int getFastestRoute(final String coordinate1, final String coordinate2, final String mode) throws IOException {
    	int distance = 0;
    	String buildUrl;
    	
    	buildUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?"
				   + "&origins="+coordinate1+"&destinations="+coordinate2+"&mode="+mode;

        BufferedReader reader = null;
        String line ="";
        final StringBuffer outputString = new StringBuffer("");

        
            final URL url = new URL(buildUrl);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET"); // NOPMD by Glyptodon on 22/05/18 02:11
            connection.setReadTimeout(15*10000); // NOPMD by Glyptodon on 22/05/18 02:11
            connection.connect(); // NOPMD by Glyptodon on 22/05/18 02:11

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); // NOPMD by Glyptodon on 22/05/18 02:11
            line = reader.readLine();
            while (line != null) {
                outputString.append(line);
                line = reader.readLine();
            }

            final JSONObject test = new JSONObject(outputString.toString());
            try{
            	final JSONObject jsonDistance = test.getJSONArray("rows") // NOPMD by Glyptodon on 22/05/18 02:14
                        .getJSONObject(0)
                        .getJSONArray ("elements")
                        .getJSONObject(0)
                        .getJSONObject("distance");
            	distance = (int)jsonDistance.get("value"); // NOPMD by Glyptodon on 22/05/18 02:14
            }catch(JSONException e){
            	distance=0;
            }
       
        return distance;
    }
    
    
    
    /**
	 * Fonction qui envoi une requete à un site (http://ip-api.com/json) pour
	 * recevoir en retour la position approximative de l'utilisateur, grace a son ip
	 * @return coords liste avec les lat et lon de la position du user
     * @throws IOException 
	 */
	public List<Double> getPositionIP() throws IOException{
		List<Double> coords = new ArrayList<Double>();
		String url = "http://ip-api.com/json";
    	URL urlv = null;
		
		urlv = new URL(url);
		
    	HttpURLConnection conn = null;
		
		conn = (HttpURLConnection) urlv.openConnection();
		
		
		conn.setRequestMethod("GET");
		
    	String line, outputString = "";
    	BufferedReader reader;
		
		reader = new BufferedReader(
		new InputStreamReader(conn.getInputStream()));
		while ((line = reader.readLine()) != null) {
		     outputString += line;
		}
		
		
    	
    	JSONObject test = new JSONObject(outputString);
    	Double lat = null;
    	Double lon = null;
        
        	
        lat = (Double) test.get("lat");
        lon = (Double) test.get("lon");
        	
        
        coords.add(lat);
        coords.add(lon);
        
		return coords;
		
	}
	
    
    
    
    
	/**
	 * Methode qui recupere les coordonnees du magasin selectionne du javascript,
	 * et le set dans la variable consideredShop.
	 * @param lng
	 * @param lat
	 */
	public void onClickMarker(final double lng, final double lat){
		final List<Double> coordinates = new ArrayList<>();
		coordinates.add(lat);
		coordinates.add(lng);
		setConsideredShop(coordinates);
	}
	
	/**
	 * Methode permettant d'ajouter un nouveau magasin,
	 * en cliquant un clic droit sur la map.
	 * @param lng
	 * @param lat
	 * @param name
	 * @param adr
	 * @param caract
	 * @param openingHours
	 * @throws DataAccessorException 
	 */
    public void addingNewShop(final double lng, final double lat, final String name, final String adr,  // NOPMD by Glyptodon on 22/05/18 02:31
    		final String caract, final String openingHours) throws DataAccessorException {
        final ShopProductDataAccessor spda = ShopProductDataAccessor.getInstance();
        final Shop shop = new Shop(name,adr,"","",caract,openingHours,lat,lng);
        spda.saveShop(shop); // NOPMD by Glyptodon on 22/05/18 02:32
        jsObject.call("markerCreation", shop.getLongitude(), shop.getLatitude(), shop.getName(), shop.getOpeningHours());
        
    }
    
    /**
     *  Ajoute les magasins de la base de données sur la map, sous
     *  forme d'epingle.
     *  @param list
     */ 
    public void initializeMarkers(final List<Shop> list){
    	if(!list.isEmpty()){
    		for (final Shop s : list) {
    			jsObject.call("markerCreation", s.getLongitude(), s.getLatitude(), s.getName(), s.getOpeningHours());
    		}
    		jsObject.call("add_markers");
    	}
    }
    
    /**
     * Methode qui recentre la map autour de longitude
     * et latitude passees en parametres.
     * @param lat
     * @param lng
     */
    public void centerMapOnCoords(final double lat, final double lng){
    	jsObject.call("recenterMap", lat, lng);
    }
    
    /**
     * Methode qui active l'evenement du clic sur la map
     * lorsqu'il s'agit du plus court chemin.
     * @param onClickActive
     */
    public void activateOnClick(Boolean onClickActive){
    	if(onClickActive){
    		jsObject.call("onClickOn");
    	}else{
    		jsObject.call("onClickOff");
    	}
    }
    
    /**
     * Methode qui active ou desactive le clique droit sur la map
     * lorsqu'il ne s'agit pas de la vue d'acueille.
     * @param onRightClickActive
     */
    public void activateRightClick(Boolean onRightClickActive){
    	if(onRightClickActive){
    		jsObject.call("rightClickOn");
    	}else{
    		jsObject.call("rightClickOff");
    	}
    }
    /**
     * Methode qui supprime tout les epingles des magasins sur la map.
     */
    public void removeMarkers(){
    	jsObject.call("removeMarkers");
    }
    
}
