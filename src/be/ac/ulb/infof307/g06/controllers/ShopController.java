package be.ac.ulb.infof307.g06.controllers;
 
 
import java.net.URL;
import java.util.ResourceBundle;

import be.ac.ulb.infof307.g06.model.ShopPrice;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
 
/** Controller de la view qui affiche le prix totale apres avoir fait
 * une liste de course */
public class ShopController implements Initializable{
   
 
 
    private ObservableList<ShopPrice> slist;
   
    @FXML
    private TableView<ShopPrice> tableView;
   
    @FXML
    private TableColumn<ShopPrice, String> columnShop;
   
    @FXML
    private TableColumn<ShopPrice, ImageView> columnLogo;
 
   
    @FXML
    private TableColumn<ShopPrice, String> columnAddress;
   
    @FXML
    private TableColumn<ShopPrice, Integer> columnPrice;
   
    /** Rempli le tableau avec les logos, adresses, noms du magasins
     * et le prix total des courses effectuees 
     * @param list */
    public void setShopList(ObservableList<ShopPrice> list){
       
        for (ShopPrice shop : list) {
            ImageView item;
            Image tmp;
            String c = getClass().getResource(shop.getLogoUrl()).toExternalForm();
            
            tmp = new Image(c);
            item = new ImageView(tmp);
            shop.setLogo(item);
        }
       
        this.slist = list;
        tableView.setItems(slist);
    }
    
    /** initialise le tableau que contient de la view
     * 4 colonnes : logo, nom, adresse, prix totale*/
    @Override
    public void initialize(URL url, ResourceBundle rb){
  
        columnLogo.setCellValueFactory(new PropertyValueFactory<>("logo"));
        columnShop.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }  
}
