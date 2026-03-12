package emanondev.itemtag;

import org.bukkit.inventory.ItemStack;

/** @deprecated */
@Deprecated
interface TagManager {
   boolean hasBooleanTag(String var1, ItemStack var2);

   boolean hasIntegerTag(String var1, ItemStack var2);

   boolean hasDoubleTag(String var1, ItemStack var2);

   boolean hasStringTag(String var1, ItemStack var2);

   ItemStack removeTag(String var1, ItemStack var2);

   ItemStack setTag(String var1, ItemStack var2, boolean var3);

   ItemStack setTag(String var1, ItemStack var2, String var3);

   ItemStack setTag(String var1, ItemStack var2, int var3);

   ItemStack setTag(String var1, ItemStack var2, double var3);

   Boolean getBoolean(String var1, ItemStack var2);

   String getString(String var1, ItemStack var2);

   Integer getInteger(String var1, ItemStack var2);

   Double getDouble(String var1, ItemStack var2);
}
