package com.nisovin.shopkeepers.shopobjects.living;

import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectCreationContext;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShops;

public class LivingShopObjectCreationContext extends BaseEntityShopObjectCreationContext {
   public final LivingShops livingShops;

   public LivingShopObjectCreationContext(BaseEntityShops baseEntityShops, LivingShops livingShops) {
      super(baseEntityShops);
      this.livingShops = livingShops;
   }
}
