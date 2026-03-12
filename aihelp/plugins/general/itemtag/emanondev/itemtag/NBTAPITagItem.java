package emanondev.itemtag;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NBTAPITagItem implements TagItem {
   private final ItemStack item;
   private NBTItem nbtItem = null;

   public NBTAPITagItem(@Nullable ItemStack item) {
      this.item = item;
   }

   public NBTItem getNbtItem() {
      if (this.nbtItem == null) {
         this.nbtItem = new NBTItem(this.item, true);
      }

      return this.nbtItem;
   }

   public boolean hasTag(@NotNull String key) {
      return this.item != null && this.item.getType() != Material.AIR && this.item.hasItemMeta() ? this.getNbtItem().hasKey(key) : false;
   }

   public boolean hasBooleanTag(@NotNull String key) {
      return this.hasTag(key);
   }

   public boolean hasIntegerTag(@NotNull String key) {
      return this.hasTag(key);
   }

   public boolean hasDoubleTag(@NotNull String key) {
      return this.hasTag(key);
   }

   public boolean hasStringTag(@NotNull String key) {
      return this.hasTag(key);
   }

   public boolean hasStringListTag(@NotNull String key) {
      return this.hasTag(key);
   }

   public void removeTag(@NotNull String key) {
      this.getNbtItem().removeKey(key);
   }

   public void setTag(@NotNull String key, boolean value) {
      this.getNbtItem().setBoolean(key, value);
   }

   public void setTag(@NotNull String key, @Nullable String value) {
      this.getNbtItem().setString(key, value);
   }

   public void setTag(@NotNull String key, int value) {
      this.getNbtItem().setInteger(key, value);
   }

   public void setTag(@NotNull String key, double value) {
      this.getNbtItem().setDouble(key, value);
   }

   @Nullable
   public Boolean getBoolean(@NotNull String key) {
      return this.getNbtItem().getBoolean(key);
   }

   @Nullable
   public String getString(@NotNull String key) {
      return this.getNbtItem().getString(key);
   }

   @Nullable
   public Integer getInteger(@NotNull String key) {
      return this.getNbtItem().getInteger(key);
   }

   @Nullable
   public Double getDouble(@NotNull String key) {
      return this.getNbtItem().getDouble(key);
   }

   public boolean isValid() {
      return this.item != null && this.item.getType() != Material.AIR;
   }

   public ItemStack getItem() {
      return this.item;
   }
}
