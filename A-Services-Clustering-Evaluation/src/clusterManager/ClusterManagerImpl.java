package clusterManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import Input.Cluster;
import Input.Observation;

import persistence.IPersistenceManager;
import persistence.PersistenceManagerImpl;
import clusterEngine.IClusterEngine;
import clusterEngine.ClusterEngineImpl;
import tools.ClusteringResultSet;
import tools.ClusterRequest;
import tools.XMLParser;

/**
 * 
 * Implements the IServiceManager interface.
 * 
 * This singleton class uses the ISearchEngine and IPresistenceManager singleton's and a XMLParser
 * for implementing the IServiceManager interface.
 * 
 * 
 * @author Salma Najar
 * 
 */
public class ClusterManagerImpl implements IClusterManager {

	/**
	 * single instance of ISearchEngine
	 */

	
	private IClusterEngine clusterEngine = ClusterEngineImpl.getInstance();
	/**
	 * single instance of IPersistenceManager
	 */
	
	private IPersistenceManager persistenceManager = null;
	private XMLParser xmlParser = new XMLParser();

	/**
	 * 
	 * @author Salma Najar
	 * 
	 */

	/**
	 * private constructor
	 */
	public ClusterManagerImpl(String File, String URL) {	
		//System.out.println("-----ServiceManagerImpl-persistenceManager");
		System.out.println("Debug");
		persistenceManager = PersistenceManagerImpl.getInstance(File, URL);
		System.out.println("Debug");
		//System.out.println("-----ServiceManagerImpl-setPersistenceManager");
		clusterEngine.setPersistenceManager(persistenceManager);
		System.out.println("Debug");
		//System.out.println("-----ServiceManagerImpl-loadDefaultMatcher");
		clusterEngine.loadDefaultCluster(File, URL);
	}

	/**
	 * @see IClusterManager.IServiceManager#addOntologies(java.net.URL)
	 */
	@Override
	public List<String> addOntologies(URL ontologyFile) throws FileNotFoundException, IOException {
		return persistenceManager.addOntologies(ontologyFile);
	}
	
	@Override
	public List<String> removeOntology(URL ont) throws FileNotFoundException, IOException{
		return persistenceManager.removeOntology(ont);
	}

	@Override
	public void getClusters(List<Observation> observations,
	    List<Cluster> clusters, int LastClusterID) {
		// TODO Auto-generated method stub
		clusterEngine.getClusters(observations, clusters, LastClusterID);
	}
	
/*	public void getClusters(Observation observations [],
		    Cluster clusters[], int LastClusterID) {
			// TODO Auto-generated method stub
			clusterEngine.getClusters(observations, clusters, LastClusterID);
		}*/
	public void getClusters(Observation observations [],
		    Cluster clusters[]) {
			// TODO Auto-generated method stub
			clusterEngine.getClusters(observations, clusters);
		}

	public void getClusters(Observation observation,
		    Cluster clusters[]) {
			// TODO Auto-generated method stub
			clusterEngine.getClusters(observation, clusters);
		}



}
