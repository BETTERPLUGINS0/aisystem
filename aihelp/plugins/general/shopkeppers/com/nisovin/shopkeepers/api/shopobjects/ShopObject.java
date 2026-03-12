package com.nisovin.shopkeepers.api.shopobjects;

import org.bukkit.Location;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface ShopObject {
   ShopObjectType<?> getType();

   boolean isSpawned();

   boolean isActive();

   @Nullable
   Location getLocation();

   int getNameLengthLimit();

   @Nullable
   String prepareName(@Nullable String var1);

   void setName(@Nullable String var1);

   @Nullable
   String getName();
}
