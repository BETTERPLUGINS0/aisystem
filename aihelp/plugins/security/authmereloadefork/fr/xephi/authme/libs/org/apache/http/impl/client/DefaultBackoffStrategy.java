package fr.xephi.authme.libs.org.apache.http.impl.client;

import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.client.ConnectionBackoffStrategy;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

public class DefaultBackoffStrategy implements ConnectionBackoffStrategy {
   public boolean shouldBackoff(Throwable t) {
      return t instanceof SocketTimeoutException || t instanceof ConnectException;
   }

   public boolean shouldBackoff(HttpResponse resp) {
      return resp.getStatusLine().getStatusCode() == 429 || resp.getStatusLine().getStatusCode() == 503;
   }
}
