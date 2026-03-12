package fr.xephi.authme.libs.org.postgresql.util;

import fr.xephi.authme.libs.org.postgresql.PGProperty;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PGPropertyUtil {
   private static final Logger LOGGER = Logger.getLogger(PGPropertyUtil.class.getName());

   @Nullable
   private static Integer convertPgPortToInt(String portStr) {
      try {
         int port = Integer.parseInt(portStr);
         if (port >= 1 && port <= 65535) {
            return port;
         } else {
            LOGGER.log(Level.WARNING, "JDBC URL port: {0} not valid (1:65535) ", portStr);
            return null;
         }
      } catch (NumberFormatException var2) {
         LOGGER.log(Level.WARNING, "JDBC URL invalid port number: {0}", portStr);
         return null;
      }
   }

   public static boolean propertiesConsistencyCheck(Properties properties) {
      String hosts = PGProperty.PG_HOST.getOrDefault(properties);
      if (hosts == null) {
         LOGGER.log(Level.WARNING, "Property [{0}] can not be null", PGProperty.PG_HOST.getName());
         return false;
      } else {
         String ports = PGProperty.PG_PORT.getOrDefault(properties);
         if (ports == null) {
            LOGGER.log(Level.WARNING, "Property [{0}] can not be null", PGProperty.PG_PORT.getName());
            return false;
         } else {
            String[] var3 = ports.split(",");
            int portCount = var3.length;

            for(int var5 = 0; var5 < portCount; ++var5) {
               String portStr = var3[var5];
               if (convertPgPortToInt(portStr) == null) {
                  return false;
               }
            }

            int hostCount = hosts.split(",").length;
            portCount = ports.split(",").length;
            if (hostCount != portCount) {
               LOGGER.log(Level.WARNING, "Properties [{0}] [{1}] must have same amount of values", new Object[]{PGProperty.PG_HOST.getName(), PGProperty.PG_PORT.getName()});
               LOGGER.log(Level.WARNING, "Property [{0}] ; value [{1}] ; count [{2}]", new Object[]{PGProperty.PG_HOST.getName(), hosts, hostCount});
               LOGGER.log(Level.WARNING, "Property [{0}] ; value [{1}] ; count [{2}]", new Object[]{PGProperty.PG_PORT.getName(), ports, portCount});
               return false;
            } else {
               return true;
            }
         }
      }
   }

   public static String translatePGServiceToPGProperty(String serviceKey) {
      String testKey = "PG" + serviceKey.toUpperCase(Locale.ROOT);
      return !PGProperty.PG_HOST.getName().equals(testKey) && !PGProperty.PG_PORT.getName().equals(testKey) && !PGProperty.PG_DBNAME.getName().equals(testKey) ? serviceKey : testKey;
   }

   public static String translatePGPropertyToPGService(String propertyKey) {
      return !PGProperty.PG_HOST.getName().equals(propertyKey) && !PGProperty.PG_PORT.getName().equals(propertyKey) && !PGProperty.PG_DBNAME.getName().equals(propertyKey) ? propertyKey : propertyKey.substring(2).toLowerCase(Locale.ROOT);
   }
}
