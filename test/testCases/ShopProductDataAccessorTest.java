package testCases;
import static org.junit.Assert.*;import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import be.ac.ulb.infof307.g06.database.DataAccessor;
import be.ac.ulb.infof307.g06.database.DataAccessorException;
import be.ac.ulb.infof307.g06.database.NutritionalValueDataAccessor;
import be.ac.ulb.infof307.g06.database.ShopProductDataAccessor;
import be.ac.ulb.infof307.g06.model.Product;
import be.ac.ulb.infof307.g06.model.Shop;
import be.ac.ulb.infof307.g06.model.ShopPrice;

public class ShopProductDataAccessorTest {

	ShopProductDataAccessor spda;
	NutritionalValueDataAccessor nvop;
	Shop s1;
	Shop bio;
	Shop lidl;
	Shop s2;
	Product p;
	Product avocat;
	Product cerise;
	Product mandarine;
	Product straciatella;
	Product croquettes;
	Product pate;
	
	@Before
	public void setUp() throws Exception {
		DataAccessor.setPassword("root");
		spda = ShopProductDataAccessor.getInstance() ;
		nvop = NutritionalValueDataAccessor.getInstance();
		s1 = new Shop("Carrefour" ,"rue du carrefour 55" ,"/images/carrefour.jpg" ,"Bruxelles" ,"Bio;Local " ,"09:00-19:00;09:00-19:00;09:00-19:00;09:00-19:00;09:00-19:00;10:00-16:00;00:00-00:00" ,4.660900, 4.351996);
		s2 = new Shop("Match" ,"rue du match 55" ,"/images/match.gif" ,"Bruxelles" ,"Bio;Local" ,"09:00-15:00;09:00-19:00;09:00-19:00;09:00-19:00;09:00-19:00;10:00-16:00;10:00-12:00" ,4.6624, 4.351997);
		bio = new Shop("BioPlanet" ,"rue du bio 55" ,null ,"Bruxelles" ,"Bio;Local" ,"09:00-15:00;09:00-19:00;09:00-19:00;09:00-19:00;09:00-19:00;10:00-16:00;10:00-12:00" ,4.52624, 4.451997);
		p = new Product("pizza", "pcs", 78, 3.5, 2.2, 1.1);
		avocat = new Product(null, "pcs", 320, 69, 120, 50);
		lidl = new Shop("Lidl", "rue du Lidl 22", null, "Nivelles", "bio", "09:00-15:00;09:00-19:00;09:00-19:00;09:00-19:00;09:00-19:00;10:00-16:00;10:00-12:00" ,50.6, 4.8353);
		cerise = new Product("cerise", "g", 69, 2, 6, 25);
		mandarine = new Product("mandarine", "g", 25, 3, 6, 36);
		straciatella = new Product("straciatella", "g", 236, 25, 36, 67);
		croquettes = new Product("croquettes", "g", 125, 26, 35, 42);
		pate = new Product("pate", "g", 256, 16, 23, 67);
	}
	
	@After
	public void tearDown() throws Exception {
		spda = null;
	}
	
	@Test
	public void changeProductTest() throws DataAccessorException{
		spda.saveProduct(p);
		p.setKcal(600);
		spda.changeProduct(p);
		String unit = nvop.getUnitOfProduct(p.getName());
		Double kcal = nvop.getKcalOfProduct(p.getName());
		Double prot = nvop.getProteinOfProduct(p.getName());
		Double lipid = nvop.getLipidOfProduct(p.getName());
		Double glucid = nvop.getGlucidOfProduct(p.getName());
		assertEquals("pcs", unit);
		assertEquals(600, kcal, 0.01);
		assertEquals(3.5, prot, 0.01);
		assertEquals(2.2, lipid, 0.01);
		assertEquals(1.1, glucid, 0.01);
	}
	
	
	@Test
	public void getShopsListTest() throws DataAccessorException{
		
		List<Shop> shopsList = spda.getShopsList();
		int listSize = shopsList.size();
		spda.saveShop(bio);
		shopsList = spda.getShopsList();
		assertEquals(listSize+1, shopsList.size());
	}
	
	
	@Test(expected = DataAccessorException.class)
	public void saveProductTest() throws DataAccessorException{
		
		spda.saveProduct(avocat);

	} 
	
	
	public void saveShopTest() throws DataAccessorException{
		
		spda.saveShop(lidl);
		int id = spda.getShopIdFromCoordinates(50.6, 4.8353);
		assertNotEquals(0, id);
	} 
	
	
	@Test
	public void getProductsListTest() throws DataAccessorException{
		List<Product> productsList = spda.getProductsList();
		int listSize = productsList.size();
		spda.saveProduct(cerise);
		productsList = spda.getProductsList();
		assertEquals(listSize+1, productsList.size());
	} 
	
	
	@Test
	public void selectShopsWhereProdExistsTest() throws DataAccessorException{
		s1.setId(spda.getShopIdFromCoordinates(s1.getLatitude(), s1.getLongitude()));
		cerise.setId(spda.getProdIdFromName(cerise.getName()));
		spda.saveMapProdStore(s1, cerise, 7.2);
		List<ShopPrice> shops = spda.selectShopsWhereProdExists(cerise);
		assertEquals(1, shops.size());
	} 
	
	
	@Test
	public void getShopPriceTest() throws DataAccessorException{
		Product p = new Product("orange", "pcs", 1, 320, 69, 120, 50);
		spda.saveProduct(p);
		p.setId(spda.getProdIdFromName(p.getName()));
		s1.setId(spda.getShopIdFromCoordinates(s1.getLatitude(), s1.getLongitude()));
		spda.saveMapProdStore(s1, p, 2.2);
		double price = spda.getShopPrice(s1, p);

		assertEquals(2.2, price, 0.001);
	} 
	
