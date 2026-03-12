package fr.xephi.authme.libs.org.jboss.security.auth.login;

import fr.xephi.authme.libs.org.jboss.security.config.Attribute;
import fr.xephi.authme.libs.org.jboss.security.config.BaseSecurityInfo;
import fr.xephi.authme.libs.org.jboss.security.config.Element;
import fr.xephi.authme.libs.org.jboss.security.config.parser.AuthenticationConfigParser;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.security.auth.AuthPermission;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class AuthenticationInfo extends BaseAuthenticationInfo {
   public static final AuthPermission GET_CONFIG_ENTRY_PERM = new AuthPermission("getLoginConfiguration");
   public static final AuthPermission SET_CONFIG_ENTRY_PERM = new AuthPermission("setLoginConfiguration");
   private CallbackHandler callbackHandler;

   public AuthenticationInfo() {
      this((String)null);
   }

   public AuthenticationInfo(String name) {
      this.name = name;
   }

   public void addAppConfigurationEntry(AppConfigurationEntry entry) {
      this.moduleEntries.add(entry);
   }

   public void setAppConfigurationEntry(AppConfigurationEntry[] loginModules) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(SET_CONFIG_ENTRY_PERM);
      }

      this.moduleEntries.addAll(Arrays.asList(loginModules));
   }

   public void setAppConfigurationEntry(List<AppConfigurationEntry> listOfEntries) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(SET_CONFIG_ENTRY_PERM);
      }

      this.moduleEntries.addAll(listOfEntries);
   }

   public CallbackHandler getAppCallbackHandler() {
      return this.callbackHandler;
   }

   public void setAppCallbackHandler(CallbackHandler handler) {
      this.callbackHandler = handler;
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer("AppConfigurationEntry[]:\n");

      for(int i = 0; i < this.moduleEntries.size(); ++i) {
         AppConfigurationEntry entry = (AppConfigurationEntry)this.moduleEntries.get(i);
         buffer.append("[" + i + "]");
         buffer.append("\nLoginModule Class: " + entry.getLoginModuleName());
         buffer.append("\nControlFlag: " + entry.getControlFlag());
         buffer.append("\nOptions:\n");
         Map<String, ?> options = entry.getOptions();
         Iterator iter = options.entrySet().iterator();

         while(iter.hasNext()) {
            Entry e = (Entry)iter.next();
            String name = (String)e.getKey();
            String value = e.getValue() == null ? "" : e.getValue().toString();
            String nameToLower = name.toLowerCase(Locale.ENGLISH);
            if (nameToLower.equals("password") || nameToLower.equals("bindcredential") || nameToLower.equals("java.naming.security.credentials")) {
               value = "****";
            }

            buffer.append("name=" + name);
            buffer.append(", value=" + value);
            buffer.append("\n");
         }
      }

      return buffer.toString();
   }

   protected BaseSecurityInfo<Object> create(String name) {
      return new AuthenticationInfo(name);
   }

   public void writeContent(XMLStreamWriter writer) throws XMLStreamException {
      for(int i = 0; i < this.moduleEntries.size(); ++i) {
         AppConfigurationEntry entry = (AppConfigurationEntry)this.moduleEntries.get(i);
         writer.writeStartElement(Element.LOGIN_MODULE.getLocalName());
         String code = entry.getLoginModuleName();
         if (!AuthenticationConfigParser.loginModulesMap.containsValue(code)) {
            writer.writeAttribute(Attribute.CODE.getLocalName(), code);
         } else {
            String value = null;
            Set<Entry<String, String>> entries = AuthenticationConfigParser.loginModulesMap.entrySet();
            Iterator i$ = entries.iterator();

            while(i$.hasNext()) {
               Entry<String, String> mapEntry = (Entry)i$.next();
               if (((String)mapEntry.getValue()).equals(code)) {
                  value = (String)mapEntry.getKey();
                  break;
               }
            }

            writer.writeAttribute(Attribute.CODE.getLocalName(), value);
         }

         writer.writeAttribute(Attribute.FLAG.getLocalName(), this.valueOf(entry.getControlFlag()));
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

   private String valueOf(LoginModuleControlFlag controlFlag) {
      if (controlFlag.equals(LoginModuleControlFlag.OPTIONAL)) {
         return "optional";
      } else if (controlFlag.equals(LoginModuleControlFlag.REQUIRED)) {
         return "required";
      } else {
         return controlFlag.equals(LoginModuleControlFlag.REQUISITE) ? "requisite" : "sufficient";
      }
   }
}
