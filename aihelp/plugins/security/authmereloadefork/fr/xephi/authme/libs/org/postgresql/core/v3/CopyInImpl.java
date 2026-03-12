package fr.xephi.authme.libs.org.postgresql.core.v3;

import fr.xephi.authme.libs.org.postgresql.copy.CopyIn;
import fr.xephi.authme.libs.org.postgresql.util.ByteStreamWriter;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import java.sql.SQLException;

public class CopyInImpl extends CopyOperationImpl implements CopyIn {
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

   protected void handleCopydata(byte[] data) throws PSQLException {
      throw new PSQLException(GT.tr("CopyIn copy direction can't receive data"), PSQLState.PROTOCOL_VIOLATION);
   }
}
