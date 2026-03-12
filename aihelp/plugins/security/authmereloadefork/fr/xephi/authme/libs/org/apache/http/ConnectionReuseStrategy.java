package fr.xephi.authme.libs.org.apache.http;

import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;

public interface ConnectionReuseStrategy {
   boolean keepAlive(HttpResponse var1, HttpContext var2);
}
