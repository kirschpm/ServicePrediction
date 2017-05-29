package tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import run.configuration;

/**
 * Helper class to get the properties file.
 * 
 * 
 * 
 * @author Salma Najar
 *
 */
public class PropertiesUtil {

	private static Properties properties = new Properties();
	String file;
	String URL;
	
	public PropertiesUtil(String File, String url){
		this.file = File;
		this.URL = url;	
	}

	/**
	 * Method to get the properties. Should work on a server or locally.
	 * 
	 * @return The config.properties file
	 */
	public java.util.Properties getProperties() {

		ClassLoader loader = PropertiesUtil.class.getClassLoader();
		if (loader == null) {
			loader = ClassLoader.getSystemClassLoader();
		}

		java.net.URL url = loader.getResource(this.file);
		if (url == null) {
			url = loader.getResource(this.URL);
		}
		try {
			
			//properties.load(url.openStream());
			properties.load(new FileInputStream(this.file));
			
			
		} catch (Exception e) {
			System.err.println("Could not load configuration file: " + this.file);
		}

		return properties;
	}
	
	
	public static java.util.Properties setProperties(String Key, String value) {
		try{
			properties.setProperty(Key, value);
		} catch (Exception e) {
			System.err.println("Could not set configuration file");
		}

		return properties;
	}
	
	public static void storeProperties (Properties prop, String comment) {

		String propFile = "conf/Config.properties";

	       try {
	           FileOutputStream out = new FileOutputStream(propFile);
	           prop.store(out, comment);
	       } catch (FileNotFoundException ex) {
	           Logger.getLogger(configuration.class.getName()).log(Level.SEVERE,
	                   "Impossible d'ouvrir fichier de propriete", ex);
	       } catch (IOException ioex) {
	           Logger.getLogger(configuration.class.getName()).log(Level.SEVERE,
	                   "Impossible d'enregistrer fichier de propriete", ioex);
	       }
	   }
	
}