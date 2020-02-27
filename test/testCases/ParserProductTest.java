package testCases;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import be.ac.ulb.infof307.g06.database.DataAccessor;
import be.ac.ulb.infof307.g06.database.DataAccessorException;
import be.ac.ulb.infof307.g06.database.NutritionalValueDataAccessor;
import be.ac.ulb.infof307.g06.database.ParserProduct;

public class ParserProductTest {
	
	private ParserProduct pp;
	private NutritionalValueDataAccessor nvda;

	@Before
	public void setUp() throws Exception {
		DataAccessor.setPassword("root");
		pp = ParserProduct.getInstance();
		nvda = NutritionalValueDataAccessor.getInstance();
	}

	@After
	public void tearDown() throws Exception {
	}
	@Test
	public void throwErrorIfIncorrectValues() {
		try{
			pp.parserText("test");
			fail("Incorrect handling");
		} catch(Exception e){
			assert(true);
		}
	}
	@Test
	public void isTrueIfParserTextInsertedValueCorrectly() {
		try{
			pp.parserText("testpp;pcs;1;2;3;8;Carrefour;rue du carrefour 55;5;\ntestpp2;pcs;1;2;3;8;Carrefour;rue du carrefour 55;5;");
			assertEquals("pcs", nvda.getUnitOfProduct("testpp2"));
			assertEquals("pcs", nvda.getUnitOfProduct("testpp"));
		} catch(DataAccessorException e){
			fail("Incorrect handling");
		} catch(Exception ex){
			fail("Incorrect handling");
		}
	}
}
