package fr.xephi.authme.libs.org.apache.http.impl.client;

import fr.xephi.authme.libs.org.apache.commons.logging.Log;
import fr.xephi.authme.libs.org.apache.commons.logging.LogFactory;
import fr.xephi.authme.libs.org.apache.http.Header;
import fr.xephi.authme.libs.org.apache.http.HttpHost;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.ProtocolException;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.client.CircularRedirectException;
import fr.xephi.authme.libs.org.apache.http.client.RedirectHandler;
import fr.xephi.authme.libs.org.apache.http.client.utils.URIUtils;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import fr.xephi.authme.libs.org.apache.http.util.Asserts;
import java.net.URI;
import java.net.URISyntaxException;

/** @deprecated */
@Deprecated
@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class DefaultRedirectHandler implements RedirectHandler {
   private final Log log = LogFactory.getLog(this.getClass());
   private static final String REDIRECT_LOCATIONS = "http.protocol.redirect-locations";

   public boolean isRedirectRequested(HttpResponse response, HttpContext context) {
      Args.notNull(response, "HTTP response");
      int statusCode = response.getStatusLine().getStatusCode();
      switch(statusCode) {
      case 301:
      case 302:
      case 307:
         HttpRequest request = (HttpRequest)context.getAttribute("http.request");
         String method = request.getRequestLine().getMethod();
         return method.equalsIgnoreCase("GET") || method.equalsIgnoreCase("HEAD");
      case 303:
         return true;
      case 304:
      case 305:
      case 306:
      default:
         return false;
      }
   }

   public URI getLocationURI(HttpResponse response, HttpContext context) throws ProtocolException {
      Args.notNull(response, "HTTP response");
      Header locationHeader = response.getFirstHeader("location");
      if (locationHeader == null) {
         throw new ProtocolException("Received redirect response " + response.getStatusLine() + " but no location header");
      } else {
         String location = locationHeader.getValue();
         if (this.log.isDebugEnabled()) {
            this.log.debug("Redirect requested to location '" + location + "'");
         }

         URI uri;
         try {
            uri = new URI(location);
         } catch (URISyntaxException var13) {
            throw new ProtocolException("Invalid redirect URI: " + location, var13);
         }

         HttpParams params = response.getParams();
         if (!uri.isAbsolute()) {
            if (params.isParameterTrue("http.protocol.reject-relative-redirect")) {
               throw new ProtocolException("Relative redirect location '" + uri + "' not allowed");
            }

            HttpHost target = (HttpHost)context.getAttribute("http.target_host");
            Asserts.notNull(target, "Target host");
            HttpRequest request = (HttpRequest)context.getAttribute("http.request");

            try {
               URI requestURI = new URI(request.getRequestLine().getUri());
               URI absoluteRequestURI = URIUtils.rewriteURI(requestURI, target, URIUtils.DROP_FRAGMENT_AND_NORMALIZE);
               uri = URIUtils.resolve(absoluteRequestURI, uri);
            } catch (URISyntaxException var12) {
               throw new ProtocolException(var12.getMessage(), var12);
            }
         }

         if (params.isParameterFalse("http.protocol.allow-circular-redirects")) {
            RedirectLocations redirectLocations = (RedirectLocations)context.getAttribute("http.protocol.redirect-locations");
            if (redirectLocations == null) {
               redirectLocations = new RedirectLocations();
               context.setAttribute("http.protocol.redirect-locations", redirectLocations);
            }

            URI redirectURI;
            if (uri.getFragment() != null) {
               try {
                  HttpHost target = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
                  redirectURI = URIUtils.rewriteURI(uri, target, URIUtils.DROP_FRAGMENT_AND_NORMALIZE);
               } catch (URISyntaxException var11) {
                  throw new ProtocolException(var11.getMessage(), var11);
               }
            } else {
               redirectURI = uri;
            }

            if (redirectLocations.contains(redirectURI)) {
               throw new CircularRedirectException("Circular redirect to '" + redirectURI + "'");
            }

            redirectLocations.add(redirectURI);
         }

         return uri;
      }
   }
}
