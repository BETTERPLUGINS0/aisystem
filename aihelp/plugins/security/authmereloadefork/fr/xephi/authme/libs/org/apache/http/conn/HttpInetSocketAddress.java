package fr.xephi.authme.libs.org.apache.http.conn;

import fr.xephi.authme.libs.org.apache.http.HttpHost;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/** @deprecated */
@Deprecated
public class HttpInetSocketAddress extends InetSocketAddress {
   private static final long serialVersionUID = -6650701828361907957L;
   private final HttpHost httphost;

   public HttpInetSocketAddress(HttpHost httphost, InetAddress addr, int port) {
      super(addr, port);
      Args.notNull(httphost, "HTTP host");
      this.httphost = httphost;
   }

   public HttpHost getHttpHost() {
      return this.httphost;
   }

   public String toString() {
      return this.httphost.getHostName() + ":" + this.getPort();
   }
}
