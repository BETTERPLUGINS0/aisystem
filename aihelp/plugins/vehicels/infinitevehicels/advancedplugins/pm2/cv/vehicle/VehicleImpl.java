package advancedplugins.pm2.cv.vehicle;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.enums.EnumDamageType;
import advancedplugins.pm2.cv.api.enums.EnumSurface;
import advancedplugins.pm2.cv.api.event.VehicleMoveEvent;
import advancedplugins.pm2.cv.api.event.VehicleStateChangeEvent;
import advancedplugins.pm2.cv.api.registry.Registries;
import advancedplugins.pm2.cv.api.service.BlockInfoService;
import advancedplugins.pm2.cv.api.upgrade.Upgrade;
import advancedplugins.pm2.cv.api.upgrade.UpgradeConfiguration;
import advancedplugins.pm2.cv.api.upgrade.UpgradeTier;
import advancedplugins.pm2.cv.api.util.Run;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.api.vehicle.VehicleHitBox;
import advancedplugins.pm2.cv.api.vehicle.VehicleSeat;
import advancedplugins.pm2.cv.api.vehicle.VehicleState;
import advancedplugins.pm2.cv.api.vehicle.configuration.VehicleConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.VehicleDamageConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.VehiclePhysicsConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.VehicleStorageSlotsConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.VehicleEffectConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.VehicleHitBoxConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.VehicleModelConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.VehicleProjectileShooterConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.VehicleSeatConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.compound.CompoundModelConfiguration;
import advancedplugins.pm2.cv.api.vehicle.controller.VehicleController;
import advancedplugins.pm2.cv.api.vehicle.controller.VehicleControllerProperties;
import advancedplugins.pm2.cv.api.vehicle.input.PlayerInput;
import advancedplugins.pm2.cv.api.vehicle.input.PlayerSteerInput;
import advancedplugins.pm2.cv.api.vehicle.item.storage.VehicleItemStorage;
import advancedplugins.pm2.cv.api.vehicle.model.VehicleModel;
import advancedplugins.pm2.cv.damage.DamageHitbox;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.packet.PacketBatcher;
import advancedplugins.pm2.cv.util.InfiniteModelUtil;
import advancedplugins.pm2.cv.vehicle.model.compound.CompoundModel;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.logging.Logger;
import me.PM2.infinitevehicles.math.util.FastMath;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VehicleImpl implements Vehicle {
   final VehicleInteraction vehicleInteraction;
   @NotNull
   final VehicleSeatImpl mainSeat;
   @NotNull
   final Set<VehicleSeatImpl> seats = new THashSet();
   @NotNull
   final Set<VehicleProjectileShooterImpl> projectileShooters = new THashSet();
   private final VehicleHandlerImpl handler;
   private final PacketBatcher packetBatcher;
   private final BlockInfoService blockInfoService;
   @NotNull
   private final UUID uniqueId;
   @NotNull
   private final VehicleConfiguration configuration;
   @NotNull
   private final VehicleModelConfiguration modelConfiguration;
   @NotNull
   private final VehicleHitBox hitBox;
   private final List<VehicleItemStorage> storage = new LinkedList();
   @NotNull
   private final Set<VehicleController> controllers = Sets.newConcurrentHashSet();
   @NotNull
   private final Map<String, Integer> upgradeTiers;
   @Nullable
   DamageHitbox damageHitbox;
   @NotNull
   private World world;
   @NotNull
   private VehicleModel<?> model;
   @NotNull
   private VehicleState state;
   @Nullable
   private UUID ownerUniqueId;
   private boolean persistent = true;
   private VehicleStorageSlotsConfiguration storageSize;
   private volatile boolean standby;
   private boolean spawned;
   private boolean removed;
   private double x;
   private double y;
   private double z;
   private boolean locationDirty;
   private float rotation;
   private boolean rotationDirty;
   private float health;
   private float fuelLevel;
   private double momentumX;
   private double momentumY;
   private double momentumZ;
   private boolean onGround;
   private Double forceX = null;
   private Double forceY = null;
   private Double forceZ = null;
   private int climbing = 0;
   @NotNull
   private List<IJoint> blockBenchBones;
   private boolean keyed;
   private float targetRotation = -1.0F;

   public VehicleImpl(@NotNull VehicleHandlerImpl handler, @NotNull VehicleConfiguration configuration, @NotNull World world, double x, double y, double z, @Nullable UUID uniqueId, @Nullable UUID ownerUniqueId) {
      this.handler = var1;
      this.packetBatcher = new PacketBatcher();
      this.blockInfoService = InfiniteVehicles.getBlockInfoService();
      this.uniqueId = var10 != null ? var10 : UUID.randomUUID();
      this.ownerUniqueId = var11;
      this.blockBenchBones = new ArrayList();
      this.configuration = var2;
      this.modelConfiguration = (VehicleModelConfiguration)Objects.requireNonNull(var2.model(), "an invalid model was provided");
      this.world = var3;
      this.setLocation(var4, var6, var8);
      this.health = var2.health().getMaxHealth();
      this.state = VehicleState.IDLE;
      this.model = this.createModelInstance(var4, var6, var8);
      this.model.setState(this.state);
      VehicleSeatImpl var12 = null;
      Iterator var13 = this.model.getConfiguration().getSeats().iterator();

      while(var13.hasNext()) {
         VehicleSeatConfiguration var14 = (VehicleSeatConfiguration)var13.next();
         VehicleSeatImpl var15 = new VehicleSeatImpl(this, var14);
         this.seats.add(var15);
         if (var14.isMain()) {
            var12 = var15;
         }
      }

      if (this.getConfiguration().model().getModelID() != null) {
         Location var17 = new Location(var3, var4, var6, var8);
         InfiniteModelUtil.ModelFetcherResult var18 = InfiniteModelUtil.getGeneratedModel((String)Objects.requireNonNull(this.getConfiguration().model().getModelID()), var17);
         if (var18 == null) {
            throw new RuntimeException("model not found: " + this.getConfiguration().model().getModelID());
         }

         IVisualModel var21 = var18.model();
         ArrayList var16 = new ArrayList(var21.getJoints().values());
         var18.armorStand().setRemoved(true);
         var18.modelContainer().destroy();
         var18.model().destroy();
         this.blockBenchBones = var16;
      }

      if (var12 == null && (var12 = (VehicleSeatImpl)this.seats.iterator().next()) == null) {
         throw new IllegalArgumentException("couldn't determine main set");
      } else {
         this.mainSeat = var12;
         this.mainSeat.setMain(true);
         var13 = this.model.getConfiguration().getProjectileShooters().iterator();

         while(var13.hasNext()) {
            VehicleProjectileShooterConfiguration var19 = (VehicleProjectileShooterConfiguration)var13.next();
            VehicleProjectileShooterImpl var22 = new VehicleProjectileShooterImpl(this, var19);
            this.projectileShooters.add(var22);
         }

         var13 = var2.controllers().getEntries().entrySet().iterator();

         while(var13.hasNext()) {
            Entry var20 = (Entry)var13.next();
            VehicleController.Factory var23 = (VehicleController.Factory)Registries.getRegistry(VehicleController.Factory.class).get((String)var20.getKey());
            if (var23 != null) {
               this.addController(var23.createInstance(this, (VehicleControllerProperties)var20.getValue()));
            } else {
               Logger var10000 = InfiniteVehicles.getPlugin().getLogger();
               String var10001 = String.valueOf(ChatColor.RED);
               var10000.severe(var10001 + "was not able to create instance of controller '" + (String)var20.getKey() + "' as no factory was registered for it");
            }
         }

         this.hitBox = VehicleHitBox.of(this.model.getConfiguration().getHitBox());
         this.hitBox.setOrigin(var4, var6, var8);
         if (!var2.health().isDisabled()) {
            this.damageHitbox = this.createDamageHitbox(var3);
         }

         this.vehicleInteraction = new VehicleInteraction(this);
         this.upgradeTiers = new HashMap();
         this.keyed = false;
      }
   }

   public void setUpgradeTier(UUID playerUUID, String upgradeId, int tier) {
      this.upgradeTiers.put(var2, var3);
   }

   @NotNull
   public Map<String, Integer> getUpgradeTier(UUID playerUUID) {
      HashMap var2 = new HashMap(this.upgradeTiers);
      if (this.getUpgradeConfiguration() != null) {
         Iterator var3 = this.getUpgradeConfiguration().getUpgrades().iterator();

         while(true) {
            Upgrade var4;
            int var5;
            do {
               if (!var3.hasNext()) {
                  return var2;
               }

               var4 = (Upgrade)var3.next();
               var5 = -1;
            } while(var2.containsKey(var4.getId()));

            Iterator var6 = var4.getUpgradeTiers().values().iterator();

            while(var6.hasNext()) {
               UpgradeTier var7 = (UpgradeTier)var6.next();
               if (var5 == -1) {
                  var5 = var7.getTier();
               } else {
                  var5 = Math.min(var5, var7.getTier());
               }
            }

            if (var5 != -1) {
               var2.put(var4.getId(), var5 - 1);
            }
         }
      } else {
         return var2;
      }
   }

   public void tick() {
      try {
         this.controllers.forEach(VehicleController::tick);
      } catch (Throwable var9) {
         var9.printStackTrace();
      }

      this.model.tick();
      this.seats.forEach(VehicleSeatImpl::tick);
      VehiclePhysicsConfiguration var1 = this.configuration.physics();
      double var2 = var1.getGravityMaximum();
      double var4 = var1.getGravityAcceleration();
      if (var2 > 0.0D && var4 > 0.0D && this.momentumY > -var2) {
         if (this.momentumY - var4 > -var2) {
            this.momentumY -= var4;
         } else {
            this.momentumY = -var2;
         }
      }

      boolean var6 = false;
      if (this.momentumX != 0.0D || this.momentumY != 0.0D || this.momentumZ != 0.0D) {
         this.forceX = null;
         this.forceY = null;
         this.forceZ = null;
         this.climbing = 0;
         this.tickMomentumY();
         this.tickMomentumX();
         this.tickMomentumZ();
         if (this.climbing > 0) {
            var6 = true;
         }

         Location var7 = new Location(this.world, this.x, this.y, this.z);
         this.setLocation(this.forceX != null ? this.forceX : this.x + this.momentumX, (this.forceY != null ? this.forceY : this.y + this.momentumY) + (double)this.climbing, this.forceZ != null ? this.forceZ : this.z + this.momentumZ);
         if (this.isLocationDirty()) {
            Location var8 = new Location(this.world, this.x, this.y, this.z);
            (new VehicleMoveEvent(this, var7, var8)).callEvent();
         }
      }

      this.tickFriction();
      this.applyLocationRotation(var6);
   }

   private void tickMomentumY() {
      VehiclePhysicsConfiguration var1 = this.configuration.physics();
      boolean var2 = var1.isFloats();
      int var5;
      int var6;
      int var7;
      int var8;
      int var9;
      int var10;
      int var11;
      int var12;
      if (this.momentumY < 0.0D) {
         int var3 = (int)FastMath.floor(this.y);
         int var4 = (int)FastMath.floor(this.y + this.momentumY);
         var5 = this.hitBox.getBlockMinX();
         var6 = this.hitBox.getBlockMinZ();
         var7 = this.hitBox.getBlockMaxX();
         var8 = this.hitBox.getBlockMaxZ();
         if (var4 < var3) {
            label91:
            for(var9 = 1; var9 <= var3 - var4; ++var9) {
               for(var10 = var5; var10 <= var7; ++var10) {
                  for(var11 = var6; var11 <= var8; ++var11) {
                     var12 = var3 - var9;
                     EnumSurface var13 = this.blockInfoService.getSurfaceTypeAt(this.world, var10, var12, var11, false);
                     if ((this.blockInfoService.isCanStandOnSurfaceAt(this.world, var10, var12, var11) || var2 && var13.isLiquid()) && (!var2 || !var13.isLiquid() || !this.blockInfoService.getSurfaceTypeAt(this.world, var10, var12 + 1, var11, false).isLiquid())) {
                        this.momentumY = 0.0D;
                        this.forceY = (double)var12 + 1.0D;
                        this.onGround = true;
                        break label91;
                     }
                  }
               }
            }
         }
      } else if (this.momentumY > 0.0D) {
         double var15 = this.y + this.hitBox.getHeight();
         var5 = (int)FastMath.floor(var15);
         var6 = (int)FastMath.floor(var15 + this.momentumY);
         var7 = this.hitBox.getBlockMinX();
         var8 = this.hitBox.getBlockMinZ();
         var9 = this.hitBox.getBlockMaxX();
         var10 = this.hitBox.getBlockMaxZ();
         if (var6 > var5) {
            label61:
            for(var11 = 1; var11 <= var6 - var5; ++var11) {
               for(var12 = var7; var12 <= var9; ++var12) {
                  for(int var16 = var8; var16 <= var10; ++var16) {
                     int var14 = var5 + var11;
                     if (this.blockInfoService.isCanStandOnSurfaceAt(this.world, var12, var14, var16)) {
                        this.momentumY = 0.0D;
                        this.forceY = (double)var14 - this.hitBox.getHeight() - 0.1D;
                        break label61;
                     }
                  }
               }
            }
         }
      }

      if (this.momentumY != 0.0D && this.onGround) {
         this.onGround = false;
      }

   }

   private void tickMomentumX() {
      if (this.momentumX != 0.0D) {
         double var1 = this.momentumX < 0.0D ? this.hitBox.getMinX() : this.hitBox.getMaxX();
         int var3 = (int)FastMath.floor(var1);
         int var4 = (int)FastMath.floor(var1 + this.momentumX);
         if (var3 != var4) {
            int var5 = var3 + (this.momentumX < 0.0D ? -1 : 1);
            int var6 = this.hitBox.getBlockMinY();
            int var7 = this.hitBox.getBlockMaxY();
            int var8 = this.hitBox.getBlockMinZ();
            int var9 = this.hitBox.getBlockMaxZ();
            boolean var10 = false;

            int var11;
            int var12;
            label71:
            for(var11 = var6; var11 <= var7; ++var11) {
               for(var12 = var8; var12 <= var9; ++var12) {
                  if (this.blockInfoService.isCanStandOnSurfaceAt(this.world, var5, var11, var12)) {
                     var10 = true;
                     break label71;
                  }
               }
            }

            var11 = this.configuration.physics().getBlockClimbCapacity();
            if (var11 > 0 && var10 && this.onGround) {
               for(var12 = 1; var12 <= var11; ++var12) {
                  if (var12 >= this.climbing) {
                     VehicleHitBox var13 = this.hitBox.future(this.x + this.momentumX, this.y + (double)var12, this.z);
                     if (!this.canClimbTo(var13)) {
                        this.climbing = var12;
                        var10 = false;
                        break;
                     }
                  }
               }
            }

            if (var10) {
               if (this.momentumX < 0.0D) {
                  this.forceX = (double)(var5 + 1) + this.hitBox.getWidth() / 2.0D + 0.1D;
               } else {
                  this.forceX = (double)var5 - this.hitBox.getWidth() / 2.0D - 0.1D;
               }

               this.momentumX = 0.0D;
            }
         }
      }
   }

   private void tickMomentumZ() {
      if (this.momentumZ != 0.0D) {
         double var1 = this.momentumZ < 0.0D ? this.hitBox.getMinZ() : this.hitBox.getMaxZ();
         int var3 = (int)FastMath.floor(var1);
         int var4 = (int)FastMath.floor(var1 + this.momentumZ);
         if (var3 != var4) {
            int var5 = var3 + (this.momentumZ < 0.0D ? -1 : 1);
            int var6 = this.hitBox.getBlockMinY();
            int var7 = this.hitBox.getBlockMaxY();
            int var8 = this.hitBox.getBlockMinX();
            int var9 = this.hitBox.getBlockMaxX();
            boolean var10 = false;

            int var11;
            int var12;
            label71:
            for(var11 = var6; var11 <= var7; ++var11) {
               for(var12 = var8; var12 <= var9; ++var12) {
                  if (this.blockInfoService.isCanStandOnSurfaceAt(this.world, var12, var11, var5)) {
                     var10 = true;
                     break label71;
                  }
               }
            }

            var11 = this.configuration.physics().getBlockClimbCapacity();
            if (var11 > 0 && var10 && this.onGround) {
               for(var12 = 1; var12 <= var11; ++var12) {
                  if (var12 >= this.climbing) {
                     VehicleHitBox var13 = this.hitBox.future(this.x, this.y + (double)var12, this.z + this.momentumZ);
                     if (!this.canClimbTo(var13)) {
                        this.climbing = var12;
                        var10 = false;
                        break;
                     }
                  }
               }
            }

            if (var10) {
               if (this.momentumZ < 0.0D) {
                  this.forceZ = (double)(var5 + 1) + this.hitBox.getDepth() / 2.0D + 0.1D;
               } else {
                  this.forceZ = (double)var5 - this.hitBox.getDepth() / 2.0D - 0.1D;
               }

               this.momentumZ = 0.0D;
            }
         }
      }
   }

   private void tickFriction() {
      if (this.momentumX != 0.0D || this.momentumY != 0.0D || this.momentumZ != 0.0D) {
         Map var1 = this.getCurrentSurface();
         VehiclePhysicsConfiguration var2 = this.configuration.physics();
         double var3 = 0.0D;
         double var5 = var2.getAirFriction();
         Iterator var7 = var1.entrySet().iterator();

         while(var7.hasNext()) {
            Entry var8 = (Entry)var7.next();
            switch((EnumSurface)var8.getKey()) {
            case EMPTY:
               var3 += (Double)var8.getValue() * var2.getAirFriction();
               break;
            case SOLID:
               var3 += (Double)var8.getValue() * var2.getFrictionOnSolid();
               break;
            case DUSTY:
               var3 += (Double)var8.getValue() * var2.getFrictionOnDusty();
               break;
            case SNOWY:
               var3 += (Double)var8.getValue() * var2.getFrictionOnSnowy();
               break;
            case SLIPPERY:
               var3 += (Double)var8.getValue() * var2.getFrictionOnSlippery();
               break;
            case WATER:
               var3 += (Double)var8.getValue() * var2.getFrictionOnWater();
               break;
            case LAVA:
               var3 += (Double)var8.getValue() * var2.getFrictionOnLava();
               break;
            case UNKNOWN:
               var3 += (Double)var8.getValue() * var2.getFrictionOnUnknown();
            }
         }

         this.momentumX = this.thresholdCheck(this.applyFriction(this.momentumX, var3));
         this.momentumY = this.thresholdCheck(this.applyFriction(this.momentumY, var5));
         this.momentumZ = this.thresholdCheck(this.applyFriction(this.momentumZ, var3));
      }
   }

   private boolean canClimbTo(VehicleHitBox future) {
      int var2 = var1.getBlockMinX();
      int var3 = var1.getBlockMinY();
      int var4 = var1.getBlockMinZ();
      int var5 = var1.getBlockMaxX();
      int var6 = var1.getBlockMaxY();
      int var7 = var1.getBlockMaxZ();

      for(int var8 = var2; var8 <= var5; ++var8) {
         for(int var9 = var6; var9 >= var3; --var9) {
            for(int var10 = var4; var10 <= var7; ++var10) {
               if (this.blockInfoService.isCanStandOnSurfaceAt(this.world, var8, var9, var10)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   @NotNull
   public UUID getUniqueId() {
      return this.uniqueId;
   }

   @NotNull
   public VehicleConfiguration getConfiguration() {
      return this.configuration;
   }

   @Nullable
   public UpgradeConfiguration getUpgradeConfiguration() {
      return this.getConfiguration().getUpgrade();
   }

   @NotNull
   public VehicleHitBox getCurrentHitBox() {
      return this.hitBox.copy();
   }

   @Nullable
   public Integer getCurrentHitBoxEntityId() {
      return this.damageHitbox != null ? this.damageHitbox.getHandleId() : null;
   }

   @NotNull
   public World getWorld() {
      return this.world;
   }

   @NotNull
   public PacketBatcher getPacketBatcher() {
      return this.packetBatcher;
   }

   @NotNull
   public Map<EnumSurface, Double> getCurrentSurface(@Nullable Collection<EnumSurface> ignore) {
      THashMap var2 = new THashMap();
      int var3 = (int)FastMath.floor(this.hitBox.getMinX());
      int var4 = (int)FastMath.floor(this.hitBox.getMinZ());
      int var5 = (int)FastMath.floor(this.hitBox.getMaxX());
      int var6 = (int)FastMath.floor(this.hitBox.getMaxZ());
      BlockInfoService.SurfaceResult var7 = this.blockInfoService.getSurfaceTypesAt(this.world, (int)FastMath.floor(this.hitBox.getMinY() - 0.001D), var3, var4, var5, var6, false, false);
      int var8 = 0;
      EnumSurface[] var9;
      int var10;
      int var11;
      EnumSurface var12;
      if (var1 != null && var1.size() > 0) {
         var9 = var7.getValue();
         var10 = var9.length;

         for(var11 = 0; var11 < var10; ++var11) {
            var12 = var9[var11];
            if (var1.contains(var12)) {
               ++var8;
            }
         }
      }

      var9 = var7.getValue();
      var10 = var9.length;

      for(var11 = 0; var11 < var10; ++var11) {
         var12 = var9[var11];
         if (var1 == null || !var1.contains(var12)) {
            double var13 = 1.0D / (double)(var7.getValue().length - var8);
            if (var2.containsKey(var12)) {
               var2.put(var12, (Double)var2.get(var12) + var13);
            } else {
               var2.put(var12, var13);
            }
         }
      }

      return var2;
   }

   @NotNull
   public Map<EnumSurface, Double> getCurrentSurface() {
      return Vehicle.super.getCurrentSurface();
   }

   public boolean isOnGround() {
      return this.onGround;
   }

   public boolean isOnSurfaces(@NotNull EnumSurface... surfaces) {
      Map var2 = this.getCurrentSurface();
      EnumSurface[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnumSurface var6 = var3[var5];
         if (!var2.containsKey(var6)) {
            return false;
         }
      }

      return true;
   }

   public boolean isOnAnySurface(@NotNull EnumSurface... surfaces) {
      Map var2 = this.getCurrentSurface();
      EnumSurface[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnumSurface var6 = var3[var5];
         if (var2.containsKey(var6)) {
            return true;
         }
      }

      return false;
   }

   public boolean isOnSurface(@NotNull EnumSurface surface) {
      return this.getCurrentSurface().containsKey(var1);
   }

   public boolean isOnSolidSurface() {
      return this.isOnSurface(EnumSurface.SOLID);
   }

   public boolean isOnWaterSurface() {
      return this.isOnSurface(EnumSurface.WATER);
   }

   public boolean isOnLavaSurface() {
      return this.isOnSurface(EnumSurface.LAVA);
   }

   public boolean isOnLiquidSurface() {
      return this.isOnWaterSurface() || this.isOnLavaSurface();
   }

   public boolean isInTheAir() {
      Map var1 = this.getCurrentSurface();
      return var1.size() == 0 || var1.size() == 1 && var1.containsKey(EnumSurface.EMPTY);
   }

   public boolean containedWithin(@NotNull EnumSurface... surfaces) {
      HashSet var2 = new HashSet(Arrays.asList(var1));
      if (var2.size() == 0) {
         return false;
      } else {
         int var3 = (int)FastMath.floor(this.hitBox.getMinX());
         int var4 = (int)FastMath.floor(this.hitBox.getMinZ());
         int var5 = (int)FastMath.floor(this.hitBox.getMinY());
         int var6 = (int)FastMath.floor(this.hitBox.getMaxY());
         int var7 = (int)FastMath.floor(this.hitBox.getMaxX());
         int var8 = (int)FastMath.floor(this.hitBox.getMaxZ());

         for(int var9 = var3; var9 <= var7; ++var9) {
            for(int var10 = var5; var10 <= var6; ++var10) {
               for(int var11 = var4; var11 <= var8; ++var11) {
                  var2.remove(this.blockInfoService.getSurfaceTypeAt(this.world, var9, var10, var11, false));
                  if (var2.size() == 0) {
                     return var2.size() == 0;
                  }
               }
            }
         }

         return var2.size() == 0;
      }
   }

   public boolean containsAnyWithin(@NotNull EnumSurface... surfaces) {
      HashSet var2 = new HashSet(Arrays.asList(var1));
      EnumSurface var3 = var2.size() == 1 ? (EnumSurface)var2.iterator().next() : null;
      int var4 = (int)FastMath.floor(this.hitBox.getMinX());
      int var5 = (int)FastMath.floor(this.hitBox.getMinZ());
      int var6 = (int)FastMath.floor(this.hitBox.getMinY());
      int var7 = (int)FastMath.floor(this.hitBox.getMaxY());
      int var8 = (int)FastMath.floor(this.hitBox.getMaxX());
      int var9 = (int)FastMath.floor(this.hitBox.getMaxZ());

      for(int var10 = var4; var10 <= var8; ++var10) {
         for(int var11 = var6; var11 <= var7; ++var11) {
            for(int var12 = var5; var12 <= var9; ++var12) {
               EnumSurface var13 = this.blockInfoService.getSurfaceTypeAt(this.world, var10, var11, var12, false);
               if (var3 != null && var13 == var3 || var2.contains(var13)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   @NotNull
   public Set<VehicleSeatImpl> getSeats() {
      return Collections.unmodifiableSet(this.seats);
   }

   @NotNull
   public Set<VehicleProjectileShooterImpl> getProjectileShooters() {
      return Collections.unmodifiableSet(this.projectileShooters);
   }

   @NotNull
   public VehicleSeatImpl getMainSeat() {
      return this.mainSeat;
   }

   @Nullable
   public Entity getOperator() {
      return this.mainSeat.getPassenger();
   }

   public void setOperator(@Nullable Entity operator) {
      this.mainSeat.setPassenger(var1);
   }

   public boolean hasController(@NotNull VehicleController controller) {
      return this.controllers.contains(var1);
   }

   public void addController(@NotNull VehicleController controller) {
      this.controllers.add(var1);
   }

   public void removeController(@NotNull VehicleController controller) {
      this.controllers.remove(var1);
   }

   boolean isSpawned() {
      return this.spawned;
   }

   void spawn() {
      if (!this.spawned) {
         this.spawned = true;
         this.model.spawn();
         this.seats.forEach(VehicleSeatImpl::spawn);
         if (this.damageHitbox != null) {
            this.damageHitbox.spawn();
         }

         this.vehicleInteraction.setWorld(this.world);
         if (this.getOwner() != null && this.getOwner().isOnline() && this.getConfiguration().getPlacement().isAddOnPlace()) {
            this.getMainSeat().setPassenger(this.getOwner());
         }

      }
   }

   public void remove() {
      this.remove(true);
   }

   public void remove(boolean pickup) {
      if (!this.removed) {
         this.despawn();
         this.handler.removeVehicle(this);
         if (this.getOwner() != null && this.getConfiguration().hasPickupItem() && var1) {
            if (Bukkit.isPrimaryThread()) {
               this.getOwner().getInventory().addItem(new ItemStack[]{this.getConfiguration().getPickupItem().getItemStack()}).forEach((var1x, var2) -> {
                  this.getOwner().getLocation().getWorld().dropItemNaturally(this.getOwner().getLocation(), var2);
               });
            } else {
               Run.sync(() -> {
                  this.getOwner().getInventory().addItem(new ItemStack[]{this.getConfiguration().getPickupItem().getItemStack()}).forEach((var1, var2) -> {
                     this.getOwner().getLocation().getWorld().dropItemNaturally(this.getOwner().getLocation(), var2);
                  });
               });
            }
         }

      }
   }

   public void setKey(boolean keyed) {
      this.keyed = var1;
   }

   public void despawn() {
      if (!this.removed) {
         this.removed = true;
         this.model.destroy();
         this.seats.forEach(VehicleSeatImpl::destroy);
         this.vehicleInteraction.clear();
         if (this.damageHitbox != null) {
            this.damageHitbox.destroy();
            this.damageHitbox = null;
         }

      }
   }

   void destroy() {
      this.destroy(true);
   }

   void destroy(boolean pickup) {
      if (!this.removed) {
         this.remove(var1);
         VehicleEffectConfiguration var2 = this.configuration.destroyEffect();
         if (var2 != null && var2.isValidParticle()) {
            this.world.spawnParticle((Particle)Objects.requireNonNull(var2.getParticleType()), this.getLocation(), var2.getParticleCount(), (double)var2.getParticleDispersion(), (double)var2.getParticleDispersion(), (double)var2.getParticleDispersion(), 0.0D, var2.getParticleData());
         }

         if (var2 != null && var2.isValidSound()) {
            Location var3 = this.getLocation();
            SoundCategory var4 = var2.getSoundCategory();
            Sound var5 = var2.getSoundType();
            String var6 = var2.getSoundTypeCustom();
            float var7 = var2.getSoundVolume();
            float var8 = var2.getSoundPitch();
            if (var5 != null) {
               if (var4 != null) {
                  this.world.playSound(var3, var5, var4, var7, var8);
               } else {
                  this.world.playSound(var3, var5, var7, var8);
               }
            } else if (StringUtils.isNotBlank(var6)) {
               if (var4 != null) {
                  this.world.playSound(var3, var6, var4, var7, var8);
               } else {
                  this.world.playSound(var3, var6, var7, var8);
               }
            }
         }

      }
   }

   public boolean isRemoved() {
      return this.removed;
   }

   public boolean isExists() {
      return Vehicle.super.isExists();
   }

   public float getFuelCapacity() {
      return Vehicle.super.getFuelCapacity();
   }

   public float getFuelLevel() {
      return (float)((double)((float)Math.round((double)this.fuelLevel * 100.0D)) / 100.0D);
   }

   public void setFuelLevel(float fuelLevel) {
      float var2 = this.configuration.fuel().getCapacity();
      if (var1 < 0.0F) {
         var1 = 0.0F;
      } else if (var1 > var2) {
         var1 = var2;
      }

      this.fuelLevel = var1;
   }

   public void addFuel(float amount) {
      Vehicle.super.addFuel(var1);
   }

   public void consumeFuel(float consumption) {
      Vehicle.super.consumeFuel(var1);
   }

   public float getMaxHealth() {
      return Vehicle.super.getMaxHealth();
   }

   public float getHealth() {
      return this.health;
   }

   public void setHealth(float health) {
      float var2 = this.configuration.health().getMaxHealth();
      if (var1 <= 0.0F) {
         this.health = 0.0F;
         this.destroy(false);
      } else {
         this.health = Math.min(var2, var1);
      }
   }

   public void heal(float amount) {
      Vehicle.super.heal(var1);
   }

   public void damage(@Nullable EnumDamageType type, float damage, @Nullable Entity causing, @Nullable Entity direct, @NotNull Entity victim, @Nullable Object extraData) {
      boolean var7 = false;
      Iterator var8 = this.seats.iterator();

      while(var8.hasNext()) {
         VehicleSeatImpl var9 = (VehicleSeatImpl)var8.next();
         if (Objects.equals(var9.getPassenger(), var3)) {
            var7 = true;
            break;
         }
      }

      if (!var7) {
         var8 = this.configuration.damage().getModifiers().iterator();

         while(var8.hasNext()) {
            VehicleDamageConfiguration.Modifier var12 = (VehicleDamageConfiguration.Modifier)var8.next();
            boolean var10 = var12.checkExtras().test(this, var6 instanceof String ? (String)var6 : null);
            if (var12.isInverted()) {
               var10 = !var10;
            }

            if (var12.getType() == var1 && var10) {
               switch(var12.getCalcType()) {
               case ADDITIVE:
                  var2 += (float)((double)var2 * var12.getValue());
                  break;
               case MULTIPLICATIVE:
                  var2 *= (float)((double)var2 * var12.getValue());
                  break;
               case DIVISIVE:
                  var2 /= (float)((double)var2 * var12.getValue());
               }
            }
         }

         if (var2 > 0.0F) {
            this.setHealth(this.getHealth() - var2);
            this.playDamageEffect(var4);
         }

         if (this.getConfiguration().getDamage().isPassThroughDamage()) {
            var8 = this.getSeats().iterator();

            while(var8.hasNext()) {
               VehicleSeat var13 = (VehicleSeat)var8.next();
               if (var13.getPassenger() != null) {
                  Entity var11 = var13.getPassenger();
                  if (var11 instanceof LivingEntity) {
                     LivingEntity var14 = (LivingEntity)var11;
                     var14.damage((double)var2, var3 != null ? var3 : var5);
                  }
               }
            }
         }

      }
   }

   @Nullable
   public UUID getOwnerUniqueId() {
      return this.ownerUniqueId;
   }

   @Nullable
   public Player getOwner() {
      return Vehicle.super.getOwner();
   }

   public void setOwner(@Nullable UUID ownerUniqueId) {
      this.ownerUniqueId = var1;
   }

   public void setOwner(@Nullable Player owner) {
      Vehicle.super.setOwner(var1);
   }

   public boolean hasOwner() {
      return Vehicle.super.hasOwner();
   }

   public boolean isTheOwner(@NotNull UUID playerUniqueId) {
      return Vehicle.super.isTheOwner(var1);
   }

   public boolean isTheOwner(@NotNull Player player) {
      return Vehicle.super.isTheOwner(var1);
   }

   public boolean isIn(@NotNull Entity entity) {
      Iterator var2 = this.seats.iterator();

      VehicleSeatImpl var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (VehicleSeatImpl)var2.next();
      } while(!Objects.equals(var1, var3.getPassenger()));

      return true;
   }

   public boolean isPassenger(@NotNull Entity entity) {
      Iterator var2 = this.seats.iterator();

      VehicleSeatImpl var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (VehicleSeatImpl)var2.next();
      } while(var3.isMain() || !Objects.equals(var1, var3.getPassenger()));

      return true;
   }

   public boolean isOperator(@NotNull Entity entity) {
      return Objects.equals(var1, this.mainSeat.getPassenger());
   }

   public boolean isPersistent() {
      return this.persistent;
   }

   public void setPersistent(boolean persistent) {
      if (this.persistent != var1) {
         this.persistent = var1;
         this.handler.processVehiclePersistenceChanged(this, var1);
      }
   }

   @NotNull
   public VehicleState getCurrentState() {
      return this.state;
   }

   public List<VehicleItemStorage> getStorage() {
      return this.storage;
   }

   public void setState(@NotNull VehicleState state) {
      if (!Objects.equals(var1, this.state)) {
         VehicleStateChangeEvent var2 = new VehicleStateChangeEvent(this, this.state, var1);
         this.state = var1;
         this.model.setState(var1);
         Objects.requireNonNull(var2);
         Run.sync(var2::callEvent);
      }

   }

   @NotNull
   public Location getLocation() {
      return new Location(this.world, this.x, this.y, this.z, this.rotation, 0.0F);
   }

   public double getX() {
      return this.x;
   }

   public double getY() {
      return this.y;
   }

   public double getZ() {
      return this.z;
   }

   public float getRotation() {
      return this.rotation;
   }

   public void setRotation(float rotation) {
      var1 %= 360.0F;
      if (Float.compare(this.rotation, var1) != 0) {
         this.rotation = var1;
         this.rotationDirty = true;
      }

   }

   public void setLocation(double x, double y, double z) {
      if (Double.compare(var1, this.x) != 0 || Double.compare(var3, this.y) != 0 || Double.compare(var5, this.z) != 0) {
         this.x = var1;
         this.y = var3;
         this.z = var5;
         this.locationDirty = true;
      }
   }

   public void setLocationAndRotation(double x, double y, double z, float rotation) {
      this.setLocation(var1, var3, var5);
      this.setRotation(var7);
   }

   private void applyLocationRotation(boolean climbing) {
      if (this.locationDirty || this.rotationDirty) {
         this.packetBatcher.begin();
         this.seats.forEach(VehicleSeatImpl::onVehicleMove);
         if (this.locationDirty && this.rotationDirty) {
            this.model.setLocationAndRotation(this.x, this.y, this.z, this.rotation, var1);
         } else if (this.locationDirty) {
            this.model.setLocation(this.x, this.y, this.z, var1);
         } else {
            this.model.setRotation(this.rotation);
         }

         this.packetBatcher.complete();
         this.hitBox.setOrigin(this.x, this.y, this.z);
         if (this.damageHitbox != null) {
            this.damageHitbox.setLocation(this.hitBox.getOriginX(), this.hitBox.getOriginY(), this.hitBox.getOriginZ());
            this.damageHitbox.setOrientation(this.rotation);
         }

         this.vehicleInteraction.processVehicleLocationChanged();
      }

      this.locationDirty = false;
      this.rotationDirty = false;
   }

   public void moveToWorld(@NotNull World world, double x, double y, double z) {
      World var8 = this.world;
      if (!Objects.equals(var1, var8)) {
         this.world = var1;
         this.setLocation(var2, var4, var6);
         this.setMomentum(0.0D, 0.0D, 0.0D);
         this.applyLocationRotation(false);
         this.model.destroy();
         this.model = this.createModelInstance(var2, var4, var6);
         if (this.spawned) {
            this.model.spawn();
            this.vehicleInteraction.setWorld(var1);
            this.handler.processVehicleWorldChanged(this, var8, var1);
         }

         this.resetDamageHitbox();
      }
   }

   public double getMomentumX() {
      return this.momentumX;
   }

   public void setMomentumX(double momentumX) {
      this.momentumComponentCheck(var1);
      this.momentumX = this.thresholdCheck(var1);
   }

   public double getMomentumY() {
      return this.momentumY;
   }

   public void setMomentumY(double momentumY) {
      this.momentumComponentCheck(var1);
      this.momentumY = this.thresholdCheck(var1);
   }

   public double getMomentumZ() {
      return this.momentumZ;
   }

   public void setMomentumZ(double momentumZ) {
      this.momentumComponentCheck(var1);
      this.momentumZ = this.thresholdCheck(var1);
   }

   public void setMomentum(double x, double y, double z) {
      this.setMomentumX(var1);
      this.setMomentumY(var3);
      this.setMomentumZ(var5);
   }

   public void addMomentumX(double momentumX) {
      this.momentumComponentCheck(var1);
      this.momentumX = this.thresholdCheck(this.momentumX + var1);
   }

   public void addMomentumY(double momentumY) {
      this.momentumComponentCheck(var1);
      this.momentumY = this.thresholdCheck(this.momentumY + var1);
   }

   public void addMomentumZ(double momentumZ) {
      this.momentumComponentCheck(var1);
      this.momentumZ = this.thresholdCheck(this.momentumZ + var1);
   }

   public void addMomentum(double x, double y, double z) {
      this.addMomentumX(var1);
      this.addMomentumY(var3);
      this.addMomentumZ(var5);
   }

   private void momentumComponentCheck(double component) {
      Preconditions.checkArgument(!Double.isNaN(var1), "momentum cannot be NaN");
      Preconditions.checkArgument(!Double.isInfinite(var1), "momentum cannot be infinite");
   }

   public void input(@NotNull PlayerInput input) {
      if (!var1.isKeepAlive()) {
         Iterator var2 = this.controllers.iterator();

         while(var2.hasNext()) {
            VehicleController var3 = (VehicleController)var2.next();
            var3.process(var1);
         }
      }

   }

   public void input(@NotNull PlayerSteerInput input) {
      if (var1.keepAlive) {
         this.standBy();
      } else {
         this.standby = false;
         Iterator var2 = this.controllers.iterator();

         while(var2.hasNext()) {
            VehicleController var3 = (VehicleController)var2.next();
            var3.process(var1);
         }
      }

   }

   protected synchronized void standBy() {
      if (!this.standby) {
         this.standby = true;
         Iterator var1 = this.controllers.iterator();

         while(var1.hasNext()) {
            VehicleController var2 = (VehicleController)var1.next();
            var2.standby();
         }

      }
   }

   void resetDamageHitbox() {
      if (this.spawned && this.damageHitbox != null) {
         this.damageHitbox.destroy();
      }

      this.damageHitbox = null;
      if (!this.configuration.health().isDisabled()) {
         this.damageHitbox = this.createDamageHitbox(this.world);
         if (this.spawned) {
            this.damageHitbox.spawn();
         }
      }

   }

   public boolean equals(Object o) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         VehicleImpl var2 = (VehicleImpl)var1;
         return this.uniqueId.equals(var2.uniqueId);
      } else {
         return false;
      }
   }

   public VehicleStorageSlotsConfiguration getStorageSize() {
      return Vehicle.super.getStorageSize();
   }

   public void setStorageSize(VehicleStorageSlotsConfiguration storageSize) {
      this.storageSize = var1;
   }

   public int hashCode() {
      return this.uniqueId.hashCode();
   }

   private VehicleModel<?> createModelInstance(double x, double y, double z) {
      if (this.modelConfiguration instanceof CompoundModelConfiguration) {
         return new CompoundModel(this, (CompoundModelConfiguration)this.modelConfiguration, this.world, var1, var3, var5);
      } else {
         throw new IllegalStateException("not implemented yet: " + String.valueOf(this.modelConfiguration));
      }
   }

   private DamageHitbox createDamageHitbox(@NotNull World world) {
      VehicleHitBoxConfiguration var2 = this.configuration.model().getHitBox();
      DamageHitbox var3 = DamageHitbox.create(var1);
      var3.setWidth((float)Math.max(var2.getWidth(), var2.getDepth()));
      var3.setHeight((float)var2.getHeight());
      var3.setLocation(this.x, this.y, this.z);
      var3.setOrientation(this.rotation);
      var3.setVehicle(this);
      var3.setListener((var1x, var2x, var3x, var4, var5) -> {
         this.damage(var1x, var2x, var3x, var4, var5, (Object)null);
      });
      return var3;
   }

   private double applyFriction(double momentum, double friction) {
      if (var1 > 0.0D) {
         var1 -= var1 * var3;
         if (var1 < 0.0D) {
            var1 = 0.0D;
         }
      } else if (var1 < 0.0D) {
         var1 += FastMath.abs(var1) * var3;
         if (var1 > 0.0D) {
            var1 = 0.0D;
         }
      }

      return var1;
   }

   private double thresholdCheck(double momentum) {
      return FastMath.abs(var1) < 0.001D ? 0.0D : var1;
   }

   private void playDamageEffect(@Nullable Entity direct) {
      VehicleEffectConfiguration var2 = this.configuration.damageEffect();
      if (var2 != null) {
         if (var2.isValidParticle() || var2.isValidSound()) {
            Vector var3 = this.calculateHitPoint(var1);
            if (var2.isValidParticle() && var3 != null) {
               this.world.spawnParticle((Particle)Objects.requireNonNull(var2.getParticleType()), var3.toLocation(this.world), var2.getParticleCount(), (double)var2.getParticleDispersion(), (double)var2.getParticleDispersion(), (double)var2.getParticleDispersion(), 0.0D, var2.getParticleData());
            }

            if (var2.isValidSound() && var3 != null) {
               Location var4 = var3.toLocation(this.world);
               SoundCategory var5 = var2.getSoundCategory();
               Sound var6 = var2.getSoundType();
               String var7 = var2.getSoundTypeCustom();
               float var8 = var2.getSoundVolume();
               float var9 = var2.getSoundPitch();
               if (var6 != null) {
                  if (var5 != null) {
                     this.world.playSound(var4, var6, var5, var8, var9);
                  } else {
                     this.world.playSound(var4, var6, var8, var9);
                  }
               } else if (StringUtils.isNotBlank(var7)) {
                  if (var5 != null) {
                     this.world.playSound(var4, var7, var5, var8, var9);
                  } else {
                     this.world.playSound(var4, var7, var8, var9);
                  }
               }
            }
         }

      }
   }

   @Nullable
   private Vector calculateHitPoint(@Nullable Entity direct) {
      Vector var2 = null;
      if (var1 instanceof Projectile) {
         var2 = var1.getLocation().toVector();
      } else if (var1 != null) {
         double var3 = Math.max(this.hitBox.getWidth(), this.hitBox.getDepth()) / 2.0D;
         var2 = this.getLocation().toVector().add(var1.getLocation().toVector().subtract(this.getLocation().toVector()).normalize().multiply(var3));
         var2.add(new Vector(0.0D, var1.getHeight() / 2.0D, 0.0D));
      }

      return var2;
   }

   @NotNull
   public List<IJoint> getBlockBenchBones() {
      return this.blockBenchBones;
   }

   public Set<VehicleController> getVehicleControllers() {
      return this.controllers;
   }

   public boolean isMoving() {
      return this.getMomentumX() == 0.0D && this.getMomentumY() == 0.0D && this.getMomentumZ() == 0.0D;
   }

   public VehicleInteraction getVehicleInteraction() {
      return this.vehicleInteraction;
   }

   public VehicleHandlerImpl getHandler() {
      return this.handler;
   }

   public BlockInfoService getBlockInfoService() {
      return this.blockInfoService;
   }

   @NotNull
   public VehicleModelConfiguration getModelConfiguration() {
      return this.modelConfiguration;
   }

   @NotNull
   public VehicleHitBox getHitBox() {
      return this.hitBox;
   }

   @NotNull
   public Set<VehicleController> getControllers() {
      return this.controllers;
   }

   @NotNull
   public Map<String, Integer> getUpgradeTiers() {
      return this.upgradeTiers;
   }

   @Nullable
   public DamageHitbox getDamageHitbox() {
      return this.damageHitbox;
   }

   @NotNull
   public VehicleModel<?> getModel() {
      return this.model;
   }

   @NotNull
   public VehicleState getState() {
      return this.state;
   }

   public boolean isStandby() {
      return this.standby;
   }

   public boolean isLocationDirty() {
      return this.locationDirty;
   }

   public boolean isRotationDirty() {
      return this.rotationDirty;
   }

   public Double getForceX() {
      return this.forceX;
   }

   public Double getForceY() {
      return this.forceY;
   }

   public Double getForceZ() {
      return this.forceZ;
   }

   public int getClimbing() {
      return this.climbing;
   }

   public boolean isKeyed() {
      return this.keyed;
   }

   public float getTargetRotation() {
      return this.targetRotation;
   }
}