	@Test
	public void getShopIdFromNameAddressTest() throws DataAccessorException{
		
		int id = spda.getShopIdFromNameAddress(s1.getName(), s1.getAddress());
		assertNotEquals(-1, id);
	}
	
	@Test
	public void getShopIdFromCoordinatesTest() throws DataAccessorException{
		int id = spda.getShopIdFromCoordinates(s1.getLatitude(), s1.getLongitude());
		assertNotEquals(0, id);
	}
	
	@Test
	public void getShopIdFromNameCoordTest() throws DataAccessorException{
		int id = spda.getShopIdFromNameCoord(s1.getName(), s1.getLongitude(), s1.getLatitude());
		assertNotEquals(0, id);
	}
	
	@Test
	public void getShopIdFromNameTest() throws DataAccessorException{
		List<Integer> ids = spda.getShopIdFromName(s1.getName());
		assertEquals(1, ids.size());
	}
	
	@Test 
	public void getProdIdFromNameTest() throws DataAccessorException{
		int id = spda.getProdIdFromName(p.getName());
		assertNotEquals(-1, id);
	}
	
	
	@Test
	public void saveMapProdStoreTest() throws DataAccessorException{
		Product p = new Product("raisins", "g", 69, 2, 6, 25);
		spda.saveProduct(p);
		s1.setId(spda.getShopIdFromCoordinates(s1.getLatitude(), s1.getLongitude()));
		p.setId(spda.getProdIdFromName(p.getName()));
		spda.saveMapProdStore(s1, p, 7.99);
		double price = spda.checkIfProdInShop(s1.getId(), p.getId());
		assertEquals(7.99, price, 0.01);
	} 
	
	
	
	@Test
	public void changePriceForProdShopTest() throws DataAccessorException{
		s1.setId(spda.getShopIdFromCoordinates(s1.getLatitude(), s1.getLongitude()));
		spda.saveProduct(straciatella);
		spda.saveMapProdStore(s1, straciatella, 2.5);
		spda.changePriceForProdShop(s1, straciatella, 4.3);
		double price = spda.checkIfProdInShop(s1.getId(), straciatella.getId());
		assertEquals(4.3, price, 0.01);
	} 
	
	
	@Test
	public void checkIfProdInShopTest() throws DataAccessorException{
		int idShop = spda.getShopIdFromCoordinates(s1.getLatitude(), s1.getLongitude());
		s1.setId(idShop);
		int idProd = spda.saveProduct(mandarine);
		mandarine.setId(idProd);
		spda.saveMapProdStore(s1, mandarine, 2.5);
		
		double price = spda.checkIfProdInShop(idShop, idProd);
		assertEquals(2.5, price, 0.01);
	}  
    
    @Test
	public void getShopsForShoppingTest() throws DataAccessorException{
		int id1 = spda.saveProduct(croquettes);
		int id2 = spda.saveProduct(pate);
		croquettes.setId(id1);
		pate.setId(id2);
		List<Product> products = new ArrayList<>();
		croquettes.setQuantity(1);
		pate.setQuantity(1);
		products.add(croquettes);
		products.add(pate);
		s1.setId(spda.getShopIdFromCoordinates(s1.getLatitude(), s1.getLongitude()));
		s2.setId(spda.getShopIdFromCoordinates(s2.getLatitude(), s2.getLongitude()));
		List<Shop> ourShops = new ArrayList<>();
		ourShops.add(s1);
		ourShops.add(s2);
		spda.saveMapProdStore(s1, croquettes, 2.3);
		spda.saveMapProdStore(s2, croquettes, 2.4);
		spda.saveMapProdStore(s1, pate, 3.2);
		spda.saveMapProdStore(s2, pate, 3.5);
		List<ShopPrice> shops = spda.getShopsForShopping(products);
		assertEquals(2, shops.size());
		for (int i = 0; i<shops.size(); i++){
			assertEquals(ourShops.get(i).getName(), shops.get(i).getName());
		}
	} 	
	
	

}
