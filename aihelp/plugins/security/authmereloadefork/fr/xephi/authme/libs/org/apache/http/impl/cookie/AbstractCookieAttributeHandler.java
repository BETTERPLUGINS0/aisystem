package fr.xephi.authme.libs.org.apache.http.impl.cookie;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.cookie.Cookie;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieAttributeHandler;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieOrigin;
import fr.xephi.authme.libs.org.apache.http.cookie.MalformedCookieException;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public abstract class AbstractCookieAttributeHandler implements CookieAttributeHandler {
   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
   }

   public boolean match(Cookie cookie, CookieOrigin origin) {
      return true;
   }
}
