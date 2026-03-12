package fr.xephi.authme.libs.org.apache.http.impl.cookie;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieSpec;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieSpecFactory;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieSpecProvider;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;

/** @deprecated */
@Deprecated
@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class IgnoreSpecFactory implements CookieSpecFactory, CookieSpecProvider {
   public CookieSpec newInstance(HttpParams params) {
      return new IgnoreSpec();
   }

   public CookieSpec create(HttpContext context) {
      return new IgnoreSpec();
   }
}
