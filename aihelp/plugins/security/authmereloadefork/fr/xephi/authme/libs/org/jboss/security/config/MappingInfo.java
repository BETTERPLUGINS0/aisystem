package fr.xephi.authme.libs.org.jboss.security.config;

import fr.xephi.authme.libs.org.jboss.security.mapping.config.MappingModuleEntry;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class MappingInfo extends BaseSecurityInfo<MappingModuleEntry> {
   public MappingInfo() {
   }

   public MappingInfo(String name) {
      super(name);
   }

   public MappingModuleEntry[] getMappingModuleEntry() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(GET_CONFIG_ENTRY_PERM);
      }

      MappingModuleEntry[] entries = new MappingModuleEntry[this.moduleEntries.size()];
      this.moduleEntries.toArray(entries);
      return entries;
   }

   protected BaseSecurityInfo<MappingModuleEntry> create(String name) {
      return new MappingInfo(name);
   }

   public void writeContent(XMLStreamWriter writer) throws XMLStreamException {
      for(int i = 0; i < this.moduleEntries.size(); ++i) {
         MappingModuleEntry entry = (MappingModuleEntry)this.moduleEntries.get(i);
         writer.writeStartElement(Element.MAPPING_MODULE.getLocalName());
         writer.writeAttribute(Attribute.CODE.getLocalName(), entry.getMappingModuleName());
         writer.writeAttribute(Attribute.TYPE.getLocalName(), entry.getMappingModuleType());
         Map<String, ?> options = entry.getOptions();
         if (options != null && options.size() > 0) {
            Iterator i$ = options.entrySet().iterator();

            while(i$.hasNext()) {
               Entry<String, ?> option = (Entry)i$.next();
               writer.writeStartElement(Element.MODULE_OPTION.getLocalName());
               writer.writeAttribute(Attribute.NAME.getLocalName(), (String)option.getKey());
               writer.writeAttribute(Attribute.VALUE.getLocalName(), option.getValue().toString());
               writer.writeEndElement();
            }
         }

         writer.writeEndElement();
      }

      writer.writeEndElement();
   }
}
