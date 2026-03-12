package fr.xephi.authme.libs.com.mysql.jdbc;

import fr.xephi.authme.libs.com.mysql.cj.conf.PropertySet;
import fr.xephi.authme.libs.com.mysql.cj.log.Log;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ServerSession;
import fr.xephi.authme.libs.com.mysql.cj.protocol.SocketConnection;
import fr.xephi.authme.libs.com.mysql.cj.protocol.StandardSocketFactory;
import java.io.Closeable;
import java.io.IOException;

public class SocketFactoryWrapper extends StandardSocketFactory implements fr.xephi.authme.libs.com.mysql.cj.protocol.SocketFactory {
   SocketFactory socketFactory;

   public SocketFactoryWrapper(SocketFactory legacyFactory) {
      this.socketFactory = legacyFactory;
   }

   public <T extends Closeable> T connect(String hostname, int portNumber, PropertySet pset, int loginTimeout) throws IOException {
      this.rawSocket = this.socketFactory.connect(hostname, portNumber, pset.exposeAsProperties());
      return this.rawSocket;
   }

   public <T extends Closeable> T performTlsHandshake(SocketConnection socketConnection, ServerSession serverSession, Log log) throws IOException {
      return super.performTlsHandshake(socketConnection, serverSession, log);
   }

   public void beforeHandshake() throws IOException {
      this.socketFactory.beforeHandshake();
   }

   public void afterHandshake() throws IOException {
      this.socketFactory.afterHandshake();
   }
}
