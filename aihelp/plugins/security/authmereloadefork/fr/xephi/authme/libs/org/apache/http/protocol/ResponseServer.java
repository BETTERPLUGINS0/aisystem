package fr.xephi.authme.libs.org.apache.http.protocol;

import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.HttpResponseInterceptor;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.io.IOException;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class ResponseServer implements HttpResponseInterceptor {
   private final String originServer;

   public ResponseServer(String originServer) {
      this.originServer = originServer;
   }

   public ResponseServer() {
      this((String)null);
   }

   public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
      Args.notNull(response, "HTTP response");
      if (!response.containsHeader("Server") && this.originServer != null) {
         response.addHeader("Server", this.originServer);
      }

   }
}
