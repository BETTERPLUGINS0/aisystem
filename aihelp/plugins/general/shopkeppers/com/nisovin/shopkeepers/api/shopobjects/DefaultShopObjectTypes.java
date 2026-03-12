package com.nisovin.shopkeepers.api.shopobjects;

import com.nisovin.shopkeepers.api.ShopkeepersAPI;
import com.nisovin.shopkeepers.api.shopobjects.citizens.CitizensShopObjectType;
import com.nisovin.shopkeepers.api.shopobjects.endcrystal.EndCrystalShopObjectType;
import com.nisovin.shopkeepers.api.shopobjects.living.LivingShopObjectTypes;
import com.nisovin.shopkeepers.api.shopobjects.sign.HangingSignShopObjectType;
import com.nisovin.shopkeepers.api.shopobjects.sign.SignShopObjectType;
import java.util.List;

public interface DefaultShopObjectTypes {
   List<? extends ShopObjectType<?>> getAll();

   LivingShopObjectTypes getLivingShopObjectTypes();

   EndCrystalShopObjectType<?> getEndCrystalShopObjectType();

   SignShopObjectType<?> getSignShopObjectType();

   HangingSignShopObjectType<?> getHangingSignShopObjectType();

   CitizensShopObjectType<?> getCitizensShopObjectType();

   static DefaultShopObjectTypes getInstance() {
      return ShopkeepersAPI.getPlugin().getDefaultShopObjectTypes();
   }

   static LivingShopObjectTypes LIVING() {
      return getInstance().getLivingShopObjectTypes();
   }

   static SignShopObjectType<?> SIGN() {
      return getInstance().getSignShopObjectType();
   }

   static HangingSignShopObjectType<?> HANGING_SIGN() {
      return getInstance().getHangingSignShopObjectType();
   }

   static CitizensShopObjectType<?> CITIZEN() {
      return getInstance().getCitizensShopObjectType();
   }
}
