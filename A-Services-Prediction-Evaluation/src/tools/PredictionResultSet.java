package tools;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * The PredictionResultSet class saves Service URIs + the matching score in a HashMap
 * You can add a new element and get the Iterator to access the elements.
 * 
 * @author Salma Najar
 * 
 */
public class PredictionResultSet {

	
	/**
	 * The Map, containing the URI and score of a service
	 */
	private Map<Integer, Double> tm = new HashMap<Integer, Double>();

	/**
	 * Default constructor
	 */
	public PredictionResultSet() {

	}

	/**
	 * Add a new service + matching score
	 * 
	 * @param uri URI of the service
	 * @param value Score (double) of the service for a search request
	 */
	public void addElement(Integer uri, Double value) {
		tm.put(uri, value);
	}

	
	/**
	 * Returns the Iterator of the MatchResultSet. The Service and score
	 * can then be accessed via iterator.next()
	 * 
	 * @return Iterator<?> The iterator of the Map
	 */
	public Iterator<?> getIterator() {

		ArrayList<?> as = new ArrayList<Object>(tm.entrySet());
		Collections.sort(as, new Comparator<Object>() {

			@Override
			public int compare(Object o1, Object o2) {
				Map.Entry e1 = (Map.Entry) o1;
				Map.Entry e2 = (Map.Entry) o2;
				Double dd1 = (Double) (e1.getValue());
				Double dd2 = (Double) (e2.getValue());

				if (dd1 > dd2) {
					return -1;
				} else if (dd1 < dd2) {
					return 1;
				} else {
					return e1.toString().compareTo(o2.toString());
				}
			}
		});

		Iterator<?> i = as.iterator();
		return i;
	}

	
	
}
