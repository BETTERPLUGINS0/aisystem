package com.nisovin.shopkeepers.shopobjects;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopobjects.ShopObjectType;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopkeeper.registry.ShopObjectRegistry;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.types.AbstractSelectableType;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AbstractShopObjectType<T extends AbstractShopObject> extends AbstractSelectableType implements ShopObjectType<T> {
   private final Class<T> shopObjectClass;

   protected AbstractShopObjectType(String identifier, List<? extends String> aliases, @Nullable String permission, Class<T> shopObjectClass) {
      super(identifier, aliases, permission);
      Validate.notNull(shopObjectClass, (String)"shopObjectClass is null");
      this.shopObjectClass = shopObjectClass;
   }

   public final Class<T> getShopObjectClass() {
      return this.shopObjectClass;
   }

   protected void onSelect(Player player) {
      TextUtils.sendMessage(player, (Text)Messages.selectedShopObjectType, (Object[])("type", this.getDisplayName()));
   }

   public abstract boolean mustBeSpawned();

   public boolean mustDespawnDuringWorldSave() {
      return this.mustBeSpawned();
   }

   public final boolean isValidSpawnLocation(@Nullable Location spawnLocation, @Nullable BlockFace attachedBlockFace) {
      return this.validateSpawnLocation((Player)null, spawnLocation, attachedBlockFace);
   }

   public boolean validateSpawnLocation(@Nullable Player creator, @Nullable Location spawnLocation, @Nullable BlockFace attachedBlockFace) {
      if (spawnLocation != null && spawnLocation.isWorldLoaded()) {
         return true;
      } else {
         if (creator != null) {
            TextUtils.sendMessage(creator, (Text)Messages.missingSpawnLocation);
         }

         return false;
      }
   }

   @NonNull
   public abstract T createObject(AbstractShopkeeper var1, @Nullable ShopCreationData var2);

   @Nullable
   protected final AbstractShopkeeper getShopkeeperByObjectId(Object objectId) {
      ShopObjectRegistry shopObjectRegistry = SKShopkeepersPlugin.getInstance().getShopkeeperRegistry().getShopObjectRegistry();
      AbstractShopkeeper shopkeeper = shopObjectRegistry.getShopkeeperByObjectId(objectId);
      return shopkeeper != null && shopkeeper.getShopObject().getType() == this ? shopkeeper : null;
   }
}
