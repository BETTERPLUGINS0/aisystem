package fr.xephi.authme.libs.org.apache.http.impl.client;

import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.client.ServiceUnavailableRetryStrategy;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import fr.xephi.authme.libs.org.apache.http.util.Args;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class DefaultServiceUnavailableRetryStrategy implements ServiceUnavailableRetryStrategy {
   private final int maxRetries;
   private final long retryInterval;

   public DefaultServiceUnavailableRetryStrategy(int maxRetries, int retryInterval) {
      Args.positive(maxRetries, "Max retries");
      Args.positive(retryInterval, "Retry interval");
      this.maxRetries = maxRetries;
      this.retryInterval = (long)retryInterval;
   }

   public DefaultServiceUnavailableRetryStrategy() {
      this(1, 1000);
   }

   public boolean retryRequest(HttpResponse response, int executionCount, HttpContext context) {
      return executionCount <= this.maxRetries && response.getStatusLine().getStatusCode() == 503;
   }

   public long getRetryInterval() {
      return this.retryInterval;
   }
}
