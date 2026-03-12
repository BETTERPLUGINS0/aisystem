package fr.xephi.authme.libs.org.postgresql.jdbc;

import java.sql.ResultSet;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.dataflow.qual.Pure;

public class ResultWrapper {
   @Nullable
   private final ResultSet rs;
   private final long updateCount;
   private final long insertOID;
   @Nullable
   private ResultWrapper next;

   public ResultWrapper(@Nullable ResultSet rs) {
      this.rs = rs;
      this.updateCount = -1L;
      this.insertOID = -1L;
   }

   public ResultWrapper(long updateCount, long insertOID) {
      this.rs = null;
      this.updateCount = updateCount;
      this.insertOID = insertOID;
   }

   @Pure
   @Nullable
   public ResultSet getResultSet() {
      return this.rs;
   }

   public long getUpdateCount() {
      return this.updateCount;
   }

   public long getInsertOID() {
      return this.insertOID;
   }

   @Nullable
   public ResultWrapper getNext() {
      return this.next;
   }

   public void append(ResultWrapper newResult) {
      ResultWrapper tail;
      for(tail = this; tail.next != null; tail = tail.next) {
      }

      tail.next = newResult;
   }
}
