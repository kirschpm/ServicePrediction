package persistence;

import java.util.EventListener;

/**
 * 
 * The listener interface for receiving ad events. 
 * 
 * @author Salma Najar
 *
 */
public interface ModelListener extends EventListener{

	public void ontologyiesChanged();
	
}
