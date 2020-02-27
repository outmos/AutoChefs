package be.ac.ulb.infof307.g06.controllers;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


import be.ac.ulb.infof307.g06.model.Shop;
import be.ac.ulb.infof307.g06.utils.BridgeSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

/**
 * Affiche le magasin le plus proche vendant tout les produits d une liste de course donee
 *
 */
public class NearestShopController extends SceneManager implements Initializable {
	
	
    BridgeSingleton bridge = BridgeSingleton.getInstance();
    MapLoading map;
	
	
	@FXML
	private WebView webviewmap;
	
	@FXML
	private VBox vboxmap;
    
    @FXML
    private Button logoutButton;
    
    @FXML
    private ComboBox<String> modes;
    
    @FXML
    private TextField longField, latField;
    
    /**
     * Creer la carte avec les magasin donnes en parametres
     * @param list
     * @throws IOException
     */
    public NearestShopController(List<Shop> list) throws IOException {
    	bridge.setShopContainer(list);
    }
    
    
    /**
     * Initialise et centrer la map sur la position de l'utilisateur.
     */
	@Override
	public void initialize(URL location, ResourceBundle resources){
		List<Double> coords = null;
		try {
			coords = bridge.getPositionIP();
		} catch (IOException e) {
			printError("votre localisation n'as pas pu etre determine");
		}
		
		map = new MapLoading(webviewmap, coords.get(0), coords.get(1), true, false);
	}
	
	
	
	
	
	/**
	 * Methode cherchant le magasin le plus proche et l'affichant sur la carte via le BridgeSingleton.
	 * @param event
	 * @throws IOException 
	 */
	public void onSearchClick(ActionEvent event) throws IOException{
		bridge.removeMarkers();
		String mode = modes.getValue();
		switch(mode){
			case "A velo": 
				mode = "bicycling";
				break;
			case "A pieds": 
				mode = "walking";
				break;
			case "En voiture": 
				mode = "driving";
				break;
			default:
				return;
		}
		
		
		String origin = String.join(",",latField.getText(), longField.getText());
		
		bridge.nearestShop(origin, mode);
	}
	
    
    /**
     * Methode qui reinitialise les champs de longitude, latitude
     * et mode de transport.
     */
	public void resetForm(){
		double lat = 50.8503396 ; 
		double lg = 4.351710300000036 ; 
		bridge.removeMarkers();
		bridge.centerMapOnCoords(lat, lg);
		
		latField.setText("");
		longField.setText("");
		modes.setValue("En voiture");
	}
}
