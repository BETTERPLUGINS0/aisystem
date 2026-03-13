package org.terraform.data;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record SimpleChunkLocation(String world, int x, int z) implements Cloneable {
   public SimpleChunkLocation(TerraformWorld tw, int x, int z) {
      this(tw.getName(), x, z);
   }

   public SimpleChunkLocation(String world, int x, int y, int z) {
      this(world, x >> 4, z >> 4);
   }

   public SimpleChunkLocation(@NotNull Chunk chunk) {
      this(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
   }

   public SimpleChunkLocation(String world, int x, int z) {
      this.world = world;
      this.x = x;
      this.z = z;
   }

   @NotNull
   public static SimpleChunkLocation of(@NotNull Block block) {
      return new SimpleChunkLocation(block.getWorld().getName(), block.getX() >> 4, block.getZ() >> 4);
   }

   public static SimpleChunkLocation chunkStrToLoc(@Nullable String chunk) {
      if (chunk == null) {
         return null;
      } else {
         String[] split = StringUtils.split(StringUtils.deleteWhitespace(chunk), ',');
         String world = split[0];
         int x = Integer.parseInt(split[1]);
         int z = Integer.parseInt(split[2]);
         return new SimpleChunkLocation(world, x, z);
      }
   }

   public String getWorld() {
      return this.world;
   }

   public int getX() {
      return this.x;
   }

   public int getZ() {
      return this.z;
   }

   @NotNull
   public SimpleChunkLocation getRelative(int nx, int nz) {
      return new SimpleChunkLocation(this.world, nx + this.x, nz + this.z);
   }

   @NotNull
   public SimpleChunkLocation clone() {
      return new SimpleChunkLocation(this.world, this.x, this.z);
   }

   @NotNull
   public String toString() {
      return this.world + ", " + this.x + ", " + this.z;
   }

   @NotNull
   public Chunk toChunk() {
      return Bukkit.getWorld(this.world).getChunkAt(this.x, this.z);
   }

   public String world() {
      return this.world;
   }

   public int x() {
      return this.x;
   }

   public int z() {
      return this.z;
   }
}
