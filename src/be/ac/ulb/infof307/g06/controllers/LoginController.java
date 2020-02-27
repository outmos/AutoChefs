package be.ac.ulb.infof307.g06.controllers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import be.ac.ulb.infof307.g06.database.CheckDatabaseId;
import be.ac.ulb.infof307.g06.database.AccountManager;
import be.ac.ulb.infof307.g06.database.CreateConnectAccountException;
import be.ac.ulb.infof307.g06.database.DataAccessorException;
import be.ac.ulb.infof307.g06.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

/** Controller de la view du login */
public class LoginController extends SceneManager {

	@FXML
	private TextField username;

	@FXML
	private PasswordField password;

	@FXML
	private Button loginButton;

	@FXML
	private Button registerButton;

	/**
	 * recupere le nom et le mot de passe encode sur la view et regarde dans la
	 * base de donnee si il est deja inscrit.
	 * @param event 
	 * @throws IOException 
	 */
	public void connection(ActionEvent event) throws IOException {

    	while (!CheckDatabaseId.checkId()) {
    		try {
    			CheckDatabaseId.savePassword(askDatabasePassword());
			} catch (Exception e) {
				printError("Erreur lors de l'encryption/l'ecriture de votre mot de passe");
			}
    	}
		String name = username.getText();
		String pass = password.getText();
		int id;

		AccountManager cc = AccountManager.getInstance();
		try {
			id = cc.securisationConnect(name, pass);
		} catch (CreateConnectAccountException e) {
			printError(e.getMessage());
			return;
		} catch (NoSuchAlgorithmException e) {
			printError("NoSuchAlgorithmException");
			return;
		} catch (DataAccessorException e) {
			printError(e.getMessage());
			return;
		}

		SceneManager.setUser(new User(id, name));
		loadNextScene("/views/HomeView.fxml", "Accueil", event);
	}

	/**
	 * fonction relie au bouton "creer un compte", elle permet d'aller a la page
	 * pour s'inscrire.
	 * @param event 
	 * @throws IOException 
	 */
	public void loadMain(ActionEvent event) throws IOException {
		loadNextScene("/views/RegisterView.fxml", "Sign In", event);
	}
	
	private String askDatabasePassword() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Mot de Passe de la Base de Donnee");
		dialog.setHeaderText("Veuillez enter le mot de passe de la base de donnee");
		dialog.setContentText("Mot de Passe:");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
		    return dialog.getResult();
		}
		else {
			return "";
		}
	}

}
