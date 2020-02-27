package testCases;
import static org.junit.Assert.*;


import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import be.ac.ulb.infof307.g06.database.ShopProductDataAccessor;
import be.ac.ulb.infof307.g06.database.DataAccessor;
import be.ac.ulb.infof307.g06.database.DataAccessorException;
import be.ac.ulb.infof307.g06.database.FilterDataAccessor;
import be.ac.ulb.infof307.g06.model.Shop;

public class FilterDataAccessorTest {
	
	FilterDataAccessor filterDataAccessor;
	ShopProductDataAccessor dbHandler ; 
	
	@Before
	public void setUp() throws Exception {
		filterDataAccessor = FilterDataAccessor.getInstance();
        DataAccessor.setPassword("root");
		dbHandler = ShopProductDataAccessor.getInstance() ; 
	}
	
	@After
	public void tearDown() throws Exception {
		filterDataAccessor = null;
	}
	
	@Test
	public void selectShopsByNameTest() throws DataAccessorException {
		List<Shop> shopsList = filterDataAccessor.selectShopsByName("Carrefour");
		assertEquals(1, shopsList.size());
	}

	
	@Test
	public void selectShopsByCharacteristicsTest() throws DataAccessorException {
		 ArrayList<String> characteristicsList = new ArrayList<String>() ; 
		 characteristicsList.add("bio"); 
		List<Shop> shopsList = filterDataAccessor.selectShopsByCharacteristics(characteristicsList); 
		assertEquals(3, shopsList.size());
	}
	
	@Test
	public void filterDayAndTimeTest() throws DataAccessorException {
		List<Shop> shopsList = filterDataAccessor.filterDayAndTime(dbHandler.getShopsList(), "12:00", "lundi") ; 
		assertEquals(6, shopsList.size());
	}
	
	@Test
	public void filterDayTest() throws DataAccessorException {
		List<Shop> shopsList = filterDataAccessor.filterDay(dbHandler.getShopsList(), "dimanche") ; 
		assertEquals(5, shopsList.size());
	}
	

}
