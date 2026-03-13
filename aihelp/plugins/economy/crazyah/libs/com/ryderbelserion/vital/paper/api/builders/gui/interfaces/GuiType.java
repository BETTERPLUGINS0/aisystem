package libs.com.ryderbelserion.vital.paper.api.builders.gui.interfaces;

import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

public enum GuiType {
   CHEST(InventoryType.CHEST, 9),
   WORKBENCH(InventoryType.WORKBENCH, 9),
   HOPPER(InventoryType.HOPPER, 5),
   DISPENSER(InventoryType.DISPENSER, 8),
   BREWING(InventoryType.BREWING, 4);

   @NotNull
   private final InventoryType inventoryType;
   private final int limit;

   private GuiType(@NotNull final InventoryType param3, final int param4) {
      this.inventoryType = inventoryType;
      this.limit = limit;
   }

   @NotNull
   public final InventoryType getInventoryType() {
      return this.inventoryType;
   }

   public final int getLimit() {
      return this.limit;
   }

   // $FF: synthetic method
   private static GuiType[] $values() {
      return new GuiType[]{CHEST, WORKBENCH, HOPPER, DISPENSER, BREWING};
   }
}
