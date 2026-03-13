package github.nighter.smartspawner.spawner.sell;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.api.events.SpawnerSellEvent;
import github.nighter.smartspawner.language.MessageService;
import github.nighter.smartspawner.spawner.gui.synchronization.SpawnerGuiViewManager;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import github.nighter.smartspawner.spawner.properties.VirtualInventory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SpawnerSellManager {
   private final SmartSpawner plugin;
   private final MessageService messageService;
   private final SpawnerGuiViewManager spawnerGuiViewManager;

   public SpawnerSellManager(SmartSpawner plugin) {
      this.plugin = plugin;
      this.messageService = plugin.getMessageService();
      this.spawnerGuiViewManager = plugin.getSpawnerGuiViewManager();
   }

   public void sellAllItems(Player player, SpawnerData spawner) {
      boolean inventoryLockAcquired = spawner.getInventoryLock().tryLock();
      if (!inventoryLockAcquired) {
         this.messageService.sendMessage(player, "action_in_progress");
      } else {
         try {
            boolean sellLockAcquired = spawner.getSellLock().tryLock();
            if (!sellLockAcquired) {
               this.messageService.sendMessage(player, "action_in_progress");
               return;
            }

            try {
               VirtualInventory virtualInv = spawner.getVirtualInventory();
               if (virtualInv.getUsedSlots() == 0) {
                  this.messageService.sendMessage(player, "no_items");
                  return;
               }

               if (spawner.isSellValueDirty()) {
                  spawner.recalculateSellValue();
               }

               Map<VirtualInventory.ItemSignature, Long> consolidatedItems = virtualInv.getConsolidatedItems();
               SellResult result = this.calculateSellValue(consolidatedItems, spawner);
               spawner.setLastSellResult(result);
               this.processSellResult(player, spawner, result);
            } finally {
               spawner.getSellLock().unlock();
            }
         } finally {
            spawner.getInventoryLock().unlock();
         }

      }
   }

   private void processSellResult(Player player, SpawnerData spawner, SellResult sellResult) {
      boolean inventoryLockAcquired = spawner.getInventoryLock().tryLock();
      if (!inventoryLockAcquired) {
         this.messageService.sendMessage(player, "action_in_progress");
      } else {
         try {
            boolean sellLockAcquired = spawner.getSellLock().tryLock();
            if (!sellLockAcquired) {
               this.messageService.sendMessage(player, "action_in_progress");
               return;
            }

            try {
               VirtualInventory virtualInv = spawner.getVirtualInventory();
               if (!sellResult.isSuccessful()) {
                  this.messageService.sendMessage(player, "no_sellable_items");
                  return;
               }

               double amount = sellResult.getTotalValue();
               if (SpawnerSellEvent.getHandlerList().getRegisteredListeners().length != 0) {
                  SpawnerSellEvent event = new SpawnerSellEvent(player, spawner.getSpawnerLocation(), sellResult.getItemsToRemove(), amount);
                  Bukkit.getPluginManager().callEvent(event);
                  if (event.isCancelled()) {
                     return;
                  }

                  if (event.getMoneyAmount() >= 0.0D) {
                     amount = event.getMoneyAmount();
                  }
               }

               boolean depositSuccess = this.plugin.getItemPriceManager().getCurrencyManager().deposit(amount, player);
               if (depositSuccess) {
                  boolean itemsRemoved = spawner.removeItemsAndUpdateSellValue(sellResult.getItemsToRemove());
                  if (!itemsRemoved) {
                     Logger var10000 = this.plugin.getLogger();
                     String var10001 = player.getName();
                     var10000.warning("Critical: Could not remove all items after depositing money for player " + var10001 + " at spawner " + spawner.getSpawnerId() + ". Possible exploit detected.");
                     this.plugin.getItemPriceManager().getCurrencyManager().withdraw(amount, player);
                     this.messageService.sendMessage(player, "sell_failed");
                  }

                  spawner.updateHologramData();
                  if (spawner.getIsAtCapacity() && virtualInv.getUsedSlots() < spawner.getMaxSpawnerLootSlots()) {
                     spawner.setIsAtCapacity(false);
                  }

                  this.spawnerGuiViewManager.updateSpawnerMenuViewers(spawner);
                  Map<String, String> placeholders = new HashMap();
                  placeholders.put("amount", this.plugin.getLanguageManager().formatNumber((double)sellResult.getItemsSold()));
                  placeholders.put("price", this.plugin.getLanguageManager().formatNumber(amount));
                  this.messageService.sendMessage((Player)player, "sell_success", placeholders);
                  player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
                  this.plugin.getSpawnerManager().markSpawnerModified(spawner.getSpawnerId());
                  spawner.markLastSellAsProcessed();
                  return;
               }

               this.messageService.sendMessage(player, "sell_failed");
            } finally {
               spawner.getSellLock().unlock();
            }
         } finally {
            spawner.getInventoryLock().unlock();
         }

      }
   }

   private SellResult calculateSellValue(Map<VirtualInventory.ItemSignature, Long> consolidatedItems, SpawnerData spawner) {
      double totalValue = spawner.getAccumulatedSellValue();
      long totalItemsSold = 0L;
      ArrayList<ItemStack> itemsToRemove = new ArrayList();
      Iterator var8 = consolidatedItems.entrySet().iterator();

      while(var8.hasNext()) {
         Entry<VirtualInventory.ItemSignature, Long> entry = (Entry)var8.next();
         ItemStack templateRef = ((VirtualInventory.ItemSignature)entry.getKey()).getTemplateRef();
         long amount = (Long)entry.getValue();
         int maxStackSize = templateRef.getMaxStackSize();
         totalItemsSold += amount;
         int stacksNeeded = (int)Math.ceil((double)amount / (double)maxStackSize);
         itemsToRemove.ensureCapacity(itemsToRemove.size() + stacksNeeded);

         int stackSize;
         for(long remainingAmount = amount; remainingAmount > 0L; remainingAmount -= (long)stackSize) {
            ItemStack stackToRemove = templateRef.clone();
            stackSize = (int)Math.min(remainingAmount, (long)maxStackSize);
            stackToRemove.setAmount(stackSize);
            itemsToRemove.add(stackToRemove);
         }
      }

      return new SellResult(totalValue, totalItemsSold, itemsToRemove);
   }
}
