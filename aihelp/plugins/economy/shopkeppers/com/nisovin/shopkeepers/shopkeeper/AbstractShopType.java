package com.nisovin.shopkeepers.shopkeeper;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.events.PlayerCreateShopkeeperEvent;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopkeeper.ShopType;
import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperCreateException;
import com.nisovin.shopkeepers.api.shopobjects.ShopObjectType;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopcreation.ShopkeeperPlacement;
import com.nisovin.shopkeepers.shopkeeper.registry.SKShopkeeperRegistry;
import com.nisovin.shopkeepers.shopobjects.AbstractShopObject;
import com.nisovin.shopkeepers.shopobjects.AbstractShopObjectType;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.types.AbstractSelectableType;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AbstractShopType<T extends AbstractShopkeeper> extends AbstractSelectableType implements ShopType<T> {
   private final Class<T> shopkeeperClass;

   protected AbstractShopType(String identifier, List<? extends String> aliases, @Nullable String permission, Class<T> shopkeeperClass) {
      super(identifier, aliases, permission);
      Validate.notNull(shopkeeperClass, (String)"shopkeeperClass is null");
      this.shopkeeperClass = shopkeeperClass;
   }

   public final Class<T> getShopkeeperClass() {
      return this.shopkeeperClass;
   }

   protected void onSelect(Player player) {
      TextUtils.sendMessage(player, (Text)Messages.selectedShopType, (Object[])("type", this.getDisplayName(), "description", this.getDescription()));
   }

   protected Text getCreatedMessage() {
      Text text = Messages.shopkeeperCreated;
      text.setPlaceholderArguments("type", this.getDisplayName(), "description", this.getDescription(), "setupDesc", this.getSetupDescription());
      return text;
   }

   @NonNull
   private final T createShopkeeper() {
      T shopkeeper = this.createNewShopkeeper();
      if (Unsafe.cast(shopkeeper) == null) {
         throw new RuntimeException("ShopType '" + this.getClass().getName() + "' created null shopkeeper!");
      } else {
         String var10002;
         if (shopkeeper.getType() != this) {
            var10002 = this.getClass().getName();
            throw new RuntimeException("ShopType '" + var10002 + "' created a shopkeeper of a different type (expected: " + this.getIdentifier() + ", got: " + shopkeeper.getType().getIdentifier() + ")!");
         } else if (shopkeeper.getClass() != this.getShopkeeperClass()) {
            var10002 = this.getClass().getName();
            throw new RuntimeException("ShopType '" + var10002 + "' created a shopkeeper of unexpected class (expected: " + this.getShopkeeperClass().getName() + ", got: " + shopkeeper.getClass().getName() + ")!");
         } else if (shopkeeper.isInitialized()) {
            throw new RuntimeException("ShopType '" + this.getClass().getName() + "' created an already initialized shopkeeper!");
         } else {
            return shopkeeper;
         }
      }
   }

   @NonNull
   protected abstract T createNewShopkeeper();

   @NonNull
   public final T createShopkeeper(int id, ShopCreationData shopCreationData) throws ShopkeeperCreateException {
      T shopkeeper = this.createShopkeeper();

      assert shopkeeper != null;

      shopkeeper.initOnCreation(id, shopCreationData);
      return shopkeeper;
   }

   @NonNull
   public final T loadShopkeeper(ShopkeeperData shopkeeperData) throws InvalidDataException {
      T shopkeeper = this.createShopkeeper();

      assert shopkeeper != null;

      shopkeeper.initOnLoad(shopkeeperData);
      return shopkeeper;
   }

   protected void validateCreationData(ShopCreationData shopCreationData) {
      Validate.notNull(shopCreationData, (String)"shopCreationData is null");
      ShopType<?> shopType = shopCreationData.getShopType();
      Validate.isTrue(this == shopType, () -> {
         String var10000 = this.getClass().getName();
         return "ShopType of shopCreationData is not of type " + var10000 + ", but: " + shopType.getClass().getName();
      });
      ShopObjectType<?> shopObjectType = shopCreationData.getShopObjectType();
      Validate.isTrue(shopObjectType instanceof AbstractShopObjectType, () -> {
         String var10000 = AbstractShopObjectType.class.getSimpleName();
         return "ShopObjectType of shopCreationData is not of type " + var10000 + ", but: " + shopType.getClass().getName();
      });
   }

   @Nullable
   public T handleShopkeeperCreation(ShopCreationData shopCreationData) {
      this.validateCreationData(shopCreationData);
      SKShopkeeperRegistry shopkeeperRegistry = SKShopkeepersPlugin.getInstance().getShopkeeperRegistry();
      Player creator = shopCreationData.getCreator();
      creator = (Player)Validate.notNull(creator, (String)"Creator of shopCreationData is null");
      AbstractShopObjectType<?> shopObjectType = (AbstractShopObjectType)shopCreationData.getShopObjectType();
      if (!this.hasPermission(creator)) {
         TextUtils.sendMessage(creator, (Text)Messages.noPermission);
         return null;
      } else if (!this.isEnabled()) {
         TextUtils.sendMessage(creator, (Text)Messages.shopTypeDisabled, (Object[])("type", this.getIdentifier()));
         return null;
      } else if (!shopObjectType.hasPermission(creator)) {
         TextUtils.sendMessage(creator, (Text)Messages.noPermission);
         return null;
      } else if (!shopObjectType.isEnabled()) {
         TextUtils.sendMessage(creator, (Text)Messages.shopObjectTypeDisabled, (Object[])("type", shopObjectType.getIdentifier()));
         return null;
      } else {
         Location spawnLocation = shopCreationData.getSpawnLocation();
         BlockFace targetedBlockFace = shopCreationData.getTargetedBlockFace();
         ShopkeeperPlacement shopkeeperPlacement = SKShopkeepersPlugin.getInstance().getShopkeeperCreation().getShopkeeperPlacement();
         boolean isSpawnLocationValid = shopkeeperPlacement.validateSpawnLocation(creator, this, shopObjectType, spawnLocation, targetedBlockFace, shopCreationData, (AbstractShopkeeper)null);
         if (!isSpawnLocationValid) {
            return null;
         } else {
            try {
               if (!this.handleSpecificShopkeeperCreation(shopCreationData)) {
                  return null;
               } else {
                  T shopkeeper = (AbstractShopkeeper)Unsafe.castNonNull(shopkeeperRegistry.createShopkeeper(shopCreationData));
                  AbstractShopObject shopObject = shopkeeper.getShopObject();
                  if (!shopkeeper.isVirtual() && !shopObject.isActive()) {
                     Log.debug(shopkeeper.getLogPrefix() + "New shopkeeper failed to spawn -> Deleting.");
                     shopkeeper.delete((Player)null);
                     SKShopkeepersPlugin.getInstance().getShopkeeperStorage().saveIfDirty();
                     TextUtils.sendMessage(creator, (Text)Messages.cannotSpawn);
                     return null;
                  } else {
                     TextUtils.sendMessage(creator, (Text)this.getCreatedMessage());
                     shopkeeper.save();
                     return shopkeeper;
                  }
               }
            } catch (ShopkeeperCreateException var12) {
               String var10001 = String.valueOf(ChatColor.RED);
               TextUtils.sendMessage(creator, (String)(var10001 + "Shopkeeper creation failed: " + var12.getMessage()));
               return null;
            }
         }
      }
   }

   protected boolean handleSpecificShopkeeperCreation(ShopCreationData creationData) {
      PlayerCreateShopkeeperEvent createEvent = new PlayerCreateShopkeeperEvent(creationData);
      Bukkit.getPluginManager().callEvent(createEvent);
      if (createEvent.isCancelled()) {
         Log.debug("ShopkeeperCreateEvent was cancelled!");
         return false;
      } else {
         return true;
      }
   }

   public boolean validateSpawnLocation(@Nullable Player player, @Nullable Location spawnLocation, @Nullable BlockFace blockFace, @Nullable ShopCreationData shopCreationData, @Nullable AbstractShopkeeper shopkeeper) {
      if (shopCreationData != null) {
         Validate.isTrue(shopkeeper == null, "shopCreationData and shopkeeper cannot both be specified");
         this.validateCreationData(shopCreationData);
      } else if (shopkeeper != null) {
         Validate.isTrue(shopkeeper.getType() == this, "shopkeeper is of a different type");
      }

      return true;
   }
}
