package com.ryandw11.structure.structure.properties.schematics;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SubSchematic {
   private final double weight;
   private String file;
   private boolean placeAir = false;
   private boolean useRotation = false;
   private VerticalRepositioning verticalRepositioning;

   public SubSchematic(@NotNull ConfigurationSection section, boolean advanced) {
      if (!section.contains("Weight") && advanced) {
         throw new RuntimeException("Format Error: " + section.getName() + " does not contain a weight!");
      } else {
         this.weight = advanced ? section.getDouble("Weight") : 0.0D;
         if (!section.contains("File")) {
            throw new RuntimeException("Format Error: " + section.getName() + " does not contain a file!");
         } else {
            this.file = section.getString("File");
            if (section.contains("PlaceAir")) {
               this.placeAir = section.getBoolean("PlaceAir");
            }

            if (section.contains("UseRotation")) {
               this.useRotation = section.getBoolean("UseRotation");
            }

            if (section.contains("VerticalRepositioning")) {
               this.verticalRepositioning = new VerticalRepositioning(section.getName(), section.getConfigurationSection("VerticalRepositioning"));
            }

         }
      }
   }

   public SubSchematic(String file, boolean placeAir, boolean useRotation, int weight, @Nullable VerticalRepositioning verticalRepositioning) {
      this.file = file;
      this.placeAir = placeAir;
      this.useRotation = useRotation;
      this.weight = (double)weight;
      this.verticalRepositioning = verticalRepositioning;
   }

   public void setPlaceAir(boolean placeAir) {
      this.placeAir = placeAir;
   }

   public boolean isPlacingAir() {
      return this.placeAir;
   }

   public void setUseRotation(boolean useRotation) {
      this.useRotation = useRotation;
   }

   public boolean isUsingRotation() {
      return this.useRotation;
   }

   public void setFile(String file) {
      this.file = file;
   }

   public String getFile() {
      return this.file;
   }

   public double getWeight() {
      return this.weight;
   }

   @Nullable
   public VerticalRepositioning getVerticalRepositioning() {
      return this.verticalRepositioning;
   }
}
