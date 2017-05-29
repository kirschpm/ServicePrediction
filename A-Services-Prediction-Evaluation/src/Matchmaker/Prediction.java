package Matchmaker;

import impl.jena.OWLOntologyImpl;
import intentionOWL.IntentionMatching;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.sql.ResultSet;

import javax.xml.parsers.ParserConfigurationException;

import org.context.model.api.IContextElement;
import org.context.profile.impl.ProfileContextImpl;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.xml.sax.SAXException;

import tools.Sqlite;
import ContextOWL.MatchingContext;
import ContextOWL.Parser;
import OWLSExtension.OWLSExtensions;
import OWLSExtension.OWLSExtensions.ExtendedService;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResIterator;

import persistence.IPersistenceManager;

import tools.PredictionRequest;
import tools.PredictionResultSet;
import tools.PropertiesUtil;
import tools.XMLParser;

public class Prediction extends AbstractPrediction {
	
	 /* 
	 * OWLKnowledgeBase this kb will be used to operate (search) on.
	 */
	private OWLKnowledgeBase kb;
	private Model ont;

	private IPersistenceManager pManager;

	private XMLParser xmlParser = new XMLParser();
	
	
	
	public static ResultSet r = null;
	
	@Override
	public String doPrediction(PredictionRequest requestDescription) {
		
		//long start = System.currentTimeMillis();
		
		String configUrl = "Config.properties";
		String configFile = "conf/Config.properties";

		PropertiesUtil propertiesUtil = new PropertiesUtil(configFile, configUrl);
		Properties prop = propertiesUtil.getProperties();
		double seuil = Double.parseDouble(prop.getProperty("threshold")); 
		System.out.println("--seuil:"+ seuil);
		
		PredictionResultSet result = new PredictionResultSet();
		String intention = requestDescription.getIntention();
		String verb = requestDescription.getVerb();
		String target = requestDescription.getTarget();
	//	String context = requestDescription.getContext();
		String context = "<ctx:context><ctx:condition><ctx:contextElement>"+
		"<ctx:hasEntity resource=\"http://www.citypassenger.com/services/ContextModel.owl#concept.Entity.Person\"/>"+
		"<ctx:hasScope resource=\"http://www.citypassenger.com/services/ContextModel.owl#concept.Scope.Location\"/>"+
		"<ctx:hasRepresentation resource=\"http://www.citypassenger.com/services/ContextModel.owl#concept.Representation.Location_Representation\"/>"+
		"<ctx:contextValueSet><ctx:contextValue><ctx:hasScope resource=\"http://www.citypassenger.com/services/ContextModel.owl#concept.Scope.Location.City\"></ctx:hasScope>"+
		"<ctx:hasRepresentation resource=\"http://www.citypassenger.com/services/ContextModel.owl#concept.Representation.Location_Representation.City_Representation\"/>"+
		"<ctx:valueSet><ctx:valueElement><ctx:operator resource=\"http://www.citypassenger.com/services/ContextModel.owl#Concept.Operator.Equal\"/>"+
		"<ctx:value>France</ctx:value></ctx:valueElement></ctx:valueSet></ctx:contextValue> </ctx:contextValueSet> </ctx:contextElement><ctx:contextElement>"+
		"<ctx:hasEntity resource=\"http://www.citypassenger.com/services/ContextModel.owl#concept.Entity.Person\"/>"+
		"<ctx:hasScope resource=\"http://www.citypassenger.com/services/ContextModel.owl#concept.Scope.Profile\"/>"+
		"<ctx:hasRepresentation resource=\"http://www.citypassenger.com/services/ContextModel.owl#concept.Representation.Profile_Representation\"/>"+
		"<ctx:contextValueSet><ctx:contextValue><ctx:hasScope resource=\"http://www.citypassenger.com/services/ContextModel.owl#concept.Scope.Profile.Age\"></ctx:hasScope>"+
		"<ctx:hasRepresentation resource=\"http://www.citypassenger.com/services/ContextModel.owl#concept.Representation.Profile_Representation.Age_Representation\"/>"+
		"<ctx:valueSet><ctx:valueElement><ctx:operator resource=\"http://www.citypassenger.com/services/ContextModel.owl#Concept.Operator.Equal\"/><ctx:value>34</ctx:value>"+
		"</ctx:valueElement></ctx:valueSet></ctx:contextValue> </ctx:contextValueSet>      </ctx:contextElement><ctx:contextElement>"+
		"<ctx:hasEntity resource=\"http://www.citypassenger.com/services/ContextModel.owl#concept.Entity.Person\"/>"+
		"<ctx:hasScope resource=\"http://www.citypassenger.com/services/ContextModel.owl#concept.Scope.DateTime\"/>"+
		"<ctx:hasRepresentation resource=\"http://www.citypassenger.com/services/ContextModel.owl#concept.Representation.DateTime_Representation\"/>"+
		"<ctx:contextValueSet><ctx:contextValue><ctx:hasScope resource=\"http://www.citypassenger.com/services/ContextModel.owl#concept.Scope.DateTime.Season\"></ctx:hasScope>"+
		"<ctx:hasRepresentation resource=\"http://www.citypassenger.com/services/ContextModel.owl#concept.Representation.DateTime_Representation.Season_Representation\"/>"+
		"<ctx:valueSet><ctx:valueElement><ctx:operator resource=\"http://www.citypassenger.com/services/ContextModel.owl#Concept.Operator.Equal\"/><ctx:value>springer</ctx:value></ctx:valueElement></ctx:valueSet>"+
		"</ctx:contextValue> </ctx:contextValueSet></ctx:contextElement></ctx:condition></ctx:context>";
		
		String cluster_verb;
		String cluster_target;
		
		String cluster_context ;
		String cluster_intention ;
		String cluster_service ;
		int cluster_ID;
		Sqlite maBase = new Sqlite();
		
		
		try{
			
			
	   	 maBase.connect();
	   
	   	 long start1 = System.currentTimeMillis();
			
	   	 File fileContext = new File ("data/owls-tc4/OntologyService/context/ContextModel.owl");
		 String ontContext = fileContext.getAbsolutePath();
		 Model modelContext = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
		 modelContext.read("file://"+ontContext);
			
		 OWLKnowledgeBase kbContext = OWLFactory.createKB();
		 kbContext.read("file://"+ ontContext);
			
		 File fileProfileContext = new File ("data/owls-tc4/OntologyService/context/ProfilContextModel.owl");
		 String file = fileProfileContext.getAbsolutePath();
		 String URI = "http://www.semanticweb.org/ontologies/2011/10/ProfilContextModel.owl#";
		 ProfileContextImpl pc = new ProfileContextImpl("file://"+file, URI);
			
	     File fileVerb = new File ("data/owls-tc4/OntologyService/verb/OntologyVerb.owl");
		 String VerbOntology = fileVerb.getAbsolutePath();
	     String URIVerbOntology = "http://www.semanticweb.org/ontologies/2011/5/Ontology1309178915833.owl";
	        
	     OntModel modelVerb = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		 modelVerb.read("file://"+VerbOntology);
		 long end1 = System.currentTimeMillis();
		 long time1 = end1 - start1;
		 System.out.println("Time reading: "+ time1);
			
 
			String sql = "SELECT * FROM MarkovChain as MC, Clusters as C Where MC.State1 = C.ID ";
			r = maBase.executeQuery(sql);
			
			
			while (r.next()){
				
				// rŽcupŽrer  
				cluster_context = r.getString("CentroidC");
				cluster_intention = r.getString("CentroidI");
				cluster_service = r.getString("CentroidS");
				cluster_ID = Integer.parseInt(r.getString("State1"));
				
			
				PredictionRequest pr = new PredictionRequest();
				pr = parse (cluster_intention);	
				
				cluster_verb = pr.getVerb();
				cluster_target = pr.getTarget();
				
				IntentionMatching intentionMatching  = new  IntentionMatching (verb , target, cluster_verb, cluster_target);
				double Iscore = 0;
				try {							
					Iscore = (intentionMatching.getScoreIntention(ont, kb, modelVerb, URIVerbOntology))/2;
				} catch (Exception e) {
					e.printStackTrace();
				}

				// Calculate the Context Score
				
				double Cscore = 0 ;
				if (Iscore>0){
					InputStream streamRequest = new ByteArrayInputStream (context.getBytes());
					InputStream streamCluster = new ByteArrayInputStream (cluster_context.getBytes());
					
					
					IContextElement[] listClusterContextElts = Parser.parseXMLData (streamRequest);
					IContextElement[] listObservationContextElts = Parser.parseXMLData (streamCluster);
					
					for (int k=0; k< listClusterContextElts.length; k++){
						double ctxEltScore = 0;
						IContextElement elt = null;
						elt = MatchingContext.FindContextElementObservation(listClusterContextElts[k], listObservationContextElts);
						
						if (elt != null){
							ctxEltScore = MatchingContext.ScoreContextElement(modelContext, kbContext, pc, listClusterContextElts[k], elt);
							Cscore = Cscore + ctxEltScore;
							
						}
						
					}
					Cscore = Cscore/listClusterContextElts.length;
				}
				
				double score = (Iscore + (0.7 * Cscore))/2;
				
				if (score >= seuil) {
					result.addElement (cluster_ID, Double.valueOf(score));
				}
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getLocalizedMessage());
		}
		
		java.util.Iterator<?> it = result.getIterator();		
		String predicted_service = null;
		double max = 0;
		int predicted_State1_ID = 0;
		
		while (it.hasNext()){
			Map.Entry topService = (Entry) it.next();
			int id = Integer.parseInt(topService.getKey().toString());
			double score_MC = Double.valueOf(topService.getValue().toString());
			if (score_MC> max){
				max = score_MC;
				predicted_State1_ID = id;
			}	
		}
		
		if (predicted_State1_ID != 0){
			try {
				String sql = "SELECT State2, Max(Prob), CentroidS FROM MarkovChain as MC, Clusters as C Where MC.State1 = " + predicted_State1_ID+ " AND MC.State2 = C.ID ";
				r = maBase.executeQuery(sql);
				if (r.next()){
					 predicted_service  = r.getString("CentroidS");
				}
					
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		// TODO Auto-generated method stub
		System.out.println(predicted_service);
		//long end = System.currentTimeMillis();
		//long time = end - start;
		//System.out.println("*************Response Time Fonction : "+ time);

		return predicted_service;
	}

	/**
	 * Loads the ontologies in the model
	 * 
	 */
	public void loadModels() {
	    // NOT SURE IF OK LIKE THIS, MAYBE NEED TO CREATE A NEW MODEL
		this.ont = pManager.getDomainOntologies();

		OWLOntologyImpl ont = new OWLOntologyImpl(null, null, null, pManager.getInfOntologies());
		kb.load(ont);
	}

	/**
	 * 
	 */
	public void loadOntologies() {

		kb = null;
		kb = OWLFactory.createKB();
		
		this.ont = pManager.getDomainOntologies();
		OWLOntologyImpl ont = new OWLOntologyImpl(null, null, null, pManager.getInfOntologies());
		kb.load(ont);

	}




	@Override
	public void setPersistenceManager(IPersistenceManager pManager) {
		this.pManager = pManager;
		loadOntologies();
		//loadModels();
	}
	
	/**
	 * Method to search for a URI of a concept for a given string. It searches through the
	 * ontologies and returns the URI when found a concept.
	 * 
	 * @param name
	 *            The basic string of a request.
	 * @return The URI if a concept is found.
	 */
	public URI getConceptForString(String name) {

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
				}
			}
		}

		return uri;

	}

	

	public PredictionRequest parse (String ToParse){
		  XMLParser s = new XMLParser();
	      StringBuffer str = new StringBuffer();
	      PredictionRequest cr = new PredictionRequest();
	      str.append(ToParse);
	      
	      try
	      {	         
	    	 s.parseRequest(str.toString(), cr);
	      }
	      catch (ParserConfigurationException e)
	      {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	      }
	      catch (SAXException e)
	      {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	      }
	      catch (IOException e)
	      {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	      }
		
		return cr;
	}

	@Override
	public void ontologyiesChanged() {
		// TODO Auto-generated method stub
		
	}

}
