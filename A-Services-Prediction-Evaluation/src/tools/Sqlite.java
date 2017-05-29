package tools;

import java.sql.*;
import java.util.Properties;

public class Sqlite {
	 private Connection con;
	 private Statement st;
	 private String databaseFile;

	 /** Constructeur de SQLite */
	 public Sqlite() {
	 }

	 public void connect(){
		 // Connexion à la base de donnnées.
		String configUrl = "Config.properties";
	    String configFile = "conf/Config.properties";
		        
		PropertiesUtil propertiesUtil = new PropertiesUtil(configFile, configUrl);
		Properties prop = propertiesUtil.getProperties();
			
		try {
			Class.forName("org.sqlite.JDBC");
			databaseFile = prop.getProperty("DataBase");
	        con = DriverManager.getConnection("jdbc:sqlite:" + databaseFile);
		}catch(Exception e){
			System.out.println("Problème connexion : "+e);
		 }
	 }

	 public void disconnect(){
		 try {
			 st.close();
			 con.close();
		 } catch(Exception e){
			 System.out.println("Problème déconnexion : "+e);
		 }
	 }

	 public ResultSet executeQuery(String query) throws SQLException{
		 st = con.createStatement();
		 return st.executeQuery(query);
	 }

	 public void executeMyUpdate(String query) throws SQLException{
		 st = con.createStatement();
		 st.executeUpdate(query);       

	 }
	 
	 public static void main (String args []) throws SQLException{
		 Sqlite sqlite = new Sqlite();
		 sqlite.connect();
		 String query = "SELECT count(*) FROM Trace";
		 ResultSet r = sqlite.executeQuery(query);
		 while (r.next())
         {
         	System.out.println(Integer.parseInt(r.getString(1)));
         }  
	 }
}
