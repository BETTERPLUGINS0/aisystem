package me.SuperRonanCraft.BetterRTP.references.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import lombok.NonNull;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.QueueData;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.QueueHandler;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.RTPWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class DatabaseQueue extends SQLite {
   public DatabaseQueue() {
      super(SQLite.DATABASE_TYPE.QUEUE);
   }

   public List<String> getTables() {
      List<String> list = new ArrayList();
      list.add("Queue");
      return list;
   }

   public void load() {
      if (QueueHandler.isEnabled()) {
         super.load();
      }

   }

   public List<QueueData> getInRange(DatabaseQueue.QueueRangeData range) {
      Connection conn = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      ArrayList queueDataList = new ArrayList();

      try {
         conn = this.getSQLConnection();
         ps = conn.prepareStatement("SELECT * FROM " + (String)this.tables.get(0) + " WHERE " + DatabaseQueue.COLUMNS.WORLD.name + " = '" + range.getWorld().getName() + "' AND " + DatabaseQueue.COLUMNS.X.name + " BETWEEN " + range.getXLow() + " AND " + range.getXHigh() + " AND " + DatabaseQueue.COLUMNS.Z.name + " BETWEEN " + range.getZLow() + " AND " + range.getZHigh() + " ORDER BY RANDOM() LIMIT " + 33);
         rs = ps.executeQuery();

         while(rs.next()) {
            long x = rs.getLong(DatabaseQueue.COLUMNS.X.name);
            long z = rs.getLong(DatabaseQueue.COLUMNS.Z.name);
            String worldName = rs.getString(DatabaseQueue.COLUMNS.WORLD.name);
            int id = rs.getInt(DatabaseQueue.COLUMNS.ID.name);
            long generated = rs.getLong(DatabaseQueue.COLUMNS.GENERATED.name);
            World world = Bukkit.getWorld(worldName);
            if (world != null) {
               queueDataList.add(new QueueData(new Location(world, (double)x, 69.0D, (double)z), generated, id));
            }
         }
      } catch (SQLException var18) {
         BetterRTP.getInstance().getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), var18);
      } finally {
         this.close(ps, rs, conn);
      }

      return queueDataList;
   }

   public QueueData addQueue(final Location loc) {
      String pre = "INSERT INTO ";
      String sql = pre + (String)this.tables.get(0) + " (" + DatabaseQueue.COLUMNS.X.name + ", " + DatabaseQueue.COLUMNS.Z.name + ", " + DatabaseQueue.COLUMNS.WORLD.name + ", " + DatabaseQueue.COLUMNS.GENERATED.name + " ) VALUES(?, ?, ?, ?)";
      List<Object> params = new ArrayList<Object>() {
         {
            this.add(loc.getBlockX());
            this.add(loc.getBlockZ());
            this.add(loc.getWorld().getName());
            this.add(System.currentTimeMillis());
         }
      };
      int database_id = this.createQueue(sql, params);
      return database_id >= 0 ? new QueueData(loc, System.currentTimeMillis(), database_id) : null;
   }

   public int getCount() {
      Connection conn = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      int count = 0;

      try {
         conn = this.getSQLConnection();
         ps = conn.prepareStatement("SELECT * FROM " + (String)this.tables.get(0));
         rs = ps.executeQuery();
         count = rs.getFetchSize();
      } catch (SQLException var9) {
         BetterRTP.getInstance().getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), var9);
      } finally {
         this.close(ps, rs, conn);
      }

      return count;
   }

   private int createQueue(String statement, @NonNull List<Object> params) {
      if (params == null) {
         throw new NullPointerException("params is marked non-null but is null");
      } else {
         Connection conn = null;
         PreparedStatement ps = null;
         int id = -1;

         try {
            conn = this.getSQLConnection();
            ps = conn.prepareStatement(statement, 1);
            Iterator<Object> it = params.iterator();

            for(int paramIndex = 1; it.hasNext(); ++paramIndex) {
               ps.setObject(paramIndex, it.next());
            }

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
               id = rs.getInt(1);
            }
         } catch (SQLException var12) {
            BetterRTP.getInstance().getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), var12);
         } finally {
            this.close(ps, (ResultSet)null, conn);
         }

         return id;
      }
   }

   public boolean removeLocation(final Location loc) {
      String sql = "DELETE FROM " + (String)this.tables.get(0) + " WHERE " + DatabaseQueue.COLUMNS.X.name + " = ? AND " + DatabaseQueue.COLUMNS.Z.name + " = ? AND " + DatabaseQueue.COLUMNS.WORLD.name + " = ?";
      List<Object> params = new ArrayList<Object>() {
         {
            this.add(loc.getBlockX());
            this.add(loc.getBlockZ());
            this.add(loc.getWorld().getName());
         }
      };
      return this.sqlUpdate(sql, params);
   }

   public static enum COLUMNS {
      ID("id", "integer PRIMARY KEY AUTOINCREMENT"),
      X("x", "long"),
      Z("z", "long"),
      WORLD("world", "varchar(32)"),
      GENERATED("generated", "long");

      public final String name;
      public final String type;

      private COLUMNS(String name, String type) {
         this.name = name;
         this.type = type;
      }

      // $FF: synthetic method
      private static DatabaseQueue.COLUMNS[] $values() {
         return new DatabaseQueue.COLUMNS[]{ID, X, Z, WORLD, GENERATED};
      }
   }

   public static class QueueRangeData {
      int xLow;
      int xHigh;
      int zLow;
      int zHigh;
      World world;

      public QueueRangeData(RTPWorld rtpWorld) {
         this.xLow = rtpWorld.getCenterX() - rtpWorld.getMaxRadius();
         this.xHigh = rtpWorld.getCenterX() + rtpWorld.getMaxRadius();
         this.zLow = rtpWorld.getCenterZ() - rtpWorld.getMaxRadius();
         this.zHigh = rtpWorld.getCenterZ() + rtpWorld.getMaxRadius();
         this.world = rtpWorld.getWorld();
      }

      public int getXLow() {
         return this.xLow;
      }

      public int getXHigh() {
         return this.xHigh;
      }

      public int getZLow() {
         return this.zLow;
      }

      public int getZHigh() {
         return this.zHigh;
      }

      public World getWorld() {
         return this.world;
      }
   }
}
