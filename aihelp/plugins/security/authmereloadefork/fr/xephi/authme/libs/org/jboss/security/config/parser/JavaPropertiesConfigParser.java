package fr.xephi.authme.libs.org.jboss.security.config.parser;

import java.util.Properties;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class JavaPropertiesConfigParser implements ParserNamespaceSupport {
   private static final String NAMESPACE_URI = "urn:jboss:java-properties";

   public boolean supports(String namespaceURI) {
      return "urn:jboss:java-properties".equals(namespaceURI);
   }

   public Object parse(XMLEventReader xmlEventReader) throws XMLStreamException {
      Properties props = new Properties();
      XMLEvent xmlEvent = null;

      while(xmlEventReader.hasNext()) {
         xmlEvent = xmlEventReader.peek();
         StartElement peekedStartElement;
         if (xmlEvent instanceof StartElement) {
            peekedStartElement = (StartElement)xmlEvent;
            if ("module-option".equals(StaxParserUtil.getStartElementName(peekedStartElement))) {
               return props;
            }
         }

         if (xmlEvent instanceof EndElement) {
            xmlEvent = xmlEventReader.nextEvent();
         } else {
            xmlEvent = xmlEventReader.nextEvent();
            peekedStartElement = (StartElement)xmlEvent;
            String peekedStartElementName = StaxParserUtil.getStartElementName(peekedStartElement);
            String key = null;
            String value = null;
            if (!peekedStartElementName.contains("property")) {
               throw StaxParserUtil.unexpectedElement(peekedStartElementName, xmlEvent);
            }

            xmlEvent = xmlEventReader.nextEvent();
            peekedStartElement = (StartElement)xmlEvent;
            peekedStartElementName = StaxParserUtil.getStartElementName(peekedStartElement);
            if (!"key".equals(peekedStartElementName)) {
               throw StaxParserUtil.unexpectedElement(peekedStartElementName, xmlEvent);
            }

            key = xmlEventReader.getElementText();
            xmlEvent = xmlEventReader.nextEvent();
            value = xmlEventReader.getElementText();
            props.put(key, value);
         }
      }

      return props;
   }
}
