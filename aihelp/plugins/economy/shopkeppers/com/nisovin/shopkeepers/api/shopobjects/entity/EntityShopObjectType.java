package com.nisovin.shopkeepers.api.shopobjects.entity;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopobjects.ShopObjectType;
import org.bukkit.entity.Entity;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface EntityShopObjectType<T extends EntityShopObject> extends ShopObjectType<T> {
   @Nullable
   Shopkeeper getShopkeeper(Entity var1);

   boolean isShopkeeper(Entity var1);
}
