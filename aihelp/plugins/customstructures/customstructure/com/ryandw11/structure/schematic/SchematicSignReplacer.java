package com.ryandw11.structure.schematic;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.api.structaddon.StructureSign;
import com.ryandw11.structure.structure.Structure;
import com.ryandw11.structure.structure.properties.AdvancedSubSchematics;
import com.ryandw11.structure.structure.properties.SubSchematics;
import com.ryandw11.structure.structure.properties.schematics.SubSchematic;
import com.ryandw11.structure.structure.properties.schematics.VerticalRepositioning;
import com.ryandw11.structure.utils.CSUtils;
import com.ryandw11.structure.utils.NumberStylizer;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.util.Vector;

public class SchematicSignReplacer {
   private SchematicSignReplacer() {
   }

   protected static void processAndReplaceSign(Location location, Location minLoc, Location maxLoc, Structure structure, double structureRotation) {
      CustomStructures plugin = CustomStructures.getInstance();
      if (location.getBlock().getState() instanceof Sign || location.getBlock().getState() instanceof WallSign) {
         Sign sign = (Sign)location.getBlock().getState();
         String firstLine = sign.getLine(0).trim();
         String secondLine = sign.getLine(1).trim();
         String thirdLine = sign.getLine(2).trim();
         String fourthLine = sign.getLine(3).trim();
         if (firstLine.startsWith("[")) {
            String signName = firstLine.replaceAll("\\[", "").replaceAll("]", "");
            if (plugin.getStructureSignHandler().structureSignExists(signName)) {
               BlockData var17 = location.getBlock().getBlockData();
               double signRotation;
               Vector direction;
               if (var17 instanceof org.bukkit.block.data.type.Sign) {
                  org.bukkit.block.data.type.Sign signData = (org.bukkit.block.data.type.Sign)var17;
                  direction = signData.getRotation().getDirection();
                  signRotation = Math.atan2(direction.getZ(), direction.getX());
                  if (direction.getX() != 0.0D) {
                     --signRotation;
                  } else {
                     ++signRotation;
                  }
               } else {
                  var17 = location.getBlock().getBlockData();
                  if (var17 instanceof WallSign) {
                     WallSign signData = (WallSign)var17;
                     direction = signData.getFacing().getDirection();
                     signRotation = Math.atan2(direction.getZ(), direction.getX());
                     if (direction.getX() != 0.0D) {
                        --signRotation;
                     } else {
                        ++signRotation;
                     }
                  } else {
                     signRotation = 0.0D;
                  }
               }

               Class structureSignClass = plugin.getStructureSignHandler().getStructureSign(signName);

               try {
                  StructureSign structureSign = (StructureSign)structureSignClass.getConstructor().newInstance();
                  String[] args = new String[]{secondLine, thirdLine, fourthLine};
                  structureSign.initialize(args, signRotation, structureRotation, minLoc, maxLoc);
                  if (structureSign.onStructureSpawn(location, structure)) {
                     location.getBlock().setType(Material.AIR);
                  }
               } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException var18) {
                  plugin.getLogger().severe(String.format("Unable to process structure sign %s in structure %s!", signName, structure.getName()));
                  plugin.getLogger().severe("Does that structure sign class have a default constructor?");
               }

            }
         }
      }
   }

   protected static void replaceSignWithSchematic(Location location, Structure parentStructure, int iteration) {
      CustomStructures plugin = CustomStructures.getInstance();
      SubSchematics subSchematics = parentStructure.getSubSchematics();
      AdvancedSubSchematics advancedSubSchematics = parentStructure.getAdvancedSubSchematics();
      Sign sign = (Sign)location.getBlock().getState();
      String firstLine = sign.getLine(0).trim();
      String secondLine = sign.getLine(1).trim();
      BlockData var11 = location.getBlock().getBlockData();
      double rotation;
      Vector direction;
      if (var11 instanceof org.bukkit.block.data.type.Sign) {
         org.bukkit.block.data.type.Sign signData = (org.bukkit.block.data.type.Sign)var11;
         direction = signData.getRotation().getDirection();
         rotation = Math.atan2(direction.getZ(), direction.getX());
         if (direction.getX() != 0.0D) {
            --rotation;
         } else {
            ++rotation;
         }

         parentStructure.setSubSchemRotation(rotation);
      } else {
         var11 = location.getBlock().getBlockData();
         if (var11 instanceof WallSign) {
            WallSign signData = (WallSign)var11;
            direction = signData.getFacing().getDirection();
            rotation = Math.atan2(direction.getZ(), direction.getX());
            if (direction.getX() != 0.0D) {
               --rotation;
            } else {
               ++rotation;
            }

            parentStructure.setSubSchemRotation(rotation);
         }
      }

      if (!firstLine.equalsIgnoreCase("[schematic]") && !firstLine.equalsIgnoreCase("[schem]")) {
         if (firstLine.equalsIgnoreCase("[advschem]")) {
            if (!advancedSubSchematics.containsCategory(secondLine)) {
               plugin.getLogger().warning("Cannot replace Advanced Sub-Schematic sign.");
               plugin.getLogger().warning(String.format("The category \"%s\" does not exist!", secondLine));
               return;
            }

            location.getBlock().setType(Material.AIR);
            SubSchematic subSchem = (SubSchematic)advancedSubSchematics.getCategory(secondLine).next();
            if (!subSchem.isUsingRotation()) {
               parentStructure.setSubSchemRotation(0.0D);
            }

            try {
               if (subSchem.getVerticalRepositioning() != null) {
                  VerticalRepositioning vertRep = subSchem.getVerticalRepositioning();
                  Location heightBlock = location.getWorld().getHighestBlockAt(location, vertRep.getSpawnYHeightMap()).getLocation();
                  int newSpawnY = vertRep.getSpawnY(heightBlock);
                  if (vertRep.getRange() != null && !CSUtils.isPairInLocalRange(vertRep.getRange(), location.getBlockY(), newSpawnY)) {
                     if (!vertRep.getNoPointSolution().equalsIgnoreCase("CURRENT")) {
                        if (vertRep.getNoPointSolution().equalsIgnoreCase("PREVENT_SPAWN")) {
                           return;
                        }

                        newSpawnY = NumberStylizer.getStylizedSpawnY(vertRep.getNoPointSolution(), location);
                        location = new Location(location.getWorld(), (double)location.getBlockX(), (double)newSpawnY, (double)location.getBlockZ());
                     }
                  } else {
                     location = new Location(location.getWorld(), (double)location.getBlockX(), (double)newSpawnY, (double)location.getBlockZ());
                  }
               }

               SchematicHandler.placeSchematic(location, subSchem.getFile(), subSchem.isPlacingAir(), parentStructure, iteration + 1);
            } catch (Exception var16) {
               plugin.getLogger().warning("An error has occurred when attempting to paste a sub schematic.");
               if (plugin.isDebug()) {
                  var16.printStackTrace();
               }
            }
         }
      } else {
         int number;
         if (secondLine.startsWith("[")) {
            try {
               number = NumberStylizer.retrieveRangedInput(secondLine);
            } catch (NumberFormatException var15) {
               plugin.getLogger().warning("Invalid schematic sign on structure. Cannot parse ranged number.");
               return;
            }
         } else {
            try {
               number = Integer.parseInt(secondLine);
            } catch (NumberFormatException var14) {
               plugin.getLogger().warning("Invalid schematic sign on structure. Cannot parse number.");
               return;
            }
         }

         if (number < -1 || number >= subSchematics.getSchematics().size()) {
            plugin.getLogger().warning("Invalid schematic sign on structure. Schematic number is not within the valid bounds.");
            return;
         }

         location.getBlock().setType(Material.AIR);
         SubSchematic subSchem = (SubSchematic)subSchematics.getSchematics().get(number);
         if (!subSchem.isUsingRotation()) {
            parentStructure.setSubSchemRotation(0.0D);
         }

         try {
            if (subSchem.getVerticalRepositioning() != null) {
               VerticalRepositioning vertRep = subSchem.getVerticalRepositioning();
               Location heightBlock = location.getWorld().getHighestBlockAt(location, vertRep.getSpawnYHeightMap()).getLocation();
               int newSpawnY = vertRep.getSpawnY(heightBlock);
               if (vertRep.getRange() != null && !CSUtils.isPairInLocalRange(vertRep.getRange(), location.getBlockY(), newSpawnY)) {
                  if (!vertRep.getNoPointSolution().equalsIgnoreCase("CURRENT")) {
                     if (vertRep.getNoPointSolution().equalsIgnoreCase("PREVENT_SPAWN")) {
                        return;
                     }

                     newSpawnY = NumberStylizer.getStylizedSpawnY(vertRep.getNoPointSolution(), location);
                     location = new Location(location.getWorld(), (double)location.getBlockX(), (double)newSpawnY, (double)location.getBlockZ());
                  }
               } else {
                  location = new Location(location.getWorld(), (double)location.getBlockX(), (double)newSpawnY, (double)location.getBlockZ());
               }
            }

            SchematicHandler.placeSchematic(location, subSchem.getFile(), subSchem.isPlacingAir(), parentStructure, iteration + 1);
         } catch (Exception var17) {
            plugin.getLogger().warning("An error has occurred when attempting to paste a sub schematic.");
            if (plugin.isDebug()) {
               var17.printStackTrace();
            }
         }
      }

   }
}
