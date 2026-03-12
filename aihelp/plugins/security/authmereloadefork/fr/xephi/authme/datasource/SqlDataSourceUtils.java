package fr.xephi.authme.datasource;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class SqlDataSourceUtils {
   private static final ConsoleLogger logger = ConsoleLoggerFactory.get(SqlDataSourceUtils.class);

   private SqlDataSourceUtils() {
   }

   public static void logSqlException(SQLException e) {
      logger.logException("Error during SQL operation:", e);
   }

   public static Long getNullableLong(ResultSet rs, String columnName) throws SQLException {
      long longValue = rs.getLong(columnName);
      return rs.wasNull() ? null : longValue;
   }

   public static boolean isNotNullColumn(DatabaseMetaData metaData, String tableName, String columnName) throws SQLException {
      ResultSet rs = metaData.getColumns((String)null, (String)null, tableName, columnName);

      boolean var5;
      label51: {
         try {
            if (!rs.next()) {
               throw new IllegalStateException("Did not find meta data for column '" + columnName + "' while checking for not-null constraint");
            }

            int nullableCode = rs.getInt("NULLABLE");
            if (nullableCode == 0) {
               var5 = true;
               break label51;
            }

            if (nullableCode == 2) {
               logger.warning("Unknown nullable status for column '" + columnName + "'");
            }
         } catch (Throwable var7) {
            if (rs != null) {
               try {
                  rs.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }
            }

            throw var7;
         }

         if (rs != null) {
            rs.close();
         }

         return false;
      }

      if (rs != null) {
         rs.close();
      }

      return var5;
   }

   public static Object getColumnDefaultValue(DatabaseMetaData metaData, String tableName, String columnName) throws SQLException {
      ResultSet rs = metaData.getColumns((String)null, (String)null, tableName, columnName);

      Object var4;
      try {
         if (!rs.next()) {
            throw new IllegalStateException("Did not find meta data for column '" + columnName + "' while checking its default value");
         }

         var4 = rs.getObject("COLUMN_DEF");
      } catch (Throwable var7) {
         if (rs != null) {
            try {
               rs.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (rs != null) {
         rs.close();
      }

      return var4;
   }

   public static int getColumnSize(DatabaseMetaData metaData, String tableName, String columnName) throws SQLException {
      ResultSet rs = metaData.getColumns((String)null, (String)null, tableName, columnName);

      int var4;
      try {
         if (!rs.next()) {
            throw new IllegalStateException("Did not find meta data for column '" + columnName + "' while checking its size");
         }

         var4 = rs.getInt("COLUMN_SIZE");
      } catch (Throwable var7) {
         if (rs != null) {
            try {
               rs.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (rs != null) {
         rs.close();
      }

      return var4;
   }
}
