package be.ac.ulb.infof307.g06.database;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.LocalTime;
import be.ac.ulb.infof307.g06.model.Shop;


/**
 * Listes de fonctions qui recherchent et filtrent les magasins sur la carte, selon certains criteres .
 */
public final class FilterDataAccessor {
	private DataAccessor dAccessor = DataAccessor.getInstance();
	private static final FilterDataAccessor INSTANCE = new FilterDataAccessor();
	private FilterDataAccessor(){
	}
	public static FilterDataAccessor getInstance(){
		return INSTANCE;
	}
	
	/**
	 * Recupere tous les magasin avec le nom : name 
	 * @param name : nom du magasin
	 * @return  listes des magasins avec le nom name
	 * @throws DataAccessorException
	 */
	public List<Shop> selectShopsByName(String name) throws DataAccessorException{
		
		Connection conn = DataAccessor.getConnection();
        PreparedStatement shopLst = null;
        try{
            shopLst = conn.prepareStatement("SELECT s.name, s.address, s.logo FROM shop s where s.name=?");
            shopLst.setString(1, name);
        } catch(SQLException e){
        	throw new DataAccessorException(DataAccessorException.DB_ACCESS);
        } 
    return executeShopList(conn, shopLst);
}
	
	
	/**
	 * Recupere la liste de magasins en fonction de la requete SQL shopLst
	 * @param conn : connection a la base de donnees
	 * @param shopLst : requete SQL sur la table des magasins
	 * @return liste des magasins 
	 * @throws DataAccessorException
	 */
    private List<Shop> executeShopList(Connection conn, PreparedStatement shopLst) throws DataAccessorException {
    	ResultSet rs = null;
    	List<Shop> shopsNames = new ArrayList<Shop>();
		Shop s = null;
		try{
            rs= shopLst.executeQuery();
           
            while (rs.next()){
                s = new Shop(rs.getString(1), rs.getString(2));
                s.setLogoUrl(rs.getString(3));
                shopsNames.add(s);  
            }
           
        } catch (SQLException e) {
        	throw new DataAccessorException(DataAccessorException.WRONG_REQUEST);
        } finally {
        	dAccessor.finallyHandler(shopLst, conn);
        }
        
        return shopsNames;
	}

	/**
	 * filtre par chatacteristiques : renvoi tous les magasins qui ont les characteristiques passees en parametre
	 * @param characteristicsList : liste des characteristiques 
	 * @return liste des magasins
	 * @throws DataAccessorException
	 */
	public List<Shop> selectShopsByCharacteristics( ArrayList<String> characteristicsList) throws DataAccessorException{
	
	Connection conn = DataAccessor.getConnection();
	PreparedStatement shopLst = null;
	try{
		shopLst = createCharactRequestWithoutName(characteristicsList);
	} catch (SQLException e) {
		throw new DataAccessorException(DataAccessorException.DB_ACCESS);
	} finally {
		dAccessor.finallyHandler(shopLst, conn);
		
	}
	return executeShopList(conn, shopLst);
	
}
	
	/**
	 * 
	 * @param characteristicsList : liste de characteristiques
	 * @return requete SQL 
	 * @throws SQLException
	 */
	private PreparedStatement createCharactRequestWithoutName(ArrayList<String> characteristicsList) throws SQLException {
		PreparedStatement shopLst;
		Connection conn = DataAccessor.getConnection();
		String request;
		String temp;
		request = "SELECT s.name, s.address, s.logo FROM shop s where s.name is not null ";
		for (int i=1; i<=characteristicsList.size(); i++){
			request += "and characteristics like ? " ;
		}
		shopLst = conn.prepareStatement(request);
		for (int i=0; i<characteristicsList.size(); i++){
			temp = "%"+characteristicsList.get(i)+"%";
			shopLst.setString(i+1, temp);
		}
		return shopLst;
	}
	
	
	/**
	 * Verifie si l'heure ClientInput se trouve dans la plage horaire entre l'heure start et end
	 * @param clientInput Heure a verifier
	 * @param start Heure de debut
	 * @param end Heure de fin
	 * @return True si ClientInput se trouve entre start et end, False sinon.
	 */
	public static boolean isBetween(LocalTime clientInput, LocalTime start, LocalTime end) {
			  return !clientInput.isBefore(start) && !clientInput.isAfter(end);  
	}
	
