package run;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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


public class LaunchClusteringAll {
	

    public static void main (String [] args){
    	ResultSet rs = null;
    	int CurrentCount= 0;
    	int New_0bservations_Count = 0;
    	//int LastCount = 0;
    	//int LastClusterID = 0;
    	String configUrl = "Config.properties";
        String configFile = "conf/Config.properties";

        PropertiesUtil propertiesUtil = new PropertiesUtil(configFile, configUrl);
    	Properties prop = propertiesUtil.getProperties();

    	try{
    	   
    		long start = System.currentTimeMillis();

    		 Observation observations [] = DataSet.getObservationsFromDB();
                	
            Cluster clusters [] = DataSet.getClustersFromDB();
                  
            IClusterManager cm = new ClusterManagerImpl(configFile, configUrl);	
                  
                	
            cm.getClusters(observations, clusters);
            long end = System.currentTimeMillis();
            long time = end - start;
            System.out.println("Response Time : "+ time);
            System.out.println(prop.getProperty("LastCount"));

    	}
    	
        catch(Exception e)
        {
            System.err.println("Connexion Problem Launch Clustering: "+ e.getMessage());
        }
    
    	
    }
}

