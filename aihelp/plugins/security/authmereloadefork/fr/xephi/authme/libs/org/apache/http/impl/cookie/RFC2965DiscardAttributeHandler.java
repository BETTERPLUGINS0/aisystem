package fr.xephi.authme.libs.org.apache.http.impl.cookie;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.cookie.CommonCookieAttributeHandler;
import fr.xephi.authme.libs.org.apache.http.cookie.Cookie;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieOrigin;
import fr.xephi.authme.libs.org.apache.http.cookie.MalformedCookieException;
import fr.xephi.authme.libs.org.apache.http.cookie.SetCookie;
import fr.xephi.authme.libs.org.apache.http.cookie.SetCookie2;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class RFC2965DiscardAttributeHandler implements CommonCookieAttributeHandler {
   public void parse(SetCookie cookie, String commenturl) throws MalformedCookieException {
      if (cookie instanceof SetCookie2) {
         SetCookie2 cookie2 = (SetCookie2)cookie;
         cookie2.setDiscard(true);
      }

   }

   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
   }

   public boolean match(Cookie cookie, CookieOrigin origin) {
      return true;
   }

   public String getAttributeName() {
      return "discard";
   }
}
