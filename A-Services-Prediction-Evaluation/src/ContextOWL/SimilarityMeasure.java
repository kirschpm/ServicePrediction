package ContextOWL;

import java.sql.ResultSet;
import java.sql.Statement;

public class SimilarityMeasure {
	
	public static Statement stmt = null;
    public static ResultSet rs = null;
    public static String pilote = "com.mysql.jdbc.Driver"; 
    
    public static String getSimilarityMeasure (String Representation){
		String methode = null;
	    try{
	    	
    		Class.forName(pilote);
            java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:8889/ContextValueDB","root","root");
            stmt = conn.createStatement();
            
            //Recover the Current count of observations
            ResultSet r = stmt.executeQuery("SELECT Measure FROM  SimilarityMeasure WHERE Representation="+Representation);
            while (r.next())
            {
            	methode = r.getString(1);
            }  
    	}
    	
        catch(Exception e)
        {
            System.out.println("Connexion Problem: "+e);
        }
        return methode;
	}

    

}
