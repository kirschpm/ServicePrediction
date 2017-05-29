package classificationManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;


import classificationEngine.ClassificationEngineImpl;
import classificationEngine.IClassificationEngine;


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
public class ClassificationManagerImpl implements IClassificationManager {

	/**
	 * single instance of ISearchEngine
	 */

	
	private IClassificationEngine clusterEngine = ClassificationEngineImpl.getInstance();
	

	/**
	 * 
	 * @author Salma Najar
	 * 
	 */

	/**
	 * private constructor
	 */
	public ClassificationManagerImpl(String File, String URL) {	
		clusterEngine.loadDefaultClassifier(File, URL);
	}

	

	public void doClassification (int LastCount, int New_0bservations_Count)
	{
		// TODO Auto-generated method stub
		clusterEngine.doClassification(LastCount, New_0bservations_Count);
	}



}
