package intentionOWL;

import java.util.Iterator;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
/**
 * @author salma
 */

public class Verb {
		
	public static Individual getVerbValue(OntModel model, String URI, String verb){
		
		Individual indiv = null;
		Individual indivVerb = null;
		OntClass v = model.getOntClass(URI+"#verb");
		Iterator instances = v.listInstances();
		String inst = null;
		while (instances.hasNext()){ 
			indiv = (Individual) instances.next(); 
			inst = getAsShortString(indiv.toString());
			if (inst.equals(verb)){
				indivVerb = indiv;
			}
		}
		
		return indivVerb;
	}
	

	public static boolean MatchProperty (Individual indiv1, Individual indiv2, OntProperty property){
		boolean find =false;	
		StmtIterator node = indiv1.listProperties(property);
			while (node.hasNext()){
				Statement var = node.next();
				Resource indiv = var.getResource();
				if (getAsShortString(indiv.toString()).equals(getAsShortString(indiv2.toString()))){
					find = true ;
					break;
				}
			}
		return find;				
	}
	
	
	
	public static boolean isSynonyms (OntModel model, Individual indiv1, Individual indiv2){	
		boolean find =false;	
		ExtendedIterator<? extends Resource> synonyms = indiv1.listSameAs();
		while (synonyms.hasNext()){
			Resource indiv = synonyms.next();
			if (getAsShortString(indiv.toString()).equals(indiv2.toString())){
				find = true ;
				break;
			}
		}
		return find;	
	}
	
	public static boolean isSynonym (OntModel model, Individual indiv1, Individual indiv2){	
	 	String URI = "http://www.semanticweb.org/ontologies/2011/5/Ontology1309178915833.owl";
		OntProperty synonym= model.getOntProperty(URI+"#hasSynonym");
		return MatchProperty (indiv1,  indiv2, synonym);
	}
	
	public static boolean isHypernym(OntModel model, Individual indiv1, Individual indiv2){
		String URI = "http://www.semanticweb.org/ontologies/2011/5/Ontology1309178915833.owl";
		OntProperty hypernym= model.getOntProperty(URI+"#hasHypernym");
		return MatchProperty (indiv1,  indiv2, hypernym);
		
	}
	
	public static boolean isHyponym(OntModel model, Individual indiv1, Individual indiv2){
		String URI = "http://www.semanticweb.org/ontologies/2011/5/Ontology1309178915833.owl";
		OntProperty hypernym= model.getOntProperty(URI+"#hasHyponym");
		return MatchProperty (indiv1,  indiv2, hypernym);
		
	}
	
	public static boolean hasSameSens(OntModel model, Individual indiv1, Individual indiv2){
		String URI = "http://www.semanticweb.org/ontologies/2011/5/Ontology1309178915833.owl";
		OntProperty hypernym= model.getOntProperty(URI+"#hasSens");
		return MatchProperty (indiv1,  indiv2, hypernym);
		
	}
	
	 public static String getAsShortString(String elt)
	    {
		final int sharpPosition = elt.indexOf('#');
		return elt.substring(sharpPosition+1, elt.length());
	    }
	
	

}
