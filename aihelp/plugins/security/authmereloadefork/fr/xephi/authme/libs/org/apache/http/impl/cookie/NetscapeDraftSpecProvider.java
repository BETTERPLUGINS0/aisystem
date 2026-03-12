package fr.xephi.authme.libs.org.apache.http.impl.cookie;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.Obsolete;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieSpec;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieSpecProvider;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;

@Obsolete
@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class NetscapeDraftSpecProvider implements CookieSpecProvider {
   private final String[] datepatterns;
   private volatile CookieSpec cookieSpec;

   public NetscapeDraftSpecProvider(String[] datepatterns) {
      this.datepatterns = datepatterns;
   }

   public NetscapeDraftSpecProvider() {
      this((String[])null);
   }

   public CookieSpec create(HttpContext context) {
      if (this.cookieSpec == null) {
         synchronized(this) {
            if (this.cookieSpec == null) {
               this.cookieSpec = new NetscapeDraftSpec(this.datepatterns);
            }
         }
      }

      return this.cookieSpec;
   }
}
