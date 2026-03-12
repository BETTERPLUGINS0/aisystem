package fr.xephi.authme.libs.org.apache.http.impl.cookie;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.conn.util.PublicSuffixMatcher;
import fr.xephi.authme.libs.org.apache.http.cookie.CommonCookieAttributeHandler;
import fr.xephi.authme.libs.org.apache.http.cookie.Cookie;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieOrigin;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieSpec;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieSpecProvider;
import fr.xephi.authme.libs.org.apache.http.cookie.MalformedCookieException;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL
)
public class RFC6265CookieSpecProvider implements CookieSpecProvider {
   private final RFC6265CookieSpecProvider.CompatibilityLevel compatibilityLevel;
   private final PublicSuffixMatcher publicSuffixMatcher;
   private volatile CookieSpec cookieSpec;

   public RFC6265CookieSpecProvider(RFC6265CookieSpecProvider.CompatibilityLevel compatibilityLevel, PublicSuffixMatcher publicSuffixMatcher) {
      this.compatibilityLevel = compatibilityLevel != null ? compatibilityLevel : RFC6265CookieSpecProvider.CompatibilityLevel.RELAXED;
      this.publicSuffixMatcher = publicSuffixMatcher;
   }

   public RFC6265CookieSpecProvider(PublicSuffixMatcher publicSuffixMatcher) {
      this(RFC6265CookieSpecProvider.CompatibilityLevel.RELAXED, publicSuffixMatcher);
   }

   public RFC6265CookieSpecProvider() {
      this(RFC6265CookieSpecProvider.CompatibilityLevel.RELAXED, (PublicSuffixMatcher)null);
   }

   public CookieSpec create(HttpContext context) {
      if (this.cookieSpec == null) {
         synchronized(this) {
            if (this.cookieSpec == null) {
               switch(this.compatibilityLevel) {
               case STRICT:
                  this.cookieSpec = new RFC6265StrictSpec(new CommonCookieAttributeHandler[]{new BasicPathHandler(), PublicSuffixDomainFilter.decorate(new BasicDomainHandler(), this.publicSuffixMatcher), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicExpiresHandler(RFC6265StrictSpec.DATE_PATTERNS)});
                  break;
               case IE_MEDIUM_SECURITY:
                  this.cookieSpec = new RFC6265LaxSpec(new CommonCookieAttributeHandler[]{new BasicPathHandler() {
                     public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
                     }
                  }, PublicSuffixDomainFilter.decorate(new BasicDomainHandler(), this.publicSuffixMatcher), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicExpiresHandler(RFC6265StrictSpec.DATE_PATTERNS)});
                  break;
               default:
                  this.cookieSpec = new RFC6265LaxSpec(new CommonCookieAttributeHandler[]{new BasicPathHandler(), PublicSuffixDomainFilter.decorate(new BasicDomainHandler(), this.publicSuffixMatcher), new LaxMaxAgeHandler(), new BasicSecureHandler(), new LaxExpiresHandler()});
               }
            }
         }
      }

      return this.cookieSpec;
   }

   public static enum CompatibilityLevel {
      STRICT,
      RELAXED,
      IE_MEDIUM_SECURITY;
   }
}
