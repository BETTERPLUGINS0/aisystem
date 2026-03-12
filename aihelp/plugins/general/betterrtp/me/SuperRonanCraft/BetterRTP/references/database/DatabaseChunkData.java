package me.SuperRonanCraft.BetterRTP.references.database;

import java.util.ArrayList;
import java.util.List;
import me.SuperRonanCraft.BetterRTP.versions.AsyncHandler;
import org.bukkit.Chunk;
import org.bukkit.block.Biome;

public class DatabaseChunkData extends SQLite {
   public DatabaseChunkData() {
      super(SQLite.DATABASE_TYPE.CHUNK_DATA);
   }

   public List<String> getTables() {
      List<String> list = new ArrayList();
      list.add("ChunkData");
      return list;
   }

   public void addChunk(Chunk chunk, int maxy, Biome biome) {
      AsyncHandler.async(() -> {
         String pre = "INSERT OR REPLACE INTO ";
         String sql = pre + (String)this.tables.get(0) + " (" + DatabaseChunkData.COLUMNS.WORLD.name + ", " + DatabaseChunkData.COLUMNS.X.name + ", " + DatabaseChunkData.COLUMNS.Z.name + ", " + DatabaseChunkData.COLUMNS.BIOME.name + ", " + DatabaseChunkData.COLUMNS.MAX_Y.name + " ) VALUES(?, ?, ?, ?, ?)";
         List<Object> params = new ArrayList<Object>() {
            {
               this.add(chunk.getWorld().getName());
               this.add(chunk.getX());
               this.add(chunk.getZ());
               this.add(biome.name());
               this.add(maxy);
            }
         };
         this.sqlUpdate(sql, params);
      });
   }

   public static enum COLUMNS {
      ID("id", "integer PRIMARY KEY AUTOINCREMENT"),
      WORLD("world", "varchar(32)"),
      X("x", "long"),
      Z("z", "long"),
      BIOME("biome", "string"),
      MAX_Y("max_y", "integer");

      public final String name;
      public final String type;

      private COLUMNS(String name, String type) {
         this.name = name;
         this.type = type;
      }

      // $FF: synthetic method
      private static DatabaseChunkData.COLUMNS[] $values() {
         return new DatabaseChunkData.COLUMNS[]{ID, WORLD, X, Z, BIOME, MAX_Y};
      }
   }
}
