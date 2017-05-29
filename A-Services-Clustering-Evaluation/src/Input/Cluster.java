/**
 * 
 */
package Input;

import java.util.List;

/**
 * @author salma
 *
 */
public class Cluster {

	/**
	 * 
	 */
	
	private int ID;
	private String Name;
	private String Centroid_Intention;
	private String Centroid_Context;
	private String Centroid_Service;
	private float Centroid_Average;
//	public static List <Observation> observation;
	private Observation observation [];

	
	public Cluster() {
		// TODO Auto-generated constructor stub
		this.ID = 0;
		this.Name = null;
		this.Centroid_Intention = null;
		this.Centroid_Context = null;
		this.Centroid_Service = null;
		this.observation = null;
		this.Centroid_Average = 0;

	}
	
	// set methods
	
	public void setID (int id){
		this.ID = id;
	}
	
	public void setName (String name){
		this.Name = name;
	}
	
	public void setCentroidIntention(String cent_Int){
		this.Centroid_Intention = cent_Int;
	}
	
	public void setCentroidContext(String cent_Ctx){
		this.Centroid_Context = cent_Ctx;
	}	
	
	public void setCentroidService(String cent_Sve){
		this.Centroid_Service = cent_Sve;
	}	
	
	public void setCentroidAverage(float cent_Avg){
		this.Centroid_Average = cent_Avg;
	}
/*	public void setObservations (List <Observation> observ){
		this.observation = observ;
	}*/
	
	public void setObservations (Observation observ[]){
		this.observation = observ;
	}
	

	
	//get methods
	
	public int getID (){
		return this.ID;
	}
	
	public String getName (){
		return this.Name ;
	}
	
	public String getCentroidIntention(){
		return this.Centroid_Intention;
	}
	
	public String getCentroidContext(){
		return this.Centroid_Context;
	}	

	public String getCentroidService(){
		return this.Centroid_Service;
	}	
	public float getCentroidAverage(){
		return this.Centroid_Average;
	}
	
/*	public List <Observation> getObservations(){
		return this.observation;
	}*/
	
	public Observation[] getObservations(){
		return this.observation;
	}
	
}
