package fr.xephi.authme.libs.org.apache.http.client;

import fr.xephi.authme.libs.org.apache.http.cookie.Cookie;
import java.util.Date;
import java.util.List;

public interface CookieStore {
   void addCookie(Cookie var1);

   List<Cookie> getCookies();

   boolean clearExpired(Date var1);

   void clear();
}
