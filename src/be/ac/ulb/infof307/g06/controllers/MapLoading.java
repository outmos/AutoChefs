/**
 * 
 */
package be.ac.ulb.infof307.g06.controllers;

import java.net.URL;
import java.util.List;
import be.ac.ulb.infof307.g06.model.Shop;
import be.ac.ulb.infof307.g06.utils.BridgeSingleton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;


/**
 * Classe qui cree la map et l'initialise avec des 
 * markers/ magasins qui lui sont passes.
 */
public class MapLoading{
	
	private WebEngine engine;
	private WebView webviewmap;
	private List<Shop> res;
    private URL urlHello;
    private Boolean onClickActive;
    private Boolean onRightClickActive;
    BridgeSingleton bridge = BridgeSingleton.getInstance();
    
    // Constructors
    public MapLoading(WebView webviewmap, double lat, double lng, Boolean onClickActive, Boolean onRightClickActive){
    	this.webviewmap = webviewmap;
    	this.onClickActive = onClickActive;
    	this.onRightClickActive = onRightClickActive;
    	initializeMap(lat, lng);
    }
    
    public MapLoading(WebView webviewmap, List<Shop> res, double lat, double lng, Boolean onClickActive, Boolean onRightClickActive){
    	this.res = res;
    	this.webviewmap = webviewmap;
    	this.onClickActive = onClickActive;
    	this.onRightClickActive = onRightClickActive;
    	initializeMap(lat, lng);
    }
    
    
    /**
     * Initialise la carte avec ou sans magasin(s), selon la valeur
     * de la liste de magasins res.
     */
    private void initializeMap(double lat, double longi){
    	engine = webviewmap.getEngine();
        urlHello = getClass().getResource("/map/map.html");
        engine.load(urlHello.toExternalForm());
        
        // A Worker load the page
        Worker<Void> worker = engine.getLoadWorker();
 
        // Listening to the status of worker
        worker.stateProperty().addListener(new ChangeListener<State>() {
            @Override
            public void changed(ObservableValue<? extends State> observable, //
                State oldValue, State newValue) {
 
                // When load successed.
                if (newValue == Worker.State.SUCCEEDED) {

                    // Get window object of page.
                    JSObject jsobj = (JSObject) engine.executeScript("window");
                    jsobj.setMember("myJavaMember", bridge);
                    bridge.setJSObject(jsobj);
                    bridge.centerMapOnCoords(lat, longi);
                    bridge.activateOnClick(onClickActive);
                    bridge.activateRightClick(onRightClickActive);
                    try{
                        bridge.initializeMarkers(res);
                    }catch(NullPointerException e){
                       
                    }
                }
            }
        });
    }

   
}
