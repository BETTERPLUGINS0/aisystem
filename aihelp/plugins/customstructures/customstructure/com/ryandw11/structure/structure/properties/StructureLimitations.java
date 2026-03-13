package com.ryandw11.structure.structure.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class StructureLimitations {
   private int worldHeightRestriction;
   private int iterationLimit;
   private final List<String> whitelistSpawnBlocks;
   private final List<String> blacklistSpawnBlocks;
   private final BlockLevelLimit blockLevelLimit;
   private final Map<Material, Material> blockReplacement;
   private double replacementBlocksDelay;

   public StructureLimitations(FileConfiguration configuration) {
      if (!configuration.contains("StructureLimitations.IterationLimit")) {
         this.iterationLimit = 2;
      } else {
         this.iterationLimit = configuration.getInt("StructureLimitations.IterationLimit");
      }

      if (!configuration.contains("StructureLimitations.WorldHeightRestriction")) {
         this.worldHeightRestriction = -1;
      } else {
         this.worldHeightRestriction = Math.max(0, configuration.getInt("StructureLimitations.WorldHeightRestriction"));
      }

      if (!configuration.contains("StructureLimitations.WhitelistSpawnBlocks")) {
         this.whitelistSpawnBlocks = new ArrayList();
      } else {
         this.whitelistSpawnBlocks = configuration.getStringList("StructureLimitations.WhitelistSpawnBlocks");
      }

      if (!configuration.contains("StructureLimitations.BlacklistSpawnBlocks")) {
         this.blacklistSpawnBlocks = new ArrayList();
      } else {
         this.blacklistSpawnBlocks = configuration.getStringList("StructureLimitations.BlacklistSpawnBlocks");
      }

      this.blockLevelLimit = new BlockLevelLimit(configuration);
      this.replacementBlocksDelay = !configuration.contains("StructureLimitations.ReplaceBlockDelay") ? 0.0D : configuration.getDouble("StructureLimitations.ReplaceBlockDelay");
      this.blockReplacement = new HashMap();
      if (configuration.contains("StructureLimitations.ReplaceBlocks")) {
         Iterator var2 = ((ConfigurationSection)Objects.requireNonNull(configuration.getConfigurationSection("StructureLimitations.ReplaceBlocks"))).getKeys(false).iterator();

         while(var2.hasNext()) {
            String s = (String)var2.next();
            Material firstMaterial = Material.getMaterial(s);
            Material secondMaterial = Material.getMaterial((String)Objects.requireNonNull(configuration.getString("StructureLimitations.ReplaceBlocks." + s)));
            this.blockReplacement.put(firstMaterial, secondMaterial);
         }
      }

   }

   public StructureLimitations(List<String> whitelistSpawnBlocks, List<String> blacklistSpawnBlocks, BlockLevelLimit blockLevelLimit, Map<Material, Material> blockReplacement) {
      this.iterationLimit = 2;
      this.worldHeightRestriction = -1;
      this.whitelistSpawnBlocks = whitelistSpawnBlocks;
      this.blacklistSpawnBlocks = blacklistSpawnBlocks;
      this.blockLevelLimit = blockLevelLimit;
      this.blockReplacement = blockReplacement;
   }

   public void setIterationLimit(int iterationLimit) {
      this.iterationLimit = iterationLimit;
   }

   public int getIterationLimit() {
      return this.iterationLimit;
   }

   public List<String> getWhitelistBlocks() {
      return this.whitelistSpawnBlocks;
   }

   public List<String> getBlacklistBlocks() {
      return this.blacklistSpawnBlocks;
   }

   public boolean hasWhitelistBlock(Block b) {
      if (this.whitelistSpawnBlocks.isEmpty()) {
         return true;
      } else {
         Iterator var2 = this.whitelistSpawnBlocks.iterator();

         String block;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            block = (String)var2.next();
         } while(!block.equalsIgnoreCase(b.getType().toString()));

         return true;
      }
   }

   public boolean hasBlacklistBlock(Block b) {
      if (this.blacklistSpawnBlocks.isEmpty()) {
         return false;
      } else {
         Iterator var2 = this.blacklistSpawnBlocks.iterator();

         String block;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            block = (String)var2.next();
         } while(!block.equalsIgnoreCase(b.getType().toString()));

         return true;
      }
   }

   public BlockLevelLimit getBlockLevelLimit() {
      return this.blockLevelLimit;
   }

   public Map<Material, Material> getBlockReplacement() {
      return this.blockReplacement;
   }

   public double getReplacementBlocksDelay() {
      return this.replacementBlocksDelay;
   }

   public void setReplacementBlocksDelay(double value) {
      this.replacementBlocksDelay = value;
   }

   public int getWorldHeightRestriction() {
      return this.worldHeightRestriction;
   }

   public void setWorldHeightRestriction(int structureHeight) {
      this.worldHeightRestriction = Math.max(-1, structureHeight);
   }
}
