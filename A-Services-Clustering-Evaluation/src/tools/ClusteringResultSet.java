package tools;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Algorithm.Sqlite;
import Input.Cluster;
import Input.Observation;


/**
 * The ClusteringResultSet class saves Service URIs + the matching score in a HashMap
 * You can add a new element and get the Iterator to access the elements.
 * 
 * @author Salma Najar
 * 
 */
public class ClusteringResultSet {

	
	/**
	 * The Map, containing the URI and score of a service
	 */
	private Map<URI, Double> tm = new HashMap<URI, Double>();

	/**
	 * Default constructor
	 */
	public ClusteringResultSet() {

	}

	/**
	 * Add a new service + matching score
	 * 
	 * @param uri URI of the service
	 * @param value Score (double) of the service for a search request
	 */
	public void addElement(URI uri, Double value) {
		tm.put(uri, value);
	}

	
	/**
	 * Returns the Iterator of the MatchResultSet. The Service and score
	 * can then be accessed via iterator.next()
	 * 
	 * @return Iterator<?> The iterator of the Map
	 */
	public Iterator<?> getIterator() {

		ArrayList<?> as = new ArrayList<Object>(tm.entrySet());
		Collections.sort(as, new Comparator<Object>() {

			@Override
			public int compare(Object o1, Object o2) {
				Map.Entry e1 = (Map.Entry) o1;
				Map.Entry e2 = (Map.Entry) o2;
				Double dd1 = (Double) (e1.getValue());
				Double dd2 = (Double) (e2.getValue());

				if (dd1 > dd2) {
					return -1;
				} else if (dd1 < dd2) {
					return 1;
				} else {
					return e1.toString().compareTo(o2.toString());
				}
			}
		});

		Iterator<?> i = as.iterator();
		return i;
	}
	
	public void setNewClustersSet(Cluster NewClustersSet []){
		
		Observation observ []= new Observation [100]; 
		String sql = null;
		Sqlite maBase = new Sqlite();
    	maBase.connect();

		
		try {
			//conn = DriverManager.getConnection("jdbc:sqlite:mydatabase.Prediction.db");
			//stmt = conn.createStatement();
			int nbo = 0;
			for(Cluster element : NewClustersSet){
			    if (element != null)
			    	nbo++;
			}								
			
			
			for (int i=0; i< NewClustersSet.length; i++){
				//System.out.println("NewClusterSet : "+ i);
				if (NewClustersSet[i] == null)
					break;
				else{
					//System.out.println(NewClustersSet[i].getName() +"', '"+ NewClustersSet[i].getCentroidContext() +"', '"+ NewClustersSet[i].getCentroidIntention() + "', '"+ NewClustersSet[i].getCentroidService()) ;
					sql = "INSERT INTO Clusters (Name, CentroidC, CentroidI, CentroidS) VALUES ('"+ NewClustersSet[i].getName() +"', '"+ NewClustersSet[i].getCentroidContext() +"', '"+ NewClustersSet[i].getCentroidIntention() + "', '"+ NewClustersSet[i].getCentroidService() + "')" ;        
					maBase.executeMyUpdate(sql);
					
					//stmt.executeUpdate(sql);
					
					observ = NewClustersSet[i].getObservations();
					if (observ != null){
					for (int j=0; j< observ.length; j++){
						if (observ[j] != null){
							 sql = "UPDATE Trace SET ClusterID="+ NewClustersSet[i].getID() +" WHERE ID = "+observ[j].getID();
							 maBase.executeMyUpdate(sql);
							 //stmt.executeUpdate(sql);
						}
						 
					}
					}
				}
				
				
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setUpdatedClustersSet(Cluster UpdatedClustersSet []){
		
		Observation observ []= new Observation [100]; 
		String sql = null;
		Sqlite maBase = new Sqlite();
    	maBase.connect();
		try {
			//conn = DriverManager.getConnection("jdbc:sqlite:mydatabase.Prediction.db");
			//stmt = conn.createStatement();
			
			for (int i=0; i< UpdatedClustersSet.length; i++){
				if (UpdatedClustersSet[i]== null)
					break;
				else{
					observ = UpdatedClustersSet[i].getObservations();
					for (int j=0; j< observ.length; j++){
						 if (observ[j] == null)
						 	break;
						 else{
							 sql = "UPDATE Trace SET ClusterID="+ UpdatedClustersSet[i].getID() +" WHERE ID="+observ[j].getID();
							 maBase.executeMyUpdate(sql);
							 //stmt.executeUpdate(sql);
						 }
						 
					}
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
