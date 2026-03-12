package fr.xephi.authme.libs.org.jboss.security.config.parser;

import fr.xephi.authme.libs.org.jboss.security.auth.spi.Users;
import java.util.Iterator;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class UsersConfigParser implements ParserNamespaceSupport {
   private static final String NAMESPACE_URI = "urn:jboss:user-roles";

   public Users parse(XMLEventReader xmlEventReader) throws XMLStreamException {
      Users users = new Users();
      XMLEvent xmlEvent = null;

      while(true) {
         while(xmlEventReader.hasNext()) {
            xmlEvent = xmlEventReader.peek();
            if (xmlEvent instanceof StartElement) {
               StartElement se = (StartElement)xmlEvent;
               if ("module-option".equals(StaxParserUtil.getStartElementName(se))) {
                  return users;
               }
            }

            if (xmlEvent instanceof EndElement) {
               xmlEvent = xmlEventReader.nextEvent();
            } else {
               xmlEvent = xmlEventReader.nextEvent();
               Users.User user = new Users.User();
               StartElement peekedStartElement = (StartElement)xmlEvent;
               Iterator attribs = peekedStartElement.getAttributes();

               while(attribs.hasNext()) {
                  Attribute attrib = (Attribute)attribs.next();
                  if ("name".equals(attrib.getName().getLocalPart())) {
                     user.setName(attrib.getValue());
                  } else if ("password".equals(attrib.getName().getLocalPart())) {
                     user.setPassword(attrib.getValue());
                  } else if ("encoding".equals(attrib.getName().getLocalPart())) {
                     user.setEncoding(attrib.getValue());
                  }
               }

               for(xmlEvent = xmlEventReader.peek(); xmlEvent != null && xmlEvent.getEventType() == 1; xmlEvent = xmlEventReader.peek()) {
                  StartElement roleElement = (StartElement)xmlEvent;
                  if (!"role".equals(roleElement.getName().getLocalPart())) {
                     break;
                  }

                  xmlEvent = xmlEventReader.nextEvent();
                  Iterator<Attribute> roleAttribs = roleElement.getAttributes();
                  String roleName = null;
                  String groupName = "Roles";

                  while(roleAttribs.hasNext()) {
                     Attribute roleAttribute = (Attribute)roleAttribs.next();
                     String attributeValue = StaxParserUtil.getAttributeValue(roleAttribute);
                     if ("name".equals(roleAttribute.getName().getLocalPart())) {
                        roleName = attributeValue;
                     } else if ("group".equals(roleAttribute.getName().getLocalPart())) {
                        groupName = attributeValue;
                     }
                  }

                  if (roleName != null) {
                     user.addRole(roleName, groupName);
                  }
               }

               users.addUser(user);
            }
         }

         return users;
      }
   }

   public boolean supports(String namespaceURI) {
      return "urn:jboss:user-roles".equals(namespaceURI);
   }
}
