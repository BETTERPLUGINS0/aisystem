package org.terraform.coregen.populatordata;

import java.lang.reflect.Method;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Beehive;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.loot.Lootable;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.bukkit.NativeGeneratorPatcherPopulator;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;

public class PopulatorDataSpigotAPI extends PopulatorDataAbstract implements IPopulatorDataBeehiveEditor, IPopulatorDataMinecartSpawner {
   public final LimitedRegion lr;
   private final TerraformWorld tw;
   private final int chunkX;
   private final int chunkZ;
   private static boolean canUseNewApi = false;
   private static Method addEntity;
   private static Method createEntity;

   public PopulatorDataSpigotAPI(LimitedRegion lr, TerraformWorld tw, int chunkX, int chunkZ) {
      this.lr = lr;
      this.tw = tw;
      this.chunkX = chunkX;
      this.chunkZ = chunkZ;
   }

   @NotNull
   public Material getType(int x, int y, int z) {
      if (!this.lr.isInRegion(x, y, z)) {
         return y > TerraformGenerator.seaLevel ? Material.AIR : Material.WATER;
      } else {
         return this.lr.getType(x, y, z);
      }
   }

   public BlockData getBlockData(int x, int y, int z) {
      if (!this.lr.isInRegion(x, y, z)) {
         return y > TerraformGenerator.seaLevel ? Bukkit.createBlockData(Material.AIR) : Bukkit.createBlockData(Material.WATER);
      } else {
         return this.lr.getBlockData(x, y, z);
      }
   }

   public void setType(int x, int y, int z, @NotNull Material type) {
      if (!this.lr.isInRegion(x, y, z)) {
         NativeGeneratorPatcherPopulator.pushChange(this.tw.getName(), x, y, z, Bukkit.createBlockData(type));
      } else {
         this.lr.setType(x, y, z, type);
      }
   }

   public void setBlockData(int x, int y, int z, @NotNull BlockData data) {
      if (!this.lr.isInRegion(x, y, z)) {
         NativeGeneratorPatcherPopulator.pushChange(this.tw.getName(), x, y, z, data);
      } else {
         this.lr.setBlockData(x, y, z, data);
      }
   }

   public Biome getBiome(int rawX, int rawZ) {
      if (!this.lr.isInRegion(rawX, 50, rawZ)) {
         TerraformGeneratorPlugin.logger.error("Tried to access biome outside of LR bounds at: " + rawX + "," + rawZ + " from LR centered at chunk " + this.chunkX + "," + this.chunkZ);
         return Biome.PLAINS;
      } else {
         return this.lr.getBiome(rawX, 50, rawZ);
      }
   }

   public void addEntity(int rawX, int rawY, int rawZ, @NotNull EntityType type) {
      if (!this.lr.isInRegion(rawX, rawY, rawZ)) {
         TerraformGeneratorPlugin.logger.error("Tried to add entity outside of LR bounds at: " + rawX + "," + rawZ + " from LR centered at chunk " + this.chunkX + "," + this.chunkZ);
      } else {
         this.lr.spawnEntity(new Location(this.tw.getWorld(), (double)rawX, (double)rawY, (double)rawZ), type);
      }
   }

   public int getChunkX() {
      return this.chunkX;
   }

   public int getChunkZ() {
      return this.chunkZ;
   }

   public void setSpawner(int rawX, int rawY, int rawZ, @NotNull EntityType type) {
      if (TConfig.c.FEATURE_SPAWNERS_ENABLED) {
         if (!this.lr.isInRegion(rawX, 50, rawZ)) {
            TerraformGeneratorPlugin.logger.error("Tried to set spawner outside of LR bounds at: " + rawX + "," + rawZ + " from LR centered at chunk " + this.chunkX + "," + this.chunkZ);
         } else {
            this.setType(rawX, rawY, rawZ, Material.SPAWNER);

            try {
               CreatureSpawner spawner = (CreatureSpawner)this.lr.getBlockState(rawX, rawY, rawZ);
               spawner.setSpawnedType(type);
               spawner.update(true, false);
            } catch (ClassCastException var6) {
               TerraformGeneratorPlugin.logger.info("Failed to set spawner at " + rawX + "," + rawY + "," + rawZ);
            }

         }
      }
   }

   public void lootTableChest(int x, int y, int z, @NotNull TerraLootTable table) {
      if (!this.lr.isInRegion(x, y, z)) {
         TerraformGeneratorPlugin.logger.error("Tried to lootTableChest outside of LR bounds at: " + x + "," + z + " from LR centered at chunk " + this.chunkX + "," + this.chunkZ);
      } else {
         BlockState s = this.lr.getBlockState(x, y, z);
         if (s instanceof Lootable) {
            Lootable t = (Lootable)s;
            t.setLootTable(table.bukkit());
            s.update(true, false);
         }

      }
   }

   @NotNull
   public TerraformWorld getTerraformWorld() {
      return this.tw;
   }

   public void setBeehiveWithBee(int rawX, int rawY, int rawZ) {
      if (TConfig.areAnimalsEnabled()) {
         if (this.lr.isInRegion(rawX, rawY, rawZ)) {
            this.setType(rawX, rawY, rawZ, Material.BEE_NEST);

            try {
               Beehive bukkitBeehive = (Beehive)this.lr.getBlockState(rawX, rawY, rawZ);
               if (!canUseNewApi) {
                  TerraformGeneratorPlugin.injector.storeBee(bukkitBeehive);
               }
            } catch (ClassCastException var5) {
               TerraformGeneratorPlugin.logger.info("Failed to set beehive at " + rawX + "," + rawY + "," + rawZ);
            }

         }
      }
   }

   public void spawnMinecartWithChest(int x, int y, int z, @NotNull TerraLootTable table, Random random) {
      StorageMinecart e = (StorageMinecart)this.lr.spawnEntity(new Location(this.tw.getWorld(), (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F)), EntityType.MINECART_CHEST);
      e.setLootTable(table.bukkit());
   }
}
