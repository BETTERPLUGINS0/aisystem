package org.terraform.coregen.populatordata;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.biome.custombiomes.CustomBiomeType;
import org.terraform.coregen.NaturalSpawnType;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;

public class PopulatorDataPostGen extends PopulatorDataICABiomeWriterAbstract implements IPopulatorDataPhysicsCapable {
   private static int spawnerRetries = 0;
   @NotNull
   private final World w;
   @NotNull
   private final Chunk c;

   public PopulatorDataPostGen(@NotNull Chunk c) {
      this.w = c.getWorld();
      this.c = c;
   }

   @NotNull
   public World getWorld() {
      return this.w;
   }

   @NotNull
   public Chunk getChunk() {
      return this.c;
   }

   @NotNull
   public Material getType(int x, int y, int z) {
      return this.w.getBlockAt(x, y, z).getType();
   }

   @Nullable
   public BlockData getBlockData(int x, int y, int z) {
      return this.w.getBlockAt(x, y, z).getBlockData();
   }

   public void setType(int x, int y, int z, @NotNull Material type) {
      boolean isFragile = Tag.DOORS.isTagged(type) || Tag.CARPETS.isTagged(type) || type == Material.FARMLAND || type == Material.WATER;
      Block b = this.w.getBlockAt(x, y, z);
      b.setType(type, !isFragile);
   }

   public void setBlockData(int x, int y, int z, @NotNull BlockData data) {
      boolean isFragile = Tag.DOORS.isTagged(data.getMaterial()) || Tag.CARPETS.isTagged(data.getMaterial()) || data.getMaterial() == Material.FARMLAND || data.getMaterial() == Material.WATER;
      Block b = this.w.getBlockAt(x, y, z);
      b.setBlockData(data.clone(), !isFragile);
   }

   public void setType(int x, int y, int z, @NotNull Material type, boolean updatePhysics) {
      Block b = this.w.getBlockAt(x, y, z);
      b.setType(type, updatePhysics);
   }

   public void setBlockData(int x, int y, int z, @NotNull BlockData data, boolean updatePhysics) {
      Block b = this.w.getBlockAt(x, y, z);
      b.setBlockData(data.clone(), updatePhysics);
   }

   @NotNull
   public BlockState getBlockState(int x, int y, int z) {
      Block b = this.w.getBlockAt(x, y, z);
      return b.getState();
   }

   public void noPhysicsUpdateForce(int x, int y, int z, @NotNull BlockData data) {
      Block b = this.w.getBlockAt(x, y, z);
      b.setBlockData(data.clone(), false);
   }

   @Nullable
   public Biome getBiome(int rawX, int rawZ) {
      return this.w.getBlockAt(rawX, TerraformGenerator.seaLevel, rawZ).getBiome();
   }

   public void setBiome(int rawX, int rawY, int rawZ, @NotNull Biome biome) {
      this.w.setBiome(rawX, rawY, rawZ, biome);
   }

   public int getChunkX() {
      return this.c.getX();
   }

   public int getChunkZ() {
      return this.c.getZ();
   }

   public void addEntity(int x, int y, int z, @NotNull EntityType type) {
      Entity e = this.c.getWorld().spawnEntity(new Location(this.c.getWorld(), (double)x + 0.5D, (double)y + 0.3D, (double)z + 0.5D), type);
      e.setPersistent(true);
      if (e instanceof LivingEntity) {
         ((LivingEntity)e).setRemoveWhenFarAway(false);
      }

   }

   public void setSpawner(int rawX, int rawY, int rawZ, @NotNull EntityType type) {
      if (TConfig.c.FEATURE_SPAWNERS_ENABLED) {
         Block b = this.w.getBlockAt(rawX, rawY, rawZ);
         b.setType(Material.SPAWNER, false);

         try {
            CreatureSpawner spawner = (CreatureSpawner)b.getState();
            spawner.setSpawnedType(type);
            spawner.update();
         } catch (ClassCastException | IllegalStateException var7) {
            ++spawnerRetries;
            if (spawnerRetries > 10) {
               Bukkit.getLogger().info("Giving up on spawner at " + rawX + "," + rawY + "," + rawZ);
               spawnerRetries = 0;
               return;
            }

            Bukkit.getLogger().info("Failed to get state for spawner at " + rawX + "," + rawY + "," + rawZ + ", try " + spawnerRetries);
            this.setSpawner(rawX, rawY, rawZ, type);
         }

      }
   }

   public void lootTableChest(int x, int y, int z, TerraLootTable table) {
      TerraformGeneratorPlugin.injector.getICAData(this.w.getBlockAt(x, y, z).getChunk()).lootTableChest(x, y, z, table);
   }

   @NotNull
   public TerraformWorld getTerraformWorld() {
      return TerraformWorld.get(this.w);
   }

   public void setBiome(int rawX, int rawY, int rawZ, CustomBiomeType cbt, Biome fallback) {
      PopulatorDataICAAbstract icad = TerraformGeneratorPlugin.injector.getICAData(this.w.getBlockAt(rawX, rawY, rawZ).getChunk());
      if (icad instanceof PopulatorDataICABiomeWriterAbstract) {
         PopulatorDataICABiomeWriterAbstract biomeWriter = (PopulatorDataICABiomeWriterAbstract)icad;
         biomeWriter.setBiome(rawX, rawY, rawZ, cbt, fallback);
      }

   }

   public void registerNaturalSpawns(NaturalSpawnType type, int x0, int y0, int z0, int x1, int y1, int z1) {
      PopulatorDataICAAbstract icad = TerraformGeneratorPlugin.injector.getICAData(this.w.getBlockAt(x0, y0, z0).getChunk());
      if (icad instanceof PopulatorDataICABiomeWriterAbstract) {
         icad.registerNaturalSpawns(type, x0, y0, z0, x1, y1, z1);
      }

   }

   public void spawnMinecartWithChest(int x, int y, int z, TerraLootTable table, Random random) {
      PopulatorDataICAAbstract icad = TerraformGeneratorPlugin.injector.getICAData(this.w.getBlockAt(x, y, z).getChunk());
      if (icad instanceof PopulatorDataICABiomeWriterAbstract) {
         icad.spawnMinecartWithChest(x, y, z, table, random);
      }

   }
}
