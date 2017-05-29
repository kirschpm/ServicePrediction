package Matchmaker;

import java.net.URI;
import java.util.List;


import persistence.IPersistenceManager;
import tools.PredictionResultSet;
import tools.PredictionRequest;


/**
 * Cluster interface. All service search classes need to implement this interface. The underlying
 * searching mechanism can thus be hidden from the application.
 * 
 * @author Salma Najar
 * 
 */
public interface IPredictionFacade {

	/**
	 * Method for finding matching services. The input is the path to a XML file. The return is a
	 * MatchResultSet object which contains all services and their corresponding match score.
	 * 
	 * @param requestDescription
	 *            the searchRequest object
	 * @return The found services and their score
	 */
	String doPrediction (PredictionRequest requestDescription);
	
	/**
	 * Method to set the PersistenceManager which is used by the class to access the models.
	 * 
	 * @param pManager The persistence Manager 
	 */
	public void setPersistenceManager(IPersistenceManager pManager);
	
}
