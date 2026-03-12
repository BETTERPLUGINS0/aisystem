package me.SuperRonanCraft.BetterRTP.references.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.CooldownData;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class DatabaseCooldowns extends SQLite {
   public DatabaseCooldowns() {
      super(SQLite.DATABASE_TYPE.COOLDOWN);
   }

   public List<String> getTables() {
      List<String> list = new ArrayList();
      if (!BetterRTP.getInstance().getCooldowns().isEnabled()) {
         return list;
      } else {
         List<String> disabledWorlds = BetterRTP.getInstance().getRTP().getDisabledWorlds();
         if (disabledWorlds == null) {
            disabledWorlds = new ArrayList();
         }

         List<World> worlds = Bukkit.getWorlds();
         if (!((List)disabledWorlds).isEmpty()) {
            Iterator var4 = worlds.iterator();

            while(var4.hasNext()) {
               World world = (World)var4.next();
               if (!((List)disabledWorlds).contains(world.getName())) {
                  list.add(world.getName());
               }
            }
         }

         return list;
      }
   }

   public void removePlayer(final UUID uuid, World world) {
      String sql = String.format("DELETE FROM `%s` WHERE %s = ?", world.getName(), DatabaseCooldowns.COLUMNS.UUID.name);
      List<Object> params = new ArrayList<Object>() {
         {
            this.add(uuid.toString());
         }
      };
      this.sqlUpdate(sql, params);
   }

   public CooldownData getCooldown(UUID uuid, World world) {
      Connection conn = null;
      PreparedStatement ps = null;
      ResultSet rs = null;

      CooldownData var7;
      try {
         conn = this.getSQLConnection();
         ps = conn.prepareStatement(String.format("SELECT * FROM `%s` WHERE %s = ?", world.getName(), DatabaseCooldowns.COLUMNS.UUID.name));
         ps.setString(1, uuid.toString());
         rs = ps.executeQuery();
         if (!rs.next()) {
            return null;
         }

         Long time = rs.getLong(DatabaseCooldowns.COLUMNS.COOLDOWN_DATE.name);
         var7 = new CooldownData(uuid, time);
      } catch (SQLException var11) {
         BetterRTP.getInstance().getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), var11);
         return null;
      } finally {
         this.close(ps, rs, conn);
      }

      return var7;
   }

   public void setCooldown(World world, final CooldownData data) {
      String pre = "INSERT OR REPLACE INTO ";
      String sql = pre + world.getName() + " (" + DatabaseCooldowns.COLUMNS.UUID.name + ", " + DatabaseCooldowns.COLUMNS.COOLDOWN_DATE.name + " ) VALUES(?, ?)";
      List<Object> params = new ArrayList<Object>() {
         {
            this.add(data.getUuid().toString());
            this.add(data.getTime());
         }
      };
      this.sqlUpdate(sql, params);
   }

   public static enum COLUMNS {
      UUID("uuid", "varchar(32) PRIMARY KEY"),
      COOLDOWN_DATE("date", "long");

      public final String name;
      public final String type;

      private COLUMNS(String name, String type) {
         this.name = name;
         this.type = type;
      }

      // $FF: synthetic method
      private static DatabaseCooldowns.COLUMNS[] $values() {
         return new DatabaseCooldowns.COLUMNS[]{UUID, COOLDOWN_DATE};
      }
   }
}
