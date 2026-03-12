package fr.xephi.authme.libs.org.jboss.security.config.parser;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.config.ApplicationPolicy;
import fr.xephi.authme.libs.org.jboss.security.config.ApplicationPolicyRegistration;
import fr.xephi.authme.libs.org.jboss.security.config.Element;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import javax.security.auth.login.Configuration;
import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class StaxBasedConfigParser implements XMLStreamConstants {
   private String schemaFile = "schema/security-config_5_0.xsd";

   public void schemaValidate(InputStream configStream) throws SAXException, IOException {
      Validator validator = this.schemaValidator();
      Source xmlSource = new StreamSource(configStream);
      validator.validate(xmlSource);
   }

   public void parse(InputStream configStream) throws XMLStreamException, SAXException, IOException {
      Configuration config = Configuration.getConfiguration();
      if (!(config instanceof ApplicationPolicyRegistration)) {
         throw PicketBoxMessages.MESSAGES.invalidType(ApplicationPolicyRegistration.class.getName());
      } else {
         ApplicationPolicyRegistration appPolicyRegistration = (ApplicationPolicyRegistration)config;
         XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
         XMLEventReader xmlEventReader = this.getXMLEventReader(configStream);
         xmlEventReader = xmlInputFactory.createFilteredReader(xmlEventReader, new EventFilter() {
            public boolean accept(XMLEvent xmlEvent) {
               return xmlEvent.isStartElement();
            }
         });

         while(true) {
            while(xmlEventReader.hasNext()) {
               XMLEvent xmlEvent = xmlEventReader.nextEvent();
               int eventType = xmlEvent.getEventType();
               switch(eventType) {
               case 1:
                  StartElement policyConfigElement = (StartElement)xmlEvent;
                  String elementName = StaxParserUtil.getStartElementName(policyConfigElement);
                  if (!"policy".equals(elementName)) {
                     throw StaxParserUtil.unexpectedElement(elementName, xmlEvent);
                  }

                  ApplicationPolicyParser appPolicyParser = new ApplicationPolicyParser();
                  List<ApplicationPolicy> appPolicies = appPolicyParser.parse(xmlEventReader);
                  Iterator i$ = appPolicies.iterator();

                  while(i$.hasNext()) {
                     ApplicationPolicy appPolicy = (ApplicationPolicy)i$.next();
                     appPolicyRegistration.addApplicationPolicy(appPolicy.getName(), appPolicy);
                  }
               }
            }

            return;
         }
      }
   }

   public void parse2(InputStream configStream) throws XMLStreamException {
      Configuration config = Configuration.getConfiguration();
      if (!(config instanceof ApplicationPolicyRegistration)) {
         throw PicketBoxMessages.MESSAGES.invalidType(ApplicationPolicyRegistration.class.getName());
      } else {
         ApplicationPolicyRegistration appPolicyRegistration = (ApplicationPolicyRegistration)config;
         XMLStreamReader reader = this.getXMLStreamReader(configStream);

         while(reader.hasNext() && reader.nextTag() != 2) {
            Element element = Element.forName(reader.getLocalName());
            if (!element.equals(Element.POLICY)) {
               throw StaxParserUtil.unexpectedElement(reader);
            }

            ApplicationPolicyParser appPolicyParser = new ApplicationPolicyParser();
            List<ApplicationPolicy> appPolicies = appPolicyParser.parse(reader);
            Iterator i$ = appPolicies.iterator();

            while(i$.hasNext()) {
               ApplicationPolicy appPolicy = (ApplicationPolicy)i$.next();
               appPolicyRegistration.addApplicationPolicy(appPolicy.getName(), appPolicy);
            }

            if (reader.isEndElement()) {
               break;
            }
         }

      }
   }

   private Validator schemaValidator() {
      try {
         ClassLoader tcl = SecurityActions.getContextClassLoader();
         URL schemaURL = tcl.getResource(this.schemaFile);
         if (schemaURL == null) {
            throw PicketBoxMessages.MESSAGES.unableToFindSchema(this.schemaFile);
         } else {
            SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            Schema schemaGrammar = schemaFactory.newSchema(schemaURL);
            Validator schemaValidator = schemaGrammar.newValidator();
            schemaValidator.setErrorHandler(new ErrorHandler() {
               public void error(SAXParseException ex) throws SAXException {
                  this.logException(ex);
               }

               public void fatalError(SAXParseException ex) throws SAXException {
                  this.logException(ex);
               }

               public void warning(SAXParseException ex) throws SAXException {
                  this.logException(ex);
               }

               private void logException(SAXParseException sax) {
                  StringBuilder builder = new StringBuilder();
                  if (PicketBoxLogger.LOGGER.isTraceEnabled()) {
                     builder.append("[").append(sax.getLineNumber()).append(",").append(sax.getColumnNumber()).append("]");
                     builder.append(":").append(sax.getLocalizedMessage());
                     PicketBoxLogger.LOGGER.trace(builder.toString());
                  }

               }
            });
            return schemaValidator;
         }
      } catch (Exception var6) {
         throw new RuntimeException(var6);
      }
   }

   private XMLEventReader getXMLEventReader(InputStream is) {
      XMLInputFactory xmlInputFactory = null;
      XMLEventReader xmlEventReader = null;

      try {
         xmlInputFactory = XMLInputFactory.newInstance();
         xmlInputFactory.setProperty("javax.xml.stream.isReplacingEntityReferences", Boolean.TRUE);
         xmlInputFactory.setProperty("javax.xml.stream.isSupportingExternalEntities", Boolean.FALSE);
         xmlInputFactory.setProperty("javax.xml.stream.isNamespaceAware", Boolean.TRUE);
         xmlInputFactory.setProperty("javax.xml.stream.isCoalescing", Boolean.TRUE);
         xmlEventReader = xmlInputFactory.createXMLEventReader(is);
         return xmlEventReader;
      } catch (Exception var5) {
         throw new RuntimeException(var5);
      }
   }

   private XMLStreamReader getXMLStreamReader(InputStream is) {
      XMLInputFactory xmlInputFactory = null;
      XMLStreamReader xmlStreamReader = null;

      try {
         xmlInputFactory = XMLInputFactory.newInstance();
         xmlInputFactory.setProperty("javax.xml.stream.isReplacingEntityReferences", Boolean.TRUE);
         xmlInputFactory.setProperty("javax.xml.stream.isSupportingExternalEntities", Boolean.FALSE);
         xmlInputFactory.setProperty("javax.xml.stream.isNamespaceAware", Boolean.TRUE);
         xmlInputFactory.setProperty("javax.xml.stream.isCoalescing", Boolean.TRUE);
         xmlStreamReader = xmlInputFactory.createXMLStreamReader(is);
         return xmlStreamReader;
      } catch (Exception var5) {
         throw new RuntimeException(var5);
      }
   }
}
