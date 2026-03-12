package fr.xephi.authme.libs.org.apache.http.client;

import fr.xephi.authme.libs.org.apache.http.conn.routing.HttpRoute;

public interface BackoffManager {
   void backOff(HttpRoute var1);

   void probe(HttpRoute var1);
}
