package fr.xephi.authme.libs.org.apache.http.impl;

import fr.xephi.authme.libs.org.apache.http.ConnectionReuseStrategy;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class NoConnectionReuseStrategy implements ConnectionReuseStrategy {
   public static final NoConnectionReuseStrategy INSTANCE = new NoConnectionReuseStrategy();

   public boolean keepAlive(HttpResponse response, HttpContext context) {
      return false;
   }
}
