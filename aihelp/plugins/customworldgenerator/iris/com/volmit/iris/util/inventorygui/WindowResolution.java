package com.volmit.iris.util.inventorygui;

import org.bukkit.event.inventory.InventoryType;

public enum WindowResolution {
   W9_H6(9, 6, InventoryType.CHEST),
   W5_H1(5, 1, InventoryType.HOPPER),
   W3_H3(3, 3, InventoryType.DROPPER);

   private final int width;
   private final int maxHeight;
   private final InventoryType type;

   private WindowResolution(int w, int h, InventoryType type) {
      this.width = var3;
      this.maxHeight = var4;
      this.type = var5;
   }

   public int getMaxWidthOffset() {
      return (this.getWidth() - 1) / 2;
   }

   public int getWidth() {
      return this.width;
   }

   public int getMaxHeight() {
      return this.maxHeight;
   }

   public InventoryType getType() {
      return this.type;
   }

   // $FF: synthetic method
   private static WindowResolution[] $values() {
      return new WindowResolution[]{W9_H6, W5_H1, W3_H3};
   }
}
