package fr.xephi.authme.libs.org.apache.http.impl.execchain;

import fr.xephi.authme.libs.org.apache.commons.logging.Log;
import fr.xephi.authme.libs.org.apache.commons.logging.LogFactory;
import fr.xephi.authme.libs.org.apache.http.Header;
import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.NoHttpResponseException;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.client.HttpRequestRetryHandler;
import fr.xephi.authme.libs.org.apache.http.client.NonRepeatableRequestException;
import fr.xephi.authme.libs.org.apache.http.client.methods.CloseableHttpResponse;
import fr.xephi.authme.libs.org.apache.http.client.methods.HttpExecutionAware;
import fr.xephi.authme.libs.org.apache.http.client.methods.HttpRequestWrapper;
import fr.xephi.authme.libs.org.apache.http.client.protocol.HttpClientContext;
import fr.xephi.authme.libs.org.apache.http.conn.routing.HttpRoute;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.io.IOException;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL
)
public class RetryExec implements ClientExecChain {
   private final Log log = LogFactory.getLog(this.getClass());
   private final ClientExecChain requestExecutor;
   private final HttpRequestRetryHandler retryHandler;

   public RetryExec(ClientExecChain requestExecutor, HttpRequestRetryHandler retryHandler) {
      Args.notNull(requestExecutor, "HTTP request executor");
      Args.notNull(retryHandler, "HTTP request retry handler");
      this.requestExecutor = requestExecutor;
      this.retryHandler = retryHandler;
   }

   public CloseableHttpResponse execute(HttpRoute route, HttpRequestWrapper request, HttpClientContext context, HttpExecutionAware execAware) throws IOException, HttpException {
      Args.notNull(route, "HTTP route");
      Args.notNull(request, "HTTP request");
      Args.notNull(context, "HTTP context");
      Header[] origheaders = request.getAllHeaders();
      int execCount = 1;

      while(true) {
         try {
            return this.requestExecutor.execute(route, request, context, execAware);
         } catch (IOException var9) {
            if (execAware != null && execAware.isAborted()) {
               this.log.debug("Request has been aborted");
               throw var9;
            }

            if (!this.retryHandler.retryRequest(var9, execCount, context)) {
               if (var9 instanceof NoHttpResponseException) {
                  NoHttpResponseException updatedex = new NoHttpResponseException(route.getTargetHost().toHostString() + " failed to respond");
                  updatedex.setStackTrace(var9.getStackTrace());
                  throw updatedex;
               }

               throw var9;
            }

            if (this.log.isInfoEnabled()) {
               this.log.info("I/O exception (" + var9.getClass().getName() + ") caught when processing request to " + route + ": " + var9.getMessage());
            }

            if (this.log.isDebugEnabled()) {
               this.log.debug(var9.getMessage(), var9);
            }

            if (!RequestEntityProxy.isRepeatable(request)) {
               this.log.debug("Cannot retry non-repeatable request");
               throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity", var9);
            }

            request.setHeaders(origheaders);
            if (this.log.isInfoEnabled()) {
               this.log.info("Retrying request to " + route);
            }

            ++execCount;
         }
      }
   }
}
