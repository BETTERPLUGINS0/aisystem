package fr.xephi.authme.libs.org.apache.http.client;

import fr.xephi.authme.libs.org.apache.http.HttpResponse;

public interface ConnectionBackoffStrategy {
   boolean shouldBackoff(Throwable var1);

   boolean shouldBackoff(HttpResponse var1);
}
