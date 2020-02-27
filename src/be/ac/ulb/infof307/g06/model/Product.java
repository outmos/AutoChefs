package be.ac.ulb.infof307.g06.model;
 
 
/** Classe generale representant un produit */
public class Product {
  /**
   * id du produit
   */
  protected int idProduit;
  /**
   * unite du produit
   */
  protected String unit;
  /**
   * quantite du produit
   */
  protected double quantity;
  /**
   * nom du produit
   */
  protected String name;
  /**
   * kcal du produit
   */
  protected double kcal;
  /**
   * proteine du produit
   */
  protected double proteins;
  /**
   * lipide du produit
   */
  protected double lipids;
  /**
   * glucide du produit
   */
  protected double glucids;
  
  
  /**
   * Constructeur 
   */
  public Product() {//empty constructeur 
  }
  
  /**
   * Constructeur
   * @param idProduct
   * @param name
   */
  public Product(final int idProduct, final String name) {
	  this.idProduit = idProduct;
	  this.name = name;
  }
  
  /**
   * Constructeur
   * @param name
   * @param quantity
   */
  public Product(final String name, final double quantity) {
      this.name=name;
      this.quantity = quantity;
  }
  
  /**
   * COnstructeur
   * @param name
   * @param unit
   * @param quantity
   */
  public Product(final String name, final String unit, final double quantity) {
	  this.name=name;
	  this.unit = unit ;   
	  this.quantity = quantity;
  }
  
  /**
   * Constructeur
   * @param name
   * @param unit
   */
  public Product(final String name, final String unit){
	  this.name = name ; 
	  this.unit = unit ; 
  }
  
  /**
   * Constructeur
   * @param name
   * @param unit
   * @param kcal
   * @param proteins
   * @param lipids
   * @param carbohydrates
   */
  public Product(final String name,final String unit,final double kcal,
		  final double proteins,final double lipids,final double carbohydrates) {
      this(name, unit) ;   
        
      this.kcal = kcal;
      this.proteins = proteins;
      this.glucids = carbohydrates;
      this.lipids = lipids;
  }
  
  /**
   * Constructeur
   * @param name
   * @param unit
   * @param quantity
   * @param kcal
   * @param proteins
   * @param lipids
   * @param carbohydrates
   */
  public Product(final String name, final String unit, final double quantity,
		  final double kcal, final double proteins, final double lipids, final double carbohydrates) {
	  this(name, unit, kcal, proteins, lipids, carbohydrates);
	  this.quantity = quantity;
  }
  /**
   * Copy constructeur
   * @param product
   */
  public Product(final Product product) {
      this.name=product.getName();
      this.unit = product.getUnit() ;   
      this.quantity = product.getQuantity();
      this.kcal = product.getKcal();
      this.proteins = product.getProteins();
      this.glucids = product.getGlucids();
      this.lipids = product.getLipids();
  }
  /**
   * getter des calories d'un produit
   */
  public double getKcal() {
	  return kcal;
  }
  
  /**
   * setter des calories d'un produit
   * @param kcal
   */
  public void setKcal(final double kcal) {
	  this.kcal = kcal;
  }
	
  /**
   * getter des proteines d'un produit
   */
  public double getProteins() {
	  return proteins;
  }
	 
  /**
   * setter des proteines d'un produit
   * @param proteins
   */
  public void setProteins(final double proteins) {
	  this.proteins = proteins;
  }
  
  /**
   * getter des lipides d'un produit
   */
  public double getLipids() {
	  return lipids;
  }
	 
  /**
   * setter des lipides d'un produit
   * @param lipids
   */
  public void setLipids(final double lipids) {
	  this.lipids = lipids;
  }
  
  /**
   * getter des glucides d'un produit
   */
  public double getGlucids() {
	  return glucids;
  }
	 
  /**
   * setter des glucides d'un produit
   * @param carbohydrates
   */
  public void setGlucids(final double carbohydrates) {
	  this.glucids = carbohydrates;
  }
	 
	        
  /**
   * arrondi d'un float a 1 decimale
   * @param input
   */  
  private static double round(final double input) {
	  return Math.round(input * 100.0) / 100.0;
  }
	 
  
  /**
   * getter de l'id d'un produit
   */
  public int getId() {
	  return idProduit;
  }
	  
  /**
   * setter de l'id d'un produit
   * @param idProduit
   */
  public void setId(final int idProduit) {
	  this.idProduit = idProduit;
  }
		 
  /**
   * getter de l'unite d'un produit
   */
  public String getUnit() {
	  return unit;
  }

  /**
   * setter de l'unite d'un produit
   * @param unit
   */
  public void setUnit(final String unit) {
	  this.unit = unit;
  }
		 
  /**
   * getter du nom d'un produit
   */
  public String getName() {
	  return name;
  }

  /**
   * setter du nom d'un produit
   * @param name
   */
  public void setName(final String name) {
	  this.name = name;
  }

  /**
   * getter de la quantite d'un produit
   */
  public double getQuantity() {
	  return quantity;
  }
	
  /**
   * setter d'une quantite d'un produit
   * @param newQuantity
   */
  public void setQuantity(final double newQuantity) {
      quantity = round(newQuantity);
  }
		   
  /**
   * multiplication des quantite d'un produit
   * @param factor
   */
  public void mulQuantity(final double factor) {
	  quantity = round(quantity*factor);
  }
		    
  /**
   * incrementation de la quantite d'un produit
   * @param inc
   */
  public void incQuantity(final double inc) {
	  quantity = round(quantity+inc);	       
  }
		 
  /**
   * decrementation de la quanite d'un produit
   * @param dec
   */
  public void decQuantity(int dec) {
	  if (quantity > dec) {
		  quantity = round(quantity-dec);
	  }       
  	}
}
