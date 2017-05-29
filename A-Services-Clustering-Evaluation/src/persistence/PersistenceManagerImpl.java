package persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.EventListenerList;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import tools.PropertiesUtil;
import tools.Utils;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.util.FileManager;

/**
 * Class which implements the IPersistenceManager interface. Singleton class
 * 
 * @author Salma Najar
 * @see IPersistenceManager
 */
public class PersistenceManagerImpl implements IPersistenceManager {
	
	

/*	private static final class InstanceHolder {
		static String Instancefile="";
		static String InstanceURL= "";
		//static PersistenceManagerImpl INSTANCE = new PersistenceManagerImpl(Instancefile, InstanceURL);
	}*/

	private static int ONT = 1;
	private static int SERV = 2;
	private static int VERB = 3;
	private static int CTX = 4;

	private IPersistenceFacade strategy;

	public static PropertiesUtil propertiesUtil = new PropertiesUtil(null,null);

	private Model ont;
	private Model serv;
	private Model verb;
	private Model ctx;
	private InfModel infOnt;
	private InfModel infServ;
	private InfModel infVerbt;
	private InfModel infSctx;

	/** List of listeners. */
	private EventListenerList listeners = new EventListenerList();

	private static String sep = Utils.separator;

	public static PersistenceManagerImpl getInstance(String Instancefile, String InstanceURL) {
		//System.out.println("------PersistenceManagerImpl-Start");
		PersistenceManagerImpl INSTANCE = new PersistenceManagerImpl(Instancefile, InstanceURL);
		//System.out.println("------PersistenceManagerImpl-End");
		return INSTANCE ;
	}

	/**
	 * private constructor
	 */
	private PersistenceManagerImpl(String file, String URL) {
		//System.out.println("------PersistenceManagerImpl-Start");
		try {
			loadPersistence(file, URL);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("------PersistenceManagerImpl-Middle-getService-getOntologies-Start");
		try {
			PropertiesUtil p = new PropertiesUtil(file, URL);
			BasicFacade.prop = p.getProperties();
		
			// load the ontologies
			ont = strategy.getOntologies();
			//load verb
			//verb = strategy.getVerbOntology();
			//load context
			//ctx = strategy.getContextModel();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("------PersistenceManagerImpl-Middle-getService-getOntologies-End");

		// load the InfModels
		loadInfModels();
		// notify the observers
		notifyObservers(ONT);
		//notifyObservers(VERB);
		//notifyObservers(CTX);
		//System.out.println("------PersistenceManagerImpl-End");
	}

	private List<String> addFiles(URL filesURL, InfModel infModel, Model model, final String filterString) throws IOException {

		List<String> addedServices = new ArrayList<String>();

		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith("." + filterString);
			}
		};

		InputStream in = null;
		File file = new File(filesURL.getFile());

		// check if remote or local file/directory
		if (filesURL.getProtocol().equals("http")) {
			// REMOTE CASE

			in = filesURL.openStream();

			infModel.read(in, "");
			// if services is valid (reasoner check) then add it also to the persistence
			if (infModel.validate().isValid()) {
				if (filterString.equals("owl")) {
					
					addedServices.add(strategy.addOntology(createTempFilefromURL(filesURL)));
				} 
				model.read(in, "");
			}
		} else {
			// LOCAL CASE
			// check if it's local file or a local directory
			if (file.isFile()) {
				// just read the file (and hope it is a valid!!!)
				in = FileManager.get().open(file.toString());

				infModel.read(in, "");

				// if services is valid (reasoner check) then add it also to the persistence
				if (infModel.validate().isValid()) {
					if (filterString.equals("owl")) {
						addedServices.add(strategy.addOntology(file));
					} 
					model.read(in, "");
				}

			} else {
				String[] files = file.list(filter);
				if (files == null) {
					System.exit(0);
				}
				for (int i = 0; i < files.length; i++) {
					in = FileManager.get().open(file.toString() + sep + files[i]);

					infModel.read(in, "");

					// if services is valid (reasoner check) then add it also to the persistence
					if (infModel.validate().isValid()) {
						if (filterString.equals("owl")) {
							addedServices.add(strategy.addOntology(new File(file.toString() + sep + files[i])));
						} 
						model.read(in, "");
					}
					in.close();

				}
				in.close();
			}
		}
		return addedServices;
	}

	/**
	 * @see persistence.IPersistenceManager#addModelListener(persistence.ModelListener)
	 */
	@Override
	public void addModelListener(ModelListener listener) {
		listeners.add(ModelListener.class, listener);
	}

	@Override
	public List<String> addOntologies(URL ontologyFile) throws FileNotFoundException, IOException {

		List<String> addedServices = addFiles(ontologyFile, infOnt, ont, "owl");
		if (!addedServices.isEmpty()) {
			notifyObservers(ONT);
		}
		return addedServices;
	}


