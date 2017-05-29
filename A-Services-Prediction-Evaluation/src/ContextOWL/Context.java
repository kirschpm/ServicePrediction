package ContextOWL;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * 
 * @author salma
 *
 */

public class Context {

	public static void main(String [] args) throws Exception{		
		URL url = new URL("http://www.citypassenger.com/services/Context.xml");
		InputStream stream = url.openStream();
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(stream);
		
		//Get the root element
		NodeList node =  doc.getElementsByTagName("ctx:condition");
		
		Element ctx= (Element) node.item(0);
		System.out.println("Node:"+ctx.getAttribute("resource"));
		
		//getElements(stream);
	}
	

	public static Node getCondition(InputStream stream) throws Exception{
		Node condition = null;
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(stream);
			
			//Get the root element
			Node node = doc.getFirstChild();
			System.out.println("Node:"+node.getNodeName());
			//Get the node service and profile
	
			NodeList list =node.getChildNodes();
			for (int i=0; i<list.getLength(); i++){
				if (list.item(i).getNodeName().equals("ctx:condition")){
					condition = list.item(i);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("-----Error: "+ e);
		}	
	return condition;
	}
	
	public static List <Node> getcontextElements(InputStream stream) throws Exception{		
		Node condition = getCondition(stream);
		List <Node> elements = null;
		try
		{
			NodeList conditionChildNodes = condition.getChildNodes();
			for (int i=0; i<conditionChildNodes.getLength(); i++){
				//System.out.println(list.item(i));
				if (conditionChildNodes.item(i).getNodeName().equals("ctx:contextElement")){
					System.out.println("Node:"+conditionChildNodes.item(i));
					elements.add(conditionChildNodes.item(i));
				}
					
			}
		}
		catch(Exception e)
		{
			System.out.println("-----Error: "+ e);
		}	
	return elements;	
	}
	
	public static String getEntity(List <Node> elements){
		String entity = null;
		try
		{
			for (int i=0; i<elements.size(); i++){
				//System.out.println(list.item(i));
				if (elements.get(i).getNodeName().equals("ctx:hasEntity"))
					entity = elements.get(i).getNodeValue();
			}
		}
		catch(Exception e)
		{
			System.out.println("-----Error: "+ e);
		}	
	return entity;	
	}
	
	
	public static Node getState(String filename){
		Node condition = null;
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filename);
			
			//Get the root element
			Node node = doc.getFirstChild();
			//Get the node service and profile
	
			NodeList list =node.getChildNodes();
			for (int i=0; i<list.getLength(); i++){
				if (list.item(i).getNodeName().equals("ctx:state")){
					condition = list.item(i);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("-----Error: "+ e);
		}	
	return condition;
	}
	
	
	
	public static void getElements(InputStream stream) throws Exception{
		List <Node> elements = null;
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(stream);
			//////////CONDITIONS////////////////
			//Get the root condition
			NodeList conditions = doc.getElementsByTagName("ctx:condition");			
			int totalconditions = conditions.getLength();
            System.out.println("Total of condition : " + totalconditions);          
			for (int i=0; i< totalconditions; i++){
				Node condition = conditions.item(i);
				if (condition.getNodeType() == Node.ELEMENT_NODE){
					Element cond  = (Element) condition ;
					
					//Get the context element
					NodeList contextElements = cond.getElementsByTagName("ctx:contextElement");
					int totalcontextElements = contextElements.getLength();
					for (int j=0; j< totalcontextElements; j++){
						//Context Element
						Node ctxElt = contextElements.item (j); 
						System.out.println("Element: " + ctxElt.getNodeName());
						if (ctxElt.getNodeType() == Node.ELEMENT_NODE){
							Element elt  = (Element) ctxElt ;
							NodeList c = elt.getChildNodes();
							int t = c.getLength();
							System.out.println("int: "+ t);
							for (int k =0; k<t ; k++){
								if (c.item(k).getFirstChild() != null && ! c.item(k).getNodeName().equals("#text")){
									System.out.println("k.node: "+ c.item(k).getNodeName());
									if (c.item(k).getNodeName().equals("ctx:hasEntity"))
										getEntity(c.item(k));
									else if (c.item(k).getNodeName().equals("ctx:hasScope"))
										getScope(c.item(k));
									else if (c.item(k).getNodeName().equals("ctx:hasRepresentation"))
										getRepresentation(c.item(k));
									else if (c.item(k).getNodeName().equals("ctx:contextValueSet"))
										getValues(c.item(k));
								}
							}
									
						}
					}
						
				}
				break;
			}
			
		}
		catch(Exception e)
		{
			System.out.println("-----Error: "+ e);
		}	
	}
	
	public static void getEntity(Node node){
		if (node.getNodeName().equals("ctx:hasEntity"))
			System.out.println("value Entity: "+ node.getFirstChild().getNodeValue());
	}
	
	public static void getScope(Node node){
		if (node.getNodeName().equals("ctx:hasScope"))
			System.out.println("value Scope: "+ node.getFirstChild().getNodeValue());
	}
	public static void getRepresentation(Node node){
		if (node.getNodeName().equals("ctx:hasRepresentation"))
			System.out.println("value Representation: "+ node.getFirstChild().getNodeValue());
	}
	
	public static void getValue(Node node){
		if (node.getNodeName().equals("ctx:value"))
			System.out.println("value: "+ node.getFirstChild().getNodeValue());
	}
	public static void getValues(Node node){
		if (node.getNodeType() == Node.ELEMENT_NODE){
			Element valueSet  = (Element) node ;
			
			//Get the context element
			NodeList values = valueSet.getElementsByTagName("ctx:contextValue");
			int totalvalue = values.getLength();
			for (int j=0; j<totalvalue; j++){
				//Context Element
				Node value = values.item (j); 
				System.out.println("Value: " + value.getNodeName());
				if (value.getNodeType() == Node.ELEMENT_NODE){
					Element val  = (Element) value ;
					NodeList v= val.getChildNodes();
					int t = v.getLength();
					System.out.println("int: "+ t);
					for (int k =0; k<t ; k++){
						if (v.item(k).getFirstChild() != null && ! v.item(k).getNodeName().equals("#text")){
							System.out.println("k.node: "+ v.item(k).getNodeName());
							if (v.item(k).getNodeName().equals("ctx:hasScope"))
								getScope(v.item(k));
							else if (v.item(k).getNodeName().equals("ctx:hasRepresentation"))
								getRepresentation(v.item(k));
							else if (v.item(k).getNodeName().equals("ctx:value"))
								getValue(v.item(k));
						}
					}
							
				}
			}
				
		}
	}
	
	
	
}
