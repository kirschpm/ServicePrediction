package Algorithm;

import java.net.URI;
import java.util.List;

import Input.Cluster;
import Input.Observation;

import persistence.IPersistenceManager;
import tools.ClusteringResultSet;


/**
 * Cluster interface. All service search classes need to implement this interface. The underlying
 * searching mechanism can thus be hidden from the application.
 * 
 * @author Salma Najar
 * 
 */
public interface IClusterFacade {

	/**
	 * Method for finding matching services. The input is the path to a XML file. The return is a
	 * MatchResultSet object which contains all services and their corresponding match score.
	 * 
	 * @param requestDescription
	 *            the searchRequest object
	 * @return The found services and their score
	 */
	void getClusters (List <Observation> observations, List <Cluster> clusters, int LastClusterID);

//	void getClusters (Observation observations [], Cluster clusters [], int LastClusterID);
	void getClusters (Observation observations [], Cluster clusters []);
	
	void getClusters (Observation observation, Cluster clusters []);

	/**
	 * Method to set the PersistenceManager which is used by the class to access the models.
	 * 
	 * @param pManager The persistence Manager 
	 */
	public void setPersistenceManager(IPersistenceManager pManager);
	
}
