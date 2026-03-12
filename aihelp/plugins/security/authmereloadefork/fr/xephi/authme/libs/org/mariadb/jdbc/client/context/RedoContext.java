package fr.xephi.authme.libs.org.mariadb.jdbc.client.context;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.HostAddress;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.PrepareCache;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.impl.TransactionSaver;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.ExceptionFactory;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.ClientMessage;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.client.RedoableClientMessage;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.server.InitialHandshakePacket;

public class RedoContext extends BaseContext {
   private final TransactionSaver transactionSaver;

   public RedoContext(HostAddress hostAddress, InitialHandshakePacket handshake, long clientCapabilities, Configuration conf, ExceptionFactory exceptionFactory, PrepareCache prepareCache) {
      super(hostAddress, handshake, clientCapabilities, conf, exceptionFactory, prepareCache);
      this.transactionSaver = new TransactionSaver(conf.transactionReplaySize());
   }

   public void setServerStatus(int serverStatus) {
      this.serverStatus = serverStatus;
      if ((serverStatus & 1) == 0) {
         this.transactionSaver.clear();
      }

   }

   public void saveRedo(ClientMessage msg) {
      if (msg instanceof RedoableClientMessage) {
         RedoableClientMessage redoMsg = (RedoableClientMessage)msg;
         redoMsg.saveParameters();
         this.transactionSaver.add(redoMsg);
      }

   }

   public void saveRedo(ClientMessage[] msgs) {
      ClientMessage[] var2 = msgs;
      int var3 = msgs.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ClientMessage msg = var2[var4];
         this.saveRedo(msg);
      }

   }

   public TransactionSaver getTransactionSaver() {
      return this.transactionSaver;
   }
}
