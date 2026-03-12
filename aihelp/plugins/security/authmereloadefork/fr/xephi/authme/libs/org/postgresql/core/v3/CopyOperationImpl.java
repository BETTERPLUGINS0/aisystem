package fr.xephi.authme.libs.org.postgresql.core.v3;

import fr.xephi.authme.libs.org.postgresql.copy.CopyOperation;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.sql.SQLException;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class CopyOperationImpl implements CopyOperation {
   @Nullable
   QueryExecutorImpl queryExecutor;
   int rowFormat;
   @Nullable
   int[] fieldFormats;
   long handledRowCount = -1L;

   void init(QueryExecutorImpl q, int fmt, int[] fmts) {
      this.queryExecutor = q;
      this.rowFormat = fmt;
      this.fieldFormats = fmts;
   }

   protected QueryExecutorImpl getQueryExecutor() {
      return (QueryExecutorImpl)Nullness.castNonNull(this.queryExecutor);
   }

   public void cancelCopy() throws SQLException {
      ((QueryExecutorImpl)Nullness.castNonNull(this.queryExecutor)).cancelCopy(this);
   }

   public int getFieldCount() {
      return ((int[])Nullness.castNonNull(this.fieldFormats)).length;
   }

   public int getFieldFormat(int field) {
      return ((int[])Nullness.castNonNull(this.fieldFormats))[field];
   }

   public int getFormat() {
      return this.rowFormat;
   }

   public boolean isActive() {
      return ((QueryExecutorImpl)Nullness.castNonNull(this.queryExecutor)).hasLockOn(this);
   }

   public void handleCommandStatus(String status) throws PSQLException {
      if (status.startsWith("COPY")) {
         int i = status.lastIndexOf(32);
         this.handledRowCount = i > 3 ? Long.parseLong(status.substring(i + 1)) : -1L;
      } else {
         throw new PSQLException(GT.tr("CommandComplete expected COPY but got: " + status), PSQLState.COMMUNICATION_ERROR);
      }
   }

   protected abstract void handleCopydata(byte[] var1) throws PSQLException;

   public long getHandledRowCount() {
      return this.handledRowCount;
   }
}
