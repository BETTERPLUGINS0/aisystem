package ac.grim.grimac.utils.common;

import ac.grim.grimac.utils.anticheat.LogUtil;
import java.io.InputStream;
import java.util.Properties;
import lombok.Generated;

public final class PropertiesUtil {
   public static Properties readProperties(Class<?> clazz, String path) {
      Properties properties = new Properties();

      try {
         InputStream inputStream = clazz.getClassLoader().getResourceAsStream(path);

         try {
            if (inputStream == null) {
               throw new RuntimeException("Cannot find properties file: " + path);
            }

            properties.load(inputStream);
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
      } catch (Exception var8) {
         LogUtil.error((Throwable)var8);
      }

      return properties;
   }

   public static String getPropertyOrElse(Properties properties, String key, String defaultValue) {
      return properties.getProperty(key, defaultValue);
   }

   @Generated
   private PropertiesUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
