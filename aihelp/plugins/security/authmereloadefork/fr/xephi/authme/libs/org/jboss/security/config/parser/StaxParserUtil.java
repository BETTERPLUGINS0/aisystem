package fr.xephi.authme.libs.org.jboss.security.config.parser;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.util.Iterator;
import java.util.Set;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class StaxParserUtil implements XMLStreamConstants {
   public static String getAttributeValue(Attribute attribute) {
      return trim(attribute.getValue());
   }

   public static String getStartElementName(StartElement startElement) {
      return trim(startElement.getName().getLocalPart());
   }

   public static final String trim(String inputStr) {
      if (inputStr != null && inputStr.length() != 0) {
         return inputStr.trim();
      } else {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("inputStr");
      }
   }

   public static XMLStreamException missingRequired(XMLStreamReader reader, Set<?> required) {
      StringBuilder b = new StringBuilder();
      Iterator iterator = required.iterator();

      while(iterator.hasNext()) {
         Object o = iterator.next();
         b.append(o.toString());
         if (iterator.hasNext()) {
            b.append(", ");
         }
      }

      return PicketBoxMessages.MESSAGES.missingRequiredAttributes(b.toString(), reader.getLocation());
   }

   public static XMLStreamException unexpectedElement(XMLStreamReader reader) {
      return PicketBoxMessages.MESSAGES.unexpectedElement(reader.getName().toString(), reader.getLocation());
   }

   public static XMLStreamException unexpectedElement(String elementName, XMLEvent event) {
      return PicketBoxMessages.MESSAGES.unexpectedElement(elementName, event.getLocation());
   }

   public static XMLStreamException unexpectedNS(String namespaceURI, XMLEvent event) {
      return PicketBoxMessages.MESSAGES.unexpectedNamespace(namespaceURI, event.getLocation());
   }

   public static XMLStreamException unexpectedAttribute(XMLStreamReader reader, int index) {
      return PicketBoxMessages.MESSAGES.unexpectedAttribute(reader.getAttributeName(index).toString(), reader.getLocation());
   }

   public static void requireNoContent(XMLStreamReader reader) throws XMLStreamException {
      if (reader.hasNext() && reader.nextTag() != 2) {
         throw unexpectedElement(reader);
      }
   }
}
