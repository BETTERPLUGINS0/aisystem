package fr.xephi.authme.libs.org.apache.http.client;

import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;

public interface ServiceUnavailableRetryStrategy {
   boolean retryRequest(HttpResponse var1, int var2, HttpContext var3);

   long getRetryInterval();
}
