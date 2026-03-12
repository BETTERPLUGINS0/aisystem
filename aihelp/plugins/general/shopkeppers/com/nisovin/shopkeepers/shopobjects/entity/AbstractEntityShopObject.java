package com.nisovin.shopkeepers.shopobjects.entity;

import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopobjects.entity.EntityShopObject;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopobjects.AbstractShopObject;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AbstractEntityShopObject extends AbstractShopObject implements EntityShopObject {
   protected AbstractEntityShopObject(AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(shopkeeper, creationData);
   }

   public abstract AbstractEntityShopObjectType<?> getType();

   public boolean isSpawned() {
      return this.getEntity() != null;
   }

   public boolean isActive() {
      Entity entity = this.getEntity();
      return entity != null && entity.isValid();
   }

   @Nullable
   public Location getLocation() {
      Entity entity = this.getEntity();
      return entity != null ? entity.getLocation() : null;
   }

   @Nullable
   public Object getId() {
      Entity entity = this.getEntity();
      return entity == null ? null : EntityShopObjectIds.getObjectId(entity);
   }

   @Nullable
   public Location getTickVisualizationParticleLocation() {
      Entity entity = this.getEntity();
      if (entity == null) {
         return null;
      } else {
         Location entityLocation = entity.getLocation();
         return entityLocation.add(0.0D, entity.getHeight() + 0.4D, 0.0D);
      }
   }
}
