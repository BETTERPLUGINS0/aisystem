package fr.xephi.authme.libs.org.postgresql.util;

import java.sql.SQLException;

public interface PGBinaryObject {
   void setByteValue(byte[] var1, int var2) throws SQLException;

   int lengthInBytes();

   void toBytes(byte[] var1, int var2);
}
