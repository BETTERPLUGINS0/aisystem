package fr.xephi.authme.libs.org.jboss.security.config.parser;

import fr.xephi.authme.libs.org.jboss.security.auth.login.AuthenticationInfo;
import fr.xephi.authme.libs.org.jboss.security.auth.login.JASPIAuthenticationInfo;
import fr.xephi.authme.libs.org.jboss.security.config.ACLInfo;
import fr.xephi.authme.libs.org.jboss.security.config.ApplicationPolicy;
import fr.xephi.authme.libs.org.jboss.security.config.AuditInfo;
import fr.xephi.authme.libs.org.jboss.security.config.AuthorizationInfo;
import fr.xephi.authme.libs.org.jboss.security.config.Element;
import fr.xephi.authme.libs.org.jboss.security.config.IdentityTrustInfo;
import fr.xephi.authme.libs.org.jboss.security.config.MappingInfo;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingType;
import fr.xephi.authme.libs.org.jboss.security.mapping.config.MappingModuleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class ApplicationPolicyParser implements XMLStreamConstants {
   public List<ApplicationPolicy> parse(XMLEventReader xmlEventReader) throws XMLStreamException {
      ArrayList policies = new ArrayList();

      while(true) {
         while(xmlEventReader.hasNext()) {
            XMLEvent xmlEvent = xmlEventReader.nextEvent();
            int eventType = xmlEvent.getEventType();
            switch(eventType) {
            case 1:
               StartElement appPolicyElement = (StartElement)xmlEvent;
               String elementName = StaxParserUtil.getStartElementName(appPolicyElement);
               if (!"application-policy".equals(elementName)) {
                  throw StaxParserUtil.unexpectedElement(elementName, xmlEvent);
               }

               Iterator<Attribute> attrs = appPolicyElement.getAttributes();
               String extendsName = null;
               String appPolicyName = null;

               while(attrs.hasNext()) {
                  Attribute attribute = (Attribute)attrs.next();
                  QName attributeName = attribute.getName();
                  String attributeValue = StaxParserUtil.getAttributeValue(attribute);
                  if ("name".equals(attributeName.getLocalPart())) {
                     appPolicyName = attributeValue;
                  } else if ("extends".equals(attributeName.getLocalPart())) {
                     extendsName = attributeValue;
                  }
               }

               ApplicationPolicy applicationPolicy = new ApplicationPolicy(appPolicyName);
               if (extendsName != null) {
                  applicationPolicy.setBaseApplicationPolicyName(extendsName);
               }

               this.route(xmlEventReader, applicationPolicy);
               policies.add(applicationPolicy);
            }
         }

         return policies;
      }
   }

   private void route(XMLEventReader xmlEventReader, ApplicationPolicy appPolicy) throws XMLStreamException {
      while(true) {
         XMLEvent xmlEvent = xmlEventReader.peek();
         if (xmlEvent == null) {
            return;
         }

         StartElement startElement = xmlEvent.asStartElement();
         String elementName = StaxParserUtil.getStartElementName(startElement);
         Set entries;
         if ("authentication".equals(elementName)) {
            xmlEvent = xmlEventReader.nextEvent();
            AuthenticationConfigParser parser = new AuthenticationConfigParser();
            entries = parser.parse(xmlEventReader);
            AuthenticationInfo authInfo = new AuthenticationInfo();
            authInfo.setAppConfigurationEntry((List)(new ArrayList(entries)));
            appPolicy.setAuthenticationInfo(authInfo);
         } else if ("authentication-jaspi".equals(elementName)) {
            xmlEvent = xmlEventReader.nextEvent();
            AuthenticationJASPIConfigParser parser = new AuthenticationJASPIConfigParser();
            JASPIAuthenticationInfo authInfo = parser.parse(xmlEventReader);
            appPolicy.setAuthenticationInfo(authInfo);
         } else if ("authorization".equals(elementName)) {
            xmlEvent = xmlEventReader.nextEvent();
            AuthorizationConfigParser parser = new AuthorizationConfigParser();
            entries = parser.parse(xmlEventReader);
            AuthorizationInfo authInfo = new AuthorizationInfo(appPolicy.getName());
            authInfo.add(new ArrayList(entries));
            appPolicy.setAuthorizationInfo(authInfo);
         } else if ("acl".equals(elementName)) {
            xmlEvent = xmlEventReader.nextEvent();
            AclConfigParser parser = new AclConfigParser();
            entries = parser.parse(xmlEventReader);
            ACLInfo aclInfo = new ACLInfo(appPolicy.getName());
            aclInfo.add(new ArrayList(entries));
            appPolicy.setAclInfo(aclInfo);
         } else {
            List entries;
            MappingConfigParser parser;
            if ("rolemapping".equals(elementName)) {
               xmlEvent = xmlEventReader.nextEvent();
               parser = new MappingConfigParser();
               entries = parser.parse(xmlEventReader);
               MappingInfo mappingInfo = new MappingInfo(appPolicy.getName());
               mappingInfo.add(entries);
               appPolicy.setMappingInfo(MappingType.ROLE.toString(), mappingInfo);
            } else if ("mapping".equals(elementName)) {
               xmlEvent = xmlEventReader.nextEvent();
               parser = new MappingConfigParser();
               entries = parser.parse(xmlEventReader);
               Iterator i$ = entries.iterator();

               while(i$.hasNext()) {
                  MappingModuleEntry entry = (MappingModuleEntry)i$.next();
                  MappingInfo mappingInfo = new MappingInfo(appPolicy.getName());
                  mappingInfo.add(entry);
                  String moduleType = entry.getMappingModuleType();
                  appPolicy.setMappingInfo(moduleType, mappingInfo);
               }
            } else if ("audit".equals(elementName)) {
               xmlEvent = xmlEventReader.nextEvent();
               AuditConfigParser parser = new AuditConfigParser();
               entries = parser.parse(xmlEventReader);
               AuditInfo authInfo = new AuditInfo(appPolicy.getName());
               authInfo.add(entries);
               appPolicy.setAuditInfo(authInfo);
            } else {
               if (!"identity-trust".equals(elementName)) {
                  if ("application-policy".equals(elementName)) {
                     return;
                  }

                  throw StaxParserUtil.unexpectedElement(elementName, xmlEvent);
               }

               xmlEvent = xmlEventReader.nextEvent();
               IdentityTrustConfigParser parser = new IdentityTrustConfigParser();
               entries = parser.parse(xmlEventReader);
               IdentityTrustInfo authInfo = new IdentityTrustInfo(appPolicy.getName());
               authInfo.add(entries);
               appPolicy.setIdentityTrustInfo(authInfo);
            }
         }
      }
   }

   public List<ApplicationPolicy> parse(XMLStreamReader reader) throws XMLStreamException {
      ArrayList policies;
      ApplicationPolicy applicationPolicy;
      for(policies = null; reader.hasNext() && reader.nextTag() != 2; policies.add(applicationPolicy)) {
         Element element = Element.forName(reader.getLocalName());
         if (!element.equals(Element.APPLICATION_POLICY) && !element.equals(Element.SECURITY_DOMAIN)) {
            throw StaxParserUtil.unexpectedElement(reader);
         }

         int count = reader.getAttributeCount();
         if (count == 0) {
            throw StaxParserUtil.missingRequired(reader, Collections.singleton(fr.xephi.authme.libs.org.jboss.security.config.Attribute.NAME));
         }

         String name = null;
         String extendsName = null;

         for(int i = 0; i < count; ++i) {
            String value = reader.getAttributeValue(i);
            fr.xephi.authme.libs.org.jboss.security.config.Attribute attribute = fr.xephi.authme.libs.org.jboss.security.config.Attribute.forName(reader.getAttributeLocalName(i));
            switch(attribute) {
            case NAME:
               name = value;
               break;
            case EXTENDS:
               extendsName = value;
               break;
            default:
               throw StaxParserUtil.unexpectedAttribute(reader, i);
            }
         }

         if (name == null) {
            throw StaxParserUtil.missingRequired(reader, Collections.singleton(fr.xephi.authme.libs.org.jboss.security.config.Attribute.NAME));
         }

         applicationPolicy = new ApplicationPolicy(name);
         if (extendsName != null) {
            applicationPolicy.setBaseApplicationPolicyName(extendsName);
         }

         this.route(reader, applicationPolicy);
         if (policies == null) {
            policies = new ArrayList();
         }
      }

      return policies;
   }

   private void route(XMLStreamReader reader, ApplicationPolicy appPolicy) throws XMLStreamException {
      label29:
      while(reader.hasNext() && reader.nextTag() != 2) {
         Element element = Element.forName(reader.getLocalName());
         MappingConfigParser parser;
         List entries;
         Set entries;
         switch(element) {
         case ACL:
            AclConfigParser parser = new AclConfigParser();
            entries = parser.parse(reader);
            ACLInfo aclInfo = new ACLInfo(appPolicy.getName());
            aclInfo.add(new ArrayList(entries));
            appPolicy.setAclInfo(aclInfo);
            break;
         case AUDIT:
            AuditConfigParser parser = new AuditConfigParser();
            entries = parser.parse(reader);
            AuditInfo authInfo = new AuditInfo(appPolicy.getName());
            authInfo.add(entries);
            appPolicy.setAuditInfo(authInfo);
            break;
         case AUTHENTICATION:
            AuthenticationConfigParser parser = new AuthenticationConfigParser();
            entries = parser.parse(reader);
            AuthenticationInfo authInfo = new AuthenticationInfo(appPolicy.getName());
            authInfo.setAppConfigurationEntry((List)(new ArrayList(entries)));
            appPolicy.setAuthenticationInfo(authInfo);
            break;
         case AUTHENTICATION_JASPI:
            AuthenticationJASPIConfigParser parser = new AuthenticationJASPIConfigParser();
            JASPIAuthenticationInfo authInfo = parser.parse(reader);
            appPolicy.setAuthenticationInfo(authInfo);
            break;
         case AUTHORIZATION:
            AuthorizationConfigParser parser = new AuthorizationConfigParser();
            entries = parser.parse(reader);
            AuthorizationInfo authInfo = new AuthorizationInfo(appPolicy.getName());
            authInfo.add(new ArrayList(entries));
            appPolicy.setAuthorizationInfo(authInfo);
            break;
         case IDENTITY_TRUST:
            IdentityTrustConfigParser parser = new IdentityTrustConfigParser();
            entries = parser.parse(reader);
            IdentityTrustInfo authInfo = new IdentityTrustInfo(appPolicy.getName());
            authInfo.add(entries);
            appPolicy.setIdentityTrustInfo(authInfo);
            break;
         case MAPPING:
            parser = new MappingConfigParser();
            entries = parser.parse(reader);
            Iterator i$ = entries.iterator();

            while(true) {
               if (!i$.hasNext()) {
                  continue label29;
               }

               MappingModuleEntry entry = (MappingModuleEntry)i$.next();
               MappingInfo mappingInfo = new MappingInfo(appPolicy.getName());
               mappingInfo.add(entry);
               String moduleType = entry.getMappingModuleType();
               appPolicy.setMappingInfo(moduleType, mappingInfo);
            }
         case ROLE_MAPPING:
            parser = new MappingConfigParser();
            entries = parser.parse(reader);
            MappingInfo mappingInfo = new MappingInfo(appPolicy.getName());
            mappingInfo.add(entries);
            appPolicy.setMappingInfo(MappingType.ROLE.toString(), mappingInfo);
            break;
         default:
            throw StaxParserUtil.unexpectedElement(reader);
         }
      }

   }
}
