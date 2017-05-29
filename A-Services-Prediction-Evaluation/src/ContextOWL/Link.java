package ContextOWL;
/**
 * 
 * @author salma
 *
 */

public class Link {
	int distance;
	boolean find;
	
	Link(int d, boolean f){
		this.distance = d;
		this.find= f;
		
	}
	
	public boolean getFind(){
		return this.find;
	}
	
	public int getdistance(){
		return this.distance;
	}
	
	public void setDistance(int d){
		this.distance = d;		
	}
	
	public void setFind(boolean f){
		this.find = f;		
	}
}
