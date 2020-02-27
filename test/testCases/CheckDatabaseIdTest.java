package testCases;
import static org.junit.Assert.*;


import be.ac.ulb.infof307.g06.database.CheckDatabaseId;

import org.junit.Test;

public class CheckDatabaseIdTest {

	CheckDatabaseId cdbi;
	
	@Test
	public void encryptAndWriteTest() {
		String toEncrypt = "encrypt_and_write_test";
		
		try{
			CheckDatabaseId.savePassword(toEncrypt);
		} catch (Exception e) {
			fail("Not expected exception");
		}
	}
	
	@Test
	public void correctPasswordEncryptAndCheckTest() {
		String toEncrypt = "root";
		try{
			CheckDatabaseId.savePassword(toEncrypt);
			
		} catch (Exception e) {
			fail("Not expected exception");
		}
		
		assertEquals(true,CheckDatabaseId.checkId());
	}

}
