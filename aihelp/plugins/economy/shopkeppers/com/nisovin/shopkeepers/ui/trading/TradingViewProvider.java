package com.nisovin.shopkeepers.ui.trading;

import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.ui.AbstractShopkeeperViewProvider;
import com.nisovin.shopkeepers.ui.SKDefaultUITypes;
import com.nisovin.shopkeepers.ui.lib.AbstractUIType;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.ui.lib.View;
import com.nisovin.shopkeepers.util.bukkit.PermissionUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public class TradingViewProvider extends AbstractShopkeeperViewProvider {
   private final List<TradingListener> tradingListeners;

   public TradingViewProvider(AbstractShopkeeper shopkeeper) {
      this(SKDefaultUITypes.TRADING(), shopkeeper);
   }

   public TradingViewProvider(AbstractUIType uiType, AbstractShopkeeper shopkeeper) {
      super(uiType, shopkeeper);
      this.tradingListeners = new ArrayList();
   }

   public final void addListener(TradingListener listener) {
      Validate.notNull(listener, (String)"listener is null");
      this.tradingListeners.add(listener);
   }

   final List<TradingListener> getTradingListeners() {
      return this.tradingListeners;
   }

   public boolean canAccess(Player player, boolean silent) {
      Validate.notNull(player, (String)"player is null");
      if (!PermissionUtils.hasPermission(player, "shopkeeper.trade")) {
         if (!silent) {
            this.debugNotOpeningUI(player, "Player is missing trade permission.");
            TextUtils.sendMessage(player, (Text)Messages.missingTradePerm);
         }

         return false;
      } else {
         return true;
      }
   }

   public boolean canOpen(Player player, boolean silent) {
      Validate.notNull(player, (String)"player is null");
      if (!super.canOpen(player, silent)) {
         return false;
      } else {
         AbstractShopkeeper shopkeeper = this.getShopkeeper();
         if (!shopkeeper.isOpen()) {
            if (!silent) {
               this.debugNotOpeningUI(player, "Shopkeeper is closed.");
               TextUtils.sendMessage(player, (Text)Messages.shopCurrentlyClosed);
            }

            return false;
         } else if (!shopkeeper.hasTradingRecipes(player)) {
            if (!silent) {
               this.debugNotOpeningUI(player, "Shopkeeper has no offers.");
               TextUtils.sendMessage(player, (Text)Messages.cannotTradeNoOffers);
               if (shopkeeper.canEdit(player, true)) {
                  TextUtils.sendMessage(player, (String)Messages.noOffersOpenEditorDescription);
               }
            }

            return false;
         } else {
            return true;
         }
      }
   }

   @Nullable
   protected View createView(Player player, UIState uiState) {
      return new TradingView(this, player, uiState);
   }
}
