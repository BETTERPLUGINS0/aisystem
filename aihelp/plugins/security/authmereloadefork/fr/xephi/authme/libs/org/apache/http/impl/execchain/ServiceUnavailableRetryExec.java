package fr.xephi.authme.libs.org.apache.http.impl.execchain;

import fr.xephi.authme.libs.org.apache.commons.logging.Log;
import fr.xephi.authme.libs.org.apache.commons.logging.LogFactory;
import fr.xephi.authme.libs.org.apache.http.Header;
import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.client.ServiceUnavailableRetryStrategy;
import fr.xephi.authme.libs.org.apache.http.client.methods.CloseableHttpResponse;
import fr.xephi.authme.libs.org.apache.http.client.methods.HttpExecutionAware;
import fr.xephi.authme.libs.org.apache.http.client.methods.HttpRequestWrapper;
import fr.xephi.authme.libs.org.apache.http.client.protocol.HttpClientContext;
import fr.xephi.authme.libs.org.apache.http.conn.routing.HttpRoute;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.io.IOException;
import java.io.InterruptedIOException;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL
)
public class ServiceUnavailableRetryExec implements ClientExecChain {
   private final Log log = LogFactory.getLog(this.getClass());
   private final ClientExecChain requestExecutor;
   private final ServiceUnavailableRetryStrategy retryStrategy;

   public ServiceUnavailableRetryExec(ClientExecChain requestExecutor, ServiceUnavailableRetryStrategy retryStrategy) {
      Args.notNull(requestExecutor, "HTTP request executor");
      Args.notNull(retryStrategy, "Retry strategy");
      this.requestExecutor = requestExecutor;
      this.retryStrategy = retryStrategy;
   }

   public CloseableHttpResponse execute(HttpRoute route, HttpRequestWrapper request, HttpClientContext context, HttpExecutionAware execAware) throws IOException, HttpException {
      Header[] origheaders = request.getAllHeaders();
      int c = 1;

      while(true) {
         CloseableHttpResponse response = this.requestExecutor.execute(route, request, context, execAware);

         try {
            if (!this.retryStrategy.retryRequest(response, c, context) || !RequestEntityProxy.isRepeatable(request)) {
               return response;
            }

            response.close();
            long nextInterval = this.retryStrategy.getRetryInterval();
            if (nextInterval > 0L) {
               try {
                  this.log.trace("Wait for " + nextInterval);
                  Thread.sleep(nextInterval);
               } catch (InterruptedException var11) {
                  Thread.currentThread().interrupt();
                  throw new InterruptedIOException();
               }
            }

            request.setHeaders(origheaders);
         } catch (RuntimeException var12) {
            response.close();
            throw var12;
         }

         ++c;
      }
   }
}
