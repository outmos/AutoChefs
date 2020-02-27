package be.ac.ulb.infof307.g06.controllers;


import be.ac.ulb.infof307.g06.utils.BridgeSingleton;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import be.ac.ulb.infof307.g06.database.DataAccessorException;
import be.ac.ulb.infof307.g06.database.FilterDataAccessor;
import be.ac.ulb.infof307.g06.database.ShopProductDataAccessor;
import be.ac.ulb.infof307.g06.model.Filtre;
import be.ac.ulb.infof307.g06.model.Shop;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;


/**
 * Affiche la page d'accueil
 */
public class HomeController extends SceneManager implements Initializable{
	private FilterDataAccessor fda = FilterDataAccessor.getInstance();
	private ShopProductDataAccessor spda = ShopProductDataAccessor.getInstance();
	
	BridgeSingleton bridge = BridgeSingleton.getInstance();
	MapLoading map;
	List<Shop> results;
	
	/**
	 * setter pour "this.results". Place les marqueurs lies a results sur la carte affichee
	 * @param results
	 */
	public void setList(List<Shop> results){
		this.results = results;
		bridge.initializeMarkers(results);
	}
	
	@FXML
	private WebView webviewmap;
	
	@FXML
	private VBox vboxmap;
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button saveButton;

    @FXML
    private Button backButton;

    @FXML
    private CheckBox bioButton;

    @FXML
    private Button searchButton;
    
    @FXML
    private Button loadButton ; 
    
    @FXML
    private Button addProductButton;
    
    @FXML
    private ChoiceBox<String> dayBox;

    @FXML
    private TextField hoursField;

    @FXML
    private CheckBox localButton;

    @FXML
    private ChoiceBox<String> nameBox;

    @FXML
    private AnchorPane shopName;

    @FXML
    private Label userName;

    @FXML
    private CheckBox vegButton;

    @FXML
    private CheckBox veganButton;
	
	@FXML
	private Text welcomeText;
	
	@FXML
	private Button logoutButton;
	
	@FXML
	private Button shoppingListButton;
	
	@FXML
	private Button recipeCreationButton;
	
	@FXML
	private Button recipePlanificationButton;
	
	@FXML
	private Button reset;
	
	/** intitialise la page de la liste de course
	 * @param event
	 * @throws IOException 
	 */
	public void shoppingList(ActionEvent event) throws IOException{
		loadNextScene("/views/ShoppingListView.fxml","Liste de courses", event);
	}
	
	/** initialise la page de la creation de recette
	 * @param event 
	 * @throws IOException 
	 * */
	public void recipeCreation(ActionEvent event) throws IOException{
		loadNextScene("/views/RecipeMainView.fxml","Recette", event);
	}
	
	/** initialise la page de planification de recette pour une semaine
	 * @param event 
	 * @throws IOException 
	 * */
	public void recipePlanification(ActionEvent event) throws IOException{
		loadNextScene("/views/PlanificationView.fxml","Recette", event);
	}
    
	/**
	 *  Cree un filtre a partir des choix fait par l'utilisateur 
	 */
    private Filtre getFiltre(){
    	String shopName ; 
    	String day ; 
    	if (nameBox.getSelectionModel().isEmpty() || nameBox.getSelectionModel().getSelectedItem() == ""){
    		shopName = null; 
    	}
    	else {
    		shopName = nameBox.getSelectionModel().getSelectedItem(); 
    	}
    	
    	if (dayBox.getSelectionModel().isEmpty() || dayBox.getSelectionModel().getSelectedItem() == ""){
    		day = null ; 
    	}
    	else {
    		day = dayBox.getSelectionModel().getSelectedItem(); 
    	}
    	return new Filtre(shopName,day,hoursField.getText().trim().isEmpty() ? null : hoursField.getText().trim(),
    			bioButton.isSelected(), vegButton.isSelected(), veganButton.isSelected(), localButton.isSelected());
    }
     
    private void initDay(){
    	List<String> listD = new ArrayList<String>();
        ObservableList<String> listDay = FXCollections.observableArrayList(listD);
        listDay.add("") ; 
        listDay.add("lundi");
        listDay.add("mardi");
        listDay.add("mercredi");
        listDay.add("jeudi");
        listDay.add("vendredi");
        listDay.add("samedi");
        listDay.add("dimanche");
        dayBox.setItems(listDay);
    }
    
