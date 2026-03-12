package fr.xephi.authme.datasource;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

final class MySqlMigrater {
   private static ConsoleLogger logger = ConsoleLoggerFactory.get(MySqlMigrater.class);

   private MySqlMigrater() {
   }

   static void migrateLastIpColumn(Statement st, DatabaseMetaData metaData, String tableName, Columns col) throws SQLException {
      boolean isNotNullWithoutDefault = SqlDataSourceUtils.isNotNullColumn(metaData, tableName, col.LAST_IP) && SqlDataSourceUtils.getColumnDefaultValue(metaData, tableName, col.LAST_IP) == null;
      if (isNotNullWithoutDefault) {
         String sql = String.format("ALTER TABLE %s MODIFY %s VARCHAR(40) CHARACTER SET ascii COLLATE ascii_bin", tableName, col.LAST_IP);
         st.execute(sql);
         logger.info("Changed last login column to allow NULL values. Please verify the registration feature if you are hooking into a forum.");
      }

   }

   static void migrateLastLoginColumn(Statement st, DatabaseMetaData metaData, String tableName, Columns col) throws SQLException {
      ResultSet rs = metaData.getColumns((String)null, (String)null, tableName, col.LAST_LOGIN);

      label47: {
         int columnType;
         try {
            if (!rs.next()) {
               logger.warning("Could not get LAST_LOGIN meta data. This should never happen!");
               break label47;
            }

            columnType = rs.getInt("DATA_TYPE");
         } catch (Throwable var9) {
            if (rs != null) {
               try {
                  rs.close();
               } catch (Throwable var8) {
                  var9.addSuppressed(var8);
               }
            }

            throw var9;
         }

         if (rs != null) {
            rs.close();
         }

         if (columnType == 4) {
            migrateLastLoginColumnFromInt(st, tableName, col);
         }

         return;
      }

      if (rs != null) {
         rs.close();
      }

   }

   private static void migrateLastLoginColumnFromInt(Statement st, String tableName, Columns col) throws SQLException {
      logger.info("Migrating lastlogin column from int to bigint");
      String sql = String.format("ALTER TABLE %s MODIFY %s BIGINT;", tableName, col.LAST_LOGIN);
      st.execute(sql);
      int rangeStart = 1262304000;
      int rangeEnd = 1514678400;
      sql = String.format("UPDATE %s SET %s = %s * 1000 WHERE %s > %d AND %s < %d;", tableName, col.LAST_LOGIN, col.LAST_LOGIN, col.LAST_LOGIN, rangeStart, col.LAST_LOGIN, rangeEnd);
      int changedRows = st.executeUpdate(sql);
      logger.warning("You may have entries with invalid timestamps. Please check your data before purging. " + changedRows + " rows were migrated from seconds to milliseconds.");
   }

   static void addRegistrationDateColumn(Statement st, String tableName, Columns col) throws SQLException {
      st.executeUpdate("ALTER TABLE " + tableName + " ADD COLUMN " + col.REGISTRATION_DATE + " BIGINT NOT NULL DEFAULT 0;");
      long currentTimestamp = System.currentTimeMillis();
      int updatedRows = st.executeUpdate(String.format("UPDATE %s SET %s = %d;", tableName, col.REGISTRATION_DATE, currentTimestamp));
      logger.info("Created column '" + col.REGISTRATION_DATE + "' and set the current timestamp, " + currentTimestamp + ", to all " + updatedRows + " rows");
   }
}
