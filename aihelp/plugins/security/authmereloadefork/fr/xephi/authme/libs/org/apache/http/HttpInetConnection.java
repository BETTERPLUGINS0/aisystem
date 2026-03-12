package fr.xephi.authme.libs.org.apache.http;

import java.net.InetAddress;

public interface HttpInetConnection extends HttpConnection {
   InetAddress getLocalAddress();

   int getLocalPort();

   InetAddress getRemoteAddress();

   int getRemotePort();
}
