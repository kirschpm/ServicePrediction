package clusterEngine;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Input.Cluster;
import Input.Observation;
import persistence.IPersistenceManager;
import persistence.ModelListener;
import Algorithm.AbstractClustering;
import tools.ClusteringResultSet;
import tools.PropertiesUtil;

/**
 * Class which implements the ISearchEngine interface.
 * 
 * @author Salma Najar
 * 
 */
public class ClusterEngineImpl implements IClusterEngine, ModelListener {

	private static final class InstanceHolder {
		private static final ClusterEngineImpl INSTANCE = new ClusterEngineImpl();
	}

	/**
	 * Get the class
	 * 
	 * @return Singleton class
	 */
	public static ClusterEngineImpl getInstance() {

		return InstanceHolder.INSTANCE;

	}

	/**
	 * The matcher class, used to make the queries
	 */
	private AbstractClustering strategy;

	/**
	 * Persistence Manager, used to get the ontologies and services
	 */
	private IPersistenceManager pManager;

	/**
	 * private constructor
	 */
	private ClusterEngineImpl() {
	}

	/*
	 * @see searchEngine.ISearchEngine#discoverServices(tools.SearchRequest)
	 */
	@Override
	public void getClusters (List <Observation> observations, List <Cluster> clusters, int LastClusterID) {
		strategy.getClusters(observations, clusters, LastClusterID );
		
	}
	
	@Override
	/*public void getClusters (Observation observations[], Cluster clusters[], int LastClusterID) {
		strategy.getClusters(observations, clusters, LastClusterID );
		
	}*/
	
	public void getClusters (Observation observations[], Cluster clusters[]) {
		strategy.getClusters(observations, clusters);
		
	}
	
	public void getClusters (Observation observation, Cluster clusters[]) {
		strategy.getClusters(observation, clusters);
		
	}

	/*
	 * @see searchEngine.ISearchEngine#loadDefaultCluster()
	 */
	@Override

	public void loadDefaultCluster(String file, String URL) {
		
		try {
			//System.out.println("-----loadDefaultMatcher");
			PropertiesUtil propertiesUtil = new PropertiesUtil(file, URL);
			String name = propertiesUtil.getProperties().getProperty("clustering");
			Class theClass = Class.forName(name);
			// get the constructor with one parameter
			// create an instance
			this.strategy = (AbstractClustering) theClass.newInstance();
			this.strategy.setPersistenceManager(pManager);
			
			pManager.addModelListener(strategy);

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	@Override
	public void ontologyiesChanged() {
		strategy.ontologyiesChanged();
		
	}

	/**
	 * @see IClusterEngine.ISearchEngine#setPersistenceManager(persistence.IPersistenceManager)
	 */
	@Override
	public void setPersistenceManager(IPersistenceManager pManager) {
		//System.out.println("-----setPersistenceManager");
		this.pManager = pManager;
	}

}
