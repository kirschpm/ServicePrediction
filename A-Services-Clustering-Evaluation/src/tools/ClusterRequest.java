package tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * The class SearchRequest contains all informations for finding a service. For each field there are
 * getter and setters.
 * 
 * 
 * @author Salma Najar
 * 
 */
public class ClusterRequest {

	private String context;
	private String intention;
	private String verb;
	private String sensVerb;
	private String target;




	/**
	 * 
	 * @return List<String>
	 */
	public String getContext() {
		return context;
	}


	/**
	 * @return String
	 */
	public String getIntention() {
		return intention;
	}

	/**
	 * @return String
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @return String
	 */
	public String getVerb() {
		return verb;
	}
	

	/**
	 * @return String
	 */
	public String getSensVerb() {
		return sensVerb;
	}

	/**
	 * 
	 * @param context
	 */
	public void setContext(String context) {
		this.context = context;
	}

	/**
	
	
	/**
	 * @param intention
	 */
	public void setIntention(String intention) {
		this.intention = intention;
	}


	/**
	 * @param target
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * @param verb
	 */
	public void setVerb(String verb) {
		this.verb = verb;
	}
	
	public void setSensVerb(String sensVerb) {
		this.sensVerb = sensVerb;
	}

	@Override
	public String toString() {

		StringBuffer resultString = new StringBuffer();

		resultString.append("SearchRequest: ");
		resultString.append("\n");
		resultString.append("Context:");
		resultString.append(context);
		resultString.append("\n");
		
		resultString.append("Verb: ");
		resultString.append(verb);
		resultString.append("\n");
		
		resultString.append("SensVerb: ");
		resultString.append(sensVerb);
		resultString.append("\n");

		resultString.append("target: ");
		resultString.append(target);		

		return resultString.toString();

	}


}
