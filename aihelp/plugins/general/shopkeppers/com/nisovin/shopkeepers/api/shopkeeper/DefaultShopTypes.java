package com.nisovin.shopkeepers.api.shopkeeper;

import com.nisovin.shopkeepers.api.ShopkeepersAPI;
import com.nisovin.shopkeepers.api.shopkeeper.admin.AdminShopType;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopType;
import java.util.List;

public interface DefaultShopTypes {
   List<? extends ShopType<?>> getAll();

   /** @deprecated */
   @Deprecated
   ShopType<?> getAdminShopType();

   AdminShopType<?> getRegularAdminShopType();

   PlayerShopType<?> getSellingPlayerShopType();

   PlayerShopType<?> getBuyingPlayerShopType();

   PlayerShopType<?> getTradingPlayerShopType();

   PlayerShopType<?> getBookPlayerShopType();

   static DefaultShopTypes getInstance() {
      return ShopkeepersAPI.getPlugin().getDefaultShopTypes();
   }

   /** @deprecated */
   @Deprecated
   static ShopType<?> ADMIN() {
      return ADMIN_REGULAR();
   }

   static AdminShopType<?> ADMIN_REGULAR() {
      return getInstance().getRegularAdminShopType();
   }

   static PlayerShopType<?> PLAYER_SELLING() {
      return getInstance().getSellingPlayerShopType();
   }

   static PlayerShopType<?> PLAYER_BUYING() {
      return getInstance().getBuyingPlayerShopType();
   }

   static PlayerShopType<?> PLAYER_TRADING() {
      return getInstance().getTradingPlayerShopType();
   }

   static PlayerShopType<?> PLAYER_BOOK() {
      return getInstance().getBookPlayerShopType();
   }
}
