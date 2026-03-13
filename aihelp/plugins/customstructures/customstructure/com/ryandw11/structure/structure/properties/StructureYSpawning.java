package com.ryandw11.structure.structure.properties;

import com.ryandw11.structure.exceptions.StructureConfigurationException;
import com.ryandw11.structure.utils.NumberStylizer;
import java.util.Objects;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;

public class StructureYSpawning {
   private boolean top = false;
   private boolean calculateSpawnYFirst = true;
   private final String value;
   private final HeightMap heightMap;

   public StructureYSpawning(FileConfiguration fc) {
      if (fc.contains("StructureLocation.SpawnY") && fc.contains("StructureLocation.SpawnYHeightMap")) {
         this.value = fc.getString("StructureLocation.SpawnY");

         try {
            this.heightMap = HeightMap.valueOf(((String)Objects.requireNonNull(fc.getString("StructureLocation.SpawnYHeightMap"))).toUpperCase());
         } catch (IllegalArgumentException var3) {
            throw new StructureConfigurationException("Invalid SpawnY HeightMap value! Please check your configuration!");
         }

         assert this.value != null;

         if (this.value.equalsIgnoreCase("top")) {
            this.top = true;
         }

         if (fc.contains("StructureLocation.CalculateSpawnFirst")) {
            this.calculateSpawnYFirst = fc.getBoolean("StructureLocation.CalculateSpawnFirst");
         }

      } else {
         throw new StructureConfigurationException("The structure must have a SpawnY value and SpawnY Height Map!");
      }
   }

   public StructureYSpawning(String value, HeightMap heightMap, boolean calculateSpawnYFirst) {
      this.value = value;
      this.heightMap = heightMap;
      if (value.equalsIgnoreCase("top")) {
         this.top = true;
      }

      this.calculateSpawnYFirst = calculateSpawnYFirst;
   }

   public String getValue() {
      return this.value;
   }

   public HeightMap getHeightMap() {
      return this.heightMap;
   }

   public boolean isTop() {
      return this.top;
   }

   public boolean isOceanFloor() {
      return this.heightMap == HeightMap.OCEAN_FLOOR;
   }

   public boolean isCalculateSpawnYFirst() {
      return this.calculateSpawnYFirst;
   }

   public Block getHighestBlock(Location loc) {
      return ((World)Objects.requireNonNull(loc.getWorld())).getHighestBlockAt(loc, this.heightMap);
   }

   public int getHeight(@Nullable Location location) {
      return NumberStylizer.getStylizedSpawnY(this.value, location);
   }
}
