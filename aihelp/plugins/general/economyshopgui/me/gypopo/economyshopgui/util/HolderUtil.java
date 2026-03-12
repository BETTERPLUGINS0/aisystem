package me.gypopo.economyshopgui.util;

import java.lang.reflect.Method;
import java.util.function.Function;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class HolderUtil {
   private static final Function<Inventory, InventoryHolder> getter = getFunction();

   public static InventoryHolder getHolder(Inventory inv) {
      return (InventoryHolder)getter.apply(inv);
   }

   private static Function<Inventory, InventoryHolder> getFunction() {
      try {
         Method method = Inventory.class.getMethod("getHolder", Boolean.TYPE);
         return (inv) -> {
            try {
               return (InventoryHolder)method.invoke(inv, false);
            } catch (Exception var3) {
               throw new RuntimeException(var3);
            }
         };
      } catch (NoSuchMethodException var1) {
         return Inventory::getHolder;
      }
   }
}
