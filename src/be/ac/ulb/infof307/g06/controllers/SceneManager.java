package be.ac.ulb.infof307.g06.controllers;


import java.io.IOException;
import java.util.ArrayList;

import be.ac.ulb.infof307.g06.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
/**
 * Classe abstraite heritee par les autres controllers.
 * Gere les fonctionnalites communes a toute les fenetres:
 * - bouton back
 * - bouton logout
 * - charger la fenetre suivante
 * - fenetres pop-ups
 * - Afficher le nom de l utilisateur courant
 */
public abstract class SceneManager {
	
	static User user = null;
	
	static ArrayList<String> stackView = new ArrayList<String>();
	static ArrayList<String> stackTitle = new ArrayList<String>();
	static String lastTitle;
	private String currentView =null;
	private String currentTitle = null;
	private FXMLLoader loader = null;
	private ActionEvent event =null;
	protected  Parent root = null;
    protected Stage stage = null;

	/**
	 * constructeur par defaut
	 */
	public SceneManager(){}
	
	/** 
	 * constructeur pour initialiser une nouvelle view 
	 * @param view 
	 * @param title 
	 * @param event 
	 */
	protected void setNextView(String view, String title, ActionEvent event) {
		this.currentView = view;
		this.currentTitle = title;
		this.event = event;
		push(view,title);
		
	}
	
	/** 
	 * Constructeur pour initialiser le retour sur la view precedente
	 * @param event 
	 */
	protected void setPreviousView(ActionEvent event){
		this.event = event;
		if(!stackView.isEmpty()){
			pop();
		}
	}
	
	/**
	 * Charge la fenetre suivante
	 * @param view
	 * @param title
	 * @param event
	 * @throws IOException
	 */
	protected void loadNextScene(String view, String title, ActionEvent event ) throws IOException{
		setNextView(view, title, event);
    	getLoader();
    	loadView();
	}
	
	/** 
	 * Change l utilisateur courant 
	 * (appele uniquement a la connexion d un utilisateur)
	 * @param user 
	 * */
	public static void setUser(User user) {
		SceneManager.user = user;
	}
	
	/** 
	 * retourne l'id du user
	 * @return 
	 * */
	public static int getUserID() {
		return user.getID();
	}
	
	/** 
	 * retourne le nom du user
	 * @return 
	 * */
	public static String getUserName() {
		return user.getName();
	}
	
	/** 
	 * ajoute un element aux stacks de fenetres (l'ajoute au sommet de la pile)
	 * */
	private void push(String view, String title){
		stackView.add(view);
		stackTitle.add(title);
	}

	/** 
	 * Enleve l'element le plus recent des stacks de fenetres
	 * (qui se trouve au sommet de la pile)
	 * */
	private void pop(){
		stackView.remove(stackView.size() - 1);
		stackTitle.remove(stackTitle.size() -1);
		if(!stackView.isEmpty()){
			this.currentView = stackView.get(stackView.size() -1);
			this.currentTitle = stackTitle.get(stackTitle.size() -1);
		}else{
			this.currentView = "/views/LoginView.fxml";
			this.currentTitle = "Shop";
		}
	}

	/** 
	 * permet de charger la view et de l'afficher
	 * @throws IOException 
	 * */
	protected void loadView() throws IOException{
        stage.hide();
        stage.setTitle(this.currentTitle);
        stage.setScene(new Scene(root));
        stage.show();

	}
	
	
	/** 
	 * permet d'avoir le loader d'une view et d'initier le root la stage 
	 * de cette view
	 * @return 
	 * @throws IOException 
	 * */
	protected FXMLLoader getLoader() throws IOException{
		this.loader = new FXMLLoader(getClass().getResource(this.currentView));
		this.root = this.loader.load();
        this.stage =  (Stage) ((Node) this.event.getSource()).getScene().getWindow() ;
		return this.loader;
	}

	/** 
	 * permet de vider le stack des fenetre : il ne restera que la page
	 * de login dans le stack.
	 * */
	private void clearStack() {
		for(int i = 0; i<=stackView.size();i++){
			pop();
		}
	}
	
	/** 
	 * fenetre popup affichant le message d'erreur donne en parametre
	 * @param msg 
	 * */
	protected void printError(String msg) {
		Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Erreur");
        alert.setContentText(msg);
        alert.showAndWait();
	}
	
	/** 
	 * fenetre popup affichant un message d'information donne en parametre
	 * @param msg 
	 * */
	protected void printInfo(String msg) {
		Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Information");
        alert.setContentText(msg);
        alert.showAndWait();
	}
	
	/** 
	 * permet de se deconnecter a tout moment de l'application
	 * @param event 
	 * @throws IOException 
	 * */
	public void logout(ActionEvent event) throws IOException{
		user = null;
		
		Parent root = FXMLLoader.load(getClass().getResource("/views/LoginView.fxml"));
        Stage stage =  (Stage) ((Node) event.getSource()).getScene().getWindow() ;
        
        stage.hide();
        stage.setTitle("Shop");
        stage.setScene(new Scene(root));
        stage.show();
		clearStack();
	}
	
	/** 
	 * retourne a la page precedente sans passages de donnees
	 * @param event 
	 * @throws IOException 
	 * */
	public void back(ActionEvent event) throws IOException{
    	setPreviousView(event);
    	getLoader();
    	loadView();
    }
}
