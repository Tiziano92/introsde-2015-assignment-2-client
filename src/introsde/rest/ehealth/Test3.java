package introsde.rest.ehealth;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpressionException;

import org.glassfish.jersey.client.ClientConfig;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Test3 {
	public static void main(String[] args) throws TransformerFactoryConfigurationError, TransformerException, JsonParseException, JsonMappingException, IOException, XPathExpressionException, ParserConfigurationException, SAXException {
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		
		WebTarget service = client.target(getBaseURI());
		
		XPathTestHelper xth = new XPathTestHelper();
		
		Response response;
		
		PrintWriter writerXml = new PrintWriter("client-server-xml.log", "UTF-8");
		
		PrintWriter writerJson = new PrintWriter("client-server-json.log", "UTF-8");
		
		//PrintWriter writer = new PrintWriter("prova.xml");
		
		
		
		//========================Printing the server url=============================
		System.out.println("Server calling: "+service.getUri());
		
		writerXml.write("Server calling: "+service.getUri());
		writerJson.write("Server calling: "+service.getUri());
		//============================================================================
		
		
		//============================Request #1==========================================
		System.out.print("\n\n===================STEP 3.1=======================");
		
		writerXml.write("\n\n===================STEP 3.1=======================");
		writerJson.write("\n\n===================STEP 3.1=======================");
		
		String xmlFormat1 = request1(service, "xml");
		
		String jsonFormat1 = request1(service, "json");
		
		int status = service.path("person")
				.request()
				.accept(MediaType.APPLICATION_XML).get().getStatus();
		
		xth.loadXML(xmlFormat1);
		NodeList nl = xth.getNodeListResult(xmlFormat1, "/people/person/idPerson");
		
		System.out.println(printStep1(nl, "xml", xmlFormat1, status));
		writerXml.write(printStep1(nl, "xml", xmlFormat1, status));
		
		System.out.println(printStep1(nl, "json", jsonFormat1, status));
		writerJson.write(printStep1(nl, "json", jsonFormat1, status));
		
		Node first = nl.item(0);
        int first_person_id = Integer.parseInt(first.getTextContent());
        
        Node last = nl.item(nl.getLength() - 1);
        int last_person_id = Integer.parseInt(last.getTextContent());
        
        //================================================================================
        
        
        //============================Request #2==========================================
        
  		System.out.println();
  		System.out.print("\n\n===================STEP 3.2=======================");
  		
  		writerXml.write("\n\n===================STEP 3.2=======================");
		writerJson.write("\n\n===================STEP 3.2=======================");
  		
        String xmlFormat = request2(service, "xml", first_person_id);
  		
  		String jsonFormat = request2(service, "json", first_person_id);
  		
  		status = service.path("person/"+first_person_id)
  				.request()
  				.accept(MediaType.APPLICATION_XML).get().getStatus();
  		
  		System.out.println(printStep2(first_person_id, "xml", xmlFormat, status));
  		writerXml.write(printStep2(first_person_id, "xml", xmlFormat, status));
  		
  		System.out.println(printStep2(first_person_id, "json", jsonFormat, status));
  		writerJson.write(printStep2(first_person_id, "json", jsonFormat, status));

        //================================================================================
  		
  		
  		
  		//============================Request #3==========================================

  		System.out.print("\n\n===================STEP 3.3=======================");
  		
  		writerXml.write("\n\n===================STEP 3.3=======================");
		writerJson.write("\n\n===================STEP 3.3=======================");
  		
  		//XML PUT REQUEST
		xth.loadXML(xmlFormat1);
		nl = xth.getNodeListResult(xmlFormat1, "/people/person/firstname");
		Node firstName = nl.item(0);
		String oldName = firstName.getTextContent();
		
		String nameToChange = "hjjhsadhjadja";
		
		String xmlRequest = "<person><firstname>"+nameToChange+"</firstname></person>";
  		response = request3(service, "xml", first_person_id, xmlRequest);
		status = response.getStatus();
		
		String xmlFormatNew = request2(service, "xml", first_person_id);
		
		xth.loadXML(xmlFormatNew);
		nl = xth.getNodeListResult(xmlFormatNew, "/person/firstname");
		Node firstNameNew = nl.item(0);
		String originalNameNew = firstNameNew.getTextContent();
		
		System.out.println(printStep3(first_person_id, originalNameNew, oldName, "xml", xmlFormatNew, status));
		writerXml.write(printStep3(first_person_id, originalNameNew, oldName, "xml", xmlFormatNew, status));
		//JSON PUT REQUEST
		
		String xmlFormat1New = request2(service, "xml", first_person_id);
		
		xth.loadXML(xmlFormat1New);
		nl = xth.getNodeListResult(xmlFormat1New, "/person/firstname");
		firstName = nl.item(0);
		oldName = firstName.getTextContent();
		
		String jsonRequest = "{\"firstname\":\""+nameToChange+"\"}";
  		response = request3(service, "json", first_person_id, jsonRequest);
		status = response.getStatus();
		
		String xmlFormatNewJson = request2(service, "xml", first_person_id);
		
		/*xth.loadXML(xmlFormatNewJson);
		nl = xth.getNodeListResult(xmlFormatNewJson, "/person/firstname");
		Node firstNameNewJson = nl.item(0);
		String originalNameNewJson = firstNameNew.getTextContent();*/
		
		xmlFormatNewJson = request2(service, "json", first_person_id);
	
		System.out.println(printStep3(first_person_id, originalNameNew, oldName, "json", xmlFormatNewJson, status));
		writerJson.write(printStep3(first_person_id, originalNameNew, oldName, "json", xmlFormatNewJson, status));
		//==============================================================================

		
		
		//============================Request #4==========================================
		
        //POST WITH XML
		System.out.print("\n\n===================STEP 3.4=======================");
		
		writerXml.write("\n\n===================STEP 3.4=======================");
		writerJson.write("\n\n===================STEP 3.4=======================");
		
		String createNewPersonXml = "<person>"
				+ "<firstname>Chuck</firstname>"
				+ "<lastname>Norris</lastname>"
				+ "<birthdate>1945-01-01</birthdate>"
				+ "<healthProfile>"
					+ "<lifeStatus>"
						+ "<measureType>"
							+ "<idMeasureDef>1</idMeasureDef>"
							+ "<name>weight</name>"
						+ "</measureType>"
						+ "<value>78.9</value>"
					+ "</lifeStatus>"
					+ "<lifeStatus>"
						+ "<measureType>"
							+ "<idMeasureDef>2</idMeasureDef>"
							+ "<name>height</name>"
						+ "</measureType>"
						+ "<value>172</value>"
					+ "</lifeStatus>"
				+ "</healthProfile>"
			+ "</person>";
		
		//response = service.path("person").request(MediaType.APPLICATION_XML_TYPE).post(Entity.xml(createNewPersonXml));
		String responseXml = request4(service, "xml", createNewPersonXml);
		status = response.getStatus();

		xth.loadXML(responseXml);
		
		nl = xth.getNodeListResult(responseXml, "/person/idPerson");

		Node newNodeId = nl.item(0);
        int new_id_personXml = Integer.parseInt(newNodeId.getTextContent());
        
        System.out.println(printStep4(status, "xml", responseXml));
        writerXml.write(printStep4(status, "xml", responseXml));
        
        
        //POST WITH JSON
        
        String createNewPersonJson ="{"
        		+ "\"firstname\" : \"Chuck\","
        		+ "\"lastname\" : \"Norris\","
        		+ "\"birthdate\" : \"1945-01-01\","
        		+ "\"lifeStatus\": [{"
        			+ "\"value\": \"78.9\","
        			+ "\"measureType\" : {"
        				+ "\"idMeasureDef\" : \"1\","
        				+ "\"name\" : \"weight\"}}"
        				+ ",{"
        			+ "\"value\" : \"172\","
        			+ "\"measureType\" : {"
        				+ "\"idMeasureDef\" : \"2\","
        				+ "\"name\" : \"height\"}}]}";
        		
		//response = service.path("person").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(createNewPersonJson));
		String responseJson = request4(service, "json", createNewPersonJson);
		status = response.getStatus();
		
		
		String xmlFormatLastId = request1(service, "xml");
		
		
		xth.loadXML(xmlFormatLastId);
		
		nl = xth.getNodeListResult(xmlFormatLastId, "/people/person/idPerson");
        
        Node new_id_personJsonNode = nl.item(nl.getLength() - 1);
        int new_id_personJson = Integer.parseInt(new_id_personJsonNode.getTextContent());

        System.out.println(printStep4(status, "json", responseJson));
        writerJson.write(printStep4(status, "json", responseJson));
        

        
        
        //============================Request #5=========================================


        System.out.print("\n\n===================STEP 3.5=======================");
        
        writerXml.write("\n\n===================STEP 3.5=======================");
		writerJson.write("\n\n===================STEP 3.5=======================");
        
        //REQUEST #5
        status = request5(service, "xml" , new_id_personXml);
        System.out.println(printStep5Req5(status, new_id_personXml, "xml"));
        writerXml.write(printStep5Req5(status, new_id_personXml, "xml"));
        
        status = request5(service, "json" , new_id_personJson);
        System.out.println(printStep5Req5(status, new_id_personJson, "json"));
        writerJson.write(printStep5Req5(status, new_id_personJson, "json"));
        
        
        //REQUEST #6
        status = service.path("person"+"/"+new_id_personXml)
				.request()
				.accept(MediaType.APPLICATION_XML).get().getStatus();
        
        System.out.println(printStep5Req6(status, new_id_personXml, "xml"));
        writerXml.write(printStep5Req6(status, new_id_personXml, "xml"));
        
        status = service.path("person"+"/"+new_id_personJson)
				.request()
				.accept(MediaType.APPLICATION_JSON).get().getStatus();
        
        System.out.println(printStep5Req6(status, new_id_personJson, "json"));
        writerJson.write(printStep5Req6(status, new_id_personJson, "json"));
        
	    //============================Request #6==========================================
        
        System.out.print("\n\n===================STEP 3.6=======================");
        
        writerXml.write("\n\n===================STEP 3.6=======================");
		writerJson.write("\n\n===================STEP 3.6=======================");
        
        String xmlFormatMeasure = request9(service, "xml");

  		String jsonFormatMeasure = request9(service, "json");
  		
  		status = service.path("measureTypes")
  				.request()
  				.accept(MediaType.APPLICATION_XML).get().getStatus();
  		
  		
  		xth.loadXML(xmlFormatMeasure);
		nl = xth.getNodeListResult(xmlFormatMeasure, "/measureTypes/measureType");
		
		System.out.println(printStep6(nl, "xml", xmlFormatMeasure, status));
		writerXml.write(printStep6(nl, "xml", xmlFormatMeasure, status));
		
		System.out.println(printStep6(nl, "json", jsonFormatMeasure, status));
		writerJson.write(printStep6(nl, "json", jsonFormatMeasure, status));
		
		String measure_types [] = new String[nl.getLength()];
        
        for (int i = 0; i < nl.getLength(); i++){
        	measure_types[i] = nl.item(i).getTextContent();
        }
        
        //=================================================================================
        
        
        
        
        //============================Request #7=============================================
        
        System.out.print("\n\n===================STEP 3.7=======================");
        
        writerXml.write("\n\n===================STEP 3.7=======================");
		writerJson.write("\n\n===================STEP 3.7=======================");
        
        boolean find = false;
        Node midNode = null;
        int mid = 0;
        int status1 = 0;
        //int personId = -1;
        NodeList nl2 = null;
        String saveMeasure = measure_types[0];
        
        for(int i = 0; i < measure_types.length - 1; i++){
        	
        	status1 = service.path("person/"+first_person_id+"/"+measure_types[i])
      				.request()
      				.accept(MediaType.APPLICATION_XML).get().getStatus();
        	
        	xmlFormat = request6(service, "xml" , first_person_id, measure_types[i], status1);
        	
        	xth.loadXML(xmlFormat);
    		nl2 = xth.getNodeListResult(xmlFormat, "/healthMeasureHistories/measure/mid");
    		
    		if(nl2.getLength() >= 1){
    			saveMeasure = measure_types[i];
    			find = true;
    			midNode = nl2.item(0);
    			mid = Integer.parseInt(midNode.getTextContent());
    			break;
    		
    		}
    		
    		status1 = service.path("person/"+last_person_id+"/"+measure_types[i])
      				.request()
      				.accept(MediaType.APPLICATION_XML).get().getStatus();
        
    		xmlFormat = request6(service, "xml" , last_person_id, measure_types[i], status1);
        	
        	xth.loadXML(xmlFormat);
    		nl2 = xth.getNodeListResult(xmlFormat, "/healthMeasureHistories/healthMeasureHistory/mid");
    		
    		if(nl2.getLength() >= 1){
    			saveMeasure = measure_types[i];
    			find = true;
    			midNode = nl2.item(0);
    			mid = Integer.parseInt(midNode.getTextContent());
    			break;
    		}
    		
        }
        
        
        for(int i = 0; i < measure_types.length - 1; i++){

        	status = service.path("person/"+first_person_id+"/"+measure_types[i])
      				.request()
      				.accept(MediaType.APPLICATION_XML).get().getStatus();
        	
        	xmlFormat = request6(service, "xml" , first_person_id, measure_types[i], status);
        	
        	jsonFormat = request6(service, "json" , first_person_id, measure_types[i], status);
        	
        	
        	System.out.println(printStep7(find, first_person_id, measure_types[i], "xml", xmlFormat, status));
        	writerXml.write(printStep7(find, first_person_id, measure_types[i], "xml", xmlFormat, status));
        	
        	System.out.println(printStep7(find, first_person_id, measure_types[i], "json", jsonFormat, status));
        	writerJson.write(printStep7(find, first_person_id, measure_types[i], "json", jsonFormat, status));
        }
        
        
        for(int i = 0; i < measure_types.length - 1; i++){
        	
        	
        	
        	status = service.path("person/"+last_person_id+"/"+measure_types[i])
      				.request()
      				.accept(MediaType.APPLICATION_XML).get().getStatus();
        	
        	xmlFormat = request6(service, "xml" , last_person_id, measure_types[i], status); 
        	
        	jsonFormat = request6(service, "json" , last_person_id, measure_types[i], status); 
        	
        	
        	System.out.println(printStep7(find, last_person_id, measure_types[i], "xml", xmlFormat, status));
        	writerXml.write(printStep7(find, last_person_id, measure_types[i], "xml", xmlFormat, status));
        	
        	System.out.println(printStep7(find, last_person_id, measure_types[i], "json", jsonFormat, status));
        	writerJson.write(printStep7(find, last_person_id, measure_types[i], "json", jsonFormat, status));
        	
        }

        //===================================================================================
        
        
        
        //============================Request #8==========================================
         
        System.out.print("\n\n===================STEP 3.8=======================");
        
        writerXml.write("\n\n===================STEP 3.8=======================");
		writerJson.write("\n\n===================STEP 3.8=======================");
        
        //GET FIRST PERSON
        xmlFormat = request7(service, "xml" , first_person_id, saveMeasure, mid);
        
  		jsonFormat = request7(service, "json" , first_person_id, saveMeasure, mid);
  		
  		status = service.path("person/"+first_person_id+"/"+saveMeasure+"/"+mid)
  				.request()
  				.accept(MediaType.APPLICATION_XML).get().getStatus();

  		
  		System.out.println(printStep8(first_person_id, saveMeasure, mid, "xml", xmlFormat, status));
  		writerXml.write(printStep8(first_person_id, saveMeasure, mid, "xml", xmlFormat, status));
  		 
  		System.out.println(printStep8(first_person_id, saveMeasure, mid,  "json", jsonFormat, status));
  		writerJson.write(printStep8(first_person_id, saveMeasure, mid,  "json", jsonFormat, status));
  		
  		//GET LAST PERSON
  		
  		xmlFormat = request7(service, "xml" , last_person_id, saveMeasure, mid);
        
  		
  		jsonFormat = request7(service, "json" , last_person_id, saveMeasure, mid);
  		
  		status = service.path("person/"+last_person_id+"/"+saveMeasure+"/"+mid)
  				.request()
  				.accept(MediaType.APPLICATION_XML).get().getStatus();

  		
  		System.out.println(printStep8(last_person_id, saveMeasure, mid, "xml", xmlFormat, status));
  		writerXml.write(printStep8(last_person_id, saveMeasure, mid, "xml", xmlFormat, status));
  		
  		System.out.println(printStep8(last_person_id, saveMeasure, mid,  "json", jsonFormat, status));
  		writerJson.write(printStep8(last_person_id, saveMeasure, mid,  "json", jsonFormat, status));
        //writer.close();
		
		//=================================================================================
        
  		
  		
  		//============================Request #9==========================================
  		
  		System.out.print("\n\n===================STEP 3.9=======================");
  		
  		writerXml.write("\n\n===================STEP 3.9=======================");
		writerJson.write("\n\n===================STEP 3.9=======================");
  		
  		
  		
  		status = service.path("person/"+first_person_id+"/"+saveMeasure)
  				.request()
  				.accept(MediaType.APPLICATION_XML).get().getStatus();
  		
  		xmlFormat = request6(service, "xml" , first_person_id, saveMeasure, status); 
        
  		jsonFormat = request6(service, "json" , first_person_id, saveMeasure, status); 
  		
  		xth.loadXML(xmlFormat);
		
		NodeList nlCount = xth.getNodeListResult(xmlFormat, "/healthMeasureHistories/measure/mid");
		
  		int startCountValue = nlCount.getLength();
  		
  		System.out.println(printStep9GET(first_person_id, saveMeasure,  "xml", xmlFormat, status));
  		writerXml.write(printStep9GET(first_person_id, saveMeasure,  "xml", xmlFormat, status));
  		
  		System.out.println(printStep9GET(first_person_id, saveMeasure,  "json", jsonFormat, status));
  		writerJson.write(printStep9GET(first_person_id, saveMeasure,  "json", jsonFormat, status));
  		
  		//SEND R8 XML
  		
  		String createNewHistoryXml = "<measure>"
				+ "<value>72</value>"
				+ "<created>2011-12-09</created>"
				+ "</measure>";
				
		
		response = service.path("person/"+first_person_id+"/"+saveMeasure).request(MediaType.APPLICATION_XML_TYPE).post(Entity.xml(createNewHistoryXml));
		responseXml = response.readEntity(String.class);
		
		//System.out.println("id Person = "+first_person_id + "\n saveMeasure = "+saveMeasure);
		
		
		
		status = response.getStatus();
		
		System.out.println(printStep8POST(first_person_id, saveMeasure, status, "xml", responseXml));
		writerXml.write(printStep8POST(first_person_id, saveMeasure, status, "xml", responseXml));
		
		
		//SEND R8 JSON
		
		String createNewHistoryJson = "{"
        		+ "\"created\" : \"2011-12-09\","
        		+ "\"value\" : \"72\""
        		+ "}";
		
		response = service.path("person/"+first_person_id+"/"+saveMeasure).request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(createNewHistoryJson));
		responseJson = response.readEntity(String.class);
		status = response.getStatus();
		
		System.out.println(printStep8POST(first_person_id, saveMeasure, status, "json", responseJson));
		writerJson.write(printStep8POST(first_person_id, saveMeasure, status, "json", responseJson));
		
		
		//GET WITH NEW VALUE
		
		
  		
  		status = service.path("person/"+first_person_id+"/"+saveMeasure)
  				.request()
  				.accept(MediaType.APPLICATION_XML).get().getStatus();
  		
  		xmlFormat = request6(service, "xml" , first_person_id, saveMeasure, status); 
 		
  		jsonFormat = request6(service, "json" , first_person_id, saveMeasure, status); 
  		
  		xth.loadXML(xmlFormat);
		
		NodeList nlCountNew = xth.getNodeListResult(xmlFormat, "/healthMeasureHistories/measure/mid");
		
  		int startCountValueNew = nlCountNew.getLength();
  		
  		System.out.println(printStep9GETWithCount(first_person_id, saveMeasure,  "xml", xmlFormat, status, startCountValue, startCountValueNew));
  		writerXml.write(printStep9GETWithCount(first_person_id, saveMeasure,  "xml", xmlFormat, status, startCountValue, startCountValueNew));
  		
  		System.out.println(printStep9GETWithCount(first_person_id, saveMeasure,  "json", jsonFormat, status, startCountValue, startCountValueNew));
  		writerJson.write(printStep9GETWithCount(first_person_id, saveMeasure,  "json", jsonFormat, status, startCountValue, startCountValueNew));
  		
		//================================================================================

  		
  		//============================Request #10==========================================
  		
  		System.out.print("\n\n===================STEP 3.10=======================");
  		
  		writerXml.write("\n\n===================STEP 3.10=======================");
  		writerJson.write("\n\n===================STEP 3.10=======================");
  		
  		status1 = service.path("person/"+first_person_id+"/"+saveMeasure)
  				.request()
  				.accept(MediaType.APPLICATION_XML).get().getStatus();
  		
  		xmlFormat = request6(service, "xml" , first_person_id, saveMeasure, status1);
  		
  		xth.loadXML(xmlFormat);
		NodeList nlList = xth.getNodeListResult(xmlFormat, "/healthMeasureHistories/measure/mid");
		
		//System.out.println("LUNGHEZZA = " +nlList.getLength());
		xth.loadXML(xmlFormat);
		NodeList nlListValue = xth.getNodeListResult(xmlFormat, "/healthMeasureHistories/measure/value");
	
		// GET THE MID FOR JSON AND FOR XML
		Node midNode_id_json = nlList.item(nlList.getLength() - 1);
        int mid_id_json = Integer.parseInt(midNode_id_json.getTextContent());

        Node midNode_id_xml = nlList.item(nlList.getLength() - 2);
        int mid_id_xml = Integer.parseInt(midNode_id_xml.getTextContent());
        
        
        //GET THE VALUE FOR JSON AND XML
        Node midNode_id_jsonValue = nlListValue.item(nlListValue.getLength() - 1);
        String mid_id_jsonValue = midNode_id_jsonValue.getTextContent();
        
        Node midNode_id_XmValue = nlListValue.item(nlListValue.getLength() - 2);
        String mid_id_xmlValue = midNode_id_XmValue.getTextContent();
        
        String updateNewHistoryXml = "<measure>"
				+ "<value>90</value>"
				+ "<created>2011-12-09</created>"
				+ "</measure>";
        
        response = request10(service, "xml", first_person_id, saveMeasure, mid_id_xml, updateNewHistoryXml);
        status = response.getStatus();
        String output = "\n\nRequest #10: PUT /person/"+first_person_id+"/"+saveMeasure+"/"+mid_id_xml+" Accept: APPLICATION/XML Content-Type: APPLICATION/XML"; 
        output += "\n=> Result: "+"OK";
        output += "\n=> HTTP Status: "+status;
        System.out.println(output);
        
        writerXml.write(output);
        
        
        String updateNewHistoryJson = "{"
        		+ "\"created\" : \"2011-12-09\","
        		+ "\"value\" : \"90\""
        		+ "}";
        
        response = request10(service, "json", first_person_id, saveMeasure, mid_id_json, updateNewHistoryJson);
        status = response.getStatus();
        output = "\n\nRequest #10: PUT /person/"+first_person_id+"/"+saveMeasure+"/"+mid_id_json+" Accept: APPLICATION/JSON Content-Type: APPLICATION/JSON"; 
        output += "\n=> Result: "+"OK";
        output += "\n=> HTTP Status: "+status;
        System.out.println(output);
        writerJson.write(output);
        
        
        //PRINT R#6
        
       
        
        status = service.path("person/"+first_person_id+"/"+saveMeasure)
  				.request()
  				.accept(MediaType.APPLICATION_XML).get().getStatus();
        
        xmlFormat = request6(service, "xml" , first_person_id, saveMeasure, status);
        
        jsonFormat = request6(service, "json" , first_person_id, saveMeasure, status);
        
        xth.loadXML(xmlFormat);
		NodeList nlListValueNew = xth.getNodeListResult(xmlFormat, "/healthMeasureHistories/measure/value");
        
		Node midNode_id_jsonValueNew = nlListValueNew.item(nlListValueNew.getLength() - 1);
        String mid_id_jsonValueNew = midNode_id_jsonValueNew.getTextContent();
        
        Node midNode_id_XmValueNew = nlListValueNew.item(nlListValueNew.getLength() - 2);
        String mid_id_xmlValueNew = midNode_id_XmValueNew.getTextContent();
        
        
        System.out.println(printStep10(first_person_id, saveMeasure, mid_id_xmlValue, mid_id_xmlValueNew,"xml", xmlFormat, status));
        writerXml.write(printStep10(first_person_id, saveMeasure, mid_id_xmlValue, mid_id_xmlValueNew,"xml", xmlFormat, status));
        
        System.out.println(printStep10(first_person_id, saveMeasure, mid_id_jsonValue, mid_id_jsonValueNew,"json", jsonFormat, status));
        writerJson.write(printStep10(first_person_id, saveMeasure, mid_id_jsonValue, mid_id_jsonValueNew,"json", jsonFormat, status));
        
         //================================================================================
        
  		writerXml.close();
  		writerJson.close();
  		
  		
	}


	
	private static URI getBaseURI() {
		return UriBuilder.fromUri(
				"https://peaceful-garden-1175.herokuapp.com/sdelab").build();
	}
	
	
	public static String prettyFormat(String input, int indent) {
	    try {
	        Source xmlInput = new StreamSource(new StringReader(input));
	        StringWriter stringWriter = new StringWriter();
	        StreamResult xmlOutput = new StreamResult(stringWriter);
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        transformerFactory.setAttribute("indent-number", indent);
	        Transformer transformer = transformerFactory.newTransformer(); 
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.transform(xmlInput, xmlOutput);
	        return xmlOutput.getWriter().toString();
	    } catch (Exception e) {
	        throw new RuntimeException(e); // simple exception handling, please review it
	    }
	}

	public static String prettyFormat(String input) {
	    return prettyFormat(input, 2);
	}
	
	/**
	 * Send #R1
	 * @param service
	 * @param format
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String request1(WebTarget service, String format) throws JsonParseException, JsonMappingException, IOException{
    	
		String res = null;
		
		if(format.equals("xml")){
			String xmlFormat = service.path("person")
					.request()
					.accept(MediaType.APPLICATION_XML).get().readEntity(String.class);
			
			res = printBody(format, xmlFormat);
		}
		else{
			String jsonFormat = service.path("person")
					.request()
					.accept(MediaType.APPLICATION_JSON).get().readEntity(String.class);
			
			res = printBody(format, jsonFormat);
		}
		
    	return res;
    	
    }
	
	/**
	 * Send #R2
	 * @param service
	 * @param format
	 * @param id
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String request2(WebTarget service, String format, int id) throws JsonParseException, JsonMappingException, IOException{
    	
		String res = null;

		if(format.equals("xml")){
			String xmlFormat = service.path("person/"+id)
	  				.request()
	  				.accept(MediaType.APPLICATION_XML).get().readEntity(String.class);
			
			res = printBody(format, xmlFormat);
		}
		else{
			String jsonFormat = service.path("person/"+id)
	  				.request()
	  				.accept(MediaType.APPLICATION_JSON).get().readEntity(String.class);
			
			res = printBody(format, jsonFormat);
		}
		
    	return res;
    	
    }
	
	/**
	 * Send #R3
	 * @param service
	 * @param format
	 * @param id
	 * @param xmlRequest
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static Response request3(WebTarget service, String format, int id, String xmlRequest) throws JsonParseException, JsonMappingException, IOException{
    	
		Response res = null;

		if(format.equals("xml")){
			res = service.path("person/"+id).
					request(MediaType.APPLICATION_XML_TYPE).put(Entity.xml(xmlRequest));

		}
		else{
			res = service.path("person/"+id).
					request(MediaType.APPLICATION_JSON_TYPE).put(Entity.json(xmlRequest));
		}
		
    	return res;
    	
    }
	
	/**
	 * Send #R4
	 * @param service
	 * @param format
	 * @param xmlRequest
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String request4(WebTarget service, String format, String xmlRequest) throws JsonParseException, JsonMappingException, IOException{
    	
		Response res = null;
		String postResp = null;

		if(format.equals("xml")){
			res = service.path("person").request(MediaType.APPLICATION_XML_TYPE).post(Entity.xml(xmlRequest));
			postResp = res.readEntity(String.class);
		}
		else{
			res = service.path("person").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(xmlRequest));
			postResp = res.readEntity(String.class);
		}
		
    	return postResp;
    	
    }
	
	/**
	 * Send #R5
	 * @param service
	 * @param format
	 * @param id
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static int request5(WebTarget service, String format, int id) throws JsonParseException, JsonMappingException, IOException{
    	
		int status = 0;

		if(format.equals("xml")){
			status = service.path("person/"+id)
					.request()
					.accept(MediaType.APPLICATION_XML).delete().getStatus();
		}
		else{
			status = service.path("person/"+id)
					.request()
					.accept(MediaType.APPLICATION_JSON).delete().getStatus();
		}
		
    	return status;
    	
    }
	
	/**
	 * Send R#6
	 * @param service
	 * @param format
	 * @param id
	 * @param measure
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String request6(WebTarget service, String format, int id, String measure, int status) throws JsonParseException, JsonMappingException, IOException{
    	
		String res = null;

		if(format.equals("xml")){
			String xmlFormat = service.path("person/"+id+"/"+measure)
      				.request()
      				.accept(MediaType.APPLICATION_XML).get().readEntity(String.class);
			
			if(status != 404){
				res = printBody(format, xmlFormat);
			}

		}
		else{
			String jsonFormat = service.path("person/"+id+"/"+measure)
      				.request()
      				.accept(MediaType.APPLICATION_JSON).get().readEntity(String.class);
			
			if(status != 404){
				res = printBody(format, jsonFormat);
			}
			
			
		}
		
    	return res;
    	
    }
	
	
	/**
	 * Send R#7
	 * @param service
	 * @param format
	 * @param id
	 * @param measure
	 * @param mid
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String request7(WebTarget service, String format, int id, String measure, int mid) throws JsonParseException, JsonMappingException, IOException{
    	
		String res = null;

		if(format.equals("xml")){
			String xmlFormat = service.path("person/"+id+"/"+measure+"/"+mid)
      				.request()
      				.accept(MediaType.APPLICATION_XML).get().readEntity(String.class);
			
			res = printBody(format, xmlFormat);
		}
		else{
			String jsonFormat = service.path("person/"+id+"/"+measure+"/"+mid)
      				.request()
      				.accept(MediaType.APPLICATION_JSON).get().readEntity(String.class);
			
			res = printBody(format, jsonFormat);
		}
		
    	return res;
    	
    }
	
	/**
	 * Send R#9
	 * @param service
	 * @param format
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String request9(WebTarget service, String format) throws JsonParseException, JsonMappingException, IOException{
    	
		String res = null;

		if(format.equals("xml")){
			String xmlFormat = service.path("measureTypes")
				.request()
				.accept(MediaType.APPLICATION_XML).get().readEntity(String.class);
			
			res = printBody(format, xmlFormat);
		}
		else{
			String jsonFormat = service.path("measureTypes")
					.request()
					.accept(MediaType.APPLICATION_JSON).get().readEntity(String.class);
			
			res = printBody(format, jsonFormat);
		}
		
    	return res;
    	
    }
	
	/**
	 * Send R#10
	 * @param service
	 * @param format
	 * @param id
	 * @param measure
	 * @param mid
	 * @param xmlRequest
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static Response request10(WebTarget service, String format, int id, String measure, int mid, String xmlRequest) throws JsonParseException, JsonMappingException, IOException{
    	
		Response res = null;

		if(format.equals("xml")){
			res = service.path("person/"+id+"/"+measure+"/"+mid).
					request(MediaType.APPLICATION_XML_TYPE).put(Entity.xml(xmlRequest));
			
		}
		else{
			res = service.path("person/"+id+"/"+measure+"/"+mid).
					request(MediaType.APPLICATION_JSON_TYPE).put(Entity.json(xmlRequest));

		}
		
    	return res;
    	
    }
	
	
	
	
	
	
	/**
	 * Print information for Step 1
	 * @param nl
	 * @param format
	 * @param output
	 * @param status
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	
	public static String printStep1(NodeList nl, String format, String output, int status) throws JsonParseException, JsonMappingException, IOException{
		
		String res = "\n\nRequest #1: GET /person Accept: APPLICATION/"+format.toUpperCase()+" Content-Type: APPLICATION/"+format.toUpperCase(); 
		
		if(nl.getLength() > 2){
			res += "\n=> Result: OK";
		}else{
			res += "\n=> Result: ERROR";
		}
		
		res += "\n=> HTTP Status: "+status;
		
		res += "\n"+printBody(format, output);
		
		return res;

	}
	
	/**
	 * Print information for Step 2
	 * @param nl
	 * @param format
	 * @param output
	 * @param status
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String printStep2(int id, String format, String output, int status) throws JsonParseException, JsonMappingException, IOException{
		
		String res = "\n\nRequest #2: GET /person/"+id+" Accept: APPLICATION/"+format.toUpperCase()+" Content-Type: APPLICATION/"+format.toUpperCase(); 
		
		if(status == 200 || status == 202){
			res += "\n=> Result: OK";
		}else{
			res += "\n=> Result: ERROR";
		}
		
		res += "\n=> HTTP Status: "+status;
		
		res += "\n"+printBody(format, output);
		
		return res;

	}
	
	/**
	 * Print Step 3
	 * @param index
	 * @param idPerson
	 * @param measure
	 * @param format
	 * @param output
	 * @param status
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String printStep3(int idPerson, String originalName, String newName, String format, String output, int status) throws JsonParseException, JsonMappingException, IOException{
		
		String res = "\n\nRequest #3: PUT /person/"+idPerson+" Accept: APPLICATION/"+format.toUpperCase()+" Content-Type: APPLICATION/"+format.toUpperCase(); 

		if(originalName.equals(newName)){
			res += "\n=> Result: ERROR";
        }
        else{
        	res += "\n=> Result: OK";
        }
		
		res += "\n=> HTTP Status: "+status;
		
		res += "\n"+printBody(format, output);
		
		return res;
        

	}
	
	/**
	 * Print Step 4
	 * 
	 * @param status
	 * @param format
	 * @param output
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String printStep4(int status, String format, String output) throws JsonParseException, JsonMappingException, IOException{
		
		String res = "\n\nRequest #4"+": POST /person"+" Accept: APPLICATION/"+format.toUpperCase()+" Content-Type: APPLICATION/"+format.toUpperCase(); 

		if(status == 201 || status == 200 || status == 202){
			res += "\n=> Result: OK";
        }
        else{
        	res += "\n=> Result: ERROR";
        }
		
		res += "\n=> HTTP Status: "+status;
		
		res += "\n"+printBody(format, output);
		
		return res;

	}
	
	/**
	 * Print Step 5
	 * 
	 * @param status
	 * @param personId
	 * @param format
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String printStep5Req5(int status, int personId, String format) throws JsonParseException, JsonMappingException, IOException{
		
		String res = "\n\nRequest #5: DELETE /person/"+personId+" Accept: APPLICATION/"+format.toUpperCase()+" Content-Type: APPLICATION/"+format.toUpperCase(); 

		if(status == 204){
			res += "\n=> Result: OK";
        }
        else{
        	res += "\n=> Result: ERROR";
        }
		
		res += "\n=> HTTP Status: "+status;
		
		return res;

	}
	
	
	public static String printStep5Req6(int status, int personId, String format) throws JsonParseException, JsonMappingException, IOException{
		
		String res = "\n\nRequest #6: GET /person/"+personId+" Accept: APPLICATION/"+format.toUpperCase()+" Content-Type: APPLICATION/"+format.toUpperCase(); 

		if(status == 404){
			res += "\n=> Result: OK";
        }
        else{
        	res += "\n=> Result: ERROR";
        }
		
		res += "\n=> HTTP Status: "+status;
		
		return res;

	}
	
	/**
	 * Print information for Step 6
	 * @param nl
	 * @param format
	 * @param output
	 * @param status
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String printStep6(NodeList nl, String format, String output, int status) throws JsonParseException, JsonMappingException, IOException{
		
		String res = "\n\nRequest #9: GET /person/measureTypes Accept: APPLICATION/"+format.toUpperCase()+" Content-Type: APPLICATION/"+format.toUpperCase(); 
		
		if(nl.getLength() > 2){
			res += "\n=> Result: OK";
		}else{
			res += "\n=> Result: ERROR";
		}
		
		res += "\n=> HTTP Status: "+status;
		
		res += "\n"+printBody(format, output);

		return res;
	}
	
	
	/**
	 * Print information for Step 7
	 * 
	 * @param index
	 * @param idPerson
	 * @param measure
	 * @param format
	 * @param output
	 * @param status
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String printStep7(boolean find, int idPerson, String measure, String format, String output, int status) throws JsonParseException, JsonMappingException, IOException{
		
		String res = "\n\nRequest #6: GET /person/"+idPerson+"/"+measure+" Accept: APPLICATION/"+format.toUpperCase()+" Content-Type: APPLICATION/"+format.toUpperCase(); 
		
		if(find == false){
			res += "\n=> Result: ERROR";
        }
        else{
        	res += "\n=> Result: OK";
        }
		
		res += "\n=> HTTP Status: "+status;
		
		if(status != 404){
			res += "\n"+printBody(format, output);
		}
		
		
		
		return res;
        

	}
	
	/**
	 * Print information for Step 8
	 * 
	 * @param idPerson
	 * @param measure
	 * @param format
	 * @param output
	 * @param status
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String printStep8(int idPerson, String measure, int mid, String format, String output, int status) throws JsonParseException, JsonMappingException, IOException{
		
		String res = "\n\nRequest #8: GET /person/"+idPerson+"/"+measure+"/"+mid+" Accept: APPLICATION/"+format.toUpperCase()+" Content-Type: APPLICATION/"+format.toUpperCase(); 
		
		if(status == 200){
			res += "\n=> Result: OK";
		}else{
			res += "\n=> Result: ERROR";
		}
		
		res += "\n=> HTTP Status: "+status;
		
		if(status == 200){
			res += "\n"+printBody(format, output);
		}

		
		return res;

	}
	
	
	/**
	 * Print Step 4
	 * 
	 * @param status
	 * @param format
	 * @param output
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String printStep8POST(int idPerson, String measureSaved, int status, String format, String output) throws JsonParseException, JsonMappingException, IOException{
		
		String res = "\n\nRequest #8"+": POST /person/"+idPerson+"/"+measureSaved+" Accept: APPLICATION/"+format.toUpperCase()+" Content-Type: APPLICATION/"+format.toUpperCase(); 

		if(status == 201 || status == 200 || status == 202){
			res += "\n=> Result: OK";
        }
        else{
        	res += "\n=> Result: ERROR";
        }
		
		res += "\n=> HTTP Status: "+status;
		
		res += "\n"+printBody(format, output);
		
		return res;

	}
	
	/**
	 * 
	 * @param idPerson
	 * @param measure
	 * @param format
	 * @param output
	 * @param status
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String printStep9GET(int idPerson, String measure, String format, String output, int status) throws JsonParseException, JsonMappingException, IOException{
		
		String res = "\n\nRequest #6: GET /person/"+idPerson+"/"+measure+" Accept: APPLICATION/"+format.toUpperCase()+" Content-Type: APPLICATION/"+format.toUpperCase(); 
		
		if(status == 200){
			res += "\n=> Result: OK";
		}else{
			res += "\n=> Result: ERROR";
		}
		
		res += "\n=> HTTP Status: "+status;
		
		res += "\n"+printBody(format, output);
		
		return res;

	}
	
	/**
	 * 
	 * @param idPerson
	 * @param measure
	 * @param format
	 * @param output
	 * @param status
	 * @param firstCount
	 * @param secondCount
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String printStep9GETWithCount(int idPerson, String measure, String format, String output, int status, int firstCount, int secondCount) throws JsonParseException, JsonMappingException, IOException{
		
		String res = "\n\nRequest #6: GET /person/"+idPerson+"/"+measure+" Accept: APPLICATION/"+format.toUpperCase()+" Content-Type: APPLICATION/"+format.toUpperCase(); 
		
		if(secondCount > firstCount){
			res += "\n=> Result: OK";
		}else{
			res += "\n=> Result: ERROR";
		}
		
		res += "\n=> HTTP Status: "+status;
		
		res += "\n"+printBody(format, output);
		
		return res;

	}
	
	/**
	 * Print information for Step 10
	 * @param idPerson
	 * @param measure
	 * @param originalValue
	 * @param newValue
	 * @param format
	 * @param output
	 * @param status
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String printStep10(int idPerson, String measure, String originalValue, String newValue, String format, String output, int status) throws JsonParseException, JsonMappingException, IOException{
		
		String res = "\n\nRequest #6: GET /person/"+idPerson+"/"+measure+" Accept: APPLICATION/"+format.toUpperCase()+" Content-Type: APPLICATION/"+format.toUpperCase(); 

		if(originalValue.equals(newValue)){
			res += "\n=> Result: ERROR";
        }
        else{
        	res += "\n=> Result: OK";
        }
		
		res += "\n=> HTTP Status: "+status;
		
		res += "\n"+printBody(format, output);
		
		return res;
        

	}

	
	/**
	 * Print the body of a specific request
	 * @param format
	 * @param output
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String printBody(String format, String output) throws JsonParseException, JsonMappingException, IOException{
		
		if(format.equals("xml")){
			return prettyFormat(output);
		}
		else{
			
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			
			Object json = mapper.readValue(output, Object.class);
			String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
			
			return indented;
		}
		
	}


}