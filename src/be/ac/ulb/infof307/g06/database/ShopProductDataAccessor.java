package be.ac.ulb.infof307.g06.database;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.mysql.jdbc.Statement;

import be.ac.ulb.infof307.g06.model.Product;
import be.ac.ulb.infof307.g06.model.Shop;
import be.ac.ulb.infof307.g06.model.ShopPrice;

/**
 * Listes de fonctions qui font les op�rations sur les tables qui concernnent les produit et les magasins.
 */
public final class ShopProductDataAccessor {
	private DataAccessor dAccessor = DataAccessor.getInstance();
	private static final ShopProductDataAccessor INSTANCE = new ShopProductDataAccessor();
	private ShopProductDataAccessor(){
	}
	public static ShopProductDataAccessor getInstance(){
		return INSTANCE;
	}
	private static double round(double input) {
		return Math.round(input * 100.0) / 100.0;
	}
	
	
	/**
	 * Sauvegarde un magasin dans la base de donnee.
	 * @param shop : la magasin a sauvegarder
	 * @throws DataAccessorException 
	 */
	public void saveShop(Shop shop) throws DataAccessorException{
		Connection conn = DataAccessor.getConnection();  
		PreparedStatement stShop = null; 
		ResultSet rs = null;
		try{
			conn.setAutoCommit(false);  
			stShop = conn.prepareStatement("insert into shop set name=?,address=?, logo=?, city=?,characteristics=?, openinghours=?, lattitude=?, longitude=?",Statement.RETURN_GENERATED_KEYS); 
			stShop.setString(1, shop.getName());
			stShop.setString(2, shop.getAddress());
			stShop.setString(3, shop.getLogoUrl());
			stShop.setString(4, shop.getCity()) ; 
			stShop.setString(5, shop.getCaracteristics()) ; 
			stShop.setString(6, shop.getOpeningHours()) ; 
			stShop.setDouble(7, shop.getLatitude()) ; 
			stShop.setDouble(8, shop.getLongitude()) ; 
			
			stShop.executeUpdate();  
			conn.commit();
			
			int autoIncKeyFromApi = -1;
			rs = stShop.getGeneratedKeys();
			if (rs.next()) {
		        autoIncKeyFromApi = rs.getInt(1);
			}
			shop.setId(autoIncKeyFromApi);
			
		} catch (BatchUpdateException e){
			throw new DataAccessorException(DataAccessorException.ALREADY_IN_DB);
		} catch (SQLException e){
			throw new DataAccessorException(DataAccessorException.DB_ACCESS);
		} finally {
			dAccessor.finallyHandler(stShop, conn);
		}
	}
	
	
	/**
	 * Enregistre un produit dans la base de donnee
	 * @param product : le produit a sauvegarder
	 * @return l'id du produit ajoute
	 * @throws DataAccessorException 
	 */
	public int saveProduct(Product product) throws DataAccessorException{
		Connection conn = DataAccessor.getConnection();  
		PreparedStatement stProd = null; 
		ResultSet rs = null;
		int autoIncKeyFromApi = -1;
		try{ 
			conn.setAutoCommit(false);  
			stProd = conn.prepareStatement("insert into product set name=?, unit=?, kcal=?, protein=?, lipid=?, glucid=?",Statement.RETURN_GENERATED_KEYS);
			stProd.setString(1, product.getName());
			stProd.setString(2, product.getUnit());
			stProd.setDouble(3, product.getKcal());
			stProd.setDouble(4, product.getProteins());
			stProd.setDouble(5, product.getLipids());
			stProd.setDouble(6, product.getGlucids());
			
			stProd.executeUpdate();  
			
			conn.commit(); 
			rs = stProd.getGeneratedKeys();
			if (rs.next()) {
		        autoIncKeyFromApi = rs.getInt(1);
			}
			product.setId(autoIncKeyFromApi);
		} catch (SQLException e) {
			throw new DataAccessorException(DataAccessorException.DB_ACCESS);
		} finally {
			dAccessor.finallyHandler(stProd, conn);
		}
		return autoIncKeyFromApi;
	}
	/**
	 * Met a jour un produit deja present dans la DB
	 * @param product : le produit a mettre a jour 
	 * @throws DataAccessorException 
	 */
	public void changeProduct(Product product) throws DataAccessorException{
		Connection conn = DataAccessor.getConnection();  
		PreparedStatement stProd = null;
		try {
			stProd = conn.prepareStatement("update product set unit = ?, kcal = ?, lipid = ?, glucid = ?, protein = ? where name = ?");//where id = ? plustot non ?
			stProd.setString(1, product.getUnit());
			stProd.setDouble(2, product.getKcal());
			stProd.setDouble(3, product.getLipids());
			stProd.setDouble(4, product.getGlucids());
			stProd.setDouble(5, product.getProteins());
			stProd.setString(6, product.getName());
			
			
			stProd.executeUpdate();
		} catch (SQLException e){
			throw new DataAccessorException(DataAccessorException.WRONG_REQUEST);
		} finally {
			dAccessor.finallyHandler(stProd, conn);
		}
	}
	
	
	/**
	 * Permet d'obtenit les identifiants de tous les magasins portant un certain nom
	 * @param name : le nom des magasins recherches
	 * @return une liste avec tous les identifiants
	 * @throws DataAccessorException 
	 */
	public List<Integer> getShopIdFromName(String name) throws DataAccessorException{
		Connection conn = DataAccessor.getConnection();  
		PreparedStatement stShop = null; 
		ResultSet rs = null;
		List<Integer> ids = new ArrayList<>();
		try{
			conn.setAutoCommit(false);
			stShop = conn.prepareStatement("select idshop from shop where name = ?");
			stShop.setString(1, name);
			rs = stShop.executeQuery();
			while (rs.next()){
				ids.add(rs.getInt(1));
			}
			
			
		} catch (SQLException e){
			throw new DataAccessorException(DataAccessorException.WRONG_REQUEST);
		} finally {
			dAccessor.finallyHandler(stShop, conn);
		}
		return ids;
	}
	
	
	/**
	 * Renvoie l id d un magasin dans la db a partir de son nom et son adresse
	 * @param name : le nom du magasin
	 * @param address : l'adresse du magasin
	 * @return l id ou -1 si le magasin n existe pas
	 * @throws DataAccessorException 
	 */
	public int getShopIdFromNameAddress(String name, String address) throws DataAccessorException{
		Connection conn = DataAccessor.getConnection();  
		PreparedStatement stShop = null; 
		ResultSet rs = null;
		int id = -1;
		try{
			conn.setAutoCommit(false);
			stShop = conn.prepareStatement("select idshop from shop where name = ? and address = ?");
			stShop.setString(1, name);
			stShop.setString(2, address);
			rs = stShop.executeQuery();
			if (rs.next()){
				id = rs.getInt(1);
			}
		} catch (SQLException e){
			throw new DataAccessorException(DataAccessorException.WRONG_REQUEST);
		} finally {
			dAccessor.finallyHandler(stShop, conn);
		}
		return id;
	}
	
	
	
