package fr.xephi.authme.libs.org.apache.http.impl.client;

import fr.xephi.authme.libs.org.apache.http.Header;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.impl.DefaultConnectionReuseStrategy;
import fr.xephi.authme.libs.org.apache.http.message.BasicHeaderIterator;
import fr.xephi.authme.libs.org.apache.http.message.BasicTokenIterator;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;

public class DefaultClientConnectionReuseStrategy extends DefaultConnectionReuseStrategy {
   public static final DefaultClientConnectionReuseStrategy INSTANCE = new DefaultClientConnectionReuseStrategy();

   public boolean keepAlive(HttpResponse response, HttpContext context) {
      HttpRequest request = (HttpRequest)context.getAttribute("http.request");
      if (request != null) {
         Header[] connHeaders = request.getHeaders("Connection");
         if (connHeaders.length != 0) {
            BasicTokenIterator ti = new BasicTokenIterator(new BasicHeaderIterator(connHeaders, (String)null));

            while(ti.hasNext()) {
               String token = ti.nextToken();
               if ("Close".equalsIgnoreCase(token)) {
                  return false;
               }
            }
         }
      }

      return super.keepAlive(response, context);
   }
}
