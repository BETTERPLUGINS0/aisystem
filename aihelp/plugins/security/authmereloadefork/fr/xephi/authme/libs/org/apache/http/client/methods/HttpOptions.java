package fr.xephi.authme.libs.org.apache.http.client.methods;

import fr.xephi.authme.libs.org.apache.http.Header;
import fr.xephi.authme.libs.org.apache.http.HeaderElement;
import fr.xephi.authme.libs.org.apache.http.HeaderIterator;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

public class HttpOptions extends HttpRequestBase {
   public static final String METHOD_NAME = "OPTIONS";

   public HttpOptions() {
   }

   public HttpOptions(URI uri) {
      this.setURI(uri);
   }

   public HttpOptions(String uri) {
      this.setURI(URI.create(uri));
   }

   public String getMethod() {
      return "OPTIONS";
   }

   public Set<String> getAllowedMethods(HttpResponse response) {
      Args.notNull(response, "HTTP response");
      HeaderIterator it = response.headerIterator("Allow");
      HashSet methods = new HashSet();

      while(it.hasNext()) {
         Header header = it.nextHeader();
         HeaderElement[] elements = header.getElements();
         HeaderElement[] arr$ = elements;
         int len$ = elements.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            HeaderElement element = arr$[i$];
            methods.add(element.getName());
         }
      }

      return methods;
   }
}
