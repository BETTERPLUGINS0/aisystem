package com.nisovin.shopkeepers.shopkeeper;

import com.nisovin.shopkeepers.util.data.property.value.AbstractPropertyValuesHolder;
import com.nisovin.shopkeepers.util.java.Validate;

public class ShopkeeperPropertyValuesHolder extends AbstractPropertyValuesHolder {
   private final AbstractShopkeeper shopkeeper;

   public ShopkeeperPropertyValuesHolder(AbstractShopkeeper shopkeeper) {
      Validate.notNull(shopkeeper, (String)"shopkeeper is null");
      this.shopkeeper = shopkeeper;
   }

   public final AbstractShopkeeper getShopkeeper() {
      return this.shopkeeper;
   }

   public String getLogPrefix() {
      return this.shopkeeper.getLogPrefix();
   }

   public void markDirty() {
      this.shopkeeper.markDirty();
   }
}
