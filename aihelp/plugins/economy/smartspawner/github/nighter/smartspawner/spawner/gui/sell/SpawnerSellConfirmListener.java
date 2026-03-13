package github.nighter.smartspawner.spawner.gui.sell;

import github.nighter.smartspawner.Scheduler;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.spawner.gui.layout.GuiButton;
import github.nighter.smartspawner.spawner.gui.layout.GuiLayout;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import java.util.Optional;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class SpawnerSellConfirmListener implements Listener {
   private final SmartSpawner plugin;

   public SpawnerSellConfirmListener(SmartSpawner plugin) {
      this.plugin = plugin;
   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onInventoryClick(InventoryClickEvent event) {
      InventoryHolder holder = event.getInventory().getHolder();
      if (holder instanceof SpawnerSellConfirmHolder) {
         event.setCancelled(true);
         HumanEntity var4 = event.getWhoClicked();
         if (var4 instanceof Player) {
            Player player = (Player)var4;
            SpawnerSellConfirmHolder confirmHolder = (SpawnerSellConfirmHolder)holder;
            SpawnerData spawner = confirmHolder.getSpawnerData();
            if (spawner == null) {
               player.closeInventory();
            } else {
               int slot = event.getRawSlot();
               GuiLayout layout = this.plugin.getGuiLayoutConfig().getCurrentSellConfirmLayout();
               if (layout == null) {
                  player.closeInventory();
               } else {
                  Optional<GuiButton> buttonOpt = layout.getButtonAtSlot(slot);
                  if (buttonOpt.isPresent()) {
                     GuiButton button = (GuiButton)buttonOpt.get();
                     String action = button.getDefaultAction();
                     if (action != null) {
                        byte var12 = -1;
                        switch(action.hashCode()) {
                        case -1367724422:
                           if (action.equals("cancel")) {
                              var12 = 0;
                           }
                           break;
                        case 951117504:
                           if (action.equals("confirm")) {
                              var12 = 1;
                           }
                        }

                        switch(var12) {
                        case 0:
                           this.handleCancel(player, spawner, confirmHolder.getPreviousGui());
                           break;
                        case 1:
                           this.handleConfirm(player, spawner, confirmHolder.getPreviousGui(), confirmHolder.isCollectExp());
                        }

                     }
                  }
               }
            }
         }
      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onInventoryClose(InventoryCloseEvent event) {
      InventoryHolder holder = event.getInventory().getHolder();
      if (holder instanceof SpawnerSellConfirmHolder) {
         ;
      }
   }

   private void handleCancel(Player player, SpawnerData spawner, SpawnerSellConfirmUI.PreviousGui previousGui) {
      player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
      spawner.clearInteracted();
      this.reopenPreviousGui(player, spawner, previousGui);
   }

   private void handleConfirm(Player player, SpawnerData spawner, SpawnerSellConfirmUI.PreviousGui previousGui, boolean collectExp) {
      if (collectExp) {
         this.plugin.getSpawnerMenuAction().handleExpBottleClick(player, spawner, true);
      }

      spawner.clearInteracted();
      this.plugin.getSpawnerSellManager().sellAllItems(player, spawner);
      Scheduler.runTask(() -> {
         this.reopenPreviousGui(player, spawner, previousGui);
      });
   }

   private void reopenPreviousGui(Player player, SpawnerData spawner, SpawnerSellConfirmUI.PreviousGui previousGui) {
      boolean isBedrockPlayer = this.isBedrockPlayer(player);
      switch(previousGui) {
      case MAIN_MENU:
         if (isBedrockPlayer && this.plugin.getSpawnerMenuFormUI() != null) {
            this.plugin.getSpawnerMenuFormUI().openSpawnerForm(player, spawner);
         } else {
            this.plugin.getSpawnerMenuUI().openSpawnerMenu(player, spawner, true);
         }
         break;
      case STORAGE:
         Inventory storageInventory = this.plugin.getSpawnerStorageUI().createStorageInventory(spawner, 1, -1);
         player.openInventory(storageInventory);
      }

   }

   private boolean isBedrockPlayer(Player player) {
      return this.plugin.getIntegrationManager() != null && this.plugin.getIntegrationManager().getFloodgateHook() != null ? this.plugin.getIntegrationManager().getFloodgateHook().isBedrockPlayer(player) : false;
   }
}
