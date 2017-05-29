package run;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;

import PredictionManager.IPredictionManager;
import PredictionManager.PredictionManagerImpl;



import tools.PropertiesUtil;

public class LaunchPrediction {
	public static String configUrl = "Config.properties";
	public static String configFile = "conf/Config.properties";

	public static PropertiesUtil propertiesUtil = new PropertiesUtil(configFile, configUrl);
	public static Properties prop = propertiesUtil.getProperties();

	/**
	 * @param args
	 */

	public static void main(String[] args) {
    	
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
		
		// TODO Auto-generated method stub
		String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><request><intention><verb>reserve</verb><target>hotel</target></intention></request>";
		
		long start = System.currentTimeMillis();
    	IPredictionManager pm = new PredictionManagerImpl(configFile, configUrl);		
	
    	pm.doPrediction(request); 
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
