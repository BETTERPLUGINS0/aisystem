package fr.xephi.authme.libs.org.jboss.security.config.parser;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

public interface ParserNamespaceSupport {
   Object parse(XMLEventReader var1) throws XMLStreamException;

   boolean supports(String var1);
}
