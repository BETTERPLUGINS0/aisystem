package com.nisovin.shopkeepers.shopobjects.living;

import com.nisovin.shopkeepers.api.shopobjects.living.LivingShopObjectType;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectType;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.List;
import org.bukkit.entity.EntityType;

public final class SKLivingShopObjectType<T extends SKLivingShopObject<?>> extends BaseEntityShopObjectType<T> implements LivingShopObjectType<T> {
   protected SKLivingShopObjectType(LivingShopObjectCreationContext shopCreationContext, EntityType entityType, Class<T> shopObjectType, BaseEntityShopObjectType.ShopObjectConstructor<T> shopObjectConstructor) {
      this(shopCreationContext, entityType, getIdentifier(entityType), getAliasesFor(entityType), getPermission(entityType), shopObjectType, shopObjectConstructor);
   }

   protected SKLivingShopObjectType(LivingShopObjectCreationContext shopCreationContext, EntityType entityType, String identifier, List<? extends String> aliases, String permission, Class<T> shopObjectType, BaseEntityShopObjectType.ShopObjectConstructor<T> shopObjectConstructor) {
      super(shopCreationContext, entityType, identifier, aliases, permission, shopObjectType, shopObjectConstructor);
      Validate.isTrue(entityType.isAlive(), "entityType is not alive");
   }

   public boolean isEnabled() {
      return Settings.DerivedSettings.enabledLivingShops.contains(this.entityType);
   }
}
