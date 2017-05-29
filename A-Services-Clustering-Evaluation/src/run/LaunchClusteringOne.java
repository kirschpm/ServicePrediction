package run;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import clusterManager.ClusterManagerImpl;
import clusterManager.IClusterManager;


import tools.ClusterRequest;
import tools.PropertiesUtil;
import tools.XMLParser;
import Algorithm.IClassificationFacade;
import Algorithm.MarkovChain;
import Algorithm.SemanticClusteringAlgorithm;
import Algorithm.Sqlite;
import Input.Cluster;
import Input.DataSet;
import Input.Observation;


public class LaunchClusteringOne {
	

    public static void main (String [] args){
    	
    	String configUrl = "Config.properties";
        String configFile = "conf/Config.properties";
        
    	PropertiesUtil propertiesUtil = new PropertiesUtil(configFile, configUrl);
     	Properties prop = propertiesUtil.getProperties();
    	
    	String DataBase = args[0];
		String outputs = args[1];
		
		
		//Output File
		File outputFile = new File(outputs);
		String output = outputFile.getAbsolutePath();
		
		prop.setProperty("DataBase", DataBase);
        try {
			prop.store(new FileOutputStream(configFile), null);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
    	ResultSet rs = null;

    	

       
    	long start = System.currentTimeMillis();
    	Observation observation = new Observation ();

    	try{

	    	Sqlite maBase = new Sqlite();
	    	maBase.connect();
	    	String sql = "SELECT ID, User, Context, Intention, Service FROM Trace ORDER BY ID DESC LIMIT 1"; 

	    	ResultSet r = maBase.executeQuery(sql);
	    		
	    	if (r.next())
	    	{

	    	    int id = Integer.parseInt(r.getString(1));
	    	    System.out.println("ID: "+ id);
	    	    int user = Integer.parseInt(r.getString(2));
	    	   	String context = r.getString(3);
	    	   	String intention = r.getString(4);
	    	   	String service = r.getString(5);
	    	   	observation.setID(id);
	    	   	observation.setUser(user);
	    	   	observation.setContext(context);
	    	   	observation.setIntention(intention);
	    	   	observation.setService(service);	        		
	    	 }

	    	maBase.disconnect();
	    		
	    	} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				 System.err.println("Connexion Problem Launch Clustering: "+ e.getMessage());
			}

    		//Observation observations [] = DataSet.getObservationsFromDB();
                	
            Cluster clusters [] = DataSet.getClustersFromDB();
                  
            IClusterManager cm = new ClusterManagerImpl(configFile, configUrl);	
                  
                	
            cm.getClusters(observation, clusters);
            long end = System.currentTimeMillis();
            long time = end - start;
            System.out.println("Response Time : "+ time);
            
          //write output
            
    		FileWriter bufferedWriter = null;     
            try {
                //Construct the BufferedWriter object
                bufferedWriter = new FileWriter(output, true);         
                //Start writing to the output stream
                System.out.println("Start Writing");
                
                bufferedWriter.write(time+ "\n");

                bufferedWriter.flush();
                bufferedWriter.close();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
    		
            

    	}
}

