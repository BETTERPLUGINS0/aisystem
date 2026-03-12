package fr.xephi.authme.libs.org.apache.http.cookie;

import fr.xephi.authme.libs.org.apache.http.Header;
import fr.xephi.authme.libs.org.apache.http.annotation.Obsolete;
import java.util.List;

public interface CookieSpec {
   @Obsolete
   int getVersion();

   List<Cookie> parse(Header var1, CookieOrigin var2) throws MalformedCookieException;

   void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException;

   boolean match(Cookie var1, CookieOrigin var2);

   List<Header> formatCookies(List<Cookie> var1);

   @Obsolete
   Header getVersionHeader();
}
