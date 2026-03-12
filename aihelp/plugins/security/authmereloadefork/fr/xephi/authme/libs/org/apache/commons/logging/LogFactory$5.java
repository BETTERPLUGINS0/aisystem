package fr.xephi.authme.libs.org.apache.commons.logging;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.PrivilegedAction;
import java.util.Properties;

final class LogFactory$5 implements PrivilegedAction {
   // $FF: synthetic field
   private final URL val$url;

   LogFactory$5(URL var1) {
      this.val$url = var1;
   }

   public Object run() {
      InputStream stream = null;

      Properties var4;
      try {
         URLConnection connection = this.val$url.openConnection();
         connection.setUseCaches(false);
         stream = connection.getInputStream();
         if (stream == null) {
            return null;
         }

         Properties props = new Properties();
         props.load(stream);
         stream.close();
         stream = null;
         var4 = props;
      } catch (IOException var15) {
         if (LogFactory.isDiagnosticsEnabled()) {
            LogFactory.access$000("Unable to read URL " + this.val$url);
         }

         return null;
      } finally {
         if (stream != null) {
            try {
               stream.close();
            } catch (IOException var14) {
               if (LogFactory.isDiagnosticsEnabled()) {
                  LogFactory.access$000("Unable to close stream for URL " + this.val$url);
               }
            }
         }

      }

      return var4;
   }
}
