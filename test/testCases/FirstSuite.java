package testCases;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({AccountManagerTest.class, CheckDatabaseIdTest.class, FilterDataAccessorTest.class, FilterTest.class,
	NearestShopControllerTest.class, NutritionalValueDataAccessorTest.class, ParserProductTest.class, 
	RecipeDataAccessorTest.class, RecipeListTest.class, ShoppingListTest.class, ProductTest.class})
@RunWith(Suite.class)
public class FirstSuite {}
