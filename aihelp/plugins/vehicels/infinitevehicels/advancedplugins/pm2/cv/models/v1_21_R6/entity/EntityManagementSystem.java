package advancedplugins.pm2.cv.models.v1_21_R6.entity;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.Hitbox;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.BukkitEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.interaction.DynamicHitbox;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.SubHitbox;
import advancedplugins.pm2.cv.models.api.nms.entity.EntityHandler;
import advancedplugins.pm2.cv.models.api.nms.entity.HitboxEntity;
import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.BodyRotationController;
import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.LookController;
import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.MoveController;
import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.TrackedEntity;
import advancedplugins.pm2.cv.models.api.nms.impl.TempTrackedEntity;
import advancedplugins.pm2.cv.models.api.utils.RaceConditionUtil;
import advancedplugins.pm2.cv.models.api.utils.config.ConfigProperty;
import advancedplugins.pm2.cv.models.api.utils.config.DebugToggle;
import advancedplugins.pm2.cv.models.api.utils.future.Future;
import advancedplugins.pm2.cv.models.api.utils.logger.LogUtil;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import advancedplugins.pm2.cv.models.api.utils.reflections.ReflectionUtils;
import advancedplugins.pm2.cv.models.v1_21_R6.ReflectionFieldCatalog;
import advancedplugins.pm2.cv.models.v1_21_R6.ReflectionMethodCatalog;
import advancedplugins.pm2.cv.models.v1_21_R6.entity.controller.EntityBodyOrientationManager;
import advancedplugins.pm2.cv.models.v1_21_R6.entity.controller.GazeDirectionHandler;
import advancedplugins.pm2.cv.models.v1_21_R6.entity.controller.LocomotionManager;
import advancedplugins.pm2.cv.models.v1_21_R6.entity.hitbox.CollisionVolumeEntity;
import advancedplugins.pm2.cv.models.v1_21_R6.entity.navigation.AerialNavigatorEnhanced;
import advancedplugins.pm2.cv.models.v1_21_R6.entity.navigation.AmphibiousPathfinderEnhanced;
import advancedplugins.pm2.cv.models.v1_21_R6.entity.navigation.AquaticNavigatorOptimized;
import advancedplugins.pm2.cv.models.v1_21_R6.entity.navigation.TerrainNavigatorOptimized;
import advancedplugins.pm2.cv.models.v1_21_R6.entity.navigation.VerticalPathfinderEnhanced;
import advancedplugins.pm2.cv.models.v1_21_R6.network.patch.ServerInteractionProcessor;
import advancedplugins.pm2.cv.models.v1_21_R6.network.utils.PacketBundleProvider;
import advancedplugins.pm2.cv.models.v1_21_R6.network.utils.PacketTransmissionUtility;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.PacketPlayInBlockPlace;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntitySound;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.network.syncher.DataWatcher.c;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.level.PlayerChunkMap.EntityTracker;
import net.minecraft.sounds.SoundCategory;
import net.minecraft.util.MathHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.EnumHand;
import net.minecraft.world.EnumInteractionResult;
import net.minecraft.world.EnumInteractionResult.d;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityAttachments;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntitySize;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumMoveType;
import net.minecraft.world.entity.ai.attributes.AttributeModifiable;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalSelector;
import net.minecraft.world.entity.ai.goal.PathfinderGoalWrapped;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.Navigation;
import net.minecraft.world.entity.ai.navigation.NavigationAbstract;
import net.minecraft.world.entity.ai.navigation.NavigationFlying;
import net.minecraft.world.entity.ai.navigation.NavigationGuardian;
import net.minecraft.world.entity.ai.navigation.NavigationSpider;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.World;
import net.minecraft.world.phys.MovingObjectPositionBlock;
import net.minecraft.world.phys.Vec3D;
import net.minecraft.world.phys.MovingObjectPosition.EnumMovingObjectType;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.craftbukkit.v1_21_R5.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R5.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R5.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class EntityManagementSystem implements EntityHandler {
   private static final AtomicInteger ID_GENERATOR;
   private static boolean paperOptimization;
   private final Set<UUID> concealedPlayers = new HashSet();
   private double overrideRenderWidth;
   private double overrideRenderHeight;
   private EntityArmorStand placeholderMount;

   public EntityManagementSystem() {
      ModelAPI.getAPI().getConfigManager().registerReferenceUpdate(this::updateConfig);
   }

   public void updateConfig() {
      this.overrideRenderWidth = ConfigProperty.BLOCK_CULL_IGNORE_SIZE_WIDTH.getDouble();
      this.overrideRenderHeight = ConfigProperty.BLOCK_CULL_IGNORE_SIZE_HEIGHT.getDouble();
      paperOptimization = ConfigProperty.BLOCK_CULL_USE_PAPER_CLIP.getBoolean();
   }

   public int getNextEntityId() {
      return ID_GENERATOR == null ? 0 : ID_GENERATOR.incrementAndGet();
   }

   public void setHitbox(Entity var1, @NotNull Hitbox var2) {
      net.minecraft.world.entity.Entity var3 = EntityConversionUtil.toNMS(var1);
      EntitySize var4 = this.createEntityDimensions(var2);
      this.applyDimensions(var3, var4, var2);
   }

   private EntitySize createEntityDimensions(Hitbox var1) {
      float var2 = (float)var1.getMaxWidth();
      float var3 = (float)var1.getHeight();
      float var4 = (float)var1.getEyeHeight();
      return new EntitySize(var2, var3, var4, EntityAttachments.a(var2, var3), true);
   }

   private void applyDimensions(net.minecraft.world.entity.Entity var1, EntitySize var2, Hitbox var3) {
      ReflectionUtils.set(var1, ReflectionFieldCatalog.ENTITY_SIZE_DATA, var2);
      ReflectionUtils.set(var1, ReflectionFieldCatalog.ENTITY_VIEW_HEIGHT, (float)var3.getEyeHeight());
      var1.a(var2.a(var1.dv()));
   }

   public void setStepHeight(Entity var1, double var2) {
      net.minecraft.world.entity.Entity var4 = EntityConversionUtil.toNMS(var1);
      if (var4 instanceof EntityLiving) {
         EntityLiving var5 = (EntityLiving)var4;

         try {
            ((AttributeModifiable)Objects.requireNonNull(var5.h(GenericAttributes.C))).a(var2);
         } catch (Exception var7) {
         }
      }

   }

   public double getStepHeight(Entity var1) {
      return (double)EntityConversionUtil.toNMS(var1).dT();
   }

   public void setPosition(Entity var1, double var2, double var4, double var6) {
      EntityConversionUtil.toNMS(var1).a_(var2, var4, var6);
   }

   public void movePassenger(Entity var1, double var2, double var4, double var6) {
      net.minecraft.world.entity.Entity var8 = EntityConversionUtil.toNMS(var1);
      this.initializePlaceholderMount(var8.ai());
      double var9 = var4 - var8.m(this.placeholderMount).e;
      this.repositionPassenger(var8, var2, var9, var6);
   }

   private void initializePlaceholderMount(World var1) {
      if (this.placeholderMount == null) {
         this.placeholderMount = new EntityArmorStand(EntityTypes.g, var1);
         this.placeholderMount.v(true);
      }

   }

   private void repositionPassenger(net.minecraft.world.entity.Entity var1, double var2, double var4, double var6) {
      var1.a_(var2, var4, var6);
      var1.i(Vec3D.c);
      var1.j();
      if (var1 instanceof EntityPlayer) {
         EntityPlayer var8 = (EntityPlayer)var1;
         ReflectionUtils.set(var8.g, ReflectionFieldCatalog.FLOATING_STATE, false);
      }

   }

   public void forceSpawn(BaseEntity<?> var1, Player var2) {
      if (var2 != null) {
         IEntityData var3 = var1.getData();
         if (var3 instanceof BukkitEntityData) {
            BukkitEntityData var4 = (BukkitEntityData)var3;
            var4.getTracked().sendPairingData(var2);
         }
      }

   }

   public void forceDespawn(BaseEntity<?> var1, Player var2) {
      if (var2 != null) {
         PacketTransmissionUtility.transmitToPlayer(var2.getUniqueId(), new PacketPlayOutEntityDestroy(new int[]{var1.getEntityId()}));
      }

   }

   public void forceSpawn(Entity var1) {
   }

   public void forceDespawn(Entity var1) {
      if (var1 != null && var1 instanceof Player) {
         int var2 = EntityConversionUtil.toNMS(var1).ar();
         PacketTransmissionUtility.transmitToPlayer(((Player)var1).getUniqueId(), new PacketPlayOutEntityDestroy(new int[]{var2}));
      }

   }

   public void setForcedInvisible(Player var1, boolean var2) {
      if (this.isForcedInvisible(var1) != var2) {
         this.updatePlayerVisibility(var1, var2);
      }

   }

   private void updatePlayerVisibility(Player var1, boolean var2) {
      net.minecraft.world.entity.Entity var3 = EntityConversionUtil.toNMS(var1);
      byte var4 = this.compileEntityFlags(var3);
      if (var2) {
         this.concealedPlayers.add(var1.getUniqueId());
         var4 = MathUtils.setBit(var4, 5, true);
      } else {
         this.concealedPlayers.remove(var1.getUniqueId());
      }

      this.sendVisibilityUpdate(var1, var4);
   }

   private byte compileEntityFlags(net.minecraft.world.entity.Entity var1) {
      byte var2 = 0;

      for(int var3 = 0; var3 < 8; ++var3) {
         var2 = MathUtils.setBit(var2, var3, var1.i(var3));
      }

      return var2;
   }

   private void sendVisibilityUpdate(Player var1, byte var2) {
      PacketPlayOutEntityMetadata var3 = new PacketPlayOutEntityMetadata(var1.getEntityId(), List.of(new c(0, DataWatcherRegistry.a, var2)));
      PacketTransmissionUtility.transmitToPlayer(var1.getUniqueId(), var3);
   }

   public boolean isForcedInvisible(Player var1) {
      return this.concealedPlayers.contains(var1.getUniqueId());
   }

   public BodyRotationController wrapBodyRotationControl(Entity var1, Supplier<BodyRotationController> var2) {
      net.minecraft.world.entity.Entity var3 = EntityConversionUtil.toNMS(var1);
      if (var3 instanceof EntityInsentient) {
         EntityInsentient var4 = (EntityInsentient)var3;
         EntityBodyOrientationManager var5 = new EntityBodyOrientationManager(var4);
         boolean var6 = ReflectionUtils.set(var4, ReflectionFieldCatalog.ROTATION_CONTROLLER, var5);
         return (BodyRotationController)(var6 ? var5 : (BodyRotationController)var2.get());
      } else {
         return (BodyRotationController)var2.get();
      }
   }

   public MoveController wrapMoveController(Entity var1, Supplier<MoveController> var2) {
      net.minecraft.world.entity.Entity var3 = EntityConversionUtil.toNMS(var1);
      if (var3 instanceof EntityInsentient) {
         EntityInsentient var4 = (EntityInsentient)var3;
         LocomotionManager var5 = new LocomotionManager(var4, var4.Q());
         boolean var6 = ReflectionUtils.set(var4, ReflectionFieldCatalog.MOVEMENT_CONTROLLER, var5);
         return (MoveController)(var6 ? var5 : (MoveController)var2.get());
      } else {
         return (MoveController)var2.get();
      }
   }

   public LookController wrapLookController(Entity var1, Supplier<LookController> var2) {
      net.minecraft.world.entity.Entity var3 = EntityConversionUtil.toNMS(var1);
      if (var3 instanceof EntityInsentient) {
         EntityInsentient var4 = (EntityInsentient)var3;
         GazeDirectionHandler var5 = new GazeDirectionHandler(var4, var4.P());
         boolean var6 = ReflectionUtils.set(var4, ReflectionFieldCatalog.VISUAL_CONTROLLER, var5);
         return (LookController)(var6 ? var5 : (LookController)var2.get());
      } else {
         return (LookController)var2.get();
      }
   }

   public void wrapNavigation(Entity var1) {
      net.minecraft.world.entity.Entity var2 = EntityConversionUtil.toNMS(var1);
      if (var2 instanceof EntityInsentient) {
         EntityInsentient var3 = (EntityInsentient)var2;

         try {
            NavigationAbstract var4 = this.createEnhancedNavigation(var3);
            if (var4 != null) {
               this.installEnhancedNavigation(var3, var4);
            }
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

   }

   private NavigationAbstract createEnhancedNavigation(EntityInsentient var1) {
      NavigationAbstract var2 = var1.S();
      if (var2 instanceof NavigationSpider) {
         return new VerticalPathfinderEnhanced(var1, (NavigationSpider)var2);
      } else if (var2 instanceof Navigation) {
         return new TerrainNavigatorOptimized(var1, (Navigation)var2);
      } else if (var2 instanceof NavigationFlying) {
         return new AerialNavigatorEnhanced(var1, (NavigationFlying)var2);
      } else if (var2 instanceof NavigationGuardian) {
         return new AquaticNavigatorOptimized(var1);
      } else if (var2 instanceof AmphibiousPathNavigation) {
         return new AmphibiousPathfinderEnhanced(var1, (AmphibiousPathNavigation)var2);
      } else {
         String var10000 = String.valueOf(var1.ap());
         LogUtil.warn("Failed to create custom navigation for " + var10000 + ": " + String.valueOf(var1.cK()));
         LogUtil.warn("Reason: Navigation class type is " + var2.getClass().getSimpleName() + ".");
         return null;
      }
   }

   private void installEnhancedNavigation(EntityInsentient var1, NavigationAbstract var2) {
      Field var3 = ReflectionUtils.getField(ReflectionFieldCatalog.PATHFINDING_SYSTEM);
      var3.set(var1, var2);
      PathfinderGoalSelector var4 = (PathfinderGoalSelector)ReflectionUtils.getField(ReflectionFieldCatalog.BEHAVIOR_MANAGER).get(var1);
      RaceConditionUtil.wrapConmod(() -> {
         this.updateGoalNavigation(var4, var2);
      });
   }

   private void updateGoalNavigation(PathfinderGoalSelector var1, NavigationAbstract var2) {
      try {
         Iterator var3 = var1.b().iterator();

         while(var3.hasNext()) {
            PathfinderGoalWrapped var4 = (PathfinderGoalWrapped)var3.next();
            PathfinderGoal var5 = var4.k();
            this.updateGoalFields(var5, var2);
         }
      } catch (IllegalAccessException var6) {
         var6.printStackTrace();
      }

   }

   private void updateGoalFields(PathfinderGoal var1, NavigationAbstract var2) {
      Field[] var3 = var1.getClass().getDeclaredFields();
      Field[] var4 = var3;
      int var5 = var3.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Field var7 = var4[var6];
         var7 = ReflectionUtils.getField(var1.getClass(), var7.getName());
         Object var8 = var7.get(var1);
         if (var8 instanceof NavigationAbstract) {
            var7.set(var1, var2);
         }
      }

   }

   public HitboxEntity createHitbox(Location var1, IJoint var2, SubHitbox var3) {
      WorldServer var4 = ((CraftWorld)var1.getWorld()).getHandle();
      CollisionVolumeEntity var5 = new CollisionVolumeEntity(var4, var2, var3);
      this.initializeHitboxEntity(var5, var1);
      this.scheduleHitboxAddition(var4, var5);
      return var5;
   }

   private void initializeHitboxEntity(CollisionVolumeEntity var1, Location var2) {
      Vector3f var3 = new Vector3f((float)var2.getX(), (float)var2.getY(), (float)var2.getZ());
      var1.queueLocation(var3);
      var1.a_(var2.getX(), var2.getY(), var2.getZ());
      ModelAPI.setRenderCanceled(var1.ar(), true);
   }

   private void scheduleHitboxAddition(WorldServer var1, CollisionVolumeEntity var2) {
      Future.start((Entity)var2.getBukkitEntity()).thenRunSync(() -> {
         var1.b(var2);
         ModelAPI.getInteractionTracker().addHitbox(var2);
      });
   }

   @Nullable
   public HitboxEntity castHitbox(Entity var1) {
      net.minecraft.world.entity.Entity var2 = EntityConversionUtil.toNMS(var1);
      return var2 instanceof HitboxEntity ? (HitboxEntity)var2 : null;
   }

   public boolean hurt(Entity var1, Object var2, float var3) {
      if (var2 instanceof DamageSource) {
         DamageSource var4 = (DamageSource)var2;
         net.minecraft.world.entity.Entity var5 = EntityConversionUtil.toNMS(var1);
         World var6 = var5.ai();
         if (var6 instanceof WorldServer) {
            WorldServer var7 = (WorldServer)var6;
            return var5.a(var7, var4, var3);
         } else {
            return false;
         }
      } else {
         throw new RuntimeException("Passed in source is not an NMS DamageSource.");
      }
   }

   public EntityHandler.InteractionResult interact(Entity var1, HumanEntity var2, EquipmentSlot var3) {
      net.minecraft.world.entity.Entity var4 = EntityConversionUtil.toNMS(var1);
      if (var4 instanceof EntityLiving) {
         EntityLiving var5 = (EntityLiving)var4;
         EnumHand var6 = var3 == EquipmentSlot.HAND ? EnumHand.a : EnumHand.b;
         EnumInteractionResult var7 = var5.a(((CraftPlayer)var2).getHandle(), var6);
         return this.convertInteractionResult(var7);
      } else {
         return EntityHandler.InteractionResult.FAIL;
      }
   }

   private EntityHandler.InteractionResult convertInteractionResult(EnumInteractionResult var1) {
      EntityHandler.InteractionResult var10000;
      switch(var1.hashCode()) {
      case 0:
         d var2 = (d)var1;
         var10000 = var2.equals(EnumInteractionResult.b) ? EntityHandler.InteractionResult.SUCCESS : (var2.equals(EnumInteractionResult.a) ? EntityHandler.InteractionResult.CONSUME_PARTIAL : EntityHandler.InteractionResult.CONSUME);
         break;
      case 1:
         var10000 = EntityHandler.InteractionResult.SUCCESS_NO_ITEM_USED;
         break;
      case 2:
         var10000 = EntityHandler.InteractionResult.PASS;
         break;
      case 3:
         var10000 = EntityHandler.InteractionResult.FAIL;
         break;
      default:
         throw new RuntimeException();
      }

      return var10000;
   }

   public void spawnDynamicHitbox(DynamicHitbox var1) {
      Vector var2 = (Vector)var1.getPositionTracker().get();
      EntityManagementSystem.DynamicHitboxSpawner var3 = new EntityManagementSystem.DynamicHitboxSpawner(var2);
      var3.sendToPlayer(var1.getPlayer());
   }

   public void updateDynamicHitbox(DynamicHitbox var1) {
      Vector3f var2 = ((Vector)var1.getPositionTracker().get()).toVector3f().add(0.0F, -0.5202F, 0.0F);
      PacketTransmissionUtility.transmitToPlayer(var1.getPlayer().getUniqueId(), PacketTransmissionUtility.generateAnchorRelocation(DynamicHitbox.getPivotId(), var2).createPacket(var1.getPlayer().getUniqueId()));
   }

   public void destroyDynamicHitbox(DynamicHitbox var1) {
      PacketPlayOutEntityDestroy var2 = new PacketPlayOutEntityDestroy(new int[]{DynamicHitbox.getHitboxId(), DynamicHitbox.getPivotId()});
      PacketTransmissionUtility.transmitToPlayer(var1.getPlayer().getUniqueId(), var2);
   }

   public void forceUseItem(Player var1, EquipmentSlot var2) {
      EntityManagementSystem.ItemUsageSimulator var3 = new EntityManagementSystem.ItemUsageSimulator(var1, var2);
      var3.simulateUsage();
   }

   public float getYRot(Entity var1) {
      return EntityConversionUtil.toNMS(var1).dP();
   }

   public float getYHeadRot(Entity var1) {
      net.minecraft.world.entity.Entity var2 = EntityConversionUtil.toNMS(var1);
      float var10000;
      if (var2 instanceof EntityLiving) {
         EntityLiving var3 = (EntityLiving)var2;
         var10000 = var3.cE();
      } else {
         var10000 = var2.dP();
      }

      return var10000;
   }

   public float getXHeadRot(Entity var1) {
      return EntityConversionUtil.toNMS(var1).dR();
   }

   public float getYBodyRot(Entity var1) {
      net.minecraft.world.entity.Entity var2 = EntityConversionUtil.toNMS(var1);
      float var10000;
      if (var2 instanceof EntityLiving) {
         EntityLiving var3 = (EntityLiving)var2;
         var10000 = var3.br;
      } else {
         var10000 = var2.dP();
      }

      return var10000;
   }

   public void setYRot(Entity var1, float var2) {
      EntityConversionUtil.toNMS(var1).v(var2);
   }

   public void setYHeadRot(Entity var1, float var2) {
      net.minecraft.world.entity.Entity var3 = EntityConversionUtil.toNMS(var1);
      if (var3 instanceof EntityLiving) {
         EntityLiving var4 = (EntityLiving)var3;
         var4.r(var2);
      } else {
         var3.v(var2);
      }

   }

   public void setXHeadRot(Entity var1, float var2) {
      EntityConversionUtil.toNMS(var1).w(var2);
   }

   public void setYBodyRot(Entity var1, float var2) {
      net.minecraft.world.entity.Entity var3 = EntityConversionUtil.toNMS(var1);
      if (var3 instanceof EntityLiving) {
         EntityLiving var4 = (EntityLiving)var3;
         var4.s(var2);
      } else {
         var3.v(var2);
      }

   }

   public void move(Entity var1, double var2, double var4, double var6) {
      EntityConversionUtil.toNMS(var1).a(EnumMoveType.a, new Vec3D(var2, var4, var6));
   }

   public boolean isWalking(Entity var1) {
      net.minecraft.world.entity.Entity var2 = EntityConversionUtil.toNMS(var1);
      if (var2.as < 1) {
         return false;
      } else {
         double var3 = var2.dC() - var2.an;
         double var5 = var2.dI() - var2.ap;
         return var3 * var3 + var5 * var5 > 2.500000277905201E-7D;
      }
   }

   public boolean isStrafing(Entity var1) {
      return false;
   }

   public boolean isJumping(Entity var1) {
      net.minecraft.world.entity.Entity var2 = EntityConversionUtil.toNMS(var1);
      if (!(var2 instanceof EntityLiving)) {
         return false;
      } else {
         EntityLiving var3 = (EntityLiving)var2;
         Boolean var4 = (Boolean)ReflectionUtils.get(var3, ReflectionFieldCatalog.JUMP_STATUS, false);
         return var4 != null && var4;
      }
   }

   public boolean isFlying(Entity var1) {
      return false;
   }

   public float getHealth(Entity var1) {
      net.minecraft.world.entity.Entity var2 = EntityConversionUtil.toNMS(var1);
      float var10000;
      if (var2 instanceof EntityLiving) {
         EntityLiving var3 = (EntityLiving)var2;
         var10000 = var3.eL();
      } else {
         var10000 = 20.0F;
      }

      return var10000;
   }

   public float getMaxHealth(Entity var1) {
      net.minecraft.world.entity.Entity var2 = EntityConversionUtil.toNMS(var1);
      float var10000;
      if (var2 instanceof EntityLiving) {
         EntityLiving var3 = (EntityLiving)var2;
         var10000 = var3.fa();
      } else {
         var10000 = 20.0F;
      }

      return var10000;
   }

   public boolean isRemoved(Entity var1) {
      return EntityConversionUtil.toNMS(var1).dU();
   }

   public int getGlowColor(Entity var1) {
      return EntityConversionUtil.toNMS(var1).m_();
   }

   public void setDeathTick(Entity var1, int var2) {
      net.minecraft.world.entity.Entity var3 = EntityConversionUtil.toNMS(var1);
      if (var3 instanceof EntityLiving) {
         EntityLiving var4 = (EntityLiving)var3;
         var4.bl = var2;
      }

   }

   public TrackedEntity wrapTrackedEntity(Entity var1) {
      WorldServer var2 = ((CraftWorld)var1.getWorld()).getHandle();
      Int2ObjectMap var3 = var2.n().a.L;
      EntityTracker var4 = (EntityTracker)var3.get(var1.getEntityId());
      return (TrackedEntity)(var4 == null ? new TempTrackedEntity(var1) : new EntityTrackingWrapper(var1, () -> {
         return (EntityTracker)var3.get(var1.getEntityId());
      }, var4));
   }

   public boolean shouldCull(Player var1, Location var2, Entity var3, BoundingBox var4) {
      if (this.shouldBypassCulling(var4)) {
         return false;
      } else {
         CraftWorld var5 = (CraftWorld)var1.getWorld();
         Vec3D var6 = new Vec3D(var2.getX(), var2.getY(), var2.getZ());
         EntityManagementSystem.OcclusionAnalyzer var7 = new EntityManagementSystem.OcclusionAnalyzer(var5, var6, var4);
         return var7.isOccluded();
      }
   }

   private boolean shouldBypassCulling(BoundingBox var1) {
      return var1.getWidthX() >= this.overrideRenderWidth || var1.getWidthZ() >= this.overrideRenderWidth || var1.getHeight() >= this.overrideRenderHeight;
   }

   static {
      ID_GENERATOR = (AtomicInteger)ReflectionUtils.get(ReflectionFieldCatalog.COUNTER_FOR_ENTITIES);
   }

   private static class DynamicHitboxSpawner {
      private final Vector location;

      DynamicHitboxSpawner(Vector var1) {
         this.location = var1;
      }

      void sendToPlayer(Player var1) {
         PacketBundleProvider var2 = this.createSpawnPackets();
         PacketTransmissionUtility.deliverPacketBundle(var1.getUniqueId(), var2);
      }

      private PacketBundleProvider createSpawnPackets() {
         PacketBundleProvider var1 = new PacketBundleProvider();
         Vector3f var2 = this.location.toVector3f().add(0.0F, -0.5202F, 0.0F);
         var1.add((var1x) -> {
            return PacketTransmissionUtility.generateAnchorSpawn(DynamicHitbox.getPivotId(), DynamicHitbox.getPivotUUID(), var2).createPacket(var1x);
         });
         var1.add((var0) -> {
            return new PacketPlayOutEntityMetadata(DynamicHitbox.getPivotId(), EntityDataConstants.AREA_EFFECT_CLOUD_DATA);
         });
         var1.add((var1x) -> {
            return new PacketPlayOutSpawnEntity(DynamicHitbox.getHitboxId(), DynamicHitbox.getHitboxUUID(), this.location.getX(), this.location.getY() - 0.5202D, this.location.getZ(), 0.0F, 0.0F, EntityTypes.bj, 0, Vec3D.c, 0.0D);
         });
         var1.add((var0) -> {
            return new PacketPlayOutEntityMetadata(DynamicHitbox.getHitboxId(), EntityDataConstants.SLIME_DATA);
         });
         var1.add((var0) -> {
            return new PacketPlayOutMount(EntityRelationship.of(DynamicHitbox.getPivotId(), DynamicHitbox.getHitboxId()));
         });
         return var1;
      }
   }

   private static class ItemUsageSimulator {
      private final Player player;
      private final EquipmentSlot slot;
      private final EntityPlayer nmsPlayer;
      private final ItemStack nmsItem;

      ItemUsageSimulator(Player var1, EquipmentSlot var2) {
         this.player = var1;
         this.slot = var2;
         this.nmsPlayer = (EntityPlayer)EntityConversionUtil.toNMS(var1);
         this.nmsItem = CraftItemStack.asNMSCopy(var1.getEquipment().getItem(var2));
      }

      void simulateUsage() {
         EnumHand var1 = this.slot == EquipmentSlot.HAND ? EnumHand.a : EnumHand.b;
         PacketPlayInBlockPlace var2 = new PacketPlayInBlockPlace(var1, 0, 0.0F, 0.0F);
         var2.timestamp = System.currentTimeMillis();
         ServerInteractionProcessor.handleUseItem(var2, this.nmsPlayer.g, (var2x) -> {
            this.processUsageResult(var2x, var1);
         });
      }

      private void processUsageResult(EnumInteractionResult var1, EnumHand var2) {
         if (this.shouldShowUsageAnimation(var1)) {
            this.sendUsageAnimation(var2);
            this.handleInstrumentSound();
         }

      }

      private boolean shouldShowUsageAnimation(EnumInteractionResult var1) {
         return this.nmsItem.w() != ItemUseAnimation.a && var1 == EnumInteractionResult.c;
      }

      private void sendUsageAnimation(EnumHand var1) {
         byte var2 = (byte)(var1 == EnumHand.a ? 1 : 3);
         PacketPlayOutEntityMetadata var3 = new PacketPlayOutEntityMetadata(this.player.getEntityId(), List.of(new c(8, DataWatcherRegistry.a, var2)));
         PacketTransmissionUtility.transmitToPlayer(this.player.getUniqueId(), var3);
      }

      private void handleInstrumentSound() {
         Item var1 = this.nmsItem.h();
         if (var1 instanceof InstrumentItem) {
            InstrumentItem var2 = (InstrumentItem)var1;
            this.playInstrumentSound(var2);
         }

      }

      private void playInstrumentSound(InstrumentItem var1) {
         Optional var2 = (Optional)ReflectionUtils.call(var1, ReflectionMethodCatalog.RETRIEVE_INSTRUMENT_DATA, this.nmsItem, this.nmsPlayer.eb());
         var2.ifPresent((var1x) -> {
            Instrument var2 = (Instrument)var1x.a();
            this.sendInstrumentSound(var2);
         });
      }

      private void sendInstrumentSound(Instrument var1) {
         Holder var2 = var1.a();
         float var3 = var1.c() / 16.0F;
         RandomSource var4 = (RandomSource)ReflectionUtils.get(this.nmsPlayer.y(), ReflectionFieldCatalog.SAFE_RANDOM_SOURCE);
         PacketPlayOutEntitySound var5 = new PacketPlayOutEntitySound(var2, SoundCategory.c, this.nmsPlayer, var3, 1.0F, var4.g());
         PacketTransmissionUtility.transmitToPlayer(this.player.getUniqueId(), var5);
      }
   }

   private static class OcclusionAnalyzer {
      private final CraftWorld world;
      private final Vec3D viewPoint;
      private final BoundingBox box;

      OcclusionAnalyzer(CraftWorld var1, Vec3D var2, BoundingBox var3) {
         this.world = var1;
         this.viewPoint = var2;
         this.box = var3;
      }

      boolean isOccluded() {
         EntityManagementSystem.OcclusionAnalyzer.BoxBounds var1 = new EntityManagementSystem.OcclusionAnalyzer.BoxBounds(this.box);
         EntityManagementSystem.OcclusionAnalyzer.BoxRelativePosition var2 = this.calculateRelativePosition(var1);
         if (var2.isInside()) {
            return false;
         } else {
            Set var3 = this.generateTestPoints(var1, var2);
            if (DebugToggle.isDebugging(DebugToggle.SHOW_CULL_POINTS)) {
               this.visualizeTestPoints(var3);
            }

            return !this.hasVisiblePoint(var3);
         }
      }

      private EntityManagementSystem.OcclusionAnalyzer.BoxRelativePosition calculateRelativePosition(EntityManagementSystem.OcclusionAnalyzer.BoxBounds var1) {
         EntityHandler.BoxRelToCam var2 = EntityHandler.BoxRelToCam.from(var1.minX, var1.maxX, MathHelper.a(this.viewPoint.d));
         EntityHandler.BoxRelToCam var3 = EntityHandler.BoxRelToCam.from(var1.minY, var1.maxY, MathHelper.a(this.viewPoint.e));
         EntityHandler.BoxRelToCam var4 = EntityHandler.BoxRelToCam.from(var1.minZ, var1.maxZ, MathHelper.a(this.viewPoint.f));
         return new EntityManagementSystem.OcclusionAnalyzer.BoxRelativePosition(var2, var3, var4);
      }

      private Set<Vec3D> generateTestPoints(EntityManagementSystem.OcclusionAnalyzer.BoxBounds var1, EntityManagementSystem.OcclusionAnalyzer.BoxRelativePosition var2) {
         LinkedHashSet var3 = new LinkedHashSet();

         for(int var4 = var1.minX; var4 <= var1.maxX; ++var4) {
            for(int var5 = var1.minY; var5 <= var1.maxY; ++var5) {
               for(int var6 = var1.minZ; var6 <= var1.maxZ; ++var6) {
                  byte var7 = this.calculateVisibleFaces(var4, var5, var6, var1, var2);
                  if (var7 != 0) {
                     this.addFacePoints(var3, var4, var5, var6, var7);
                  }
               }
            }
         }

         return var3;
      }

      private byte calculateVisibleFaces(int var1, int var2, int var3, EntityManagementSystem.OcclusionAnalyzer.BoxBounds var4, EntityManagementSystem.OcclusionAnalyzer.BoxRelativePosition var5) {
         byte var6 = 0;
         if (var1 == var4.minX && var5.relX == EntityHandler.BoxRelToCam.POSITIVE) {
            var6 = (byte)(var6 | 1);
         }

         if (var1 == var4.maxX && var5.relX == EntityHandler.BoxRelToCam.NEGATIVE) {
            var6 = (byte)(var6 | 2);
         }

         if (var2 == var4.minY && var5.relY == EntityHandler.BoxRelToCam.POSITIVE) {
            var6 = (byte)(var6 | 4);
         }

         if (var2 == var4.maxY && var5.relY == EntityHandler.BoxRelToCam.NEGATIVE) {
            var6 = (byte)(var6 | 8);
         }

         if (var3 == var4.minZ && var5.relZ == EntityHandler.BoxRelToCam.POSITIVE) {
            var6 = (byte)(var6 | 16);
         }

         if (var3 == var4.maxZ && var5.relZ == EntityHandler.BoxRelToCam.NEGATIVE) {
            var6 = (byte)(var6 | 32);
         }

         return var6;
      }

      private void addFacePoints(Set<Vec3D> var1, int var2, int var3, int var4, byte var5) {
         Iterator var6 = EntityHandler.getPoints(var5).iterator();

         while(var6.hasNext()) {
            EntityHandler.Point var7 = (EntityHandler.Point)var6.next();
            var1.add(new Vec3D((double)((float)var2 + var7.x), (double)((float)var3 + var7.y), (double)((float)var4 + var7.z)));
         }

      }

      private void visualizeTestPoints(Set<Vec3D> var1) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Vec3D var3 = (Vec3D)var2.next();
            this.world.spawnParticle(Particle.DUST, var3.d, var3.e, var3.f, 1, new DustOptions(Color.RED, 0.2F));
         }

      }

      private boolean hasVisiblePoint(Set<Vec3D> var1) {
         Iterator var2 = var1.iterator();

         Vec3D var3;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            var3 = (Vec3D)var2.next();
         } while(!this.isPointVisible(var3));

         return true;
      }

      private boolean isPointVisible(Vec3D var1) {
         MovingObjectPositionBlock var2 = this.world.getHandle().a(new VisibilityCheckContext(this.viewPoint, var1));
         return var2.d() == EnumMovingObjectType.a;
      }

      private static class BoxBounds {
         final int minX;
         final int minY;
         final int minZ;
         final int maxX;
         final int maxY;
         final int maxZ;

         BoxBounds(BoundingBox var1) {
            this.minX = MathHelper.a(var1.getMinX());
            this.minY = MathHelper.a(var1.getMinY());
            this.minZ = MathHelper.a(var1.getMinZ());
            this.maxX = MathHelper.c(var1.getMaxX()) - 1;
            this.maxY = MathHelper.c(var1.getMaxY()) - 1;
            this.maxZ = MathHelper.c(var1.getMaxZ()) - 1;
         }
      }

      private static class BoxRelativePosition {
         final EntityHandler.BoxRelToCam relX;
         final EntityHandler.BoxRelToCam relY;
         final EntityHandler.BoxRelToCam relZ;

         BoxRelativePosition(EntityHandler.BoxRelToCam var1, EntityHandler.BoxRelToCam var2, EntityHandler.BoxRelToCam var3) {
            this.relX = var1;
            this.relY = var2;
            this.relZ = var3;
         }

         boolean isInside() {
            return this.relX == EntityHandler.BoxRelToCam.INSIDE && this.relY == EntityHandler.BoxRelToCam.INSIDE && this.relZ == EntityHandler.BoxRelToCam.INSIDE;
         }
      }
   }
}
