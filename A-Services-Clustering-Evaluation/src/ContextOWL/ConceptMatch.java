package ContextOWL;

import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Set;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLType;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ResIterator;
import org.semanticweb.owl.model.*;

/**
 * 
 * @author salma
z */

public class ConceptMatch {
	
	public static String[] MATCHES = { "EXACT", "SUBSUME", "RELAXED", "FAIL" };
	public static String EXACT = "EXACT";
	public static String SUBSUME = "SUBSUME";
	public static String PLUGIN = "PLUGIN";
	public static String FAIL = "FAIL";
	
	public static String matchContextProfile(Model model, OWLKnowledgeBase kb, String entityService, String entityContextProfile) throws OWLOntologyCreationException, FileNotFoundException, URISyntaxException{

		OWLClass owlc1 = null;
		OWLClass owlc2 = null;
		
		URI c1URI = getConceptForString(model, entityService);
		URI c2URI = getConceptForString(model, entityContextProfile);
		String match = "FAIL";
		double d = -1;
		if (c1URI != null && c2URI != null) {
			 owlc1 = kb.getClass(c1URI);
			 owlc2 = kb.getClass(c2URI);
			 match = getMatchType(owlc1, owlc2);	
		} 
		else
			System.out.println("URI Problem matchContextProfile "+ entityService + " , "+ entityContextProfile);
		
		return match ;
		
	}

	public static double calculDistanceConcept (Model model, OWLKnowledgeBase kb, String start, String end) throws OWLOntologyCreationException, FileNotFoundException, URISyntaxException{

		OWLClass owlc1 = null;
		OWLClass owlc2 = null;
		
		URI c1URI = getConceptForString(model, start);
		URI c2URI = getConceptForString(model, end);
		double d = -1;
		if (c1URI != null && c2URI != null) {
			 owlc1 = kb.getClass(c1URI);
			 owlc2 = kb.getClass(c2URI);
			 d = distance (owlc1, owlc2);
		} 
		else
			System.out.println("URI Problem calculDistance: "+ start + " , "+ end + "," + d);
		return d;
	}
	
	public static double calculScoreConcept(Model model,OWLKnowledgeBase kb, String start, String end) throws OWLOntologyCreationException, FileNotFoundException, URISyntaxException{
		double d = calculDistanceConcept (model, kb, start, end);
		if (d == 0)
			return 1;
		if (d == -1)
			return 0;
		else 
			return 1/(d+1);
	}
	
	public static String getMatchType(OWLClass start, OWLClass end) throws OWLOntologyCreationException {		
		if (start.isEquivalent(end))
			return EXACT;
		else if (start.isSubTypeOf(end))
			return SUBSUME;
		else if (calculDistance(end, start) > 0)
			return SUBSUME;
		else if (end.isSubTypeOf(start))
			return PLUGIN;
		else if (calculDistance(start, end) > 0)
			return PLUGIN;
		else
			return FAIL;
	}
	
	////////////////
	
	public static double distance (OWLClass start, OWLClass end) throws OWLOntologyCreationException{
		double distance = -1;
		String match = null;
		if (start !=null && end != null){			
				match = getMatchType(start, end);
				if (match.equals(PLUGIN))
					distance = calculDistance(start, end);	
				if (match.equals(SUBSUME))
					distance = calculDistance(end, start);
				if (match.equals(EXACT))
					distance=0;
				if (match.equals(FAIL))
					distance = -1;
		}
		else 
			distance = -1;
		return distance;
	}
	
	///////////////
	public static double calculDistance(OWLClass start, OWLClass end) throws OWLOntologyCreationException{
		int distance = 0;
		Link link = new Link (0, false);
		int calcul = descendants (start, end, distance, link);
		return calcul;
	}

	public static double calculScore(OWLClass start, OWLClass end) throws OWLOntologyCreationException{

		double d = distance (start, end);
		if (d == 0)
			return 1;
		if (d == -1)
			return 0;
		else 
			return 1/(d+1);
	}
	
	////////////////
	
	public static int descendants (OWLClass start, OWLType end, int distance, Link link){
		int i =0;
		boolean find = false;
		
		Set descendants = start.getSubClasses();
		Iterator it = descendants.iterator();
		if (descendants.isEmpty())
			distance--;
		else 
			distance ++;
		while (it.hasNext()){		
			i++;
			OWLClass  desc = (OWLClass) it.next();
			if (desc.isEquivalent(end)){
				find = true;
				link.setDistance(distance);
				link.setFind(find);
				break;
			}
			else{
				descendants (desc, end, distance, link);
			}
				
		}
		
		if (link.getFind()){
			return link.getdistance() ;
		}
		else 
			return 0;
	}
	
	/////////////
	public static URI getConceptForString(Model ont, String name) {

		ResIterator ri = ont.listSubjects();
		URI uri = null;

		while (ri.hasNext()) {
			String temp = ri.next().toString();
			
			if (temp.matches(".*#(?i:" + name + ")")) {
				try {
					uri = new URI(temp);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("getConceptForString problem : "+ e.getLocalizedMessage());
				}
			}
		}

		return uri;

	}
}
