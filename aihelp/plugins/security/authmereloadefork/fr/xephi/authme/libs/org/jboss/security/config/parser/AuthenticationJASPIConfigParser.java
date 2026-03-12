package fr.xephi.authme.libs.org.jboss.security.config.parser;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.auth.container.config.AuthModuleEntry;
import fr.xephi.authme.libs.org.jboss.security.auth.login.JASPIAuthenticationInfo;
import fr.xephi.authme.libs.org.jboss.security.auth.login.LoginModuleStackHolder;
import fr.xephi.authme.libs.org.jboss.security.config.ControlFlag;
import fr.xephi.authme.libs.org.jboss.security.config.Element;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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

public class AuthenticationJASPIConfigParser implements XMLStreamConstants {
   public JASPIAuthenticationInfo parse(XMLEventReader xmlEventReader) throws XMLStreamException {
      JASPIAuthenticationInfo authInfo = new JASPIAuthenticationInfo();
      HashMap holders = new HashMap();

      while(xmlEventReader.hasNext()) {
         XMLEvent xmlEvent = xmlEventReader.peek();
         StartElement peekedStartElement = (StartElement)xmlEvent;
         String peekedStartElementName = StaxParserUtil.getStartElementName(peekedStartElement);
         if ("login-module-stack".equals(peekedStartElementName)) {
            StartElement lmshEvent = (StartElement)xmlEventReader.nextEvent();
            Attribute nameAttribute = (Attribute)lmshEvent.getAttributes().next();
            String nameAttributeValue = StaxParserUtil.getAttributeValue(nameAttribute);
            LoginModuleStackHolder holder = new LoginModuleStackHolder(nameAttributeValue, (List)null);
            holders.put(nameAttributeValue, holder);
            authInfo.add(holder);

            while(true) {
               xmlEvent = xmlEventReader.peek();
               peekedStartElement = (StartElement)xmlEvent;
               peekedStartElementName = StaxParserUtil.getStartElementName(peekedStartElement);
               if (!"login-module".equals(peekedStartElementName)) {
                  break;
               }

               holder.addAppConfigurationEntry(this.getJAASEntry(xmlEventReader));
            }
         } else {
            if (!"auth-module".equals(peekedStartElementName)) {
               break;
            }

            AuthModuleEntry entry = this.getJaspiEntry(xmlEventReader);
            String stackHolderRefName = entry.getLoginModuleStackHolderName();
            if (stackHolderRefName != null) {
               if (!holders.containsKey(stackHolderRefName)) {
                  throw PicketBoxMessages.MESSAGES.invalidLoginModuleStackRef(stackHolderRefName);
               }

               entry.setLoginModuleStackHolder((LoginModuleStackHolder)holders.get(stackHolderRefName));
            }

            authInfo.add(entry);
         }
      }

      return authInfo;
   }

