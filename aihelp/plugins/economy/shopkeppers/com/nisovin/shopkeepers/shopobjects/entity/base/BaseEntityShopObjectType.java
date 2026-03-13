package com.nisovin.shopkeepers.shopobjects.entity.base;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopobjects.entity.AbstractEntityShopObjectType;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.BlockFaceUtils;
import com.nisovin.shopkeepers.util.bukkit.EntityUtils;
import com.nisovin.shopkeepers.util.bukkit.PermissionUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class BaseEntityShopObjectType<T extends BaseEntityShopObject<?>> extends AbstractEntityShopObjectType<T> {
   private static final Map<? extends EntityType, ? extends List<? extends String>> ALIASES;
   private static final String PERMISSION_PREFIX = "shopkeeper.entity.";
   private static final String PERMISSION_ALL_ENTITY_TYPES = "shopkeeper.entity.*";
   protected final BaseEntityShopObjectCreationContext shopCreationContext;
   protected final EntityType entityType;
   private final BaseEntityShopObjectType.ShopObjectConstructor<T> shopObjectConstructor;

   protected static String getIdentifier(EntityType entityType) {
      assert entityType != null;

      return StringUtils.normalize(entityType.name());
   }

   private static List<? extends String> prepareAliases(List<? extends String> aliases) {
      return Collections.unmodifiableList(StringUtils.normalize(aliases));
   }

   protected static List<? extends String> getAliasesFor(EntityType entityType) {
      Validate.notNull(entityType, (String)"entityType is null");
      List<? extends String> aliases = (List)ALIASES.get(entityType);
      return aliases != null ? aliases : Collections.emptyList();
   }

   protected static String getPermission(EntityType entityType) {
      assert entityType != null;

      String typeName = entityType.name().toLowerCase(Locale.ROOT);
      String permission = "shopkeeper.entity." + typeName;
      return permission;
   }

   protected BaseEntityShopObjectType(BaseEntityShopObjectCreationContext shopCreationContext, EntityType entityType, Class<T> shopObjectType, BaseEntityShopObjectType.ShopObjectConstructor<T> shopObjectConstructor) {
      this(shopCreationContext, entityType, getIdentifier(entityType), getAliasesFor(entityType), getPermission(entityType), shopObjectType, shopObjectConstructor);
   }

   protected BaseEntityShopObjectType(BaseEntityShopObjectCreationContext shopCreationContext, EntityType entityType, String identifier, List<? extends String> aliases, String permission, Class<T> shopObjectType, BaseEntityShopObjectType.ShopObjectConstructor<T> shopObjectConstructor) {
      super(identifier, aliases, permission, shopObjectType);
      Validate.notNull(shopCreationContext, (String)"shopCreationContext is null");
      Validate.isTrue(entityType.isSpawnable(), "entityType is not spawnable");
      Validate.notNull(shopObjectConstructor, (String)"shopObjectConstructor is null");
      this.shopCreationContext = shopCreationContext;
      this.entityType = entityType;
      this.shopObjectConstructor = shopObjectConstructor;
   }

   public final EntityType getEntityType() {
      return this.entityType;
   }

   public abstract boolean isEnabled();

   public boolean hasPermission(Player player) {
      return PermissionUtils.hasPermission(player, "shopkeeper.entity.*") || super.hasPermission(player);
   }

   private Permission createPermission() {
      String permission = (String)Unsafe.assertNonNull(this.getPermission());
      String description = "Create shopkeepers of the specific entity type";
      return new Permission(permission, description, PermissionDefault.FALSE);
   }

   public void registerPermission() {
      String permission = this.getPermission();
      if (permission != null) {
         PermissionUtils.registerPermission(permission, (node) -> {
            return this.createPermission();
         });
      }
   }

   public String getDisplayName() {
      return StringUtils.replaceArguments(Messages.shopObjectTypeEntity, "type", StringUtils.normalize(this.entityType.name()));
   }

   public boolean mustBeSpawned() {
      return true;
   }

   public boolean mustDespawnDuringWorldSave() {
      return false;
   }

   protected boolean isDownValidAttachedBlockFace() {
      switch(this.entityType) {
      case SHULKER:
         return true;
      default:
         return false;
      }
   }

   public boolean validateSpawnLocation(@Nullable Player creator, @Nullable Location spawnLocation, @Nullable BlockFace attachedBlockFace) {
      if (!super.validateSpawnLocation(creator, spawnLocation, attachedBlockFace)) {
         return false;
      } else {
         assert spawnLocation != null;

         World world = (World)Unsafe.assertNonNull(spawnLocation.getWorld());
         if (EntityUtils.isRemovedOnPeacefulDifficulty(this.entityType) && world.getDifficulty() == Difficulty.PEACEFUL) {
            if (creator != null) {
               TextUtils.sendMessage(creator, (Text)Messages.mobCannotSpawnOnPeacefulDifficulty);
            }

            return false;
         } else if (this.entityType == EntityType.END_CRYSTAL && !Settings.allowEndCrystalShopsInTheEnd && world.getEnvironment() == Environment.THE_END) {
            if (creator != null) {
               TextUtils.sendMessage(creator, (Text)Messages.endCrystalDisabledInTheEnd);
            }

            return false;
         } else if (attachedBlockFace == BlockFace.DOWN && !this.isDownValidAttachedBlockFace() || attachedBlockFace != null && !BlockFaceUtils.isBlockSide(attachedBlockFace)) {
            if (creator != null) {
               TextUtils.sendMessage(creator, (Text)Messages.invalidSpawnBlockFace);
            }

            return false;
         } else {
            Block spawnBlock = spawnLocation.getBlock();
            if (!spawnBlock.isPassable()) {
               if (creator != null) {
                  TextUtils.sendMessage(creator, (Text)Messages.spawnBlockNotEmpty);
               }

               return false;
            } else {
               if (!EntityUtils.canFly(this.entityType) && this.entityType != EntityType.SHULKER) {
                  Location standingLocation = EntityUtils.getStandingLocation(this.entityType, spawnBlock);
                  if (standingLocation == null) {
                     if (creator != null) {
                        TextUtils.sendMessage(creator, (Text)Messages.cannotSpawnMidair);
                     }

                     return false;
                  }
               }

               return true;
            }
         }
      }
   }

   public final T createObject(AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      T shopObject = this.shopObjectConstructor.create(this.shopCreationContext, this, shopkeeper, creationData);
      Validate.State.notNull(shopObject, (Supplier)(() -> {
         return "BaseEntityShopObjectType for entity type '" + String.valueOf(this.entityType) + "' created null shop object!";
      }));

      assert shopObject != null;

      return shopObject;
   }

   static {
      Map<EntityType, List<? extends String>> aliases = new HashMap();
      aliases.put(EntityType.MOOSHROOM, prepareAliases(Arrays.asList("mooshroom", "mushroom-cow")));
      aliases.put(EntityType.SNOW_GOLEM, prepareAliases(Arrays.asList("snow-golem", "snowman")));
      ALIASES = Collections.unmodifiableMap(aliases);
   }

   @FunctionalInterface
   public interface ShopObjectConstructor<T extends BaseEntityShopObject<?>> {
      @NonNull
      T create(BaseEntityShopObjectCreationContext var1, BaseEntityShopObjectType<T> var2, AbstractShopkeeper var3, @Nullable ShopCreationData var4);
   }
}
