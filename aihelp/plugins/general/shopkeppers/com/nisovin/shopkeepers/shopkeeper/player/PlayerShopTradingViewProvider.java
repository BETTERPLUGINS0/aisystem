package com.nisovin.shopkeepers.shopkeeper.player;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.ui.lib.View;
import com.nisovin.shopkeepers.ui.trading.TradingViewProvider;
import com.nisovin.shopkeepers.util.bukkit.PermissionUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class PlayerShopTradingViewProvider extends TradingViewProvider {
   protected PlayerShopTradingViewProvider(AbstractPlayerShopkeeper shopkeeper) {
      super(shopkeeper);
   }

   public AbstractPlayerShopkeeper getShopkeeper() {
      return (AbstractPlayerShopkeeper)super.getShopkeeper();
   }

   public boolean canOpen(Player player, boolean silent) {
      if (!super.canOpen(player, silent)) {
         return false;
      } else {
         PlayerShopkeeper shopkeeper = this.getShopkeeper();
         if (Settings.preventTradingWhileOwnerIsOnline && !PermissionUtils.hasPermission(player, "shopkeeper.bypass")) {
            Player ownerPlayer = shopkeeper.getOwner();
            if (ownerPlayer != null) {
               if (!silent) {
                  this.debugNotOpeningUI(player, "Shop owner is online.");
                  TextUtils.sendMessage(player, (Text)Messages.cannotTradeWhileOwnerOnline, (Object[])("owner", Unsafe.assertNonNull(ownerPlayer.getName())));
               }

               return false;
            }
         }

         return true;
      }
   }

   @Nullable
   protected View createView(Player player, UIState uiState) {
      return new PlayerShopTradingView(this, player, uiState);
   }
}
