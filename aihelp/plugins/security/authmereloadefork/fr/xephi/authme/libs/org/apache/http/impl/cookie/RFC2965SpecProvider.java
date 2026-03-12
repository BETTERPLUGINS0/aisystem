package fr.xephi.authme.libs.org.apache.http.impl.cookie;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.Obsolete;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.conn.util.PublicSuffixMatcher;
import fr.xephi.authme.libs.org.apache.http.cookie.CommonCookieAttributeHandler;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieSpec;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieSpecProvider;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;

@Obsolete
@Contract(
   threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL
)
public class RFC2965SpecProvider implements CookieSpecProvider {
   private final PublicSuffixMatcher publicSuffixMatcher;
   private final boolean oneHeader;
   private volatile CookieSpec cookieSpec;

   public RFC2965SpecProvider(PublicSuffixMatcher publicSuffixMatcher, boolean oneHeader) {
      this.oneHeader = oneHeader;
      this.publicSuffixMatcher = publicSuffixMatcher;
   }

   public RFC2965SpecProvider(PublicSuffixMatcher publicSuffixMatcher) {
      this(publicSuffixMatcher, false);
   }

   public RFC2965SpecProvider() {
      this((PublicSuffixMatcher)null, false);
   }

   public CookieSpec create(HttpContext context) {
      if (this.cookieSpec == null) {
         synchronized(this) {
            if (this.cookieSpec == null) {
               this.cookieSpec = new RFC2965Spec(this.oneHeader, new CommonCookieAttributeHandler[]{new RFC2965VersionAttributeHandler(), new BasicPathHandler(), PublicSuffixDomainFilter.decorate(new RFC2965DomainAttributeHandler(), this.publicSuffixMatcher), new RFC2965PortAttributeHandler(), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicCommentHandler(), new RFC2965CommentUrlAttributeHandler(), new RFC2965DiscardAttributeHandler()});
            }
         }
      }

      return this.cookieSpec;
   }
}
