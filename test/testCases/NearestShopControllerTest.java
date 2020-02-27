package testCases;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import be.ac.ulb.infof307.g06.controllers.NearestShopController;
import be.ac.ulb.infof307.g06.utils.BridgeSingleton;

public class NearestShopControllerTest {
	NearestShopController controller;
	BridgeSingleton bridge;
	final int valid = 1260;
	
	@Before
	public void setUp() throws Exception {
		bridge = BridgeSingleton.getInstance();
	}

	@After
	public void tearDown() throws Exception {
		controller = null;
		bridge = null;
	}
	
	@Test
	public void IsTrueIfDistanceIsEqualToExpected(){
		int distance = 0;
		try {
			distance = bridge.getFastestRoute("50.81559389999999,4.38328039999999","50.8183539,4.397580800000014", "driving");
		} catch (IOException e) {
			fail("fail du test");
		}
		assertEquals(valid, distance);
	}

}
