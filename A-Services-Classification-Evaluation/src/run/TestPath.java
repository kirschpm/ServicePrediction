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


public class TestPath {


		 public static void main (String [] args){
			
	    	String outputs = args[0];

			
			
			//Output File
			File outputFile = new File(outputs);
			String output = outputFile.getAbsolutePath();
			
			    		
	            		 //write output
	                    
	            		FileWriter bufferedWriter = null;     
	                    try {
	                        //Construct the BufferedWriter object
	                        bufferedWriter = new FileWriter(output, true);         
	                        //Start writing to the output stream
	                        System.out.println("Start Writing");
	                        
	                        bufferedWriter.write("Response Time: "+ "\n");
	                        bufferedWriter.flush();
	                        bufferedWriter.close();
	                    } catch (FileNotFoundException ex) {
	                        ex.printStackTrace();
	                    } catch (IOException ex) {
	                        ex.printStackTrace();
	                    }

			       		    	
		    }
}
