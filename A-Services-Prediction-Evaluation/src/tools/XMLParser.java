package tools;

import java.io.*;

import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * A SAX XML Parser which takes a XML file and a SearchRequest, parses the xml into the
 * SearchRequest.
 * 
 * 
 * @author Salma Najar
 * 
 */
public class XMLParser extends DefaultHandler {

	private String elementText;
	private static PredictionRequest pr;
	

		public XMLParser() {
		}

		@Override
		public void characters(char[] buf, int offset, int len) throws SAXException {
			elementText = new String(buf, offset, len);

		}

		// ---- SAX DefaultHandler methods ----

		@Override
		public void endDocument() throws SAXException {

		}

		@Override
		public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

			// the tag name
			String eName = ("".equals(localName)) ? qName : localName;

			// check the name and add the element

			if (eName.equals("intention")) {
				pr.setIntention(elementText);
			} else if (eName.equals("verb")) {
				pr.setVerb(elementText);
			}else if (eName.equals("sensVerb")) {
				pr.setSensVerb(elementText);
			}else if (eName.equals("target")) {
				pr.setTarget(elementText);
			} else if (eName.equals("context")) {
				pr.setContext(elementText);
			} 
		}

		/**
		 * Reads the XML file and adds the elements to the SearchRequest.
		 * 
		 * @param request
		 *            XMLFile
		 * @param sr
		 *            SearchRequest which will be filled with the parsed informations
		 * @throws SAXException
		 * @throws ParserConfigurationException
		 * @throws IOException
		 */
	public void parseRequest(String request, PredictionRequest cr) throws ParserConfigurationException, SAXException, IOException {
		XMLParser.pr = cr;

		// Use an instance of ourselves as the SAX event handler
		DefaultHandler handler = new XMLParser();

		
		// Parse the input with the default (non-validating) parser
		SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
		
		saxParser.parse(new InputSource(new ByteArrayInputStream(request.getBytes("utf-8"))), handler);
	

	}
	
	public void parseRequest(File request, PredictionRequest sr) throws ParserConfigurationException, SAXException, IOException {

		XMLParser.pr = sr;

		// Use an instance of ourselves as the SAX event handler
		DefaultHandler handler = new XMLParser();

		// Parse the input with the default (non-validating) parser
		SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
		saxParser.parse(request, handler);

	}

	@Override
	public void startDocument() throws SAXException {

	}

	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes attrs) throws SAXException {
	}
}
