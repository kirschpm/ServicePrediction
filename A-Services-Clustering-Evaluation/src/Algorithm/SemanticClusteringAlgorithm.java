/**
 * 
 */
package Algorithm;

import impl.jena.OWLOntologyImpl;
import intentionOWL.IntentionMatching;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.context.model.api.IContextElement;
import org.context.profile.impl.ProfileContextImpl;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.xml.sax.SAXException;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResIterator;

import persistence.IPersistenceManager;

import clusterManager.ClusterManagerImpl;
import clusterManager.IClusterManager;

import tools.ClusterRequest;
import tools.ClusteringResultSet;
import tools.XMLParser;

import ContextOWL.MatchingContext;
import ContextOWL.Parser;
import Input.Cluster;
import Input.Observation;


/**
 * @author salma Najar
 *
 */
public class SemanticClusteringAlgorithm  extends AbstractClustering  {

	/**
	 * 
	 */
	
	String file = "Conf/Confi.properties";
	String URL = "config.properties";
	
	private OWLKnowledgeBase kb;
	private Model ont;

	private IPersistenceManager pManager;
	private XMLParser xmlParser = new XMLParser();
	
	//ClusteringThreshold to be added in the configuration file
	private double clusteringThreshold = 0.6;
	private double clusteringScore = 0;
	public Cluster cluster = null;

	
	
	
	public SemanticClusteringAlgorithm() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see Algorithm.ClusteringAlgorithm#doClustering(java.util.List, java.util.List)
	 */
	@Override
	public  void getClusters(List<Observation> Observations, List<Cluster> Clusters, int LastClusterID) {
		
	}
	@Override
	public  void getClusters(Observation Observations[], Cluster Clusters[]) {
	//public  void getClusters(Observation Observations[], Cluster Clusters[], int LastClusterID) {
		
		// TODO Auto-generated method stub
		  
			// Create a hash map for the updated and new clusters
			
		     int newclustersize = 0;
		     int clusterNbNew =0;
		     int updatedclustersize = 0;
			 
			 Cluster UpdatedClustersSet[] = new Cluster[50];
			 Cluster NewClustersSet[] = new Cluster[50];
			 Cluster ListClusters [] = new Cluster [100];
			 
			 
			 // Create a hash map for the final clusters modification
			 ClusteringResultSet result = new ClusteringResultSet();
			
			 if (Clusters.length>0){
				 for (int i=0; i<Clusters.length;i++){
					 ListClusters [i] = new Cluster();
					 ListClusters [i] = Clusters[i];
				 }
				 
			 }
				 
			 ClusterRequest Ocr = new ClusterRequest();
		
			 try{
				 
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
				 Sqlite maBase = new Sqlite();
		    	 maBase.connect();
		    	 
				 Observation Observ = new Observation (); 
				 int idObserv = 0;
				 long FinalTime = 0;

				 for (int i=0; i<Observations.length; i++){
					 //Get the parsing of the observation intention  (verb + target)
					long start = System.currentTimeMillis();
				
					Observ = Observations[i];
					Ocr = parse(Observations[i].getIntention());	
					String oVerb = Ocr.getVerb();
					
					String oTarget = Ocr.getTarget();
//					String oSens = Ocr.getSensVerb();
					String oContext = Observ.getContext();
					String oService = Observ.getService();
				
					ClusterRequest Ccr = new ClusterRequest();
					
					//if (ListClusters.size() == 0){
					int size = -1;
					
					if (ListClusters[0] != null)
						size = ListClusters.length;
					/*for (int o=0; o<ListClusters.length; o++){
						if (ListClusters[o] == null)
							break;
						else
							size = o;
							
					}*/// End for ListClusters
						
					if (size == -1){	
					    //System.out.println("Clusters Vide");
			    		String sql = "INSERT INTO Clusters (Name, CentroidC, CentroidI, CentroidS) VALUES ('Cluster_1', '"+ oContext +"', '"+ Observations[i].getIntention() + "', '"+ oService + "')";            

						maBase.executeMyUpdate(sql);
						//System.out.println("Executed");
						
						
						Observation obs[] = new Observation[1];
						obs[0] = new Observation();
		    	   		
		        		obs[0].setID(Observations[i].getID());
		        		obs[0].setUser(Observations[i].getUser());
		        		obs[0].setContext(Observations[i].getContext());
		        		obs[0].setIntention(Observations[i].getIntention());
		        		obs[0].setService(Observations[i].getService());

		   	    		ListClusters[0] = new Cluster();
		   	    		ListClusters[0].setID(1);
		   	    		ListClusters[0].setName("Cluster_1");
		   	    		ListClusters[0].setCentroidContext(oContext);
		   	    		ListClusters[0].setCentroidIntention(Observations[i].getIntention());
		   	    		ListClusters[0].setCentroidService(oService);
		   	    		ListClusters[0].setCentroidAverage(0);
		   	    		

						//System.out.println("Observations.get(i).getID() (2): "+ idObserv);
						sql = "UPDATE Trace SET ClusterID ="+ 1 +" WHERE ID="+ Observ.getID();
						maBase.executeMyUpdate(sql);
						
					} //end if (size == -1)
					
					else{						
						int clusterNb = 0;
						cluster = null;
						clusteringScore = 0;
						
						for (int j=0; j<ListClusters.length; j++){
							if (ListClusters[j]==null)
								break;
							else{
								clusterNb++;
								//System.out.println("---- ListClusters number: " + clusterNb);
								
								Ccr = parse (ListClusters[j].getCentroidIntention());	
								// get Intention from cluster
									
								String cVerb = Ccr.getVerb();
								String cTarget = Ccr.getTarget();
								//String cSens = Ccr.getSensVerb();
								
								String cContext = ListClusters[j].getCentroidContext();
								//URL cContextURL = getContextURL(cContext);
									
								
								//DistanceMeasure (oVerb,oTarget, cVerb, cTarget)
								//System.out.println(oVerb +","+ oTarget+","+ cVerb+","+ cTarget);
								IntentionMatching intentionMatching  = new  IntentionMatching (oVerb , oTarget, cVerb, cTarget);
//								IntentionMatching intentionMatching  = new  IntentionMatching (oVerb , oTarget, oSens, cVerb, cTarget, cSens);
								double Iscore = 0;
								try {							
									Iscore = (intentionMatching.getScoreIntention(ont, kb, modelVerb, URIVerbOntology))/2;
								} catch (Exception e) {
									e.printStackTrace();
								}

								// Calculate the Context Score
								
								double Cscore = 0 ;
								if (Iscore>0){
							       
									InputStream streamObservation = new ByteArrayInputStream (oContext.getBytes());
									InputStream streamCluster = new ByteArrayInputStream (cContext.getBytes());
									IContextElement[] listClusterContextElts = Parser.parseXMLData (streamObservation);
									IContextElement[] listObservationContextElts = Parser.parseXMLData (streamCluster);
									
									for (int k=0; k< listClusterContextElts.length; k++){
										double ctxEltScore = 0;
										IContextElement elt = null;
										elt = MatchingContext.FindContextElementObservation(listClusterContextElts[k], listObservationContextElts);
										if (elt != null){
											ctxEltScore = MatchingContext.ScoreContextElement(modelContext, kbContext, pc, listClusterContextElts[k], elt);
											Cscore = Cscore + ctxEltScore;
										}
										
									} //End for K
									Cscore = Cscore/listClusterContextElts.length;
								} // end if (Iscore>0)
								
								double score = (Iscore + (0.7 * Cscore))/2;
								//System.out.println("Score Matching: "+ score);
								
								if (score > clusteringScore){
									clusteringScore = score;
									
									cluster = ListClusters[j];
								}
							}// end Else
						 } // 	end  for (int j=0; j<ListClusters.length; j++)
						
						
						// vŽrifier que le cluster existe dans 
						
						if (clusteringScore >= clusteringThreshold){

							if (ExistInClusterSet(cluster, UpdatedClustersSet)){
								//System.out.println("-----Step (2) - Match - ExistInUpdatedClustersSet-------");
								
								// Display elements

								for (int j=0; j<UpdatedClustersSet.length;j++){
									Cluster updated_cluster = UpdatedClustersSet[j];
									Observation observ[] = new Observation[50];
									if (updated_cluster.getName().equals(cluster.getName())){
										Observation observ2[] = new Observation [50];
										observ2 = updated_cluster.getObservations();
										
										int nbo = 0;
										for(Observation element : observ2){
										    if (element != null)
										    	nbo++;
										}									

										observ2[nbo] = Observ;
										UpdatedClustersSet[j].setObservations(observ2);
										break;
									}	
								}// end For Updated
							} // end if (ExistInClusterSet(cluster, UpdatedClustersSet))
							
							else{
								//System.out.println("-----Step (2) - NotExistInUpdatedClustersSet-------");		
								Observation observTab [] = new Observation [50];
								Observation observ [] = new Observation [50];
								int nb = 0;
								if (cluster.getObservations() != null){
									observ = cluster.getObservations();	
									for (int p=0; p<observ.length;p++){
										observTab[p] = new Observation ();
										observTab[p] = observ[p];
										nb = p+1 ;
									}
								}
								
								//System.out.println("Nombre nb : "+ nb);
								observTab[nb] = new Observation ();
								observTab[nb].setID(Observations[i].getID());
								observTab[nb].setUser(Observations[i].getUser());
								observTab[nb].setContext(Observations[i].getContext());
								observTab[nb].setIntention(Observations[i].getIntention());
								observTab[nb].setService(Observations[i].getService());
								
								cluster.setObservations(observTab);
								// voir s'il existe dans le updateClusterSet ou pas
								UpdatedClustersSet[updatedclustersize] = cluster;
								updatedclustersize++;
								
							}  //end ELSE NotExistInUpdatedClustersSet 
						} // end if (clusteringScore >= clusteringThreshold){

	
						else{ //ELSE FINALE
							
							/// test avec newClusterSet
								double clusterScore=0;
								Cluster cluster2 = null;
								int lCluster = -1;
								//Cluster new_cluster = new Cluster ();
								for (int l=0; l<NewClustersSet.length; l++){
									
									if ( NewClustersSet[l] != null){
										
										
										ClusterRequest cr = parse (NewClustersSet[l].getCentroidIntention());	
										// get Intention from cluster
											
										String Verb = cr.getVerb();
										String Target = cr.getTarget();

										String Context = NewClustersSet[l].getCentroidContext();
										IntentionMatching intentionMatching  = new  IntentionMatching (oVerb , oTarget, Verb, Target);
										double Iscore = 0;
										try {							
											Iscore = (intentionMatching.getScoreIntention(ont, kb, modelVerb, URIVerbOntology))/2;
										} catch (Exception e) {
											e.printStackTrace();
										}

										// Calculate the Context Score
										
										double Cscore = 0 ;
										if (Iscore>0){
									       
											InputStream streamObservation = new ByteArrayInputStream (oContext.getBytes());
											InputStream streamCluster = new ByteArrayInputStream (Context.getBytes());
											IContextElement[] listClusterContextElts = Parser.parseXMLData (streamObservation);
											IContextElement[] listObservationContextElts = Parser.parseXMLData (streamCluster);
											
											for (int k=0; k< listClusterContextElts.length; k++){
												double ctxEltScore = 0;
												IContextElement elt = null;
												elt = MatchingContext.FindContextElementObservation(listClusterContextElts[k], listObservationContextElts);
												if (elt != null){
													ctxEltScore = MatchingContext.ScoreContextElement(modelContext, kbContext, pc, listClusterContextElts[k], elt);
													Cscore = Cscore + ctxEltScore;
												}
												
											} //End for K
											Cscore = Cscore/listClusterContextElts.length;
										} // end if (Iscore>0)
										
										double score2 = (Iscore + (0.7 * Cscore))/2;

										if (score2 > clusterScore){
											clusterScore = score2;
											lCluster = l;
										}
									}
									else 
										break;
									
								}// End for New Cluster
										
									
								if (clusterScore >= clusteringThreshold){
								
									Observation observ2 [] = new Observation [50];
									Observation observat [] = new Observation [50];
									int nbo = 0;
									
									if (NewClustersSet[lCluster].getObservations() != null){
										observat = NewClustersSet[lCluster].getObservations();	
										for (int p=0; p<observat.length;p++){
											if (observat[p]!=null){
												observ2[p] = new Observation ();
												observ2[p] = observat[p];
												nbo = p+1 ;
											}
											
										}
									}	
									
									observ2[nbo] = Observ;
									NewClustersSet[lCluster].setObservations(observ2);
									
								}	

								else{ /// else if score < threshlod
										
										//System.out.println("-----NewClustersSet-------");
										
										Observation obs[] = new Observation[1];
										obs[0] = new Observation ();
						        		obs[0].setID(Observations[i].getID());
						        		obs[0].setUser(Observations[i].getUser());
						        		obs[0].setContext(Observations[i].getContext());
						        		obs[0].setIntention(Observations[i].getIntention());
						        		obs[0].setService(Observations[i].getService());
						        		
						        		//clusterNb++;
						        		
						        		clusterNbNew = clusterNb + newclustersize+1;  
						   	    		//System.out.println("---clusterNb"+ clusterNb);
						   	    		
						        		NewClustersSet[newclustersize] = new Cluster();
						   	    		/*NewClustersSet[newclustersize].setID(clusterNb);
						   	    		String name = "Cluster_" + clusterNb;*/
						        		NewClustersSet[newclustersize].setID(clusterNbNew);
						   	    		String name = "Cluster_" + clusterNbNew;
						   	    		NewClustersSet[newclustersize].setName(name);
						   	    		NewClustersSet[newclustersize].setCentroidContext(oContext);
						   	    		NewClustersSet[newclustersize].setCentroidIntention(Observations[i].getIntention());
						   	    		NewClustersSet[newclustersize].setCentroidService(oService);
						   	    		NewClustersSet[newclustersize].setCentroidAverage(0);
						   	    		NewClustersSet[newclustersize].setObservations(obs);
						   	    		
						   	    		newclustersize++;
								}
							} // //ELSE FINALE

					}// end else
					long end = System.currentTimeMillis();
					long time = end - start;
					//System.out.println("*************Response Time : "+ time);
					FinalTime = FinalTime + time;

					 
				 }	 // end For Observations

					result.setNewClustersSet(NewClustersSet);
					result.setUpdatedClustersSet(UpdatedClustersSet);
					
					System.out.println("*************Final Time : "+ FinalTime/Observations.length);
					
		
			 } // End Try
			 catch(Exception e){
					e.printStackTrace();
					System.err.println(e.getLocalizedMessage());
			}
		}
////////////////////////////////////////////////////////////////////////////////////////////	
	
