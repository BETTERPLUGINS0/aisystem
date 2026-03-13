package com.nisovin.shopkeepers.shopkeeper.admin;

import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperCreateException;
import com.nisovin.shopkeepers.api.shopkeeper.admin.AdminShopkeeper;
import com.nisovin.shopkeepers.api.ui.DefaultUITypes;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopkeeper.ShopkeeperData;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.ui.trading.TradingViewProvider;
import com.nisovin.shopkeepers.util.bukkit.PermissionUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.DataKeyAccessor;
import com.nisovin.shopkeepers.util.data.property.EmptyDataPredicates;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.java.StringSerializers;
import com.nisovin.shopkeepers.util.java.StringUtils;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AbstractAdminShopkeeper extends AbstractShopkeeper implements AdminShopkeeper {
   @Nullable
   private String tradePermission = null;
   public static final Property<String> TRADE_PERMISSION;

   protected AbstractAdminShopkeeper() {
   }

   protected void loadFromCreationData(int id, ShopCreationData shopCreationData) throws ShopkeeperCreateException {
      super.loadFromCreationData(id, shopCreationData);
   }

   protected void setup() {
      this.registerViewProviderIfMissing(DefaultUITypes.TRADING(), () -> {
         return new AbstractAdminShopkeeper.AdminShopTradingViewProvider(this);
      });
      super.setup();
   }

   public void loadDynamicState(ShopkeeperData shopkeeperData) throws InvalidDataException {
      super.loadDynamicState(shopkeeperData);
      this.loadTradePermission(shopkeeperData);
   }

   public void saveDynamicState(ShopkeeperData shopkeeperData, boolean saveAll) {
      super.saveDynamicState(shopkeeperData, saveAll);
      this.saveTradePermission(shopkeeperData);
   }

   private void loadTradePermission(ShopkeeperData shopkeeperData) throws InvalidDataException {
      assert shopkeeperData != null;

      this._setTradePermission((String)shopkeeperData.get(TRADE_PERMISSION));
   }

   private void saveTradePermission(ShopkeeperData shopkeeperData) {
      assert shopkeeperData != null;

      shopkeeperData.set(TRADE_PERMISSION, this.tradePermission);
   }

   @Nullable
   public String getTradePermission() {
      return this.tradePermission;
   }

   public void setTradePermission(@Nullable String tradePermission) {
      this._setTradePermission(tradePermission);
      this.markDirty();
   }

   private void _setTradePermission(@Nullable String tradePermission) {
      this.tradePermission = StringUtils.getNotEmpty(tradePermission);
   }

   static {
      TRADE_PERMISSION = (new BasicProperty()).dataAccessor((new DataKeyAccessor("tradePerm", StringSerializers.SCALAR)).emptyDataPredicate(EmptyDataPredicates.EMPTY_STRING)).nullable().defaultValue((Object)null).build();
   }

   public static class AdminShopTradingViewProvider extends TradingViewProvider {
      protected AdminShopTradingViewProvider(AbstractAdminShopkeeper shopkeeper) {
         super(shopkeeper);
      }

      public AbstractAdminShopkeeper getShopkeeper() {
         return (AbstractAdminShopkeeper)super.getShopkeeper();
      }

      public boolean canAccess(Player player, boolean silent) {
         if (!super.canAccess(player, silent)) {
            return false;
         } else {
            String tradePermission = this.getShopkeeper().getTradePermission();
            if (tradePermission != null && !PermissionUtils.hasPermission(player, tradePermission)) {
               if (!silent) {
                  this.debugNotOpeningUI(player, "Player is missing the custom trade permission '" + tradePermission + "'.");
                  TextUtils.sendMessage(player, (Text)Messages.missingCustomTradePerm);
               }

               return false;
            } else {
               return true;
            }
         }
      }
   }
}
