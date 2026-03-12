package emanondev.itemtag;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/** @deprecated */
@Deprecated
public class NBTAPITagManager implements TagManager {
   public boolean hasTag(String key, ItemStack item) {
      return item != null && item.getType() != Material.AIR ? (new NBTItem(item)).hasKey(key) : false;
   }

   public ItemStack removeTag(String key, ItemStack item) {
      NBTItem nbti = new NBTItem(item);
      if (nbti.hasKey(key)) {
         nbti.removeKey(key);
         return nbti.getItem();
      } else {
         return item;
      }
   }

   public ItemStack setTag(String key, ItemStack item, boolean value) {
      NBTItem nbti = new NBTItem(item);
      nbti.setInteger(key, value ? 1 : 0);
      return nbti.getItem();
   }

   public ItemStack setTag(String key, ItemStack item, String value) {
      NBTItem nbti = new NBTItem(item);
      nbti.setString(key, value);
      return nbti.getItem();
   }

   public ItemStack setTag(String key, ItemStack item, int value) {
      NBTItem nbti = new NBTItem(item);
      nbti.setInteger(key, value);
      return nbti.getItem();
   }

   public ItemStack setTag(String key, ItemStack item, double value) {
      NBTItem nbti = new NBTItem(item);
      nbti.setDouble(key, value);
      return nbti.getItem();
   }

   public Boolean getBoolean(String key, ItemStack item) {
      return (new NBTItem(item)).getInteger(key) != 0;
   }

   public String getString(String key, ItemStack item) {
      return (new NBTItem(item)).getString(key);
   }

   public Integer getInteger(String key, ItemStack item) {
      return (new NBTItem(item)).getInteger(key);
   }

   public Double getDouble(String key, ItemStack item) {
      return (new NBTItem(item)).getDouble(key);
   }

   public boolean hasBooleanTag(String key, ItemStack item) {
      return this.hasTag(key, item);
   }

   public boolean hasIntegerTag(String key, ItemStack item) {
      return this.hasTag(key, item);
   }

   public boolean hasDoubleTag(String key, ItemStack item) {
      return this.hasTag(key, item);
   }

   public boolean hasStringTag(String key, ItemStack item) {
      return this.hasTag(key, item);
   }
}
