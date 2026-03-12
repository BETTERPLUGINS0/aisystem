package fr.xephi.authme.libs.org.postgresql.core.v3;

import fr.xephi.authme.libs.org.postgresql.copy.CopyDual;
import fr.xephi.authme.libs.org.postgresql.util.ByteStreamWriter;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Queue;
import org.checkerframework.checker.nullness.qual.Nullable;

public class CopyDualImpl extends CopyOperationImpl implements CopyDual {
   private final Queue<byte[]> received = new ArrayDeque();

   public void writeToCopy(byte[] data, int off, int siz) throws SQLException {
      this.getQueryExecutor().writeToCopy(this, data, off, siz);
   }

   public void writeToCopy(ByteStreamWriter from) throws SQLException {
      this.getQueryExecutor().writeToCopy(this, from);
   }

   public void flushCopy() throws SQLException {
      this.getQueryExecutor().flushCopy(this);
   }

   public long endCopy() throws SQLException {
      return this.getQueryExecutor().endCopy(this);
   }

   @Nullable
   public byte[] readFromCopy() throws SQLException {
      return this.readFromCopy(true);
   }

   @Nullable
   public byte[] readFromCopy(boolean block) throws SQLException {
      if (this.received.isEmpty()) {
         this.getQueryExecutor().readFromCopy(this, block);
      }

      return (byte[])this.received.poll();
   }

   public void handleCommandStatus(String status) throws PSQLException {
   }

   protected void handleCopydata(byte[] data) {
      this.received.add(data);
   }
}
