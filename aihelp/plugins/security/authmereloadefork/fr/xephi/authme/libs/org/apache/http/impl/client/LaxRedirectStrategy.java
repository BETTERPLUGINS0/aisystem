package fr.xephi.authme.libs.org.apache.http.impl.client;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class LaxRedirectStrategy extends DefaultRedirectStrategy {
   public static final LaxRedirectStrategy INSTANCE = new LaxRedirectStrategy();

   public LaxRedirectStrategy() {
      super(new String[]{"GET", "POST", "HEAD", "DELETE"});
   }
}
