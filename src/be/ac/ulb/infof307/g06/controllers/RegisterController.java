package be.ac.ulb.infof307.g06.controllers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.util.Optional;

import be.ac.ulb.infof307.g06.database.AccountManager;
import be.ac.ulb.infof307.g06.database.CheckDatabaseId;
import be.ac.ulb.infof307.g06.database.CreateConnectAccountException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

/** Controller de la view de la creation d'un compte*/
public class RegisterController extends SceneManager{
	
	private  AccountManager ca = AccountManager.getInstance() ; 
	@FXML
    private TextField registerUsername;

    @FXML
    private PasswordField registerPass;

    @FXML
    private PasswordField registerConfPass;

    @FXML
    private Label errorLabel;

	/** recupere le nom d'utilisateur et le mot de passe , verifie si 
	 * tout est bien encode et si le nom d'utilisateur n'est pas deje� utilise.
	 * Si toutes les conditions sont ok, cree un compte, sinon reste sur la view.
	 * @param event 
	 * @throws IOException 
	 */
    public void register(final ActionEvent event) throws IOException {
    
    	while(!CheckDatabaseId.checkId()){
    		try{
    			CheckDatabaseId.savePassword(askDatabasePassword());
    		}catch (Exception e){
    			printError("Erreur lors de l'encryption/l'ecriture de votre mot de passe");
    		}
    	}
    	
        final String username = registerUsername.getText();
        final String password = registerPass.getText();
        final String confPassword = registerConfPass.getText();
        
        try {
        	ca.insertToDatabase(username, password, confPassword);
        }
        catch (CreateConnectAccountException e) {
        	printError(e.getMessage());
        	return;
        }
        catch (BatchUpdateException e) {
        	printError("Le pseudo est deja pris");
        	return;
        }
        catch (SQLException e) {
        	printError("Impossible de se connecter a la base de donnee");
        	return ;
        }
        catch ( NoSuchAlgorithmException e) {
        	printError("NoSuchAlgorithmException");
        	return;
        }
       
        printInfo("Votre compte a bien ete cree");
        loadNextScene("/views/LoginView.fxml","Liste de courses", event); 
    }
    
    
    /** 
     * Ferme la page pour s'enregistrer et revient au menu
     * @param event 
     * @throws IOException 
     */
    @FXML
    public void connect(ActionEvent event) throws  IOException{
    	super.back(event);
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

