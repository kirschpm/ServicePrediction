package PredictionManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


import PredictionEngine.PredictionEngineImpl;
import PredictionEngine.IPredictionEngine;

import persistence.IPersistenceManager;
import persistence.PersistenceManagerImpl;
import tools.PredictionResultSet;
import tools.PredictionRequest;
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
public class PredictionManagerImpl implements IPredictionManager {

	/**
	 * single instance of ISearchEngine
	 */

	
	private IPredictionEngine predictionEngine = PredictionEngineImpl.getInstance();
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
	public PredictionManagerImpl(String File, String URL) {	
		//System.out.println("-----ServiceManagerImpl-persistenceManager");
		persistenceManager = PersistenceManagerImpl.getInstance(File, URL);
		//System.out.println("-----ServiceManagerImpl-setPersistenceManager");
		predictionEngine.setPersistenceManager(persistenceManager);
		//System.out.println("-----ServiceManagerImpl-loadDefaultMatcher");
		predictionEngine.loadDefaultCluster(File, URL);
	}
	
	

	/**
	 * @see IPredictionManager.IServiceManager#addOntologies(java.net.URL)
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
	public String doPrediction (String requestDescriptionFile) {
		
		
		PredictionRequest sr = new PredictionRequest();
		
		try {
			//xmlParser.parseRequest(new File(requestDescriptionFile), sr);
			xmlParser.parseRequest(requestDescriptionFile, sr);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		System.out.println("DebugDoPrediction");
		return predictionEngine.doPrediction(sr);
	}



}
