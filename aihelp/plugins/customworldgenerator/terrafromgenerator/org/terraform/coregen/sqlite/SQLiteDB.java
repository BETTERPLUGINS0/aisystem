package org.terraform.coregen.sqlite;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.main.TerraformGeneratorPlugin;

public class SQLiteDB {
   private static final Set<String> PREPARED_WORLDS = new HashSet();
   private static SQLiteDB i;

   @NotNull
   public static SQLiteDB get() {
      if (i == null) {
         i = new SQLiteDB();
      }

      return i;
   }

   public static void createTableIfNotExists(String world) {
      if (!PREPARED_WORLDS.contains(world)) {
         Connection conn = null;
         String dir = "plugins" + File.separator + "TerraformGenerator" + File.separator + world + ".db";

         try {
            String url = "jdbc:sqlite:" + dir;
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS CHUNKS (CHUNK STRING PRIMARY KEY     NOT NULL, POPULATED           BOOLEAN     NOT NULL); ";
            stmt.executeUpdate(sql);
            stmt.close();
            stmt = conn.createStatement();
            sql = "CREATE TABLE IF NOT EXISTS BLOCKDATA (CHUNK STRING NOT NULL,COORDS STRING NOT NULL, DATA STRING NOT NULL,PRIMARY KEY (CHUNK,COORDS)); ";
            stmt.executeUpdate(sql);
            stmt.close();
            PREPARED_WORLDS.add(world);
         } catch (SQLException var9) {
            TerraformGeneratorPlugin.logger.stackTrace(var9);
         } finally {
            closeConn(conn);
         }

      }
   }

   private static void closeConn(@Nullable Connection conn) {
      try {
         if (conn != null) {
            conn.close();
         }
      } catch (SQLException var2) {
         TerraformGeneratorPlugin.logger.stackTrace(var2);
      }

   }

   public void updateBlockData(String world, int chunkX, int chunkZ, int x, int y, int z, @NotNull BlockData data) {
      createTableIfNotExists(world);
      String dir = "plugins" + File.separator + "TerraformGenerator" + File.separator + world + ".db";
      String CHUNK = chunkX + "," + chunkZ;
      String COORDS = x + "," + y + "," + z;
      String DATA = data.toString();

      try {
         Class.forName("org.sqlite.JDBC");
         Connection c = DriverManager.getConnection("jdbc:sqlite:" + dir);
         c.setAutoCommit(false);
         Statement stmt = c.createStatement();
         String sql = "DELETE from BLOCKDATA where CHUNK='" + CHUNK + "' and COORDS='" + COORDS + "';";
         stmt.executeUpdate(sql);
         sql = "INSERT INTO BLOCKDATA (CHUNK,COORDS,DATA) VALUES ('" + CHUNK + "', '" + COORDS + "', '" + DATA + "');";
         stmt.executeUpdate(sql);
         stmt.close();
         c.commit();
         c.close();
      } catch (Exception var15) {
         TerraformGeneratorPlugin.logger.stackTrace(var15);
      }

   }

   public boolean[] fetchFromChunks(String world, int chunkX, int chunkZ) {
      createTableIfNotExists(world);
      String dir = "plugins" + File.separator + "TerraformGenerator" + File.separator + world + ".db";
      String key = chunkX + "," + chunkZ;
      boolean[] queryReply = new boolean[]{false, false};

      try {
         Class.forName("org.sqlite.JDBC");
         Connection c = DriverManager.getConnection("jdbc:sqlite:" + dir);
         c.setAutoCommit(false);
         Statement stmt = c.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM CHUNKS WHERE CHUNK='" + key + "';");
         if (rs.next()) {
            queryReply = new boolean[]{true, rs.getBoolean("POPULATED")};
         } else {
            queryReply = new boolean[]{false, false};
         }

         rs.close();
         stmt.close();
         c.close();
      } catch (Exception var10) {
         TerraformGeneratorPlugin.logger.stackTrace(var10);
      }

      return queryReply;
   }

   public void putChunk(String world, int chunkX, int chunkZ, boolean populated) {
      createTableIfNotExists(world);
      String dir = "plugins" + File.separator + "TerraformGenerator" + File.separator + world + ".db";

      try {
         Class.forName("org.sqlite.JDBC");
         Connection c = DriverManager.getConnection("jdbc:sqlite:" + dir);
         c.setAutoCommit(false);
         Statement stmt = c.createStatement();
         String key = chunkX + "," + chunkZ;
         String sql = "DELETE from CHUNKS where CHUNK='" + key + "';";
         stmt.executeUpdate(sql);
         sql = "INSERT INTO CHUNKS (CHUNK,POPULATED) VALUES ('" + key + "', '" + populated + "');";
         stmt.executeUpdate(sql);
         stmt.close();
         c.commit();
         c.close();
      } catch (Exception var10) {
         TerraformGeneratorPlugin.logger.stackTrace(var10);
      }

   }
}