	/**
	 * Verifie si les magasins sont ouverts a l'heure input et au jour day
	 * @param shops liste de magasins
	 * @param input heure d'ouverture a verifier
	 * @param day jour d'ouverture a verifier
	 * @return liste des magasins ouvert a l'heure input au jour day
	 */
	public List<Shop> filterDayAndTime(List<Shop> shops, String input, String day) {
		
		
		LocalTime userInput = LocalTime.parse(input);
		List<Shop> openShops  = new ArrayList<>(); 
		
		List<String> days = new ArrayList<String>(); 
		days = Arrays.asList("lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi", "dimanche");
		
		for (Shop s : shops ){
			String[] time = s.getOpeningHours().split(";") ;
			
			String[] dailyOpeningHours = time[days.indexOf(day) ].split("-|\\:") ; 
			int startHour = Integer.parseInt(dailyOpeningHours[0]) ; 
			int startMinute = Integer.parseInt(dailyOpeningHours[1]) ; 
			int endHour = Integer.parseInt(dailyOpeningHours[2]) ;
			int endMinute = Integer.parseInt(dailyOpeningHours[3]) ;
					
			if ( isBetween(userInput , LocalTime.of(startHour, startMinute), LocalTime.of(endHour, endMinute))) {
				openShops.add(s) ; 
				}
			}
		return openShops ; 
	}
	
	/**
	 * Verifie si les magasins sont ouverts au jour day
	 * @param shops liste des magasins
	 * @param day jour d'ouverture a verifier
	 * @return liste des magasins ouvert au jour day
	 */
	public List<Shop> filterDay(List<Shop> shops, String day) {
		
		List<Shop> openShops  = new ArrayList<>(); 
		List<String> days = new ArrayList<String>(); 
		days = Arrays.asList("lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi", "dimanche") ;
		
		for (Shop s : shops){
			String time = s.getOpeningHours();
			String[] weekOpeningHours = time.split(";") ; 

			if (   !( weekOpeningHours[days.indexOf(day) ].trim().matches("00:00-00:00"))  ) {
				openShops.add(s) ; 
			}
		}
		return openShops ; 
	}
	
	/**
	 * Recupere les recherches de filtre precedentes faites par le user idUser
	 * @param idUser : id du user voulant recuperer ses recherches
	 * @return list des filtres recherchees precedemment 
	 * @throws DataAccessorException
	 */
	public java.util.List<String> getFiltreFromDb(int idUser) throws DataAccessorException{
		
		java.util.List<String> fList = new ArrayList<String>();
		Connection conn = DataAccessor.getConnection();  //pr�paration connexion 
		PreparedStatement stResearch = null; //pr�paration requ�te
		ResultSet rs = null;
		
		try {
			stResearch = conn.prepareStatement("SELECT h.research FROM history h WHERE h.idclient=?");
			stResearch.setInt(1, idUser);
			rs= stResearch.executeQuery();
			
			while (rs.next()){
				fList.add(rs.getString(1))  ; 
			}
			
		} catch (SQLException e){
			throw new DataAccessorException(DataAccessorException.WRONG_REQUEST);
		} finally {
			dAccessor.finallyHandler(stResearch, conn);
		}		
		return fList;
	}
	
	/**
	 * Verifie si la recherche n'existe pas deja�dans la base de donnees
	 * @param userId : id du user dans la base de donnees
	 * @param fltr : filtre 
	 * @return True si la base de donnee a deja la recherche, False sinon
	 * @throws DataAccessorException
	 */
	public boolean researchAlreadyInDb(int userId, String fltr) throws DataAccessorException{
		java.util.List<String> fList = getFiltreFromDb(userId) ;
		 return fList.contains(fltr) ; 
	}
	
	/**
	 * Sauvegarde la recherche de filtre fltr faite par le user
	 * @param userId : id du user dans la base de donnees
	 * @param fltr : filtre 
	 * @return True si la recherche est sauvergardee, False sinon
	 * @throws DataAccessorException
	 */
	public Boolean saveResearch(int userId, String fltr) throws DataAccessorException{
		
		Connection conn = DataAccessor.getConnection();   
		PreparedStatement stResearch = null; 
		
		if (!researchAlreadyInDb(userId, fltr)) {
			try{
				conn.setAutoCommit(false);  
				stResearch = conn.prepareStatement("insert into history set idclient=?,research=? "); 
				stResearch.setInt(1, userId) ; 
				stResearch.setString(2, fltr);
				
				stResearch.executeUpdate();  
				conn.commit();
				
				return true;
				
			} catch (BatchUpdateException e){
				throw new DataAccessorException(DataAccessorException.ALREADY_IN_DB);
			} catch (SQLException e){
				throw new DataAccessorException(DataAccessorException.WRONG_REQUEST);
			} finally {
				dAccessor.finallyHandler(stResearch, conn);
			}
		}
		
		return false;
	}
}



