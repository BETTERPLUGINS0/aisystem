package com.ryandw11.structure.structure.properties;

import com.ryandw11.structure.exceptions.StructureConfigurationException;
import com.ryandw11.structure.structure.StructureBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.HeightMap;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class StructureLocation implements StructureProperty {
   private List<String> worlds;
   private List<String> worldBlacklist;
   private StructureYSpawning spawnY;
   private List<String> biomes;
   private double distanceFromOthers;
   private double distanceFromSame;
   private int xLimitation;
   private int zLimitation;

   /** @deprecated */
   @Deprecated
   public StructureLocation(StructureBuilder sb, FileConfiguration configuration) {
      this(configuration);
   }

   public StructureLocation(FileConfiguration fileConfiguration) {
      ConfigurationSection cs = fileConfiguration.getConfigurationSection("StructureLocation");
      if (cs == null) {
         throw new StructureConfigurationException("The `StructureLocation` property is mandatory, please add one to the file for the structure to be valid.");
      } else {
         if (cs.contains("Worlds")) {
            this.worlds = cs.getStringList("Worlds");
         } else {
            this.worlds = new ArrayList();
         }

         if (cs.contains("WorldBlacklist")) {
            this.worldBlacklist = cs.getStringList("WorldBlacklist");
         } else {
            this.worldBlacklist = new ArrayList();
         }

         this.spawnY = new StructureYSpawning(fileConfiguration);
         if (cs.contains("Biome")) {
            this.biomes = cs.getStringList("Biome");
         } else {
            this.biomes = new ArrayList();
         }

         if (cs.contains("DistanceFromOthers")) {
            this.distanceFromOthers = Math.max(0.0D, cs.getDouble("DistanceFromOthers"));
         } else {
            this.distanceFromOthers = 100.0D;
         }

         if (cs.contains("DistanceFromSame")) {
            this.distanceFromSame = Math.max(0.0D, cs.getDouble("DistanceFromSame"));
         } else {
            this.distanceFromSame = 100.0D;
         }

         this.xLimitation = 0;
         this.zLimitation = 0;
         if (cs.contains("SpawnDistance.x")) {
            this.xLimitation = cs.getInt("SpawnDistance.x");
         }

         if (cs.contains("SpawnDistance.z")) {
            this.zLimitation = cs.getInt("SpawnDistance.z");
         }

      }
   }

   public StructureLocation(List<String> worlds, StructureYSpawning spawnSettings, List<String> biomes) {
      this.worlds = worlds;
      this.worldBlacklist = new ArrayList();
      this.spawnY = spawnSettings;
      this.biomes = biomes;
      this.distanceFromOthers = 100.0D;
      this.distanceFromSame = 100.0D;
      this.xLimitation = 0;
      this.zLimitation = 0;
   }

   public StructureLocation() {
      this(new ArrayList(), new StructureYSpawning("top", HeightMap.WORLD_SURFACE, true), new ArrayList());
   }

   public List<String> getWorlds() {
      return this.worlds;
   }

   public List<String> getWorldBlacklist() {
      return this.worldBlacklist;
   }

   public boolean canSpawnInWorld(@NotNull World world) {
      if (!this.worlds.isEmpty() && !this.worlds.contains(world.getName())) {
         return false;
      } else {
         return !this.worldBlacklist.contains(world.getName());
      }
   }

   public StructureYSpawning getSpawnSettings() {
      return this.spawnY;
   }

   public List<String> getBiomes() {
      return this.biomes;
   }

   public void setWorlds(List<String> worlds) {
      this.worlds = worlds;
   }

   public void setSpawnSettings(StructureYSpawning spawnY) {
      this.spawnY = spawnY;
   }

   public void setBiomes(List<String> biomes) {
      this.biomes = biomes;
   }

   public boolean hasBiome(Biome b) {
      if (this.biomes.isEmpty()) {
         return true;
      } else {
         Iterator var2 = this.biomes.iterator();

         String biome;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            biome = (String)var2.next();
         } while(!b.toString().equalsIgnoreCase(biome.replace("minecraft:", "")));

         return true;
      }
   }

   public void setXLimitation(int x) {
      this.xLimitation = x;
   }

   public int getXLimitation() {
      return this.xLimitation;
   }

   public void setZLimitation(int z) {
      this.zLimitation = z;
   }

   public int getZLimitation() {
      return this.zLimitation;
   }

   public double getDistanceFromOthers() {
      return this.distanceFromOthers;
   }

   public void setDistanceFromOthers(double distance) {
      if (distance < 0.0D) {
         throw new IllegalArgumentException("Distance must be greater than 0!");
      } else {
         this.distanceFromOthers = distance;
      }
   }

   public double getDistanceFromSame() {
      return this.distanceFromSame;
   }

   public void setDistanceFromSame(double distance) {
      if (distance < 0.0D) {
         throw new IllegalArgumentException("Distance must be greater than 0!");
      } else {
         this.distanceFromSame = distance;
      }
   }

   public void saveToFile(ConfigurationSection configurationSection) {
      configurationSection.set("Worlds", this.worlds);
      configurationSection.set("WorldBlacklist", this.worldBlacklist);
      configurationSection.set("SpawnY", this.spawnY.getValue());
      configurationSection.set("SpawnYHeightMap", this.spawnY.getHeightMap().toString());
      configurationSection.set("Biome", this.biomes);
      configurationSection.set("DistanceFromOthers", this.distanceFromOthers);
      if (this.xLimitation > 0) {
         configurationSection.set("SpawnDistance.x", this.xLimitation);
      }

      if (this.zLimitation > 0) {
         configurationSection.set("SpawnDistance.z", this.zLimitation);
      }

   }
}
