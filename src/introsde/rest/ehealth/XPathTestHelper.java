package introsde.rest.ehealth;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XPathTestHelper {

	Document doc;
    XPath xpath;
    InputSource inputSource;
	
    public void loadXML(String xmlToParse) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
    	 
        //creating xpath object
        getXPathObj();
        inputSource = new InputSource(new StringReader(xmlToParse));
       
    }

    public XPath getXPathObj() {

        XPathFactory factory = XPathFactory.newInstance();
        xpath = factory.newXPath();
        return xpath;
    }

    public Node getBookByName(String bookTitle) throws XPathExpressionException {

    	
        XPathExpression expr = xpath.compile("/bookstore/book[title='" + bookTitle + "']");
        Node node = (Node) expr.evaluate("sdd", XPathConstants.NODE);
        return node;
    }
    
    public NodeList getNodeListResult(String xml, String condition) throws XPathExpressionException {

    	XPathExpression expr = xpath.compile(condition);
        NodeList nodes = (NodeList) expr.evaluate(inputSource, XPathConstants.NODESET);
        
        return nodes;
    	
    	/*System.out.println("---"+condition);
    	System.out.print(xml);
        XPathExpression expr = xpath.compile("/people/person/idPerson");
        System.out.println("\n\n\n\nquiuiiiiii---->"+expr);*/
        
       
        //NodeList nodeLista = (NodeList) expr.evaluate(new StringReader(xml),XPathConstants.NODE);
        //return null;
        
    }

    public Node getBookByISBN(String ISBN) throws XPathExpressionException {

        XPathExpression expr = xpath.compile("/bookstore/book[isbn='" + ISBN + "']");
        Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
        return node;
    }

    public NodeList getBooksByPrice(String price, String condition) throws XPathExpressionException {

        XPathExpression expr = xpath.compile("//book[price " + condition + "'" + price + "']");
        NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        return nodes;

    }

    public Node getBookByAuthorUsingAxis(String authorName) throws XPathExpressionException {

        XPathExpression expr = xpath.compile("//child::book[author='" + authorName + "']");
        Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
        return node;
    }
    
    /*public String printHeader(int requestNumber){
    	return "Request #"+requestNumber+": "+ this.connection.getRequestMethod() + " "+this.url.getPath() + " Accept: " +this.connection.getRequestProperty("Accept")+ " Content-Type: " + this.connection.getRequestProperty("Content-Type");
    }*/
}
