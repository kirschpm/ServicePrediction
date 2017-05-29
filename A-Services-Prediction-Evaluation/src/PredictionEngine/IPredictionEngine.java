package PredictionEngine;

import java.net.URI;
import java.util.List;

import persistence.IPersistenceManager;
import tools.PredictionResultSet;
import tools.PredictionRequest;

/**
 * This Interface provides the clustering methods.
 * 
 * @author Salma Najar
 */
public interface IPredictionEngine {

	/**
	 * Method for finding matching services. The input is the path to a XML file. The return is a
	 * MatchResultSet object which contains all services and their corresponding match score.
	 * 
	 * @param requestDescriptionFile
	 *            Location of the XML request file
	 * @return The found services and their score
	 */
	public String doPrediction (PredictionRequest requestDescription);
	

	/**
	 * Loads the default matcher.
	 */
	public void loadDefaultCluster(String file, String URL);

	/**
	 * Method to set the persistence manager.
	 * 
	 * @param pManager
	 *            Persistence Manager to be loaded
	 */
	public void setPersistenceManager(IPersistenceManager pManager);

}
