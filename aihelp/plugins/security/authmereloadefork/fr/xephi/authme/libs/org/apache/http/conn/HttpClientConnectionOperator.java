package fr.xephi.authme.libs.org.apache.http.conn;

import fr.xephi.authme.libs.org.apache.http.HttpHost;
import fr.xephi.authme.libs.org.apache.http.config.SocketConfig;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import java.io.IOException;
import java.net.InetSocketAddress;

public interface HttpClientConnectionOperator {
   void connect(ManagedHttpClientConnection var1, HttpHost var2, InetSocketAddress var3, int var4, SocketConfig var5, HttpContext var6) throws IOException;

   void upgrade(ManagedHttpClientConnection var1, HttpHost var2, HttpContext var3) throws IOException;
}
