package fr.xephi.authme.libs.org.apache.http.impl.execchain;

import fr.xephi.authme.libs.org.apache.commons.logging.Log;
import fr.xephi.authme.libs.org.apache.commons.logging.LogFactory;
import fr.xephi.authme.libs.org.apache.http.HttpEntityEnclosingRequest;
import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpHost;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.ProtocolException;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.auth.AuthState;
import fr.xephi.authme.libs.org.apache.http.client.RedirectException;
import fr.xephi.authme.libs.org.apache.http.client.RedirectStrategy;
import fr.xephi.authme.libs.org.apache.http.client.config.RequestConfig;
import fr.xephi.authme.libs.org.apache.http.client.methods.CloseableHttpResponse;
import fr.xephi.authme.libs.org.apache.http.client.methods.HttpExecutionAware;
import fr.xephi.authme.libs.org.apache.http.client.methods.HttpRequestWrapper;
import fr.xephi.authme.libs.org.apache.http.client.protocol.HttpClientContext;
import fr.xephi.authme.libs.org.apache.http.client.utils.URIUtils;
import fr.xephi.authme.libs.org.apache.http.conn.routing.HttpRoute;
import fr.xephi.authme.libs.org.apache.http.conn.routing.HttpRoutePlanner;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import fr.xephi.authme.libs.org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL
)
public class RedirectExec implements ClientExecChain {
   private final Log log = LogFactory.getLog(this.getClass());
   private final ClientExecChain requestExecutor;
   private final RedirectStrategy redirectStrategy;
   private final HttpRoutePlanner routePlanner;

   public RedirectExec(ClientExecChain requestExecutor, HttpRoutePlanner routePlanner, RedirectStrategy redirectStrategy) {
      Args.notNull(requestExecutor, "HTTP client request executor");
      Args.notNull(routePlanner, "HTTP route planner");
      Args.notNull(redirectStrategy, "HTTP redirect strategy");
      this.requestExecutor = requestExecutor;
      this.routePlanner = routePlanner;
      this.redirectStrategy = redirectStrategy;
   }

   public CloseableHttpResponse execute(HttpRoute route, HttpRequestWrapper request, HttpClientContext context, HttpExecutionAware execAware) throws IOException, HttpException {
      Args.notNull(route, "HTTP route");
      Args.notNull(request, "HTTP request");
      Args.notNull(context, "HTTP context");
      List<URI> redirectLocations = context.getRedirectLocations();
      if (redirectLocations != null) {
         redirectLocations.clear();
      }

      RequestConfig config = context.getRequestConfig();
      int maxRedirects = config.getMaxRedirects() > 0 ? config.getMaxRedirects() : 50;
      HttpRoute currentRoute = route;
      HttpRequestWrapper currentRequest = request;
      int redirectCount = 0;

      while(true) {
         CloseableHttpResponse response = this.requestExecutor.execute(currentRoute, currentRequest, context, execAware);

         try {
            if (!config.isRedirectsEnabled() || !this.redirectStrategy.isRedirected(currentRequest.getOriginal(), response, context)) {
               return response;
            }

            if (!RequestEntityProxy.isRepeatable(currentRequest)) {
               if (this.log.isDebugEnabled()) {
                  this.log.debug("Cannot redirect non-repeatable request");
               }

               return response;
            }

            if (redirectCount >= maxRedirects) {
               throw new RedirectException("Maximum redirects (" + maxRedirects + ") exceeded");
            }

            ++redirectCount;
            HttpRequest redirect = this.redirectStrategy.getRedirect(currentRequest.getOriginal(), response, context);
            if (!redirect.headerIterator().hasNext()) {
               HttpRequest original = request.getOriginal();
               redirect.setHeaders(original.getAllHeaders());
            }

            currentRequest = HttpRequestWrapper.wrap(redirect);
            if (currentRequest instanceof HttpEntityEnclosingRequest) {
               RequestEntityProxy.enhance((HttpEntityEnclosingRequest)currentRequest);
            }

            URI uri = currentRequest.getURI();
            HttpHost newTarget = URIUtils.extractHost(uri);
            if (newTarget == null) {
               throw new ProtocolException("Redirect URI does not specify a valid host name: " + uri);
            }

            if (!currentRoute.getTargetHost().equals(newTarget)) {
               AuthState targetAuthState = context.getTargetAuthState();
               if (targetAuthState != null) {
                  this.log.debug("Resetting target auth state");
                  targetAuthState.reset();
               }

               AuthState proxyAuthState = context.getProxyAuthState();
               if (proxyAuthState != null && proxyAuthState.isConnectionBased()) {
                  this.log.debug("Resetting proxy auth state");
                  proxyAuthState.reset();
               }
            }

            currentRoute = this.routePlanner.determineRoute(newTarget, currentRequest, context);
            if (this.log.isDebugEnabled()) {
               this.log.debug("Redirecting to '" + uri + "' via " + currentRoute);
            }

            EntityUtils.consume(response.getEntity());
            response.close();
         } catch (RuntimeException var25) {
            response.close();
            throw var25;
         } catch (IOException var26) {
            response.close();
            throw var26;
         } catch (HttpException var27) {
            try {
               EntityUtils.consume(response.getEntity());
            } catch (IOException var23) {
               this.log.debug("I/O error while releasing connection", var23);
            } finally {
               response.close();
            }

            throw var27;
         }
      }
   }
}
