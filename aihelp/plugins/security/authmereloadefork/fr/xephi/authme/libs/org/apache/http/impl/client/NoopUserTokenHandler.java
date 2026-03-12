package fr.xephi.authme.libs.org.apache.http.impl.client;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.client.UserTokenHandler;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class NoopUserTokenHandler implements UserTokenHandler {
   public static final NoopUserTokenHandler INSTANCE = new NoopUserTokenHandler();

   public Object getUserToken(HttpContext context) {
      return null;
   }
}
