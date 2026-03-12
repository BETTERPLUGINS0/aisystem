package fr.xephi.authme.libs.org.postgresql;

import java.sql.SQLException;

public interface PGResultSetMetaData {
   String getBaseColumnName(int var1) throws SQLException;

   String getBaseTableName(int var1) throws SQLException;

   String getBaseSchemaName(int var1) throws SQLException;

   int getFormat(int var1) throws SQLException;
}
