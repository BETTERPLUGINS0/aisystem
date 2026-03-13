package com.ryandw11.structure.utils;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.api.structaddon.StructureSection;
import com.ryandw11.structure.exceptions.StructureConfigurationException;
import com.ryandw11.structure.ignoreblocks.IgnoreBlocks;
import com.ryandw11.structure.schematic.SchematicHandler;
import com.ryandw11.structure.structure.PriorityStructureQueue;
import com.ryandw11.structure.structure.Structure;
import com.ryandw11.structure.structure.StructureHandler;
import com.ryandw11.structure.structure.properties.BlockLevelLimit;
import com.ryandw11.structure.structure.properties.StructureYSpawning;
import com.sk89q.worldedit.WorldEditException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

public class StructurePicker extends BukkitRunnable {
   private final CustomStructures plugin;
   private final PriorityStructureQueue priorityStructureQueue;
   private final IgnoreBlocks ignoreBlocks;
   private final Block bl;
   private final Chunk ch;
   private Block structureBlock;

   public StructurePicker(@Nullable Block bl, Chunk ch, CustomStructures plugin) {
      this.plugin = plugin;
      this.bl = bl;
      this.ch = ch;
      this.ignoreBlocks = plugin.getBlockIgnoreManager();
      StructureHandler structureHandler = plugin.getStructureHandler();
      if (structureHandler == null) {
         plugin.getLogger().warning("A structure is trying to spawn without the plugin initialization step being completed.");
         plugin.getLogger().warning("If you are using a fork of Spigot, this likely means that the fork does not adhere to the API standard properly.");
         throw new RuntimeException("Plugin Not Initialized.");
      } else {
         this.priorityStructureQueue = new PriorityStructureQueue(structureHandler.getStructures(), (Block)Objects.requireNonNull(bl), ch);
      }
   }

   public void run() {
      Structure gStructure = null;

      try {
         if (!this.priorityStructureQueue.hasNextStructure()) {
            this.cancel();
            return;
         }

         gStructure = this.priorityStructureQueue.getNextStructure();
         Structure structure = gStructure;

         assert gStructure != null;

         StructureYSpawning structureSpawnSettings = gStructure.getStructureLocation().getSpawnSettings();
         this.structureBlock = structureSpawnSettings.getHighestBlock(this.bl.getLocation());
         if (this.structureBlock.getType() == Material.VOID_AIR) {
            this.structureBlock = null;
         }

         if (this.structureBlock == null) {
            this.structureBlock = this.ch.getBlock(8, structureSpawnSettings.getHeight((Location)null), 8);
            this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
               this.plugin.getStructureHandler().putSpawnedStructure(this.structureBlock.getLocation(), gStructure);

               try {
                  SchematicHandler.placeSchematic(this.structureBlock.getLocation(), gStructure.getSchematic(), gStructure.getStructureProperties().canPlaceAir(), gStructure);
               } catch (WorldEditException | IOException var3) {
                  var3.printStackTrace();
               }

            });
            this.cancel();
            return;
         }

         if (gStructure.getStructureProperties().isIgnoringPlants() && this.ignoreBlocks.getBlocks().contains(this.structureBlock.getType())) {
            for(int i = this.structureBlock.getY(); i >= 4; --i) {
               if (!this.ignoreBlocks.getBlocks().contains(this.ch.getBlock(8, i, 8).getType()) && !this.ch.getBlock(8, i, 8).getType().isAir()) {
                  this.structureBlock = this.ch.getBlock(8, i, 8);
                  break;
               }
            }
         }

         if (structureSpawnSettings.isCalculateSpawnYFirst()) {
            this.structureBlock = this.ch.getBlock(8, structureSpawnSettings.getHeight(this.structureBlock.getLocation()), 8);
         }

         if (!gStructure.getStructureLimitations().hasWhitelistBlock(this.structureBlock)) {
            return;
         }

         if (gStructure.getStructureLimitations().hasBlacklistBlock(this.structureBlock)) {
            return;
         }

         if (!gStructure.getStructureProperties().canSpawnInWater() && this.structureBlock.getType() == Material.WATER) {
            return;
         }

         if (!gStructure.getStructureProperties().canSpawnInLavaLakes() && this.structureBlock.getType() == Material.LAVA) {
            return;
         }

