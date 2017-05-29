package PredictionEngine;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import persistence.IPersistenceManager;
import persistence.ModelListener;
import Matchmaker.AbstractPrediction;
import tools.PredictionRequest;
import tools.PredictionResultSet;
import tools.PropertiesUtil;

/**
 * Class which implements the ISearchEngine interface.
 * 
 * @author Salma Najar
 * 
 */
public class PredictionEngineImpl implements IPredictionEngine, ModelListener {

	private static final class InstanceHolder {
		private static final PredictionEngineImpl INSTANCE = new PredictionEngineImpl();
	}

	/**
	 * Get the class
	 * 
	 * @return Singleton class
	 */
	public static PredictionEngineImpl getInstance() {

		return InstanceHolder.INSTANCE;

	}

	/**
	 * The matcher class, used to make the queries
	 */
	private AbstractPrediction strategy;

	/**
	 * Persistence Manager, used to get the ontologies and services
	 */
	private IPersistenceManager pManager;

	/**
	 * private constructor
	 */
	private PredictionEngineImpl() {
	}

	/*
	 * @see searchEngine.ISearchEngine#discoverServices(tools.SearchRequest)
	 */
	@Override
	public String doPrediction (PredictionRequest requestDescription) {
		return strategy.doPrediction(requestDescription);
		
	}

	/*
	 * @see searchEngine.ISearchEngine#loadDefaultCluster()
	 */
	@Override

	public void loadDefaultCluster(String file, String URL) {
		
		try {
			//System.out.println("-----loadDefaultMatcher");
			PropertiesUtil propertiesUtil = new PropertiesUtil(file, URL);
			String name = propertiesUtil.getProperties().getProperty("prediction");
			Class theClass = Class.forName(name);
			// get the constructor with one parameter
			// create an instance
			this.strategy = (AbstractPrediction) theClass.newInstance();
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
	 * @see IPredictionEngine.ISearchEngine#setPersistenceManager(persistence.IPersistenceManager)
	 */
	@Override
	public void setPersistenceManager(IPersistenceManager pManager) {
		//System.out.println("-----setPersistenceManager");
		this.pManager = pManager;
	}

}
