package com.nisovin.shopkeepers.api.shopobjects.living;

import java.util.Collection;
import org.bukkit.entity.EntityType;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface LivingShopObjectTypes {
   Collection<? extends LivingShopObjectType<?>> getAll();

   @Nullable
   LivingShopObjectType<?> get(EntityType var1);
}
