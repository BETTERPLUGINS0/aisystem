package fr.xephi.authme.libs.org.apache.http.conn;

import fr.xephi.authme.libs.org.apache.http.HttpClientConnection;
import fr.xephi.authme.libs.org.apache.http.HttpInetConnection;
import java.io.IOException;
import java.net.Socket;
import javax.net.ssl.SSLSession;

public interface ManagedHttpClientConnection extends HttpClientConnection, HttpInetConnection {
   String getId();

   void bind(Socket var1) throws IOException;

   Socket getSocket();

   SSLSession getSSLSession();
}
