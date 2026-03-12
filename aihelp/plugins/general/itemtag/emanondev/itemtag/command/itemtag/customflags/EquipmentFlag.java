package emanondev.itemtag.command.itemtag.customflags;

import emanondev.itemedit.utility.InventoryUtils;
import emanondev.itemedit.utility.ItemUtils;
import emanondev.itemedit.utility.InventoryUtils.ExcessMode;
import emanondev.itemtag.ItemTag;
import emanondev.itemtag.command.itemtag.Flag;
import emanondev.itemtag.equipmentchange.EquipmentChangeEvent;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EquipmentFlag extends CustomFlag {
   private static final String EQUIPMENT;

   public EquipmentFlag(@NotNull Flag cmd) {
      super("equipment", EQUIPMENT, cmd);
   }

   public ItemStack getGuiItem() {
      return new ItemStack(Material.IRON_CHESTPLATE);
   }

   @EventHandler
   public void event(EquipmentChangeEvent event) {
      if (InventoryUtils.getPlayerEquipmentSlots().contains(event.getSlotType())) {
         switch(event.getSlotType()) {
         case FEET:
         case LEGS:
         case CHEST:
         case HEAD:
            if (ItemUtils.isAirOrNull(event.getTo())) {
               return;
            } else {
               if (this.getValue(ItemTag.getTagItem(event.getTo()))) {
                  return;
               }

               Bukkit.getScheduler().runTaskLater(this.getPlugin(), () -> {
                  if (event.getPlayer().isOnline()) {
                     ItemStack originalItem = null;

                     try {
                        originalItem = event.getPlayer().getInventory().getItem(event.getSlotType());
                     } catch (Throwable var7) {
                        EntityEquipment equip = event.getPlayer().getEquipment();
                        if (equip != null) {
                           String var5 = event.getSlotType().name();
                           byte var6 = -1;
                           switch(var5.hashCode()) {
                           case 2153902:
                              if (var5.equals("FEET")) {
                                 var6 = 4;
                              }
                              break;
                           case 2209903:
                              if (var5.equals("HAND")) {
                                 var6 = 0;
                              }
                              break;
                           case 2213344:
                              if (var5.equals("HEAD")) {
                                 var6 = 3;
                              }
                              break;
                           case 2332709:
                              if (var5.equals("LEGS")) {
                                 var6 = 1;
                              }
                              break;
                           case 37796191:
                              if (var5.equals("OFF_HAND")) {
                                 var6 = 5;
                              }
                              break;
                           case 64089825:
                              if (var5.equals("CHEST")) {
                                 var6 = 2;
                              }
                           }

                           switch(var6) {
                           case 0:
                              originalItem = equip.getItemInHand();
                              break;
                           case 1:
                              originalItem = equip.getLeggings();
                              break;
                           case 2:
                              originalItem = equip.getChestplate();
                              break;
                           case 3:
                              originalItem = equip.getHelmet();
                              break;
                           case 4:
                              originalItem = equip.getBoots();
                              break;
                           case 5:
                              originalItem = equip.getItemInOffHand();
                              break;
                           default:
                              throw new IllegalArgumentException();
                           }
                        }
                     }

                     if (!ItemUtils.isAirOrNull(originalItem)) {
                        ItemStack item = originalItem.clone();
                        if (!this.getValue(ItemTag.getTagItem(item))) {
                           event.getPlayer().getInventory().setItem(event.getSlotType(), (ItemStack)null);
                           event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
                           InventoryUtils.giveAmount(event.getPlayer(), item, item.getAmount(), ExcessMode.DROP_EXCESS);
                           ItemTag.get().getEquipChangeListener().onEquipChange(event.getPlayer(), EquipmentChangeEvent.EquipMethod.UNKNOWN, event.getSlotType(), item, (ItemStack)null);
                        }
                     }
                  }
               }, 1L);
               return;
            }
         default:
         }
      }
   }

   static {
      EQUIPMENT = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":equipment";
   }
}
