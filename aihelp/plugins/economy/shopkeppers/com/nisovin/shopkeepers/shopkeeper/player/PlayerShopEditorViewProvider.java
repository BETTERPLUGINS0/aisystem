package com.nisovin.shopkeepers.shopkeeper.player;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.currency.Currencies;
import com.nisovin.shopkeepers.currency.Currency;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.TradingRecipeDraft;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.ui.SKDefaultUITypes;
import com.nisovin.shopkeepers.ui.editor.ShopkeeperEditorLayout;
import com.nisovin.shopkeepers.ui.editor.ShopkeeperEditorViewProvider;
import com.nisovin.shopkeepers.ui.editor.TradingRecipesAdapter;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.bukkit.PermissionUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class PlayerShopEditorViewProvider extends ShopkeeperEditorViewProvider {
   protected PlayerShopEditorViewProvider(AbstractPlayerShopkeeper shopkeeper, TradingRecipesAdapter tradingRecipesAdapter) {
      super(SKDefaultUITypes.EDITOR(), shopkeeper, tradingRecipesAdapter);
   }

   public AbstractPlayerShopkeeper getShopkeeper() {
      return (AbstractPlayerShopkeeper)super.getShopkeeper();
   }

   public boolean canAccess(Player player, boolean silent) {
      if (!super.canAccess(player, silent)) {
         return false;
      } else if (!this.getShopkeeper().isOwner(player) && !PermissionUtils.hasPermission(player, "shopkeeper.bypass")) {
         if (!silent) {
            this.debugNotOpeningUI(player, "Player is not owning this shop.");
            TextUtils.sendMessage(player, (Text)Messages.notOwner);
         }

         return false;
      } else {
         return true;
      }
   }

   protected ShopkeeperEditorLayout createLayout() {
      return new PlayerShopEditorLayout(this.getShopkeeper());
   }

   protected static TradingRecipeDraft createTradingRecipeDraft(@ReadOnly ItemStack resultItem, int cost) {
      ItemStack highCostItem = null;
      ItemStack lowCostItem = null;
      int remainingCost = cost;
      Currency highCurrency;
      if (Currencies.isHighCurrencyEnabled()) {
         highCurrency = Currencies.getHigh();
         int highCost = 0;
         if (cost > Settings.highCurrencyMinCost) {
            highCost = Math.min(cost / highCurrency.getValue(), highCurrency.getMaxStackSize());
         }

         if (highCost > 0) {
            remainingCost = cost - highCost * highCurrency.getValue();
            highCostItem = Currencies.getHigh().getItemData().createItemStack(highCost);
         }
      }

      if (remainingCost > 0) {
         highCurrency = Currencies.getBase();
         if (remainingCost <= highCurrency.getMaxStackSize()) {
            lowCostItem = Currencies.getBase().getItemData().createItemStack(remainingCost);
         } else {
            assert lowCostItem == null;

            highCostItem = null;
         }
      }

      return new TradingRecipeDraft(resultItem, lowCostItem, highCostItem);
   }

   protected static int getPrice(Shopkeeper shopkeeper, TradingRecipeDraft recipe) {
      Validate.notNull(recipe, (String)"recipe is null");
      int price = 0;
      UnmodifiableItemStack item1 = recipe.getItem1();
      Currency currency1 = Currencies.match(item1);
      if (currency1 != null) {
         assert item1 != null;

         price += currency1.getValue() * item1.getAmount();
      } else if (!ItemUtils.isEmpty(item1)) {
         Log.debug(shopkeeper.getLogPrefix() + "Price item 1 does not match any currency!");
      }

      UnmodifiableItemStack item2 = recipe.getItem2();
      Currency currency2 = Currencies.match(item2);
      if (currency2 != null) {
         assert item2 != null;

         price += currency2.getValue() * item2.getAmount();
      } else if (!ItemUtils.isEmpty(item2)) {
         Log.debug(shopkeeper.getLogPrefix() + "Price item 2 does not match any currency!");
      }

      return price;
   }
}
