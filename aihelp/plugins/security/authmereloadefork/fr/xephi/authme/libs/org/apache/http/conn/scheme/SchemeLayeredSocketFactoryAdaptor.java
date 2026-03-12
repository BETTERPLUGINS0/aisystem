package fr.xephi.authme.libs.org.apache.http.conn.scheme;

import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/** @deprecated */
@Deprecated
class SchemeLayeredSocketFactoryAdaptor extends SchemeSocketFactoryAdaptor implements SchemeLayeredSocketFactory {
   private final LayeredSocketFactory factory;

   SchemeLayeredSocketFactoryAdaptor(LayeredSocketFactory factory) {
      super(factory);
      this.factory = factory;
   }

   public Socket createLayeredSocket(Socket socket, String target, int port, HttpParams params) throws IOException, UnknownHostException {
      return this.factory.createSocket(socket, target, port, true);
   }
}
