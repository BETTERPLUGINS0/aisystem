package fr.xephi.authme.libs.org.apache.http.impl.cookie;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.cookie.ClientCookie;
import fr.xephi.authme.libs.org.apache.http.cookie.CommonCookieAttributeHandler;
import fr.xephi.authme.libs.org.apache.http.cookie.Cookie;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieOrigin;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieRestrictionViolationException;
import fr.xephi.authme.libs.org.apache.http.cookie.MalformedCookieException;
import fr.xephi.authme.libs.org.apache.http.cookie.SetCookie;
import fr.xephi.authme.libs.org.apache.http.cookie.SetCookie2;
import fr.xephi.authme.libs.org.apache.http.util.Args;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class RFC2965VersionAttributeHandler implements CommonCookieAttributeHandler {
   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
      Args.notNull(cookie, "Cookie");
      if (value == null) {
         throw new MalformedCookieException("Missing value for version attribute");
      } else {
         boolean var3 = true;

         int version;
         try {
            version = Integer.parseInt(value);
         } catch (NumberFormatException var5) {
            version = -1;
         }

         if (version < 0) {
            throw new MalformedCookieException("Invalid cookie version.");
         } else {
            cookie.setVersion(version);
         }
      }
   }

   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
      Args.notNull(cookie, "Cookie");
      if (cookie instanceof SetCookie2 && cookie instanceof ClientCookie && !((ClientCookie)cookie).containsAttribute("version")) {
         throw new CookieRestrictionViolationException("Violates RFC 2965. Version attribute is required.");
      }
   }

   public boolean match(Cookie cookie, CookieOrigin origin) {
      return true;
   }

   public String getAttributeName() {
      return "version";
   }
}
