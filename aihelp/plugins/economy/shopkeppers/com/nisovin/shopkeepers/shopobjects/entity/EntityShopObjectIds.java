package com.nisovin.shopkeepers.shopobjects.entity;

import com.nisovin.shopkeepers.util.bukkit.EntityUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.entity.Entity;

public final class EntityShopObjectIds {
   public static Object getObjectId(Entity entity) {
      Validate.notNull(entity, (String)"entity is null");
      Entity resolvedEntity = EntityUtils.resolveComplexEntity(entity);
      return resolvedEntity.getUniqueId();
   }

   private EntityShopObjectIds() {
   }
}
