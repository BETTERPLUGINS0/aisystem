package fr.xephi.authme.libs.org.apache.http.impl.client;

import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.client.ConnectionBackoffStrategy;

public class NullBackoffStrategy implements ConnectionBackoffStrategy {
   public boolean shouldBackoff(Throwable t) {
      return false;
   }

   public boolean shouldBackoff(HttpResponse resp) {
      return false;
   }
}
