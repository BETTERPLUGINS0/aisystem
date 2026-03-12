package com.nisovin.shopkeepers.api.shopkeeper;

import com.google.common.base.Preconditions;
import com.nisovin.shopkeepers.api.shopobjects.ShopObjectType;
import com.nisovin.shopkeepers.api.shopobjects.virtual.VirtualShopObjectType;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class ShopCreationData {
   @Nullable
   private final Player creator;
   private final ShopType<?> shopType;
   private final ShopObjectType<?> shopObjectType;
   @Nullable
   private Location spawnLocation;
   @Nullable
   private BlockFace targetedBlockFace;
   @Nullable
   private Map<String, Object> additionalData;

   protected ShopCreationData(@Nullable Player creator, ShopType<?> shopType, ShopObjectType<?> shopObjectType, @Nullable Location spawnLocation, @Nullable BlockFace targetedBlockFace) {
      Preconditions.checkNotNull(shopType, "shopType is null");
      Preconditions.checkNotNull(shopObjectType, "shopObjectType is null");
      this.creator = creator;
      this.shopType = shopType;
      this.shopObjectType = shopObjectType;
      if (spawnLocation != null) {
         Preconditions.checkNotNull(spawnLocation.getWorld(), "spawnLocation has no world");
         spawnLocation.checkFinite();
         this.spawnLocation = spawnLocation.clone();
      } else {
         Preconditions.checkArgument(shopObjectType instanceof VirtualShopObjectType, "spawnLocation is null, but the shop object type is not virtual");
         this.spawnLocation = null;
      }

      this.targetedBlockFace = targetedBlockFace;
   }

   @Nullable
   public Player getCreator() {
      return this.creator;
   }

   public ShopType<?> getShopType() {
      return this.shopType;
   }

   public ShopObjectType<?> getShopObjectType() {
      return this.shopObjectType;
   }

   @Nullable
   public Location getSpawnLocation() {
      return this.spawnLocation != null ? this.spawnLocation.clone() : null;
   }

   public void setSpawnLocation(@Nullable Location newSpawnLocation) {
      if (!(this.shopObjectType instanceof VirtualShopObjectType)) {
         Preconditions.checkNotNull(newSpawnLocation, "newSpawnLocation is null, but the shop object type is not virtual");
      }

      if (newSpawnLocation == null) {
         this.spawnLocation = null;
      } else {
         Preconditions.checkNotNull(newSpawnLocation.getWorld(), "newSpawnLocation has no world");
         newSpawnLocation.checkFinite();
         if (this.spawnLocation != null) {
            Preconditions.checkArgument(this.spawnLocation.getWorld() == newSpawnLocation.getWorld(), "Cannot set the spawn location to a different world!");
         }

         this.spawnLocation = newSpawnLocation.clone();
      }

   }

   @Nullable
   public BlockFace getTargetedBlockFace() {
      return this.targetedBlockFace;
   }

   public void setTargetedBlockFace(@Nullable BlockFace blockFace) {
      this.targetedBlockFace = blockFace;
   }

   @Nullable
   public <T> T getValue(String key) {
      return this.additionalData != null ? this.additionalData.get(key) : null;
   }

   public <T> void setValue(String key, @Nullable T value) {
      if (value == null) {
         if (this.additionalData != null) {
            this.additionalData.remove(key);
         }
      } else {
         if (this.additionalData == null) {
            this.additionalData = new HashMap();
         }

         assert this.additionalData != null;

         this.additionalData.put(key, value);
      }

   }
}
