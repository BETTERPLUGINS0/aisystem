package com.nisovin.shopkeepers.util.bukkit;

import com.nisovin.shopkeepers.api.util.ChunkCoords;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class MutableChunkCoords extends ChunkCoords {
   public static final String UNSET_WORLD_NAME = "<UNSET>";

   public MutableChunkCoords() {
      super("<UNSET>", 0, 0);
   }

   public MutableChunkCoords(String worldName, int chunkX, int chunkZ) {
      super(worldName, chunkX, chunkZ);
   }

   public MutableChunkCoords(Chunk chunk) {
      super(chunk);
   }

   public MutableChunkCoords(ChunkCoords chunkCoords) {
      super(chunkCoords);
   }

   public MutableChunkCoords(Location location) {
      super(location);
   }

   public MutableChunkCoords(Block block) {
      super(block);
   }

   public void set(Chunk chunk) {
      Validate.notNull(chunk, (String)"chunk is null");
      this.set(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
   }

   public void set(Block block) {
      Validate.notNull(block, (String)"block is null");
      this.set(block.getWorld().getName(), fromBlock(block.getX()), fromBlock(block.getZ()));
   }

   public void set(Location location) {
      this.set(LocationUtils.getWorld(location).getName(), fromBlock(location.getBlockX()), fromBlock(location.getBlockZ()));
   }

   public void set(ChunkCoords chunkCoords) {
      Validate.notNull(chunkCoords, (String)"chunkCoords is null");
      this.set(chunkCoords.getWorldName(), chunkCoords.getChunkX(), chunkCoords.getChunkZ());
   }

   public void set(String worldName, int chunkX, int chunkZ) {
      this.setWorldName(worldName);
      this.setChunkX(chunkX);
      this.setChunkZ(chunkZ);
   }

   public void unsetWorldName() {
      this.setWorldName("<UNSET>");
   }

   public boolean hasWorldName() {
      return !this.getWorldName().equals("<UNSET>");
   }

   public void setWorldName(String worldName) {
      super.setWorldName(worldName);
   }

   public void setChunkX(int chunkX) {
      super.setChunkX(chunkX);
   }

   public void setChunkZ(int chunkZ) {
      super.setChunkZ(chunkZ);
   }
}
