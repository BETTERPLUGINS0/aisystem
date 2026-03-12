package com.nisovin.shopkeepers.shopkeeper.player;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.ui.trading.Trade;
import com.nisovin.shopkeepers.ui.trading.TradingContext;
import com.nisovin.shopkeepers.ui.trading.TradingView;
import com.nisovin.shopkeepers.util.bukkit.PermissionUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PlayerShopTradingView extends TradingView {
   @Nullable
   protected Inventory containerInventory = null;
   @Nullable
   protected ItemStack[] newContainerContents = null;

   protected PlayerShopTradingView(PlayerShopTradingViewProvider provider, Player player, UIState uiState) {
      super(provider, player, uiState);
   }

   public AbstractPlayerShopkeeper getShopkeeperNonNull() {
      return (AbstractPlayerShopkeeper)super.getShopkeeperNonNull();
   }

   protected boolean prepareTrade(Trade trade) {
      if (!super.prepareTrade(trade)) {
         return false;
      } else {
         AbstractPlayerShopkeeper shopkeeper = this.getShopkeeperNonNull();
         Player tradingPlayer = trade.getTradingPlayer();
         if (Settings.preventTradingWithOwnShop && shopkeeper.isOwner(tradingPlayer) && !PermissionUtils.hasPermission(tradingPlayer, "shopkeeper.bypass")) {
            TextUtils.sendMessage(tradingPlayer, (Text)Messages.cannotTradeWithOwnShop);
            this.debugPreventedTrade("Trading with the own shop is not allowed.");
            return false;
         } else {
            if (Settings.preventTradingWhileOwnerIsOnline) {
               Player ownerPlayer = shopkeeper.getOwner();
               if (ownerPlayer != null && !shopkeeper.isOwner(tradingPlayer) && !PermissionUtils.hasPermission(tradingPlayer, "shopkeeper.bypass")) {
                  TextUtils.sendMessage(tradingPlayer, (Text)Messages.cannotTradeWhileOwnerOnline, (Object[])("owner", Unsafe.assertNonNull(ownerPlayer.getName())));
                  this.debugPreventedTrade("Trading is not allowed while the shop owner is online.");
                  return false;
               }
            }

            Inventory containerInventory = shopkeeper.getContainerInventory();
            if (containerInventory == null) {
               TextUtils.sendMessage(tradingPlayer, (Text)Messages.cannotTradeWithShopMissingContainer, (Object[])("owner", shopkeeper.getOwnerName()));
               this.debugPreventedTrade("The shop's container is missing.");
               return false;
            } else {
               this.containerInventory = containerInventory;
               this.newContainerContents = (ItemStack[])Unsafe.cast(containerInventory.getContents());
               return true;
            }
         }
      }
   }

   protected void onTradeApplied(Trade trade) {
      super.onTradeApplied(trade);
      if (this.containerInventory != null && this.newContainerContents != null) {
         this.containerInventory.setContents((ItemStack[])Unsafe.castNonNull(this.newContainerContents));
      }

   }

   protected void onTradeOver(TradingContext tradingContext) {
      super.onTradeOver(tradingContext);
      this.containerInventory = null;
      this.newContainerContents = null;
   }
}