	@Override
	public  void getClusters(Observation Observation, Cluster Clusters[]) {
		
			 ClusterRequest Ocr = new ClusterRequest();
		
			 try{
				 
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
				 Sqlite maBase = new Sqlite();
		    	 maBase.connect();

					///
					Ocr = parse(Observation.getIntention());	
					String oVerb = Ocr.getVerb();
					String oTarget = Ocr.getTarget();
					
					String oContext = Observation.getContext();
					String oService = Observation.getService();
				
					ClusterRequest Ccr = new ClusterRequest();

					int size = -1;
					
					if (Clusters[0] != null)
						size = Clusters.length;

					if (size == -1){	
			    		String sql = "INSERT INTO Clusters (Name, CentroidC, CentroidI, CentroidS) VALUES ('Cluster_1', '"+ oContext +"', '"+ Observation.getIntention() + "', '"+ oService + "')";            
						maBase.executeMyUpdate(sql);
						sql = "UPDATE Trace SET ClusterID ="+ 1 +" WHERE ID="+ Observation.getID();
						maBase.executeMyUpdate(sql);						
					} 
					
					else{			
						
						int clusterNb = 0;
						cluster = null;
						clusteringScore = 0;
						
						for (int j=0; j<Clusters.length; j++){
								if (Clusters[j]==null)
									break;
								
								System.out.println("---- Cluster number: " + j);
								
								Ccr = parse (Clusters[j].getCentroidIntention());	
								// get Intention from cluster
									
								String cVerb = Ccr.getVerb();
								String cTarget = Ccr.getTarget();
								//String cSens = Ccr.getSensVerb();
								
								String cContext = Clusters[j].getCentroidContext();
								
								IntentionMatching intentionMatching  = new  IntentionMatching (oVerb , oTarget, cVerb, cTarget);
								double Iscore = 0;
								try {							
									Iscore = (intentionMatching.getScoreIntention(ont, kb, modelVerb, URIVerbOntology))/2;
								} catch (Exception e) {
									e.printStackTrace();
								}
								double Cscore = 0 ;
								if (Iscore>0){
							       
									InputStream streamObservation = new ByteArrayInputStream (oContext.getBytes());
									InputStream streamCluster = new ByteArrayInputStream (cContext.getBytes());
									IContextElement[] listClusterContextElts = Parser.parseXMLData (streamObservation);
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
								
								
								if (score > clusteringScore){
									clusteringScore = score;
									cluster = Clusters [j];
								}
							}
						 } 
							
					    
						if (clusteringScore >= clusteringThreshold){
							String sql = "UPDATE Trace SET ClusterID="+ cluster.getID() +" WHERE ID="+ Observation.getID();
							maBase.executeMyUpdate(sql);	
							
						} 

						else{ 
							String name = "Cluster_" + (Clusters.length+1);
							String sql = "INSERT INTO Clusters (Name, CentroidC, CentroidI, CentroidS) VALUES ('"+ name +"', '"+ Observation.getContext() +"', '"+ Observation.getIntention() + "', '"+ Observation.getService() + "')" ;        
							maBase.executeMyUpdate(sql);	
							sql = "UPDATE Trace SET ClusterID="+ (Clusters.length+1) +" WHERE ID = "+ Observation.getID();
							maBase.executeMyUpdate(sql);			
						}
					
		
			 } // End Try
			 catch(Exception e){
					e.printStackTrace();
					System.err.println(e.getLocalizedMessage());
			}
		}
	
	
	
	
	
	
	
	
	

///////////////////////////////////////////////////////////////////////////////////////////
    public boolean ExistInClusterSet (Cluster cluster,  Cluster ListClusters[]){
		
		  boolean exist = false;
		
		  // Get an iterator 
		
		
		  // Display elements
		  //System.out.println("-----Cluster Name: "+ cluster.getName());
		  for (int i=0; i<ListClusters.length; i++)  { 
			
			  Cluster new_cluster = ListClusters[i]; 
			  if (new_cluster != null && new_cluster.getName().equals(cluster.getName())){
				  exist = true;
				  break;
			  }	
		  } 
		return exist;
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
	public void ontologyiesChanged() {

		loadOntologies();
		loadModels();

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


	public ClusterRequest parse (String ToParse){
		  XMLParser s = new XMLParser();
	      StringBuffer str = new StringBuffer();
	      ClusterRequest cr = new ClusterRequest();
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


	public static String getURI(String elt)
    {
	final int sharpPosition = elt.indexOf('#');
	return elt.substring(0,sharpPosition);
    }

	


}
