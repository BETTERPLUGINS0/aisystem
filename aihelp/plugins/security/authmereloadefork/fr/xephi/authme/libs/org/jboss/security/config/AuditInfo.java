package fr.xephi.authme.libs.org.jboss.security.config;

import fr.xephi.authme.libs.org.jboss.security.audit.config.AuditProviderEntry;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class AuditInfo extends BaseSecurityInfo<AuditProviderEntry> {
   public AuditInfo(String name) {
      super(name);
   }

   public AuditProviderEntry[] getAuditProviderEntry() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(GET_CONFIG_ENTRY_PERM);
      }

      AuditProviderEntry[] entries = new AuditProviderEntry[this.moduleEntries.size()];
      this.moduleEntries.toArray(entries);
      return entries;
   }

   protected BaseSecurityInfo<AuditProviderEntry> create(String name) {
      return new AuditInfo(name);
   }

   public void writeContent(XMLStreamWriter writer) throws XMLStreamException {
      for(int i = 0; i < this.moduleEntries.size(); ++i) {
         AuditProviderEntry entry = (AuditProviderEntry)this.moduleEntries.get(i);
         writer.writeStartElement(Element.PROVIDER_MODULE.getLocalName());
         writer.writeAttribute(Attribute.CODE.getLocalName(), entry.getName());
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
