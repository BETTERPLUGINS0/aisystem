package com.volmit.iris.core.edit;

import com.volmit.iris.util.math.M;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;

public class BukkitBlockEditor implements BlockEditor {
   private final World world;

   public BukkitBlockEditor(World world) {
      this.world = var1;
   }

   public void set(int x, int y, int z, BlockData d) {
      this.world.getBlockAt(var1, var2, var3).setBlockData(var4, false);
   }

   public BlockData get(int x, int y, int z) {
      return this.world.getBlockAt(var1, var2, var3).getBlockData();
   }

   public void close() {
   }

   public long last() {
      return M.ms();
   }

   public void setBiome(int x, int z, Biome b) {
      this.world.setBiome(var1, var2, var3);
   }

   public void setBiome(int x, int y, int z, Biome b) {
      this.world.setBiome(var1, var2, var3, var4);
   }

   public Biome getBiome(int x, int y, int z) {
      return this.world.getBiome(var1, var2, var3);
   }

   public Biome getBiome(int x, int z) {
      return this.world.getBiome(var1, var2);
   }
}
