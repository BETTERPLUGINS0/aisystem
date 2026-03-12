package fr.xephi.authme.libs.org.jboss.security.config;

import fr.xephi.authme.libs.org.jboss.security.acl.config.ACLProviderEntry;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class ACLInfo extends BaseSecurityInfo<ACLProviderEntry> {
   public ACLInfo(String name) {
      super(name);
   }

   public ACLProviderEntry[] getACLProviderEntry() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(GET_CONFIG_ENTRY_PERM);
      }

      ACLProviderEntry[] entries = new ACLProviderEntry[this.moduleEntries.size()];
      this.moduleEntries.toArray(entries);
      return entries;
   }

   protected BaseSecurityInfo<ACLProviderEntry> create(String name) {
      return new ACLInfo(name);
   }

   public void writeContent(XMLStreamWriter writer) throws XMLStreamException {
      for(int i = 0; i < this.moduleEntries.size(); ++i) {
         ACLProviderEntry entry = (ACLProviderEntry)this.moduleEntries.get(i);
         writer.writeStartElement(Element.ACL_MODULE.getLocalName());
         writer.writeAttribute(Attribute.CODE.getLocalName(), entry.getAclProviderName());
         writer.writeAttribute(Attribute.FLAG.getLocalName(), entry.getControlFlag().toString().toLowerCase(Locale.ENGLISH));
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
