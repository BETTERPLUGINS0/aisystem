package fr.xephi.authme.libs.org.jboss.security.config.parser;

import fr.xephi.authme.libs.org.jboss.security.config.Element;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class ModuleOptionParser implements XMLStreamConstants {
   private static final Map<String, ParserNamespaceSupport> parsers = Collections.synchronizedMap(new HashMap());

   public static void addParser(String parserName, ParserNamespaceSupport parser) {
      parsers.put(parserName, parser);
   }

   public Map<String, Object> parse(XMLEventReader xmlEventReader) throws XMLStreamException {
      HashMap options = new HashMap();

      while(true) {
         XMLEvent xmlEvent = xmlEventReader.peek();
         if (xmlEvent instanceof EndElement) {
            break;
         }

         StartElement peekedStartElement = (StartElement)xmlEvent;
         if (xmlEvent == null) {
            break;
         }

         String peekedStartElementName = StaxParserUtil.getStartElementName(peekedStartElement);
         if (!"module-option".equals(peekedStartElementName)) {
            break;
         }

         xmlEvent = xmlEventReader.nextEvent();
         Attribute attribute = (Attribute)peekedStartElement.getAttributes().next();
         Object val = null;

         try {
            val = xmlEventReader.getElementText();
         } catch (XMLStreamException var10) {
            XMLEvent embeddedOrText = xmlEventReader.peek();
            if (embeddedOrText.getEventType() == 1) {
               val = this.embeddedXMLParsing(xmlEventReader);
            }
         }

         options.put(attribute.getValue(), val);
      }

      return options;
   }

   private Object embeddedXMLParsing(XMLEventReader xmlEventReader) throws XMLStreamException {
      Object retVal = null;
      XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
      xmlEventReader = xmlInputFactory.createFilteredReader(xmlEventReader, new EventFilter() {
         public boolean accept(XMLEvent xmlEvent) {
            return xmlEvent.isStartElement();
         }
      });

      while(xmlEventReader.hasNext()) {
         XMLEvent xmlEvent = xmlEventReader.peek();
         int eventType = xmlEvent.getEventType();
         switch(eventType) {
         case 1:
            StartElement xmlStartElement = (StartElement)xmlEvent;
            String nsURI = xmlStartElement.getName().getNamespaceURI();
            ParserNamespaceSupport parser = this.getSupportingParser(nsURI);
            if (parser == null) {
               throw StaxParserUtil.unexpectedNS(nsURI, xmlEvent);
            }

            return parser.parse(xmlEventReader);
         }
      }

      return retVal;
   }

   private ParserNamespaceSupport getSupportingParser(String nsURI) {
      return (ParserNamespaceSupport)parsers.get(nsURI);
   }

   public Map<String, Object> parse(XMLStreamReader reader) throws XMLStreamException {
      HashMap options;
      String name;
      String optionValue;
      for(options = new HashMap(); reader.hasNext() && reader.nextTag() != 2; options.put(name, optionValue)) {
         Element element = Element.forName(reader.getLocalName());
         if (!element.equals(Element.MODULE_OPTION)) {
            throw StaxParserUtil.unexpectedElement(reader);
         }

         int count = reader.getAttributeCount();
         if (count == 0) {
            throw StaxParserUtil.missingRequired(reader, Collections.singleton(fr.xephi.authme.libs.org.jboss.security.config.Attribute.NAME));
         }

         name = null;
         optionValue = null;

         for(int i = 0; i < count; ++i) {
            String value = reader.getAttributeValue(i);
            fr.xephi.authme.libs.org.jboss.security.config.Attribute attribute = fr.xephi.authme.libs.org.jboss.security.config.Attribute.forName(reader.getAttributeLocalName(i));
            switch(attribute) {
            case NAME:
               name = value;
               break;
            case VALUE:
               optionValue = value;
               break;
            default:
               throw StaxParserUtil.unexpectedAttribute(reader, i);
            }
         }

         if (optionValue == null) {
            optionValue = reader.getElementText();
         } else {
            StaxParserUtil.requireNoContent(reader);
         }
      }

      return options;
   }

   static {
      parsers.put("urn:jboss:user-roles", new UsersConfigParser());
      parsers.put("urn:jboss:java-properties", new JavaPropertiesConfigParser());
   }
}
