package fr.xephi.authme.libs.org.apache.http.impl.cookie;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.conn.util.InetAddressUtils;
import fr.xephi.authme.libs.org.apache.http.cookie.ClientCookie;
import fr.xephi.authme.libs.org.apache.http.cookie.CommonCookieAttributeHandler;
import fr.xephi.authme.libs.org.apache.http.cookie.Cookie;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieOrigin;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieRestrictionViolationException;
import fr.xephi.authme.libs.org.apache.http.cookie.MalformedCookieException;
import fr.xephi.authme.libs.org.apache.http.cookie.SetCookie;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import fr.xephi.authme.libs.org.apache.http.util.TextUtils;
import java.util.Locale;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class BasicDomainHandler implements CommonCookieAttributeHandler {
   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
      Args.notNull(cookie, "Cookie");
      if (TextUtils.isBlank(value)) {
         throw new MalformedCookieException("Blank or null value for domain attribute");
      } else if (!value.endsWith(".")) {
         String domain = value;
         if (value.startsWith(".")) {
            domain = value.substring(1);
         }

         domain = domain.toLowerCase(Locale.ROOT);
         cookie.setDomain(domain);
      }
   }

   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
      Args.notNull(cookie, "Cookie");
      Args.notNull(origin, "Cookie origin");
      String host = origin.getHost();
      String domain = cookie.getDomain();
      if (domain == null) {
         throw new CookieRestrictionViolationException("Cookie 'domain' may not be null");
      } else if (!host.equals(domain) && !domainMatch(domain, host)) {
         throw new CookieRestrictionViolationException("Illegal 'domain' attribute \"" + domain + "\". Domain of origin: \"" + host + "\"");
      }
   }

   static boolean domainMatch(String domain, String host) {
      if (!InetAddressUtils.isIPv4Address(host) && !InetAddressUtils.isIPv6Address(host)) {
         String normalizedDomain = domain.startsWith(".") ? domain.substring(1) : domain;
         if (host.endsWith(normalizedDomain)) {
            int prefix = host.length() - normalizedDomain.length();
            if (prefix == 0) {
               return true;
            }

            if (prefix > 1 && host.charAt(prefix - 1) == '.') {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public boolean match(Cookie cookie, CookieOrigin origin) {
      Args.notNull(cookie, "Cookie");
      Args.notNull(origin, "Cookie origin");
      String host = origin.getHost();
      String domain = cookie.getDomain();
      if (domain == null) {
         return false;
      } else {
         if (domain.startsWith(".")) {
            domain = domain.substring(1);
         }

         domain = domain.toLowerCase(Locale.ROOT);
         if (host.equals(domain)) {
            return true;
         } else {
            return cookie instanceof ClientCookie && ((ClientCookie)cookie).containsAttribute("domain") ? domainMatch(domain, host) : false;
         }
      }
   }

   public String getAttributeName() {
      return "domain";
   }
}
