package persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import tools.PropertiesUtil;

import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

/**
 * A basic persistence class which uses the normal file system to access the service and ontology
 * files. Uses the jena framework.
 * 
 * @author Salma Najar
 *  
 */
public class BasicFacade implements IPersistenceFacade {

	public static Properties prop;

	/**
	 * Default constructor
	 */
	public BasicFacade() {
	}

	
	/**
	 * @see persistence.IPersistenceFacade#addOntology(java.io.File)
	 */
	@Override
	public String addOntology(File ontology) throws IOException {

		BufferedReader reader = new BufferedReader(new FileReader(ontology));
		File copyFile = new File(prop.getProperty("ontologies") + "/" + ontology.getName());
		BufferedWriter writer = new BufferedWriter(new FileWriter(copyFile));

		// ... Loop as long as there are input lines.
		String line = null;
		while ((line = reader.readLine()) != null) {
			writer.write(line);
			writer.newLine(); // Write system dependent end of line.
		}

		// ... Close reader and writer.
		reader.close(); // Close to unlock.
		writer.close(); // Close to unlock and flush to disk.

		return copyFile.getCanonicalPath();
	}


	/**
	 * 
	 * Load a jena model but without a reasoner
	 * 
	 * @throws IOException
	 * 
	 * @see persistence.IPersistenceFacade#getDomainOntologies()
	 */
	@Override
	public Model getOntologies() throws IOException {
		//System.out.println("----getOntologies-Start");
		
		File OntoFile = new File(prop.getProperty("ontologies"));
		
		URL ontologies = new URL("file://" + OntoFile.getAbsolutePath());
		//System.out.println("******Onto:"+ ontologies.toString());
		Model model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".owl");
			}
		};

		InputStream in = null;
		File file = new File(ontologies.getFile());

		// check if remote or local file/directory
		if (ontologies.getProtocol().equals("http")) {
			// REMOTE CASE

			in = ontologies.openStream();

			model.read(in, "");

		} else {
			// LOCAL CASE
			// check if it's local file or a local directory
			if (file.isFile()) {
				// just read the file (and hope it is a valid!!!)
				in = FileManager.get().open(file.toString());

				model.read(in, "");

			} else {
				String[] files = file.list(filter);
				if (files == null) {

					System.exit(0);
				}
				for (int i = 0; i < files.length; i++) {
					in = FileManager.get().open(file + "/" + files[i]);
					//System.out.println("File: "+ file + "/" + files[i]);
					model.read(in, "");
					in.close();

				}
				in.close();
			}
		}
		//System.out.println("----getOntologies-End");
		return model;
	}

	@Override
	public void setPropertiesUtil(PropertiesUtil util) {
		//System.out.println("----setPropertiesUtil-Start");
		prop = util.getProperties();
		//System.out.println("----setPropertiesUtil-Start");
		
	}	

	@Override
	public Model getContextModel() throws IOException {
		URL context = new URL("file://" + prop.getProperty("context"));
		Model model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".owl");
			}
		};

		InputStream in = null;
		File file = new File(context.getFile());

		// check if remote or local file/directory
		if (context.getProtocol().equals("http")) {
			// REMOTE CASE

			in = context.openStream();

			model.read(in, "");

		} else {
			// LOCAL CASE
			// check if it's local file or a local directory
			if (file.isFile()) {
				// just read the file (and hope it is a valid!!!)
				in = FileManager.get().open(file.toString());

				model.read(in, "");

			} else {
				String[] files = file.list(filter);
				if (files == null) {

					System.exit(0);
				}
				for (int i = 0; i < files.length; i++) {
					in = FileManager.get().open(file + "/" + files[i]);
					//System.out.println("File: "+ file + "/" + files[i]);
					model.read(in, "");
					in.close();

				}
				in.close();
			}
		}
		return model;
	}

	@Override
	public Model getVerbOntology() throws IOException {
		URL verb = new URL("file://" + prop.getProperty("verb"));
		Model model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".owl");
			}
		};

		InputStream in = null;
		File file = new File(verb.getFile());

		// check if remote or local file/directory
		if (verb.getProtocol().equals("http")) {
			// REMOTE CASE

			in = verb.openStream();

			model.read(in, "");

		} else {
			// LOCAL CASE
			// check if it's local file or a local directory
			if (file.isFile()) {
				// just read the file (and hope it is a valid!!!)
				in = FileManager.get().open(file.toString());

				model.read(in, "");

			} else {
				String[] files = file.list(filter);
				if (files == null) {

					System.exit(0);
				}
				for (int i = 0; i < files.length; i++) {
					in = FileManager.get().open(file + "/" + files[i]);
					//System.out.println("File: "+ file + "/" + files[i]);
					model.read(in, "");
					in.close();

				}
				in.close();
			}
		}
		return model;
	}
	
	@Override
	public String removeOntology(File ontology) throws IOException {

		File rmFile = new File(prop.getProperty("ontologies") + "/" + ontology.getName());
		rmFile.delete();

		return ontology.getName();

	}

}