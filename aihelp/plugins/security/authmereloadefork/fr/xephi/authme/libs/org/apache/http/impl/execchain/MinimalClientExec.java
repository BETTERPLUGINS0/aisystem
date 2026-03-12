package fr.xephi.authme.libs.org.apache.http.impl.execchain;

import fr.xephi.authme.libs.org.apache.commons.logging.Log;
import fr.xephi.authme.libs.org.apache.commons.logging.LogFactory;
import fr.xephi.authme.libs.org.apache.http.ConnectionReuseStrategy;
import fr.xephi.authme.libs.org.apache.http.HttpClientConnection;
import fr.xephi.authme.libs.org.apache.http.HttpEntity;
import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpHost;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.HttpRequestInterceptor;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.ProtocolException;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.client.config.RequestConfig;
import fr.xephi.authme.libs.org.apache.http.client.methods.CloseableHttpResponse;
import fr.xephi.authme.libs.org.apache.http.client.methods.HttpExecutionAware;
import fr.xephi.authme.libs.org.apache.http.client.methods.HttpRequestWrapper;
import fr.xephi.authme.libs.org.apache.http.client.methods.HttpUriRequest;
import fr.xephi.authme.libs.org.apache.http.client.protocol.HttpClientContext;
import fr.xephi.authme.libs.org.apache.http.client.protocol.RequestClientConnControl;
import fr.xephi.authme.libs.org.apache.http.client.utils.URIUtils;
import fr.xephi.authme.libs.org.apache.http.conn.ConnectionKeepAliveStrategy;
import fr.xephi.authme.libs.org.apache.http.conn.ConnectionRequest;
import fr.xephi.authme.libs.org.apache.http.conn.HttpClientConnectionManager;
import fr.xephi.authme.libs.org.apache.http.conn.routing.HttpRoute;
import fr.xephi.authme.libs.org.apache.http.impl.conn.ConnectionShutdownException;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpProcessor;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpRequestExecutor;
import fr.xephi.authme.libs.org.apache.http.protocol.ImmutableHttpProcessor;
import fr.xephi.authme.libs.org.apache.http.protocol.RequestContent;
import fr.xephi.authme.libs.org.apache.http.protocol.RequestTargetHost;
import fr.xephi.authme.libs.org.apache.http.protocol.RequestUserAgent;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import fr.xephi.authme.libs.org.apache.http.util.VersionInfo;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL
)
public class MinimalClientExec implements ClientExecChain {
   private final Log log = LogFactory.getLog(this.getClass());
   private final HttpRequestExecutor requestExecutor;
   private final HttpClientConnectionManager connManager;
   private final ConnectionReuseStrategy reuseStrategy;
   private final ConnectionKeepAliveStrategy keepAliveStrategy;
   private final HttpProcessor httpProcessor;

   public MinimalClientExec(HttpRequestExecutor requestExecutor, HttpClientConnectionManager connManager, ConnectionReuseStrategy reuseStrategy, ConnectionKeepAliveStrategy keepAliveStrategy) {
      Args.notNull(requestExecutor, "HTTP request executor");
      Args.notNull(connManager, "Client connection manager");
      Args.notNull(reuseStrategy, "Connection reuse strategy");
      Args.notNull(keepAliveStrategy, "Connection keep alive strategy");
      this.httpProcessor = new ImmutableHttpProcessor(new HttpRequestInterceptor[]{new RequestContent(), new RequestTargetHost(), new RequestClientConnControl(), new RequestUserAgent(VersionInfo.getUserAgent("Apache-HttpClient", "fr.xephi.authme.libs.org.apache.http.client", this.getClass()))});
      this.requestExecutor = requestExecutor;
      this.connManager = connManager;
      this.reuseStrategy = reuseStrategy;
      this.keepAliveStrategy = keepAliveStrategy;
   }

   static void rewriteRequestURI(HttpRequestWrapper request, HttpRoute route, boolean normalizeUri) throws ProtocolException {
      try {
         URI uri = request.getURI();
         if (uri != null) {
            if (uri.isAbsolute()) {
               uri = URIUtils.rewriteURI(uri, (HttpHost)null, normalizeUri ? URIUtils.DROP_FRAGMENT_AND_NORMALIZE : URIUtils.DROP_FRAGMENT);
            } else {
               uri = URIUtils.rewriteURI(uri);
            }

            request.setURI(uri);
         }

      } catch (URISyntaxException var4) {
         throw new ProtocolException("Invalid URI: " + request.getRequestLine().getUri(), var4);
      }
   }

