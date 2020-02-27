package testCases;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import be.ac.ulb.infof307.g06.model.Product;

public class ProductTest {
	
	private static Product p;

	@Before
	public void setUp() throws Exception {
		p = new Product("poires",4);
		
	}

	@After
	public void tearDown() throws Exception {
		p = null;
	}

	@Test
	public void isTrueIfQuantityIsCorrectedlyMultiplied() {
		p.mulQuantity(2);
		assertEquals(8, p.getQuantity(), 0);
	}
	
	@Test
	public void isTrueIfQuantityIsCorrectedlyIncreased() {
		p.incQuantity(2);
		assertEquals(6, p.getQuantity(), 0);
	}
	
	@Test
	public void isTrueIfQuantityIsCorrectedlyDecreased() {
		p.decQuantity(2);
		assertEquals(2, p.getQuantity(), 0);
	}

}
