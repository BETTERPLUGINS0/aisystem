package fr.xephi.authme.libs.org.apache.http.conn;

import fr.xephi.authme.libs.org.apache.http.HttpInetConnection;
import fr.xephi.authme.libs.org.apache.http.conn.routing.HttpRoute;
import javax.net.ssl.SSLSession;

/** @deprecated */
@Deprecated
public interface HttpRoutedConnection extends HttpInetConnection {
   boolean isSecure();

   HttpRoute getRoute();

   SSLSession getSSLSession();
}
