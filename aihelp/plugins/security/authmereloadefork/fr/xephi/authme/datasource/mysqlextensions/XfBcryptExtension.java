package fr.xephi.authme.datasource.mysqlextensions;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.Columns;
import fr.xephi.authme.security.crypts.HashedPassword;
import fr.xephi.authme.security.crypts.XfBCrypt;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.HooksSettings;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.OptionalInt;

class XfBcryptExtension extends MySqlExtension {
   private final String xfPrefix;
   private final int xfGroup;

   XfBcryptExtension(Settings settings, Columns col) {
      super(settings, col);
      this.xfPrefix = (String)settings.getProperty(HooksSettings.XF_TABLE_PREFIX);
      this.xfGroup = (Integer)settings.getProperty(HooksSettings.XF_ACTIVATED_GROUP_ID);
   }

   public void saveAuth(PlayerAuth auth, Connection con) throws SQLException {
      OptionalInt authId = this.retrieveIdFromTable(auth.getNickname(), con);
      if (authId.isPresent()) {
         this.updateXenforoTablesOnSave(auth, authId.getAsInt(), con);
      }

   }

   private void updateXenforoTablesOnSave(PlayerAuth auth, int id, Connection con) throws SQLException {
      String sql = "INSERT INTO " + this.xfPrefix + "user_authenticate (user_id, scheme_class, data) VALUES (?,?,?)";
      PreparedStatement pst = con.prepareStatement(sql);

      try {
         pst.setInt(1, id);
         pst.setString(2, "XenForo_Authentication_Core12");
         String serializedHash = XfBCrypt.serializeHash(auth.getPassword().getHash());
         byte[] bytes = serializedHash.getBytes();
         Blob blob = con.createBlob();
         blob.setBytes(1L, bytes);
         pst.setBlob(3, blob);
         pst.executeUpdate();
      } catch (Throwable var14) {
         if (pst != null) {
            try {
               pst.close();
            } catch (Throwable var12) {
               var14.addSuppressed(var12);
            }
         }

         throw var14;
      }

      if (pst != null) {
         pst.close();
      }

      sql = "UPDATE " + this.tableName + " SET " + this.tableName + ".user_group_id=? WHERE " + this.col.NAME + "=?;";
      pst = con.prepareStatement(sql);

      try {
         pst.setInt(1, this.xfGroup);
         pst.setString(2, auth.getNickname());
         pst.executeUpdate();
      } catch (Throwable var17) {
         if (pst != null) {
            try {
               pst.close();
            } catch (Throwable var11) {
               var17.addSuppressed(var11);
            }
         }

         throw var17;
      }

      if (pst != null) {
         pst.close();
      }

      sql = "UPDATE " + this.tableName + " SET " + this.tableName + ".permission_combination_id=? WHERE " + this.col.NAME + "=?;";
      pst = con.prepareStatement(sql);

      try {
         pst.setInt(1, this.xfGroup);
         pst.setString(2, auth.getNickname());
         pst.executeUpdate();
      } catch (Throwable var15) {
         if (pst != null) {
            try {
               pst.close();
            } catch (Throwable var9) {
               var15.addSuppressed(var9);
            }
         }

         throw var15;
      }

      if (pst != null) {
         pst.close();
      }

      sql = "INSERT INTO " + this.xfPrefix + "user_privacy (user_id, allow_view_profile, allow_post_profile, allow_send_personal_conversation, allow_view_identities, allow_receive_news_feed) VALUES (?,?,?,?,?,?)";
      pst = con.prepareStatement(sql);

      try {
         pst.setInt(1, id);
         pst.setString(2, "everyone");
         pst.setString(3, "members");
         pst.setString(4, "members");
         pst.setString(5, "everyone");
         pst.setString(6, "everyone");
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

      sql = "INSERT INTO " + this.xfPrefix + "user_group_relation (user_id, user_group_id, is_primary) VALUES (?,?,?)";
      pst = con.prepareStatement(sql);

      try {
         pst.setInt(1, id);
         pst.setInt(2, this.xfGroup);
         pst.setString(3, "1");
         pst.executeUpdate();
      } catch (Throwable var18) {
         if (pst != null) {
            try {
               pst.close();
            } catch (Throwable var13) {
               var18.addSuppressed(var13);
            }
         }

         throw var18;
      }

      if (pst != null) {
         pst.close();
      }

   }

   public void extendAuth(PlayerAuth auth, int id, Connection con) throws SQLException {
      PreparedStatement pst = con.prepareStatement("SELECT data FROM " + this.xfPrefix + "user_authenticate WHERE " + this.col.ID + "=?;");

      try {
         pst.setInt(1, id);
         ResultSet rs = pst.executeQuery();

         try {
            if (rs.next()) {
               Blob blob = rs.getBlob("data");
               byte[] bytes = blob.getBytes(1L, (int)blob.length());
               auth.setPassword(new HashedPassword(XfBCrypt.getHashFromBlob(bytes)));
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

   }

   public void changePassword(String user, HashedPassword password, Connection con) throws SQLException {
      OptionalInt authId = this.retrieveIdFromTable(user, con);
      if (authId.isPresent()) {
         int id = authId.getAsInt();
         String sql = "UPDATE " + this.xfPrefix + "user_authenticate SET data=? WHERE " + this.col.ID + "=?;";
         PreparedStatement pst = con.prepareStatement(sql);

         try {
            String serializedHash = XfBCrypt.serializeHash(password.getHash());
            byte[] bytes = serializedHash.getBytes();
            Blob blob = con.createBlob();
            blob.setBytes(1L, bytes);
            pst.setBlob(1, blob);
            pst.setInt(2, id);
            pst.executeUpdate();
         } catch (Throwable var14) {
            if (pst != null) {
               try {
                  pst.close();
               } catch (Throwable var12) {
                  var14.addSuppressed(var12);
               }
            }

            throw var14;
         }

         if (pst != null) {
            pst.close();
         }

         sql = "UPDATE " + this.xfPrefix + "user_authenticate SET scheme_class=? WHERE " + this.col.ID + "=?;";
         pst = con.prepareStatement(sql);

         try {
            pst.setString(1, "XenForo_Authentication_Core12");
            pst.setInt(2, id);
            pst.executeUpdate();
         } catch (Throwable var13) {
            if (pst != null) {
               try {
                  pst.close();
               } catch (Throwable var11) {
                  var13.addSuppressed(var11);
               }
            }

            throw var13;
         }

         if (pst != null) {
            pst.close();
         }
      }

   }

   public void removeAuth(String user, Connection con) throws SQLException {
      OptionalInt authId = this.retrieveIdFromTable(user, con);
      if (authId.isPresent()) {
         String sql = "DELETE FROM " + this.xfPrefix + "user_authenticate WHERE " + this.col.ID + "=?;";
         PreparedStatement xfDelete = con.prepareStatement(sql);

         try {
            xfDelete.setInt(1, authId.getAsInt());
            xfDelete.executeUpdate();
         } catch (Throwable var9) {
            if (xfDelete != null) {
               try {
                  xfDelete.close();
               } catch (Throwable var8) {
                  var9.addSuppressed(var8);
               }
            }

            throw var9;
         }

         if (xfDelete != null) {
            xfDelete.close();
         }
      }

   }
}