         if (!structureSpawnSettings.isCalculateSpawnYFirst()) {
            this.structureBlock = this.ch.getBlock(8, structureSpawnSettings.getHeight(this.structureBlock.getLocation()), 8);
         }

         if (gStructure.getStructureLimitations().getWorldHeightRestriction() != -1 && this.structureBlock.getLocation().getY() > (double)(this.ch.getWorld().getMaxHeight() - gStructure.getStructureLimitations().getWorldHeightRestriction())) {
            return;
         }

         if (gStructure.getStructureLimitations().getBlockLevelLimit().isEnabled()) {
            BlockLevelLimit limit = gStructure.getStructureLimitations().getBlockLevelLimit();
            int total;
            int error;
            if (limit.getMode().equalsIgnoreCase("flat")) {
               for(total = limit.getX1() + this.structureBlock.getX(); total <= limit.getX2() + this.structureBlock.getX(); ++total) {
                  for(error = limit.getZ1() + this.structureBlock.getZ(); error <= limit.getZ2() + this.structureBlock.getZ(); ++error) {
                     Block top = this.ch.getWorld().getBlockAt(total, this.structureBlock.getY() + 1, error);
                     Block bottom = this.ch.getWorld().getBlockAt(total, this.structureBlock.getY() - 1, error);
                     if (!top.getType().isAir() && !this.ignoreBlocks.getBlocks().contains(top.getType())) {
                        return;
                     }

                     if (bottom.getType().isAir()) {
                        return;
                     }
                  }
               }
            } else if (limit.getMode().equalsIgnoreCase("flat_error")) {
               total = 0;
               error = 0;
               int x = limit.getX1() + this.structureBlock.getX();

               while(true) {
                  if (x > limit.getX2() + this.structureBlock.getX()) {
                     if ((double)error / (double)total > limit.getError()) {
                        return;
                     }
                     break;
                  }

                  for(int z = limit.getZ1() + this.structureBlock.getZ(); z <= limit.getZ2() + this.structureBlock.getZ(); ++z) {
                     Block top = this.ch.getWorld().getBlockAt(x, this.structureBlock.getY() + 1, z);
                     Block bottom = this.ch.getWorld().getBlockAt(x, this.structureBlock.getY() - 1, z);
                     if (!top.getType().isAir() && !this.ignoreBlocks.getBlocks().contains(top.getType())) {
                        ++error;
                     }

                     if (bottom.getType().isAir()) {
                        ++error;
                     }

                     total += 2;
                  }

                  ++x;
               }
            }
         }

         Iterator var15 = gStructure.getStructureSections().iterator();

         while(var15.hasNext()) {
            StructureSection section = (StructureSection)var15.next();

            try {
               if (!section.checkStructureConditions(structure, this.structureBlock, this.ch)) {
                  return;
               }
            } catch (Exception var11) {
               this.plugin.getLogger().severe(String.format("[CS Addon] An error has occurred when attempting to spawn the structure %s with the custom property %s!", structure.getName(), section.getName()));
               this.plugin.getLogger().severe("This is not a CustomStructures error! Please report this to the developer of the addon.");
               if (this.plugin.isDebug()) {
                  var11.printStackTrace();
               } else {
                  this.plugin.getLogger().severe("Enable debug mode to see the stack trace.");
               }

               return;
            }
         }

         this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
            this.plugin.getStructureHandler().putSpawnedStructure(this.structureBlock.getLocation(), structure);

            try {
               SchematicHandler.placeSchematic(this.structureBlock.getLocation(), structure.getSchematic(), structure.getStructureProperties().canPlaceAir(), structure);
            } catch (WorldEditException | IOException var3) {
               var3.printStackTrace();
            }

         });
         this.cancel();
      } catch (StructureConfigurationException var12) {
         this.cancel();
         if (gStructure != null) {
            this.plugin.getLogger().severe("A configuration error was encountered when attempting to spawn the structure: " + gStructure.getName());
         } else {
            this.plugin.getLogger().severe("A configuration error was encountered when attempting to spawn a structure.");
         }

         this.plugin.getLogger().severe(var12.getMessage());
      } catch (Exception var13) {
         this.cancel();
         this.plugin.getLogger().severe("An error was encountered during the schematic pasting section.");
         this.plugin.getLogger().severe("The task was stopped for the safety of your server!");
         this.plugin.getLogger().severe("For more information enable debug mode.");
         if (this.plugin.isDebug()) {
            var13.printStackTrace();
         }
      }

   }
}
