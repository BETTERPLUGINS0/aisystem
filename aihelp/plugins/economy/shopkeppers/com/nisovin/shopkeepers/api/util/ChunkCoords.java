package com.nisovin.shopkeepers.api.util;

import com.google.common.base.Preconditions;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ChunkCoords {
   private String worldName;
   private int chunkX;
   private int chunkZ;

   public static int fromBlock(int blockCoord) {
      return blockCoord >> 4;
   }

   public static boolean isSameChunk(@Nullable Location location1, @Nullable Location location2) {
      if (location1 != null && location2 != null) {
         World world1 = location1.getWorld();
         World world2 = location2.getWorld();
         if (world1 != null && world2 != null) {
            if (!world1.getName().equals(world2.getName())) {
               return false;
            } else {
               int chunkX1 = fromBlock(location1.getBlockX());
               int chunkX2 = fromBlock(location2.getBlockX());
               if (chunkX1 != chunkX2) {
                  return false;
               } else {
                  int chunkZ1 = fromBlock(location1.getBlockZ());
                  int chunkZ2 = fromBlock(location2.getBlockZ());
                  return chunkZ1 == chunkZ2;
               }
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean isChunkLoaded(@Nullable Location location) {
      if (location == null) {
         return false;
      } else {
         World world;
         try {
            world = location.getWorld();
         } catch (IllegalArgumentException var3) {
            return false;
         }

         return world == null ? false : world.isChunkLoaded(fromBlock(location.getBlockX()), fromBlock(location.getBlockZ()));
      }
   }

   public static ChunkCoords fromBlock(String worldName, int blockX, int blockZ) {
      return new ChunkCoords(worldName, fromBlock(blockX), fromBlock(blockZ));
   }

   public ChunkCoords(String worldName, int chunkX, int chunkZ) {
      Preconditions.checkNotNull(worldName, "worldName is null");
      Preconditions.checkArgument(!worldName.isEmpty(), "worldName is empty");
      this.worldName = worldName;
      this.chunkX = chunkX;
      this.chunkZ = chunkZ;
   }

   public ChunkCoords(Chunk chunk) {
      this(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
   }

   public ChunkCoords(ChunkCoords chunkCoords) {
      this(chunkCoords.getWorldName(), chunkCoords.getChunkX(), chunkCoords.getChunkZ());
   }

   public ChunkCoords(Location location) {
      this(getWorldName(location), fromBlock(location.getBlockX()), fromBlock(location.getBlockZ()));
   }

   private static String getWorldName(Location location) {
      Preconditions.checkNotNull(location, "location is null");
      World world = location.getWorld();
      Preconditions.checkNotNull(world, "location's world is null");
      return ((World)Unsafe.assertNonNull(world)).getName();
   }

   public ChunkCoords(Block block) {
      this(block.getWorld().getName(), fromBlock(block.getX()), fromBlock(block.getZ()));
   }

   public String getWorldName() {
      return this.worldName;
   }

   protected void setWorldName(String worldName) {
      Preconditions.checkNotNull(worldName, "worldName is null");
      Preconditions.checkArgument(!worldName.isEmpty(), "worldName is empty");
      this.worldName = worldName;
   }

   public int getChunkX() {
      return this.chunkX;
   }

   protected void setChunkX(int chunkX) {
      this.chunkX = chunkX;
   }

   public int getChunkZ() {
      return this.chunkZ;
   }

   protected void setChunkZ(int chunkZ) {
      this.chunkZ = chunkZ;
   }

   @Nullable
   public World getWorld() {
      return Bukkit.getWorld(this.worldName);
   }

   public boolean isChunkLoaded() {
      World world = this.getWorld();
      return world != null ? world.isChunkLoaded(this.chunkX, this.chunkZ) : false;
   }

   @Nullable
   public Chunk getChunk() {
      World world = this.getWorld();
      return world != null && world.isChunkLoaded(this.chunkX, this.chunkZ) ? world.getChunkAt(this.chunkX, this.chunkZ) : null;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + this.worldName.hashCode();
      result = 31 * result + this.chunkX;
      result = 31 * result + this.chunkZ;
      return result;
   }

   public boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ChunkCoords)) {
         return false;
      } else {
         ChunkCoords other = (ChunkCoords)obj;
         return this.matches(other.worldName, other.chunkX, other.chunkZ);
      }
   }

   public boolean matches(@Nullable String worldName, int chunkX, int chunkZ) {
      if (this.chunkX != chunkX) {
         return false;
      } else if (this.chunkZ != chunkZ) {
         return false;
      } else {
         return this.worldName.equals(worldName);
      }
   }

   public boolean matches(@Nullable Chunk chunk) {
      return chunk == null ? false : this.matches(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(this.getClass().getSimpleName());
      builder.append(" [worldName=");
      builder.append(this.worldName);
      builder.append(", chunkX=");
      builder.append(this.chunkX);
      builder.append(", chunkZ=");
      builder.append(this.chunkZ);
      builder.append("]");
      return builder.toString();
   }
}
