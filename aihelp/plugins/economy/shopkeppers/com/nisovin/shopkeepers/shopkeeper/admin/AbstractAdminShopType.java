package com.nisovin.shopkeepers.shopkeeper.admin;

import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopkeeper.admin.AdminShopCreationData;
import com.nisovin.shopkeepers.api.shopkeeper.admin.AdminShopType;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopType;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.List;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AbstractAdminShopType<T extends AbstractAdminShopkeeper> extends AbstractShopType<T> implements AdminShopType<T> {
   protected AbstractAdminShopType(String identifier, List<? extends String> aliases, @Nullable String permission, Class<T> shopkeeperType) {
      super(identifier, aliases, permission, shopkeeperType);
   }

   protected void validateCreationData(ShopCreationData shopCreationData) {
      super.validateCreationData(shopCreationData);
      Validate.isTrue(shopCreationData instanceof AdminShopCreationData, () -> {
         String var10000 = AdminShopCreationData.class.getName();
         return "shopCreationData is not of type " + var10000 + ", but: " + shopCreationData.getClass().getName();
      });
   }
}
