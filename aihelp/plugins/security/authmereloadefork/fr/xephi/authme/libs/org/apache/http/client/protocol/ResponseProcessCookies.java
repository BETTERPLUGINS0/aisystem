package fr.xephi.authme.libs.org.apache.http.client.protocol;

import fr.xephi.authme.libs.org.apache.commons.logging.Log;
import fr.xephi.authme.libs.org.apache.commons.logging.LogFactory;
import fr.xephi.authme.libs.org.apache.http.Header;
import fr.xephi.authme.libs.org.apache.http.HeaderIterator;
import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.HttpResponseInterceptor;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.client.CookieStore;
import fr.xephi.authme.libs.org.apache.http.cookie.Cookie;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieOrigin;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieSpec;
import fr.xephi.authme.libs.org.apache.http.cookie.MalformedCookieException;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class ResponseProcessCookies implements HttpResponseInterceptor {
   private final Log log = LogFactory.getLog(this.getClass());

   public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
      Args.notNull(response, "HTTP request");
      Args.notNull(context, "HTTP context");
      HttpClientContext clientContext = HttpClientContext.adapt(context);
      CookieSpec cookieSpec = clientContext.getCookieSpec();
      if (cookieSpec == null) {
         this.log.debug("Cookie spec not specified in HTTP context");
      } else {
         CookieStore cookieStore = clientContext.getCookieStore();
         if (cookieStore == null) {
            this.log.debug("Cookie store not specified in HTTP context");
         } else {
            CookieOrigin cookieOrigin = clientContext.getCookieOrigin();
            if (cookieOrigin == null) {
               this.log.debug("Cookie origin not specified in HTTP context");
            } else {
               HeaderIterator it = response.headerIterator("Set-Cookie");
               this.processCookies(it, cookieSpec, cookieOrigin, cookieStore);
               if (cookieSpec.getVersion() > 0) {
                  it = response.headerIterator("Set-Cookie2");
                  this.processCookies(it, cookieSpec, cookieOrigin, cookieStore);
               }

            }
         }
      }
   }

   private void processCookies(HeaderIterator iterator, CookieSpec cookieSpec, CookieOrigin cookieOrigin, CookieStore cookieStore) {
      while(iterator.hasNext()) {
         Header header = iterator.nextHeader();

         try {
            List<Cookie> cookies = cookieSpec.parse(header, cookieOrigin);
            Iterator i$ = cookies.iterator();

            while(i$.hasNext()) {
               Cookie cookie = (Cookie)i$.next();

               try {
                  cookieSpec.validate(cookie, cookieOrigin);
                  cookieStore.addCookie(cookie);
                  if (this.log.isDebugEnabled()) {
                     this.log.debug("Cookie accepted [" + formatCooke(cookie) + "]");
                  }
               } catch (MalformedCookieException var10) {
                  if (this.log.isWarnEnabled()) {
                     this.log.warn("Cookie rejected [" + formatCooke(cookie) + "] " + var10.getMessage());
                  }
               }
            }
         } catch (MalformedCookieException var11) {
            if (this.log.isWarnEnabled()) {
               this.log.warn("Invalid cookie header: \"" + header + "\". " + var11.getMessage());
            }
         }
      }

   }

   private static String formatCooke(Cookie cookie) {
      StringBuilder buf = new StringBuilder();
      buf.append(cookie.getName());
      buf.append("=\"");
      String v = cookie.getValue();
      if (v != null) {
         if (v.length() > 100) {
            v = v.substring(0, 100) + "...";
         }

         buf.append(v);
      }

      buf.append("\"");
      buf.append(", version:");
      buf.append(Integer.toString(cookie.getVersion()));
      buf.append(", domain:");
      buf.append(cookie.getDomain());
      buf.append(", path:");
      buf.append(cookie.getPath());
      buf.append(", expiry:");
      buf.append(cookie.getExpiryDate());
      return buf.toString();
   }
}
