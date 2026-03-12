package com.nisovin.shopkeepers.shopobjects;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.shopobjects.DefaultShopObjectTypes;
import com.nisovin.shopkeepers.shopobjects.block.base.BaseBlockShops;
import com.nisovin.shopkeepers.shopobjects.citizens.SKCitizensShopObjectType;
import com.nisovin.shopkeepers.shopobjects.endcrystal.SKEndCrystalShopObjectType;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShops;
import com.nisovin.shopkeepers.shopobjects.living.SKLivingShopObjectTypes;
import com.nisovin.shopkeepers.shopobjects.sign.SKHangingSignShopObjectType;
import com.nisovin.shopkeepers.shopobjects.sign.SKSignShopObjectType;
import java.util.ArrayList;
import java.util.List;

public final class SKDefaultShopObjectTypes implements DefaultShopObjectTypes {
   private final SKShopkeepersPlugin plugin;
   private final SKEndCrystalShopObjectType endCrystalShopObjectType;
   private final SKSignShopObjectType signShopObjectType;
   private final SKHangingSignShopObjectType hangingSignShopObjectType;

   public SKDefaultShopObjectTypes(SKShopkeepersPlugin plugin, BaseBlockShops blockShops, BaseEntityShops entityShops) {
      this.plugin = plugin;
      this.endCrystalShopObjectType = new SKEndCrystalShopObjectType(entityShops);
      this.signShopObjectType = new SKSignShopObjectType(blockShops);
      this.hangingSignShopObjectType = new SKHangingSignShopObjectType(blockShops);
   }

   public void onRegisterDefaults() {
      this.getLivingShopObjectTypes().onRegisterDefaults();
      this.endCrystalShopObjectType.registerPermission();
   }

   public List<? extends AbstractShopObjectType<?>> getAll() {
      List<AbstractShopObjectType<?>> shopObjectTypes = new ArrayList();
      shopObjectTypes.addAll(this.getLivingShopObjectTypes().getAll());
      shopObjectTypes.add(this.getEndCrystalShopObjectType());
      shopObjectTypes.add(this.getSignShopObjectType());
      shopObjectTypes.add(this.getHangingSignShopObjectType());
      shopObjectTypes.add(this.getCitizensShopObjectType());
      return shopObjectTypes;
   }

   public SKLivingShopObjectTypes getLivingShopObjectTypes() {
      return this.plugin.getLivingShops().getLivingShopObjectTypes();
   }

   public SKEndCrystalShopObjectType getEndCrystalShopObjectType() {
      return this.endCrystalShopObjectType;
   }

   public SKSignShopObjectType getSignShopObjectType() {
      return this.signShopObjectType;
   }

   public SKHangingSignShopObjectType getHangingSignShopObjectType() {
      return this.hangingSignShopObjectType;
   }

   public SKCitizensShopObjectType getCitizensShopObjectType() {
      return this.plugin.getCitizensShops().getCitizensShopObjectType();
   }

   public static SKDefaultShopObjectTypes getInstance() {
      return SKShopkeepersPlugin.getInstance().getDefaultShopObjectTypes();
   }

   public static SKLivingShopObjectTypes LIVING() {
      return getInstance().getLivingShopObjectTypes();
   }

   public static SKEndCrystalShopObjectType END_CRYSTAL() {
      return getInstance().getEndCrystalShopObjectType();
   }

   public static SKSignShopObjectType SIGN() {
      return getInstance().getSignShopObjectType();
   }

   public static SKHangingSignShopObjectType HANGING_SIGN() {
      return getInstance().getHangingSignShopObjectType();
   }

   public static SKCitizensShopObjectType CITIZEN() {
      return getInstance().getCitizensShopObjectType();
   }
}
