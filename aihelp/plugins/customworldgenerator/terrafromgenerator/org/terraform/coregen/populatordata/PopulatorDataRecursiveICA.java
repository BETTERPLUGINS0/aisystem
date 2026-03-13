package org.terraform.coregen.populatordata;

import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.data.SimpleChunkLocation;
import org.terraform.main.TerraformGeneratorPlugin;

public class PopulatorDataRecursiveICA extends PopulatorDataPostGen {
   @NotNull
   private final World w;
   @NotNull
   private final Chunk c;
   private final ConcurrentHashMap<SimpleChunkLocation, PopulatorDataICAAbstract> loadedChunks = new ConcurrentHashMap();

   public PopulatorDataRecursiveICA(@NotNull Chunk c) {
      super(c);
      this.c = c;
      this.w = c.getWorld();
   }

   @NotNull
   private PopulatorDataICAAbstract getData(int x, int z) {
      SimpleChunkLocation scl = new SimpleChunkLocation(this.w.getName(), x, z);
      synchronized(this.loadedChunks) {
         return (PopulatorDataICAAbstract)this.loadedChunks.computeIfAbsent(scl, (k) -> {
            int chunkX = x >> 4;
            int chunkZ = z >> 4;
            if (!this.w.isChunkLoaded(chunkX, chunkZ)) {
               this.w.loadChunk(chunkX, chunkZ);
            }

            PopulatorDataICAAbstract data = TerraformGeneratorPlugin.injector.getICAData(this.w.getChunkAt(chunkX, chunkZ));
            this.loadedChunks.put(scl, data);
            return data;
         });
      }
   }

   @NotNull
   public Material getType(int x, int y, int z) {
      return this.getData(x, z).getType(x, y, z);
   }

   public BlockData getBlockData(int x, int y, int z) {
      return this.getData(x, z).getBlockData(x, y, z);
   }

   public void setType(int x, int y, int z, @NotNull Material type) {
      this.getData(x, z).setType(x, y, z, type);
   }

   public void setBlockData(int x, int y, int z, @NotNull BlockData data) {
      this.getData(x, z).setBlockData(x, y, z, data);
   }

   public Biome getBiome(int rawX, int rawZ) {
      return this.getData(rawX, rawZ).getBiome(rawX, rawZ);
   }

   public void addEntity(int rawX, int rawY, int rawZ, @NotNull EntityType type) {
      this.getData(rawX, rawZ).addEntity(rawX, rawY, rawZ, type);
   }

   public int getChunkX() {
      return this.c.getX();
   }

   public int getChunkZ() {
      return this.c.getZ();
   }

   public void setSpawner(int rawX, int rawY, int rawZ, @NotNull EntityType type) {
      this.getData(rawX, rawZ).setSpawner(rawX, rawY, rawZ, type);
   }

   public void lootTableChest(int x, int y, int z, TerraLootTable table) {
      this.getData(x, z).lootTableChest(x, y, z, table);
   }
}
