package fr.xephi.authme.libs.org.apache.http.impl.cookie;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.cookie.Cookie;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieOrigin;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieRestrictionViolationException;
import fr.xephi.authme.libs.org.apache.http.cookie.MalformedCookieException;
import fr.xephi.authme.libs.org.apache.http.cookie.SetCookie;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import fr.xephi.authme.libs.org.apache.http.util.TextUtils;
import java.util.Locale;
import java.util.StringTokenizer;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class NetscapeDomainHandler extends BasicDomainHandler {
   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
      Args.notNull(cookie, "Cookie");
      if (TextUtils.isBlank(value)) {
         throw new MalformedCookieException("Blank or null value for domain attribute");
      } else {
         cookie.setDomain(value);
      }
   }

   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
      String host = origin.getHost();
      String domain = cookie.getDomain();
      if (!host.equals(domain) && !BasicDomainHandler.domainMatch(domain, host)) {
         throw new CookieRestrictionViolationException("Illegal domain attribute \"" + domain + "\". Domain of origin: \"" + host + "\"");
      } else {
         if (host.contains(".")) {
            int domainParts = (new StringTokenizer(domain, ".")).countTokens();
            if (isSpecialDomain(domain)) {
               if (domainParts < 2) {
                  throw new CookieRestrictionViolationException("Domain attribute \"" + domain + "\" violates the Netscape cookie specification for " + "special domains");
               }
            } else if (domainParts < 3) {
               throw new CookieRestrictionViolationException("Domain attribute \"" + domain + "\" violates the Netscape cookie specification");
            }
         }

      }
   }

   private static boolean isSpecialDomain(String domain) {
      String ucDomain = domain.toUpperCase(Locale.ROOT);
      return ucDomain.endsWith(".COM") || ucDomain.endsWith(".EDU") || ucDomain.endsWith(".NET") || ucDomain.endsWith(".GOV") || ucDomain.endsWith(".MIL") || ucDomain.endsWith(".ORG") || ucDomain.endsWith(".INT");
   }

   public boolean match(Cookie cookie, CookieOrigin origin) {
      Args.notNull(cookie, "Cookie");
      Args.notNull(origin, "Cookie origin");
      String host = origin.getHost();
      String domain = cookie.getDomain();
      return domain == null ? false : host.endsWith(domain);
   }

   public String getAttributeName() {
      return "domain";
   }
}