	private File createTempFilefromURL(URL url) throws IOException {

		File temp = File.createTempFile(url.getFile(), null);

		// Delete temp file when program exits.
		temp.deleteOnExit();

		// Copy resource to local file, use remote file
		// if no local file name specified
		InputStream is = url.openStream();
		FileOutputStream fos = null;

		fos = new FileOutputStream(temp);

		int oneChar, count = 0;
		while ((oneChar = is.read()) != -1) {
			fos.write(oneChar);
			count++;
		}
		is.close();
		fos.close();

		return temp;
	}

	/**
	 * @see persistence.IPersistenceManager#getDomainOntologies()
	 */
	@Override
	public Model getDomainOntologies() {
		return ont;
	}

	/**
	 * @see persistence.IPersistenceManager#getInfOntologies()
	 */
	@Override
	public InfModel getInfOntologies() {
		return infOnt;
	}


	private void loadInfModels() {

		Reasoner reasoner = PelletReasonerFactory.theInstance().create();

		// create the two inferenced models

		infOnt = ModelFactory.createInfModel(reasoner, ont);
		//System.out.println("loadInfModels");
	}

	/**
	 * @throws ClassNotFoundException
	 * @see persistence.IPersistenceManager#loadDefaultPersistence()
	 */
	@Override
	public void loadPersistence(String file, String URL) throws ClassNotFoundException {
		//System.out.println("------loadPersistence-Start");
		PropertiesUtil prop = new PropertiesUtil(file, URL);
		String name = prop.getProperties().getProperty("persistence");
		//System.out.println("PersistenceMangerImpl-name:"+ name);
		Class theClass = Class.forName(name);
		// get the constructor with one parameter
		// create an instance
		try {
			this.strategy = (IPersistenceFacade) theClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		// set the propertiesUtil
		this.strategy.setPropertiesUtil(prop);
		//System.out.println("------loadPersistence-End");

	}

	/**
	 * Notifies all that have registered interest for notification..
	 * 
	 * @param int the model which changed
	 * @see EventListenerList
	 */
	public synchronized void notifyObservers(int model) {
		if (model == ONT) {
			for (ModelListener l : listeners.getListeners(ModelListener.class)) {
				l.ontologyiesChanged();
			}
		} 
	}

	private List<String> removeFiles(URL filesURL, InfModel infModel, Model model, final String filterString) throws IOException {

		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith("." + filterString);
			}
		};

		List<String> removedServices = new ArrayList<String>();

		InputStream in = null;
		File file = new File(filesURL.getFile());

		// check if remote or local file/directory
		if (filesURL.getProtocol().equals("http")) {
			// REMOTE CASE

			in = filesURL.openStream();

			Model tempModel = ModelFactory.createDefaultModel();
			tempModel.read(in, "");

			infModel.remove(tempModel);

			// if services is valid (reasoner check) then add it also to the persistence
			if (infModel.validate().isValid()) {
				if (filterString.equals("owl")) {
					removedServices.add(strategy.removeOntology(createTempFilefromURL(filesURL)));
				} 
				model.remove(tempModel);
			}
		} else {
			// LOCAL CASE
			// check if it's local file or a local directory
			if (file.isFile()) {
				// just read the file (and hope it is a valid!!!)
				in = FileManager.get().open(file.toString());

				Model tempModel = ModelFactory.createDefaultModel();
				tempModel.read(in, "");

				infModel.remove(tempModel);

				// if services is valid (reasoner check) then remove it
				if (infModel.validate().isValid()) {
					if (filterString.equals("owl")) {
						removedServices.add(strategy.removeOntology(file));
					} 
					model.remove(tempModel);
				}

			} else {
				String[] files = file.list(filter);
				if (files == null) {
				}

				Model tempModel = ModelFactory.createDefaultModel();

				for (int i = 0; i < files.length; i++) {
					in = FileManager.get().open(file.toString() + sep + files[i]);

					tempModel.removeAll();
					tempModel.read(in, "");

					infModel.remove(tempModel);

					// if services is valid (reasoner check) then remove it.
					if (infModel.validate().isValid()) {
						if (filterString.equals("owl")) {
							removedServices.add(strategy.removeOntology(new File(file.toString() + sep + files[i])));
						} 
						model.remove(tempModel);
					}
					in.close();

				}
				in.close();
			}

		}
		return removedServices;
	}

	/**
	 * @see persistence.IPersistenceManager#removeModelListener(persistence.ModelListener)
	 */
	@Override
	public void removeModelListener(ModelListener listener) {
		listeners.remove(ModelListener.class, listener);
	}

	/**
	 * @see persistence.IPersistenceManager#removeOntology(java.net.URL)
	 */
	@Override
	public List<String> removeOntology(URL ontologyFile) throws FileNotFoundException, IOException {

		List<String> removedOntologies = removeFiles(ontologyFile, infOnt, ont, "owl");
		if (!removedOntologies.isEmpty()) {
			notifyObservers(ONT);
		}
		return removedOntologies;
	}

	@Override
	public void saveOntologies() {

	}


	@Override
	public List<String> addverb(URL ontologyFile) throws FileNotFoundException,
			IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> addctx(URL ontologyFile) throws FileNotFoundException,
			IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InfModel getInfVerb() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InfModel getInfContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model getVerb() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model getContext() {
		// TODO Auto-generated method stub
		return null;
	}

}
