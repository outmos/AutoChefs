package testCases;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import be.ac.ulb.infof307.g06.database.DataAccessor;
import be.ac.ulb.infof307.g06.database.DataAccessorException;
import be.ac.ulb.infof307.g06.database.NutritionalValueDataAccessor;

public class NutritionalValueDataAccessorTest {

NutritionalValueDataAccessor nutritionalValueDataAccesor;
	
	@Before
	public void setUp() throws Exception {
        nutritionalValueDataAccesor = NutritionalValueDataAccessor.getInstance();
        DataAccessor.setPassword("root");
	}
	
	@After
	public void tearDown() throws Exception {
		nutritionalValueDataAccesor = null;
	}
	
	@Test
	public void getKcalOfProductTest() throws NumberFormatException, DataAccessorException {
		double kcal = 83.00;
		assertEquals(kcal, nutritionalValueDataAccesor.getKcalOfProduct("pomme"), 0.01);
	}

	@Test
	public void getLipidOfProductTest() throws NumberFormatException, DataAccessorException {
		double lipid = 47.00;
		assertEquals(lipid, nutritionalValueDataAccesor.getLipidOfProduct("pomme"), 0.01);
	}
	
	@Test
	public void getUnitOfProductTest() throws DataAccessorException {
		String unit = "pcs";
		assertEquals(unit, nutritionalValueDataAccesor.getUnitOfProduct("pomme"));
	}
	
	@Test
	public void getProteinOfProductTest() throws NumberFormatException, DataAccessorException {
		double protein = 6.00;
		assertEquals(protein, nutritionalValueDataAccesor.getProteinOfProduct("pomme"), 0.01);
	}
	
	@Test
	public void getGlucidOfProductTest() throws NumberFormatException, DataAccessorException {
		double glucid = 54.00;
		assertEquals(glucid, nutritionalValueDataAccesor.getGlucidOfProduct("pomme"), 0.01);
	}
	
	@Test(expected = DataAccessorException.class)
	public void getKcalOfProductThrowTest() throws DataAccessorException {
		try {
			nutritionalValueDataAccesor.getKcalOfProduct("");
		} catch (NumberFormatException ne){
			fail("Not expected exception");
		} catch (DataAccessorException e){
			throw e;
		}
	}
}
