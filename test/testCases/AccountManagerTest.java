package testCases;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import be.ac.ulb.infof307.g06.database.AccountManager;
import be.ac.ulb.infof307.g06.database.CreateConnectAccountException;
import be.ac.ulb.infof307.g06.database.DataAccessor;

public class AccountManagerTest {
	

	AccountManager accountManager;
	@Before
	public void setUp() throws Exception {
        accountManager = AccountManager.getInstance();
        DataAccessor.setPassword("root");
        try {
        accountManager.insertToDatabase("usertest", "passtest1", "passtest1");
        } catch(Exception e) {
        	//si le compte existe deja
        }
	}
	
	@After
	public void tearDown() throws Exception {
		accountManager = null;
	}
	
	@Test(expected = CreateConnectAccountException.class)
	public void insertToDatabaseWrongPassword2Test() throws CreateConnectAccountException {
		String username = "usernamefalse";
		String password1 = "password1";
		String password2 = "password2";
		try{
			accountManager.insertToDatabase(username, password1, password2);
		} catch(CreateConnectAccountException e){
			throw e;
		} catch (Exception e){
			fail("Not expected exception");
		}
	}

	@Test(expected = CreateConnectAccountException.class)
	public void insertToDatabaseWrongUsernameTest() throws CreateConnectAccountException{
		String username = "user namefalse";
		String password = "password1";
		try{
			accountManager.insertToDatabase(username, password, password);
		} catch(CreateConnectAccountException e){
			throw e;
		} catch (Exception e){
			fail("Not expected exception");
		} 
	}
	
	@Test(expected = CreateConnectAccountException.class)
	public void insertToDatabaseWrongPasswordTest() throws CreateConnectAccountException{
		String username = "usernamefalse";
		String password = "pass word";
		try{
			accountManager.insertToDatabase(username, password, password);
		} catch(CreateConnectAccountException e){
			throw e;
		} catch (Exception e){
			fail("Not expected exception");
		}
	}
	
	
	@Test
	public void SecurConnectTest(){
		String username = "usertest";
		String password = "passtest1";
		try{
			accountManager.securisationConnect(username, password); //test aussi AccountManager.connect()
		}  catch(Exception e){
			fail("Not expected exception");
		}
	}
	
}
