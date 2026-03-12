package com.nisovin.shopkeepers.shopkeeper.player;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.events.PlayerCreatePlayerShopkeeperEvent;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperRegistry;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopCreationData;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopType;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.container.ShopContainers;
import com.nisovin.shopkeepers.dependencies.towny.TownyDependency;
import com.nisovin.shopkeepers.dependencies.worldguard.WorldGuardDependency;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.playershops.PlayerShopsLimit;
import com.nisovin.shopkeepers.shopcreation.ContainerSelection;
import com.nisovin.shopkeepers.shopcreation.ShopkeeperCreation;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopType;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.BlockLocation;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.interaction.InteractionUtils;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AbstractPlayerShopType<T extends AbstractPlayerShopkeeper> extends AbstractShopType<T> implements PlayerShopType<T> {
   protected AbstractPlayerShopType(String identifier, List<? extends String> aliases, @Nullable String permission, Class<T> shopkeeperType) {
      super(identifier, aliases, permission, shopkeeperType);
   }

   protected void validateCreationData(ShopCreationData shopCreationData) {
      super.validateCreationData(shopCreationData);
      Validate.isTrue(shopCreationData instanceof PlayerShopCreationData, () -> {
         String var10000 = PlayerShopCreationData.class.getName();
         return "shopCreationData is not of type " + var10000 + ", but: " + shopCreationData.getClass().getName();
      });
   }

   protected boolean handleSpecificShopkeeperCreation(ShopCreationData shopCreationData) {
      assert shopCreationData instanceof PlayerShopCreationData;

      PlayerShopCreationData playerShopCreationData = (PlayerShopCreationData)shopCreationData;
      Player creator = (Player)Unsafe.assertNonNull(shopCreationData.getCreator());
      Block containerBlock = playerShopCreationData.getShopContainer();
      if (!ShopContainers.isSupportedContainer(containerBlock.getType())) {
         if (ItemUtils.isContainer(containerBlock.getType())) {
            TextUtils.sendMessage(creator, (Text)Messages.unsupportedContainer);
         } else {
            TextUtils.sendMessage(creator, (Text)Messages.invalidContainer);
         }

         return false;
      } else {
         ShopkeeperCreation shopkeeperCreation = SKShopkeepersPlugin.getInstance().getShopkeeperCreation();
         ContainerSelection containerSelection = shopkeeperCreation.getContainerSelection();
         if (!containerSelection.validateContainer(creator, containerBlock)) {
            return false;
         } else {
            int maxShopsLimit = PlayerShopsLimit.getMaxShopsLimit(creator);
            PlayerCreatePlayerShopkeeperEvent createEvent = new PlayerCreatePlayerShopkeeperEvent(shopCreationData, maxShopsLimit);
            Bukkit.getPluginManager().callEvent(createEvent);
            if (createEvent.isCancelled()) {
               Log.debug("PlayerShopkeeperCreateEvent was cancelled!");
               return false;
            } else {
               maxShopsLimit = createEvent.getMaxShopsLimit();
               if (maxShopsLimit != Integer.MAX_VALUE) {
                  ShopkeeperRegistry shopkeeperRegistry = SKShopkeepersPlugin.getInstance().getShopkeeperRegistry();
                  int count = shopkeeperRegistry.getPlayerShopkeepersByOwner(creator.getUniqueId()).size();
                  if (count >= maxShopsLimit) {
                     TextUtils.sendMessage(creator, (Text)Messages.tooManyShops);
                     return false;
                  }
               }

               return true;
            }
         }
      }
   }

   public boolean validateSpawnLocation(@Nullable Player player, @Nullable Location spawnLocation, @Nullable BlockFace blockFace, @Nullable ShopCreationData shopCreationData, @Nullable AbstractShopkeeper shopkeeper) {
      if (!super.validateSpawnLocation(player, spawnLocation, blockFace, shopCreationData, shopkeeper)) {
         return false;
      } else if (spawnLocation == null) {
         return true;
      } else {
         BlockLocation containerLocation = null;
         if (shopCreationData != null) {
            assert shopCreationData instanceof PlayerShopCreationData;

            PlayerShopCreationData playerShopCreationData = (PlayerShopCreationData)shopCreationData;
            containerLocation = BlockLocation.of(playerShopCreationData.getShopContainer());
         } else if (shopkeeper != null) {
            assert shopkeeper instanceof AbstractPlayerShopkeeper;

            AbstractPlayerShopkeeper playerShopkeeper = (AbstractPlayerShopkeeper)shopkeeper;
            containerLocation = playerShopkeeper.getContainerLocation();
         }

         if (containerLocation != null) {
            double maxContainerDistanceSq = (double)(Settings.maxContainerDistance * Settings.maxContainerDistance);
            if (containerLocation.getBlockCenterDistanceSquared(spawnLocation) > maxContainerDistanceSq) {
               if (player != null) {
                  TextUtils.sendMessage(player, (Text)Messages.containerTooFarAway);
               }

               return false;
            }
         }

         if (Settings.enableWorldGuardRestrictions && !WorldGuardDependency.isShopAllowed(player, spawnLocation)) {
            if (player != null) {
               TextUtils.sendMessage(player, (Text)Messages.restrictedArea);
            }

            return false;
         } else if (Settings.enableTownyRestrictions && !TownyDependency.isCommercialArea(spawnLocation)) {
            if (player != null) {
               TextUtils.sendMessage(player, (Text)Messages.restrictedArea);
            }

            return false;
         } else {
            if (Settings.checkSpawnLocationInteractionResult && player != null) {
               Block spawnLocationBlock = spawnLocation.getBlock();
               if (!InteractionUtils.checkBlockInteract(player, spawnLocationBlock, true)) {
                  TextUtils.sendMessage(player, (Text)Messages.restrictedArea);
                  return false;
               }
            }

            return true;
         }
      }
   }
}
