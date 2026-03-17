package com.bergerkiller.bukkit.tc.editor;

import com.bergerkiller.bukkit.common.inventory.CommonItemMaterials;
import com.bergerkiller.bukkit.common.inventory.CommonItemStack;
import com.bergerkiller.bukkit.common.map.MapDisplay;
import com.bergerkiller.bukkit.common.nbt.CommonTagCompound;
import java.util.UUID;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class TCMapControl {
   public static void updateMapItem(Player player, boolean opened) {
      PlayerInventory inv = player.getInventory();
      ItemStack heldItem = inv.getItem(inv.getHeldItemSlot());
      if (isTCMapItem(heldItem)) {
         updateMapItem(player, heldItem, opened);
      }
   }

   public static void updateMapItem(Player player, ItemStack item, boolean opened) {
      updateMapItem(player, CommonItemStack.of(item), opened);
   }

   public static void updateMapItem(Player player, CommonItemStack item, boolean opened) {
      if (isTCMapItem(item) && player.isValid()) {
         PlayerInventory inv = player.getInventory();
         UUID uuid = item.getCustomData().getUUID("editor");

         for(int i = 0; i < inv.getSize(); ++i) {
            CommonItemStack playerItem = CommonItemStack.of(inv.getItem(i));
            if (isTCMapItem(playerItem) && playerItem.getCustomData().getUUID("editor").equals(uuid)) {
               CommonItemStack newItem = playerItem.clone();
               if (opened) {
                  newItem.setType(CommonItemMaterials.FILLED_MAP);
               } else {
                  newItem.setType(CommonItemMaterials.EMPTY_MAP);
               }

               inv.setItem(i, newItem.toBukkit());
               return;
            }
         }

      }
   }

   public static boolean isTCMapItem(ItemStack item) {
      return isTCMapItem(CommonItemStack.of(item));
   }

   public static boolean isTCMapItem(CommonItemStack item) {
      if (item != null && (item.isType(CommonItemMaterials.FILLED_MAP) || !item.isType(CommonItemMaterials.EMPTY_MAP))) {
         CommonTagCompound tag = item.getCustomData();
         if (tag == null) {
            return false;
         } else {
            return tag.getUUID("editor") != null && ((String)tag.getValue("plugin", "")).equals("TrainCarts");
         }
      } else {
         return false;
      }
   }

   public static ItemStack createTCMapItem() {
      CommonItemStack item = CommonItemStack.of(MapDisplay.createMapItem(TCMapEditor.class));
      item.setType(CommonItemMaterials.EMPTY_MAP);
      item.setCustomNameMessage("TrainCarts Editor");
      item.updateCustomData((tag) -> {
         tag.putValue("plugin", "TrainCarts");
         tag.putUUID("editor", UUID.randomUUID());
      });
      item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
      return item.toBukkit();
   }
}
