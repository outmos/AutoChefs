package be.ac.ulb.infof307.g06.controllers;


import be.ac.ulb.infof307.g06.utils.BridgeSingleton;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import be.ac.ulb.infof307.g06.model.Shop;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

/**
 * Affiche une carte avec le magasin le moins cher pour une liste de course donnee
 */
public class CheapestShopController extends SceneManager implements Initializable{
	MapLoading map; 
	private List<Shop> res;
	BridgeSingleton bridge = BridgeSingleton.getInstance();
	
	@FXML
	private WebView webviewmap;
	
	@FXML
	private VBox vboxmap;

    @FXML
    private Button searchButton;
    
    /**
     * Constructeur initialisant la liste de magasins contenant 
     * les produits de la liste des produits.
     * @param list
     * @throws IOException
     */
    
    public CheapestShopController(List<Shop> list) throws IOException {
    	this.res = list;
    }
    
    /**
     * Initialise la carte et centre cette derniere sur le magasin le moins cher.
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Shop cheapestShop = res.get(0);
		map = new MapLoading(webviewmap, res, cheapestShop.getLatitude(), cheapestShop.getLongitude(), false, false);
		
		
	}
}
