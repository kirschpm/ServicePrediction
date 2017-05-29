package intentionOWL;

import org.semanticweb.owl.model.OWLOntologyCreationException;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * 
 * @author salma
 *
 */
public class VerbMatch {
	public static String[] MATCHES = { "EXACT", "SYNONYM", "HYPERNYM", "HYPONYM", "FAIL" };
	public static String EXACT = "EXACT";
	public static String SYNONYM = "SYNONYM";
	public static String HYPERNYM = "HYPERNYM";
	public static String HYPONYM = "HYPONYM";
	public static String FAIL = "FAIL";
	
	String URI = "http://www.semanticweb.org/ontologies/2011/5/Ontology1309178915833.owl";
	
	public static void main (String [] args) throws OWLOntologyCreationException{
		 String URIService = "http://www.citypassenger.com/services/OntologyVerb.owl";
		 String URI = "http://www.semanticweb.org/ontologies/2011/5/Ontology1309178915833.owl";
	        
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		model.read(URIService);
		String book = "book";
		String request = "request";		
	}
	
	public double calculScoreVerb(String match) {
		if (match.equals("EXACT"))
			return 1;
		if (match.equals("SYNONYM"))
			return 0.8;
		if (match.equals("HYPONYM"))
			return 0.6;
		if (match.equals("HYPERNYM"))
			return 0.4;
		else 
			return 0;
	}
	
	public static String getMatchType(OntModel model, String URI, String verbUser, String verbService) throws OWLOntologyCreationException {	
		Individual vUser  =  Verb.getVerbValue(model, URI, verbUser);
		Individual vService = Verb.getVerbValue(model, URI, verbService);	
		
		Verb verb = new Verb();
		if (vUser == null || vService == null)
			return FAIL;
		else {
			
			if (vUser.equals(vService))
				return EXACT;
			else if(Verb.isSynonym( model,  vUser,  vService))
				return SYNONYM;
			else if (Verb.isHypernym( model,  vUser,  vService))
				return HYPERNYM;
			else if(Verb.isHyponym( model,  vUser,  vService))
				return HYPONYM;
			else
				return FAIL;
		}
				
		
		
	}
}
