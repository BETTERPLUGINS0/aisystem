package fr.xephi.authme.libs.org.apache.http.conn;

import fr.xephi.authme.libs.org.apache.http.HttpHost;
import fr.xephi.authme.libs.org.apache.http.conn.routing.HttpRoute;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSession;

/** @deprecated */
@Deprecated
public interface ManagedClientConnection extends HttpRoutedConnection, ManagedHttpClientConnection, ConnectionReleaseTrigger {
   boolean isSecure();

   HttpRoute getRoute();

   SSLSession getSSLSession();

   void open(HttpRoute var1, HttpContext var2, HttpParams var3) throws IOException;

   void tunnelTarget(boolean var1, HttpParams var2) throws IOException;

   void tunnelProxy(HttpHost var1, boolean var2, HttpParams var3) throws IOException;

   void layerProtocol(HttpContext var1, HttpParams var2) throws IOException;

   void markReusable();

   void unmarkReusable();

   boolean isMarkedReusable();

   void setState(Object var1);

   Object getState();

   void setIdleDuration(long var1, TimeUnit var3);
}
