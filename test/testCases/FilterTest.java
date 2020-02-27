package testCases;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import be.ac.ulb.infof307.g06.database.DataAccessor;
import be.ac.ulb.infof307.g06.database.DataAccessorException;
import be.ac.ulb.infof307.g06.model.Filtre;
import be.ac.ulb.infof307.g06.model.Shop;

public class FilterTest {
	Filtre ftr;
	@Before
	public void setUp() throws Exception {
		DataAccessor.setPassword("root");
		ftr = new Filtre("test", null, "test2", true, true, false, false);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void isTrueIfgetResearchCorrect() {
		assertEquals(ftr.getResearch(), "name test;hour test2;1;1;0;0;");
	}
	@Test
	public void isTrueIfConstructorResearchIsCorrect() {
		Filtre newftr = ftr.createFiltreFromResearch(ftr.getResearch());
		assertEquals("name test;hour test2;1;1;0;0;", newftr.getResearch());
	}
	
	@Test
	public void isTrueIfCreateListShopIsCorrectBio() throws DataAccessorException {
		Filtre newftr = new Filtre(null, null, null, true, false, false, false);
		List<Shop> ls = newftr.createListShop();
		assertEquals(3, ls.size());
	}
	
	@Test
	public void isTrueIfCreateListShopIsCorrectCarrefour() throws DataAccessorException {
		Filtre newftr = new Filtre("Carrefour", null, null, false, false, false, false);
		List<Shop> ls = newftr.createListShop();
		assertEquals(1, ls.size());
	}
	
	@Test
	public void isTrueIfCreateListShopIsCorrectLundi() throws DataAccessorException {
		Filtre newftr = new Filtre(null, "lundi", null, false, false, false, false);
		List<Shop> ls = newftr.createListShop();
		assertEquals(6, ls.size());
	}
	
	@Test
	public void isTrueIfCreateListShopIsCorrectLundiHour() throws DataAccessorException {
		Filtre newftr = new Filtre(null, "lundi", "12:00", false, false, false, false);
		List<Shop> ls = newftr.createListShop();
		assertEquals(6, ls.size());
	}
}