	/**
	 * Permet d'obtenir l'identifiant d'un magasin a partir de ses coordonnees geographiques
	 * @param lattitude : la latitude du magaisn
	 * @param longitude : la longitude du magasin
	 * @return l'id du magasin
	 * @throws DataAccessorException 
	 */
	public int getShopIdFromCoordinates(double lattitude, double longitude) throws DataAccessorException{
		Connection conn = DataAccessor.getConnection();  
		PreparedStatement stShop = null; 
		ResultSet rs = null;
		int id = 0;
		try{
			conn.setAutoCommit(false);
			stShop = conn.prepareStatement("select idshop from shop where lattitude = ? and longitude = ?");
			stShop.setDouble(1, lattitude);
			stShop.setDouble(2, longitude);
			rs = stShop.executeQuery();
			if (rs.next()){
				id = rs.getInt(1);
			}
		} catch (SQLException e){
			throw new DataAccessorException(DataAccessorException.WRONG_REQUEST);
		} finally {
			dAccessor.finallyHandler(stShop, conn);
		}
		return id;
	}
	
	
	/**
	 * Permet d'obtenir l'identifiant d'un magasin a partir de son nom et de ses coordonnees geographiques
	 * @param name : le nnom du magaisn
	 * @param longitude : la longitude du magasin
	 * @param latitude : la latitude du magasin
	 * @return l'id du magasin
	 * @throws DataAccessorException 
	 */
	public int getShopIdFromNameCoord(String name, double longitude, double latitude) throws DataAccessorException{
		Connection conn = DataAccessor.getConnection();  
		PreparedStatement stShop = null; 
		ResultSet rs = null;
		int id = 0;
		try{
			conn.setAutoCommit(false);
			stShop = conn.prepareStatement("select idshop from shop where name = ? and longitude = ? and lattitude = ?");
			stShop.setString(1, name);
			stShop.setDouble(2, longitude);
			stShop.setDouble(3, latitude);
			rs = stShop.executeQuery();
			if (rs.next()){
				id = rs.getInt(1);
			}
		} catch (SQLException e){
			throw new DataAccessorException(DataAccessorException.WRONG_REQUEST);
		} finally {
			dAccessor.finallyHandler(stShop, conn);
		}
		return id;
	}
	
	
	/**
	 * Permet d'obtenir l'identifiant d'un produit a partir de son nom
	 * @param name : le nom du produit
	 * @return l'id du produit, -1 si il n'esxiste pas 
	 * @throws DataAccessorException 
	 */
	
