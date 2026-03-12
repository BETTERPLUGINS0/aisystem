package fr.xephi.authme.libs.org.apache.http.conn.routing;

import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpHost;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;

public interface HttpRoutePlanner {
   HttpRoute determineRoute(HttpHost var1, HttpRequest var2, HttpContext var3) throws HttpException;
}
