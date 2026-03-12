package fr.xephi.authme.datasource.mysqlextensions;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.Columns;
import fr.xephi.authme.security.crypts.HashedPassword;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.DatabaseSettings;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.OptionalInt;

public abstract class MySqlExtension {
   protected final Columns col;
   protected final String tableName;

   MySqlExtension(Settings settings, Columns col) {
      this.col = col;
      this.tableName = (String)settings.getProperty(DatabaseSettings.MYSQL_TABLE);
   }

   public void saveAuth(PlayerAuth auth, Connection con) throws SQLException {
   }

   public void extendAuth(PlayerAuth auth, int id, Connection con) throws SQLException {
   }

   public void changePassword(String user, HashedPassword password, Connection con) throws SQLException {
   }

   public void removeAuth(String user, Connection con) throws SQLException {
   }

   protected OptionalInt retrieveIdFromTable(String name, Connection con) throws SQLException {
      String sql = "SELECT " + this.col.ID + " FROM " + this.tableName + " WHERE " + this.col.NAME + "=?;";
      PreparedStatement pst = con.prepareStatement(sql);

      OptionalInt var6;
      label69: {
         try {
            label70: {
               pst.setString(1, name);
               ResultSet rs = pst.executeQuery();

               label71: {
                  try {
                     if (rs.next()) {
                        var6 = OptionalInt.of(rs.getInt(this.col.ID));
                        break label71;
                     }
                  } catch (Throwable var10) {
                     if (rs != null) {
                        try {
                           rs.close();
                        } catch (Throwable var9) {
                           var10.addSuppressed(var9);
                        }
                     }

                     throw var10;
                  }

                  if (rs != null) {
                     rs.close();
                  }
                  break label70;
               }

               if (rs != null) {
                  rs.close();
               }
               break label69;
            }
         } catch (Throwable var11) {
            if (pst != null) {
               try {
                  pst.close();
               } catch (Throwable var8) {
                  var11.addSuppressed(var8);
               }
            }

            throw var11;
         }

         if (pst != null) {
            pst.close();
         }

         return OptionalInt.empty();
      }

      if (pst != null) {
         pst.close();
      }

      return var6;
   }
}
