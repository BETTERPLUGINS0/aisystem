package fr.xephi.authme.libs.org.postgresql.replication;

import java.nio.ByteBuffer;
import java.sql.SQLException;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface PGReplicationStream extends AutoCloseable {
   @Nullable
   ByteBuffer read() throws SQLException;

   @Nullable
   ByteBuffer readPending() throws SQLException;

   LogSequenceNumber getLastReceiveLSN();

   LogSequenceNumber getLastFlushedLSN();

   LogSequenceNumber getLastAppliedLSN();

   void setFlushedLSN(LogSequenceNumber var1);

   void setAppliedLSN(LogSequenceNumber var1);

   void forceUpdateStatus() throws SQLException;

   boolean isClosed();

   void close() throws SQLException;
}
