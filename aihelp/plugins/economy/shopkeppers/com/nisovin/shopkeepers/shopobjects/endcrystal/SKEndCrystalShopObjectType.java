package com.nisovin.shopkeepers.shopobjects.endcrystal;

import com.nisovin.shopkeepers.api.shopobjects.endcrystal.EndCrystalShopObjectType;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectCreationContext;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectType;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShops;
import org.bukkit.entity.EntityType;

public final class SKEndCrystalShopObjectType extends BaseEntityShopObjectType<SKEndCrystalShop> implements EndCrystalShopObjectType<SKEndCrystalShop> {
   public SKEndCrystalShopObjectType(BaseEntityShops entityShops) {
      super(new BaseEntityShopObjectCreationContext(entityShops), EntityType.END_CRYSTAL, SKEndCrystalShop.class, SKEndCrystalShop::new);
   }

   public boolean isEnabled() {
      return Settings.enableEndCrystalShops;
   }
}
