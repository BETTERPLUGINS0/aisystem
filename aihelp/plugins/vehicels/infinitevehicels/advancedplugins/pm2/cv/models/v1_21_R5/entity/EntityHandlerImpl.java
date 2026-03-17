package advancedplugins.pm2.cv.models.v1_21_R5.entity;

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
import advancedplugins.pm2.cv.models.v1_21_R5.NMSFields;
import advancedplugins.pm2.cv.models.v1_21_R5.NMSMethods;
import advancedplugins.pm2.cv.models.v1_21_R5.entity.controller.BodyRotationControlWrapper;
import advancedplugins.pm2.cv.models.v1_21_R5.entity.controller.LookControlWrapper;
import advancedplugins.pm2.cv.models.v1_21_R5.entity.controller.MoveControlWrapper;
import advancedplugins.pm2.cv.models.v1_21_R5.entity.hitbox.HitboxEntityImpl;
import advancedplugins.pm2.cv.models.v1_21_R5.entity.navigation.AmphibiousNavigationWrapper;
import advancedplugins.pm2.cv.models.v1_21_R5.entity.navigation.FlyingNavigationWrapper;
import advancedplugins.pm2.cv.models.v1_21_R5.entity.navigation.GroundNavigationWrapper;
import advancedplugins.pm2.cv.models.v1_21_R5.entity.navigation.WallClimberNavigationWrapper;
import advancedplugins.pm2.cv.models.v1_21_R5.entity.navigation.WaterBoundNavigationWrapper;
import advancedplugins.pm2.cv.models.v1_21_R5.network.patch.PatchedServerGamePacketListener;
import advancedplugins.pm2.cv.models.v1_21_R5.network.utils.NetworkUtils;
import advancedplugins.pm2.cv.models.v1_21_R5.network.utils.Packets;
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
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData.DataValue;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult.Fail;
import net.minecraft.world.InteractionResult.Pass;
import net.minecraft.world.InteractionResult.Success;
import net.minecraft.world.InteractionResult.TryEmptyHandInteraction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityAttachments;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class EntityHandlerImpl implements EntityHandler {
   private static final AtomicInteger ENTITY_COUNTER;
   private static boolean usePaperClipMethod;
   private final Set<UUID> forceInvisible = new HashSet();
   private double forceRenderWidth;
   private double forceRenderHeight;
   private ArmorStand dummyArmorStand;

   public EntityHandlerImpl() {
      ModelAPI.getAPI().getConfigManager().registerReferenceUpdate(this::updateConfig);
   }

   private static boolean isVisible(CraftWorld var0, Vec3 var1, Vec3 var2) {
      BlockHitResult var3 = usePaperClipMethod ? var0.getHandle().clip(new OcclusionClipContext(var1, var2)) : var0.getHandle().clip(new OcclusionClipContext(var1, var2), (Predicate)null);
      return var3.getType() == Type.MISS;
   }

   public void updateConfig() {
      this.forceRenderWidth = ConfigProperty.BLOCK_CULL_IGNORE_SIZE_WIDTH.getDouble();
      this.forceRenderHeight = ConfigProperty.BLOCK_CULL_IGNORE_SIZE_HEIGHT.getDouble();
      usePaperClipMethod = ConfigProperty.BLOCK_CULL_USE_PAPER_CLIP.getBoolean();
   }

   public int getNextEntityId() {
      return ENTITY_COUNTER == null ? 0 : ENTITY_COUNTER.incrementAndGet();
   }

   public void setHitbox(Entity var1, @NotNull Hitbox var2) {
      float var3 = (float)var2.getMaxWidth();
      float var4 = (float)var2.getHeight();
      net.minecraft.world.entity.Entity var5 = EntityUtils.nms(var1);
      EntityDimensions var6 = new EntityDimensions(var3, var4, (float)var2.getEyeHeight(), EntityAttachments.createDefault(var3, var4), true);
      ReflectionUtils.set(var5, NMSFields.ENTITY_dimensions, var6);
      ReflectionUtils.set(var5, NMSFields.ENTITY_eyeHeight, (float)var2.getEyeHeight());
      var5.setBoundingBox(var6.makeBoundingBox(var5.position()));
   }

   public void setStepHeight(Entity var1, double var2) {
      net.minecraft.world.entity.Entity var4 = EntityUtils.nms(var1);
      if (var4 instanceof LivingEntity) {
         LivingEntity var5 = (LivingEntity)var4;
         var5.getAttributes().registerAttribute(Attributes.STEP_HEIGHT);
         ((AttributeInstance)Objects.requireNonNull(var5.getAttribute(Attributes.STEP_HEIGHT))).setBaseValue(var2);
      }

   }

   public double getStepHeight(Entity var1) {
      return (double)EntityUtils.nms(var1).maxUpStep();
   }

   public void setPosition(Entity var1, double var2, double var4, double var6) {
      EntityUtils.nms(var1).setPos(var2, var4, var6);
   }

   public void movePassenger(Entity var1, double var2, double var4, double var6) {
      net.minecraft.world.entity.Entity var8 = EntityUtils.nms(var1);
      if (this.dummyArmorStand == null) {
         this.dummyArmorStand = new ArmorStand(EntityType.ARMOR_STAND, var8.level());
         this.dummyArmorStand.setMarker(true);
      }

      double var9 = var4 - var8.getVehicleAttachmentPoint(this.dummyArmorStand).y;
      var8.setPos(var2, var9, var6);
      var8.setDeltaMovement(Vec3.ZERO);
      var8.resetFallDistance();
      if (var8 instanceof ServerPlayer) {
         ServerPlayer var11 = (ServerPlayer)var8;
         ReflectionUtils.set(var11.connection, NMSFields.SERVER_GAME_PACKET_LISTENER_IMPL_clientIsFloating, false);
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
         NetworkUtils.send((UUID)var2.getUniqueId(), new ClientboundRemoveEntitiesPacket(new int[]{var1.getEntityId()}));
      }

   }

   public void setForcedInvisible(Player var1, boolean var2) {
      if (this.isForcedInvisible(var1) != var2) {
         net.minecraft.world.entity.Entity var3 = EntityUtils.nms(var1);
         byte var4 = 0;

         for(int var5 = 0; var5 < 8; ++var5) {
            var4 = MathUtils.setBit(var4, var5, var3.getSharedFlag(var5));
         }

         if (var2) {
            this.forceInvisible.add(var1.getUniqueId());
            var4 = MathUtils.setBit(var4, 5, true);
         } else {
            this.forceInvisible.remove(var1.getUniqueId());
         }

         ClientboundSetEntityDataPacket var6 = new ClientboundSetEntityDataPacket(var1.getEntityId(), List.of(new DataValue(0, EntityDataSerializers.BYTE, var4)));
         NetworkUtils.send((UUID)var1.getUniqueId(), var6);
      }

   }

   public boolean isForcedInvisible(Player var1) {
      return this.forceInvisible.contains(var1.getUniqueId());
   }

   public BodyRotationController wrapBodyRotationControl(Entity var1, Supplier<BodyRotationController> var2) {
      net.minecraft.world.entity.Entity var3 = EntityUtils.nms(var1);
      if (var3 instanceof Mob) {
         Mob var4 = (Mob)var3;
         BodyRotationControlWrapper var5 = new BodyRotationControlWrapper(var4);
         return (BodyRotationController)(ReflectionUtils.set(var4, NMSFields.MOB_bodyRotationControl, var5) ? var5 : (BodyRotationController)var2.get());
      } else {
         return (BodyRotationController)var2.get();
      }
   }

   public MoveController wrapMoveController(Entity var1, Supplier<MoveController> var2) {
      net.minecraft.world.entity.Entity var3 = EntityUtils.nms(var1);
      if (var3 instanceof Mob) {
         Mob var4 = (Mob)var3;
         MoveControlWrapper var5 = new MoveControlWrapper(var4, var4.getMoveControl());
         return (MoveController)(ReflectionUtils.set(var4, NMSFields.MOB_moveControl, var5) ? var5 : (MoveController)var2.get());
      } else {
         return (MoveController)var2.get();
      }
   }

   public LookController wrapLookController(Entity var1, Supplier<LookController> var2) {
      net.minecraft.world.entity.Entity var3 = EntityUtils.nms(var1);
      if (var3 instanceof Mob) {
         Mob var4 = (Mob)var3;
         LookControlWrapper var5 = new LookControlWrapper(var4, var4.getLookControl());
         return (LookController)(ReflectionUtils.set(var4, NMSFields.MOB_lookControl, var5) ? var5 : (LookController)var2.get());
      } else {
         return (LookController)var2.get();
      }
   }

   public void wrapNavigation(Entity var1) {
      net.minecraft.world.entity.Entity var2 = EntityUtils.nms(var1);
      if (var2 instanceof Mob) {
         Mob var3 = (Mob)var2;

         try {
            Field var4 = ReflectionUtils.getField(NMSFields.MOB_navigation);
            PathNavigation var5 = var3.getNavigation();
            Object var6;
            if (var5 instanceof WallClimberNavigation) {
               WallClimberNavigation var7 = (WallClimberNavigation)var5;
               var6 = new WallClimberNavigationWrapper(var3, var7);
            } else if (var5 instanceof GroundPathNavigation) {
               GroundPathNavigation var9 = (GroundPathNavigation)var5;
               var6 = new GroundNavigationWrapper(var3, var9);
            } else if (var5 instanceof FlyingPathNavigation) {
               FlyingPathNavigation var10 = (FlyingPathNavigation)var5;
               var6 = new FlyingNavigationWrapper(var3, var10);
            } else if (var5 instanceof WaterBoundPathNavigation) {
               var6 = new WaterBoundNavigationWrapper(var3);
            } else {
               if (!(var5 instanceof AmphibiousPathNavigation)) {
                  String var13 = String.valueOf(var3.getType());
                  LogUtil.warn("Failed to create custom navigation for " + var13 + ": " + String.valueOf(var3.getUUID()));
                  LogUtil.warn("Reason: Navigation class type is " + var5.getClass().getSimpleName() + ".");
                  return;
               }

               AmphibiousPathNavigation var11 = (AmphibiousPathNavigation)var5;
               var6 = new AmphibiousNavigationWrapper(var3, var11);
            }

            var4.set(var3, var6);
            GoalSelector var12 = (GoalSelector)ReflectionUtils.getField(NMSFields.MOB_goalSelector).get(var3);
            RaceConditionUtil.wrapConmod(() -> {
               this.replaceNavigation(var12, var6);
            });
         } catch (IllegalAccessException var8) {
            var8.printStackTrace();
         }
      }

   }

   private void replaceNavigation(GoalSelector var1, PathNavigation var2) {
      try {
         Iterator var3 = var1.getAvailableGoals().iterator();

         while(var3.hasNext()) {
            WrappedGoal var4 = (WrappedGoal)var3.next();
            Goal var5 = var4.getGoal();
            Field[] var6 = var5.getClass().getDeclaredFields();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               Field var9 = var6[var8];
               var9 = ReflectionUtils.getField(var5.getClass(), var9.getName());
               Object var10 = var9.get(var5);
               if (var10 instanceof PathNavigation) {
                  var9.set(var5, var2);
               }
            }
         }
      } catch (IllegalAccessException var11) {
         var11.printStackTrace();
      }

   }

   public HitboxEntity createHitbox(Location var1, IJoint var2, SubHitbox var3) {
      ServerLevel var4 = ((CraftWorld)var1.getWorld()).getHandle();
      HitboxEntityImpl var5 = new HitboxEntityImpl(var4, var2, var3);
      var5.queueLocation((new Vector3f()).set(var1.getX(), var1.getY(), var1.getZ()));
      var5.setPos(var1.getX(), var1.getY(), var1.getZ());
      ModelAPI.setRenderCanceled(var5.getId(), true);
      Future.start((Entity)var5.getBukkitEntity()).thenRunSync(() -> {
         var4.addFreshEntity(var5);
         ModelAPI.getInteractionTracker().addHitbox(var5);
      });
      return var5;
   }

   @Nullable
   public HitboxEntity castHitbox(Entity var1) {
      net.minecraft.world.entity.Entity var2 = EntityUtils.nms(var1);
      HitboxEntity var3;
      if (var2 instanceof HitboxEntity) {
         HitboxEntity var4 = (HitboxEntity)var2;
         var3 = var4;
      } else {
         var3 = null;
      }

      return var3;
   }

   public boolean hurt(Entity var1, Object var2, float var3) {
      if (var2 instanceof DamageSource) {
         DamageSource var4 = (DamageSource)var2;
         net.minecraft.world.entity.Entity var5 = EntityUtils.nms(var1);
         Level var6 = var5.level();
         if (var6 instanceof ServerLevel) {
            ServerLevel var7 = (ServerLevel)var6;
            return var5.hurtServer(var7, var4, var3);
         }
      }

      throw new RuntimeException("Passed in source is not an NMS DamageSource.");
   }

   public EntityHandler.InteractionResult interact(Entity var1, HumanEntity var2, EquipmentSlot var3) {
      net.minecraft.world.entity.Entity var4 = EntityUtils.nms(var1);
      if (var4 instanceof LivingEntity) {
         LivingEntity var5 = (LivingEntity)var4;
         net.minecraft.world.InteractionResult var6 = var5.interact(((CraftPlayer)var2).getHandle(), var3 == EquipmentSlot.HAND ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
         Objects.requireNonNull(var6);
         boolean var7 = false;
         EntityHandler.InteractionResult var10000;
         switch(var6.hashCode()) {
         case 0:
            Success var11 = (Success)var6;
            var10000 = var11.equals(net.minecraft.world.InteractionResult.SUCCESS_SERVER) ? EntityHandler.InteractionResult.SUCCESS : (var11.equals(net.minecraft.world.InteractionResult.SUCCESS) ? EntityHandler.InteractionResult.CONSUME_PARTIAL : EntityHandler.InteractionResult.CONSUME);
            break;
         case 1:
            TryEmptyHandInteraction var10 = (TryEmptyHandInteraction)var6;
            var10000 = EntityHandler.InteractionResult.SUCCESS_NO_ITEM_USED;
            break;
         case 2:
            Pass var9 = (Pass)var6;
            var10000 = EntityHandler.InteractionResult.PASS;
            break;
         case 3:
            Fail var8 = (Fail)var6;
            var10000 = EntityHandler.InteractionResult.FAIL;
            break;
         default:
            throw new RuntimeException();
         }

         return var10000;
      } else {
         return EntityHandler.InteractionResult.FAIL;
      }
   }

   public void spawnDynamicHitbox(DynamicHitbox var1) {
      Vector var2 = (Vector)var1.getPositionTracker().get();
      final Packets.PacketSupplier var3 = NetworkUtils.createPivotSpawn(DynamicHitbox.getPivotId(), DynamicHitbox.getPivotUUID(), var2.toVector3f().add(0.0F, -0.5202F, 0.0F));
      final ClientboundSetEntityDataPacket var4 = new ClientboundSetEntityDataPacket(DynamicHitbox.getPivotId(), EntityUtils.DEFAULT_AREA_EFFECT_CLOUD_DATA);
      final ClientboundAddEntityPacket var5 = new ClientboundAddEntityPacket(DynamicHitbox.getHitboxId(), DynamicHitbox.getHitboxUUID(), var2.getX(), var2.getY() - 0.5202D, var2.getZ(), 0.0F, 0.0F, EntityType.SLIME, 0, Vec3.ZERO, 0.0D);
      final ClientboundSetEntityDataPacket var6 = new ClientboundSetEntityDataPacket(DynamicHitbox.getHitboxId(), EntityUtils.DEFAULT_SLIME_DATA);
      final ClientboundSetPassengersPacket var7 = new ClientboundSetPassengersPacket(EntityContainer.of(DynamicHitbox.getPivotId(), DynamicHitbox.getHitboxId()));
      NetworkUtils.sendBundled(Set.of(var1.getPlayer().getUniqueId()), new Packets() {
         {
            this.add(var3);
            this.add(var4);
            this.add(var5);
            this.add(var6);
            this.add(var7);
         }
      });
   }

   public void updateDynamicHitbox(DynamicHitbox var1) {
      Vector3f var2 = ((Vector)var1.getPositionTracker().get()).toVector3f().add(0.0F, -0.5202F, 0.0F);
      NetworkUtils.send(var1.getPlayer().getUniqueId(), NetworkUtils.createPivotTeleport(DynamicHitbox.getPivotId(), var2).supply(var1.getPlayer().getUniqueId()));
   }

   public void destroyDynamicHitbox(DynamicHitbox var1) {
      ClientboundRemoveEntitiesPacket var2 = new ClientboundRemoveEntitiesPacket(new int[]{DynamicHitbox.getHitboxId(), DynamicHitbox.getPivotId()});
      NetworkUtils.send((UUID)var1.getPlayer().getUniqueId(), var2);
   }

   public void forceUseItem(Player var1, EquipmentSlot var2) {
      ItemStack var3 = var1.getEquipment().getItem(var2);
      net.minecraft.world.item.ItemStack var4 = ((CraftItemStack)var3).handle;
      ServerPlayer var5 = (ServerPlayer)EntityUtils.nms(var1);
      ServerGamePacketListenerImpl var6 = var5.connection;
      ServerboundUseItemPacket var7 = new ServerboundUseItemPacket(var2 == EquipmentSlot.HAND ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND, 0, 0.0F, 0.0F);
      var7.timestamp = System.currentTimeMillis();
      PatchedServerGamePacketListener.handleUseItem(var7, var6, (var4x) -> {
         if (var4.getUseAnimation() != ItemUseAnimation.NONE && var4x == net.minecraft.world.InteractionResult.CONSUME) {
            ClientboundSetEntityDataPacket var5x = new ClientboundSetEntityDataPacket(var1.getEntityId(), List.of(new DataValue(8, EntityDataSerializers.BYTE, (byte)(var2 == EquipmentSlot.HAND ? 1 : 3))));
            NetworkUtils.send((UUID)var1.getUniqueId(), var5x);
            Item var6 = var4.getItem();
            if (var6 instanceof InstrumentItem) {
               InstrumentItem var7 = (InstrumentItem)var6;
               Optional var8 = (Optional)ReflectionUtils.call(var7, NMSMethods.INSTRUMENT_ITEM_getInstrument, var4, var5.registryAccess());
               var8.ifPresent((var2x) -> {
                  Instrument var3 = (Instrument)var2x.value();
                  Holder var4 = var3.soundEvent();
                  float var5x = var3.range() / 16.0F;
                  RandomSource var6 = (RandomSource)ReflectionUtils.get(var5.level(), NMSFields.LEVEL_threadSafeRandom);
                  ClientboundSoundEntityPacket var7 = new ClientboundSoundEntityPacket(var4, SoundSource.RECORDS, var5, var5x, 1.0F, var6.nextLong());
                  NetworkUtils.send((UUID)var1.getUniqueId(), var7);
               });
            }
         }

      });
   }

   public float getYRot(Entity var1) {
      return EntityUtils.nms(var1).getYRot();
   }

   public float getYHeadRot(Entity var1) {
      net.minecraft.world.entity.Entity var2 = EntityUtils.nms(var1);
      if (var2 instanceof LivingEntity) {
         LivingEntity var3 = (LivingEntity)var2;
         return var3.getYHeadRot();
      } else {
         return var2.getYRot();
      }
   }

   public float getXHeadRot(Entity var1) {
      return EntityUtils.nms(var1).getXRot();
   }

   public float getYBodyRot(Entity var1) {
      net.minecraft.world.entity.Entity var2 = EntityUtils.nms(var1);
      if (var2 instanceof LivingEntity) {
         LivingEntity var3 = (LivingEntity)var2;
         return var3.yBodyRot;
      } else {
         return var2.getYRot();
      }
   }

   public void setYRot(Entity var1, float var2) {
      EntityUtils.nms(var1).setYRot(var2);
   }

   public void setYHeadRot(Entity var1, float var2) {
      net.minecraft.world.entity.Entity var3 = EntityUtils.nms(var1);
      if (var3 instanceof LivingEntity) {
         LivingEntity var4 = (LivingEntity)var3;
         var4.setYHeadRot(var2);
      } else {
         var3.setYRot(var2);
      }

   }

   public void setXHeadRot(Entity var1, float var2) {
      EntityUtils.nms(var1).setXRot(var2);
   }

   public void setYBodyRot(Entity var1, float var2) {
      net.minecraft.world.entity.Entity var3 = EntityUtils.nms(var1);
      if (var3 instanceof LivingEntity) {
         LivingEntity var4 = (LivingEntity)var3;
         var4.setYBodyRot(var2);
      } else {
         var3.setYRot(var2);
      }

   }

   public void move(Entity var1, double var2, double var4, double var6) {
      net.minecraft.world.entity.Entity var8 = EntityUtils.nms(var1);
      var8.move(MoverType.SELF, new Vec3(var2, var4, var6));
   }

   public boolean isWalking(Entity var1) {
      net.minecraft.world.entity.Entity var2 = EntityUtils.nms(var1);
      if (var2.tickCount < 1) {
         return false;
      } else {
         double var3 = var2.getX() - var2.xOld;
         double var5 = var2.getZ() - var2.zOld;
         return var3 * var3 + var5 * var5 > 2.500000277905201E-7D;
      }
   }

   public boolean isStrafing(Entity var1) {
      return false;
   }

   public boolean isJumping(Entity var1) {
      net.minecraft.world.entity.Entity var2 = EntityUtils.nms(var1);
      if (!(var2 instanceof LivingEntity)) {
         return false;
      } else {
         LivingEntity var3 = (LivingEntity)var2;
         Boolean var4 = (Boolean)ReflectionUtils.get(var3, NMSFields.LIVING_ENTITY_jumping, false);
         return var4 != null && var4;
      }
   }

   public boolean isFlying(Entity var1) {
      return false;
   }

   public float getHealth(Entity var1) {
      net.minecraft.world.entity.Entity var2 = EntityUtils.nms(var1);
      if (var2 instanceof LivingEntity) {
         LivingEntity var3 = (LivingEntity)var2;
         return var3.getHealth();
      } else {
         return 20.0F;
      }
   }

   public float getMaxHealth(Entity var1) {
      net.minecraft.world.entity.Entity var2 = EntityUtils.nms(var1);
      if (var2 instanceof LivingEntity) {
         LivingEntity var3 = (LivingEntity)var2;
         return var3.getMaxHealth();
      } else {
         return 20.0F;
      }
   }

   public boolean isRemoved(Entity var1) {
      return EntityUtils.nms(var1).isRemoved();
   }

   public int getGlowColor(Entity var1) {
      net.minecraft.world.entity.Entity var2 = EntityUtils.nms(var1);
      return var2.getTeamColor();
   }

   public void setDeathTick(Entity var1, int var2) {
      net.minecraft.world.entity.Entity var3 = EntityUtils.nms(var1);
      if (var3 instanceof LivingEntity) {
         LivingEntity var4 = (LivingEntity)var3;
         var4.deathTime = var2;
      }

   }

   public TrackedEntity wrapTrackedEntity(Entity var1) {
      ServerLevel var2 = ((CraftWorld)var1.getWorld()).getHandle();
      Int2ObjectMap var3 = var2.getChunkSource().chunkMap.entityMap;
      net.minecraft.server.level.ChunkMap.TrackedEntity var4 = (net.minecraft.server.level.ChunkMap.TrackedEntity)var3.get(var1.getEntityId());
      return (TrackedEntity)(var4 == null ? new TempTrackedEntity(var1) : new TrackedEntityImpl(var1, () -> {
         return (net.minecraft.server.level.ChunkMap.TrackedEntity)var3.get(var1.getEntityId());
      }, var4));
   }

   public boolean shouldCull(Player var1, Location var2, Entity var3, BoundingBox var4) {
      CraftWorld var5 = (CraftWorld)var1.getWorld();
      Vec3 var6 = CraftLocation.toVec3(var2);
      if (!(var4.getWidthX() >= this.forceRenderWidth) && !(var4.getWidthZ() >= this.forceRenderWidth) && !(var4.getHeight() >= this.forceRenderHeight)) {
         int var7 = Mth.floor(var4.getMinX());
         int var8 = Mth.floor(var4.getMinY());
         int var9 = Mth.floor(var4.getMinZ());
         int var10 = Mth.ceil(var4.getMaxX()) - 1;
         int var11 = Mth.ceil(var4.getMaxY()) - 1;
         int var12 = Mth.ceil(var4.getMaxZ()) - 1;
         EntityHandler.BoxRelToCam var13 = EntityHandler.BoxRelToCam.from(var7, var10, Mth.floor(var6.x));
         EntityHandler.BoxRelToCam var14 = EntityHandler.BoxRelToCam.from(var8, var11, Mth.floor(var6.y));
         EntityHandler.BoxRelToCam var15 = EntityHandler.BoxRelToCam.from(var9, var12, Mth.floor(var6.z));
         if (var13 != EntityHandler.BoxRelToCam.INSIDE || var14 != EntityHandler.BoxRelToCam.INSIDE || var15 != EntityHandler.BoxRelToCam.INSIDE) {
            LinkedHashSet var16 = new LinkedHashSet();

            for(int var17 = var7; var17 <= var10; ++var17) {
               byte var18 = (byte)(var17 == var7 && var13 == EntityHandler.BoxRelToCam.POSITIVE ? 1 : 0);
               var18 = (byte)(var18 | (var17 == var10 && var13 == EntityHandler.BoxRelToCam.NEGATIVE ? 2 : 0));

               for(int var19 = var8; var19 <= var11; ++var19) {
                  byte var20 = (byte)(var18 | (var19 == var8 && var14 == EntityHandler.BoxRelToCam.POSITIVE ? 4 : 0));
                  var20 = (byte)(var20 | (var19 == var11 && var14 == EntityHandler.BoxRelToCam.NEGATIVE ? 8 : 0));

                  for(int var21 = var9; var21 <= var12; ++var21) {
                     byte var22 = (byte)(var20 | (var21 == var9 && var15 == EntityHandler.BoxRelToCam.POSITIVE ? 16 : 0));
                     var22 = (byte)(var22 | (var21 == var12 && var15 == EntityHandler.BoxRelToCam.NEGATIVE ? 32 : 0));
                     if (var22 != 0) {
                        Iterator var23 = EntityHandler.getPoints(var22).iterator();

                        while(var23.hasNext()) {
                           EntityHandler.Point var24 = (EntityHandler.Point)var23.next();
                           var16.add(new Vec3((double)((float)var17 + var24.x), (double)((float)var19 + var24.y), (double)((float)var21 + var24.z)));
                        }
                     }
                  }
               }
            }

            Iterator var25;
            Vec3 var26;
            if (DebugToggle.isDebugging(DebugToggle.SHOW_CULL_POINTS)) {
               var25 = var16.iterator();

               while(var25.hasNext()) {
                  var26 = (Vec3)var25.next();
                  var5.spawnParticle(Particle.DUST, var26.x, var26.y, var26.z, 1, new DustOptions(Color.RED, 0.2F));
               }
            }

            var25 = var16.iterator();

            do {
               if (!var25.hasNext()) {
                  return true;
               }

               var26 = (Vec3)var25.next();
            } while(!isVisible(var5, var6, var26));
         }

         return false;
      } else {
         return false;
      }
   }

   static {
      ENTITY_COUNTER = (AtomicInteger)ReflectionUtils.get(NMSFields.ENTITY_ENTITY_COUNTER);
   }
}
