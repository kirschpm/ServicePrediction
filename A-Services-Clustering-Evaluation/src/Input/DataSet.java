package Input;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import Algorithm.Sqlite;

public class DataSet {
	
    public static ResultSet r;
    public static String sql;
    
 //   public static List<Observation> getObservationsFromDB(int LastCount, int nb_new_observations) {
      public static Observation[] getObservationsFromDB(int LastCount, int nb_new_observations) {
    	
    	
    //	List <Observation> observations = new ArrayList<Observation>() ;
    	//Observation observ = new Observation();
    	int id = 0;
    	int user = 0;
    	String context = null;
    	String intention = null;
    	String service = null;
        ResultSet r;
        String sql;
        Observation observations[] = null;

    	
    	try
    	{
    		
    		Sqlite maBase = new Sqlite();
    		maBase.connect();
    		
    		String sql1 = "SELECT ID, User, Context, Intention, Service FROM Trace ORDER BY ID Limit "+ (LastCount)+","+ (nb_new_observations); 
    		ResultSet r1 = maBase.executeQuery(sql1);
    		int taille = 0;
    		while (r1.next()){
    			taille++;
    		}
            observations = new Observation[taille];
    		sql = "SELECT ID, User, Context, Intention, Service FROM Trace ORDER BY ID Limit "+ (LastCount)+","+ (nb_new_observations); 
    		r = maBase.executeQuery(sql);
    		int i= 0;
    		
    		
    		//observations = new Observation[r.getRow()];
    	    while (r.next())
    	    {
    	    	
    	    	//Observation observ = new Observation();
    	    	observations[i] = new Observation();
    	    	id = Integer.parseInt(r.getString(1));
    	    	user = Integer.parseInt(r.getString(2));
    	   		context = r.getString(3);
    	   		intention = r.getString(4);
    	   		service = r.getString(5);
    	   		observations[i].setID(id);
    	   		observations[i].setUser(user);
    	   		observations[i].setContext(context);
    	   		observations[i].setIntention(intention);
    	   		observations[i].setService(service);
        		/*observ.setID(id);
   	    		observ.setUser(user);
   	    		observ.setContext(context);
   	    		observ.setIntention(intention);
   	    		observ.setService(service);
   	    		
    	    	observations.add(observ);*/
    	    	i++;
    	   	}

    		maBase.disconnect();
    		
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return observations;
    	
    
    }
      
      public static Observation[] getObservationsFromDB() {

    	    	int id = 0;
    	    	int user = 0;
    	    	String context = null;
    	    	String intention = null;
    	    	String service = null;
    	        ResultSet r;
    	        String sql;
    	        Observation observations[] = null;

    	    	
    	    	try
    	    	{
    	    		
    	    		Sqlite maBase = new Sqlite();
    	    		maBase.connect();
    	    		
    	    		String sql1 = "SELECT ID, User, Context, Intention, Service FROM Trace "; 
    	    		ResultSet r1 = maBase.executeQuery(sql1);
    	    		int taille = 0;
    	    		while (r1.next()){
    	    			taille++;
    	    		}
    	            observations = new Observation[taille];
    	    		sql = "SELECT ID, User, Context, Intention, Service FROM Trace"; 
    	    		r = maBase.executeQuery(sql);
    	    		int i= 0;

    	    	    while (r.next())
    	    	    {
    	    	    	observations[i] = new Observation();
    	    	    	id = Integer.parseInt(r.getString(1));
    	    	    	user = Integer.parseInt(r.getString(2));
    	    	   		context = r.getString(3);
    	    	   		intention = r.getString(4);
    	    	   		service = r.getString(5);
    	    	   		observations[i].setID(id);
    	    	   		observations[i].setUser(user);
    	    	   		observations[i].setContext(context);
    	    	   		observations[i].setIntention(intention);
    	    	   		observations[i].setService(service);
    	        		
    	    	    	i++;
    	    	   	}

    	    		maBase.disconnect();
    	    		
    	    	} catch (SQLException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		
    			return observations;
    	    	
    	    
    	    }
 
    //public static List <Cluster> getClustersFromDB(){
      public static Cluster[] getClustersFromDB() {
        Observation observations[] = null;
      	Cluster clusters[] = null;
    	//List<Cluster> clusters = new ArrayList<Cluster>() ;
    	Cluster cluster = new Cluster();
		int id = 0;
		String name = null;
		String centroid_C = null;
		String centroid_I = null;
		String centroid_S = null;
		float centroid_avg = 0;
        ResultSet r;
        String sql;
    	
		try {
    		int j = 0;

    		Sqlite maBase = new Sqlite();
    		maBase.connect();
 
    		String sql1 = "SELECT * FROM Clusters"; 
    		ResultSet r1 = maBase.executeQuery(sql1);
    		int tailleC = 0;
    		
    		while (r1.next()){
    			tailleC++;
    		}
    		
    		clusters = new Cluster[tailleC];
    		
    		sql = "SELECT * FROM Clusters"; 
    		r = maBase.executeQuery(sql);

			while (r.next())
			{
			    int i = 0;
				
				id = Integer.parseInt(r.getString(1));
				//System.out.println ("****I = "+ i);
				//System.out.println ("****id Clusters : "+id);	
	    		name = r.getString(2);
	    		centroid_C = r.getString(3);
	    		centroid_I = r.getString(4);
	    		centroid_S = r.getString(5);
	    		//centroid_avg = new Float(r.getString(6));
	    		
	    		String sql2 = "SELECT * FROM Trace Where ClusterID = "+ id; 
	    		ResultSet r2 = maBase.executeQuery(sql2);
	    		int tailleO = 0;
	    		while (r2.next()){
	    			tailleO ++;
	    		}
	    		
	     		observations = new Observation[tailleO];
	     		
	    		String sql3 = "SELECT * FROM Trace Where ClusterID = "+ id; 
	    		ResultSet r3 = maBase.executeQuery(sql3);

	    		while (r3.next()){
	    			
	    			observations[i] = new Observation();
	    	   		observations[i].setID(Integer.parseInt(r3.getString(1)));
	    	   		observations[i].setUser(Integer.parseInt(r3.getString(2)));
	    	   		observations[i].setContext(r3.getString(3));
	    	   		observations[i].setIntention(r3.getString(4));
	    	   		observations[i].setService(r3.getString(5));

	    	   		i++;
	    		}
	    		
	    		clusters[j] = new Cluster();
	    		clusters[j].setID(id);
	    		clusters[j].setObservations(observations);
	    		clusters[j].setName(name);
	    		clusters[j].setCentroidContext(centroid_C);
	    		clusters[j].setCentroidIntention(centroid_I);
	    		clusters[j].setCentroidService(centroid_S);
	    		clusters[j].setCentroidAverage(centroid_avg);
	    		j++;
	    	}
			//System.out.println ("****End Clusters****");
			maBase.disconnect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error DataSet :" + e.getMessage());
		}
    	return clusters;
    	
    }

}
