package run;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
* @author salma najar
*/
public class configurationPrediction {

   static String DEFAULT_PREDICTION_KEY = "prediction";
   static String DEFAULT_ONTOLOGIES_KEY = "ontologies";
   static String DEFAULT_PERSISTENCE_KEY = "persistence";
   static String DEFAULT_REQUEST_KEY = "threshold";
   static String DEFAULT_DATABASE_KEY = "DataBase";


   static String DEFAULT_PREDICTION_VALUE = "";
   static String DEFAULT_ONTOLOGIES_VALUE = "";
   static String DEFAULT_PERSISTENCE_VALUE = "";
   static String DEFAULT_REQUEST_VALUE = "";
   static String DEFAULT_DATABASE_VALUE = "";

   
   

   static Properties loadDefaultProperties() {
       Properties defaultsettings = new Properties();
       defaultsettings.put(DEFAULT_PREDICTION_KEY, DEFAULT_PREDICTION_VALUE);
       defaultsettings.put(DEFAULT_ONTOLOGIES_KEY, DEFAULT_ONTOLOGIES_VALUE);
       defaultsettings.put(DEFAULT_PERSISTENCE_KEY, DEFAULT_PERSISTENCE_VALUE);
       defaultsettings.put(DEFAULT_REQUEST_KEY, DEFAULT_REQUEST_VALUE);
       defaultsettings.put(DEFAULT_DATABASE_KEY, DEFAULT_DATABASE_VALUE);
       

       
       return (defaultsettings);
   }


   static Properties loadProperties (String path, Properties defaultsettings) {
       Properties settings = new Properties(defaultsettings);
       File propfile = new File(path);
       if (propfile.exists()) {
           try {
               FileInputStream in = new FileInputStream(propfile);
               settings.load(in);
           } catch (FileNotFoundException ex) {
               Logger.getLogger(configurationPrediction.class.getName()).log(Level.SEVERE,
                       "Impossible d'ouvrir fichier de propriete", ex);
           } catch (IOException ioex) {
               Logger.getLogger(configurationPrediction.class.getName()).log(Level.SEVERE,
                       "Impossible de lire le fichier de propriete", ioex);
           }
       }
       return settings;
   }

   static void storeProperties (String path, String comment, Properties settings) {
       File propfile = new File(path);

       try {
           FileOutputStream out = new FileOutputStream(propfile);
           settings.store(out, comment);
       } catch (FileNotFoundException ex) {
           Logger.getLogger(configurationPrediction.class.getName()).log(Level.SEVERE,
                   "Impossible d'ouvrir fichier de propriete", ex);
       } catch (IOException ioex) {
           Logger.getLogger(configurationPrediction.class.getName()).log(Level.SEVERE,
                   "Impossible d'enregistrer fichier de propriete", ioex);
       }
   }


   static String findSettingsFilePath () {
       String path;
       path = "/conf/config.properties";
       System.out.println(path);
       return (path);
   }

   static void printSettings (Properties settings) {
       Enumeration keys = settings.propertyNames();
       while (keys.hasMoreElements()) {
           String aKey = (String) keys.nextElement();
           String value = settings.getProperty(aKey);
           System.out.println (aKey + "=" + value);
       }
   }


   static void setProperties (Properties settings,String clustering) {
       settings.setProperty(DEFAULT_PREDICTION_KEY, clustering);
   }


   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {
       Properties defaultsettings = loadDefaultProperties();
       String path = findSettingsFilePath();
       Properties settings = loadProperties(path, defaultsettings);

       System.out.println("Loaded properties");
       printSettings(settings);
       setProperties(settings, "Test");
       
       printSettings (settings);

       System.out.println ("Saving properties into "+path);
       storeProperties(path, "SimplePropertiesDemo", settings);
   }

}
