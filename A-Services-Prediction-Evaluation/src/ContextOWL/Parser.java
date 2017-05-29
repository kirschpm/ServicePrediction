package ContextOWL;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.context.model.impl.ContextElement;
import org.context.model.impl.ContextValue;
import org.context.model.impl.ContextValueSet;
import org.context.model.impl.Entity;
import org.context.model.impl.Operator;
import org.context.model.impl.Representation;
import org.context.model.impl.Scope;
import org.context.model.impl.Value;
import org.context.model.impl.ValueElement;
import org.context.model.impl.ValueSet;
import org.context.model.api.IContextValue;
import org.context.model.api.IOperator;
import org.context.model.api.IValue;
import org.context.model.api.IContextElement;
import org.context.model.api.IValueElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * 
 * @author salma
 *
 */
public class Parser {
	
	private static final String CTX_TAG = "ctx:context";
	private static final String CTX_CONDITION_TAG = "ctx:condition";
	private static final String CTX_STATE_TAG = "ctx:state";
	private static final String CTX_ELT_TAG = "ctx:contextElement";
	private static final String CTX_ENTITY_TAG = "ctx:hasEntity";
	private static final String CTX_SCOPE_TAG = "ctx:hasScope";
	private static final String CTX_REPRESENTATION_TAG = "ctx:hasRepresentation";
	private static final String CTX_CONTEXTVALUESET_TAG = "ctx:contextValueSet";
	private static final String CTX_VALUESET_TAG = "ctx:valueSet";
	private static final String CTX_CONTEXTVALUE_TAG = "ctx:contextValue";
	private static final String CTX_VALUEELEMENT_TAG = "ctx:valueElement";
	private static final String CTX_VALUE_TAG = "ctx:value";
	private static final String CTX_OPERATOR_TAG = "ctx:operator";
	private static final String CTX_hasSCOPE_TAG = "ctx:hasScope";
	private static final String CTX_hasREPRESENTATION_TAG = "ctx:hasRepresentation";
	private static final String RESOURCE_TAG = "resource";

	public static void main(String [] args) throws Exception{		
		URL url = new URL("http://www.citypassenger.com/services/Context.xml");
		InputStream stream = url.openStream();
		//IContextElement[]  ctxElements = toContextElements(stream);
		//test(stream);
		
	}
	
    static public IContextElement[] toContextElements(final InputStream xml) throws Exception
    {
    		IContextElement[] elements = parseXMLData(xml);
    		if (elements == null) 
    			return null;
    		else 
    			return elements;
    }
    
