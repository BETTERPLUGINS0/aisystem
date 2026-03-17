package me.PM2.infinitevehicles.xseries.inventory;

import org.bukkit.inventory.InventoryView;

public final class XInventoryView {
   private static final boolean USE_INTERFACE = InventoryView.class.isInterface();

   private XInventoryView() {
   }

   public static BukkitInventoryView of(InventoryView var0) {
      return (BukkitInventoryView)(USE_INTERFACE ? new NewInventoryView(var0) : new OldInventoryView(var0));
   }
}