    /**
	 * Methode qui refresh la map en enlevant les markers et replacer 
	 * que ceux qui correspondent a la recherche de l'utilisateur.
     * @param event 
     * @throws IOException 
	 */
    @FXML
    public void confirmSelection(ActionEvent event) throws IOException {
    	List<Integer> unacceptable = new ArrayList<Integer>();
    	for (int i=6; i<=9; i++){
    		unacceptable.add(i);
    	}
        Filtre result = getFiltre();
        
        if(result.getHour() != null  && result.getHour().matches("\\d{2}:\\d{2}")
        		&& !(unacceptable.contains(Integer.valueOf(result.getHour().substring(0, 1))))
        		&& !(unacceptable.contains(Integer.valueOf(result.getHour().substring(3, 4))))){
        	if(result.getDay() == null || result.getDay() == "" ){
        		printError("Veuillez preciser un jour");
        	}else{
        		List<Shop> filterResults;
				try {
					filterResults = result.createListShop();
	        	    bridge.removeMarkers();
	        	    bridge.initializeMarkers(filterResults);
				} catch (DataAccessorException e) {
					printError(e.getMessage());
					return;
				}
        	}
        	
        }else if(result.getHour() != null  && ((!result.getHour().matches("\\d{2}:\\d{2}")
        		|| (unacceptable.contains(Integer.valueOf(result.getHour().substring(0, 1)))) 
        		|| (unacceptable.contains(Integer.valueOf(result.getHour().substring(3, 4))))))){
        	printError("Veuillez mettre l'heure en format suivant: HH:MM .");
        	
        }else{
        	List<Shop> filterResults;
			try {
				filterResults = result.createListShop();
				if (!filterResults.isEmpty()) {
		    	    bridge.removeMarkers();
		    	    bridge.initializeMarkers(filterResults);
				}
				else {
					printError("Aucun Magasin ne correspond a votre recherche");
				}
			} catch (DataAccessorException e) {
				printError(e.getMessage());
			}
        }
    }
    
    /**
	 *  Sauvegarde une recherche faite par l'utilisateur
     * @param event 
     * @throws IOException 
	 */
    @FXML
    public void confirmSave(ActionEvent event) throws IOException {
    	Filtre result = getFiltre() ; 
    	if(result.getResearch().isEmpty()){
    		printError("La sauvegarde a echouee, veuillez reessayer.");
    	}
    	else{
    		try{
    			fda.saveResearch(SceneManager.getUserID(), result.getResearch());
    			printInfo("Votre recherche est sauvegardee et peut etre chargee plus tard.");
    		} catch (DataAccessorException e) {
    			printError("La sauvegarde a echoue: "+e.getMessage());
    		}
    	}
    }
    
    /**
     * Ajoute un produit a un magasin 
     * @param event
     * @throws IOException
     */
    @FXML
    public void addProducts(ActionEvent event) throws IOException {
    	bridge.addProducts();
    }
    
    /**
     * Charge toutes l'historique des recherches faites par l'utilisateur
     * @param event
     * @throws IOException
     */
    @FXML
    public void loadPreviousResearch(ActionEvent event) throws IOException {
    	setNextView("/views/LoadFilterView.fxml","Filtres", event);
    		List<String> researches;
			try {
				researches = fda.getFiltreFromDb(SceneManager.getUserID());
				List<Filtre> filtres = new ArrayList<>();
	        	for (String s : researches) {
	        		filtres.add( Filtre.createFiltreFromResearch(s)); 
	        	}
	    		LoadFilterController controller = getLoader().getController();
	        	controller.setFilterList(filtres);
	        	loadView();
			} catch (DataAccessorException e) {
				printError(e.getMessage());
			}
    }
    
    public void resetMap() {
    	 bridge.removeMarkers(); 
    	 try {
			bridge.initializeMarkers(spda.getShopsList());
		} catch (DataAccessorException e) {
			printError(e.getMessage());
		}
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	
    	try {
			results = spda.getShopsList();
		} catch (DataAccessorException e) {
			printError(e.getMessage());
		}
    	List<Double> coords = null;
		try {
			coords = bridge.getPositionIP();
		} catch (IOException e1) {
			printError("votre localisation n'as pas pu etre determine");
		}
        Double lat = coords.get(0);
        Double lon = coords.get(1);
        map = new MapLoading(webviewmap, results, lat, lon, false, true);
    	userName.setText(SceneManager.getUserName());

        assert saveButton != null : "fx:id=\"SaveButton\" was not injected: check your FXML file 'FiltreView.fxml'.";
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'FiltreView.fxml'.";
        assert bioButton != null : "fx:id=\"bioButton\" was not injected: check your FXML file 'FiltreView.fxml'.";
        assert searchButton != null : "fx:id=\"searchButton\" was not injected: check your FXML file 'FiltreView.fxml'.";
        assert dayBox != null : "fx:id=\"dayBox\" was not injected: check your FXML file 'FiltreView.fxml'.";
        assert hoursField != null : "fx:id=\"hoursField\" was not injected: check your FXML file 'FiltreView.fxml'.";
        assert localButton != null : "fx:id=\"localButton\" was not injected: check your FXML file 'FiltreView.fxml'.";
        assert nameBox != null : "fx:id=\"nameBox\" was not injected: check your FXML file 'FiltreView.fxml'.";
        assert shopName != null : "fx:id=\"shopName\" was not injected: check your FXML file 'FiltreView.fxml'.";
        assert userName != null : "fx:id=\"userName\" was not injected: check your FXML file 'FiltreView.fxml'.";
        assert vegButton != null : "fx:id=\"vegButton\" was not injected: check your FXML file 'FiltreView.fxml'.";
        assert veganButton != null : "fx:id=\"veganButton\" was not injected: check your FXML file 'FiltreView.fxml'.";
        initDay();
        
        
        
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// pas besoin de prevenir l'utilisateur sur l'exception
		}
		
        printInfo("Chargement");
        
    }

}
