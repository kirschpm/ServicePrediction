package ContextOWL;

import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import Algorithm.SemanticClusteringAlgorithm;
import Input.Cluster;
import Input.DataSet;
import Input.Observation;

public class RepresentationTransformation {
	
	public static Statement stmt = null;
    public static ResultSet rs = null;
    public static String pilote = "com.mysql.jdbc.Driver"; 
    
	
	//valueRepresentationService.getRepresentationAsString().equals(valueRepresentationUser.getRepresentationAsString()
	// Retorune le nom de la méthode de transformation
    
	public  String getTransformationMethod (String CRepresentation, String ORepresentation){
		
		String methode = null;
	    try{
	    	
    		Class.forName(pilote);
            java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:8889/ContextValueDB","root","root");
            stmt = conn.createStatement();
            
            //Recover the Current count of observations
            ResultSet r = stmt.executeQuery("SELECT MethodeTransformation FROM Representation WHERE Representation1="+CRepresentation+" AND Representation2="+ ORepresentation);
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
