package persistence;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import tools.PropertiesUtil;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * Interface to code against if implementing a new persistence solution
 * 
 * 
 * @author Salma Najar
 * 
 */
public interface IPersistenceFacade {

	
	/**
	 * @param ontology
	 * @return
	 * @throws IOException
	 */
	public String addOntology(File ontology) throws IOException;
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public Model getOntologies() throws IOException;
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public Model getContextModel() throws IOException;
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public Model getVerbOntology() throws IOException;
	
	
	/**
	 * @param ontology
	 * @return
	 * @throws IOException
	 */
	public String removeOntology(File ontology) throws IOException;

	
	
	/**
	 * @param util
	 */
	public void setPropertiesUtil(PropertiesUtil util);

}
