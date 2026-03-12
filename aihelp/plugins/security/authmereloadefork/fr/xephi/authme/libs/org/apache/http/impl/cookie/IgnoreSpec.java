package fr.xephi.authme.libs.org.apache.http.impl.cookie;

import fr.xephi.authme.libs.org.apache.http.Header;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.cookie.Cookie;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieOrigin;
import fr.xephi.authme.libs.org.apache.http.cookie.MalformedCookieException;
import java.util.Collections;
import java.util.List;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class IgnoreSpec extends CookieSpecBase {
   public int getVersion() {
      return 0;
   }

   public List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
      return Collections.emptyList();
   }

   public boolean match(Cookie cookie, CookieOrigin origin) {
      return false;
   }

   public List<Header> formatCookies(List<Cookie> cookies) {
      return Collections.emptyList();
   }

   public Header getVersionHeader() {
      return null;
   }
}