	public int getProdIdFromName(String name) throws DataAccessorException{
		Connection conn = DataAccessor.getConnection();  
		PreparedStatement stShop = null; 
		ResultSet rs = null;
		int id = -1;
		try{
			conn.setAutoCommit(false);
			stShop = conn.prepareStatement("select idproduct from product where name = ?");
			stShop.setString(1, name);
			rs = stShop.executeQuery();
			if (rs.next()){
				id = rs.getInt(1);
			}
			
		} catch (SQLException e){
			throw new DataAccessorException(DataAccessorException.WRONG_REQUEST);
		} finally {
			dAccessor.finallyHandler(stShop, conn);
		}
		return id;
	}
	
		
	/**
	 * Enregistre une entree de type (magasin, produit, prix) dans la table map_prod_shop,
	 * table qui represente tout les liens entre les produits et les magasins avec leur propre prix.
	 * @param shop : le magasin concerne
	 * @param prod : le produit concerne
	 * @param price : le prix du tuple
	 * @throws DataAccessorException 
	 */
	public void saveMapProdStore(Shop shop, Product prod, double price) throws DataAccessorException{
		Connection conn = DataAccessor.getConnection();  
		PreparedStatement stMap = null; 
		
		try{
			conn.setAutoCommit(false);  
			stMap = conn.prepareStatement("insert into map_prod_shop set id_shop=?, id_prod=?, price=?"); 
			stMap.setInt(1, shop.getId());
			stMap.setInt(2, prod.getId());
			stMap.setDouble(3, price);
			
			stMap.executeUpdate();
			conn.commit();
		} catch (BatchUpdateException e){
			throw new DataAccessorException(DataAccessorException.ALREADY_IN_DB);
		} catch (SQLException e) {
			throw new DataAccessorException(DataAccessorException.WRONG_REQUEST);
		}
		finally {
			dAccessor.finallyHandler(stMap, conn);
		}
	} 
	
	
	/**
     * Renvoie tous les magasins de la table Shop.
	 * @throws DataAccessorException 
     */
    public List<Shop> getShopsList() throws DataAccessorException{
        List<Shop> shopsList = new ArrayList<Shop>();

        Connection conn = DataAccessor.getConnection();
        PreparedStatement stShops = null;
        ResultSet rs = null;
        Shop s = null;

        try {
            stShops = conn.prepareStatement("select name, address, logo, openinghours, lattitude, longitude, city, characteristics, idshop from shop");
            rs= stShops.executeQuery();

            while (rs.next()){

                s = new Shop(rs.getString(1), rs.getString(2), rs.getString(3));
                 s.setOpeningHours(rs.getString(4));
                 s.setLattitude( Double.parseDouble(rs.getString(5)) );
                 s.setLongitude( Double.parseDouble(rs.getString(6)) );
                 s.setCity(rs.getString(7));
                 s.setCaracteristics(rs.getString(8));
                 s.setId(rs.getInt(9));
                shopsList.add(s);
            }

        } catch (SQLException e){
        	throw new DataAccessorException(DataAccessorException.WRONG_REQUEST);
        } finally {
        	dAccessor.finallyHandler(stShops, conn);
        }

        return shopsList;
    }

	
	
