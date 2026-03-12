package fr.xephi.authme.libs.org.jboss.security.auth.login;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.auth.container.config.AuthModuleEntry;
import fr.xephi.authme.libs.org.jboss.security.config.Attribute;
import fr.xephi.authme.libs.org.jboss.security.config.BaseSecurityInfo;
import fr.xephi.authme.libs.org.jboss.security.config.Element;
import fr.xephi.authme.libs.org.jboss.security.config.parser.AuthenticationConfigParser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class JASPIAuthenticationInfo extends BaseAuthenticationInfo {
   List<LoginModuleStackHolder> loginModuleStack = Collections.synchronizedList(new ArrayList());

   public JASPIAuthenticationInfo() {
   }

   public JASPIAuthenticationInfo(String name) {
      super(name);
   }

   public void add(LoginModuleStackHolder lmsHolder) {
      this.loginModuleStack.add(lmsHolder);
   }

   public void add(AuthModuleEntry ame) {
      this.moduleEntries.add(ame);
   }

   public AuthModuleEntry[] getAuthModuleEntry() {
      AuthModuleEntry[] entries = new AuthModuleEntry[this.moduleEntries.size()];
      this.moduleEntries.toArray(entries);
      return entries;
   }

   public LoginModuleStackHolder getLoginModuleStackHolder(String name) {
      Iterator i$ = this.loginModuleStack.iterator();

      LoginModuleStackHolder holder;
      do {
         if (!i$.hasNext()) {
            return null;
         }

         holder = (LoginModuleStackHolder)i$.next();
      } while(!holder.getName().equals(name));

      return holder;
   }

   public LoginModuleStackHolder[] getLoginModuleStackHolder() {
      LoginModuleStackHolder[] lmshArr = new LoginModuleStackHolder[this.loginModuleStack.size()];
      this.loginModuleStack.toArray(lmshArr);
      return lmshArr;
   }

   public LoginModuleStackHolder removeLoginModuleStackHolder(String name) {
      Iterator it = this.loginModuleStack.iterator();

      LoginModuleStackHolder holder;
      do {
         if (!it.hasNext()) {
            return null;
         }

         holder = (LoginModuleStackHolder)it.next();
      } while(!holder.getName().equals(name));

      it.remove();
      return holder;
   }

   public void copy(JASPIAuthenticationInfo pc) {
      this.loginModuleStack.addAll(pc.loginModuleStack);
      this.moduleEntries.addAll(pc.moduleEntries);
   }

   public AppConfigurationEntry[] getAppConfigurationEntry() {
      return this.loginModuleStack.size() > 0 ? ((LoginModuleStackHolder)this.loginModuleStack.get(0)).getAppConfigurationEntry() : new AppConfigurationEntry[0];
   }

   public AppConfigurationEntry[] copyAppConfigurationEntry() {
      List<Object> entries = new ArrayList();
      AppConfigurationEntry[] arr$ = this.getAppConfigurationEntry();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         AppConfigurationEntry entry = arr$[i$];
         entries.add(entry);
      }

      return super.copyAppConfigurationEntry(entries);
   }

   protected BaseSecurityInfo<Object> create(String name) {
      return new JASPIAuthenticationInfo(name);
   }

   public BaseSecurityInfo<Object> merge(BaseSecurityInfo<Object> bi) {
      if (!(bi instanceof JASPIAuthenticationInfo)) {
         throw PicketBoxMessages.MESSAGES.invalidType(JASPIAuthenticationInfo.class.getName());
      } else {
         JASPIAuthenticationInfo merged = (JASPIAuthenticationInfo)super.merge(bi);
         JASPIAuthenticationInfo parent = (JASPIAuthenticationInfo)bi;
         LoginModuleStackHolder[] arr$ = parent.getLoginModuleStackHolder();
         int len$ = arr$.length;

         int i$;
         LoginModuleStackHolder holder;
         for(i$ = 0; i$ < len$; ++i$) {
            holder = arr$[i$];
            merged.add(holder);
         }

         arr$ = this.getLoginModuleStackHolder();
         len$ = arr$.length;

         for(i$ = 0; i$ < len$; ++i$) {
            holder = arr$[i$];
            merged.add(holder);
         }

         return merged;
      }
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer("AuthModuleEntry[]:\n");

      for(int i = 0; i < this.moduleEntries.size(); ++i) {
         AuthModuleEntry entry = (AuthModuleEntry)this.moduleEntries.get(i);
         buffer.append("[" + i + "]");
         buffer.append("\nAuthModule Class: " + entry.getAuthModuleName());
         buffer.append("\nOptions:");
         Map<String, Object> options = entry.getOptions();
         Iterator i$ = options.entrySet().iterator();

         while(i$.hasNext()) {
            Entry<String, Object> optionsEntry = (Entry)i$.next();
            buffer.append("name=" + (String)optionsEntry.getKey());
            buffer.append(", value=" + optionsEntry.getValue());
            buffer.append("\n");
         }
      }

      return buffer.toString();
   }

   public void writeContent(XMLStreamWriter writer) throws XMLStreamException {
      int i;
      for(i = 0; i < this.loginModuleStack.size(); ++i) {
         LoginModuleStackHolder entry = (LoginModuleStackHolder)this.loginModuleStack.get(i);
         writer.writeStartElement(Element.LOGIN_MODULE_STACK.getLocalName());
         writer.writeAttribute(Attribute.NAME.getLocalName(), entry.getName());

         for(int j = 0; j < entry.getAppConfigurationEntry().length; ++j) {
            writer.writeStartElement(Element.LOGIN_MODULE.getLocalName());
            AppConfigurationEntry ace = entry.getAppConfigurationEntry()[j];
            String code = ace.getLoginModuleName();
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

            writer.writeAttribute(Attribute.FLAG.getLocalName(), this.valueOf(ace.getControlFlag()));
            Map<String, ?> options = ace.getOptions();
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

      for(i = 0; i < this.moduleEntries.size(); ++i) {
         AuthModuleEntry entry = (AuthModuleEntry)this.moduleEntries.get(i);
         writer.writeStartElement(Element.AUTH_MODULE.getLocalName());
         writer.writeAttribute(Attribute.CODE.getLocalName(), entry.getAuthModuleName());
         writer.writeAttribute(Attribute.FLAG.getLocalName(), entry.getControlFlag().toString().toLowerCase(Locale.ENGLISH));
         writer.writeAttribute(Attribute.LOGIN_MODULE_STACK_REF.getLocalName(), entry.getLoginModuleStackHolderName());
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
