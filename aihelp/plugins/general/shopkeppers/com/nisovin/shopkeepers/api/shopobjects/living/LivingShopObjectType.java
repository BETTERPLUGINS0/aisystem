package com.nisovin.shopkeepers.api.shopobjects.living;

import com.nisovin.shopkeepers.api.shopobjects.entity.EntityShopObjectType;
import org.bukkit.entity.EntityType;

public interface LivingShopObjectType<T extends LivingShopObject> extends EntityShopObjectType<T> {
   EntityType getEntityType();
}
