package fr.xephi.authme.libs.org.jboss.security.config;

import fr.xephi.authme.libs.org.jboss.security.authorization.config.AuthorizationModuleEntry;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class AuthorizationInfo extends BaseSecurityInfo<AuthorizationModuleEntry> {
   public AuthorizationInfo(String name) {
      super(name);
   }

   public AuthorizationModuleEntry[] getAuthorizationModuleEntry() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(GET_CONFIG_ENTRY_PERM);
      }

      AuthorizationModuleEntry[] entries = new AuthorizationModuleEntry[this.moduleEntries.size()];
      this.moduleEntries.toArray(entries);
      return entries;
   }

   protected BaseSecurityInfo<AuthorizationModuleEntry> create(String name) {
      return new AuthorizationInfo(name);
   }

   public void writeContent(XMLStreamWriter writer) throws XMLStreamException {
      for(int i = 0; i < this.moduleEntries.size(); ++i) {
         AuthorizationModuleEntry entry = (AuthorizationModuleEntry)this.moduleEntries.get(i);
         writer.writeStartElement(Element.POLICY_MODULE.getLocalName());
         writer.writeAttribute(Attribute.CODE.getLocalName(), entry.getPolicyModuleName());
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
