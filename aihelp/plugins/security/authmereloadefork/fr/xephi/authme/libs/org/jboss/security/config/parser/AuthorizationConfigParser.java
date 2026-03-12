package fr.xephi.authme.libs.org.jboss.security.config.parser;

import fr.xephi.authme.libs.org.jboss.security.authorization.config.AuthorizationModuleEntry;
import fr.xephi.authme.libs.org.jboss.security.config.ControlFlag;
import fr.xephi.authme.libs.org.jboss.security.config.Element;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class AuthorizationConfigParser implements XMLStreamConstants {
   public Set<AuthorizationModuleEntry> parse(XMLEventReader xmlEventReader) throws XMLStreamException {
      LinkedHashSet entries = new LinkedHashSet();

      while(xmlEventReader.hasNext()) {
         XMLEvent xmlEvent = xmlEventReader.peek();
         StartElement peekedStartElement = (StartElement)xmlEvent;
         AuthorizationModuleEntry entry = null;
         if (!"policy-module".equals(StaxParserUtil.getStartElementName(peekedStartElement))) {
            break;
         }

         entry = this.getEntry(xmlEventReader);
         entries.add(entry);
      }

      return entries;
   }

   private AuthorizationModuleEntry getEntry(XMLEventReader xmlEventReader) throws XMLStreamException {
      XMLEvent xmlEvent = xmlEventReader.nextEvent();
      Map<String, Object> options = new HashMap();
      String codeName = null;
      ControlFlag controlFlag = ControlFlag.REQUIRED;
      StartElement policyModuleElement = (StartElement)xmlEvent;
      Iterator attrs = policyModuleElement.getAttributes();

      while(attrs.hasNext()) {
         Attribute attribute = (Attribute)attrs.next();
         QName attQName = attribute.getName();
         String attributeValue = StaxParserUtil.getAttributeValue(attribute);
         if ("code".equals(attQName.getLocalPart())) {
            codeName = attributeValue;
         } else if ("flag".equals(attQName.getLocalPart())) {
            controlFlag = ControlFlag.valueOf(attributeValue);
         }
      }

      ModuleOptionParser moParser = new ModuleOptionParser();
      options.putAll(moParser.parse(xmlEventReader));
      AuthorizationModuleEntry entry = new AuthorizationModuleEntry(codeName, options);
      entry.setControlFlag(controlFlag);
      return entry;
   }

   public Set<AuthorizationModuleEntry> parse(XMLStreamReader reader) throws XMLStreamException {
      LinkedHashSet entries = new LinkedHashSet();

      while(reader.hasNext() && reader.nextTag() != 2) {
         Element element = Element.forName(reader.getLocalName());
         AuthorizationModuleEntry entry = null;
         if (!element.equals(Element.POLICY_MODULE)) {
            throw StaxParserUtil.unexpectedElement(reader);
         }

         entry = this.getEntry(reader);
         entries.add(entry);
      }

      return entries;
   }

   private AuthorizationModuleEntry getEntry(XMLStreamReader reader) throws XMLStreamException {
      Map<String, Object> options = new HashMap();
      String codeName = null;
      ControlFlag controlFlag = ControlFlag.REQUIRED;
      int count = reader.getAttributeCount();
      if (count < 2) {
         Set<fr.xephi.authme.libs.org.jboss.security.config.Attribute> set = new HashSet();
         set.add(fr.xephi.authme.libs.org.jboss.security.config.Attribute.CODE);
         set.add(fr.xephi.authme.libs.org.jboss.security.config.Attribute.FLAG);
         throw StaxParserUtil.missingRequired(reader, set);
      } else {
         for(int i = 0; i < count; ++i) {
            String value = reader.getAttributeValue(i);
            fr.xephi.authme.libs.org.jboss.security.config.Attribute attribute = fr.xephi.authme.libs.org.jboss.security.config.Attribute.forName(reader.getAttributeLocalName(i));
            switch(attribute) {
            case CODE:
               codeName = value;
               break;
            case FLAG:
               controlFlag = ControlFlag.valueOf(value);
               break;
            default:
               throw StaxParserUtil.unexpectedAttribute(reader, i);
            }
         }

         ModuleOptionParser moParser = new ModuleOptionParser();
         options.putAll(moParser.parse(reader));
         AuthorizationModuleEntry entry = new AuthorizationModuleEntry(codeName, options);
         entry.setControlFlag(controlFlag);
         return entry;
      }
   }
}
