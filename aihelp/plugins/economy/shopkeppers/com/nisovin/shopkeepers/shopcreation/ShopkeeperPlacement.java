package com.nisovin.shopkeepers.shopcreation;

import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperRegistry;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopType;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopobjects.AbstractShopObjectType;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.BlockFaceUtils;
import com.nisovin.shopkeepers.util.bukkit.LocationUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ShopkeeperPlacement {
   private final ShopkeeperRegistry shopkeeperRegistry;

   public ShopkeeperPlacement(ShopkeeperRegistry shopkeeperRegistry) {
      Validate.notNull(shopkeeperRegistry, (String)"shopkeeperRegistry is null");
      this.shopkeeperRegistry = shopkeeperRegistry;
   }

   public Location determineSpawnLocation(Player player, Block targetBlock, BlockFace targetBlockFace) {
      Validate.notNull(player, (String)"player is null");
      Validate.notNull(targetBlock, (String)"targetBlock is null");
      Validate.notNull(targetBlockFace, (String)"targetBlockFace is null");
      Block spawnBlock;
      if (targetBlock.isPassable() && !targetBlock.isLiquid()) {
         spawnBlock = targetBlock;
      } else {
         spawnBlock = targetBlock.getRelative(targetBlockFace);
      }

      Location spawnLocation = LocationUtils.getBlockCenterLocation(spawnBlock);
      if (targetBlockFace.getModY() == 0 && targetBlockFace != BlockFace.SELF) {
         spawnLocation.setYaw(BlockFaceUtils.getYaw(targetBlockFace));
      } else {
         spawnLocation.setDirection(player.getEyeLocation().subtract(spawnLocation).toVector());
      }

      return spawnLocation;
   }

   public boolean validateSpawnLocation(@Nullable Player player, AbstractShopType<?> shopType, AbstractShopObjectType<?> shopObjectType, @Nullable Location spawnLocation, @Nullable BlockFace blockFace, @Nullable ShopCreationData shopCreationData, @Nullable AbstractShopkeeper shopkeeper) {
      if (!shopObjectType.validateSpawnLocation(player, spawnLocation, blockFace)) {
         return false;
      } else if (spawnLocation != null && !this.shopkeeperRegistry.getShopkeepersAtLocation(spawnLocation).isEmpty()) {
         if (player != null) {
            TextUtils.sendMessage(player, (Text)Messages.locationAlreadyInUse);
         }

         return false;
      } else {
         boolean isSpawnLocationValid = shopType.validateSpawnLocation(player, spawnLocation, blockFace, shopCreationData, shopkeeper);
         return isSpawnLocationValid;
      }
   }
}
