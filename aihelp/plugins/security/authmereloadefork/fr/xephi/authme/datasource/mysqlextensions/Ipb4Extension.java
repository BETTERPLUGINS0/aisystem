package fr.xephi.authme.datasource.mysqlextensions;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.Columns;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.HooksSettings;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class Ipb4Extension extends MySqlExtension {
   private final String ipbPrefix;
   private final int ipbGroup;

   Ipb4Extension(Settings settings, Columns col) {
      super(settings, col);
      this.ipbPrefix = (String)settings.getProperty(HooksSettings.IPB_TABLE_PREFIX);
      this.ipbGroup = (Integer)settings.getProperty(HooksSettings.IPB_ACTIVATED_GROUP_ID);
   }

   public void saveAuth(PlayerAuth auth, Connection con) throws SQLException {
      String sql = "UPDATE " + this.ipbPrefix + this.tableName + " SET " + this.tableName + ".member_group_id=? WHERE " + this.col.NAME + "=?;";
      PreparedStatement pst2 = con.prepareStatement(sql);

      try {
         pst2.setInt(1, this.ipbGroup);
         pst2.setString(2, auth.getNickname());
         pst2.executeUpdate();
      } catch (Throwable var14) {
         if (pst2 != null) {
            try {
               pst2.close();
            } catch (Throwable var10) {
               var14.addSuppressed(var10);
            }
         }

         throw var14;
      }

      if (pst2 != null) {
         pst2.close();
      }

      long time = System.currentTimeMillis() / 1000L;
      sql = "UPDATE " + this.ipbPrefix + this.tableName + " SET " + this.tableName + ".joined=? WHERE " + this.col.NAME + "=?;";
      PreparedStatement pst2 = con.prepareStatement(sql);

      try {
         pst2.setLong(1, time);
         pst2.setString(2, auth.getNickname());
         pst2.executeUpdate();
      } catch (Throwable var12) {
         if (pst2 != null) {
            try {
               pst2.close();
            } catch (Throwable var9) {
               var12.addSuppressed(var9);
            }
         }

         throw var12;
      }

      if (pst2 != null) {
         pst2.close();
      }

      sql = "UPDATE " + this.ipbPrefix + this.tableName + " SET " + this.tableName + ".last_visit=? WHERE " + this.col.NAME + "=?;";
      pst2 = con.prepareStatement(sql);

      try {
         pst2.setLong(1, time);
         pst2.setString(2, auth.getNickname());
         pst2.executeUpdate();
      } catch (Throwable var13) {
         if (pst2 != null) {
            try {
               pst2.close();
            } catch (Throwable var11) {
               var13.addSuppressed(var11);
            }
         }

         throw var13;
      }

      if (pst2 != null) {
         pst2.close();
      }

   }
}
