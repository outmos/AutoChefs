
package be.ac.ulb.infof307.g06;

import java.security.NoSuchAlgorithmException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



/**Main */
public class Main extends Application {
	
    @Override
    public void start(final Stage primaryStage) throws Exception{
    	
		final Parent root = FXMLLoader.load(getClass().getResource("/views/LoginView.fxml"));
        primaryStage.setTitle("Shop");

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
		launch(args);
    }
    
}