package classificationEngine;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Algorithm.AbstractClassification;

import tools.PropertiesUtil;

/**
 * Class which implements the ISearchEngine interface.
 * 
 * @author Salma Najar
 * 
 */
public class ClassificationEngineImpl implements IClassificationEngine {

	private static final class InstanceHolder {
		private static final ClassificationEngineImpl INSTANCE = new ClassificationEngineImpl();
	}

	/**
	 * Get the class
	 * 
	 * @return Singleton class
	 */
	public static ClassificationEngineImpl getInstance() {

		return InstanceHolder.INSTANCE;

	}

	/**
	 * The clssifier class, used to make the queries
	 */
	private AbstractClassification strategy;


	/**
	 * private constructor
	 */
	private ClassificationEngineImpl() {
	}

	
	@Override
	
	
	public void doClassification (int LastCount, int New_0bservations_Count){
		strategy.doClassification ();
	}
	
	
	/*
	 * @see searchEngine.ISearchEngine#loadDefaultCluster()
	 */
	@Override

	public void loadDefaultClassifier(String file, String URL) {
		
		try {
			//System.out.println("-----loadDefaultMatcher");
			PropertiesUtil propertiesUtil = new PropertiesUtil(file, URL);
			String name = propertiesUtil.getProperties().getProperty("classification");
			Class theClass = Class.forName(name);
			// get the constructor with one parameter
			// create an instance
			this.strategy = (AbstractClassification) theClass.newInstance();
			
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

}
