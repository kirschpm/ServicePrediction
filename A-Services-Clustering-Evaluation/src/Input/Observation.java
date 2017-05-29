/**
 * 
 */
package Input;

import java.sql.Timestamp;

/**
 * @author salma
 *
 */
public class Observation {

	/**
	 * 
	 */
	
    private  int ID;
    private int User;
    private Timestamp TimeStamp;
    private String Context;
    private String Intention;
    private String Service;
    private int Cluster;
	
	// Constructor
	public Observation() {
		/*this.ID = 0;
		this.User = 0;
		this.TimeStamp = null;
		this.Context = null;
		this.Intention = null;
		this.Service = null;
		this.Cluster = 0;*/
	}
   
	// Set methods
	
	public  void setID (int id){
		this.ID = id;
	}
	
	
	public void setUser (int user){
		this.User = user;
	}
	
	
	public void setTimeStamp (Timestamp time){
		this.TimeStamp = time;
	}
	
	public void setContext (String ctx){
		this.Context = ctx;
	}
	
	public void setIntention (String Int){
		this.Intention = Int;
	}
	
	public void setService(String sce){
		this.Service = sce;
	}
	
	public void setCluster(int clust){
		this.Cluster = clust;
	}
	
	
	// Get Methods
	
	public int getID (){
		return this.ID;
	}
	
	
	public int getUser (){
		return this.User;
	}
	
	
	public Timestamp getTimeStamp (){
		return this.TimeStamp;
	}
	
	public String getContext (){
		return this.Context;
	}
	
	public String getIntention (){
		return this.Intention;
	}
	
	public String getService(){
		return this.Service;
	}
	
	public int getCluster(){
		return this.Cluster;
	}
}
