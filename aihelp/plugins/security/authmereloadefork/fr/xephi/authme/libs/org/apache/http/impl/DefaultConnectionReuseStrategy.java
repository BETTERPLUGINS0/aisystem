package fr.xephi.authme.libs.org.apache.http.impl;

import fr.xephi.authme.libs.org.apache.http.ConnectionReuseStrategy;
import fr.xephi.authme.libs.org.apache.http.Header;
import fr.xephi.authme.libs.org.apache.http.HeaderIterator;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.HttpVersion;
import fr.xephi.authme.libs.org.apache.http.ParseException;
import fr.xephi.authme.libs.org.apache.http.ProtocolVersion;
import fr.xephi.authme.libs.org.apache.http.TokenIterator;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.message.BasicTokenIterator;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import fr.xephi.authme.libs.org.apache.http.util.Args;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class DefaultConnectionReuseStrategy implements ConnectionReuseStrategy {
   public static final DefaultConnectionReuseStrategy INSTANCE = new DefaultConnectionReuseStrategy();

   public boolean keepAlive(HttpResponse response, HttpContext context) {
      Args.notNull(response, "HTTP response");
      Args.notNull(context, "HTTP context");
      if (response.getStatusLine().getStatusCode() == 204) {
         Header clh = response.getFirstHeader("Content-Length");
         if (clh != null) {
            try {
               int contentLen = Integer.parseInt(clh.getValue());
               if (contentLen > 0) {
                  return false;
               }
            } catch (NumberFormatException var11) {
            }
         }

         Header teh = response.getFirstHeader("Transfer-Encoding");
         if (teh != null) {
            return false;
         }
      }

      HttpRequest request = (HttpRequest)context.getAttribute("http.request");
      if (request != null) {
         try {
            BasicTokenIterator ti = new BasicTokenIterator(request.headerIterator("Connection"));

            while(ti.hasNext()) {
               String token = ti.nextToken();
               if ("Close".equalsIgnoreCase(token)) {
                  return false;
               }
            }
         } catch (ParseException var13) {
            return false;
         }
      }

      ProtocolVersion ver = response.getStatusLine().getProtocolVersion();
      Header teh = response.getFirstHeader("Transfer-Encoding");
      if (teh != null) {
         if (!"chunked".equalsIgnoreCase(teh.getValue())) {
            return false;
         }
      } else if (this.canResponseHaveBody(request, response)) {
         Header[] clhs = response.getHeaders("Content-Length");
         if (clhs.length != 1) {
            return false;
         }

         Header clh = clhs[0];

         try {
            long contentLen = Long.parseLong(clh.getValue());
            if (contentLen < 0L) {
               return false;
            }
         } catch (NumberFormatException var10) {
            return false;
         }
      }

      HeaderIterator headerIterator = response.headerIterator("Connection");
      if (!headerIterator.hasNext()) {
         headerIterator = response.headerIterator("Proxy-Connection");
      }

      if (headerIterator.hasNext()) {
         try {
            TokenIterator ti = new BasicTokenIterator(headerIterator);
            boolean keepalive = false;

            while(ti.hasNext()) {
               String token = ti.nextToken();
               if ("Close".equalsIgnoreCase(token)) {
                  return false;
               }

               if ("Keep-Alive".equalsIgnoreCase(token)) {
                  keepalive = true;
               }
            }

            if (keepalive) {
               return true;
            }
         } catch (ParseException var12) {
            return false;
         }
      }

      return !ver.lessEquals(HttpVersion.HTTP_1_0);
   }

   protected TokenIterator createTokenIterator(HeaderIterator hit) {
      return new BasicTokenIterator(hit);
   }

   private boolean canResponseHaveBody(HttpRequest request, HttpResponse response) {
      if (request != null && request.getRequestLine().getMethod().equalsIgnoreCase("HEAD")) {
         return false;
      } else {
         int status = response.getStatusLine().getStatusCode();
         return status >= 200 && status != 204 && status != 304 && status != 205;
      }
   }
}
