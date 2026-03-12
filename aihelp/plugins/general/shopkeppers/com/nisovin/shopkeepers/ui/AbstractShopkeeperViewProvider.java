package com.nisovin.shopkeepers.ui;

import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.ui.lib.AbstractUIType;
import com.nisovin.shopkeepers.ui.lib.ViewProvider;

public abstract class AbstractShopkeeperViewProvider extends ViewProvider implements ShopkeeperViewProvider {
   protected AbstractShopkeeperViewProvider(AbstractUIType uiType, AbstractShopkeeper shopkeeper) {
      super(uiType, new ShopkeeperViewContext(shopkeeper));
   }

   public ShopkeeperViewContext getContext() {
      return (ShopkeeperViewContext)super.getContext();
   }

   public AbstractShopkeeper getShopkeeper() {
      return this.getContext().getObject();
   }
}
