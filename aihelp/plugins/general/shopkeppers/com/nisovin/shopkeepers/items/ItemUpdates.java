package com.nisovin.shopkeepers.items;

import com.nisovin.shopkeepers.api.events.UpdateItemEvent;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.util.inventory.ItemData;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import org.bukkit.Bukkit;
import org.checkerframework.checker.nullness.qual.PolyNull;

public class ItemUpdates {
   public static UpdateItemEvent callUpdateItemEvent(UnmodifiableItemStack item) {
      UpdateItemEvent updateItemEvent = new UpdateItemEvent(item);
      Bukkit.getPluginManager().callEvent(updateItemEvent);
      return updateItemEvent;
   }

   @PolyNull
   public static UnmodifiableItemStack updateItem(@PolyNull UnmodifiableItemStack item) {
      if (ItemUtils.isEmpty(item)) {
         return item;
      } else {
         assert item != null;

         UpdateItemEvent updateItemEvent = callUpdateItemEvent(item);
         return !updateItemEvent.isItemAltered() ? item : ItemUtils.nonNullUnmodifiableClone(ItemUtils.asItemStack(updateItemEvent.getItem()));
      }
   }

   @PolyNull
   public static ItemData updateItemData(@PolyNull ItemData itemData) {
      UnmodifiableItemStack item = itemData == null ? null : itemData.asUnmodifiableItemStack();
      if (ItemUtils.isEmpty(item)) {
         return itemData;
      } else {
         assert item != null;

         UpdateItemEvent updateItemEvent = callUpdateItemEvent(item);
         return !updateItemEvent.isItemAltered() ? itemData : new ItemData(ItemUtils.asItemStack(updateItemEvent.getItem()));
      }
   }

   private ItemUpdates() {
   }
}
