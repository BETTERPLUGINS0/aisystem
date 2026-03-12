package emanondev.itemtag.equipmentchange;

import emanondev.itemedit.utility.InventoryUtils;
import emanondev.itemedit.utility.ItemUtils;
import emanondev.itemtag.ItemTag;
import emanondev.itemtag.ItemTagUtility;
import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class EquipmentChangeListener extends EquipmentChangeListenerBase {
   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   public void event(InventoryClickEvent event) {
      ItemTag.get().log(event.getAction().name());
      if (event.getWhoClicked() instanceof Player) {
         if (!event.getWhoClicked().hasMetadata("NPC") && !event.getWhoClicked().hasMetadata("BOT")) {
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
               ItemStack to = event.getHotbarButton() == -1 ? p.getInventory().getItemInOffHand() : p.getInventory().getItem(event.getHotbarButton());
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
                  Iterator var5 = ItemTagUtility.getPlayerEquipmentSlots().iterator();

                  while(var5.hasNext()) {
                     EquipmentSlot slot = (EquipmentSlot)var5.next();
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

   @EventHandler
   private void event(BlockDispenseArmorEvent event) {
      if (event.getTargetEntity() instanceof Player) {
         if (!event.getTargetEntity().hasMetadata("NPC")) {
            EquipmentSlot slot = this.guessDispenserSlotType(event.getItem());
            if (slot == null) {
               throw new IllegalStateException(event.getItem().toString());
            } else {
               this.onEquipChange((Player)event.getTargetEntity(), EquipmentChangeEvent.EquipMethod.DISPENSER, slot, (ItemStack)null, event.getItem());
            }
         }
      }
   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.MONITOR
   )
   private void event(EntityPickupItemEvent event) {
      if (event.getEntity() instanceof Player) {
         if (!event.getEntity().hasMetadata("NPC")) {
            Player p = (Player)event.getEntity();
            if (ItemUtils.isAirOrNull(this.getEquip(p, EquipmentSlot.HAND))) {
               for(int i = 0; i < p.getInventory().getHeldItemSlot(); ++i) {
                  if (ItemUtils.isAirOrNull(p.getInventory().getItem(i))) {
                     return;
                  }
               }

               (new EquipmentChangeListenerBase.SlotCheck(p, EquipmentChangeEvent.EquipMethod.PICKUP, new EquipmentSlot[]{EquipmentSlot.HAND})).runTaskLater(ItemTag.get(), 1L);
            }
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
         ItemMeta meta1 = item.getItemMeta();
         if (!(meta1 instanceof Damageable)) {
            return false;
         } else {
            ItemMeta meta2 = item2.getItemMeta();
            if (!meta1.isUnbreakable() && !meta2.isUnbreakable()) {
               ((Damageable)meta2).setDamage(((Damageable)meta1).getDamage());
               return meta1.equals(meta2);
            } else {
               return false;
            }
         }
      }
   }
}
