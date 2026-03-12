package fr.xephi.authme.libs.org.jboss.security.config.parser;

import fr.xephi.authme.libs.org.jboss.security.audit.config.AuditProviderEntry;
import fr.xephi.authme.libs.org.jboss.security.config.Element;
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

public class AuditConfigParser implements XMLStreamConstants {
   public List<AuditProviderEntry> parse(XMLEventReader xmlEventReader) throws XMLStreamException {
      ArrayList entries = new ArrayList();

      while(xmlEventReader.hasNext()) {
         XMLEvent xmlEvent = xmlEventReader.peek();
         StartElement peekedStartElement = (StartElement)xmlEvent;
         AuditProviderEntry entry = null;
         if (!"provider-module".equals(StaxParserUtil.getStartElementName(peekedStartElement))) {
            break;
         }

         entry = this.getEntry(xmlEventReader);
         entries.add(entry);
      }

      return entries;
   }

   private AuditProviderEntry getEntry(XMLEventReader xmlEventReader) throws XMLStreamException {
      XMLEvent xmlEvent = xmlEventReader.nextEvent();
      Map<String, Object> options = new HashMap();
      String codeName = null;
      StartElement policyModuleElement = (StartElement)xmlEvent;
      Iterator attrs = policyModuleElement.getAttributes();

      while(attrs.hasNext()) {
         Attribute attribute = (Attribute)attrs.next();
         QName attQName = attribute.getName();
         String attributeValue = StaxParserUtil.getAttributeValue(attribute);
         if ("code".equals(attQName.getLocalPart())) {
            codeName = attributeValue;
         }
      }

      ModuleOptionParser moParser = new ModuleOptionParser();
      options.putAll(moParser.parse(xmlEventReader));
      AuditProviderEntry entry = new AuditProviderEntry(codeName, options);
      return entry;
   }

   public List<AuditProviderEntry> parse(XMLStreamReader reader) throws XMLStreamException {
      ArrayList entries = new ArrayList();

      while(reader.hasNext() && reader.nextTag() != 2) {
         Element element = Element.forName(reader.getLocalName());
         AuditProviderEntry entry = null;
         if (!element.equals(Element.PROVIDER_MODULE)) {
            throw StaxParserUtil.unexpectedElement(reader);
         }

         entry = this.getEntry(reader);
         entries.add(entry);
      }

      return entries;
   }

   private AuditProviderEntry getEntry(XMLStreamReader reader) throws XMLStreamException {
      String codeName = null;
      Map<String, Object> options = new HashMap();
      int count = reader.getAttributeCount();
      if (count < 1) {
         throw StaxParserUtil.missingRequired(reader, Collections.singleton(fr.xephi.authme.libs.org.jboss.security.config.Attribute.CODE));
      } else {
         int i = 0;

         while(i < count) {
            String value = reader.getAttributeValue(i);
            fr.xephi.authme.libs.org.jboss.security.config.Attribute attribute = fr.xephi.authme.libs.org.jboss.security.config.Attribute.forName(reader.getAttributeLocalName(i));
            switch(attribute) {
            case CODE:
               codeName = value;
               ++i;
               break;
            default:
               throw StaxParserUtil.unexpectedAttribute(reader, i);
            }
         }

         ModuleOptionParser moParser = new ModuleOptionParser();
         options.putAll(moParser.parse(reader));
         AuditProviderEntry entry = new AuditProviderEntry(codeName, options);
         return entry;
      }
   }
}
