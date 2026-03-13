package com.nisovin.shopkeepers.api.shopobjects.entity;

import com.nisovin.shopkeepers.api.shopobjects.ShopObject;
import org.bukkit.entity.Entity;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface EntityShopObject extends ShopObject {
   @Nullable
   Entity getEntity();
}
