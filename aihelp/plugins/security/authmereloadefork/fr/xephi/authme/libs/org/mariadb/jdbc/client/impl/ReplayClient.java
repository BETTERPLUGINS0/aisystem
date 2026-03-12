package fr.xephi.authme.libs.org.mariadb.jdbc.client.impl;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.HostAddress;
import fr.xephi.authme.libs.org.mariadb.jdbc.Statement;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Completion;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.context.RedoContext;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.MaxAllowedPacketException;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.Prepare;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.ClientMessage;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.client.PreparePacket;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.client.RedoableClientMessage;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.client.RedoableWithPrepareClientMessage;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.server.PrepareResultPacket;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.log.Logger;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.log.Loggers;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ReplayClient extends StandardClient {
   private static final Logger logger = Loggers.getLogger(ReplayClient.class);

   public ReplayClient(Configuration conf, HostAddress hostAddress, ReentrantLock lock, boolean skipPostCommands) throws SQLException {
      super(conf, hostAddress, lock, skipPostCommands);
   }

   public int sendQuery(ClientMessage message) throws SQLException {
      this.checkNotClosed();

      try {
         if (message instanceof RedoableClientMessage) {
            ((RedoableClientMessage)message).ensureReplayable(this.context);
         }

         return message.encode(this.writer, this.context);
      } catch (IOException var3) {
         if (var3 instanceof MaxAllowedPacketException) {
            if (((MaxAllowedPacketException)var3).isMustReconnect()) {
               this.destroySocket();
               throw this.exceptionFactory.withSql(message.description()).create("Packet too big for current server max_allowed_packet value", "08000", var3);
            } else {
               throw this.exceptionFactory.withSql(message.description()).create("Packet too big for current server max_allowed_packet value", "HZ000", var3);
            }
         } else {
            this.destroySocket();
            throw this.exceptionFactory.withSql(message.description()).create("Socket error", "08000", var3);
         }
      }
   }

   public List<Completion> executePipeline(ClientMessage[] messages, Statement stmt, int fetchSize, long maxRows, int resultSetConcurrency, int resultSetType, boolean closeOnCompletion, boolean canRedo) throws SQLException {
      List<Completion> res = super.executePipeline(messages, stmt, fetchSize, maxRows, resultSetConcurrency, resultSetType, closeOnCompletion, canRedo);
      ((RedoContext)this.context).saveRedo(messages);
      return res;
   }

   public List<Completion> execute(ClientMessage message, Statement stmt, int fetchSize, long maxRows, int resultSetConcurrency, int resultSetType, boolean closeOnCompletion, boolean canRedo) throws SQLException {
      List<Completion> completions = super.execute(message, stmt, fetchSize, maxRows, resultSetConcurrency, resultSetType, closeOnCompletion, canRedo);
      ((RedoContext)this.context).saveRedo(message);
      return completions;
   }

   public void transactionReplay(TransactionSaver transactionSaver) throws SQLException {
      RedoableClientMessage[] buffers = transactionSaver.getBuffers();

      try {
         for(int i = 0; i < transactionSaver.getIdx(); ++i) {
            RedoableClientMessage querySaver = buffers[i];
            int responseNo;
            if (querySaver instanceof RedoableWithPrepareClientMessage) {
               RedoableWithPrepareClientMessage redoable = (RedoableWithPrepareClientMessage)querySaver;
               String cmd = redoable.getCommand();
               Prepare prepare = this.context.getPrepareCache().get(cmd, redoable.prep());
               if (prepare == null) {
                  PreparePacket preparePacket = new PreparePacket(cmd);
                  this.sendQuery(preparePacket);
                  prepare = (PrepareResultPacket)this.readPacket(preparePacket);
                  logger.info("replayed command after failover: " + preparePacket.description());
               }

               responseNo = querySaver.reEncode(this.writer, this.context, (Prepare)prepare);
            } else {
               responseNo = querySaver.reEncode(this.writer, this.context, (Prepare)null);
            }

            logger.info("replayed command after failover: " + querySaver.description());

            for(int j = 0; j < responseNo; ++j) {
               this.readResponse(querySaver);
            }
         }

      } catch (IOException var10) {
         throw this.context.getExceptionFactory().create("Socket error during transaction replay", "08000", var10);
      }
   }
}
