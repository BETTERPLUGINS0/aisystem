package ac.grim.grimac.manager.violationdatabase;

import ac.grim.grimac.utils.anticheat.LogUtil;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import lombok.Generated;

public final class DatabaseUtils {
   public static byte[] uuidToBytes(UUID uuid) {
      ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
      bb.putLong(uuid.getMostSignificantBits());
      bb.putLong(uuid.getLeastSignificantBits());
      return bb.array();
   }

   public static UUID bytesToUuid(byte[] bytes) {
      if (bytes != null && bytes.length == 16) {
         ByteBuffer bb = ByteBuffer.wrap(bytes);
         long msb = bb.getLong();
         long lsb = bb.getLong();
         return new UUID(msb, lsb);
      } else {
         String var10002 = bytes == null ? "null" : bytes.length + " bytes";
         throw new IllegalArgumentException("UUID bytes must be 16 bytes long. Received: " + var10002);
      }
   }

   public static long getOrCreateId(Connection connection, DatabaseDialect dialect, String tableName, String stringColumnName, String value) throws SQLException {
      String insertSql = dialect.getInsertOrIgnoreSyntax(tableName, stringColumnName);

      PreparedStatement selectStmt;
      try {
         selectStmt = connection.prepareStatement(insertSql);

         try {
            selectStmt.setString(1, value);
            selectStmt.executeUpdate();
         } catch (Throwable var15) {
            if (selectStmt != null) {
               try {
                  selectStmt.close();
               } catch (Throwable var12) {
                  var15.addSuppressed(var12);
               }
            }

            throw var15;
         }

         if (selectStmt != null) {
            selectStmt.close();
         }
      } catch (SQLException var16) {
         if (!var16.getSQLState().equals(dialect.getUniqueConstraintViolationSQLState()) || var16.getErrorCode() != dialect.getUniqueConstraintViolationErrorCode()) {
            LogUtil.error("Failed to insert into " + tableName + ": " + value, var16);
            throw var16;
         }
      }

      selectStmt = connection.prepareStatement("SELECT id FROM " + tableName + " WHERE " + stringColumnName + " = ?");

      long var8;
      try {
         selectStmt.setString(1, value);
         ResultSet rs = selectStmt.executeQuery();

         try {
            if (!rs.next()) {
               throw new SQLException("Failed to retrieve ID for " + value + " from " + tableName);
            }

            var8 = rs.getLong("id");
         } catch (Throwable var13) {
            if (rs != null) {
               try {
                  rs.close();
               } catch (Throwable var11) {
                  var13.addSuppressed(var11);
               }
            }

            throw var13;
         }

         if (rs != null) {
            rs.close();
         }
      } catch (Throwable var14) {
         if (selectStmt != null) {
            try {
               selectStmt.close();
            } catch (Throwable var10) {
               var14.addSuppressed(var10);
            }
         }

         throw var14;
      }

      if (selectStmt != null) {
         selectStmt.close();
      }

      return var8;
   }

   @Generated
   private DatabaseUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
