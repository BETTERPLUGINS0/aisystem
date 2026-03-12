package fr.xephi.authme.libs.org.apache.http.impl.cookie;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;

/** @deprecated */
@Deprecated
@Contract(
   threading = ThreadingBehavior.SAFE
)
public class BestMatchSpec extends DefaultCookieSpec {
   public BestMatchSpec(String[] datepatterns, boolean oneHeader) {
      super(datepatterns, oneHeader);
   }

   public BestMatchSpec() {
      this((String[])null, false);
   }

   public String toString() {
      return "best-match";
   }
}
