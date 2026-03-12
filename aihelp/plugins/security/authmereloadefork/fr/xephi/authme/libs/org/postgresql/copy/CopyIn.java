package fr.xephi.authme.libs.org.postgresql.copy;

import fr.xephi.authme.libs.org.postgresql.util.ByteStreamWriter;
import java.sql.SQLException;

public interface CopyIn extends CopyOperation {
   void writeToCopy(byte[] var1, int var2, int var3) throws SQLException;

   void writeToCopy(ByteStreamWriter var1) throws SQLException;

   void flushCopy() throws SQLException;

   long endCopy() throws SQLException;
}
