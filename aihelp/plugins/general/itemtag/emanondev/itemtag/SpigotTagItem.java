package emanondev.itemtag;

import emanondev.itemedit.utility.ItemUtils;
import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpigotTagItem implements TagItem {
   private static final HashMap<String, NamespacedKey> keys = new HashMap<String, NamespacedKey>() {
      public NamespacedKey get(Object key) {
         NamespacedKey keyN = (NamespacedKey)super.get(key);
         if (keyN != null) {
            return keyN;
         } else {
            String[] args = ((String)key).split(":");
            keyN = new NamespacedKey(args[0], args[1]);
            this.put((String)key, keyN);
            return keyN;
         }
      }
   };
   private final ItemStack item;
   private ItemMeta meta = null;
   private PersistentDataContainer data = null;

   public SpigotTagItem(@Nullable ItemStack item) {
      this.item = item;
   }

   private PersistentDataContainer getData() {
      if (this.data == null) {
         this.meta = ItemUtils.getMeta(this.item);
         this.data = this.meta.getPersistentDataContainer();
      }

      return this.data;
   }

   public <T, Z> boolean hasTag(@NotNull String key, @NotNull PersistentDataType<T, Z> type) {
      if (this.item != null && this.item.getType() != Material.AIR && this.item.hasItemMeta()) {
         return this.getData().get((NamespacedKey)keys.get(key), type) != null;
      } else {
         return false;
      }
   }

   public boolean hasBooleanTag(@NotNull String key) {
      return this.hasTag(key, PersistentDataType.INTEGER);
   }

   public boolean hasIntegerTag(@NotNull String key) {
      return this.hasTag(key, PersistentDataType.INTEGER);
   }

   public boolean hasDoubleTag(@NotNull String key) {
      return this.hasTag(key, PersistentDataType.DOUBLE);
   }

   public boolean hasStringTag(@NotNull String key) {
      return this.hasTag(key, PersistentDataType.STRING);
   }

   public boolean hasStringListTag(@NotNull String key) {
      return this.hasTag(key, PersistentDataType.STRING);
   }

   public void removeTag(@NotNull String key) {
      this.getData().remove((NamespacedKey)keys.get(key));
      this.item.setItemMeta(this.meta);
   }

   public void setTag(@NotNull String key, boolean value) {
      this.setTag(key, value ? 1 : 0);
   }

   public void setTag(@NotNull String key, @Nullable String value) {
      if (value == null) {
         this.removeTag(key);
      } else {
         this.getData().set((NamespacedKey)keys.get(key), PersistentDataType.STRING, value);
         this.item.setItemMeta(this.meta);
      }

   }

   public void setTag(@NotNull String key, int value) {
      this.getData().set((NamespacedKey)keys.get(key), PersistentDataType.INTEGER, value);
      this.item.setItemMeta(this.meta);
   }

   public void setTag(@NotNull String key, double value) {
      this.getData().set((NamespacedKey)keys.get(key), PersistentDataType.DOUBLE, value);
      this.item.setItemMeta(this.meta);
   }

   @Nullable
   public Boolean getBoolean(@NotNull String key) {
      Integer value = this.getInteger(key);
      return value == null ? null : value != 0;
   }

   @Nullable
   public String getString(@NotNull String key) {
      return (String)this.getData().get((NamespacedKey)keys.get(key), PersistentDataType.STRING);
   }

   @Nullable
   public Integer getInteger(@NotNull String key) {
      return (Integer)this.getData().get((NamespacedKey)keys.get(key), PersistentDataType.INTEGER);
   }

   @Nullable
   public Double getDouble(@NotNull String key) {
      return (Double)this.getData().get((NamespacedKey)keys.get(key), PersistentDataType.DOUBLE);
   }

   public boolean isValid() {
      return this.item != null && this.item.getType() != Material.AIR;
   }

   public ItemStack getItem() {
      return this.item;
   }
}
