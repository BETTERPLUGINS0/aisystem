package emanondev.itemtag;

import java.util.WeakHashMap;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

/** @deprecated */
@Deprecated
public class SpigotTagManager implements TagManager {
   private final WeakHashMap<String, NamespacedKey> keys = new WeakHashMap();

   public boolean hasBooleanTag(String key, ItemStack item) {
      return this.hasTag(key, item, PersistentDataType.INTEGER);
   }

   public boolean hasIntegerTag(String key, ItemStack item) {
      return this.hasTag(key, item, PersistentDataType.INTEGER);
   }

   public boolean hasDoubleTag(String key, ItemStack item) {
      return this.hasTag(key, item, PersistentDataType.DOUBLE);
   }

   public boolean hasStringTag(String key, ItemStack item) {
      return this.hasTag(key, item, PersistentDataType.STRING);
   }

   public ItemStack setTag(String key, ItemStack item, boolean value) {
      return this.setTag(key, item, PersistentDataType.INTEGER, value ? 1 : 0);
   }

   public ItemStack setTag(String key, ItemStack item, String value) {
      return this.setTag(key, item, PersistentDataType.STRING, value);
   }

   public ItemStack setTag(String key, ItemStack item, int value) {
      return this.setTag(key, item, PersistentDataType.INTEGER, value);
   }

   public ItemStack setTag(String key, ItemStack item, double value) {
      return this.setTag(key, item, PersistentDataType.DOUBLE, value);
   }

   public Boolean getBoolean(String key, ItemStack item) {
      Integer value = (Integer)this.get(key, item, PersistentDataType.INTEGER);
      return value == null ? null : value != 0;
   }

   public String getString(String key, ItemStack item) {
      return (String)this.get(key, item, PersistentDataType.STRING);
   }

   public Integer getInteger(String key, ItemStack item) {
      return (Integer)this.get(key, item, PersistentDataType.INTEGER);
   }

   public Double getDouble(String key, ItemStack item) {
      return (Double)this.get(key, item, PersistentDataType.DOUBLE);
   }

   public ItemStack removeTag(String key, ItemStack item) {
      ItemMeta meta = item.getItemMeta();
      this.putKeyIfAbsent(key);
      meta.getPersistentDataContainer().remove((NamespacedKey)this.keys.get(key));
      item.setItemMeta(meta);
      return item;
   }

   public <T, Z> boolean hasTag(String key, ItemStack item, PersistentDataType<T, Z> type) {
      if (item != null && item.getType() != Material.AIR && item.hasItemMeta()) {
         this.putKeyIfAbsent(key);
         return item.getItemMeta().getPersistentDataContainer().get((NamespacedKey)this.keys.get(key), type) != null;
      } else {
         return false;
      }
   }

   public <Z, T> ItemStack setTag(String key, ItemStack item, PersistentDataType<Z, T> type, T value) {
      ItemMeta meta = item.getItemMeta();
      this.putKeyIfAbsent(key);
      meta.getPersistentDataContainer().set((NamespacedKey)this.keys.get(key), type, value);
      item.setItemMeta(meta);
      return item;
   }

   public <Z, T> T get(String key, ItemStack item, PersistentDataType<Z, T> type) {
      if (item != null && item.getType() != Material.AIR && item.hasItemMeta()) {
         this.putKeyIfAbsent(key);
         return item.getItemMeta().getPersistentDataContainer().get((NamespacedKey)this.keys.get(key), type);
      } else {
         return null;
      }
   }

   private void putKeyIfAbsent(String key) {
      if (!this.keys.containsKey(key)) {
         String[] args = key.split(":");
         this.keys.put(key, new NamespacedKey(args[0], args[1]));
      }

   }
}
