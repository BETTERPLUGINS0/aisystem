package com.nisovin.shopkeepers.shopobjects.citizens;

import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopobjects.citizens.CitizensShopObjectType;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopobjects.entity.AbstractEntityShopObjectType;
import java.util.Arrays;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class SKCitizensShopObjectType extends AbstractEntityShopObjectType<SKCitizensShopObject> implements CitizensShopObjectType<SKCitizensShopObject> {
   private final CitizensShops citizensShops;

   public SKCitizensShopObjectType(CitizensShops citizensShops) {
      super("citizen", Arrays.asList("npc"), "shopkeeper.citizen", SKCitizensShopObject.class);
      this.citizensShops = citizensShops;
   }

   public boolean isEnabled() {
      return this.citizensShops.isEnabled();
   }

   public String getDisplayName() {
      return Messages.shopObjectTypeNpc;
   }

   public boolean mustBeSpawned() {
      return false;
   }

   public boolean validateSpawnLocation(@Nullable Player creator, @Nullable Location spawnLocation, @Nullable BlockFace attachedBlockFace) {
      return super.validateSpawnLocation(creator, spawnLocation, attachedBlockFace);
   }

   public SKCitizensShopObject createObject(AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      return new SKCitizensShopObject(this.citizensShops, shopkeeper, creationData);
   }
}
