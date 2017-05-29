package Algorithm;

import org.gavaghan.geodesy.*;

public class DistanceBetweenValues {

	
	public static float PositionDistance(float lat1, float lng1, float lat2, float lng2) {
	    double earthRadius = 3958.75;
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;

	    int meterConversion = 1609;

	    return new Float(dist * meterConversion).floatValue()/1000;
	  }
	

	
	public static float EuclideanDistanceOneDimension(float pt1, float pt2){
		return Math.abs(pt1-pt2);
	}
	
	public static float EuclideanDistanceThreeDimension(float ptX1, float ptY1, float ptZ1, float ptX2, float ptY2, float ptZ2){
		
		float x = (ptX1 - ptX2) * (ptX1 - ptX2);
		float y = (ptY1 - ptY2) * (ptY1 - ptY2);		
		float z = (ptZ1 - ptZ2) * (ptZ1 - ptZ2);
		return (float) Math.sqrt(x+y+z);
		
	}
	
	public static boolean CompareExactString(String ch1, String ch2){
		return ch1.equals(ch2);
	}
	
	public static boolean Like(String ch1, String ch2){
		if (ch1.matches(".*"+ch2+".*"))
				return true;
		else if (ch2.matches(".*"+ch1+".*"))
			return true;
		else
			return false;
	}
	
	public static void main (String args []){
		
		System.out.println(PositionDistance(Float.parseFloat("48.8671679"), Float.parseFloat("2.2894155000000183"), Float.parseFloat("48.8686863"), Float.parseFloat("2.287344200000007")));
		System.out.println(PositionDistance(Float.parseFloat("49.2444843352286"), Float.parseFloat("4.0221457393499"), Float.parseFloat("48.799627"), Float.parseFloat("2.340088")));
		System.out.println(Like("android 5.0", "android 4.0"));
	}
}
