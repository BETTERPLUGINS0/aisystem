package fr.xephi.authme.libs.org.apache.http.impl.client;

import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpHost;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.client.ClientProtocolException;
import fr.xephi.authme.libs.org.apache.http.client.config.RequestConfig;
import fr.xephi.authme.libs.org.apache.http.client.methods.CloseableHttpResponse;
import fr.xephi.authme.libs.org.apache.http.client.methods.Configurable;
import fr.xephi.authme.libs.org.apache.http.client.methods.HttpExecutionAware;
import fr.xephi.authme.libs.org.apache.http.client.methods.HttpRequestWrapper;
import fr.xephi.authme.libs.org.apache.http.client.protocol.HttpClientContext;
import fr.xephi.authme.libs.org.apache.http.conn.ClientConnectionManager;
import fr.xephi.authme.libs.org.apache.http.conn.ClientConnectionRequest;
import fr.xephi.authme.libs.org.apache.http.conn.HttpClientConnectionManager;
import fr.xephi.authme.libs.org.apache.http.conn.ManagedClientConnection;
import fr.xephi.authme.libs.org.apache.http.conn.routing.HttpRoute;
import fr.xephi.authme.libs.org.apache.http.conn.scheme.SchemeRegistry;
import fr.xephi.authme.libs.org.apache.http.impl.DefaultConnectionReuseStrategy;
import fr.xephi.authme.libs.org.apache.http.impl.execchain.MinimalClientExec;
import fr.xephi.authme.libs.org.apache.http.params.BasicHttpParams;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import fr.xephi.authme.libs.org.apache.http.protocol.BasicHttpContext;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpRequestExecutor;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Contract(
   threading = ThreadingBehavior.SAFE_CONDITIONAL
)
class MinimalHttpClient extends CloseableHttpClient {
   private final HttpClientConnectionManager connManager;
   private final MinimalClientExec requestExecutor;
   private final HttpParams params;

   public MinimalHttpClient(HttpClientConnectionManager connManager) {
      this.connManager = (HttpClientConnectionManager)Args.notNull(connManager, "HTTP connection manager");
      this.requestExecutor = new MinimalClientExec(new HttpRequestExecutor(), connManager, DefaultConnectionReuseStrategy.INSTANCE, DefaultConnectionKeepAliveStrategy.INSTANCE);
      this.params = new BasicHttpParams();
   }

   protected CloseableHttpResponse doExecute(HttpHost target, HttpRequest request, HttpContext context) throws IOException, ClientProtocolException {
      Args.notNull(target, "Target host");
      Args.notNull(request, "HTTP request");
      HttpExecutionAware execAware = null;
      if (request instanceof HttpExecutionAware) {
         execAware = (HttpExecutionAware)request;
      }

      try {
         HttpRequestWrapper wrapper = HttpRequestWrapper.wrap(request);
         HttpClientContext localcontext = HttpClientContext.adapt((HttpContext)(context != null ? context : new BasicHttpContext()));
         HttpRoute route = new HttpRoute(target);
         RequestConfig config = null;
         if (request instanceof Configurable) {
            config = ((Configurable)request).getConfig();
         }

         if (config != null) {
            localcontext.setRequestConfig(config);
         }

         return this.requestExecutor.execute(route, wrapper, localcontext, execAware);
      } catch (HttpException var9) {
         throw new ClientProtocolException(var9);
      }
   }

   public HttpParams getParams() {
      return this.params;
   }

   public void close() {
      this.connManager.shutdown();
   }

   public ClientConnectionManager getConnectionManager() {
      return new ClientConnectionManager() {
         public void shutdown() {
            MinimalHttpClient.this.connManager.shutdown();
         }

         public ClientConnectionRequest requestConnection(HttpRoute route, Object state) {
            throw new UnsupportedOperationException();
         }

         public void releaseConnection(ManagedClientConnection conn, long validDuration, TimeUnit timeUnit) {
            throw new UnsupportedOperationException();
         }

         public SchemeRegistry getSchemeRegistry() {
            throw new UnsupportedOperationException();
         }

         public void closeIdleConnections(long idletime, TimeUnit timeUnit) {
            MinimalHttpClient.this.connManager.closeIdleConnections(idletime, timeUnit);
         }

         public void closeExpiredConnections() {
            MinimalHttpClient.this.connManager.closeExpiredConnections();
         }
      };
   }
}
