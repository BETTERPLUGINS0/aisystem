package fr.xephi.authme.libs.org.apache.http.client.protocol;

import fr.xephi.authme.libs.org.apache.commons.logging.Log;
import fr.xephi.authme.libs.org.apache.commons.logging.LogFactory;
import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpHost;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.HttpResponseInterceptor;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.auth.AuthScheme;
import fr.xephi.authme.libs.org.apache.http.auth.AuthState;
import fr.xephi.authme.libs.org.apache.http.client.AuthCache;
import fr.xephi.authme.libs.org.apache.http.conn.scheme.Scheme;
import fr.xephi.authme.libs.org.apache.http.conn.scheme.SchemeRegistry;
import fr.xephi.authme.libs.org.apache.http.impl.client.BasicAuthCache;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.io.IOException;

/** @deprecated */
@Deprecated
@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class ResponseAuthCache implements HttpResponseInterceptor {
   private final Log log = LogFactory.getLog(this.getClass());

   public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
      Args.notNull(response, "HTTP request");
      Args.notNull(context, "HTTP context");
      AuthCache authCache = (AuthCache)context.getAttribute("http.auth.auth-cache");
      HttpHost target = (HttpHost)context.getAttribute("http.target_host");
      AuthState targetState = (AuthState)context.getAttribute("http.auth.target-scope");
      if (target != null && targetState != null) {
         if (this.log.isDebugEnabled()) {
            this.log.debug("Target auth state: " + targetState.getState());
         }

         if (this.isCachable(targetState)) {
            SchemeRegistry schemeRegistry = (SchemeRegistry)context.getAttribute("http.scheme-registry");
            if (target.getPort() < 0) {
               Scheme scheme = schemeRegistry.getScheme(target);
               target = new HttpHost(target.getHostName(), scheme.resolvePort(target.getPort()), target.getSchemeName());
            }

            if (authCache == null) {
               authCache = new BasicAuthCache();
               context.setAttribute("http.auth.auth-cache", authCache);
            }

            switch(targetState.getState()) {
            case CHALLENGED:
               this.cache((AuthCache)authCache, target, targetState.getAuthScheme());
               break;
            case FAILURE:
               this.uncache((AuthCache)authCache, target, targetState.getAuthScheme());
            }
         }
      }

      HttpHost proxy = (HttpHost)context.getAttribute("http.proxy_host");
      AuthState proxyState = (AuthState)context.getAttribute("http.auth.proxy-scope");
      if (proxy != null && proxyState != null) {
         if (this.log.isDebugEnabled()) {
            this.log.debug("Proxy auth state: " + proxyState.getState());
         }

         if (this.isCachable(proxyState)) {
            if (authCache == null) {
               authCache = new BasicAuthCache();
               context.setAttribute("http.auth.auth-cache", authCache);
            }

            switch(proxyState.getState()) {
            case CHALLENGED:
               this.cache((AuthCache)authCache, proxy, proxyState.getAuthScheme());
               break;
            case FAILURE:
               this.uncache((AuthCache)authCache, proxy, proxyState.getAuthScheme());
            }
         }
      }

   }

   private boolean isCachable(AuthState authState) {
      AuthScheme authScheme = authState.getAuthScheme();
      if (authScheme != null && authScheme.isComplete()) {
         String schemeName = authScheme.getSchemeName();
         return schemeName.equalsIgnoreCase("Basic") || schemeName.equalsIgnoreCase("Digest");
      } else {
         return false;
      }
   }

   private void cache(AuthCache authCache, HttpHost host, AuthScheme authScheme) {
      if (this.log.isDebugEnabled()) {
         this.log.debug("Caching '" + authScheme.getSchemeName() + "' auth scheme for " + host);
      }

      authCache.put(host, authScheme);
   }

   private void uncache(AuthCache authCache, HttpHost host, AuthScheme authScheme) {
      if (this.log.isDebugEnabled()) {
         this.log.debug("Removing from cache '" + authScheme.getSchemeName() + "' auth scheme for " + host);
      }

      authCache.remove(host);
   }
}
