package fr.xephi.authme.libs.org.postgresql.core.v3;

import fr.xephi.authme.libs.org.postgresql.copy.CopyOut;
import java.sql.SQLException;
import org.checkerframework.checker.nullness.qual.Nullable;

public class CopyOutImpl extends CopyOperationImpl implements CopyOut {
   @Nullable
   private byte[] currentDataRow;

   @Nullable
   public byte[] readFromCopy() throws SQLException {
      return this.readFromCopy(true);
   }

   @Nullable
   public byte[] readFromCopy(boolean block) throws SQLException {
      this.currentDataRow = null;
      this.getQueryExecutor().readFromCopy(this, block);
      return this.currentDataRow;
   }

   protected void handleCopydata(byte[] data) {
      this.currentDataRow = data;
   }
}
