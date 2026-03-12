package fr.xephi.authme.libs.org.apache.http.impl.client;

import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class StandardHttpRequestRetryHandler extends DefaultHttpRequestRetryHandler {
   private final Map<String, Boolean> idempotentMethods;

   public StandardHttpRequestRetryHandler(int retryCount, boolean requestSentRetryEnabled) {
      super(retryCount, requestSentRetryEnabled);
      this.idempotentMethods = new ConcurrentHashMap();
      this.idempotentMethods.put("GET", Boolean.TRUE);
      this.idempotentMethods.put("HEAD", Boolean.TRUE);
      this.idempotentMethods.put("PUT", Boolean.TRUE);
      this.idempotentMethods.put("DELETE", Boolean.TRUE);
      this.idempotentMethods.put("OPTIONS", Boolean.TRUE);
      this.idempotentMethods.put("TRACE", Boolean.TRUE);
   }

   public StandardHttpRequestRetryHandler() {
      this(3, false);
   }

   protected boolean handleAsIdempotent(HttpRequest request) {
      String method = request.getRequestLine().getMethod().toUpperCase(Locale.ROOT);
      Boolean b = (Boolean)this.idempotentMethods.get(method);
      return b != null && b;
   }
}
