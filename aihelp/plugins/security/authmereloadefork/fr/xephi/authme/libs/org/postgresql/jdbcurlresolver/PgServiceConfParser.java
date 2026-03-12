package fr.xephi.authme.libs.org.postgresql.jdbcurlresolver;

import fr.xephi.authme.libs.org.postgresql.PGEnvironment;
import fr.xephi.authme.libs.org.postgresql.PGProperty;
import fr.xephi.authme.libs.org.postgresql.util.OSUtil;
import fr.xephi.authme.libs.org.postgresql.util.PGPropertyUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PgServiceConfParser {
   private static final Logger LOGGER = Logger.getLogger(PgServiceConfParser.class.getName());
   private final String serviceName;
   private boolean ignoreIfOpenFails = true;

   private PgServiceConfParser(String serviceName) {
      this.serviceName = serviceName;
   }

   @Nullable
   public static Properties getServiceProperties(String serviceName) {
      PgServiceConfParser pgServiceConfParser = new PgServiceConfParser(serviceName);
      return pgServiceConfParser.findServiceDescription();
   }

   @Nullable
   private Properties findServiceDescription() {
      String resourceName = this.findPgServiceConfResourceName();
      if (resourceName == null) {
         return null;
      } else {
         Properties result = null;

         try {
            InputStream inputStream = this.openInputStream(resourceName);

            try {
               result = this.parseInputStream(inputStream);
            } catch (Throwable var7) {
               if (inputStream != null) {
                  try {
                     inputStream.close();
                  } catch (Throwable var6) {
                     var7.addSuppressed(var6);
                  }
               }

               throw var7;
            }

            if (inputStream != null) {
               inputStream.close();
            }
         } catch (IOException var8) {
            Level level = this.ignoreIfOpenFails ? Level.FINE : Level.WARNING;
            LOGGER.log(level, "Failed to handle resource [{0}] with error [{1}]", new Object[]{resourceName, var8.getMessage()});
         }

         return result;
      }
   }

   private InputStream openInputStream(String resourceName) throws IOException {
      try {
         URL url = new URL(resourceName);
         return url.openStream();
      } catch (MalformedURLException var4) {
         File file = new File(resourceName);
         return new FileInputStream(file);
      }
   }

   @Nullable
   private String findPgServiceConfResourceName() {
      String pgServiceConfFileDefaultName = PGEnvironment.PGSERVICEFILE.getDefaultValue();
      String envVariableName = PGEnvironment.ORG_POSTGRESQL_PGSERVICEFILE.getName();
      String pgSysconfDir = System.getProperty(envVariableName);
      if (pgSysconfDir != null && !pgSysconfDir.trim().isEmpty()) {
         this.ignoreIfOpenFails = false;
         LOGGER.log(Level.FINE, "Value [{0}] selected from property [{1}]", new Object[]{pgSysconfDir, envVariableName});
         return pgSysconfDir;
      } else {
         envVariableName = PGEnvironment.PGSERVICEFILE.getName();
         pgSysconfDir = (String)System.getenv().get(envVariableName);
         if (pgSysconfDir != null && !pgSysconfDir.trim().isEmpty()) {
            this.ignoreIfOpenFails = false;
            LOGGER.log(Level.FINE, "Value [{0}] selected from environment variable [{1}]", new Object[]{pgSysconfDir, envVariableName});
            return pgSysconfDir;
         } else {
            envVariableName = "." + pgServiceConfFileDefaultName;
            File resourceFile = new File(OSUtil.getUserConfigRootDirectory(), envVariableName);
            if (resourceFile.canRead()) {
               LOGGER.log(Level.FINE, "Value [{0}] selected because file exist in user home directory", new Object[]{resourceFile.getAbsolutePath()});
               return resourceFile.getAbsolutePath();
            } else {
               envVariableName = PGEnvironment.PGSYSCONFDIR.getName();
               pgSysconfDir = (String)System.getenv().get(envVariableName);
               if (pgSysconfDir != null && !pgSysconfDir.trim().isEmpty()) {
                  String resourceName = pgSysconfDir + File.separator + pgServiceConfFileDefaultName;
                  LOGGER.log(Level.FINE, "Value [{0}] selected using environment variable [{1}]", new Object[]{resourceName, envVariableName});
                  return resourceName;
               } else {
                  LOGGER.log(Level.FINE, "Value for resource [{0}] not found", pgServiceConfFileDefaultName);
                  return null;
               }
            }
         }
      }
   }

   @Nullable
   private Properties parseInputStream(InputStream inputStream) throws IOException {
      Set<String> allowedServiceKeys = (Set)Arrays.stream(PGProperty.values()).map(PGProperty::getName).map(PGPropertyUtil::translatePGPropertyToPGService).collect(Collectors.toSet());
      Properties result = new Properties();
      boolean isFound = false;
      InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

      Object var14;
      label105: {
         String key;
         label109: {
            try {
               BufferedReader br;
               label101: {
                  label110: {
                     br = new BufferedReader(reader);

                     try {
                        int lineNumber = 0;

                        label96:
                        while(true) {
                           while(true) {
                              String originalLine;
                              String line;
                              do {
                                 do {
                                    if ((originalLine = br.readLine()) == null) {
                                       break label101;
                                    }

                                    ++lineNumber;
                                    line = originalLine.trim();
                                 } while(line.isEmpty());
                              } while(line.startsWith("#"));

                              int indexOfEqualSign = line.indexOf("=");
                              if (line.startsWith("[") && line.endsWith("]")) {
                                 if (isFound) {
                                    break label101;
                                 }

                                 key = line.substring(1, line.length() - 1);
                                 if (this.serviceName.equals(key)) {
                                    isFound = true;
                                 }
                              } else if (isFound) {
                                 if (indexOfEqualSign <= 1) {
                                    LOGGER.log(Level.WARNING, "Not valid line: line number [{0}], value [{1}]", new Object[]{lineNumber, originalLine});
                                    key = null;
                                    break label96;
                                 }

                                 key = line.substring(0, indexOfEqualSign);
                                 String value = line.substring(indexOfEqualSign + 1);
                                 if (!allowedServiceKeys.contains(key)) {
                                    String allowedValuesCommaSeparated = (String)allowedServiceKeys.stream().sorted().collect(Collectors.joining(","));
                                    LOGGER.log(Level.SEVERE, "Got invalid key: line number [{0}], value [{1}], allowed values [{2}]", new Object[]{lineNumber, originalLine, allowedValuesCommaSeparated});
                                    var14 = null;
                                    break label110;
                                 }

                                 if (!value.isEmpty()) {
                                    result.putIfAbsent(PGPropertyUtil.translatePGServiceToPGProperty(key), value);
                                 }
                              }
                           }
                        }
                     } catch (Throwable var17) {
                        try {
                           br.close();
                        } catch (Throwable var16) {
                           var17.addSuppressed(var16);
                        }

                        throw var17;
                     }

                     br.close();
                     break label109;
                  }

                  br.close();
                  break label105;
               }

               br.close();
            } catch (Throwable var18) {
               try {
                  reader.close();
               } catch (Throwable var15) {
                  var18.addSuppressed(var15);
               }

               throw var18;
            }

            reader.close();
            return isFound ? result : null;
         }

         reader.close();
         return key;
      }

      reader.close();
      return (Properties)var14;
   }
}
