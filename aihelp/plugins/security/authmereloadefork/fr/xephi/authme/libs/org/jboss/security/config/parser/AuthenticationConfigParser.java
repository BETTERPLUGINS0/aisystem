package fr.xephi.authme.libs.org.jboss.security.config.parser;

import fr.xephi.authme.libs.org.jboss.security.ClientLoginModule;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.auth.spi.BaseCertLoginModule;
import fr.xephi.authme.libs.org.jboss.security.auth.spi.CertRolesLoginModule;
import fr.xephi.authme.libs.org.jboss.security.auth.spi.DatabaseCertLoginModule;
import fr.xephi.authme.libs.org.jboss.security.auth.spi.DatabaseServerLoginModule;
import fr.xephi.authme.libs.org.jboss.security.auth.spi.IdentityLoginModule;
import fr.xephi.authme.libs.org.jboss.security.auth.spi.LdapExtLoginModule;
import fr.xephi.authme.libs.org.jboss.security.auth.spi.LdapLoginModule;
import fr.xephi.authme.libs.org.jboss.security.auth.spi.RoleMappingLoginModule;
import fr.xephi.authme.libs.org.jboss.security.auth.spi.RunAsLoginModule;
import fr.xephi.authme.libs.org.jboss.security.auth.spi.SimpleServerLoginModule;
import fr.xephi.authme.libs.org.jboss.security.auth.spi.UsersRolesLoginModule;
import fr.xephi.authme.libs.org.jboss.security.config.Element;
import fr.xephi.authme.libs.org.picketbox.datasource.security.CallerIdentityLoginModule;
import fr.xephi.authme.libs.org.picketbox.datasource.security.ConfiguredIdentityLoginModule;
import fr.xephi.authme.libs.org.picketbox.datasource.security.JaasSecurityDomainIdentityLoginModule;
import fr.xephi.authme.libs.org.picketbox.datasource.security.PBEIdentityLoginModule;
import fr.xephi.authme.libs.org.picketbox.datasource.security.SecureIdentityLoginModule;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class AuthenticationConfigParser implements XMLStreamConstants {
   public static final Map<String, String> loginModulesMap;

   public Set<AppConfigurationEntry> parse(XMLEventReader xmlEventReader) throws XMLStreamException {
      LinkedHashSet entries = new LinkedHashSet();

      while(xmlEventReader.hasNext()) {
         XMLEvent xmlEvent = xmlEventReader.peek();
         StartElement peekedStartElement = (StartElement)xmlEvent;
         AppConfigurationEntry entry = null;
         if (!"login-module".equals(StaxParserUtil.getStartElementName(peekedStartElement))) {
            break;
         }

         entry = this.getEntry(xmlEventReader);
         entries.add(entry);
      }

      return entries;
   }

   private AppConfigurationEntry getEntry(XMLEventReader xmlEventReader) throws XMLStreamException {
      XMLEvent xmlEvent = xmlEventReader.nextEvent();
      Map<String, Object> options = new HashMap();
      String codeName = null;
      LoginModuleControlFlag controlFlag = LoginModuleControlFlag.REQUIRED;
      StartElement loginModuleElement = (StartElement)xmlEvent;
      Iterator attrs = loginModuleElement.getAttributes();

      while(attrs.hasNext()) {
         Attribute attribute = (Attribute)attrs.next();
         QName attQName = attribute.getName();
         String attributeValue = StaxParserUtil.getAttributeValue(attribute);
         if ("code".equals(attQName.getLocalPart())) {
            codeName = attributeValue;
         } else if ("flag".equals(attQName.getLocalPart())) {
            controlFlag = this.getControlFlag(attributeValue);
         }
      }

      ModuleOptionParser moParser = new ModuleOptionParser();
      options.putAll(moParser.parse(xmlEventReader));
      return new AppConfigurationEntry(codeName, controlFlag, options);
   }

   private LoginModuleControlFlag getControlFlag(String flag) {
      if ("required".equalsIgnoreCase(flag)) {
         return LoginModuleControlFlag.REQUIRED;
      } else if ("sufficient".equalsIgnoreCase(flag)) {
         return LoginModuleControlFlag.SUFFICIENT;
      } else if ("optional".equalsIgnoreCase(flag)) {
         return LoginModuleControlFlag.OPTIONAL;
      } else if ("requisite".equalsIgnoreCase(flag)) {
         return LoginModuleControlFlag.REQUISITE;
      } else {
         throw PicketBoxMessages.MESSAGES.invalidControlFlag(flag);
      }
   }

   public Set<AppConfigurationEntry> parse(XMLStreamReader reader) throws XMLStreamException {
      LinkedHashSet entries = new LinkedHashSet();

      while(reader.hasNext() && reader.nextTag() != 2) {
         Element element = Element.forName(reader.getLocalName());
         AppConfigurationEntry entry = null;
         if (!element.equals(Element.LOGIN_MODULE)) {
            throw StaxParserUtil.unexpectedElement(reader);
         }

         entry = this.getEntry(reader);
         entries.add(entry);
      }

      return entries;
   }

   private AppConfigurationEntry getEntry(XMLStreamReader reader) throws XMLStreamException {
      Map<String, Object> options = new HashMap();
      String codeName = null;
      LoginModuleControlFlag controlFlag = LoginModuleControlFlag.REQUIRED;
      int count = reader.getAttributeCount();
      if (count < 2) {
         Set<fr.xephi.authme.libs.org.jboss.security.config.Attribute> set = new HashSet();
         set.add(fr.xephi.authme.libs.org.jboss.security.config.Attribute.CODE);
         set.add(fr.xephi.authme.libs.org.jboss.security.config.Attribute.FLAG);
         throw StaxParserUtil.missingRequired(reader, set);
      } else {
         for(int i = 0; i < count; ++i) {
            String value = reader.getAttributeValue(i);
            fr.xephi.authme.libs.org.jboss.security.config.Attribute attribute = fr.xephi.authme.libs.org.jboss.security.config.Attribute.forName(reader.getAttributeLocalName(i));
            switch(attribute) {
            case CODE:
               if (loginModulesMap.containsKey(value)) {
                  codeName = (String)loginModulesMap.get(value);
               } else {
                  codeName = value;
               }
               break;
            case FLAG:
               controlFlag = this.getControlFlag(value);
               break;
            default:
               throw StaxParserUtil.unexpectedAttribute(reader, i);
            }
         }

         ModuleOptionParser moParser = new ModuleOptionParser();
         options.putAll(moParser.parse(reader));
         return new AppConfigurationEntry(codeName, controlFlag, options);
      }
   }

   static {
      Map<String, String> loginModules = new HashMap();
      loginModules.put("Client", ClientLoginModule.class.getName());
      loginModules.put("Certificate", BaseCertLoginModule.class.getName());
      loginModules.put("CertificateRoles", CertRolesLoginModule.class.getName());
      loginModules.put("DatabaseCertificate", DatabaseCertLoginModule.class.getName());
      loginModules.put("Database", DatabaseServerLoginModule.class.getName());
      loginModules.put("Identity", IdentityLoginModule.class.getName());
      loginModules.put("Ldap", LdapLoginModule.class.getName());
      loginModules.put("LdapExtended", LdapExtLoginModule.class.getName());
      loginModules.put("RoleMapping", RoleMappingLoginModule.class.getName());
      loginModules.put("RunAs", RunAsLoginModule.class.getName());
      loginModules.put("Simple", SimpleServerLoginModule.class.getName());
      loginModules.put("UsersRoles", UsersRolesLoginModule.class.getName());
      loginModules.put("CallerIdentity", CallerIdentityLoginModule.class.getName());
      loginModules.put("ConfiguredIdentity", ConfiguredIdentityLoginModule.class.getName());
      loginModules.put("JaasSecurityDomainIdentity", JaasSecurityDomainIdentityLoginModule.class.getName());
      loginModules.put("PBEIdentity", PBEIdentityLoginModule.class.getName());
      loginModules.put("SecureIdentity", SecureIdentityLoginModule.class.getName());
      loginModulesMap = Collections.unmodifiableMap(loginModules);
   }
}
