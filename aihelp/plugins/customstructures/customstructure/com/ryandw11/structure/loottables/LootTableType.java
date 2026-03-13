package com.ryandw11.structure.loottables;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;

public enum LootTableType {
   CHEST(Material.CHEST),
   FURNACE(Material.FURNACE),
   HOPPER(Material.HOPPER),
   BREWING_STAND(Material.BREWING_STAND),
   BARREL(Material.BARREL),
   TRAPPED_CHEST(Material.TRAPPED_CHEST),
   DROPPER(Material.DROPPER),
   DISPENSER(Material.DISPENSER),
   BLAST_FURNACE("BLAST_FURNACE"),
   SMOKER("SMOKER"),
   SHULKER_BOX(Material.SHULKER_BOX),
   BLACK_SHULKER_BOX(Material.BLACK_SHULKER_BOX),
   BLUE_SHULKER_BOX(Material.BLUE_SHULKER_BOX),
   BROWN_SHULKER_BOX(Material.BROWN_SHULKER_BOX),
   CYAN_SHULKER_BOX(Material.CYAN_SHULKER_BOX),
   GRAY_SHULKER_BOX(Material.GRAY_SHULKER_BOX),
   GREEN_SHULKER_BOX(Material.GREEN_SHULKER_BOX),
   LIGHT_BLUE_SHULKER_BOX(Material.LIGHT_BLUE_SHULKER_BOX),
   LIME_SHULKER_BOX(Material.LIME_SHULKER_BOX),
   MAGENTA_SHULKER_BOX(Material.MAGENTA_SHULKER_BOX),
   ORANGE_SHULKER_BOX(Material.ORANGE_SHULKER_BOX),
   PINK_SHULKER_BOX(Material.PINK_SHULKER_BOX),
   PURPLE_SHULKER_BOX(Material.PURPLE_SHULKER_BOX),
   RED_SHULKER_BOX(Material.RED_SHULKER_BOX),
   WHITE_SHULKER_BOX(Material.WHITE_SHULKER_BOX),
   YELLOW_SHULKER_BOX(Material.YELLOW_SHULKER_BOX);

   private Material material;

   private LootTableType(String materialName) {
      try {
         this.material = Material.valueOf(materialName);
      } catch (IllegalArgumentException var5) {
         this.material = Material.CHEST;
      }

   }

   private LootTableType(Material material) {
      this.material = material;
   }

   public Material getMaterial() {
      return this.material;
   }

   public static List<LootTableType> valueOfList(String value) {
      String[] splitList = value.split(",");
      List<LootTableType> output = new ArrayList();
      String[] var3 = splitList;
      int var4 = splitList.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String s = var3[var5];
         output.add(valueOf(s));
      }

      return output;
   }

   public static LootTableType valueOf(Material material) {
      LootTableType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         LootTableType type = var1[var3];
         if (material == type.getMaterial()) {
            return type;
         }
      }

      return null;
   }

   public static boolean exists(String value) {
      LootTableType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         LootTableType s = var1[var3];
         if (s.toString().equalsIgnoreCase(value)) {
            return true;
         }
      }

      return false;
   }

   // $FF: synthetic method
   private static LootTableType[] $values() {
      return new LootTableType[]{CHEST, FURNACE, HOPPER, BREWING_STAND, BARREL, TRAPPED_CHEST, DROPPER, DISPENSER, BLAST_FURNACE, SMOKER, SHULKER_BOX, BLACK_SHULKER_BOX, BLUE_SHULKER_BOX, BROWN_SHULKER_BOX, CYAN_SHULKER_BOX, GRAY_SHULKER_BOX, GREEN_SHULKER_BOX, LIGHT_BLUE_SHULKER_BOX, LIME_SHULKER_BOX, MAGENTA_SHULKER_BOX, ORANGE_SHULKER_BOX, PINK_SHULKER_BOX, PURPLE_SHULKER_BOX, RED_SHULKER_BOX, WHITE_SHULKER_BOX, YELLOW_SHULKER_BOX};
   }
}
