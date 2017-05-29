package clusterManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import Input.Cluster;
import Input.Observation;

import tools.ClusteringResultSet;

/**
 * 
 * The main access point to the search API. This interface provides the methods to add, remove and
 * search services.
 * 
 * @author Salma Najar
 * 
 */
public interface IClusterManager {

	/**
	 * Method for adding an new domain ontology to the knowledge base. The .owl file can be either
	 * local (file and directory) or online.
	 * 
	 * @param ontolgyFile
	 *            The location of the ontology file.
	 * @return A List of added ontologies
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<String> addOntologies(URL ontolgyFile) throws FileNotFoundException, IOException;

	
	/**
	 * Method for finding matching services. The input is the path to a XML file. The return is a
	 * MatchResultSet object which contains all services and their corresponding match score.
	 * 
	 * @param requestDescriptionFile
	 *            Location of the XML request file
	 * @return The found services and their score
	 */
	public void getClusters (List <Observation> observations, List <Cluster> clusters, int LastClusterID);
	
//	public void getClusters (Observation observations[], Cluster clusters[], int LastClusterID);
	public void getClusters (Observation observations[], Cluster clusters[]);
	public void getClusters (Observation observation, Cluster clusters[]);
	

	/**
	 * @param ont
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<String> removeOntology(URL ont) throws FileNotFoundException, IOException;

}
