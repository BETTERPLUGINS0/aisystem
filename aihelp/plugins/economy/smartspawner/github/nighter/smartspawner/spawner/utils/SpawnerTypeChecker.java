package github.nighter.smartspawner.spawner.utils;

import github.nighter.smartspawner.SmartSpawner;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class SpawnerTypeChecker {
   private static NamespacedKey VANILLA_SPAWNER_KEY;
   private static NamespacedKey ITEM_SPAWNER_KEY;

   public static void init(SmartSpawner plugin) {
      VANILLA_SPAWNER_KEY = new NamespacedKey(plugin, "vanilla_spawner");
      ITEM_SPAWNER_KEY = new NamespacedKey(plugin, "item_spawner_material");
   }

   public static boolean isVanillaSpawner(ItemStack item) {
      if (item != null && item.hasItemMeta()) {
         ItemMeta meta = item.getItemMeta();
         return meta != null && meta.getPersistentDataContainer().has(VANILLA_SPAWNER_KEY, PersistentDataType.BOOLEAN);
      } else {
         return false;
      }
   }

   public static boolean isItemSpawner(ItemStack item) {
      if (item != null && item.getType() == Material.SPAWNER && item.hasItemMeta()) {
         ItemMeta meta = item.getItemMeta();
         return meta != null && meta.getPersistentDataContainer().has(ITEM_SPAWNER_KEY, PersistentDataType.STRING);
      } else {
         return false;
      }
   }

   public static Material getItemSpawnerMaterial(ItemStack item) {
      if (!isItemSpawner(item)) {
         return null;
      } else {
         ItemMeta meta = item.getItemMeta();
         if (meta == null) {
            return null;
         } else {
            String materialName = (String)meta.getPersistentDataContainer().get(ITEM_SPAWNER_KEY, PersistentDataType.STRING);
            if (materialName == null) {
               return null;
            } else {
               try {
                  return Material.valueOf(materialName);
               } catch (IllegalArgumentException var4) {
                  return null;
               }
            }
         }
      }
   }
}
