package fr.xephi.authme.datasource.converter;

import de.luricos.bukkit.xAuth.xAuth;
import de.luricos.bukkit.xAuth.database.DatabaseTables;
import de.luricos.bukkit.xAuth.utils.xAuthLog;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.util.FileUtils;
import fr.xephi.authme.util.Utils;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;

public class XAuthConverter implements Converter {
   @Inject
   @DataFolder
   private File dataFolder;
   @Inject
   private DataSource database;
   @Inject
   private PluginManager pluginManager;

   XAuthConverter() {
   }

   public void execute(CommandSender sender) {
      try {
         Class.forName("de.luricos.bukkit.xAuth.xAuth");
         this.convert(sender);
      } catch (ClassNotFoundException var3) {
         sender.sendMessage("xAuth has not been found, please put xAuth.jar in your plugin folder and restart!");
      }

   }

   private void convert(CommandSender sender) {
      if (this.pluginManager.getPlugin("xAuth") == null) {
         sender.sendMessage("[AuthMe] xAuth plugin not found");
      } else {
         File xAuthDb = new File(this.dataFolder.getParent(), FileUtils.makePath("xAuth", "xAuth.h2.db"));
         if (!xAuthDb.exists()) {
            sender.sendMessage("[AuthMe] xAuth H2 database not found, checking for MySQL or SQLite data...");
         }

         List<Integer> players = this.getXAuthPlayers();
         if (Utils.isCollectionEmpty(players)) {
            sender.sendMessage("[AuthMe] Error while importing xAuthPlayers: did not find any players");
         } else {
            sender.sendMessage("[AuthMe] Starting import...");
            Iterator var4 = players.iterator();

            while(var4.hasNext()) {
               int id = (Integer)var4.next();
               String pl = this.getIdPlayer(id);
               String psw = this.getPassword(id);
               if (psw != null && !psw.isEmpty() && pl != null) {
                  PlayerAuth auth = PlayerAuth.builder().name(pl.toLowerCase(Locale.ROOT)).realName(pl).password(psw, (String)null).build();
                  this.database.saveAuth(auth);
               }
            }

            sender.sendMessage("[AuthMe] Successfully converted from xAuth database");
         }
      }
   }

   private String getIdPlayer(int id) {
      String realPass = "";
      Connection conn = xAuth.getPlugin().getDatabaseController().getConnection();
      PreparedStatement ps = null;
      ResultSet rs = null;

      Object var7;
      try {
         String sql = String.format("SELECT `playername` FROM `%s` WHERE `id` = ?", xAuth.getPlugin().getDatabaseController().getTable(DatabaseTables.ACCOUNT));
         ps = conn.prepareStatement(sql);
         ps.setInt(1, id);
         rs = ps.executeQuery();
         if (!rs.next()) {
            var7 = null;
            return (String)var7;
         }

         realPass = rs.getString("playername").toLowerCase(Locale.ROOT);
         return realPass;
      } catch (SQLException var11) {
         xAuthLog.severe("Failed to retrieve name for account: " + id, var11);
         var7 = null;
      } finally {
         xAuth.getPlugin().getDatabaseController().close(conn, ps, rs);
      }

      return (String)var7;
   }

   private List<Integer> getXAuthPlayers() {
      List<Integer> xP = new ArrayList();
      Connection conn = xAuth.getPlugin().getDatabaseController().getConnection();
      PreparedStatement ps = null;
      ResultSet rs = null;

      ArrayList var6;
      try {
         String sql = String.format("SELECT * FROM `%s`", xAuth.getPlugin().getDatabaseController().getTable(DatabaseTables.ACCOUNT));
         ps = conn.prepareStatement(sql);
         rs = ps.executeQuery();

         while(rs.next()) {
            xP.add(rs.getInt("id"));
         }

         return xP;
      } catch (SQLException var10) {
         xAuthLog.severe("Cannot import xAuthPlayers", var10);
         var6 = new ArrayList();
      } finally {
         xAuth.getPlugin().getDatabaseController().close(conn, ps, rs);
      }

      return var6;
   }

   private String getPassword(int accountId) {
      String realPass = "";
      Connection conn = xAuth.getPlugin().getDatabaseController().getConnection();
      PreparedStatement ps = null;
      ResultSet rs = null;

      Object var7;
      try {
         String sql = String.format("SELECT `password`, `pwtype` FROM `%s` WHERE `id` = ?", xAuth.getPlugin().getDatabaseController().getTable(DatabaseTables.ACCOUNT));
         ps = conn.prepareStatement(sql);
         ps.setInt(1, accountId);
         rs = ps.executeQuery();
         if (rs.next()) {
            realPass = rs.getString("password");
            return realPass;
         }

         var7 = null;
      } catch (SQLException var11) {
         xAuthLog.severe("Failed to retrieve password hash for account: " + accountId, var11);
         var7 = null;
         return (String)var7;
      } finally {
         xAuth.getPlugin().getDatabaseController().close(conn, ps, rs);
      }

      return (String)var7;
   }
}