   private AppConfigurationEntry getJAASEntry(XMLEventReader xmlEventReader) throws XMLStreamException {
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

   private AuthModuleEntry getJaspiEntry(XMLEventReader xmlEventReader) throws XMLStreamException {
      XMLEvent xmlEvent = xmlEventReader.nextEvent();
      Map<String, Object> options = new HashMap();
      String codeName = null;
      String loginModuleStackRef = null;
      ControlFlag flag = ControlFlag.REQUIRED;
      StartElement authModuleElement = (StartElement)xmlEvent;
      Iterator attrs = authModuleElement.getAttributes();

      while(attrs.hasNext()) {
         Attribute attribute = (Attribute)attrs.next();
         QName attQName = attribute.getName();
         String attributeValue = StaxParserUtil.getAttributeValue(attribute);
         if ("code".equals(attQName.getLocalPart())) {
            codeName = attributeValue;
         } else if ("flag".equals(attQName.getLocalPart())) {
            flag = ControlFlag.valueOf(attributeValue);
         } else if ("login-module-stack-ref".equals(attQName.getLocalPart())) {
            loginModuleStackRef = attributeValue;
         }
      }

      ModuleOptionParser moParser = new ModuleOptionParser();
      options.putAll(moParser.parse(xmlEventReader));
      AuthModuleEntry entry = new AuthModuleEntry(codeName, options, loginModuleStackRef);
      entry.setControlFlag(flag);
      return entry;
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

   public JASPIAuthenticationInfo parse(XMLStreamReader reader) throws XMLStreamException {
      JASPIAuthenticationInfo authInfo = new JASPIAuthenticationInfo();
      HashMap holders = new HashMap();

      label53:
      while(reader.hasNext() && reader.nextTag() != 2) {
         Element element = Element.forName(reader.getLocalName());
         switch(element) {
         case LOGIN_MODULE_STACK:
            int count = reader.getAttributeCount();
            if (count < 1) {
               throw StaxParserUtil.missingRequired(reader, Collections.singleton(fr.xephi.authme.libs.org.jboss.security.config.Attribute.NAME));
            }

            LoginModuleStackHolder holder = null;
            int i = 0;

            while(i < count) {
               String value = reader.getAttributeValue(i);
               fr.xephi.authme.libs.org.jboss.security.config.Attribute attribute = fr.xephi.authme.libs.org.jboss.security.config.Attribute.forName(reader.getAttributeLocalName(i));
               switch(attribute) {
               case NAME:
                  holder = new LoginModuleStackHolder(value, (List)null);
                  holders.put(value, holder);
                  authInfo.add(holder);
                  ++i;
                  break;
               default:
                  throw StaxParserUtil.unexpectedAttribute(reader, i);
               }
            }

            while(true) {
               if (!reader.hasNext() || reader.nextTag() == 2) {
                  continue label53;
               }

               Element element2 = Element.forName(reader.getLocalName());
               if (element2.equals(Element.LOGIN_MODULE)) {
                  holder.addAppConfigurationEntry(this.getJAASEntry(reader));
               }
            }
         case AUTH_MODULE:
            AuthModuleEntry entry = this.getJaspiEntry(reader);
            String stackHolderRefName = entry.getLoginModuleStackHolderName();
            if (stackHolderRefName != null) {
               if (!holders.containsKey(stackHolderRefName)) {
                  throw PicketBoxMessages.MESSAGES.invalidLoginModuleStackRef(stackHolderRefName);
               }

               entry.setLoginModuleStackHolder((LoginModuleStackHolder)holders.get(stackHolderRefName));
            }

            authInfo.add(entry);
            break;
         default:
            throw StaxParserUtil.unexpectedElement(reader);
         }
      }

      return authInfo;
   }

   private AppConfigurationEntry getJAASEntry(XMLStreamReader reader) throws XMLStreamException {
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
               if (AuthenticationConfigParser.loginModulesMap.containsKey(value)) {
                  codeName = (String)AuthenticationConfigParser.loginModulesMap.get(value);
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

   private AuthModuleEntry getJaspiEntry(XMLStreamReader reader) throws XMLStreamException {
      Map<String, Object> options = new HashMap();
      String codeName = null;
      String loginModuleStackRef = null;
      ControlFlag flag = ControlFlag.REQUIRED;
      int count = reader.getAttributeCount();
      if (count == 0) {
         throw StaxParserUtil.missingRequired(reader, Collections.singleton(fr.xephi.authme.libs.org.jboss.security.config.Attribute.CODE));
      } else {
         for(int i = 0; i < count; ++i) {
            String value = reader.getAttributeValue(i);
            fr.xephi.authme.libs.org.jboss.security.config.Attribute attribute = fr.xephi.authme.libs.org.jboss.security.config.Attribute.forName(reader.getAttributeLocalName(i));
            switch(attribute) {
            case CODE:
               codeName = value;
               break;
            case FLAG:
               flag = ControlFlag.valueOf(value);
               break;
            case LOGIN_MODULE_STACK_REF:
               loginModuleStackRef = value;
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
            AuthModuleEntry entry = new AuthModuleEntry(codeName, options, loginModuleStackRef);
            entry.setControlFlag(flag);
            return entry;
         }
      }
   }
}
