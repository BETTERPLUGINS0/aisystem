package fr.xephi.authme.libs.org.postgresql.xml;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public class NullErrorHandler implements ErrorHandler {
   public static final NullErrorHandler INSTANCE = new NullErrorHandler();

   public void error(SAXParseException e) {
   }

   public void fatalError(SAXParseException e) {
   }

   public void warning(SAXParseException e) {
   }
}
