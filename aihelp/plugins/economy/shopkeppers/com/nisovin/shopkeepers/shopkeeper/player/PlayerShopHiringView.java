package com.nisovin.shopkeepers.shopkeeper.player;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.events.PlayerShopkeeperHireEvent;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperRegistry;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.playershops.PlayerShopsLimit;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.ui.hiring.HiringView;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.inventory.InventoryUtils;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.logging.Log;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PlayerShopHiringView extends HiringView {
   protected static final int HIRE_COST_SLOT = 4;
   protected static final int HIRE_BUTTON_1_SLOT = 2;
   protected static final int HIRE_BUTTON_2_SLOT = 6;

   public PlayerShopHiringView(PlayerShopHiringViewProvider provider, Player player, UIState uiState) {
      super(provider, player, uiState);
   }

   public AbstractPlayerShopkeeper getShopkeeperNonNull() {
      return (AbstractPlayerShopkeeper)super.getShopkeeperNonNull();
   }

   @Nullable
   protected InventoryView openInventoryView() {
      Inventory inventory = Bukkit.createInventory((InventoryHolder)null, 9, Messages.forHireTitle);
      this.updateInventory(inventory);
      Player player = this.getPlayer();
      return player.openInventory(inventory);
   }

   private void updateInventory(Inventory inventory) {
      PlayerShopkeeper shopkeeper = this.getShopkeeperNonNull();
      ItemStack hireItem = Settings.DerivedSettings.hireButtonItem.createItemStack();
      inventory.setItem(2, hireItem);
      inventory.setItem(6, hireItem);
      UnmodifiableItemStack hireCost = shopkeeper.getHireCost();
      if (hireCost != null) {
         inventory.setItem(4, ItemUtils.asItemStack(hireCost));
      }

   }

   public void updateInventory() {
      Inventory inventory = this.getInventory();
      this.updateInventory(inventory);
      this.syncInventory();
   }

   private boolean canPlayerHireShopType(Player player, Shopkeeper shopkeeper) {
      if (!Settings.hireRequireCreationPermission) {
         return true;
      } else if (!shopkeeper.getType().hasPermission(player)) {
         return false;
      } else {
         return shopkeeper.getShopObject().getType().hasPermission(player);
      }
   }

   private int getOwnedShopsCount(Player player) {
      assert player != null;

      ShopkeeperRegistry shopkeeperRegistry = SKShopkeepersPlugin.getInstance().getShopkeeperRegistry();
      return shopkeeperRegistry.getPlayerShopkeepersByOwner(player.getUniqueId()).size();
   }

   protected void onInventoryClickEarly(InventoryClickEvent event) {
      super.onInventoryClickEarly(event);
      if (!this.isAutomaticShiftLeftClick()) {
         Player player = this.getPlayer();
         PlayerShopkeeper shopkeeper = this.getShopkeeperNonNull();
         int slot = event.getRawSlot();
         if (slot == 2 || slot == 6) {
            if (!this.canPlayerHireShopType(player, shopkeeper)) {
               TextUtils.sendMessage(player, (Text)Messages.cannotHireShopType);
               this.abortDelayed();
               return;
            }

            UnmodifiableItemStack hireCost = shopkeeper.getHireCost();
            if (hireCost == null) {
               this.abortDelayed();
               return;
            }

            PlayerInventory playerInventory = player.getInventory();
            ItemStack[] newPlayerInventoryContents = (ItemStack[])Unsafe.castNonNull(playerInventory.getContents());
            if (InventoryUtils.removeItems(newPlayerInventoryContents, hireCost) != 0) {
               TextUtils.sendMessage(player, (Text)Messages.cannotHire);
               this.abortDelayed();
               return;
            }

            int maxShopsLimit = PlayerShopsLimit.getMaxShopsLimit(player);
            PlayerShopkeeperHireEvent hireEvent = new PlayerShopkeeperHireEvent(shopkeeper, player, newPlayerInventoryContents, maxShopsLimit);
            Bukkit.getPluginManager().callEvent(hireEvent);
            if (hireEvent.isCancelled()) {
               Log.debug("PlayerShopkeeperHireEvent was cancelled!");
               this.abortDelayed();
               return;
            }

            maxShopsLimit = hireEvent.getMaxShopsLimit();
            if (maxShopsLimit != Integer.MAX_VALUE) {
               int ownedShopsCount = this.getOwnedShopsCount(player);
               if (ownedShopsCount >= maxShopsLimit) {
                  TextUtils.sendMessage(player, (Text)Messages.tooManyShops);
                  this.abortDelayed();
                  return;
               }
            }

            InventoryUtils.setContents(playerInventory, newPlayerInventoryContents);
            shopkeeper.setForHire((UnmodifiableItemStack)null);
            shopkeeper.setOwner(player);
            shopkeeper.save();
            TextUtils.sendMessage(player, (Text)Messages.hired);
            shopkeeper.abortUISessionsDelayed();
         }

      }
   }
}
