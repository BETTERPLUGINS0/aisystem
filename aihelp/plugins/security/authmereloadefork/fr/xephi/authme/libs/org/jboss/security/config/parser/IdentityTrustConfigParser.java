package fr.xephi.authme.libs.org.jboss.security.config.parser;

import fr.xephi.authme.libs.org.jboss.security.config.ControlFlag;
import fr.xephi.authme.libs.org.jboss.security.config.Element;
import fr.xephi.authme.libs.org.jboss.security.identitytrust.config.IdentityTrustModuleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class IdentityTrustConfigParser implements XMLStreamConstants {
   public List<IdentityTrustModuleEntry> parse(XMLEventReader xmlEventReader) throws XMLStreamException {
      ArrayList entries = new ArrayList();

      while(xmlEventReader.hasNext()) {
         XMLEvent xmlEvent = xmlEventReader.peek();
         StartElement peekedStartElement = (StartElement)xmlEvent;
         IdentityTrustModuleEntry entry = null;
         if (!"trust-module".equals(StaxParserUtil.getStartElementName(peekedStartElement))) {
            break;
         }

         entry = this.getEntry(xmlEventReader);
         entries.add(entry);
      }

      return entries;
   }

   private IdentityTrustModuleEntry getEntry(XMLEventReader xmlEventReader) throws XMLStreamException {
      XMLEvent xmlEvent = xmlEventReader.nextEvent();
      Map<String, Object> options = new HashMap();
      String codeName = null;
      ControlFlag flag = null;
      StartElement policyModuleElement = (StartElement)xmlEvent;
      Iterator attrs = policyModuleElement.getAttributes();

      while(attrs.hasNext()) {
         Attribute attribute = (Attribute)attrs.next();
         QName attQName = attribute.getName();
         String attributeValue = StaxParserUtil.getAttributeValue(attribute);
         if ("code".equals(attQName.getLocalPart())) {
            codeName = attributeValue;
         }

         if ("flag".equals(attQName.getLocalPart())) {
            flag = ControlFlag.valueOf(attributeValue);
         }
      }

      ModuleOptionParser moParser = new ModuleOptionParser();
      options.putAll(moParser.parse(xmlEventReader));
      IdentityTrustModuleEntry entry = new IdentityTrustModuleEntry(codeName, options);
      entry.setControlFlag(flag);
      return entry;
   }

   public List<IdentityTrustModuleEntry> parse(XMLStreamReader reader) throws XMLStreamException {
      ArrayList entries = new ArrayList();

      while(reader.hasNext() && reader.nextTag() != 2) {
         Element element = Element.forName(reader.getLocalName());
         IdentityTrustModuleEntry entry = null;
         if (!element.equals(Element.TRUST_MODULE)) {
            throw StaxParserUtil.unexpectedElement(reader);
         }

         entry = this.getEntry(reader);
         entries.add(entry);
      }

      return entries;
   }

   private IdentityTrustModuleEntry getEntry(XMLStreamReader reader) throws XMLStreamException {
      Map<String, Object> options = new HashMap();
      String codeName = null;
      ControlFlag flag = ControlFlag.REQUIRED;
      int count = reader.getAttributeCount();
      if (count == 0) {
         throw StaxParserUtil.missingRequired(reader, Collections.singleton(fr.xephi.authme.libs.org.jboss.security.config.Attribute.CODE));
      } else {
         for(int i = 0; i < count; ++i) {
            String value = reader.getAttributeValue(i);
            fr.xephi.authme.libs.org.jboss.security.config.Attribute attribute = fr.xephi.authme.libs.org.jboss.security.config.Attribute.forName(reader.getAttributeLocalName(i));
            switch(attribute) {
            case CODE:
               codeName = value;
               break;
            case FLAG:
               flag = ControlFlag.valueOf(value);
               break;
            default:
               throw StaxParserUtil.unexpectedAttribute(reader, i);
            }
         }

         if (codeName == null) {
            throw StaxParserUtil.missingRequired(reader, Collections.singleton(fr.xephi.authme.libs.org.jboss.security.config.Attribute.CODE));
         } else {
            ModuleOptionParser moParser = new ModuleOptionParser();
            options.putAll(moParser.parse(reader));
            IdentityTrustModuleEntry entry = new IdentityTrustModuleEntry(codeName, options);
            entry.setControlFlag(flag);
            return entry;
         }
      }
   }
}
