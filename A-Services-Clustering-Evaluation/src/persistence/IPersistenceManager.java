package persistence;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;

/**
 * This interface provides the access to the ontologies, services directories.
 * 
 * @author Salma Najar
 */
public interface IPersistenceManager {

	/**
	 * Adds a {@code ModelListener}.
	 * 
	 * @param listener
	 *            the {@code ModelListener} to be added
	 */
	public void addModelListener(ModelListener listener);

	/**
	 * @param ontologyFile
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<String> addOntologies(URL ontologyFile) throws FileNotFoundException, IOException;
	
	/**
	 * @param verbFile
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<String> addverb(URL ontologyFile) throws FileNotFoundException, IOException;
	/**
	 * @param contextFile
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<String> addctx(URL ontologyFile) throws FileNotFoundException, IOException;

	
	/**
	 * Method which returns a jena model loaded with domain ontologies. Without any reasoner, used
	 * for calculating the distance between two nodes.
	 * 
	 * @return a jena model with loaded ontologies, without reasoner
	 */
	public Model getDomainOntologies();

	public InfModel getInfOntologies();
	public InfModel getInfVerb();
	public InfModel getInfContext();


	
	
	/**
	 * Method which returns a jena model of all verb
	 * 
	 * @return jena model of services
	 */
	public Model getVerb();
	
	/**
	 * Method which returns a jena model of all context
	 * 
	 * @return jena model of services
	 */
	public Model getContext();

	/**
	 * method for loading the default persistence
	 * 
	 * @throws ClassNotFoundException
	 */
	public void loadPersistence(String file, String URL) throws ClassNotFoundException;

	/**
	 * Removes a {@code ModelListener}.
	 * 
	 * @param listener
	 *            the {@code ModelListener} to be removed
	 */
	public void removeModelListener(ModelListener listener);

	/**
	 * @param ontologyFile
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<String> removeOntology(URL ontologyFile) throws FileNotFoundException, IOException;

	
	/**
	 * 
	 */
	public void saveOntologies();
	
}