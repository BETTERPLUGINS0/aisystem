package com.nisovin.shopkeepers.util.inventory;

import com.nisovin.shopkeepers.util.java.MathUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.event.inventory.InventoryType;

public final class ChestLayout {
   public static final int SLOTS_PER_ROW = 9;
   public static final int MAX_ROWS = 6;
   public static final int MAX_SLOTS = 54;

   public static int toRows(int slots) {
      return 1 + (slots - 1) / 9;
   }

   public static int toX(int slot) {
      return slot % 9;
   }

   public static int toY(int slot) {
      return slot / 9;
   }

   public static int toSlot(int x, int y) {
      return y * 9 + x;
   }

   public static int getRequiredSlots(int requestedSlots) {
      Validate.isTrue(requestedSlots >= 0, "requestedSlots must not be negative");
      int requiredRows = requestedSlots / 9;
      if (requiredRows == 0 || requestedSlots % 9 != 0) {
         ++requiredRows;
      }

      return Math.min(requiredRows * 9, 54);
   }

   public static boolean isChestLike(InventoryType inventoryType) {
      switch(inventoryType) {
      case CHEST:
      case ENDER_CHEST:
      case SHULKER_BOX:
      case BARREL:
         return true;
      default:
         return false;
      }
   }

   public static void validateSlotRange(int startSlot, int endSlot) {
      Validate.isTrue(startSlot >= 0 && startSlot <= endSlot, () -> {
         return "Invalid start or end slot: " + startSlot + ", " + endSlot;
      });
   }

   public static int getCenterSlot(int startSlot, int endSlot) {
      validateSlotRange(startSlot, endSlot);
      return MathUtils.middle(startSlot, endSlot);
   }

   private ChestLayout() {
   }
}
