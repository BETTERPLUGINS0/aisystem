package com.ryandw11.structure.structure.properties;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class StructureProperties {
   private boolean placeAir;
   private boolean randomRotation;
   private boolean ignorePlants;
   private boolean spawnInWater;
   private boolean spawnInLavaLakes;
   private boolean spawnInVoid;
   private boolean ignoreWater;

   public StructureProperties(FileConfiguration configuration) {
      ConfigurationSection cs = configuration.getConfigurationSection("StructureProperties");
      if (cs == null) {
         this.placeAir = true;
         this.randomRotation = false;
         this.ignorePlants = true;
         this.spawnInWater = true;
         this.spawnInLavaLakes = true;
         this.spawnInVoid = false;
      } else {
         this.placeAir = cs.contains("PlaceAir") && cs.getBoolean("PlaceAir");
         this.randomRotation = cs.contains("RandomRotation") && cs.getBoolean("RandomRotation");
         this.ignorePlants = cs.contains("IgnorePlants") && cs.getBoolean("IgnorePlants");
         this.spawnInWater = cs.contains("SpawnInWater") && cs.getBoolean("SpawnInWater");
         this.spawnInLavaLakes = cs.contains("SpawnInLavaLakes") && cs.getBoolean("SpawnInLavaLakes");
         this.spawnInVoid = cs.contains("SpawnInVoid") && cs.getBoolean("SpawnInVoid");
         this.ignoreWater = cs.contains("IgnoreWater") && cs.getBoolean("IgnoreWater");
      }
   }

   public StructureProperties() {
      this.placeAir = true;
      this.randomRotation = false;
      this.ignorePlants = true;
      this.spawnInWater = true;
      this.spawnInLavaLakes = true;
      this.spawnInVoid = false;
      this.ignoreWater = false;
   }

   public boolean canPlaceAir() {
      return this.placeAir;
   }

   public void setPlaceAir(boolean placeAir) {
      this.placeAir = placeAir;
   }

   public boolean isRandomRotation() {
      return this.randomRotation;
   }

   public void setRandomRotation(boolean randomRotation) {
      this.randomRotation = randomRotation;
   }

   public boolean isIgnoringPlants() {
      return this.ignorePlants;
   }

   public void setIgnorePlants(boolean ignorePlants) {
      this.ignorePlants = ignorePlants;
   }

   public boolean canSpawnInWater() {
      return this.spawnInWater;
   }

   public void setSpawnInWater(boolean spawnInWater) {
      this.spawnInWater = spawnInWater;
   }

   public boolean canSpawnInLavaLakes() {
      return this.spawnInLavaLakes;
   }

   public void setSpawnInLavaLakes(boolean spawnInLavaLakes) {
      this.spawnInLavaLakes = spawnInLavaLakes;
   }

   public boolean canSpawnInVoid() {
      return this.spawnInVoid;
   }

   public void setSpawnInVoid(boolean spawnInVoid) {
      this.spawnInVoid = spawnInVoid;
   }

   public boolean shouldIgnoreWater() {
      return this.ignoreWater;
   }

   public void setIgnoreWater(boolean ignoreWater) {
      this.ignoreWater = ignoreWater;
   }
}
