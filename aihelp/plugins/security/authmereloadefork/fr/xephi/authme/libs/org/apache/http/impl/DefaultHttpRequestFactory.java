package fr.xephi.authme.libs.org.apache.http.impl;

import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.HttpRequestFactory;
import fr.xephi.authme.libs.org.apache.http.MethodNotSupportedException;
import fr.xephi.authme.libs.org.apache.http.RequestLine;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.message.BasicHttpEntityEnclosingRequest;
import fr.xephi.authme.libs.org.apache.http.message.BasicHttpRequest;
import fr.xephi.authme.libs.org.apache.http.util.Args;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class DefaultHttpRequestFactory implements HttpRequestFactory {
   public static final DefaultHttpRequestFactory INSTANCE = new DefaultHttpRequestFactory();
   private static final String[] RFC2616_COMMON_METHODS = new String[]{"GET"};
   private static final String[] RFC2616_ENTITY_ENC_METHODS = new String[]{"POST", "PUT"};
   private static final String[] RFC2616_SPECIAL_METHODS = new String[]{"HEAD", "OPTIONS", "DELETE", "TRACE", "CONNECT"};
   private static final String[] RFC5789_ENTITY_ENC_METHODS = new String[]{"PATCH"};

   private static boolean isOneOf(String[] methods, String method) {
      String[] arr$ = methods;
      int len$ = methods.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String method2 = arr$[i$];
         if (method2.equalsIgnoreCase(method)) {
            return true;
         }
      }

      return false;
   }

   public HttpRequest newHttpRequest(RequestLine requestline) throws MethodNotSupportedException {
      Args.notNull(requestline, "Request line");
      String method = requestline.getMethod();
      if (isOneOf(RFC2616_COMMON_METHODS, method)) {
         return new BasicHttpRequest(requestline);
      } else if (isOneOf(RFC2616_ENTITY_ENC_METHODS, method)) {
         return new BasicHttpEntityEnclosingRequest(requestline);
      } else if (isOneOf(RFC2616_SPECIAL_METHODS, method)) {
         return new BasicHttpRequest(requestline);
      } else if (isOneOf(RFC5789_ENTITY_ENC_METHODS, method)) {
         return new BasicHttpEntityEnclosingRequest(requestline);
      } else {
         throw new MethodNotSupportedException(method + " method not supported");
      }
   }

   public HttpRequest newHttpRequest(String method, String uri) throws MethodNotSupportedException {
      if (isOneOf(RFC2616_COMMON_METHODS, method)) {
         return new BasicHttpRequest(method, uri);
      } else if (isOneOf(RFC2616_ENTITY_ENC_METHODS, method)) {
         return new BasicHttpEntityEnclosingRequest(method, uri);
      } else if (isOneOf(RFC2616_SPECIAL_METHODS, method)) {
         return new BasicHttpRequest(method, uri);
      } else if (isOneOf(RFC5789_ENTITY_ENC_METHODS, method)) {
         return new BasicHttpEntityEnclosingRequest(method, uri);
      } else {
         throw new MethodNotSupportedException(method + " method not supported");
      }
   }
}
