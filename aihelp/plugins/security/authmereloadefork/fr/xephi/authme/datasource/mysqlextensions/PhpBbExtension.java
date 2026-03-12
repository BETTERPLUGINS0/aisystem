package fr.xephi.authme.datasource.mysqlextensions;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.Columns;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.HooksSettings;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.OptionalInt;

class PhpBbExtension extends MySqlExtension {
   private final String phpBbPrefix;
   private final int phpBbGroup;

   PhpBbExtension(Settings settings, Columns col) {
      super(settings, col);
      this.phpBbPrefix = (String)settings.getProperty(HooksSettings.PHPBB_TABLE_PREFIX);
      this.phpBbGroup = (Integer)settings.getProperty(HooksSettings.PHPBB_ACTIVATED_GROUP_ID);
   }

   public void saveAuth(PlayerAuth auth, Connection con) throws SQLException {
      OptionalInt authId = this.retrieveIdFromTable(auth.getNickname(), con);
      if (authId.isPresent()) {
         this.updateSpecificsOnSave(authId.getAsInt(), auth.getNickname(), con);
      }

   }

   private void updateSpecificsOnSave(int id, String name, Connection con) throws SQLException {
      String sql = "INSERT INTO " + this.phpBbPrefix + "user_group (group_id, user_id, group_leader, user_pending) VALUES (?,?,?,?);";
      PreparedStatement pst = con.prepareStatement(sql);

      try {
         pst.setInt(1, this.phpBbGroup);
         pst.setInt(2, id);
         pst.setInt(3, 0);
         pst.setInt(4, 0);
         pst.executeUpdate();
      } catch (Throwable var19) {
         if (pst != null) {
            try {
               pst.close();
            } catch (Throwable var13) {
               var19.addSuppressed(var13);
            }
         }

         throw var19;
      }

      if (pst != null) {
         pst.close();
      }

      sql = "UPDATE " + this.tableName + " SET " + this.tableName + ".username_clean=? WHERE " + this.col.NAME + "=?;";
      pst = con.prepareStatement(sql);

      try {
         pst.setString(1, name);
         pst.setString(2, name);
         pst.executeUpdate();
      } catch (Throwable var17) {
         if (pst != null) {
            try {
               pst.close();
            } catch (Throwable var12) {
               var17.addSuppressed(var12);
            }
         }

         throw var17;
      }

      if (pst != null) {
         pst.close();
      }

      sql = "UPDATE " + this.tableName + " SET " + this.tableName + ".group_id=? WHERE " + this.col.NAME + "=?;";
      pst = con.prepareStatement(sql);

      try {
         pst.setInt(1, this.phpBbGroup);
         pst.setString(2, name);
         pst.executeUpdate();
      } catch (Throwable var21) {
         if (pst != null) {
            try {
               pst.close();
            } catch (Throwable var15) {
               var21.addSuppressed(var15);
            }
         }

         throw var21;
      }

      if (pst != null) {
         pst.close();
      }

      long time = System.currentTimeMillis() / 1000L;
      sql = "UPDATE " + this.tableName + " SET " + this.tableName + ".user_regdate=? WHERE " + this.col.NAME + "=?;";
      PreparedStatement pst = con.prepareStatement(sql);

      try {
         pst.setLong(1, time);
         pst.setString(2, name);
         pst.executeUpdate();
      } catch (Throwable var16) {
         if (pst != null) {
            try {
               pst.close();
            } catch (Throwable var10) {
               var16.addSuppressed(var10);
            }
         }

         throw var16;
      }

      if (pst != null) {
         pst.close();
      }

      sql = "UPDATE " + this.tableName + " SET " + this.tableName + ".user_lastvisit=? WHERE " + this.col.NAME + "=?;";
      pst = con.prepareStatement(sql);

      try {
         pst.setLong(1, time);
         pst.setString(2, name);
         pst.executeUpdate();
      } catch (Throwable var20) {
         if (pst != null) {
            try {
               pst.close();
            } catch (Throwable var14) {
               var20.addSuppressed(var14);
            }
         }

         throw var20;
      }

      if (pst != null) {
         pst.close();
      }

      sql = "UPDATE " + this.phpBbPrefix + "config SET config_value = config_value + 1 WHERE config_name = 'num_users';";
      pst = con.prepareStatement(sql);

      try {
         pst.executeUpdate();
      } catch (Throwable var18) {
         if (pst != null) {
            try {
               pst.close();
            } catch (Throwable var11) {
               var18.addSuppressed(var11);
            }
         }

         throw var18;
      }

      if (pst != null) {
         pst.close();
      }

   }
}
