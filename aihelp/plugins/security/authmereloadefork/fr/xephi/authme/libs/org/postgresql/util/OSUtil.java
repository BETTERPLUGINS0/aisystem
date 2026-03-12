package fr.xephi.authme.libs.org.postgresql.util;

import java.io.File;
import java.util.Locale;

public class OSUtil {
   public static boolean isWindows() {
      return System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("windows");
   }

   public static String getUserConfigRootDirectory() {
      return isWindows() ? System.getenv("APPDATA") + File.separator + "postgresql" : System.getProperty("user.home");
   }
}
