package fr.xephi.authme.libs.org.apache.http.impl.cookie;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.cookie.CommonCookieAttributeHandler;

@Contract(
   threading = ThreadingBehavior.SAFE
)
public class RFC6265LaxSpec extends RFC6265CookieSpecBase {
   public RFC6265LaxSpec() {
      super(new BasicPathHandler(), new BasicDomainHandler(), new LaxMaxAgeHandler(), new BasicSecureHandler(), new LaxExpiresHandler());
   }

   RFC6265LaxSpec(CommonCookieAttributeHandler... handlers) {
      super(handlers);
   }

   public String toString() {
      return "rfc6265-lax";
   }
}
