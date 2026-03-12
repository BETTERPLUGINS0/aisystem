package fr.xephi.authme.message.updater;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.libs.ch.jalu.configme.properties.Property;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyReader;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.util.FileUtils;
import java.io.IOException;
import java.io.InputStream;

public class JarMessageSource {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(JarMessageSource.class);
   private final PropertyReader localJarMessages;
   private final PropertyReader defaultJarMessages;

   public JarMessageSource(String localJarPath, String defaultJarPath) {
      this.localJarMessages = localJarPath.equals(defaultJarPath) ? null : this.loadJarFile(localJarPath);
      this.defaultJarMessages = this.loadJarFile(defaultJarPath);
      if (this.defaultJarMessages == null) {
         throw new IllegalStateException("Default JAR file '" + defaultJarPath + "' could not be loaded");
      }
   }

   public String getMessageFromJar(Property<?> property) {
      String key = property.getPath();
      String message = getString(key, this.localJarMessages);
      return message == null ? getString(key, this.defaultJarMessages) : message;
   }

   private static String getString(String path, PropertyReader reader) {
      return reader == null ? null : reader.getString(path);
   }

   private MessageMigraterPropertyReader loadJarFile(String jarPath) {
      try {
         InputStream stream = FileUtils.getResourceFromJar(jarPath);

         MessageMigraterPropertyReader var3;
         label48: {
            try {
               if (stream == null) {
                  this.logger.debug("Could not load '" + jarPath + "' from JAR");
                  var3 = null;
                  break label48;
               }

               var3 = MessageMigraterPropertyReader.loadFromStream(stream);
            } catch (Throwable var6) {
               if (stream != null) {
                  try {
                     stream.close();
                  } catch (Throwable var5) {
                     var6.addSuppressed(var5);
                  }
               }

               throw var6;
            }

            if (stream != null) {
               stream.close();
            }

            return var3;
         }

         if (stream != null) {
            stream.close();
         }

         return var3;
      } catch (IOException var7) {
         this.logger.logException("Exception while handling JAR path '" + jarPath + "'", var7);
         return null;
      }
   }
}
