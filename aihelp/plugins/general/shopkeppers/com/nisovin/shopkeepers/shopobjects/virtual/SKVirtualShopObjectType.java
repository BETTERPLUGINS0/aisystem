package com.nisovin.shopkeepers.shopobjects.virtual;

import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopobjects.virtual.VirtualShopObjectType;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopobjects.AbstractShopObjectType;
import java.util.Collections;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class SKVirtualShopObjectType extends AbstractShopObjectType<SKVirtualShopObject> implements VirtualShopObjectType<SKVirtualShopObject> {
   private final VirtualShops virtualShops;

   public SKVirtualShopObjectType(VirtualShops virtualShops) {
      super("virtual", Collections.emptyList(), "shopkeeper.virtual", SKVirtualShopObject.class);
      this.virtualShops = virtualShops;
   }

   public boolean isEnabled() {
      return false;
   }

   public String getDisplayName() {
      return "virtual";
   }

   public boolean mustBeSpawned() {
      return false;
   }

   public boolean validateSpawnLocation(@Nullable Player creator, @Nullable Location spawnLocation, @Nullable BlockFace attachedBlockFace) {
      return true;
   }

   public SKVirtualShopObject createObject(AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      return new SKVirtualShopObject(this.virtualShops, shopkeeper, creationData);
   }
}
