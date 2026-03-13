package com.ryandw11.structure.api.structaddon;

import com.ryandw11.structure.structure.Structure;
import com.ryandw11.structure.utils.NumberStylizer;
import com.ryandw11.structure.utils.Pair;
import java.util.Objects;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public abstract class StructureSign {
   private String[] arguments;
   private double signRotation;
   private double structureRotation;
   private Location structureMinimumLocation;
   private Location structureMaximumLocation;

   public final void initialize(String[] arguments, double signRotation, double structureRotation, Location structureMinimumLocation, Location structureMaximumLocation) {
      this.arguments = arguments;
      this.signRotation = signRotation;
      this.structureRotation = structureRotation;
      this.structureMinimumLocation = structureMinimumLocation;
      this.structureMaximumLocation = structureMaximumLocation;
   }

   public abstract boolean onStructureSpawn(@NotNull Location var1, @NotNull Structure var2);

   public final double getSignRotation() {
      return this.signRotation;
   }

   public final double getStructureRotation() {
      return this.structureRotation;
   }

   public final Location getStructureMinimumLocation() {
      return this.structureMinimumLocation;
   }

   public final Location getStructureMaximumLocation() {
      return this.structureMaximumLocation;
   }

   public final boolean hasArgument(int argNumber) {
      Objects.checkIndex(argNumber, this.arguments.length);
      return !this.arguments[argNumber].isEmpty();
   }

   public final String getStringArgument(int argNumber) {
      Objects.checkIndex(argNumber, this.arguments.length);
      return this.arguments[argNumber];
   }

   public final int getIntArgument(int argNumber) {
      Objects.checkIndex(argNumber, this.arguments.length);
      return Integer.parseInt(this.arguments[argNumber]);
   }

   public final int getIntArgument(int argNumber, int defaultValue) {
      try {
         return this.getIntArgument(argNumber);
      } catch (NumberFormatException var4) {
         return defaultValue;
      }
   }

   public final double getDoubleArgument(int argNumber) {
      Objects.checkIndex(argNumber, this.arguments.length);
      return Double.parseDouble(this.arguments[argNumber]);
   }

   public final double getDoubleArgument(int argNumber, double defaultValue) {
      try {
         return this.getDoubleArgument(argNumber);
      } catch (NumberFormatException var5) {
         return defaultValue;
      }
   }

   public final Pair<Integer, Integer> getRangedIntArgument(int argNumber) {
      Objects.checkIndex(argNumber, this.arguments.length);
      return NumberStylizer.parseRangedInput(this.arguments[argNumber]);
   }

   public final Pair<Integer, Integer> getRangedIntArgument(int argNumber, int defaultLowerValue, int defaultUpperValue) {
      try {
         return this.getRangedIntArgument(argNumber);
      } catch (NumberFormatException var5) {
         return Pair.of(defaultLowerValue, defaultUpperValue);
      }
   }

   public final int calculateRangedIntArgument(int argNumber) {
      Objects.checkIndex(argNumber, this.arguments.length);
      return NumberStylizer.retrieveRangedInput(this.arguments[argNumber]);
   }

   public final int calculateRangedIntArgument(int argNumber, int defaultValue) {
      try {
         return this.calculateRangedIntArgument(argNumber);
      } catch (NumberFormatException var4) {
         return defaultValue;
      }
   }

   public final int getStylizedIntArgument(int argNumber) {
      Objects.checkIndex(argNumber, this.arguments.length);
      return NumberStylizer.getStylizedInt(this.arguments[argNumber]);
   }
}
