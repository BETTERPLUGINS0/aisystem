package fr.xephi.authme.libs.org.apache.http.impl.client;

import fr.xephi.authme.libs.org.apache.commons.logging.Log;
import fr.xephi.authme.libs.org.apache.commons.logging.LogFactory;
import fr.xephi.authme.libs.org.apache.http.HttpHost;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.client.HttpClient;
import fr.xephi.authme.libs.org.apache.http.client.ResponseHandler;
import fr.xephi.authme.libs.org.apache.http.client.ServiceUnavailableRetryStrategy;
import fr.xephi.authme.libs.org.apache.http.client.methods.HttpUriRequest;
import fr.xephi.authme.libs.org.apache.http.conn.ClientConnectionManager;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import fr.xephi.authme.libs.org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;

/** @deprecated */
@Deprecated
@Contract(
   threading = ThreadingBehavior.SAFE_CONDITIONAL
)
public class AutoRetryHttpClient implements HttpClient {
   private final HttpClient backend;
   private final ServiceUnavailableRetryStrategy retryStrategy;
   private final Log log;

   public AutoRetryHttpClient(HttpClient client, ServiceUnavailableRetryStrategy retryStrategy) {
      this.log = LogFactory.getLog(this.getClass());
      Args.notNull(client, "HttpClient");
      Args.notNull(retryStrategy, "ServiceUnavailableRetryStrategy");
      this.backend = client;
      this.retryStrategy = retryStrategy;
   }

   public AutoRetryHttpClient() {
      this(new DefaultHttpClient(), new DefaultServiceUnavailableRetryStrategy());
   }

   public AutoRetryHttpClient(ServiceUnavailableRetryStrategy config) {
      this(new DefaultHttpClient(), config);
   }

   public AutoRetryHttpClient(HttpClient client) {
      this(client, new DefaultServiceUnavailableRetryStrategy());
   }

   public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException {
      HttpContext defaultContext = null;
      return this.execute((HttpHost)target, (HttpRequest)request, (HttpContext)defaultContext);
   }

   public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler) throws IOException {
      return this.execute(target, request, responseHandler, (HttpContext)null);
   }

   public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException {
      HttpResponse resp = this.execute(target, request, context);
      return responseHandler.handleResponse(resp);
   }

   public HttpResponse execute(HttpUriRequest request) throws IOException {
      HttpContext context = null;
      return this.execute((HttpUriRequest)request, (HttpContext)context);
   }

   public HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException {
      URI uri = request.getURI();
      HttpHost httpHost = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
      return this.execute((HttpHost)httpHost, (HttpRequest)request, (HttpContext)context);
   }

   public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException {
      return this.execute((HttpUriRequest)request, (ResponseHandler)responseHandler, (HttpContext)null);
   }

   public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException {
      HttpResponse resp = this.execute(request, context);
      return responseHandler.handleResponse(resp);
   }

   public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException {
      int c = 1;

      while(true) {
         HttpResponse response = this.backend.execute(target, request, context);

         try {
            if (!this.retryStrategy.retryRequest(response, c, context)) {
               return response;
            }

            EntityUtils.consume(response.getEntity());
            long nextInterval = this.retryStrategy.getRetryInterval();

            try {
               this.log.trace("Wait for " + nextInterval);
               Thread.sleep(nextInterval);
            } catch (InterruptedException var10) {
               Thread.currentThread().interrupt();
               throw new InterruptedIOException();
            }
         } catch (RuntimeException var11) {
            try {
               EntityUtils.consume(response.getEntity());
            } catch (IOException var9) {
               this.log.warn("I/O error consuming response content", var9);
            }

            throw var11;
         }

         ++c;
      }
   }

   public ClientConnectionManager getConnectionManager() {
      return this.backend.getConnectionManager();
   }

   public HttpParams getParams() {
      return this.backend.getParams();
   }
}
