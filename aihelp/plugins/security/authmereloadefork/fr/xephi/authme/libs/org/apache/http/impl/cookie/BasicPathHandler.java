package fr.xephi.authme.libs.org.apache.http.impl.cookie;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.cookie.CommonCookieAttributeHandler;
import fr.xephi.authme.libs.org.apache.http.cookie.Cookie;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieOrigin;
import fr.xephi.authme.libs.org.apache.http.cookie.MalformedCookieException;
import fr.xephi.authme.libs.org.apache.http.cookie.SetCookie;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import fr.xephi.authme.libs.org.apache.http.util.TextUtils;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class BasicPathHandler implements CommonCookieAttributeHandler {
   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
      Args.notNull(cookie, "Cookie");
      cookie.setPath(!TextUtils.isBlank(value) ? value : "/");
   }

   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
   }

   static boolean pathMatch(String uriPath, String cookiePath) {
      String normalizedCookiePath = cookiePath;
      if (cookiePath == null) {
         normalizedCookiePath = "/";
      }

      if (normalizedCookiePath.length() > 1 && normalizedCookiePath.endsWith("/")) {
         normalizedCookiePath = normalizedCookiePath.substring(0, normalizedCookiePath.length() - 1);
      }

      if (uriPath.startsWith(normalizedCookiePath)) {
         if (normalizedCookiePath.equals("/")) {
            return true;
         }

         if (uriPath.length() == normalizedCookiePath.length()) {
            return true;
         }

         if (uriPath.charAt(normalizedCookiePath.length()) == '/') {
            return true;
         }
      }

      return false;
   }

   public boolean match(Cookie cookie, CookieOrigin origin) {
      Args.notNull(cookie, "Cookie");
      Args.notNull(origin, "Cookie origin");
      return pathMatch(origin.getPath(), cookie.getPath());
   }

   public String getAttributeName() {
      return "path";
   }
}
