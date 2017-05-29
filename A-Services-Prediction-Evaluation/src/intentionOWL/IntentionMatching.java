/**
 * 
 */
package intentionOWL;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import org.mindswap.owl.OWLKnowledgeBase;
import org.semanticweb.owl.model.OWLOntologyCreationException;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import ContextOWL.ConceptMatch;
import OWLSExtension.OWLSExtensions.Intention;
import OWLSExtension.OWLSExtensions.Verb;
import OWLSExtension.OWLSExtensions.Target;
import tools.PredictionRequest;

/**
 * @author Salma Najar
 *
 */



public class IntentionMatching {
	PredictionRequest requestDescription;
	//Change to String
	String Sverb;
	String Starget;
	String Ssens;
	String Uverb;
	String Utarget;
//	String Usens;

	
	//public IntentionMatching(String sVERB, String sTARG, String sSENS, String uVERB, String uTARG, String uSENS) {
	public IntentionMatching(String sVERB, String sTARG, String uVERB, String uTARG) {
		
		this.Sverb = sVERB;
		this.Starget = sTARG;	
//		this.Ssens = sSENS;	
		
		this.Uverb = uVERB;
		this.Utarget = uTARG;	
//		this.Usens = uSENS;
		// TODO Auto-generated constructor stub
	}
	
	public double getScoreIntention(Model modelTarget, OWLKnowledgeBase kb, OntModel modelVerb, String URI) throws OWLOntologyCreationException, FileNotFoundException, URISyntaxException{
        double scoreIntention = 0 ;
        double scoreTarget = ConceptMatch.calculScoreConcept(modelTarget, kb, getAsShortString(this.Utarget), getAsShortString(this.Starget)); 
    	scoreIntention =  0;
        //System.out.println("---score Target:" + scoreTarget);
        if (scoreTarget >= 0.25){
        	//Score Verb

                VerbMatch vmatch = new VerbMatch();           
                String match = VerbMatch.getMatchType(modelVerb, URI, this.Uverb, getAsShortString(this.Sverb));
                double scoreVerb = vmatch.calculScoreVerb(match);  
               // System.out.println("---score Verb:" + scoreVerb);
                scoreIntention =  (0.8 *scoreVerb) + scoreTarget;
        		//score Intention

        }
        else
        	scoreIntention =  0;
      //  System.out.println("score Intention:" + scoreIntention);
		return scoreIntention;
	}
	
	
	 public static String getAsShortString(String elt)
	    {
		final int sharpPosition = elt.indexOf('#');
		return elt.substring(sharpPosition+1, elt.length());
	    }
	 
	 public static String getURI(String elt)
	    {
		final int sharpPosition = elt.indexOf('#');
		return elt.substring(0,sharpPosition);
	    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
