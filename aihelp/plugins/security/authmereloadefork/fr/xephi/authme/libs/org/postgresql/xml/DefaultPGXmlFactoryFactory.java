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

public class DefaultPGXmlFactoryFactory implements PGXmlFactoryFactory {
   public static final DefaultPGXmlFactoryFactory INSTANCE = new DefaultPGXmlFactoryFactory();

   private DefaultPGXmlFactoryFactory() {
   }

   private DocumentBuilderFactory getDocumentBuilderFactory() {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      setFactoryProperties(factory);
      factory.setXIncludeAware(false);
      factory.setExpandEntityReferences(false);
      return factory;
   }

   public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
      DocumentBuilder builder = this.getDocumentBuilderFactory().newDocumentBuilder();
      builder.setEntityResolver(EmptyStringEntityResolver.INSTANCE);
      builder.setErrorHandler(NullErrorHandler.INSTANCE);
      return builder;
   }

   public TransformerFactory newTransformerFactory() {
      TransformerFactory factory = TransformerFactory.newInstance();
      setFactoryProperties(factory);
      return factory;
   }

   public SAXTransformerFactory newSAXTransformerFactory() {
      SAXTransformerFactory factory = (SAXTransformerFactory)SAXTransformerFactory.newInstance();
      setFactoryProperties(factory);
      return factory;
   }

   public XMLInputFactory newXMLInputFactory() {
      XMLInputFactory factory = XMLInputFactory.newInstance();
      setPropertyQuietly(factory, "javax.xml.stream.supportDTD", false);
      setPropertyQuietly(factory, "javax.xml.stream.isSupportingExternalEntities", false);
      return factory;
   }

   public XMLOutputFactory newXMLOutputFactory() {
      return XMLOutputFactory.newInstance();
   }

   public XMLReader createXMLReader() throws SAXException {
      XMLReader factory = XMLReaderFactory.createXMLReader();
      setFeatureQuietly(factory, "http://apache.org/xml/features/disallow-doctype-decl", true);
      setFeatureQuietly(factory, "http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
      setFeatureQuietly(factory, "http://xml.org/sax/features/external-general-entities", false);
      setFeatureQuietly(factory, "http://xml.org/sax/features/external-parameter-entities", false);
      factory.setErrorHandler(NullErrorHandler.INSTANCE);
      return factory;
   }

   private static void setFeatureQuietly(Object factory, String name, boolean value) {
      try {
         if (factory instanceof DocumentBuilderFactory) {
            ((DocumentBuilderFactory)factory).setFeature(name, value);
         } else if (factory instanceof TransformerFactory) {
            ((TransformerFactory)factory).setFeature(name, value);
         } else {
            if (!(factory instanceof XMLReader)) {
               throw new Error("Invalid factory class: " + factory.getClass());
            }

            ((XMLReader)factory).setFeature(name, value);
         }

      } catch (Exception var4) {
      }
   }

   private static void setAttributeQuietly(Object factory, String name, Object value) {
      try {
         if (factory instanceof DocumentBuilderFactory) {
            ((DocumentBuilderFactory)factory).setAttribute(name, value);
         } else {
            if (!(factory instanceof TransformerFactory)) {
               throw new Error("Invalid factory class: " + factory.getClass());
            }

            ((TransformerFactory)factory).setAttribute(name, value);
         }
      } catch (Exception var4) {
      }

   }

   private static void setFactoryProperties(Object factory) {
      setFeatureQuietly(factory, "http://javax.xml.XMLConstants/feature/secure-processing", true);
      setFeatureQuietly(factory, "http://apache.org/xml/features/disallow-doctype-decl", true);
      setFeatureQuietly(factory, "http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
      setFeatureQuietly(factory, "http://xml.org/sax/features/external-general-entities", false);
      setFeatureQuietly(factory, "http://xml.org/sax/features/external-parameter-entities", false);
      setAttributeQuietly(factory, "http://javax.xml.XMLConstants/property/accessExternalDTD", "");
      setAttributeQuietly(factory, "http://javax.xml.XMLConstants/property/accessExternalSchema", "");
      setAttributeQuietly(factory, "http://javax.xml.XMLConstants/property/accessExternalStylesheet", "");
   }

   private static void setPropertyQuietly(Object factory, String name, Object value) {
      try {
         if (factory instanceof XMLReader) {
            ((XMLReader)factory).setProperty(name, value);
         } else {
            if (!(factory instanceof XMLInputFactory)) {
               throw new Error("Invalid factory class: " + factory.getClass());
            }

            ((XMLInputFactory)factory).setProperty(name, value);
         }
      } catch (Exception var4) {
      }

   }
}
