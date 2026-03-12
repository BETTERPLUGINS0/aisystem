package fr.xephi.authme.libs.org.apache.http.conn;

import fr.xephi.authme.libs.org.apache.http.HttpClientConnection;
import fr.xephi.authme.libs.org.apache.http.HttpHost;
import fr.xephi.authme.libs.org.apache.http.HttpInetConnection;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import java.io.IOException;
import java.net.Socket;

/** @deprecated */
@Deprecated
public interface OperatedClientConnection extends HttpClientConnection, HttpInetConnection {
   HttpHost getTargetHost();

   boolean isSecure();

   Socket getSocket();

   void opening(Socket var1, HttpHost var2) throws IOException;

   void openCompleted(boolean var1, HttpParams var2) throws IOException;

   void update(Socket var1, HttpHost var2, boolean var3, HttpParams var4) throws IOException;
}