	/**
	 * Donne la liste des produits presents dans la base de donnee.
	 * @throws DataAccessorException 
	 */
	public List<Product> getProductsList() throws DataAccessorException{
		List<Product> productsList = new ArrayList<Product>();
		
		Connection conn = DataAccessor.getConnection();
		PreparedStatement stProducts = null;
		ResultSet rs = null;
		Product s = null;
		
		try {
			stProducts = conn.prepareStatement("Select name from product");
			rs= stProducts.executeQuery();
			
			while (rs.next()){
				s = new Product(rs.getString(1), "pcs");
				productsList.add(s);
			}
		} catch (SQLException e){
			throw new DataAccessorException(DataAccessorException.WRONG_REQUEST);
		} finally {
			dAccessor.finallyHandler(stProducts, conn);
		}
		return productsList;
	}

	
	
	/**
	 * Change le prix pour une relation magasin-produit
	 * @param shop : le magasin concerne
	 * @param prod : le produit concerne
	 * @param price : le nouveau prix
	 * @throws DataAccessorException 
	 */
	public void changePriceForProdShop(Shop shop, Product prod, double price) throws DataAccessorException{
		Connection conn = DataAccessor.getConnection();  
		PreparedStatement stMap = null; 
		
		try{
			conn.setAutoCommit(false);
			stMap = conn.prepareStatement("UPDATE map_prod_shop SET price = ?  WHERE id_shop=? and id_prod=?");
			stMap.setDouble(1, price);
			stMap.setInt(2, shop.getId());
			stMap.setInt(3, prod.getId());
			
			stMap.executeUpdate();
			conn.commit();
		} catch (SQLException e){
			throw new DataAccessorException(DataAccessorException.WRONG_REQUEST);
		} finally {
			dAccessor.finallyHandler(stMap, conn);
		}
	}
	
