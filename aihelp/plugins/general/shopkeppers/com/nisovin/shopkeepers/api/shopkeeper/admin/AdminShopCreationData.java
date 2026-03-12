package com.nisovin.shopkeepers.api.shopkeeper.admin;

import com.google.common.base.Preconditions;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopkeeper.ShopType;
import com.nisovin.shopkeepers.api.shopobjects.ShopObjectType;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public class AdminShopCreationData extends ShopCreationData {
   private static AdminShopType<?> toAdminShopType(ShopType<?> shopType) {
      Preconditions.checkArgument(shopType instanceof AdminShopType, "shopType has to be an AdminShopType");
      return (AdminShopType)shopType;
   }

   /** @deprecated */
   @Deprecated
   public static AdminShopCreationData create(@Nullable Player creator, ShopType<?> shopType, ShopObjectType<?> shopObjectType, @Nullable Location spawnLocation, @Nullable BlockFace targetedBlockFace) {
      return create(creator, toAdminShopType(shopType), shopObjectType, spawnLocation, targetedBlockFace);
   }

   public static AdminShopCreationData create(@Nullable Player creator, AdminShopType<?> shopType, ShopObjectType<?> shopObjectType, @Nullable Location spawnLocation, @Nullable BlockFace targetedBlockFace) {
      return new AdminShopCreationData(creator, shopType, shopObjectType, spawnLocation, targetedBlockFace);
   }

   /** @deprecated */
   @Deprecated
   protected AdminShopCreationData(@Nullable Player creator, ShopType<?> shopType, ShopObjectType<?> shopObjectType, @Nullable Location spawnLocation, @Nullable BlockFace targetedBlockFace) {
      this(creator, toAdminShopType(shopType), shopObjectType, spawnLocation, targetedBlockFace);
   }

   protected AdminShopCreationData(@Nullable Player creator, AdminShopType<?> shopType, ShopObjectType<?> shopObjectType, @Nullable Location spawnLocation, @Nullable BlockFace targetedBlockFace) {
      super(creator, shopType, shopObjectType, spawnLocation, targetedBlockFace);
   }
}
