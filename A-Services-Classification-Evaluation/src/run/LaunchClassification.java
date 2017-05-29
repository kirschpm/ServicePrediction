package run;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import classificationManager.ClassificationManagerImpl;
import classificationManager.IClassificationManager;

import tools.PropertiesUtil;

import Algorithm.IClassificationFacade;
import Algorithm.MarkovChain;
import Algorithm.Sqlite;

public class LaunchClassification {

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
	            	
	            }  
	           
	            
	            New_0bservations_Count = CurrentCount - LastCount;
	             
		        int param = Integer.parseInt(prop.getProperty("clustering_param"));
		        maBase.disconnect();
		        if (New_0bservations_Count > param){
		        	
		              
	                long start = System.currentTimeMillis();  	
	                IClassificationManager cm = new ClassificationManagerImpl(configFile, configUrl);	
	                cm.doClassification (LastCount, New_0bservations_Count);
                	long end = System.currentTimeMillis();
            		long time = end - start;
            		System.out.println("-----time: "+ time);
            		
            		 //write output
                    
            		FileWriter bufferedWriter = null;     
                    try {
                        //Construct the BufferedWriter object
                        bufferedWriter = new FileWriter(output, true);         
                        //Start writing to the output stream
                        System.out.println("Start Writing");
                        
                        bufferedWriter.write( time+ "\n");

                        bufferedWriter.flush();
                        bufferedWriter.close();
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

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
	 

}
