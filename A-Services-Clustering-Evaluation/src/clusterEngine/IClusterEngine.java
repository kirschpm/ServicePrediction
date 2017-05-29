package clusterEngine;

import java.net.URI;
import java.util.List;

import Input.Cluster;
import Input.Observation;

import persistence.IPersistenceManager;
import tools.ClusteringResultSet;

/**
 * This Interface provides the clustering methods.
 * 
 * @author Salma Najar
 */
public interface IClusterEngine {

	/**
	 * Method for finding matching services. The input is the path to a XML file. The return is a
	 * MatchResultSet object which contains all services and their corresponding match score.
	 * 
	 * @param requestDescriptionFile
	 *            Location of the XML request file
	 * @return The found services and their score
	 */
	public void getClusters (List <Observation> observations, List <Cluster> clusters, int LastClusterID);
	
	//public void getClusters (Observation observations[], Cluster clusters[], int LastClusterID);
	public void getClusters (Observation observations[], Cluster clusters[]);
	
	public void getClusters (Observation observation, Cluster clusters[]);
	

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
