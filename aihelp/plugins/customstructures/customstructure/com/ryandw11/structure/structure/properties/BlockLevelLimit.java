package com.ryandw11.structure.structure.properties;

import com.ryandw11.structure.exceptions.StructureConfigurationException;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class BlockLevelLimit {
   private String mode;
   private int x1;
   private int z1;
   private int x2;
   private int z2;
   private double error = -1.0D;

   public BlockLevelLimit(String mode, int x1, int z1, int x2, int z2) {
      this.mode = mode;
      this.x1 = x1;
      this.z1 = z1;
      this.x2 = x2;
      this.z2 = z2;
   }

   public BlockLevelLimit() {
      this.mode = "NONE";
   }

   public BlockLevelLimit(FileConfiguration fileConfiguration) {
      if (!fileConfiguration.contains("StructureLimitations.BlockLevelLimit")) {
         this.mode = "NONE";
      } else {
         ConfigurationSection cs = fileConfiguration.getConfigurationSection("StructureLimitations.BlockLevelLimit");

         assert cs != null;

         this.mode = cs.getString("Mode");
         this.x1 = cs.getInt("CornerOne.x");
         this.z1 = cs.getInt("CornerOne.z");
         this.x2 = cs.getInt("CornerTwo.x");
         this.z2 = cs.getInt("CornerTwo.z");
         if (cs.contains("Error")) {
            this.error = cs.getDouble("Error");
            if (this.error < 0.0D || this.error > 1.0D) {
               throw new StructureConfigurationException("'BlockLevelLimit.Error' must be greater than 0 and less than 1.");
            }
         }

         assert this.mode != null;

         if (this.mode.equalsIgnoreCase("flat_error") && !cs.contains("Error")) {
            throw new StructureConfigurationException("The BlockLevelLimit mode 'flat_error' must contain an error setting!");
         }
      }
   }

   public boolean isEnabled() {
      return !this.mode.equalsIgnoreCase("none");
   }

   public double getError() {
      return this.error;
   }

   public void setError(double error) {
      if (!(error < 0.0D) && !(error > 1.0D)) {
         this.error = error;
      } else {
         throw new IllegalArgumentException("Error value must be > 0 and < 1!");
      }
   }

   public String getMode() {
      return this.mode;
   }

   public void setMode(String mode) {
      this.mode = mode;
   }

   public int getX1() {
      return this.x1;
   }

   public int getX2() {
      return this.x2;
   }

   public int getZ1() {
      return this.z1;
   }

   public int getZ2() {
      return this.z2;
   }
}
