package classificationEngine;

import java.net.URI;
import java.util.List;


/**
 * This Interface provides the classification methods.
 * 
 * @author Salma Najar
 */
public interface IClassificationEngine {


	public void doClassification (int LastCount, int New_0bservations_Count);
	
	public void loadDefaultClassifier(String file, String URL);


}
