package Algorithm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import tools.PropertiesUtil;




/**
 * Author Salma Najar 
 * 
 **/

public class MarkovChain extends AbstractClassification {
	

	public static String configUrl = "Config.properties";
	public static String configFile = "conf/Config.properties";

	public static PropertiesUtil propertiesUtil = new PropertiesUtil(configFile, configUrl);
	public static Properties prop = propertiesUtil.getProperties();

	public static void MarkovChain(){
		
	}

	@Override
	public void doClassification(int LastCount, int New_0bservations_Count) {
		// TODO Auto-generated method stub
		//System.out.println("-----Step1");
		//Step1: Create Map containing all the next situation for a situation S
		HashMap <Integer, List> MapNextSituation = new HashMap <Integer, List> ();
		MapNextSituation= getAllNextSituationsMap(LastCount,New_0bservations_Count);
		//System.out.println("-----Step2");
		
		//Step 2: Déterminer pour une situation S son NSS' et son NSS" avec S" ≠ S'
		HashMap <List, List> Map = new HashMap <List, List> ();		
		Map = getPossibleSituationsFromSMap(MapNextSituation);
		//System.out.println("-----Step3");
		
		// Step 3: Update DataBase
		updateTransitionMatrix(Map);
	}
	
	@Override
	public void doClassification() {
		
		 int LastCount = Integer.parseInt(prop.getProperty("LastCount"));
		// System.out.println("----LastCount is : "+ LastCount);
		 int New_0bservations_Count = 0;
		 int CurrentCount= 0;
		 ResultSet r;
		 
		 try{
			 	Sqlite maBase = new Sqlite();
	    		maBase.connect();
	    		String sql = "SELECT count(*) FROM Trace";
	            r = maBase.executeQuery(sql);
	            while (r.next())
	            {
	            	CurrentCount = Integer.parseInt(r.getString(1));
	            	System.out.println("----CurrentCount : "+ CurrentCount);
	            }  
	            System.out.println("-----Step0");
	            maBase.disconnect();
	            New_0bservations_Count = CurrentCount - LastCount;
	            
	           // System.out.println("------New_0bservations_Count: "+ New_0bservations_Count);
               
		        int param = Integer.parseInt(prop.getProperty("clustering_param"));
		        
		        if (New_0bservations_Count > param){
		        			        	
		        	IClassificationFacade MC= new MarkovChain();
	                MC.doClassification (LastCount, New_0bservations_Count);
		        	System.out.println("-----Step4");
		        	//prop.setProperty("LastCount", Integer.toString(CurrentCount));
	                //prop.store(new FileOutputStream("conf/config.properties"), null);
	                
	                //System.out.println(prop.getProperty("LastCount"));
		        }
		        else{
		        	System.out.println("It has no sufficient observations to launch the clustering process");
		        }
	    	}
	    	
		 catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		    }
		
	}
	
	/**
	 * 
	 * @param date1
	 * @param date2
	 * @param time
	 * @return boolean: if withinTenMins or not
	 */
	
	public static boolean withinXMinutes(Date date1, Date date2, int time) {

		boolean withinXMins = false;
		
		long diff = date2.getTime() - date1.getTime();            
        long diffMinutes = diff / (60 * 1000);         
                           
        //System.out.println("Time in minutes: " + diffMinutes + " minutes.");         
        
		if (diffMinutes <= time) {
			withinXMins = true;
		}
		
		return withinXMins;
	}
	
	/**
	 * 
	 * @param str
	 * @return only the time from a timeStamp
	 */
	
	 public static String getTime(String str){
		
		 StringTokenizer st = new StringTokenizer(str, " ");
		 String Date = st.nextToken();
		 String time = st.nextToken();
		 
		 return time;
	 }
	 
	 /**
	  * 
	  * @return the Difference time allowed from the config.properties file
	  */
	 
	public static int getDiffTimeAllowed(){
		
		int DiffTimeAllowed = Integer.parseInt(prop.getProperty("Time"));
		
		return DiffTimeAllowed;
	
	}
	
	/**
	 * 
	 * @param r
	 * @return a map containing a last state S with a next state S'
	 * @throws ParseException 
	 */
	
	//public static HashMap <Integer, Integer> getAllNextSituationsMap(ResultSet r){
		
	    public static HashMap <Integer, List> getAllNextSituationsMap(int LastCount, int New_0bservations_Count){
		//System.out.println("--------------------------------------------");
	    //System.out.println("-------getAllNextSituationsMap--------------");
		//System.out.println("--------------------------------------------");
	    int DiffTimeAllowed = getDiffTimeAllowed();
			
		String TimeStamp_S1 = null;
		String Time_S1 = null;
		Date Date_S1 = null;
		int Cluster_S1 = 0;
		
		String TimeStamp_S2 = null;
		String Time_S2 = null;
		Date Date_S2 = null;
		int Cluster_S2 = 0;
			
		HashMap <Integer, List> Map = new HashMap <Integer, List> ();
		//DateFormat formatter = new SimpleDateFormat("hh:mm:ss");
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		ResultSet r = null;
	
		Sqlite maBase = new Sqlite();
		maBase.connect();
	
		//Create Map containing all the next situation for a situation S
		try {
			String sql = "SELECT * From Trace ORDER BY ID Limit "+ LastCount+", "+ New_0bservations_Count;
			r = maBase.executeQuery(sql);
			int i =0;
			
			
			HashMap <Integer, List> MapClusters = new HashMap <Integer, List> ();
			
			
			while (r.next()){
				List ListClusters = new ArrayList();
				ListClusters.add(r.getString("TimeStamp"));
				ListClusters.add(Integer.parseInt(r.getString("ClusterID")));
		    	
		    	MapClusters.put(i++, ListClusters);
		    	
			}
			
			Iterator it = MapClusters.entrySet().iterator();
			//System.out.println("*****MapTrace : "+ MapClusters);
			
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry)it.next();
				int key = (Integer) pairs.getKey();
		        List value = (List) pairs.getValue();
		        //System.out.println("*****Iterator : "+ key + "--TimeStamp : "+value.get(0) + "--ClusterID : "+ value.get(1));
				
		        TimeStamp_S1 = value.get(0).toString();

		        Date_S1 = format.parse(TimeStamp_S1);
		        Cluster_S1 = (Integer) value.get(1);

		        int key2 = key+1;
		        if (key2 < MapClusters.size()){
		        	List value2 = MapClusters.get(key2);
			        
			        
			        TimeStamp_S2 = value2.get(0).toString();

			        Date_S2 = format.parse(TimeStamp_S2);
			        Cluster_S2 = (Integer) value2.get(1);
			        
			        if (withinXMinutes(Date_S1, Date_S2, DiffTimeAllowed)){ 
			   		    List ListDateInt = new ArrayList();
		    	    	ListDateInt.add(Cluster_S1);
		    	    	ListDateInt.add(Cluster_S2);
		    	    	
					 	Map.put(i++, ListDateInt);
					 	//System.out.println("-------Added to Map [ "+ ListDateInt.get(0)+ ", "+ListDateInt.get(1)+ "]");
						//System.out.println("--------------------------------------------");
						//System.out.println("--------------------------------------------");
					
				 }
		        }
		        
		}
			maBase.disconnect();
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("SQLException getAllNextSituationsMap : " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			System.err.println("SQLException  getAllNextSituationsMap : " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		
		//Debug("End step1");
		return Map;
		
	}
	
	/**
	 * 
	 * @param NextSituationMap
	 * @return for a last situation S his next state S', Nbr S-->S' and Nbr S-->S" 
	 * where S" is all the other possible next states from S which are ≠ from S' 
	 */
	
	public static HashMap <List, List> getPossibleSituationsFromSMap(HashMap<Integer,List> NextSituationMap){
		
		int NSS1; // Passage de l'état S à l'état S1		
		int NSS2;// Passage de l'état S avec différents états S2 qui sont différents de S1
		int NSS;	// Passage de l'état S à d'autres état S
		
		HashMap <List, List> Map = new HashMap <List, List> ();		
		
		Iterator it1 = NextSituationMap.entrySet().iterator();
		boolean exist;
		
		//System.out.println("--------------------------------------------");
	    //System.out.println("----getPossibleSituationsFromSMap-----------");
		//System.out.println("--------------------------------------------");

		while (it1.hasNext()) {
			NSS1 = 0;
			NSS2 = 0;
			
			exist = false;
			
			Map.Entry pairs1 = (Map.Entry)it1.next();
	        List value = (List) pairs1.getValue();
			
	        int Last1 = Integer.parseInt(value.get(0).toString());
	        int Next1 = Integer.parseInt(value.get(1).toString());

	        if (! Map.isEmpty()){       //Vérifier si Last1 et Next1 sont déjà traités ou pas	
	        	Iterator it = Map.entrySet().iterator();
	    		while (it.hasNext()) {
	    			Map.Entry pairs = (Map.Entry)it.next();
	    	        List Key = (List) pairs.getKey();
	    	        int Last = Integer.parseInt(Key.get(0).toString());
	    	        int Next = Integer.parseInt(Key.get(1).toString());
	    	       
	    	        if (Last1 == Last && Next1 == Next){
	    	        	exist = true;
	    	        	break;
	    	        }
	    	        else 
	    	        	exist = false;
	    		}	
	        }
	    		
	    	if ( Map.isEmpty() || !exist){
	    		int i= 0;
	    		
	    		Iterator it2 = NextSituationMap.entrySet().iterator();
		        while (it2.hasNext()) {
		        	Map.Entry pairs2 = (Map.Entry)it2.next();
			        List value2 = (List) pairs2.getValue();
					
			        int Last2 = Integer.parseInt(value2.get(0).toString());
			        int Next2 = Integer.parseInt(value2.get(1).toString());
			        
			        if ((Last1 == Last2) && (Next1 == Next2)){
			        	NSS1 = NSS1 + 1;  	
			        }
			        else if ((Last1 == Last2) && (Next1 != Next2)){
			        	NSS2 = NSS2 + 1;  	
			        }
			        else{
			        	//Debug("Nothing");
			        }
			             
		        }
		        NSS = NSS1 + NSS2;
		    	
		    	 List list1 = new ArrayList(2);
			     list1.add(Last1);
			     list1.add(Next1);

			     List list2 = new ArrayList(2);
			     list2.add(NSS1);
			     list2.add(NSS);
			        
			     Map.put(list1, list2);
	    	}	
	     }
		//Debug("End step2");
		return Map;
	}
	/**
	 * 
	 * @param Map
	 */
	
	public static void updateTransitionMatrix(HashMap <List, List> Map){
		
		 try{
		    	
			    Sqlite maBase = new Sqlite();
				maBase.connect();
				String sql = "SELECT * FROM MarkovChain";
				ResultSet result = maBase.executeQuery(sql);
	            int LastSituation;
	            int NextSituation;
	            int Nss1;
	            int Nss;
	            float prob;
	          
	            List list2 = new ArrayList();
	            
	            while (result.next()){
	            	//Debug("-------*****Result["+result.getString("State1")+", "+ result.getString("State2")+"], ["+result.getString("NS1S2")+", "+result.getString("NS1S")+"]");
	            	List list1 = new ArrayList();
	                list1.add(Integer.parseInt(result.getString("State1")));
	                list1.add(Integer.parseInt(result.getString("State2")));
	                list1.add(Integer.parseInt(result.getString("NS1S2")));
	                list1.add(Integer.parseInt(result.getString("NS1S")));
	                list2.add(list1);
	               
	            }
	            Iterator it = Map.entrySet().iterator();
	            boolean ExistInMap = false;
	            
	            while (it.hasNext()) {		
		    		ExistInMap = false;
		    		Map.Entry pairs = (Map.Entry)it.next();
		    		
		    	    List Key = (List) pairs.getKey();        
		    	    int Last = Integer.parseInt(Key.get(0).toString());
		    	    int Next = Integer.parseInt(Key.get(1).toString());
		    	    List value = (List) pairs.getValue();
	    		    int n1 = Integer.parseInt(value.get(0).toString());
	    		   	int n2 = Integer.parseInt(value.get(1).toString());
	    		   //	Debug("-------ItMap["+Last+", "+ Next+"], ["+n1+", "+n2+"]");
	    		   	Iterator itList = list2.iterator();
	    		   	List l = new ArrayList();
	    		   	List l2 = new ArrayList();
	    		   	int NS1S2;
	    		   	int NS1S;
	    		   	
	    		   	while (itList.hasNext()){
	    		   		NS1S = 0;
	    		   		NS1S2 = 0;
	    		   		List list = (List)itList.next();
	    		   		LastSituation = Integer.parseInt(list.get(0).toString());
		                NextSituation = Integer.parseInt(list.get(1).toString());
		                Nss1 = Integer.parseInt(list.get(2).toString());
		                Nss = Integer.parseInt(list.get(3).toString());
		               // Debug("******ItBaseDeDonnée["+LastSituation+", "+ NextSituation+"], ["+n1+", "+n2+"]");
		               
		                if (LastSituation == Last && NextSituation == Next){
		                	NS1S2 = n1+Nss1;
		                	NS1S = n2+Nss;
		                	prob = (float) NS1S2/NS1S;
		                	
		                	//Debug("********prob: "+ prob);  
		                	// update database ici
		    		   	    try{
		    		   	    	//Debug("******Update");
		    		   	    	sql = "UPDATE MarkovChain SET NS1S2="+NS1S2+" , NS1S="+NS1S+", Prob="+prob+" WHERE State1="+Last+" AND State2="+Next;
		    		   	    	//System.out.println(" -----LastSituation == Last && NextSituation == Next----State1="+Last+" AND State2="+Next +" NS1S2 =  "+ NS1S2 +"-------");
			                	maBase.executeMyUpdate(sql);
			                	ExistInMap = true;
		    		   	    }catch (SQLException e) {
		                        System.err.println("SQL Problem: Update DataBase Failed : " + e.getLocalizedMessage());
		                    }
		                }
		    		   	    
		    		   	else if (LastSituation == Last && NextSituation != Next){	 
		    		   		NS1S = n2+Nss;
	    		   	    	//System.out.println(" -----LastSituation == Last && NextSituation != Next ----State1="+Last+" AND State2="+NextSituation +" NS1S2 =  "+ n2+Nss +"-------");
	    		   	    	try{
			            		
			            		prob = (float) n1/NS1S;
		    		   	    	sql = "UPDATE MarkovChain SET NS1S="+NS1S+", Prob="+prob+" WHERE State1="+LastSituation+" AND State2 = "+NextSituation;
			                	maBase.executeMyUpdate(sql);
			            	}
			            	catch (SQLException e) {
			            		System.err.println("SQL Problem: Insert DataBase Failed : "+ e.getLocalizedMessage());
			            	} 
		                	l.add(LastSituation);
		                	l.add(n2+Nss);
		                	l2.add(l);
		                	
		                	
		                } // end if last and not Next 
		    	 }    //END WHile itListMarkovChain
		                
	    		   	boolean existInList = false;
				    List lUpdated ;
				    int ln2= 0;
				    
				    for (int i=0; i< l2.size() ; i++){
				    	lUpdated = (List) l2.get(i);
				    	if (Last == Integer.parseInt(lUpdated.get(0).toString())){
				    		ln2 = Integer.parseInt(lUpdated.get(1).toString());
				    		existInList = true;
				    		break;
				    	}
				    }
				    
	    		/*   	if (ExistInMap){
	    		   		if (existInList) {
					  	
					    	///////////A VERIFIER //////////////////	
				            	try{
				            		
				            		prob = (float) n1/ln2;
			    		   	    	sql = "UPDATE MarkovChain SET NS1S="+ln2+", Prob="+prob+" WHERE State1="+Last+" AND State2 = "+Next;
				                	maBase.executeMyUpdate(sql);
				            	}
				            	catch (SQLException e) {
				            		System.err.println("SQL Problem: Insert DataBase Failed : "+ e.getLocalizedMessage());
				            	}  
	    		   		}
	    		   	} 
	    		   	
	    		   	else { // ! ExistInMap*/
				    if (! ExistInMap){
		         
	    		   		if (!existInList){ // Nouvel enregistrement
	    		   			prob = (float) n1/n2;
	    		   			try{
		            			sql = "INSERT INTO MarkovChain (State1, State2, NS1S2, NS1S, Prob) VALUES ("+ Last +", "+ Next +", "+ n1 + ", "+ n2 + ", "+ prob+")" ;
		            			maBase.executeMyUpdate(sql);
		            			//stmt.executeUpdate(sql);
	    		   			}
	    		   			catch (SQLException e) {
	    		   				System.err.println("SQL Problem: Insert DataBase Failed : "+ e.getLocalizedMessage());
	    		   			}
	    		   		}
	    		   	    else{
			    	
	    		   			///////////A VERIFIER //////////////////	

	    		   			try{    		   				
	    		   				prob = (float) n1/ln2;
	    		   				sql = "INSERT INTO MarkovChain (State1, State2, NS1S2, NS1S, Prob) VALUES ("+ Last +", "+ Next +", "+ n1 + ", "+ ln2 + ", "+ prob+")" ;
	    		   				maBase.executeMyUpdate(sql);
	    		   			}
		            	catch (SQLException e) {
		            		System.err.println("SQL Problem: Insert DataBase Failed : "+ e.getLocalizedMessage());
		            	}
	    		   	}
	    		   } //End while (possible)
	            }
			
			  }catch(Exception e){
	                System.out.println("Connexion Problem step 3: "+ e.getLocalizedMessage());
	          }	
			  //Debug("End step3");
	}
	 
	 public static void main (String [] args){
		
		 int LastCount = Integer.parseInt(prop.getProperty("LastCount"));
		 int New_0bservations_Count = 0;
		 int CurrentCount= 0;
		 
		 
		 try{
			    Sqlite maBase = new Sqlite();
	    		maBase.connect();
	    		String sql = "SELECT count(*) FROM Trace";
	    		maBase.executeQuery(sql);
	    
	    		ResultSet r = maBase.executeQuery(sql);
	            while (r.next())
	            {
	            	CurrentCount = Integer.parseInt(r.getString(1));
	            	//System.out.println("----LastCount : "+ LastCount);
	            	//System.out.println("----CurrentCount : "+ CurrentCount);
	            }  
	            //System.out.println("-----Step0");
	            
	            New_0bservations_Count = CurrentCount - LastCount;
	             
		        int param = Integer.parseInt(prop.getProperty("clustering_param"));
		        maBase.disconnect();
		        if (New_0bservations_Count > param){
		        	
		              
	                long start = System.currentTimeMillis();  	
		        	IClassificationFacade MC= new MarkovChain();
	                MC.doClassification (LastCount, New_0bservations_Count);
                	long end = System.currentTimeMillis();
            		long time = end - start;
            		System.out.println("-----time: "+ time);
            		
		        	//System.out.println("-----Step4");
		        	//prop.setProperty("LastCount", (Integer.toString(CurrentCount-1)));
	                //prop.store(new FileOutputStream("conf/config.properties"), null);
	                
	                //System.out.println(prop.getProperty("LastCount"));
		        }
		        else{
		        	System.out.println("It has no sufficient observations to launch the clustering process");
		        }
	    	}
	    	
		 catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		    }
	    	
	    }
	 
	 public static void Debug(String msg){
		 System.out.println(msg);
	 }
	  
}
