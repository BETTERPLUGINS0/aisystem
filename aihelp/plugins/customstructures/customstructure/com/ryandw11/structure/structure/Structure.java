package com.ryandw11.structure.structure;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.api.structaddon.StructureSection;
import com.ryandw11.structure.loottables.LootTable;
import com.ryandw11.structure.loottables.LootTableType;
import com.ryandw11.structure.schematic.SchematicHandler;
import com.ryandw11.structure.structure.properties.AdvancedSubSchematics;
import com.ryandw11.structure.structure.properties.BottomSpaceFill;
import com.ryandw11.structure.structure.properties.MaskProperty;
import com.ryandw11.structure.structure.properties.StructureLimitations;
import com.ryandw11.structure.structure.properties.StructureLocation;
import com.ryandw11.structure.structure.properties.StructureProperties;
import com.ryandw11.structure.structure.properties.SubSchematics;
import com.ryandw11.structure.utils.RandomCollection;
import com.sk89q.worldedit.WorldEditException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Structure {
   private final String name;
   private final String schematic;
   private final int probabilityNumerator;
   private final int probabilityDenominator;
   private final int priority;
   private final boolean isCompiled;
   private final String compiledSchematic;
   private final StructureLocation structureLocation;
   private final StructureProperties structureProperties;
   private final StructureLimitations structureLimitations;
   private final MaskProperty sourceMaskProperty;
   private final MaskProperty targetMaskProperty;
   private final SubSchematics subSchematics;
   private final AdvancedSubSchematics advancedSubSchematics;
   private final BottomSpaceFill bottomSpaceFill;
   private final Map<LootTableType, RandomCollection<LootTable>> lootTables;
   private final List<StructureSection> structureSections;
   private final double baseRotation;
   private double subSchemRotation = 0.0D;

   protected Structure(StructureBuilder builder) {
      this.name = builder.name;
      this.schematic = builder.schematic;
      this.probabilityNumerator = builder.probabilityNumerator;
      this.probabilityDenominator = builder.probabilityDenominator;
      this.priority = builder.priority;
      this.isCompiled = builder.isCompiled;
      this.compiledSchematic = builder.compiledSchematic;
      this.structureLocation = builder.structureLocation;
      this.structureProperties = builder.structureProperties;
      this.structureLimitations = builder.structureLimitations;
      this.sourceMaskProperty = builder.sourceMaskProperty;
      this.targetMaskProperty = builder.targetMaskProperty;
      this.subSchematics = builder.subSchematics;
      this.advancedSubSchematics = builder.advancedSubSchematics;
      this.bottomSpaceFill = builder.bottomSpaceFill;
      this.lootTables = builder.lootTables;
      this.structureSections = builder.structureSections;
      this.baseRotation = builder.baseRotation;
   }

   public String getName() {
      return this.name;
   }

   public String getSchematic() {
      return this.schematic;
   }

   public int getProbabilityNumerator() {
      return this.probabilityNumerator;
   }

   public int getProbabilityDenominator() {
      return this.probabilityDenominator;
   }

   public int getPriority() {
      return this.priority;
   }

   public boolean isCompiled() {
      return this.isCompiled;
   }

   @Nullable
   public String getCompiledSchematic() {
      return this.compiledSchematic;
   }

   public StructureLocation getStructureLocation() {
      return this.structureLocation;
   }

   public StructureProperties getStructureProperties() {
      return this.structureProperties;
   }

   public StructureLimitations getStructureLimitations() {
      return this.structureLimitations;
   }

   public MaskProperty getSourceMaskProperties() {
      return this.sourceMaskProperty;
   }

   public MaskProperty getTargetMaskProperties() {
      return this.targetMaskProperty;
   }

   public SubSchematics getSubSchematics() {
      return this.subSchematics;
   }

   public AdvancedSubSchematics getAdvancedSubSchematics() {
      return this.advancedSubSchematics;
   }

   public BottomSpaceFill getBottomSpaceFill() {
      return this.bottomSpaceFill;
   }

   public Map<LootTableType, RandomCollection<LootTable>> getLootTables() {
      return this.lootTables;
   }

   public RandomCollection<LootTable> getLootTables(LootTableType type) {
      return (RandomCollection)this.lootTables.get(type);
   }

   public List<StructureSection> getStructureSections() {
      return Collections.unmodifiableList(this.structureSections);
   }

   public double getBaseRotation() {
      return this.baseRotation;
   }

   public boolean canSpawn(@Nullable Block block, @NotNull Chunk chunk) {
      if (!this.getStructureLocation().canSpawnInWorld(chunk.getWorld())) {
         return false;
      } else if (block == null && !this.getStructureProperties().canSpawnInVoid()) {
         return false;
      } else if (block == null) {
         return ThreadLocalRandom.current().nextInt(0, this.getProbabilityDenominator() + 1) > this.getProbabilityNumerator() ? false : this.getStructureLocation().hasBiome(chunk.getBlock(0, 20, 0).getBiome());
      } else if (Math.abs(block.getX()) < this.getStructureLocation().getXLimitation()) {
         return false;
      } else if (Math.abs(block.getZ()) < this.getStructureLocation().getZLimitation()) {
         return false;
      } else if (!CustomStructures.getInstance().getStructureHandler().validDistance(this, block.getLocation())) {
         return false;
      } else if (!CustomStructures.getInstance().getStructureHandler().validSameDistance(this, block.getLocation())) {
         return false;
      } else {
         return ThreadLocalRandom.current().nextInt(0, this.getProbabilityDenominator() + 1) > this.getProbabilityNumerator() ? false : this.getStructureLocation().hasBiome(block.getBiome());
      }
   }

   public void setSubSchemRotation(double rot) {
      this.subSchemRotation = rot;
   }

   public double getSubSchemRotation() {
      return this.subSchemRotation;
   }

   public boolean spawn(Location location) {
      try {
         SchematicHandler.placeSchematic(location, this.getSchematic(), this.getStructureProperties().canPlaceAir(), this);
         return true;
      } catch (WorldEditException | IOException var3) {
         if (CustomStructures.getInstance().isDebug()) {
            var3.printStackTrace();
         }

         return false;
      }
   }
}
