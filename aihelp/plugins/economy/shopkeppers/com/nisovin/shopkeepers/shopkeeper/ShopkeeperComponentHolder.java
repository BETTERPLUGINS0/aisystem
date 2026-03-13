package com.nisovin.shopkeepers.shopkeeper;

import com.nisovin.shopkeepers.component.ComponentHolder;
import com.nisovin.shopkeepers.util.java.Validate;

public class ShopkeeperComponentHolder extends ComponentHolder {
   private final AbstractShopkeeper shopkeeper;

   public ShopkeeperComponentHolder(AbstractShopkeeper shopkeeper) {
      Validate.notNull(shopkeeper, (String)"shopkeeper is null");
      this.shopkeeper = shopkeeper;
   }

   public final AbstractShopkeeper getShopkeeper() {
      return this.shopkeeper;
   }
}
