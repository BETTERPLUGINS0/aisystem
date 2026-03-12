package fr.xephi.authme.libs.org.mariadb.jdbc.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class VersionFactory {
   private static volatile Version instance = null;

   public static Version getInstance() {
      if (instance == null) {
         Class var0 = VersionFactory.class;
         synchronized(VersionFactory.class) {
            if (instance == null) {
               String tmpVersion = "5.5.0";

               try {
                  InputStream inputStream = Version.class.getClassLoader().getResourceAsStream("mariadb.properties");

                  try {
                     if (inputStream == null) {
                        System.out.println("property file 'mariadb.properties' not found in the classpath");
                     } else {
                        Properties prop = new Properties();
                        prop.load(inputStream);
                        tmpVersion = prop.getProperty("version");
                     }
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
                  var8.printStackTrace();
               }

               instance = new Version(tmpVersion);
            }
         }
      }

      return instance;
   }
}
