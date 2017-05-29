package ContextOWL;
/**
 * @author salma
 */
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.context.model.api.IContextElement;
import org.context.model.api.IContextValue;
import org.context.model.api.IContextValueSet;
import org.context.model.api.IEntity;
import org.context.model.api.IOperator;
import org.context.model.api.IRepresentation;
import org.context.model.api.IScope;
import org.context.model.api.IValue;
import org.context.model.impl.ValueElement;
import org.context.model.impl.ValueSet;
import org.context.profile.impl.ProfileContextImpl;
import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLKnowledgeBase;
import org.semanticweb.owl.model.OWLOntologyCreationException;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

public class MatchingContext {
	
	public static boolean MatchEntity(String EntityContextService, String EntityContextUser){
		if (EntityContextService.equals(EntityContextUser))
			return true;
		else
			return false;	
	}
	
	public static boolean MatchScope(String ScopeContextService, String ScopeContextUser){
		if (ScopeContextService.equals(ScopeContextService)){
			return true;
		}
			
		else
			return false;	
	}
	
	public static IContextElement FindContextElementObservation(IContextElement contextElementService, IContextElement[] contextElementsUser){
		if (contextElementsUser != null){
			IContextElement elt = null;
			
    		IEntity entityService = contextElementService.getEntity();
    		IScope scopeService = contextElementService.getScope();
	
			for (int i=0; i<contextElementsUser.length; i++){
				IContextElement contextElementUser = contextElementsUser[i];
	    		IEntity entityUser = contextElementUser.getEntity();
	    		IScope scopeUser = contextElementUser.getScope();
	    		if (getAsShortString(entityService.getEntityAsString()).equals(getAsShortString(entityUser.getEntityAsString())) && getAsShortString(scopeService.getScopeAsString()).equals(getAsShortString(scopeUser.getScopeAsString()))){
	    			elt = contextElementUser;
	    			break;
	    		}
	    			
			}	 
			return elt;
		}
		else 
			return null;
	}
	