    public static IContextElement[] parseXMLData(final InputStream xml) throws Exception
    {
    	Vector elements = new Vector();
    	
    	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(xml);
		
		Element context = (Element) doc.getFirstChild();
		if (context != null){
			NodeList conditions = context.getElementsByTagName(CTX_CONDITION_TAG);
			/*for (int i=0; i< conditions.getLength(); i++){
			}*/
			if (conditions != null){
				Element condition = (Element) conditions.item(0);
				NodeList ctxEls = condition.getElementsByTagName(CTX_ELT_TAG);
				if (ctxEls != null){
					for (int i=0; i<ctxEls.getLength(); i++){
						Element ctxEl = (Element) ctxEls.item(i);
						if (ctxEl !=null){
							
							// get Entity
							Element ent = (Element) ctxEl.getElementsByTagName(CTX_ENTITY_TAG).item(0);
							String entType = ent.getAttribute(RESOURCE_TAG);
							Entity entity = Entity.createEntity(entType); 
							//System.out.println(entType);
							
							//get scope
							Element scp = (Element) ctxEl.getElementsByTagName(CTX_SCOPE_TAG).item(0);
							String scpType = scp.getAttribute(RESOURCE_TAG);
							Scope scope = Scope.createScope(scpType); 
							//System.out.println(scpType);
							
							//get representation
							Element rep = (Element) ctxEl.getElementsByTagName(CTX_REPRESENTATION_TAG).item(0);
							String repType = rep.getAttribute(RESOURCE_TAG);
							Representation representation = Representation.createRepresentation(repType); 
							//System.out.println(repType);
							
							//get ContextValueSet
							Element ctxValSet = (Element) ctxEl.getElementsByTagName(CTX_CONTEXTVALUESET_TAG).item(0);
							if (ctxValSet != null){
								HashMap ctxValuesMap = new HashMap();
								NodeList ctxValues = ctxValSet.getElementsByTagName(CTX_CONTEXTVALUE_TAG);
								ContextValueSet ctxValueSet = null;
								if (ctxValues != null){								
									for (int j=0; j<ctxValues.getLength(); j++){
										Element ctxVal = (Element) ctxValues.item(j);
										if (ctxVal != null){
											
											//get scope
											Element scpVal = (Element) ctxVal.getElementsByTagName(CTX_hasSCOPE_TAG).item(0);
											String scpValType = scpVal.getAttribute(RESOURCE_TAG);
											Scope scopeVal = Scope.createScope(scpValType); 
											//System.out.println(scpValType);
											
											
											//get representation
											Element repVal = (Element) ctxVal.getElementsByTagName(CTX_hasREPRESENTATION_TAG).item(0);
											String repValType = repVal.getAttribute(RESOURCE_TAG);
											Representation representationVal = Representation.createRepresentation(repValType); 
											//System.out.println(repValType);
											
											//get valueSet
											
											Vector values = new Vector();
											Element ValSet = (Element) ctxVal.getElementsByTagName(CTX_VALUESET_TAG).item(0);
											ValueSet valueSet = null;
																		if (ValSet != null){
																			List valuesList = new ArrayList();
																			NodeList valueElements = ValSet.getElementsByTagName(CTX_VALUEELEMENT_TAG);
																			
																			if (valueElements != null){								
																				for (int k=0; k<valueElements.getLength(); k++){
																					Element value = (Element) valueElements.item(k);
																					if (value != null){
																						
																						//get value
																						Node val =  value.getElementsByTagName(CTX_VALUE_TAG).item(0);
																						Object valType = val.getFirstChild().getNodeValue();
																						IValue v = Value.createValue(valType);
																						
																						//get Operator										
																						Element Op =  (Element) value.getElementsByTagName(CTX_OPERATOR_TAG).item(0);
																						String opType = Op.getAttribute(RESOURCE_TAG);																		
																						IOperator operator = Operator.createOperator(opType);
																						
																						IValueElement valueElement = ValueElement.createValueElement(v, operator);
																						valuesList.add(valueElement);
																					}
																				}
																				valueSet = ValueSet.createValueSet(valuesList);
																			}
																		}
	
											IContextValue ctxValue = ContextValue.createValueElement(scopeVal, representationVal, valueSet);
											ctxValuesMap.put(scopeVal, ctxValue);
											
										}
									}
									ctxValueSet = ContextValueSet.createContextValueSet(ctxValuesMap);
								
								}
								ContextElement contextElement = (ContextElement) ContextElement.createContextElement(entity, scope, representation, ctxValueSet);
								elements.add(contextElement);
							} //ValueSet
							
							
						}
					}
				}// ContextElements						
			}
		}
		if (elements.size() == 0) 
			return null;
		else 
			return (IContextElement[])elements.toArray(new IContextElement[0]);
    	
    }
    
/*    public static void test(final InputStream xml) throws Exception{
    	IContextElement[] list = parseXMLData(xml);
    	for (int i=0; i<list.length; i++){
    		IContextElement ctxElt = list[i];
    		IEntity entity = ctxElt.getEntity();
    		IScope scope = ctxElt.getScope();
    		IRepresentation representation = ctxElt.getRepresentation();
    		//ContextValue
    		IContextValueSet ctxHashMap = list[i].getContext();
    		Set<IScope> keys = (Set<IScope>) ctxHashMap.keys();
    		Iterator <IScope> it = keys.iterator();
    		System.out.println("ContextElement-"+i+"-");
    		System.out.println("----Entity: "+ entity.getEntityAsString());
    		System.out.println("----Scope: "+ scope.getScopeAsString());
    		System.out.println("----Representation: "+ representation.getRepresentationAsString());
    		System.out.println("----ContextValueSet");
    		while (it.hasNext()){
    			System.out.println("****ContextValue");
    			IScope valueScope = it.next();
    			IContextValue ctxValue = ctxHashMap.getContextValue(valueScope);
    			IValue value = ctxValue.getValue();
    			IRepresentation valuerepresentation = ctxValue.getRepresentation();		
    			System.out.println("****Scopevalue: "+ valueScope.getScopeAsString());
    			System.out.println("****Representationvalue: "+ valuerepresentation.getRepresentationAsString());
    			System.out.println("****Value: "+ value.getValue().toString());
    			System.out.println("****ValueType: "+ value.getValueType().toString());
    		}
    	}
    }
*/    
}
