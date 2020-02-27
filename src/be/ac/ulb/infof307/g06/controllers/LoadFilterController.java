package be.ac.ulb.infof307.g06.controllers;
import java.io.IOException;import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import be.ac.ulb.infof307.g06.database.DataAccessorException;
import be.ac.ulb.infof307.g06.model.Filtre;
import be.ac.ulb.infof307.g06.model.Shop;
import be.ac.ulb.infof307.g06.utils.BridgeSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

/**
 *
 *
 */
public class LoadFilterController  extends SceneManager implements Initializable  {
	private List<Filtre> filtres = new ArrayList<>();
	private  ObservableList<String> items =FXCollections.observableArrayList ();
	BridgeSingleton bridge = BridgeSingleton.getInstance();
	
	@FXML
	private Label userName;
	
	@FXML
    private Button logoutButton;
	
	@FXML
    private Button backButton;
	
	@FXML
    private Button loadButton;
	
	@FXML 
	private ListView<String>  historique ; 
	
	public void initialize(URL url, ResourceBundle rb){
		userName.setText(SceneManager.getUserName());
	    historique.setItems(items);
	}

	/**
	 * Recharge une recherche sauvegarder par l'utilisateur
	 * @param event
	 * @throws IOException
	 */
	public void loadFilter(ActionEvent event) throws IOException  {
		int tmp = historique.getSelectionModel().getSelectedIndex();

		if(tmp != -1){
			Filtre chosen = filtres.get(tmp) ; 
			setNextView("/views/HomeView.fxml","Filtres", event);
			
			HomeController controller = getLoader().getController();
			try {
				List<Shop> results = chosen.createListShop();
				bridge.removeMarkers();
				controller.setList(results);
				
				
		    	//bridge.initializeMarkers(results);
		    	loadView();
			} catch (DataAccessorException e) {
				printError(e.getMessage());
			}
		}
	}
	
	@Override
	public void back(ActionEvent event) throws IOException{
		/**
		 *  Retourne à la fenêtre precedente
		 */
		super.back(event);
	}
	
	/**
	 * Obtient tous les choix de l'utilisateur pour l'affichage
	 * @param f
	 * @return
	 */
	public String getAllAttributes(Filtre f){
		String str = ""; 
		
		if (f.getNameShop() != null){
			 str += f.getNameShop()+ " " ; 
		 }
		
		if (f.getDay()!= null){
			 str += f.getDay()+"  " ; 
		 }
		
		if (f.getHour() != null){
			str += f.getHour()+"  " ;  
		}
		
		if (f.getBio() != null && f.getBio()) {
			str += "bio "; 
		}
		if (f.getVeg() !=null && f.getVeg() ) {
			str += "veg " ; 
		}
		if (f.getVegan() != null && f.getVegan()) {
			str += "vegan " ; 
		}
		if (f.getLocal() != null && f.getLocal()) {
			str += "local" ; 
		} 
		return str ; 
		
	}
	
	/**
	 * set pour la liste de filtres
	 * @param list
	 */
	public void setFilterList(List<Filtre> list){
		 this.filtres= list; 
		 for (Filtre f : filtres){
			 items.add(getAllAttributes(f)) ; 
		 }
	}
}
