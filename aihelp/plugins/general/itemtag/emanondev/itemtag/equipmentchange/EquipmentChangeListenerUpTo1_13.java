package emanondev.itemtag.equipmentchange;

import emanondev.itemedit.utility.InventoryUtils;
import emanondev.itemedit.utility.ItemUtils;
import emanondev.itemtag.ItemTag;
import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EquipmentChangeListenerUpTo1_13 extends EquipmentChangeListenerBase {
   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   private void event(InventoryClickEvent event) {
      if (event.getWhoClicked() instanceof Player) {
         if (!event.getWhoClicked().hasMetadata("NPC")) {
            Player p = (Player)event.getWhoClicked();
            EquipmentSlot clickedSlot = this.getEquipmentSlotAtPosition(event.getRawSlot(), p, event.getView());
            switch(event.getAction()) {
            case CLONE_STACK:
            case NOTHING:
            case HOTBAR_MOVE_AND_READD:
               return;
            case DROP_ONE_CURSOR:
            case DROP_ALL_CURSOR:
               this.clickDrop.add(p);
               return;
            case DROP_ONE_SLOT:
               if (!ItemUtils.isAirOrNull(event.getCursor())) {
                  return;
               }

               if (clickedSlot != null && event.getCurrentItem().getAmount() == 1) {
                  this.onEquipChange(p, EquipmentChangeEvent.EquipMethod.INVENTORY_DROP, clickedSlot, event.getCurrentItem(), (ItemStack)null);
               }

               this.clickDrop.add(p);
               return;
            case DROP_ALL_SLOT:
               if (!ItemUtils.isAirOrNull(event.getCursor())) {
                  return;
               }

               if (clickedSlot != null) {
                  this.onEquipChange(p, EquipmentChangeEvent.EquipMethod.INVENTORY_DROP, clickedSlot, event.getCurrentItem(), (ItemStack)null);
               }

               this.clickDrop.add(p);
               return;
            case PICKUP_ALL:
               if (clickedSlot == null) {
                  return;
               }

               this.onEquipChange(p, EquipmentChangeEvent.EquipMethod.INVENTORY_PICKUP, clickedSlot, event.getCurrentItem(), (ItemStack)null);
               return;
            case PICKUP_HALF:
               if (clickedSlot == null) {
                  return;
               } else {
                  if (event.getCurrentItem().getAmount() > 1) {
                     return;
                  }

                  this.onEquipChange(p, EquipmentChangeEvent.EquipMethod.INVENTORY_PICKUP, clickedSlot, event.getCurrentItem(), (ItemStack)null);
                  return;
               }
            case PICKUP_ONE:
               if (clickedSlot == null) {
                  return;
               } else {
                  if (event.getCurrentItem().getAmount() != 1) {
                     return;
                  }

                  this.onEquipChange(p, EquipmentChangeEvent.EquipMethod.INVENTORY_PICKUP, clickedSlot, event.getCurrentItem(), (ItemStack)null);
                  return;
               }
            case PLACE_ALL:
            case PLACE_ONE:
            case PLACE_SOME:
               if (clickedSlot == null) {
                  return;
               }

               if (ItemUtils.isAirOrNull(event.getCurrentItem())) {
                  this.onEquipChange(p, EquipmentChangeEvent.EquipMethod.INVENTORY_PLACE, clickedSlot, (ItemStack)null, event.getCursor());
               }

               return;
            case SWAP_WITH_CURSOR:
               if (clickedSlot == null) {
                  return;
               } else {
                  if (this.isSimilarIgnoreDamage(event.getCurrentItem(), event.getCursor())) {
                     return;
                  }

                  this.onEquipChange(p, EquipmentChangeEvent.EquipMethod.INVENTORY_SWAP_WITH_CURSOR, clickedSlot, event.getCurrentItem(), event.getCursor());
                  return;
               }
            case HOTBAR_SWAP:
               ItemStack to = p.getInventory().getItem(event.getHotbarButton() == -1 ? 45 : event.getHotbarButton());
               if (this.isSimilarIgnoreDamage(event.getCurrentItem(), to)) {
                  return;
               }

               if (clickedSlot != null) {
                  this.onEquipChange(p, EquipmentChangeEvent.EquipMethod.INVENTORY_HOTBAR_SWAP, clickedSlot, event.getCurrentItem(), to);
               }

               if (event.getHotbarButton() == -1) {
                  this.onEquipChange(p, EquipmentChangeEvent.EquipMethod.INVENTORY_HOTBAR_SWAP, EquipmentSlot.OFF_HAND, to, event.getCurrentItem());
               } else if (event.getHotbarButton() == p.getInventory().getHeldItemSlot()) {
                  this.onEquipChange(p, EquipmentChangeEvent.EquipMethod.INVENTORY_HOTBAR_SWAP, EquipmentSlot.HAND, to, event.getCurrentItem());
               }

               return;
            case MOVE_TO_OTHER_INVENTORY:
               EquipmentSlot slot = InventoryUtils.getTopInventory(event).getType() == InventoryType.CRAFTING ? this.guessDispenserSlotType(event.getCurrentItem()) : null;
               if (slot != null && ItemUtils.isAirOrNull(this.getEquip(p, slot))) {
                  this.onEquipChange(p, EquipmentChangeEvent.EquipMethod.INVENTORY_MOVE_TO_OTHER_INVENTORY, slot, (ItemStack)null, event.getCurrentItem());
               }

               if (clickedSlot != null && clickedSlot != EquipmentSlot.HAND) {
                  (new EquipmentChangeListenerBase.SlotCheck(p, EquipmentChangeEvent.EquipMethod.INVENTORY_MOVE_TO_OTHER_INVENTORY, new EquipmentSlot[]{clickedSlot})).runTaskLater(ItemTag.get(), 1L);
               } else {
                  (new EquipmentChangeListenerBase.SlotCheck(p, EquipmentChangeEvent.EquipMethod.INVENTORY_MOVE_TO_OTHER_INVENTORY, new EquipmentSlot[]{EquipmentSlot.HAND})).runTaskLater(ItemTag.get(), 1L);
               }

               return;
            case COLLECT_TO_CURSOR:
               ArrayList<EquipmentSlot> slots = new ArrayList();
               if (InventoryUtils.getTopInventory(event).getType() == InventoryType.CRAFTING) {
                  EquipmentSlot[] var5 = EquipmentSlot.values();
                  int var6 = var5.length;

                  for(int var7 = 0; var7 < var6; ++var7) {
                     EquipmentSlot slot = var5[var7];
                     if (event.getCursor().isSimilar(this.getEquip(p, slot))) {
                        slots.add(slot);
                     }
                  }
               } else if (event.getCursor().isSimilar(this.getEquip(p, EquipmentSlot.HAND))) {
                  slots.add(EquipmentSlot.HAND);
               }

               if (!slots.isEmpty()) {
                  (new EquipmentChangeListenerBase.SlotCheck(p, EquipmentChangeEvent.EquipMethod.INVENTORY_COLLECT_TO_CURSOR, slots)).runTaskLater(ItemTag.get(), 1L);
               }

               return;
            case PICKUP_SOME:
            case UNKNOWN:
               return;
            default:
            }
         }
      }
   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.MONITOR
   )
   private void event(PlayerSwapHandItemsEvent event) {
      if (!event.getPlayer().hasMetadata("NPC")) {
         if (!this.isSimilarIgnoreDamage(event.getMainHandItem(), event.getOffHandItem())) {
            this.onEquipChange(event.getPlayer(), EquipmentChangeEvent.EquipMethod.SWAP_HANDS_ITEM, EquipmentSlot.HAND, event.getOffHandItem(), event.getMainHandItem());
            this.onEquipChange(event.getPlayer(), EquipmentChangeEvent.EquipMethod.SWAP_HANDS_ITEM, EquipmentSlot.OFF_HAND, event.getMainHandItem(), event.getOffHandItem());
         }
      }
   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.MONITOR
   )
   private void event(PlayerPickupItemEvent event) {
      if (!event.getPlayer().hasMetadata("NPC")) {
         if (ItemUtils.isAirOrNull(this.getEquip(event.getPlayer(), EquipmentSlot.HAND))) {
            for(int i = 0; i < event.getPlayer().getInventory().getHeldItemSlot(); ++i) {
               if (ItemUtils.isAirOrNull(event.getPlayer().getInventory().getItem(i))) {
                  return;
               }
            }

            (new EquipmentChangeListenerBase.SlotCheck(event.getPlayer(), EquipmentChangeEvent.EquipMethod.PICKUP, new EquipmentSlot[]{EquipmentSlot.HAND})).runTaskLater(ItemTag.get(), 1L);
         }
      }
   }

   public boolean isSimilarIgnoreDamage(ItemStack item, ItemStack item2) {
      if (ItemUtils.isAirOrNull(item)) {
         return ItemUtils.isAirOrNull(item2);
      } else if (ItemUtils.isAirOrNull(item2)) {
         return false;
      } else if (item.isSimilar(item2)) {
         return true;
      } else if (item.getType() != item2.getType()) {
         return false;
      } else {
         ItemMeta meta1 = ItemUtils.getMeta(item);
         ItemMeta meta2 = ItemUtils.getMeta(item2);
         if (!this.isUbreakable(meta1) && !this.isUbreakable(meta2)) {
            ItemStack itemCopy = item.clone();
            ItemStack itemCopy2 = item2.clone();
            itemCopy.setDurability(itemCopy2.getDurability());
            return itemCopy.isSimilar(itemCopy2);
         } else {
            return false;
         }
      }
   }

   private boolean isUbreakable(ItemMeta meta) {
      return meta.serialize().containsKey("Unbreakable");
   }
}
