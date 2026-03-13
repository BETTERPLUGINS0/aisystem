package com.nisovin.shopkeepers.ui;

import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.ui.lib.ViewContext;
import com.nisovin.shopkeepers.util.java.Validate;

public class ShopkeeperViewContext implements ViewContext {
   private final AbstractShopkeeper shopkeeper;

   public ShopkeeperViewContext(AbstractShopkeeper shopkeeper) {
      Validate.notNull(shopkeeper, (String)"shopkeeper is null");
      this.shopkeeper = shopkeeper;
   }

   public String getName() {
      return "Shopkeeper " + this.shopkeeper.getId();
   }

   public AbstractShopkeeper getObject() {
      return this.shopkeeper;
   }

   public String getLogPrefix() {
      return this.shopkeeper.getLogPrefix();
   }

   public boolean isValid() {
      return this.shopkeeper.isValid();
   }

   public Text getNoLongerValidMessage() {
      return Messages.shopNoLongerExists;
   }
}
