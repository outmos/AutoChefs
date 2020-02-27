package be.ac.ulb.infof307.g06.database;
import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import be.ac.ulb.infof307.g06.model.Product;
import be.ac.ulb.infof307.g06.utils.Constants;
import be.ac.ulb.infof307.g06.model.Shop;

public final class ParserProduct {
	private static final int NONEXISTANT_PRODUCT = 1;
	private static final int IN_SHOP_PRODUCT = 2;
	private static final int NON_IN_SHOP_PRODUCT = 3;
	
	ArrayList<Product> products;
	ArrayList<Shop> shops;
	ArrayList<Integer> actions;
	ArrayList<Double> prices;
	private ShopProductDataAccessor spda;
	
	private static final ParserProduct INSTANCE = new ParserProduct();
	
	
	private ParserProduct() {
		spda = ShopProductDataAccessor.getInstance();
		products = new ArrayList<Product>();
		shops = new ArrayList<Shop>();
		prices = new ArrayList<Double>();
		actions = new ArrayList<Integer>();
	}
	
	
	public static ParserProduct getInstance(){
		return INSTANCE;
	}
	
	/**
	 * Parse une ligne et remplit si necessaire la base de donnees
	 * @param line : ligne a parser 
	 * @throws ParseProductException
	 * @throws DataAccessorException 
	 */
	private void parserLine(String line) throws ParseProductException, DataAccessorException {
		String[] res=line.split(";");
		Shop shop = new Shop(res[6], res[7]);
		int shopId = spda.getShopIdFromNameAddress(shop.getName(), shop.getAddress());
		if (shopId == -1){
			throw new ParseProductException(ParseProductException.INVALID_SHOP);
		}
		parserLine(line,shopId,Double.valueOf(res[8]));
		
	}
	
	/**
	 * Parse une ligne et remplit si necessaire la base de donnees
	 * @param line : ligne a parser
	 * @param shopId : id du magasin 
	 * @throws ParseProductException
	 * @throws NumberFormatException
	 * @throws DataAccessorException
	 */
	private void parserLine(String line, int shopId) throws ParseProductException, NumberFormatException, DataAccessorException {
		String[] res=line.split(";");
		parserLine(line,shopId,Double.valueOf(res[6]));
	}
	
	private void parserLine(String line, int shopId, double price) throws ParseProductException, DataAccessorException {
		boolean actionfound = false;
		String[] res=line.split(";");
		
		Product prod = new Product(res[0], res[1], Double.valueOf(res[2]), Double.valueOf(res[3]),
				Double.valueOf(res[4]), Double.valueOf(res[5]));
		
		prod.setQuantity(1);
		products.add(prod);
		
		Shop shop = new Shop(shopId);
		shops.add(shop);
		
		prices.add(price);
		
		int productId = spda.getProdIdFromName(prod.getName());
		if (productId == -1){
			actions.add(NONEXISTANT_PRODUCT);
			actionfound = true;
		}
		else {
			prod.setId(productId);
		}
		
		if (!actionfound) {
			double oldPrice = spda.getShopPrice(shop, prod);
			if (oldPrice == -1) {
				actions.add(NON_IN_SHOP_PRODUCT);
				actionfound = true;
			}
			else {
				actions.add(IN_SHOP_PRODUCT);
			}
		}
		
	}
	
	
	
	/**
	 * Execute les modifications devant etre apportees a la base de donnees decrites par les listes
	 * @throws DataAccessorException 
	 */
	private void executeModifications() throws DataAccessorException {
		for (int i = 0; i < actions.size(); ++i) {
			if (actions.get(i) == NONEXISTANT_PRODUCT) { // le produit existe pas
				spda.saveProduct(products.get(i));
				spda.saveMapProdStore(shops.get(i), products.get(i), prices.get(i));
			}
			if (actions.get(i) == IN_SHOP_PRODUCT) { // le produit existe et est deja dans le magasin
				spda.changeProduct(products.get(i));
				spda.changePriceForProdShop(shops.get(i), products.get(i), prices.get(i));
			}
			if (actions.get(i) == NON_IN_SHOP_PRODUCT) { // le produit existe mais n est pas encore dans le magasin
				spda.changeProduct(products.get(i));
				spda.saveMapProdStore(shops.get(i), products.get(i), prices.get(i));
			}
		}
	}
	
	
	/**
	 * Parse un texte donnee et appelle la fonction parserLine a chaque ligne
	 * @param text : le texte a parser
	 * @param shopId : id du magasin, NO_SHOPID si on l a pas
	 * @throws ParseProductException : erreur specifique si l'informartion n'est pas correcte
	 * @throws DataAccessorException 
	 */
	private void parserText(String text, int shopId) throws ParseProductException, DataAccessorException {
		String[] res = text.split("\n");
		try {
		for (int i=0; i<res.length; i++){
			if (shopId == Constants.NO_SHOPID) {
				parserLine(res[i]);
			}
			else{
				parserLine(res[i],shopId);
			}
		}
		executeModifications();
		} catch( ParseProductException ex) {
			throw ex;
		}
	}
	
	/**
	 * Parse un texte donnee
	 * @param text : le texte a parser
	 * @throws ParseProductException : erreur specifique si l'informartion n'est pas correcte
	 * @throws DataAccessorException 
	 */
	public void parserText(String text) throws ParseProductException, DataAccessorException {
		parserText(text,Constants.NO_SHOPID);
	}
	
	
	/**
	 * Parse un fichier dont le chemin est donne et appelle la fonction parserLine a chaque ligne
	 * @param pathToFile : le chemin vers le fichier
	 * @param shopId : id du magasin, NO_SHOPID si on l a pas
	 * @throws FileNotFoundException :  si le path ne correspond pas a un fichier 
	 * @throws ParseProductException : erreur specifique si l'informartion n'est pas correcte
	 * @throws DataAccessorException 
	 */
	public void parserFile(String pathToFile, int shopId) throws FileNotFoundException, ParseProductException, DataAccessorException { 
        String line = "";
        FileReader fReader;
        fReader = new FileReader(pathToFile);            
        
        try {
        	BufferedReader bReader = new BufferedReader(fReader);
            line=bReader.readLine();
            while(line != null) {
                try{
                	if (shopId == Constants.NO_SHOPID) {
                		parserLine(line);
                	}
                	else {
                		parserLine(line,shopId);
                	}
                	line=bReader.readLine();
                }
                catch(ParseProductException ex){
                	bReader.close();
                	throw ex;
                }
                catch(Exception ex){
                	bReader.close();
                	throw new ParseProductException(ParseProductException.ERROR_IN_FILE);
                }
            }
            bReader.close();
            executeModifications();
        }
        catch(IOException ex) {
        	throw new ParseProductException(ParseProductException.ERROR_IN_FILE);
        }
    }
	
	/**
	 * Parse un fichier dont le chemin est donne et appelle la fonction de parsing par ligne a chaque ligne
	 * @param pathToFile : le chemin vers le fichier
	 * @throws FileNotFoundException :  si le path ne correspond pas a un fichier 
	 * @throws ParseProductException : erreur specifique si l'informartion n'est pas correcte
	 * @throws DataAccessorException 
	 */
	public void parserFile(String pathToFile) throws FileNotFoundException, ParseProductException, DataAccessorException {
		parserFile(pathToFile, Constants.NO_SHOPID);
	}
}
