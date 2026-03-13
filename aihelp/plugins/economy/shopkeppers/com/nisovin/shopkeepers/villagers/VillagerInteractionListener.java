package com.nisovin.shopkeepers.villagers;

import com.nisovin.shopkeepers.api.ShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.dependencies.citizens.CitizensUtils;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopcreation.ShopCreationItem;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.ui.lib.UISessionManager;
import com.nisovin.shopkeepers.ui.villager.editor.VillagerEditorViewProvider;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.inventory.InventoryUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.Map;
import org.bukkit.World;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class VillagerInteractionListener implements Listener {
   private final ShopkeepersPlugin plugin;

   public VillagerInteractionListener(ShopkeepersPlugin plugin) {
      Validate.notNull(plugin, (String)"plugin is null");
      this.plugin = plugin;
   }

   @EventHandler(
      priority = EventPriority.HIGH,
      ignoreCancelled = true
   )
   void onEntityInteract(PlayerInteractEntityEvent event) {
      if (event.getRightClicked() instanceof AbstractVillager) {
         AbstractVillager villager = (AbstractVillager)event.getRightClicked();
         boolean isVillager = villager instanceof Villager;
         boolean isWanderingTrader = !isVillager && villager instanceof WanderingTrader;
         if (isVillager || isWanderingTrader) {
            if (!this.plugin.getShopkeeperRegistry().isShopkeeper((Entity)villager)) {
               Log.debug("Interaction with non-shopkeeper villager ..");
               if (CitizensUtils.isNPC(villager)) {
                  Log.debug("  ignoring (probably Citizens) NPC");
               } else {
                  if (isVillager && this.isOtherVillagersDisabled(villager.getWorld()) || isWanderingTrader && this.isWanderingTradersDisabled(villager.getWorld())) {
                     event.setCancelled(true);
                     Log.debug("  trading prevented");
                  }

                  Player player = event.getPlayer();
                  if (event.getHand() != EquipmentSlot.HAND) {
                     if (InventoryUtils.hasInventoryOpen(player)) {
                        event.setCancelled(true);
                        Log.debug("  off-hand interaction prevented due to open inventory");
                     }

                  } else {
                     boolean overrideTrading = false;
                     if (this.handleEditRegularVillager(player, villager)) {
                        overrideTrading = true;
                     } else if (this.handleHireOtherVillager(player, villager)) {
                        overrideTrading = true;
                     }

                     if (overrideTrading) {
                        event.setCancelled(true);
                     }

                  }
               }
            }
         }
      }
   }

   private boolean isOtherVillagersDisabled(World world) {
      return Settings.disableOtherVillagers && (Settings.disableOtherVillagersWorlds.isEmpty() || Settings.disableOtherVillagersWorlds.contains(world.getName()));
   }

   private boolean isWanderingTradersDisabled(World world) {
      return Settings.disableWanderingTraders && (Settings.disableWanderingTradersWorlds.isEmpty() || Settings.disableWanderingTradersWorlds.contains(world.getName()));
   }

   private boolean handleEditRegularVillager(Player player, AbstractVillager villager) {
      if (!player.isSneaking()) {
         return false;
      } else {
         if (Settings.editRegularVillagers && villager instanceof Villager || Settings.editRegularWanderingTraders && villager instanceof WanderingTrader) {
            Log.debug("  possible villager editor request ..");
            VillagerEditorViewProvider villagerEditorViewProvider = new VillagerEditorViewProvider(villager);
            boolean uiOpened = UISessionManager.getInstance().requestUI(villagerEditorViewProvider, player, true);
            if (uiOpened) {
               Log.debug("    ..success (normal trading prevented).");
               return true;
            }

            Log.debug("    ..no access (probably missing permission).");
         }

         return false;
      }
   }

   private boolean isHireOtherVillagersEnabled(World world) {
      return Settings.hireOtherVillagers && (Settings.hireOtherVillagersWorlds.isEmpty() || Settings.hireOtherVillagersWorlds.contains(world.getName()));
   }

   private boolean isHireWanderingTradersEnabled(World world) {
      return Settings.hireWanderingTraders && (Settings.hireWanderingTradersWorlds.isEmpty() || Settings.hireWanderingTradersWorlds.contains(world.getName()));
   }

   private boolean isHireVillagerEnabled(AbstractVillager villager) {
      if (villager instanceof Villager) {
         return this.isHireOtherVillagersEnabled(villager.getWorld());
      } else {
         return villager instanceof WanderingTrader ? this.isHireWanderingTradersEnabled(villager.getWorld()) : false;
      }
   }

   private boolean handleHireOtherVillager(Player player, AbstractVillager villager) {
      if (!this.isHireVillagerEnabled(villager)) {
         return false;
      } else {
         Log.debug("  possible hire ..");
         Log.debug("    checking villager access.");
         if (!this.checkEntityAccess(player, villager)) {
            Log.debug("    ..no permission to remove villager.");
            return false;
         } else {
            PlayerInventory playerInventory = player.getInventory();
            ItemStack itemInMainHand = playerInventory.getItemInMainHand();
            if (!Settings.hireItem.matches(itemInMainHand)) {
               TextUtils.sendMessage(player, (Text)Messages.villagerForHire, (Object[])("costs", Settings.hireOtherVillagersCosts, "hire-item", Settings.hireItem.getType().name()));
               Log.debug("    ..not holding hire item.");
               return false;
            } else {
               int costs = Settings.hireOtherVillagersCosts;
               if (costs > 0) {
                  ItemStack[] storageContents = (ItemStack[])Unsafe.cast(playerInventory.getStorageContents());
                  if (!InventoryUtils.containsAtLeast(storageContents, Settings.hireItem, costs)) {
                     TextUtils.sendMessage(player, (Text)Messages.cannotHire);
                     Log.debug("    ..not holding enough hire items.");
                     return false;
                  }

                  Log.debug("  Villager hiring: The player has the needed amount of hiring items.");
                  int inHandAmount = itemInMainHand.getAmount();
                  int remaining = inHandAmount - costs;
                  Log.debug(() -> {
                     return "  Villager hiring: in hand=" + inHandAmount + " costs=" + costs + " remaining=" + remaining;
                  });
                  if (remaining > 0) {
                     itemInMainHand.setAmount(remaining);
                  } else {
                     playerInventory.setItemInMainHand((ItemStack)null);
                     if (remaining < 0) {
                        InventoryUtils.removeItems(storageContents, Settings.hireItem, -remaining);
                        InventoryUtils.setStorageContents(playerInventory, storageContents);
                     }
                  }
               }

               ItemStack shopCreationItem = ShopCreationItem.create();
               Map<Integer, ItemStack> remaining = playerInventory.addItem(new ItemStack[]{shopCreationItem});
               if (!remaining.isEmpty()) {
                  villager.getWorld().dropItem(villager.getLocation(), shopCreationItem);
               }

               villager.remove();
               player.updateInventory();
               TextUtils.sendMessage(player, (Text)Messages.hired);
               Log.debug("    ..success (normal trading prevented).");
               return true;
            }
         }
      }
   }

   private boolean checkEntityAccess(Player player, Entity entity) {
      TestEntityDamageByEntityEvent fakeDamageEvent = new TestEntityDamageByEntityEvent(player, entity);
      this.plugin.getServer().getPluginManager().callEvent(fakeDamageEvent);
      return !fakeDamageEvent.isCancelled();
   }
}
