package fr.xephi.authme.libs.org.postgresql.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public interface PGXmlFactoryFactory {
   DocumentBuilder newDocumentBuilder() throws ParserConfigurationException;

   TransformerFactory newTransformerFactory();

   SAXTransformerFactory newSAXTransformerFactory();

   XMLInputFactory newXMLInputFactory();

   XMLOutputFactory newXMLOutputFactory();

   XMLReader createXMLReader() throws SAXException;
}