	/** Check si le produit donne en argument est present dans le magasin donne en argument
	 * @param idShop : l'id du magasin concerne
	 * @param idProd : l'id du produit concerne
	 * @throws DataAccessorException 
	 */
	public double checkIfProdInShop(int idShop, int idProd) throws DataAccessorException{
		Connection conn = DataAccessor.getConnection();   
		PreparedStatement stProdShop = null; 
		ResultSet rs = null;
		double prodShopPrice = -1;
		
		try {
			stProdShop = conn.prepareStatement("select m.price from map_prod_shop m, shop s, product p where s.idshop = m.id_shop and p.idproduct = m.id_prod and id_shop=? and id_prod=?"); //la requ�te
			stProdShop.setInt(1, idShop);
			stProdShop.setInt(2, idProd);
			
			rs= stProdShop.executeQuery();
			if (!rs.next()){   
				
				return -1;
			}
			prodShopPrice = rs.getDouble(1);

		} catch (SQLException e){
			throw new DataAccessorException(DataAccessorException.WRONG_REQUEST);
		} finally {
			dAccessor.finallyHandler(stProdShop, conn);
		}
		return prodShopPrice;
	}
	
	
	/**
	 * Selectionne les magasins dans lesquels le produit est present.
	 * @param product : le produit en question
	 * @throws DataAccessorException 
	 */
	public List<ShopPrice> selectShopsWhereProdExists(Product product) throws DataAccessorException{
       
        java.util.List<ShopPrice> shopsNames = new ArrayList<ShopPrice>();
        ShopPrice s = null;
        
       
        Connection conn = DataAccessor.getConnection();  
        PreparedStatement stProdShops = null; 
        ResultSet rs = null;
       
        try{
            conn=DataAccessor.getConnection();
            stProdShops = conn.prepareStatement("SELECT s.name, s.address, m.price, s.logo FROM shop s, map_prod_shop m, product p WHERE s.idshop = m.id_shop and p.idproduct = m.id_prod and p.name=? order by m.price");
            stProdShops.setString(1, product.getName());
            rs= stProdShops.executeQuery();
           
           
            while (rs.next()){
                s = new ShopPrice(rs.getString(1), rs.getString(2), rs.getDouble(3));
                s.setLogoUrl(rs.getString(4));
               
                shopsNames.add(s);
            }
           
        } catch (SQLException e) {
        	throw new DataAccessorException(DataAccessorException.WRONG_REQUEST);
        } finally {
        	dAccessor.finallyHandler(stProdShops, conn);
        }
       
        return shopsNames;
    }
		
	/**
	 * Donne le prix du produit donne en argument dans le magasin donne en argument.
	 * @param shop : le magasin en question
	 * @param prod : le produit en question
	 * @return le prix ou -1 si il n est pas vendu dans le magasin
	 * @throws DataAccessorException 
	 */
	public double getShopPrice(Shop shop, Product prod) throws DataAccessorException {
		
		Connection conn = DataAccessor.getConnection();  
		PreparedStatement stProdShops = null; 
		ResultSet rs = null;
		double price = -1;
		
		try{
			stProdShops = conn.prepareStatement("SELECT m.price FROM shop s, map_prod_shop m, product p WHERE s.idshop=? and p.name=? and s.idshop = m.id_shop and p.idproduct = m.id_prod"); 
			stProdShops.setInt(1, shop.getId());
			stProdShops.setString(2, prod.getName());
			rs= stProdShops.executeQuery();
			
			if (!rs.next()) {
				return -1;
			}
			price = rs.getDouble(1);
			
		} catch (SQLException e) {
			throw new DataAccessorException(DataAccessorException.WRONG_REQUEST);
		} finally {
			dAccessor.finallyHandler(stProdShops, conn);
		}
		price  = price*(prod.getQuantity());
		return price;
	}
	
	/**
	 * Donne la liste des magasins dans lequel la listes des produits existe.
	 * @param products : la liste des produits en question
	 * @throws DataAccessorException 
	 */
	public List<ShopPrice> getShopsForShopping(List<Product> products) throws DataAccessorException{
        List<Shop> shops = getShopsList();
        List<ShopPrice> toReturn = new ArrayList<ShopPrice>();
       
        double total;
		double tmp = 0; 
        for (Shop shop : shops) {
            total = 0;
            for (Product prod : products) {
        		tmp = getShopPrice(shop,prod);
        		total += tmp;
            }
            if (tmp > 0) { //0 si products empty, -1 dans quel cas ?
                ShopPrice s = new ShopPrice(shop, round(total));
                s.setLogoUrl(shop.getLogoUrl());
                toReturn.add(s);
            }
        }
       
        Collections.sort(toReturn, new Comparator<ShopPrice>() {
            @Override
            public int compare(ShopPrice o1, ShopPrice o2) {
                return (int)(o1.getPrice()*100)-(int)(o2.getPrice()*100);
            }
        });
        return toReturn;
    }
	
}
