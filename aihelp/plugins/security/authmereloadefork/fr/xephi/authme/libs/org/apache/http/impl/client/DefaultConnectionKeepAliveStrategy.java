package fr.xephi.authme.libs.org.apache.http.impl.client;

import fr.xephi.authme.libs.org.apache.http.HeaderElement;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.conn.ConnectionKeepAliveStrategy;
import fr.xephi.authme.libs.org.apache.http.message.BasicHeaderElementIterator;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import fr.xephi.authme.libs.org.apache.http.util.Args;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class DefaultConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {
   public static final DefaultConnectionKeepAliveStrategy INSTANCE = new DefaultConnectionKeepAliveStrategy();

   public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
      Args.notNull(response, "HTTP response");
      BasicHeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator("Keep-Alive"));

      while(true) {
         String param;
         String value;
         do {
            do {
               if (!it.hasNext()) {
                  return -1L;
               }

               HeaderElement he = it.nextElement();
               param = he.getName();
               value = he.getValue();
            } while(value == null);
         } while(!param.equalsIgnoreCase("timeout"));

         try {
            return Long.parseLong(value) * 1000L;
         } catch (NumberFormatException var8) {
         }
      }
   }
}
