package fr.xephi.authme.libs.org.postgresql;

import fr.xephi.authme.libs.org.postgresql.copy.CopyManager;
import fr.xephi.authme.libs.org.postgresql.fastpath.Fastpath;
import fr.xephi.authme.libs.org.postgresql.jdbc.AutoSave;
import fr.xephi.authme.libs.org.postgresql.jdbc.PreferQueryMode;
import fr.xephi.authme.libs.org.postgresql.largeobject.LargeObjectManager;
import fr.xephi.authme.libs.org.postgresql.replication.PGReplicationConnection;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PGobject;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.PasswordUtil;
import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface PGConnection {
   Array createArrayOf(String var1, @Nullable Object var2) throws SQLException;

   PGNotification[] getNotifications() throws SQLException;

   PGNotification[] getNotifications(int var1) throws SQLException;

   CopyManager getCopyAPI() throws SQLException;

   LargeObjectManager getLargeObjectAPI() throws SQLException;

   /** @deprecated */
   @Deprecated
   Fastpath getFastpathAPI() throws SQLException;

   /** @deprecated */
   @Deprecated
   void addDataType(String var1, String var2);

   void addDataType(String var1, Class<? extends PGobject> var2) throws SQLException;

   void setPrepareThreshold(int var1);

   int getPrepareThreshold();

   void setDefaultFetchSize(int var1) throws SQLException;

   int getDefaultFetchSize();

   int getBackendPID();

   void cancelQuery() throws SQLException;

   String escapeIdentifier(String var1) throws SQLException;

   String escapeLiteral(String var1) throws SQLException;

   PreferQueryMode getPreferQueryMode();

   AutoSave getAutosave();

   void setAutosave(AutoSave var1);

   PGReplicationConnection getReplicationAPI();

   default void alterUserPassword(String user, char[] newPassword, @Nullable String encryptionType) throws SQLException {
      try {
         Statement stmt = ((Connection)this).createStatement();

         try {
            if (encryptionType == null) {
               ResultSet rs = stmt.executeQuery("SHOW password_encryption");

               try {
                  if (!rs.next()) {
                     throw new PSQLException(GT.tr("Expected a row when reading password_encryption but none was found"), PSQLState.NO_DATA);
                  }

                  encryptionType = rs.getString(1);
                  if (encryptionType == null) {
                     throw new PSQLException(GT.tr("SHOW password_encryption returned null value"), PSQLState.NO_DATA);
                  }
               } catch (Throwable var16) {
                  if (rs != null) {
                     try {
                        rs.close();
                     } catch (Throwable var15) {
                        var16.addSuppressed(var15);
                     }
                  }

                  throw var16;
               }

               if (rs != null) {
                  rs.close();
               }
            }

            String sql = PasswordUtil.genAlterUserPasswordSQL(user, newPassword, encryptionType);
            stmt.execute(sql);
         } catch (Throwable var17) {
            if (stmt != null) {
               try {
                  stmt.close();
               } catch (Throwable var14) {
                  var17.addSuppressed(var14);
               }
            }

            throw var17;
         }

         if (stmt != null) {
            stmt.close();
         }
      } finally {
         Arrays.fill(newPassword, '\u0000');
      }

   }

   Map<String, String> getParameterStatuses();

   @Nullable
   String getParameterStatus(String var1);

   void setAdaptiveFetch(boolean var1);

   boolean getAdaptiveFetch();
}
