package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.addon.gssapi;

import com.sun.jna.Platform;

public class GssUtility {
   public static GssapiAuth getAuthenticationMethod() {
      try {
         if (Platform.isWindows()) {
            try {
               Class.forName("fr.xephi.authme.libs.waffle.windows.auth.impl.WindowsAuthProviderImpl");
               return new WindowsNativeSspiAuthentication();
            } catch (ClassNotFoundException var1) {
            }
         }
      } catch (Throwable var2) {
      }

      return new StandardGssapiAuthentication();
   }
}
