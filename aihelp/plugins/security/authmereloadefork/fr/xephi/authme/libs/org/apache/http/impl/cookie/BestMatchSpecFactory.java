package fr.xephi.authme.libs.org.apache.http.impl.cookie;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieSpec;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieSpecFactory;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieSpecProvider;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import java.util.Collection;

/** @deprecated */
@Deprecated
@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class BestMatchSpecFactory implements CookieSpecFactory, CookieSpecProvider {
   private final CookieSpec cookieSpec;

   public BestMatchSpecFactory(String[] datepatterns, boolean oneHeader) {
      this.cookieSpec = new BestMatchSpec(datepatterns, oneHeader);
   }

   public BestMatchSpecFactory() {
      this((String[])null, false);
   }

   public CookieSpec newInstance(HttpParams params) {
      if (params != null) {
         String[] patterns = null;
         Collection<?> param = (Collection)params.getParameter("http.protocol.cookie-datepatterns");
         if (param != null) {
            patterns = new String[param.size()];
            patterns = (String[])param.toArray(patterns);
         }

         boolean singleHeader = params.getBooleanParameter("http.protocol.single-cookie-header", false);
         return new BestMatchSpec(patterns, singleHeader);
      } else {
         return new BestMatchSpec();
      }
   }

   public CookieSpec create(HttpContext context) {
      return this.cookieSpec;
   }
}
