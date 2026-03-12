package me.SuperRonanCraft.BetterRTP.references.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.references.player.playerdata.PlayerData;

public class DatabasePlayers extends SQLite {
   public DatabasePlayers() {
      super(SQLite.DATABASE_TYPE.PLAYERS);
   }

   public List<String> getTables() {
      List<String> list = new ArrayList();
      list.add("Players");
      return list;
   }

   public void setupData(PlayerData data) {
      Connection conn = null;
      PreparedStatement ps = null;
      ResultSet rs = null;

      try {
         conn = this.getSQLConnection();
         ps = conn.prepareStatement("SELECT * FROM " + (String)this.tables.get(0) + " WHERE " + DatabasePlayers.COLUMNS.UUID.name + " = ?");
         ps.setString(1, data.player.getUniqueId().toString());
         rs = ps.executeQuery();
         if (rs.next()) {
            long count = rs.getLong(DatabasePlayers.COLUMNS.COUNT.name);
            long time = rs.getLong(DatabasePlayers.COLUMNS.LAST_COOLDOWN_DATE.name);
            data.setRtpCount(Math.toIntExact(count));
            data.setGlobalCooldown(time);
         }
      } catch (SQLException var12) {
         BetterRTP.getInstance().getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), var12);
      } finally {
         this.close(ps, rs, conn);
      }

   }

   public void setData(final PlayerData data) {
      String pre = "INSERT OR REPLACE INTO ";
      String sql = pre + (String)this.tables.get(0) + " (" + DatabasePlayers.COLUMNS.UUID.name + ", " + DatabasePlayers.COLUMNS.COUNT.name + ", " + DatabasePlayers.COLUMNS.LAST_COOLDOWN_DATE.name + " ) VALUES(?, ?, ?)";
      List<Object> params = new ArrayList<Object>() {
         {
            this.add(data.player.getUniqueId().toString());
            this.add(data.getRtpCount());
            this.add(data.getGlobalCooldown());
         }
      };
      this.sqlUpdate(sql, params);
   }

   public static enum COLUMNS {
      UUID("uuid", "varchar(32) PRIMARY KEY"),
      COUNT("count", "long"),
      LAST_COOLDOWN_DATE("last_rtp_date", "long");

      public final String name;
      public final String type;

      private COLUMNS(String name, String type) {
         this.name = name;
         this.type = type;
      }

      // $FF: synthetic method
      private static DatabasePlayers.COLUMNS[] $values() {
         return new DatabasePlayers.COLUMNS[]{UUID, COUNT, LAST_COOLDOWN_DATE};
      }
   }
}
