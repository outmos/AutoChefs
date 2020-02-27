package testCases;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import be.ac.ulb.infof307.g06.model.ShoppingList;
import be.ac.ulb.infof307.g06.model.Product;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ShoppingListTest {
	ShoppingList obj;
	List<Product> productsList;
	Product apple, banana, orange;
	
	@Before
	public void setUp() throws Exception {
		
		obj  = new ShoppingList();
		productsList = new ArrayList<Product>();
		
		// Set of products for testing . . .
		apple = new Product("apple", 1.3);
		banana = new Product("banana", 0.4);
		orange = new Product("orange", 0.6);
		
		// Adding test products to list . . .
		productsList.add(apple);
		productsList.add(banana);
		productsList.add(orange);
		
		// Add list to ShoppingList obj . . .
		obj.setProductsList(productsList);
	}

	@After
	public void tearDown() throws Exception {
		obj = null;
		productsList = null;
		apple = null; banana = null; orange = null;
	}

	@Test
	public void IsTrueIfItemIsRemovedFromList() {
		assertEquals(true, obj.remove(0));
	}
	
	@Test
	public void IsTrueIfItemAddedToList(){
		Product kiwi = new Product("kiwi", 1.4); obj.add(kiwi);
		List<Product> tmpList= obj.getProductsList();
		Boolean kiwi_is_in = tmpList.get(tmpList.size()-1).getName().contains("kiwi");
		
		assertEquals(true, kiwi_is_in);
	}
	
	@Test
	public void IsTrueIfAddeddNewListToOldList(){
		Boolean flag = true;
		ShoppingList test_list = new ShoppingList();
		List<Product> products = new ArrayList<>();
		
		// Set of products for testing . . .
		Product kiwi = new Product("kiwi", 0.3);
		Product ananas = new Product("ananas", 0.4);
		Product lemon = new Product("lemon", 0.6);
				
		// Adding test products to list . . .
		products.add(kiwi);
		products.add(ananas);
		products.add(lemon);
		
		test_list.setProductsList(products);
		
		obj.addList(test_list);
		for (Product p: test_list.getProductsList()){
			String name = p.getName();
			Boolean test=false;
			for (Product tmp:products){
				if (tmp.getName().contains(name)){
					test=true;
				}
			}
			flag &= test;
		}
		
		assertEquals(true, flag);
	}
	
	@Test
	public void IsIncrementedIfItemQuantityIncreasedByOne(){
		// increment apple's quantity
		obj.inc(0);
		
		// get apple product
		Product p = obj.get(0);
		
		// testing . . .
		assertEquals(2.3, p.getQuantity(), 0.01);
	}
	
	@Test
	public void IsDecrementedIfItemQuantityDecreasedByOne(){
		// increment apple's quantity
		obj.dec(0);
		
		// get apple product
		Product p = obj.get(0);
		
		// testing . . .
		assertEquals(0.3, p.getQuantity(), 0.01);
	}
	
	@Test
	public void QuantityIsChangedIfItemQuantityIsFactorTimesBigger(){
		// change quantity of products with factor = 2 . . .
		obj.changeQuantity(2.0);
		
		// get apple product . . .
		Product p = obj.get(0);
		
		assertEquals(2.6, p.getQuantity(), 0.01);
	}
}
