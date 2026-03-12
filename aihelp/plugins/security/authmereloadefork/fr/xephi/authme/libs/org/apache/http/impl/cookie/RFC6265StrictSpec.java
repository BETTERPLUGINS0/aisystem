package fr.xephi.authme.libs.org.apache.http.impl.cookie;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.cookie.CommonCookieAttributeHandler;

@Contract(
   threading = ThreadingBehavior.SAFE
)
public class RFC6265StrictSpec extends RFC6265CookieSpecBase {
   static final String[] DATE_PATTERNS = new String[]{"EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy"};

   public RFC6265StrictSpec() {
      super(new BasicPathHandler(), new BasicDomainHandler(), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicExpiresHandler(DATE_PATTERNS));
   }

   RFC6265StrictSpec(CommonCookieAttributeHandler... handlers) {
      super(handlers);
   }

   public String toString() {
      return "rfc6265-strict";
   }
}
