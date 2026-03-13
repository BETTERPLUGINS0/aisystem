package com.nisovin.shopkeepers.shopkeeper.admin.regular;

import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.admin.AbstractAdminShopType;
import java.util.Collections;
import java.util.List;

public final class RegularAdminShopType extends AbstractAdminShopType<SKRegularAdminShopkeeper> {
   public RegularAdminShopType() {
      super("admin", Collections.emptyList(), "shopkeeper.admin", SKRegularAdminShopkeeper.class);
   }

   public String getDisplayName() {
      return Messages.shopTypeAdminRegular;
   }

   public String getDescription() {
      return Messages.shopTypeDescAdminRegular;
   }

   public String getSetupDescription() {
      return Messages.shopSetupDescAdminRegular;
   }

   public List<? extends String> getTradeSetupDescription() {
      return Messages.tradeSetupDescAdminRegular;
   }

   protected SKRegularAdminShopkeeper createNewShopkeeper() {
      return new SKRegularAdminShopkeeper();
   }
}
