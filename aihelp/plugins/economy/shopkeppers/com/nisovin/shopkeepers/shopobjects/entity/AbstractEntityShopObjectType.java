package com.nisovin.shopkeepers.shopobjects.entity;

import com.nisovin.shopkeepers.api.shopobjects.entity.EntityShopObjectType;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopobjects.AbstractShopObjectType;
import java.util.List;
import org.bukkit.entity.Entity;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AbstractEntityShopObjectType<T extends AbstractEntityShopObject> extends AbstractShopObjectType<T> implements EntityShopObjectType<T> {
   protected AbstractEntityShopObjectType(String identifier, List<? extends String> aliases, @Nullable String permission, Class<T> shopObjectType) {
      super(identifier, aliases, permission, shopObjectType);
   }

   @Nullable
   public AbstractShopkeeper getShopkeeper(Entity entity) {
      Object objectId = EntityShopObjectIds.getObjectId(entity);
      return this.getShopkeeperByObjectId(objectId);
   }

   public boolean isShopkeeper(Entity entity) {
      return this.getShopkeeper(entity) != null;
   }
}
