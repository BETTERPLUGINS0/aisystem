package fr.xephi.authme.libs.org.picketbox.config;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.config.parser.StaxBasedConfigParser;
import fr.xephi.authme.libs.org.picketbox.exceptions.ConfigurationParsingException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

public class PicketBoxConfiguration {
   public void load(String configFileName) throws ConfigurationParsingException {
      if (configFileName == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("configFileName");
      } else {
         InputStream configStream = null;

         try {
            configStream = this.loadStream(configFileName);
            this.load(configStream);
         } finally {
            this.safeClose(configStream);
         }

      }
   }

   public void load(InputStream configStream) throws ConfigurationParsingException {
      if (configStream == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("configStream");
      } else {
         StaxBasedConfigParser parser = new StaxBasedConfigParser();

         try {
            parser.parse(configStream);
         } catch (XMLStreamException var4) {
            throw new ConfigurationParsingException(var4);
         } catch (SAXException var5) {
            throw new ConfigurationParsingException(var5);
         } catch (IOException var6) {
            throw new ConfigurationParsingException(var6);
         }
      }
   }

   private InputStream loadStream(String configFileName) {
      InputStream configStream = null;

      try {
         ClassLoader tcl = SecurityActions.getContextClassLoader();
         configStream = tcl.getResourceAsStream(configFileName);
      } catch (Exception var6) {
         PicketBoxLogger.LOGGER.errorLoadingConfigFile(configFileName, var6);
      }

      try {
         if (configStream == null) {
            configStream = SecurityActions.getClassLoader(this.getClass()).getResourceAsStream(configFileName);
         }
      } catch (Exception var5) {
         PicketBoxLogger.LOGGER.errorLoadingConfigFile(configFileName, var5);
      }

      try {
         if (configStream == null) {
            URL url = new URL(configFileName);
            configStream = url.openStream();
         }
      } catch (Exception var4) {
         PicketBoxLogger.LOGGER.errorLoadingConfigFile(configFileName, var4);
      }

      return configStream;
   }

   private void safeClose(InputStream fis) {
      try {
         if (fis != null) {
            fis.close();
         }
      } catch (Exception var3) {
      }

   }
}
