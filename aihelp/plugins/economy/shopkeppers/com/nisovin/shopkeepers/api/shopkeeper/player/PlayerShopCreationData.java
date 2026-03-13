package com.nisovin.shopkeepers.api.shopkeeper.player;

import com.google.common.base.Preconditions;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopkeeper.ShopType;
import com.nisovin.shopkeepers.api.shopobjects.ShopObjectType;
import com.nisovin.shopkeepers.api.shopobjects.virtual.VirtualShopObjectType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PlayerShopCreationData extends ShopCreationData {
   private final Block shopContainer;

   private static PlayerShopType<?> toPlayerShopType(ShopType<?> shopType) {
      Preconditions.checkArgument(shopType instanceof PlayerShopType, "shopType has to be a PlayerShopType");
      return (PlayerShopType)shopType;
   }

   /** @deprecated */
   @Deprecated
   public static PlayerShopCreationData create(Player creator, ShopType<?> shopType, ShopObjectType<?> shopObjectType, @Nullable Location spawnLocation, @Nullable BlockFace targetedBlockFace, Block shopContainer) {
      return create(creator, toPlayerShopType(shopType), shopObjectType, spawnLocation, targetedBlockFace, shopContainer);
   }

   public static PlayerShopCreationData create(Player creator, PlayerShopType<?> shopType, ShopObjectType<?> shopObjectType, @Nullable Location spawnLocation, @Nullable BlockFace targetedBlockFace, Block shopContainer) {
      return new PlayerShopCreationData(creator, shopType, shopObjectType, spawnLocation, targetedBlockFace, shopContainer);
   }

   protected PlayerShopCreationData(Player creator, ShopType<?> shopType, ShopObjectType<?> shopObjectType, @Nullable Location spawnLocation, @Nullable BlockFace targetedBlockFace, Block shopContainer) {
      this(creator, toPlayerShopType(shopType), shopObjectType, spawnLocation, targetedBlockFace, shopContainer);
   }

   protected PlayerShopCreationData(Player creator, PlayerShopType<?> shopType, ShopObjectType<?> shopObjectType, @Nullable Location spawnLocation, @Nullable BlockFace targetedBlockFace, Block shopContainer) {
      super(creator, shopType, shopObjectType, spawnLocation, targetedBlockFace);
      Preconditions.checkArgument(!(shopObjectType instanceof VirtualShopObjectType), "Virtual player shops are not yet supported!");
      Preconditions.checkNotNull(shopContainer, "shopContainer is null");
      if (spawnLocation != null) {
         Preconditions.checkArgument(shopContainer.getWorld().equals(spawnLocation.getWorld()), "The shop container is located in a different world than the spawn location!");
      }

      Preconditions.checkNotNull(creator, "creator is null");
      this.shopContainer = shopContainer;
   }

   /** @deprecated */
   @Deprecated
   public Block getShopChest() {
      return this.getShopContainer();
   }

   public Block getShopContainer() {
      return this.shopContainer;
   }
}
