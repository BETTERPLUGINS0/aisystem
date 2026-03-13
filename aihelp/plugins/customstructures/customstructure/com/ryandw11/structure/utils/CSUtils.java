package com.ryandw11.structure.utils;

import com.ryandw11.structure.structure.Structure;
import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public class CSUtils {
   public static String replacePlaceHolders(String command, Location signLocation, Location minLoc, Location maxLoc, Structure structure) {
      return command.replace("<world>", ((World)Objects.requireNonNull(signLocation.getWorld())).getName()).replace("<x>", signLocation.getBlockX().makeConcatWithConstants<invokedynamic>(signLocation.getBlockX())).replace("<y>", signLocation.getBlockY().makeConcatWithConstants<invokedynamic>(signLocation.getBlockY())).replace("<z>", signLocation.getBlockZ().makeConcatWithConstants<invokedynamic>(signLocation.getBlockZ())).replace("<structX1>", minLoc.getBlockX().makeConcatWithConstants<invokedynamic>(minLoc.getBlockX())).replace("<structY1>", minLoc.getBlockY().makeConcatWithConstants<invokedynamic>(minLoc.getBlockY())).replace("<structZ1>", minLoc.getBlockZ().makeConcatWithConstants<invokedynamic>(minLoc.getBlockZ())).replace("<structX2>", maxLoc.getBlockX().makeConcatWithConstants<invokedynamic>(maxLoc.getBlockX())).replace("<structY2>", maxLoc.getBlockY().makeConcatWithConstants<invokedynamic>(maxLoc.getBlockY())).replace("<structZ2>", maxLoc.getBlockZ().makeConcatWithConstants<invokedynamic>(maxLoc.getBlockZ())).replace("<minX>", minLoc.getBlockX().makeConcatWithConstants<invokedynamic>(minLoc.getBlockX())).replace("<minY>", minLoc.getBlockY().makeConcatWithConstants<invokedynamic>(minLoc.getBlockY())).replace("<minZ>", minLoc.getBlockZ().makeConcatWithConstants<invokedynamic>(minLoc.getBlockZ())).replace("<maxX>", maxLoc.getBlockX().makeConcatWithConstants<invokedynamic>(maxLoc.getBlockX())).replace("<maxY>", maxLoc.getBlockY().makeConcatWithConstants<invokedynamic>(maxLoc.getBlockY())).replace("<maxZ>", maxLoc.getBlockZ().makeConcatWithConstants<invokedynamic>(maxLoc.getBlockZ())).replace("<uuid>", UUID.randomUUID().toString()).replace("<structName>", structure.getName());
   }

   public static void renameConfigString(ConfigurationSection configurationSection, String originalName, String newName) {
      if (configurationSection.contains(originalName)) {
         configurationSection.set(newName, configurationSection.getString(originalName));
         configurationSection.set(originalName, (Object)null);
      }

   }

   public static void renameConfigBoolean(ConfigurationSection configurationSection, String originalName, String newName) {
      if (configurationSection.contains(originalName)) {
         configurationSection.set(newName, configurationSection.getBoolean(originalName));
         configurationSection.set(originalName, (Object)null);
      }

   }

   public static void renameConfigInteger(ConfigurationSection configurationSection, String originalName, String newName) {
      if (configurationSection.contains(originalName)) {
         configurationSection.set(newName, configurationSection.getInt(originalName));
         configurationSection.set(originalName, (Object)null);
      }

   }

   public static void renameConfigStringList(ConfigurationSection configurationSection, String originalName, String newName) {
      if (configurationSection.contains(originalName)) {
         configurationSection.set(newName, configurationSection.getStringList(originalName));
         configurationSection.set(originalName, (Object)null);
      }

   }

   public static void renameStringConfigurationSection(ConfigurationSection configurationSection, String originalName, String newName) {
      if (configurationSection.contains(originalName)) {
         Iterator var3 = configurationSection.getKeys(false).iterator();

         while(var3.hasNext()) {
            String key = (String)var3.next();
            configurationSection.set(newName + "." + key, configurationSection.getString(originalName + "." + key));
         }

         configurationSection.set(originalName, (Object)null);
      }
   }

   public static boolean isPairInRange(Pair<Integer, Integer> pair, int value) {
      if ((Integer)pair.getLeft() > value) {
         return false;
      } else {
         return (Integer)pair.getRight() > value;
      }
   }

   public static boolean isPairInLocalRange(Pair<Integer, Integer> pair, int localPin, int value) {
      if ((Integer)pair.getLeft() + localPin > value) {
         return false;
      } else {
         return (Integer)pair.getRight() + localPin > value;
      }
   }
}
