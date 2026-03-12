package fr.xephi.authme.libs.org.jboss.security.config;

import fr.xephi.authme.libs.org.jboss.security.identitytrust.config.IdentityTrustModuleEntry;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class IdentityTrustInfo extends BaseSecurityInfo<IdentityTrustModuleEntry> {
   public IdentityTrustInfo(String name) {
      super(name);
   }

   public IdentityTrustModuleEntry[] getIdentityTrustModuleEntry() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(GET_CONFIG_ENTRY_PERM);
      }

      IdentityTrustModuleEntry[] entries = new IdentityTrustModuleEntry[this.moduleEntries.size()];
      this.moduleEntries.toArray(entries);
      return entries;
   }

   protected BaseSecurityInfo<IdentityTrustModuleEntry> create(String name) {
      return new IdentityTrustInfo(name);
   }

   public void writeContent(XMLStreamWriter writer) throws XMLStreamException {
      for(int i = 0; i < this.moduleEntries.size(); ++i) {
         IdentityTrustModuleEntry entry = (IdentityTrustModuleEntry)this.moduleEntries.get(i);
         writer.writeStartElement(Element.TRUST_MODULE.getLocalName());
         writer.writeAttribute(Attribute.CODE.getLocalName(), entry.getName());
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
