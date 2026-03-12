package fr.xephi.authme.libs.org.apache.http.conn;

import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;

public interface ConnectionKeepAliveStrategy {
   long getKeepAliveDuration(HttpResponse var1, HttpContext var2);
}