   public CloseableHttpResponse execute(HttpRoute route, HttpRequestWrapper request, HttpClientContext context, HttpExecutionAware execAware) throws IOException, HttpException {
      Args.notNull(route, "HTTP route");
      Args.notNull(request, "HTTP request");
      Args.notNull(context, "HTTP context");
      rewriteRequestURI(request, route, context.getRequestConfig().isNormalizeUri());
      ConnectionRequest connRequest = this.connManager.requestConnection(route, (Object)null);
      if (execAware != null) {
         if (execAware.isAborted()) {
            connRequest.cancel();
            throw new RequestAbortedException("Request aborted");
         }

         execAware.setCancellable(connRequest);
      }

      RequestConfig config = context.getRequestConfig();

      HttpClientConnection managedConn;
      try {
         int timeout = config.getConnectionRequestTimeout();
         managedConn = connRequest.get(timeout > 0 ? (long)timeout : 0L, TimeUnit.MILLISECONDS);
      } catch (InterruptedException var20) {
         Thread.currentThread().interrupt();
         throw new RequestAbortedException("Request aborted", var20);
      } catch (ExecutionException var21) {
         Throwable cause = var21.getCause();
         if (cause == null) {
            cause = var21;
         }

         throw new RequestAbortedException("Request execution failed", (Throwable)cause);
      }

      ConnectionHolder releaseTrigger = new ConnectionHolder(this.log, this.connManager, managedConn);

      try {
         if (execAware != null) {
            if (execAware.isAborted()) {
               releaseTrigger.close();
               throw new RequestAbortedException("Request aborted");
            }

            execAware.setCancellable(releaseTrigger);
         }

         int timeout;
         if (!managedConn.isOpen()) {
            timeout = config.getConnectTimeout();
            this.connManager.connect(managedConn, route, timeout > 0 ? timeout : 0, context);
            this.connManager.routeComplete(managedConn, route, context);
         }

         timeout = config.getSocketTimeout();
         if (timeout >= 0) {
            managedConn.setSocketTimeout(timeout);
         }

         HttpHost target = null;
         HttpRequest original = request.getOriginal();
         if (original instanceof HttpUriRequest) {
            URI uri = ((HttpUriRequest)original).getURI();
            if (uri.isAbsolute()) {
               target = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
            }
         }

         if (target == null) {
            target = route.getTargetHost();
         }

         context.setAttribute("http.target_host", target);
         context.setAttribute("http.request", request);
         context.setAttribute("http.connection", managedConn);
         context.setAttribute("http.route", route);
         this.httpProcessor.process(request, context);
         HttpResponse response = this.requestExecutor.execute(request, managedConn, context);
         this.httpProcessor.process(response, context);
         if (this.reuseStrategy.keepAlive(response, context)) {
            long duration = this.keepAliveStrategy.getKeepAliveDuration(response, context);
            releaseTrigger.setValidFor(duration, TimeUnit.MILLISECONDS);
            releaseTrigger.markReusable();
         } else {
            releaseTrigger.markNonReusable();
         }

         HttpEntity entity = response.getEntity();
         if (entity != null && entity.isStreaming()) {
            return new HttpResponseProxy(response, releaseTrigger);
         } else {
            releaseTrigger.releaseConnection();
            return new HttpResponseProxy(response, (ConnectionHolder)null);
         }
      } catch (ConnectionShutdownException var15) {
         InterruptedIOException ioex = new InterruptedIOException("Connection has been shut down");
         ioex.initCause(var15);
         throw ioex;
      } catch (HttpException var16) {
         releaseTrigger.abortConnection();
         throw var16;
      } catch (IOException var17) {
         releaseTrigger.abortConnection();
         throw var17;
      } catch (RuntimeException var18) {
         releaseTrigger.abortConnection();
         throw var18;
      } catch (Error var19) {
         this.connManager.shutdown();
         throw var19;
      }
   }
}
