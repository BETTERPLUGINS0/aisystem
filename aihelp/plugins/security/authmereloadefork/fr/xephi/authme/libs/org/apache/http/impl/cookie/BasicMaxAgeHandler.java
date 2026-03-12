package fr.xephi.authme.libs.org.apache.http.impl.cookie;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.cookie.CommonCookieAttributeHandler;
import fr.xephi.authme.libs.org.apache.http.cookie.MalformedCookieException;
import fr.xephi.authme.libs.org.apache.http.cookie.SetCookie;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.util.Date;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class BasicMaxAgeHandler extends AbstractCookieAttributeHandler implements CommonCookieAttributeHandler {
   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
      Args.notNull(cookie, "Cookie");
      if (value == null) {
         throw new MalformedCookieException("Missing value for 'max-age' attribute");
      } else {
         int age;
         try {
            age = Integer.parseInt(value);
         } catch (NumberFormatException var5) {
            throw new MalformedCookieException("Invalid 'max-age' attribute: " + value);
         }

         if (age < 0) {
            throw new MalformedCookieException("Negative 'max-age' attribute: " + value);
         } else {
            cookie.setExpiryDate(new Date(System.currentTimeMillis() + (long)age * 1000L));
         }
      }
   }

   public String getAttributeName() {
      return "max-age";
   }
}