	  public static double ScoreContextElement(Model model, OWLKnowledgeBase kb, ProfileContextImpl pc,IContextElement ctxEltService, IContextElement ctxEltUser) throws OWLOntologyCreationException, FileNotFoundException, URISyntaxException{

		double score = 0;
		
		/*********************Score entity*********************/
		/******************************************************/	
		String entityService = ctxEltService.getEntity().getEntityAsString();
		String entityUser = ctxEltUser.getEntity().getEntityAsString();
		
		URI entitySceURI = ConceptMatch.getConceptForString(model, getAsShortString(entityService));
		URI entityUserURI = ConceptMatch.getConceptForString(model, getAsShortString(entityUser));
		
		
		OWLClass owlEntitySce = kb.getClass(entitySceURI);
		OWLClass owlEntityUser = kb.getClass(entityUserURI);
		double scoreEntity = ConceptMatch.calculScore(owlEntitySce, owlEntityUser);
		//System.out.println("----score Entity: "+ scoreEntity);
		/*********************Score Scope*********************/
		/*****************************************************/	
		
		String scopeService = ctxEltService.getScope().getScopeAsShortString();
		String scopeUser = ctxEltUser.getScope().getScopeAsShortString();
		URI scopeSceURI = ConceptMatch.getConceptForString(model, getAsShortString(scopeService));
		URI scopeUserURI = ConceptMatch.getConceptForString(model, getAsShortString(scopeUser));
		OWLClass owlScopeSce = kb.getClass(scopeSceURI);
		OWLClass owlScopeUser = kb.getClass(scopeUserURI);
		double scoreScope = ConceptMatch.calculScore(owlScopeSce, owlScopeUser);
		//System.out.println("----score Scope: "+ scoreScope);
		
		
		
		/// rŽcupŽrer les deux reprŽsentations: 
		/**
		String CRepresentation = ctxEltService.getRepresentation().getRepresentationAsString();
		String ORepresentation = ctxEltUser.getRepresentation().getRepresentationAsString();
		SimilarityMeasure sm = new SimilarityMeasure();
		if (CRepresentation.equals(ORepresentation)){
			//trouver la mŽthode
			SimilarityMeasure = sm.getSimilarityMeasure(CRepresentation);
			score = sm.+SimilarityMeasure+();
		}
		else
		if ( RepresentationTransformation.getTransformationMethod (CRepresentation, ORepresentation) != null){
			
		}
		else
		if ( RepresentationTransformation.getTransformationMethod (ORepresentation, CRepresentation) != null){
		}
		else
			score = 0;
		*/
		
		/*********************Score Context Value*********************/
		/************************************************************/	
		
		IContextValueSet ctxServiceHashMap = ctxEltService.getContext();
		double ctxelt = ctxServiceHashMap.keys().size();
		Set<IScope> keysService = (Set<IScope>) ctxServiceHashMap.keys();
		Iterator <IScope> itService = keysService.iterator();
		double scoreCtxEltValueSet = 0;	
		double scoreCtxEltValue = 0;
		while (itService.hasNext()){
			IScope valueScopeService = itService.next();
			String valScopeSce = valueScopeService.getScopeAsShortString();
			
			IContextValue ctxValueService = ctxServiceHashMap.getContextValue(valueScopeService);
			IRepresentation valueRepresentationService = ctxValueService.getRepresentation();
				
			IContextValueSet ctxUserHashMap = ctxEltUser.getContext();
			Set<IScope> keysUser = (Set<IScope>) ctxUserHashMap.keys();
			Iterator <IScope> itUser = keysUser.iterator();			
			int i =0;
			while (itUser.hasNext()){
				i++;
				scoreCtxEltValue = 0;
				IScope valueScopeUser = itUser.next();
				String valScopeUser = valueScopeUser.getScopeAsShortString();

				URI valscopeSceURI = ConceptMatch.getConceptForString(model, getAsShortString(valScopeSce));
				URI valscopeUserURI = ConceptMatch.getConceptForString(model, getAsShortString(valScopeUser));
				OWLClass owlvalScopeSce = kb.getClass(valscopeSceURI);
				OWLClass owlvalScopeUser = kb.getClass(valscopeUserURI);
				
				double scoreValScope = ConceptMatch.calculScoreConcept(model, kb, getAsShortString(valScopeSce), getAsShortString(valScopeUser));

				if (scoreValScope > 0){
					
					/////////Find Weight Attribut
					Iterator it = pc.getProfileAttribut();
					float Wcontext = 1;
					while(it.hasNext()){
						
						Individual indiv = (Individual) it.next();
						float weight =  (float) pc.getWeight(indiv);
						RDFNode profile = pc.getProfile(indiv);
						RDFNode attribut = pc.getAttribut(indiv);
						RDFNode entity = pc.getEntity((Resource)profile);		
						
						String entService = getAsShortString(entityService);
						String entProfileContext = getAsString(entity.toString());
						
						if (getAsString(attribut.toString()).equals(getAsShortString(valScopeUser))){
							if (!ConceptMatch.matchContextProfile(model, kb, entService,entProfileContext).equals("FAIL")){
								Wcontext = weight;
								break;
							}
							
						}
					}
					/////////////////////
					
					float scoreVal = 0;				
					
					IContextValue ctxValueUser = ctxUserHashMap.getContextValue(valueScopeUser);
					IRepresentation valueRepresentationUser = ctxValueUser.getRepresentation();
					
					//if (valueRepresentationService.getRepresentationAsString().equals(valueRepresentationUser.getRepresentationAsString())){
					    ValueSet valueSetUser = ctxValueUser.getValueset();
					    Collection valuesUser = valueSetUser.getValueElements();	
					    Iterator itValUser = valuesUser.iterator();
					    IValue valUser = ((ValueElement) itValUser.next()).getValue();
						
					    ValueSet valueSetService = ctxValueService.getValueset();
						Collection values = valueSetService.getValueElements();				
						int l = values.size();
						Iterator itVal = values.iterator();
						while (itVal.hasNext()){
							ValueElement element = (ValueElement) itVal.next();
							IOperator op = element.getOperator();
							IValue val = element.getValue();
							float scorevalue = calculScoreValue (getAsShortString(op.toString()), val, valUser);	
							scoreVal = scoreVal + scorevalue;			
						}
						
						scoreVal = Wcontext * (scoreVal/l);
						// added 05/01/2012
						if (scoreVal > 0)
							scoreCtxEltValue = ((scoreValScope + scoreVal)/2);		
						else
							scoreCtxEltValue = 0;
						
						scoreCtxEltValueSet = scoreCtxEltValueSet +  scoreCtxEltValue;
						break;
							
					}
				}
			}
		//System.out.println("----score value: "+ scoreCtxEltValueSet/ctxelt);
		
		score = (scoreEntity+ scoreScope + (scoreCtxEltValueSet/ctxelt))/3;
		//System.out.println("-------Final Score Context: "+ score);
		return score;
	}

    private static float calculScoreValue (String op, IValue valService,  IValue valUser) {
		float scoreVal = 0;
		
		Boolean valid; 

		if (op.equals("Equal")){
			if (valUser.toString().equals(valService.toString()))
				scoreVal = 1; 
			else
				scoreVal = 0;
		}
		if (op.equals("Not-Equal")){
			if (! (valUser.toString()).equals((valService.toString())))
				scoreVal = 1; 
			else
				scoreVal = 0;
		}				
		try {
			double v = Double.parseDouble(valService.toString());
			valid = true;
		} catch (NumberFormatException e) {
			valid = false;
		}
			
		if (valid){
			double valueUser = Double.parseDouble(valUser.toString());
			double valueService = Double.parseDouble(valService.toString());
			
			if (op.equals("Higher")){
				if (valueUser > valueService)
					scoreVal = 1; 
				else
					scoreVal = 0;
			}
				
			if (op.equals("Higher-Equal")){
					
				if (valueUser >= valueService)
					scoreVal = 1; 
				else
					scoreVal = 0;
			}
				
			if (op.equals("Lower")){
					
				if (valueUser < valueService)
					scoreVal = 1; 
				else
					scoreVal = 0;
			}
				
			if (op.equals("Lower-Equal")){
					
				if (valueUser <= valueService)
					scoreVal = 1; 
				else
					scoreVal = 0;
					
			}
		}
		return scoreVal;
		
	}

	public static String getAsShortString(String elt)
    {
	final int sharpPosition = elt.lastIndexOf('.');

	return elt.substring(sharpPosition+1, elt.length());
    }
    
    public static String getAsString(String elt)
    {
	final int sharpPosition = elt.lastIndexOf('#');

	return elt.substring(sharpPosition+1, elt.length());
    }
		
}
