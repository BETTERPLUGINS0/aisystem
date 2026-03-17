package advancedplugins.pm2.cv.api.vehicle.configuration;

import advancedplugins.pm2.cv.api.enums.EnumExitShortcut;
import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import advancedplugins.pm2.cv.api.interfaces.IDeyed;
import advancedplugins.pm2.cv.api.interfaces.Named;
import advancedplugins.pm2.cv.api.item.ItemConfiguration;
import advancedplugins.pm2.cv.api.registry.Registries;
import advancedplugins.pm2.cv.api.upgrade.UpgradeConfiguration;
import advancedplugins.pm2.cv.api.util.ConfigurationUtil;
import advancedplugins.pm2.cv.api.util.Constants;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.VehicleEffectConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.VehicleModelConfiguration;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VehicleConfiguration implements IDeyed, Named, ConfigurationSectionWritable {
   @NotNull
   private final String id;
   @NotNull
   private final String name;
   @NotNull
   private final EnumExitShortcut operatorExitShortcut;
   @NotNull
   private final EnumExitShortcut passengerExitShortcut;
   @NotNull
   private final VehicleModelConfiguration model;
   @Nullable
   private final ItemConfiguration pickupItem;
   @Nullable
   private final ItemConfiguration adminPickupItem;
   @NotNull
   private final VehiclePhysicsConfiguration physics;
   @NotNull
   private final VehicleFuelConfiguration fuel;
   @NotNull
   private final VehicleControllersConfiguration controllers;
   @NotNull
   private final VehicleHealthConfiguration health;
   @NotNull
   private final VehicleDamageConfiguration damage;
   private boolean leaderboard;
   @Nullable
   private final VehicleEffectConfiguration damageEffect;
   @Nullable
   private final VehicleEffectConfiguration destroyEffect;
   private boolean blockBenchPartsLoaded;
   private boolean blockBenchAnimationsLoaded;
   @NotNull
   private final VehicleStorageSlotsConfiguration storage;
   @Nullable
   private final UpgradeConfiguration upgrade;
   @NotNull
   private final VehiclePlacementConfiguration placement;
   @Nullable
   private final VehicleRepairConfiguration repair;

   public static VehicleConfiguration load(@NotNull ConfigurationSection var0) {
      String var1 = IDeyed.loadId(var0);
      String var2 = Named.loadName(var0, var1);
      EnumExitShortcut var3 = (EnumExitShortcut)ConfigurationUtil.loadEnum(EnumExitShortcut.class, var0, Constants.Key.OPERATOR_EXIT_SHORTCUT);
      EnumExitShortcut var4 = (EnumExitShortcut)ConfigurationUtil.loadEnum(EnumExitShortcut.class, var0, Constants.Key.PASSENGER_EXIT_SHORTCUT);
      if (var3 == null) {
         throw new InvalidConfigurationException("invalid exit shortcut");
      } else {
         if (var4 == null) {
            var4 = var3;
         }

         VehicleStorageSlotsConfiguration var5 = VehicleStorageSlotsConfiguration.load(var0);
         String var6 = var0.getString("model");
         if (StringUtils.isBlank(var6)) {
            throw new InvalidConfigurationException("invalid model id");
         } else {
            VehicleModelConfiguration var7 = (VehicleModelConfiguration)Registries.getRegistry(VehicleModelConfiguration.class).get(IDeyed.idCheck(var6.toLowerCase()));
            if (var7 == null) {
               throw new InvalidConfigurationException("the model '" + var6 + "' is unknown");
            } else {
               boolean var8 = var0.getBoolean("leaderboard", false);
               String var9 = var0.getString("pickup-item");
               ItemConfiguration var10 = var9 != null ? (ItemConfiguration)Registries.getRegistry(ItemConfiguration.class).get(var9) : null;
               String var11 = var0.getString("admin-pickup-item");
               ItemConfiguration var12 = var11 != null ? (ItemConfiguration)Registries.getRegistry(ItemConfiguration.class).get(var11) : null;
               ConfigurationSection var13 = var0.getConfigurationSection("physics");
               VehiclePhysicsConfiguration var14;
               if (var13 != null) {
                  var14 = VehiclePhysicsConfiguration.load(var13);
               } else {
                  var14 = VehiclePhysicsConfiguration.DEFAULTS;
               }

               ConfigurationSection var15 = var0.getConfigurationSection("fuel");
               VehicleFuelConfiguration var16;
               if (var15 != null) {
                  var16 = VehicleFuelConfiguration.load(var15);
               } else {
                  var16 = VehicleFuelConfiguration.DEFAULTS;
               }

               ConfigurationSection var17 = var0.getConfigurationSection("controllers");
               VehicleControllersConfiguration var18;
               if (var17 != null) {
                  var18 = VehicleControllersConfiguration.load(var17);
               } else {
                  var18 = VehicleControllersConfiguration.EMPTY;
               }

               ConfigurationSection var19 = var0.getConfigurationSection("health");
               VehicleHealthConfiguration var20 = var19 != null ? VehicleHealthConfiguration.load(var19) : null;
               ConfigurationSection var21 = var0.getConfigurationSection("damage");
               VehicleDamageConfiguration var22 = var21 != null ? VehicleDamageConfiguration.load(var21) : null;
               ConfigurationSection var23 = var0.getConfigurationSection(Constants.Key.DAMAGE_EFFECT);
               ConfigurationSection var24 = var0.getConfigurationSection(Constants.Key.DESTROY_EFFECT);
               VehicleEffectConfiguration var25 = var23 != null ? VehicleEffectConfiguration.load(var23) : null;
               VehicleEffectConfiguration var26 = var24 != null ? VehicleEffectConfiguration.load(var24) : null;
               String var27 = var0.getString("upgrade");
               UpgradeConfiguration var28 = var27 != null ? (UpgradeConfiguration)Registries.getRegistry(UpgradeConfiguration.class).get(IDeyed.idCheck(var27.toLowerCase())) : null;
               if (var27 != null && var28 == null) {
                  throw new InvalidConfigurationException("the upgrade '" + var27 + "' is unknown");
               } else {
                  ConfigurationSection var29 = var0.getConfigurationSection("placement");
                  VehiclePlacementConfiguration var30 = var29 != null ? VehiclePlacementConfiguration.load(var29) : VehiclePlacementConfiguration.DEFAULTS;
                  ConfigurationSection var31 = var0.getConfigurationSection("repair");
                  VehicleRepairConfiguration var32 = var31 != null ? VehicleRepairConfiguration.load(var31) : null;
                  return new VehicleConfiguration(var1, var2, var3, var4, var7, var10, var12, var14, var16, var18, var20, var22, var25, var26, var8, var5, var28, var30, var32);
               }
            }
         }
      }
   }

   public VehicleConfiguration(@NotNull String var1, @NotNull String var2, @NotNull EnumExitShortcut var3, @NotNull EnumExitShortcut var4, @NotNull VehicleModelConfiguration var5, @Nullable ItemConfiguration var6, @Nullable ItemConfiguration var7, @Nullable VehiclePhysicsConfiguration var8, @Nullable VehicleFuelConfiguration var9, @Nullable VehicleControllersConfiguration var10, @Nullable VehicleHealthConfiguration var11, @Nullable VehicleDamageConfiguration var12, @Nullable VehicleEffectConfiguration var13, @Nullable VehicleEffectConfiguration var14, boolean var15, @NotNull VehicleStorageSlotsConfiguration var16, @Nullable UpgradeConfiguration var17, @NotNull VehiclePlacementConfiguration var18, @Nullable VehicleRepairConfiguration var19) {
      this.id = IDeyed.idCheck(var1.toLowerCase());
      this.name = Named.nameCheck(var2);
      this.operatorExitShortcut = var3;
      this.passengerExitShortcut = var4;
      this.model = var5;
      this.pickupItem = var6;
      this.adminPickupItem = var7;
      this.physics = var8 != null ? var8 : VehiclePhysicsConfiguration.DEFAULTS;
      this.fuel = var9 != null ? var9 : VehicleFuelConfiguration.DEFAULTS;
      this.controllers = var10 != null ? var10 : VehicleControllersConfiguration.EMPTY;
      this.health = var11 != null ? var11 : VehicleHealthConfiguration.DEFAULTS;
      this.damage = var12 != null ? var12 : VehicleDamageConfiguration.EMPTY;
      this.damageEffect = var13;
      this.destroyEffect = var14;
      this.storage = var16;
      this.upgrade = var17;
      this.blockBenchPartsLoaded = false;
      this.blockBenchAnimationsLoaded = false;
      this.leaderboard = var15;
      this.placement = var18;
      this.repair = var19;
   }

   @NotNull
   public String getId() {
      return this.id;
   }

   @NotNull
   public String getName() {
      return this.name;
   }

   @NotNull
   public EnumExitShortcut getOperatorExitShortcut() {
      return this.operatorExitShortcut;
   }

   @NotNull
   public EnumExitShortcut getPassengerExitShortcut() {
      return this.passengerExitShortcut;
   }

   @NotNull
   public VehicleModelConfiguration model() {
      return this.model;
   }

   @Nullable
   public ItemConfiguration pickupItem() {
      return this.pickupItem;
   }

   public boolean hasPickupItem() {
      return this.pickupItem != null;
   }

   @Nullable
   public ItemConfiguration adminPickupItem() {
      return this.adminPickupItem;
   }

   public boolean hasAdminPickupItem() {
      return this.adminPickupItem != null;
   }

   @NotNull
   public VehiclePhysicsConfiguration physics() {
      return this.physics;
   }

   @NotNull
   public VehicleFuelConfiguration fuel() {
      return this.fuel;
   }

   @NotNull
   public VehicleControllersConfiguration controllers() {
      return this.controllers;
   }

   @NotNull
   public VehicleHealthConfiguration health() {
      return this.health;
   }

   @NotNull
   public VehicleDamageConfiguration damage() {
      return this.damage;
   }

   @Nullable
   public VehicleEffectConfiguration damageEffect() {
      return this.damageEffect;
   }

   @Nullable
   public VehicleEffectConfiguration destroyEffect() {
      return this.destroyEffect;
   }

   @NotNull
   public VehicleStorageSlotsConfiguration storage() {
      return this.storage;
   }

   public void write(@NotNull ConfigurationSection var1) {
      IDeyed.writeId((IDeyed)this, var1);
      Named.writeName(this, var1);
      ConfigurationUtil.writeEnum(this.operatorExitShortcut, var1, Constants.Key.OPERATOR_EXIT_SHORTCUT);
      ConfigurationUtil.writeEnum(this.passengerExitShortcut, var1, Constants.Key.PASSENGER_EXIT_SHORTCUT);
      var1.set("model", this.model.getId());
      if (this.pickupItem != null) {
         var1.set("pickup-item", this.pickupItem.getId());
      }

      if (this.adminPickupItem != null) {
         var1.set("admin-pickup-item", this.adminPickupItem.getId());
      }

      this.physics.write(var1.createSection("physics"));
      this.fuel.write(var1.createSection("fuel"));
      this.controllers.write(var1.createSection("controllers"));
      this.storage.write(var1.createSection("item-storage-slots"));
      this.health.write(var1.createSection("health"));
      this.damage.write(var1.createSection("damage"));
      var1.set("leaderboard", this.leaderboard);
      ConfigurationSection var2 = var1.createSection("effects");
      if (this.damageEffect != null) {
         this.damageEffect.write(var2.createSection("damage"));
      }

      if (this.destroyEffect != null) {
         this.destroyEffect.write(var2.createSection("destroy"));
      }

   }

   public static VehicleConfiguration.VehicleConfigurationBuilder builder() {
      return new VehicleConfiguration.VehicleConfigurationBuilder();
   }

   @NotNull
   public VehicleModelConfiguration getModel() {
      return this.model;
   }

   @Nullable
   public ItemConfiguration getPickupItem() {
      return this.pickupItem;
   }

   @Nullable
   public ItemConfiguration getAdminPickupItem() {
      return this.adminPickupItem;
   }

   @NotNull
   public VehiclePhysicsConfiguration getPhysics() {
      return this.physics;
   }

   @NotNull
   public VehicleFuelConfiguration getFuel() {
      return this.fuel;
   }

   @NotNull
   public VehicleControllersConfiguration getControllers() {
      return this.controllers;
   }

   @NotNull
   public VehicleHealthConfiguration getHealth() {
      return this.health;
   }

   @NotNull
   public VehicleDamageConfiguration getDamage() {
      return this.damage;
   }

   @Nullable
   public VehicleEffectConfiguration getDamageEffect() {
      return this.damageEffect;
   }

   @Nullable
   public VehicleEffectConfiguration getDestroyEffect() {
      return this.destroyEffect;
   }

   @NotNull
   public VehicleStorageSlotsConfiguration getStorage() {
      return this.storage;
   }

   @Nullable
   public UpgradeConfiguration getUpgrade() {
      return this.upgrade;
   }

   @NotNull
   public VehiclePlacementConfiguration getPlacement() {
      return this.placement;
   }

   @Nullable
   public VehicleRepairConfiguration getRepair() {
      return this.repair;
   }

   public boolean isLeaderboard() {
      return this.leaderboard;
   }

   public void setLeaderboard(boolean var1) {
      this.leaderboard = var1;
   }

   public void setBlockBenchPartsLoaded(boolean var1) {
      this.blockBenchPartsLoaded = var1;
   }

   public void setBlockBenchAnimationsLoaded(boolean var1) {
      this.blockBenchAnimationsLoaded = var1;
   }

   public boolean isBlockBenchPartsLoaded() {
      return this.blockBenchPartsLoaded;
   }

   public boolean isBlockBenchAnimationsLoaded() {
      return this.blockBenchAnimationsLoaded;
   }

   public static class VehicleConfigurationBuilder {
      private String id;
      private String name;
      private EnumExitShortcut operatorExitShortcut;
      private EnumExitShortcut passengerExitShortcut;
      private VehicleModelConfiguration model;
      private ItemConfiguration pickupItem;
      private ItemConfiguration adminPickupItem;
      private VehiclePhysicsConfiguration physics;
      private VehicleFuelConfiguration fuel;
      private VehicleControllersConfiguration controllers;
      private VehicleHealthConfiguration health;
      private VehicleDamageConfiguration damage;
      private VehicleEffectConfiguration damageEffect;
      private VehicleEffectConfiguration destroyEffect;
      private boolean leaderboard;
      private VehicleStorageSlotsConfiguration storage;
      private UpgradeConfiguration upgrade;
      private VehiclePlacementConfiguration placement;
      private VehicleRepairConfiguration repair;

      VehicleConfigurationBuilder() {
      }

      public VehicleConfiguration.VehicleConfigurationBuilder id(@NotNull String var1) {
         this.id = var1;
         return this;
      }

      public VehicleConfiguration.VehicleConfigurationBuilder name(@NotNull String var1) {
         this.name = var1;
         return this;
      }

      public VehicleConfiguration.VehicleConfigurationBuilder operatorExitShortcut(@NotNull EnumExitShortcut var1) {
         this.operatorExitShortcut = var1;
         return this;
      }

      public VehicleConfiguration.VehicleConfigurationBuilder passengerExitShortcut(@NotNull EnumExitShortcut var1) {
         this.passengerExitShortcut = var1;
         return this;
      }

      public VehicleConfiguration.VehicleConfigurationBuilder model(@NotNull VehicleModelConfiguration var1) {
         this.model = var1;
         return this;
      }

      public VehicleConfiguration.VehicleConfigurationBuilder pickupItem(@Nullable ItemConfiguration var1) {
         this.pickupItem = var1;
         return this;
      }

      public VehicleConfiguration.VehicleConfigurationBuilder adminPickupItem(@Nullable ItemConfiguration var1) {
         this.adminPickupItem = var1;
         return this;
      }

      public VehicleConfiguration.VehicleConfigurationBuilder physics(@Nullable VehiclePhysicsConfiguration var1) {
         this.physics = var1;
         return this;
      }

      public VehicleConfiguration.VehicleConfigurationBuilder fuel(@Nullable VehicleFuelConfiguration var1) {
         this.fuel = var1;
         return this;
      }

      public VehicleConfiguration.VehicleConfigurationBuilder controllers(@Nullable VehicleControllersConfiguration var1) {
         this.controllers = var1;
         return this;
      }

      public VehicleConfiguration.VehicleConfigurationBuilder health(@Nullable VehicleHealthConfiguration var1) {
         this.health = var1;
         return this;
      }

      public VehicleConfiguration.VehicleConfigurationBuilder damage(@Nullable VehicleDamageConfiguration var1) {
         this.damage = var1;
         return this;
      }

      public VehicleConfiguration.VehicleConfigurationBuilder damageEffect(@Nullable VehicleEffectConfiguration var1) {
         this.damageEffect = var1;
         return this;
      }

      public VehicleConfiguration.VehicleConfigurationBuilder destroyEffect(@Nullable VehicleEffectConfiguration var1) {
         this.destroyEffect = var1;
         return this;
      }

      public VehicleConfiguration.VehicleConfigurationBuilder leaderboard(boolean var1) {
         this.leaderboard = var1;
         return this;
      }

      public VehicleConfiguration.VehicleConfigurationBuilder storage(@NotNull VehicleStorageSlotsConfiguration var1) {
         this.storage = var1;
         return this;
      }

      public VehicleConfiguration.VehicleConfigurationBuilder upgrade(@Nullable UpgradeConfiguration var1) {
         this.upgrade = var1;
         return this;
      }

      public VehicleConfiguration.VehicleConfigurationBuilder placement(@NotNull VehiclePlacementConfiguration var1) {
         this.placement = var1;
         return this;
      }

      public VehicleConfiguration.VehicleConfigurationBuilder repair(@Nullable VehicleRepairConfiguration var1) {
         this.repair = var1;
         return this;
      }

      public VehicleConfiguration build() {
         return new VehicleConfiguration(this.id, this.name, this.operatorExitShortcut, this.passengerExitShortcut, this.model, this.pickupItem, this.adminPickupItem, this.physics, this.fuel, this.controllers, this.health, this.damage, this.damageEffect, this.destroyEffect, this.leaderboard, this.storage, this.upgrade, this.placement, this.repair);
      }

      public String toString() {
         String var10000 = this.id;
         return "VehicleConfiguration.VehicleConfigurationBuilder(id=" + var10000 + ", name=" + this.name + ", operatorExitShortcut=" + String.valueOf(this.operatorExitShortcut) + ", passengerExitShortcut=" + String.valueOf(this.passengerExitShortcut) + ", model=" + String.valueOf(this.model) + ", pickupItem=" + String.valueOf(this.pickupItem) + ", adminPickupItem=" + String.valueOf(this.adminPickupItem) + ", physics=" + String.valueOf(this.physics) + ", fuel=" + String.valueOf(this.fuel) + ", controllers=" + String.valueOf(this.controllers) + ", health=" + String.valueOf(this.health) + ", damage=" + String.valueOf(this.damage) + ", damageEffect=" + String.valueOf(this.damageEffect) + ", destroyEffect=" + String.valueOf(this.destroyEffect) + ", leaderboard=" + this.leaderboard + ", storage=" + String.valueOf(this.storage) + ", upgrade=" + String.valueOf(this.upgrade) + ", placement=" + String.valueOf(this.placement) + ", repair=" + String.valueOf(this.repair) + ")";
      }
   }
}
