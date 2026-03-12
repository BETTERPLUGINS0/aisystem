package fr.xephi.authme.libs.org.postgresql.core;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;

public class QueryExecutorCloseAction implements Closeable {
   private static final Logger LOGGER = Logger.getLogger(QueryExecutorBase.class.getName());
   private static final AtomicReferenceFieldUpdater<QueryExecutorCloseAction, PGStream> PG_STREAM_UPDATER = AtomicReferenceFieldUpdater.newUpdater(QueryExecutorCloseAction.class, PGStream.class, "pgStream");
   @Nullable
   private volatile PGStream pgStream;

   public QueryExecutorCloseAction(PGStream pgStream) {
      this.pgStream = pgStream;
   }

   public boolean isClosed() {
      PGStream pgStream = this.pgStream;
      return pgStream == null || pgStream.isClosed();
   }

   public void abort() {
      PGStream pgStream = this.pgStream;
      if (pgStream != null && PG_STREAM_UPDATER.compareAndSet(this, pgStream, (Object)null)) {
         try {
            LOGGER.log(Level.FINEST, " FE=> close socket");
            pgStream.getSocket().close();
         } catch (IOException var3) {
         }

      }
   }

   public void close() throws IOException {
      LOGGER.log(Level.FINEST, " FE=> Terminate");
      PGStream pgStream = this.pgStream;
      if (pgStream != null && PG_STREAM_UPDATER.compareAndSet(this, pgStream, (Object)null)) {
         this.sendCloseMessage(pgStream);
         if (!pgStream.isClosed()) {
            pgStream.flush();
            pgStream.close();
         }
      }
   }

   public void sendCloseMessage(PGStream pgStream) throws IOException {
      if (!pgStream.isClosed()) {
         int timeout = pgStream.getNetworkTimeout();
         if (timeout == 0 || timeout > 1000) {
            pgStream.setNetworkTimeout(1000);
         }

         pgStream.sendChar(88);
         pgStream.sendInteger4(4);
      }
   }
}
