package fr.xephi.authme.libs.org.postgresql.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class LegacyInsecurePGXmlFactoryFactory implements PGXmlFactoryFactory {
   public static final LegacyInsecurePGXmlFactoryFactory INSTANCE = new LegacyInsecurePGXmlFactoryFactory();

   private LegacyInsecurePGXmlFactoryFactory() {
   }

   public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      builder.setErrorHandler(NullErrorHandler.INSTANCE);
      return builder;
   }

   public TransformerFactory newTransformerFactory() {
      return TransformerFactory.newInstance();
   }

   public SAXTransformerFactory newSAXTransformerFactory() {
      return (SAXTransformerFactory)SAXTransformerFactory.newInstance();
   }

   public XMLInputFactory newXMLInputFactory() {
      return XMLInputFactory.newInstance();
   }

   public XMLOutputFactory newXMLOutputFactory() {
      return XMLOutputFactory.newInstance();
   }

   public XMLReader createXMLReader() throws SAXException {
      return XMLReaderFactory.createXMLReader();
   }
}
