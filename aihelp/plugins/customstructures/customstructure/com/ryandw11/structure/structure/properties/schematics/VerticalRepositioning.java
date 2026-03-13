package com.ryandw11.structure.structure.properties.schematics;

import com.ryandw11.structure.utils.NumberStylizer;
import com.ryandw11.structure.utils.Pair;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

public class VerticalRepositioning {
   private final String range;
   private final String spawnY;
   private final HeightMap spawnYHeightMap;
   private final String noPointSolution;

   public VerticalRepositioning(String sectionName, ConfigurationSection section) {
      if (section.contains("Range")) {
         this.range = section.getString("Range");
      } else {
         this.range = "";
      }

      if (section.contains("SpawnY")) {
         this.spawnY = section.getString("SpawnY");
         if (section.contains("SpawnYHeightMap")) {
            this.spawnYHeightMap = HeightMap.valueOf(section.getString("SpawnYHeightMap").toUpperCase());
         } else {
            this.spawnYHeightMap = HeightMap.WORLD_SURFACE;
         }

         if (section.contains("NoPointSolution")) {
            this.noPointSolution = section.getString("NoPointSolution");
         } else {
            this.noPointSolution = "CURRENT";
         }

      } else {
         throw new RuntimeException(String.format("Unable to find SpawnY section for sub-schematic %s!", sectionName));
      }
   }

   public VerticalRepositioning(String range, String spawnY, HeightMap heightMap, String noPointSolution) {
      this.range = range;
      this.spawnY = spawnY;
      this.spawnYHeightMap = heightMap;
      this.noPointSolution = noPointSolution;
   }

   @Nullable
   public Pair<Integer, Integer> getRange() {
      return this.range.isEmpty() ? null : NumberStylizer.parseRangedInput(this.range);
   }

   public String getRawSpawnY() {
      return this.spawnY;
   }

   public int getSpawnY(@Nullable Location location) {
      return NumberStylizer.getStylizedSpawnY(this.spawnY, location);
   }

   public HeightMap getSpawnYHeightMap() {
      return this.spawnYHeightMap;
   }

   public String getNoPointSolution() {
      return this.noPointSolution;
   }
}
