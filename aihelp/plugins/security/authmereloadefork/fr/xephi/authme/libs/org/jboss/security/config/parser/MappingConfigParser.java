package fr.xephi.authme.libs.org.jboss.security.config.parser;

import fr.xephi.authme.libs.org.jboss.security.config.Element;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingType;
import fr.xephi.authme.libs.org.jboss.security.mapping.config.MappingModuleEntry;
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

public class MappingConfigParser implements XMLStreamConstants {
   private String typeName;

   public MappingConfigParser() {
      this.typeName = MappingType.ROLE.toString();
   }

   public List<MappingModuleEntry> parse(XMLEventReader xmlEventReader) throws XMLStreamException {
      ArrayList entries = new ArrayList();

      while(xmlEventReader.hasNext()) {
         XMLEvent xmlEvent = xmlEventReader.peek();
         StartElement peekedStartElement = (StartElement)xmlEvent;
         String peekedStartElementName = StaxParserUtil.getStartElementName(peekedStartElement);
         MappingModuleEntry entry = null;
         if (!"mapping-module".equals(peekedStartElementName)) {
            break;
         }

         entry = this.getEntry(xmlEventReader);
         entries.add(entry);
      }

      return entries;
   }

   private MappingModuleEntry getEntry(XMLEventReader xmlEventReader) throws XMLStreamException {
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
         } else if ("type".equals(attQName.getLocalPart())) {
            this.typeName = attributeValue;
         }
      }

      ModuleOptionParser moParser = new ModuleOptionParser();
      options.putAll(moParser.parse(xmlEventReader));
      return new MappingModuleEntry(codeName, options, this.typeName);
   }

   public List<MappingModuleEntry> parse(XMLStreamReader reader) throws XMLStreamException {
      ArrayList entries = new ArrayList();

      while(reader.hasNext() && reader.nextTag() != 2) {
         Element element = Element.forName(reader.getLocalName());
         MappingModuleEntry entry = null;
         if (!element.equals(Element.MAPPING_MODULE)) {
            throw StaxParserUtil.unexpectedElement(reader);
         }

         entry = this.getEntry(reader);
         entries.add(entry);
      }

      return entries;
   }

   private MappingModuleEntry getEntry(XMLStreamReader reader) throws XMLStreamException {
      Map<String, Object> options = new HashMap();
      String codeName = null;
      int count = reader.getAttributeCount();
      if (count < 1) {
         throw StaxParserUtil.missingRequired(reader, Collections.singleton(fr.xephi.authme.libs.org.jboss.security.config.Attribute.CODE));
      } else {
         for(int i = 0; i < count; ++i) {
            String value = reader.getAttributeValue(i);
            fr.xephi.authme.libs.org.jboss.security.config.Attribute attribute = fr.xephi.authme.libs.org.jboss.security.config.Attribute.forName(reader.getAttributeLocalName(i));
            switch(attribute) {
            case CODE:
               codeName = value;
               break;
            case TYPE:
               this.typeName = value;
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
            return new MappingModuleEntry(codeName, options, this.typeName);
         }
      }
   }
}
