package github.nighter.smartspawner.libs.mariadb.client.impl;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.HostAddress;
import github.nighter.smartspawner.libs.mariadb.Statement;
import github.nighter.smartspawner.libs.mariadb.client.Completion;
import github.nighter.smartspawner.libs.mariadb.client.context.RedoContext;
import github.nighter.smartspawner.libs.mariadb.client.util.ClosableLock;
import github.nighter.smartspawner.libs.mariadb.export.MaxAllowedPacketException;
import github.nighter.smartspawner.libs.mariadb.export.Prepare;
import github.nighter.smartspawner.libs.mariadb.message.ClientMessage;
import github.nighter.smartspawner.libs.mariadb.message.client.PreparePacket;
import github.nighter.smartspawner.libs.mariadb.message.client.RedoableClientMessage;
import github.nighter.smartspawner.libs.mariadb.message.client.RedoableWithPrepareClientMessage;
import github.nighter.smartspawner.libs.mariadb.message.server.PrepareResultPacket;
import github.nighter.smartspawner.libs.mariadb.util.log.Logger;
import github.nighter.smartspawner.libs.mariadb.util.log.Loggers;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ReplayClient extends StandardClient {
   private static final Logger logger = Loggers.getLogger(ReplayClient.class);

   public ReplayClient(Configuration conf, HostAddress hostAddress, ClosableLock lock, boolean skipPostCommands) throws SQLException {
      super(conf, hostAddress, lock, skipPostCommands);
   }

   public int sendQuery(ClientMessage message) throws SQLException {
      this.checkNotClosed();

      try {
         if (message instanceof RedoableClientMessage) {
            ((RedoableClientMessage)message).ensureReplayable(this.context);
         }

         return message.encode(this.writer, this.context);
      } catch (MaxAllowedPacketException var3) {
         if (var3.isMustReconnect()) {
            this.destroySocket();
            throw this.exceptionFactory.withSql(message.description()).create("Packet too big for current server max_allowed_packet value", "08000", var3);
         } else {
            throw this.exceptionFactory.withSql(message.description()).create("Packet too big for current server max_allowed_packet value", "HZ000", var3);
         }
      } catch (IOException var4) {
         this.destroySocket();
         throw this.exceptionFactory.withSql(message.description()).create("Socket error", "08000", var4);
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
               Prepare prepare = this.context.getPrepareCacheCmd(cmd, redoable.prep());
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
