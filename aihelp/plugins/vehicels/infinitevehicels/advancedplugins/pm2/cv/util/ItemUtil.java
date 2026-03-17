package advancedplugins.pm2.cv.util;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

public class ItemUtil {
   public static void setPDCInt(ItemStack itemStack, NamespacedKey namespacedKey, int data) {
      if (var0 != null && var0.getItemMeta() != null && var0.getItemMeta().getPersistentDataContainer() != null) {
         PersistentDataContainer var3 = var0.getItemMeta().getPersistentDataContainer();
         var3.set(var1, PersistentDataType.INTEGER, var2);
      }
   }

   @Nullable
   public static Integer getPDCInt(ItemStack itemStack, NamespacedKey key) {
      return var0 != null && var0.getItemMeta() != null && var0.getItemMeta().getPersistentDataContainer() != null ? (Integer)var0.getItemMeta().getPersistentDataContainer().get(var1, PersistentDataType.INTEGER) : null;
   }
}
