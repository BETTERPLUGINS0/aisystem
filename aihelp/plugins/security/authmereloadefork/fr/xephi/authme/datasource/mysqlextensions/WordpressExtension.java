package fr.xephi.authme.datasource.mysqlextensions;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.Columns;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.HooksSettings;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.OptionalInt;

class WordpressExtension extends MySqlExtension {
   private final String wordpressPrefix;

   WordpressExtension(Settings settings, Columns col) {
      super(settings, col);
      this.wordpressPrefix = (String)settings.getProperty(HooksSettings.WORDPRESS_TABLE_PREFIX);
   }

   public void saveAuth(PlayerAuth auth, Connection con) throws SQLException {
      OptionalInt authId = this.retrieveIdFromTable(auth.getNickname(), con);
      if (authId.isPresent()) {
         this.saveSpecifics(auth, authId.getAsInt(), con);
      }

   }

   private void saveSpecifics(PlayerAuth auth, int id, Connection con) throws SQLException {
      String sql = "INSERT INTO " + this.wordpressPrefix + "usermeta (user_id, meta_key, meta_value) VALUES (?,?,?)";
      PreparedStatement pst = con.prepareStatement(sql);

      try {
         (new WordpressExtension.UserMetaBatchAdder(pst, id)).addMetaRow("first_name", "").addMetaRow("last_name", "").addMetaRow("nickname", auth.getNickname()).addMetaRow("description", "").addMetaRow("rich_editing", "true").addMetaRow("comment_shortcuts", "false").addMetaRow("admin_color", "fresh").addMetaRow("use_ssl", "0").addMetaRow("show_admin_bar_front", "true").addMetaRow(this.wordpressPrefix + "capabilities", "a:1:{s:10:\"subscriber\";b:1;}").addMetaRow(this.wordpressPrefix + "user_level", "0").addMetaRow("default_password_nag", "");
         pst.executeBatch();
         pst.clearBatch();
      } catch (Throwable var9) {
         if (pst != null) {
            try {
               pst.close();
            } catch (Throwable var8) {
               var9.addSuppressed(var8);
            }
         }

         throw var9;
      }

      if (pst != null) {
         pst.close();
      }

   }

   private static final class UserMetaBatchAdder {
      private final PreparedStatement pst;
      private final int userId;

      UserMetaBatchAdder(PreparedStatement pst, int userId) {
         this.pst = pst;
         this.userId = userId;
      }

      WordpressExtension.UserMetaBatchAdder addMetaRow(String metaKey, String metaValue) throws SQLException {
         this.pst.setInt(1, this.userId);
         this.pst.setString(2, metaKey);
         this.pst.setString(3, metaValue);
         this.pst.addBatch();
         return this;
      }
   }
}
