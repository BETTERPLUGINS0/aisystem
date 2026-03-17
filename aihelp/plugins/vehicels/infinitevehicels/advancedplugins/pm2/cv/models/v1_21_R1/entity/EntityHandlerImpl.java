package advancedplugins.pm2.cv.models.v1_21_R1.entity;

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
import advancedplugins.pm2.cv.models.v1_21_R1.NMSFields;
import advancedplugins.pm2.cv.models.v1_21_R1.NMSMethods;
import advancedplugins.pm2.cv.models.v1_21_R1.entity.controller.BodyRotationControlWrapper;
import advancedplugins.pm2.cv.models.v1_21_R1.entity.controller.LookControlWrapper;
import advancedplugins.pm2.cv.models.v1_21_R1.entity.controller.MoveControlWrapper;
import advancedplugins.pm2.cv.models.v1_21_R1.entity.hitbox.HitboxEntityImpl;
import advancedplugins.pm2.cv.models.v1_21_R1.entity.navigation.AmphibiousNavigationWrapper;
import advancedplugins.pm2.cv.models.v1_21_R1.entity.navigation.FlyingNavigationWrapper;
import advancedplugins.pm2.cv.models.v1_21_R1.entity.navigation.GroundNavigationWrapper;
import advancedplugins.pm2.cv.models.v1_21_R1.entity.navigation.WallClimberNavigationWrapper;
import advancedplugins.pm2.cv.models.v1_21_R1.entity.navigation.WaterBoundNavigationWrapper;
import advancedplugins.pm2.cv.models.v1_21_R1.network.patch.PatchedServerGamePacketListener;
import advancedplugins.pm2.cv.models.v1_21_R1.network.utils.NetworkUtils;
import advancedplugins.pm2.cv.models.v1_21_R1.network.utils.Packets;
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
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.sounds.SoundCategory;
import net.minecraft.util.MathHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.EnumHand;
import net.minecraft.world.EnumInteractionResult;
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
import net.minecraft.world.item.EnumAnimation;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.MovingObjectPositionBlock;
import net.minecraft.world.phys.Vec3D;
import net.minecraft.world.phys.MovingObjectPosition.EnumMovingObjectType;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_21_R1.util.CraftLocation;
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
   private EntityArmorStand dummyArmorStand;

   public EntityHandlerImpl() {
      ModelAPI.getAPI().getConfigManager().registerReferenceUpdate(this::updateConfig);
   }

   private static boolean isVisible(CraftWorld world, Vec3D startPos, Vec3D endPos) {
      MovingObjectPositionBlock var3 = usePaperClipMethod ? var0.getHandle().a(new OcclusionClipContext(var1, var2)) : var0.getHandle().clip(new OcclusionClipContext(var1, var2), (Predicate)null);
      return var3.c() == EnumMovingObjectType.a;
   }

   public void updateConfig() {
      this.forceRenderWidth = ConfigProperty.BLOCK_CULL_IGNORE_SIZE_WIDTH.getDouble();
      this.forceRenderHeight = ConfigProperty.BLOCK_CULL_IGNORE_SIZE_HEIGHT.getDouble();
      usePaperClipMethod = ConfigProperty.BLOCK_CULL_USE_PAPER_CLIP.getBoolean();
   }

   public int getNextEntityId() {
      return ENTITY_COUNTER == null ? 0 : ENTITY_COUNTER.incrementAndGet();
   }

   public void setHitbox(Entity entity, @NotNull Hitbox hitbox) {
      float var3 = (float)var2.getMaxWidth();
      float var4 = (float)var2.getHeight();
      net.minecraft.world.entity.Entity var5 = EntityUtils.nms(var1);
      EntitySize var6 = new EntitySize(var3, var4, (float)var2.getEyeHeight(), EntityAttachments.a(var3, var4), true);
      ReflectionUtils.set(var5, NMSFields.ENTITY_dimensions, var6);
      ReflectionUtils.set(var5, NMSFields.ENTITY_eyeHeight, (float)var2.getEyeHeight());
      var5.a(var6.a(var5.dm()));
   }

   public void setStepHeight(Entity entity, double height) {
      net.minecraft.world.entity.Entity var4 = EntityUtils.nms(var1);
      if (var4 instanceof EntityLiving) {
         EntityLiving var5 = (EntityLiving)var4;
         var5.eS().registerAttribute(GenericAttributes.B);
         ((AttributeModifiable)Objects.requireNonNull(var5.f(GenericAttributes.B))).a(var2);
      }

   }

   public double getStepHeight(Entity entity) {
      return (double)EntityUtils.nms(var1).dI();
   }

   public void setPosition(Entity entity, double x, double y, double z) {
      EntityUtils.nms(var1).a_(var2, var4, var6);
   }

   public void movePassenger(Entity entity, double x, double y, double z) {
      net.minecraft.world.entity.Entity var8 = EntityUtils.nms(var1);
      if (this.dummyArmorStand == null) {
         this.dummyArmorStand = new EntityArmorStand(EntityTypes.d, var8.dO());
         this.dummyArmorStand.v(true);
      }

      double var9 = var4 - var8.l(this.dummyArmorStand).d;
      var8.a_(var2, var9, var6);
      var8.i(Vec3D.b);
      var8.n();
      if (var8 instanceof EntityPlayer) {
         EntityPlayer var11 = (EntityPlayer)var8;
         ReflectionUtils.set(var11.c, NMSFields.SERVER_GAME_PACKET_LISTENER_IMPL_clientIsFloating, false);
      }

   }

   public void forceSpawn(BaseEntity<?> entity, Player player) {
      if (var2 != null) {
         IEntityData var3 = var1.getData();
         if (var3 instanceof BukkitEntityData) {
            BukkitEntityData var4 = (BukkitEntityData)var3;
            var4.getTracked().sendPairingData(var2);
         }
      }

   }

   public void forceDespawn(BaseEntity<?> entity, Player player) {
      if (var2 != null) {
         NetworkUtils.send((UUID)var2.getUniqueId(), new PacketPlayOutEntityDestroy(new int[]{var1.getEntityId()}));
      }

   }

   public void setForcedInvisible(Player player, boolean flag) {
      if (this.isForcedInvisible(var1) != var2) {
         net.minecraft.world.entity.Entity var3 = EntityUtils.nms(var1);
         byte var4 = 0;

         for(int var5 = 0; var5 < 8; ++var5) {
            var4 = MathUtils.setBit(var4, var5, var3.i(var5));
         }

         if (var2) {
            this.forceInvisible.add(var1.getUniqueId());
            var4 = MathUtils.setBit(var4, 5, true);
         } else {
            this.forceInvisible.remove(var1.getUniqueId());
         }

         PacketPlayOutEntityMetadata var6 = new PacketPlayOutEntityMetadata(var1.getEntityId(), List.of(new c(0, DataWatcherRegistry.a, var4)));
         NetworkUtils.send((UUID)var1.getUniqueId(), var6);
      }

   }

   public boolean isForcedInvisible(Player player) {
      return this.forceInvisible.contains(var1.getUniqueId());
   }

   public BodyRotationController wrapBodyRotationControl(Entity entity, Supplier<BodyRotationController> def) {
      net.minecraft.world.entity.Entity var3 = EntityUtils.nms(var1);
      if (var3 instanceof EntityInsentient) {
         EntityInsentient var4 = (EntityInsentient)var3;
         BodyRotationControlWrapper var5 = new BodyRotationControlWrapper(var4);
         return (BodyRotationController)(ReflectionUtils.set(var4, NMSFields.MOB_bodyRotationControl, var5) ? var5 : (BodyRotationController)var2.get());
      } else {
         return (BodyRotationController)var2.get();
      }
   }

   public MoveController wrapMoveController(Entity entity, Supplier<MoveController> def) {
      net.minecraft.world.entity.Entity var3 = EntityUtils.nms(var1);
      if (var3 instanceof EntityInsentient) {
         EntityInsentient var4 = (EntityInsentient)var3;
         MoveControlWrapper var5 = new MoveControlWrapper(var4, var4.J());
         return (MoveController)(ReflectionUtils.set(var4, NMSFields.MOB_moveControl, var5) ? var5 : (MoveController)var2.get());
      } else {
         return (MoveController)var2.get();
      }
   }

   public LookController wrapLookController(Entity entity, Supplier<LookController> def) {
      net.minecraft.world.entity.Entity var3 = EntityUtils.nms(var1);
      if (var3 instanceof EntityInsentient) {
         EntityInsentient var4 = (EntityInsentient)var3;
         LookControlWrapper var5 = new LookControlWrapper(var4, var4.I());
         return (LookController)(ReflectionUtils.set(var4, NMSFields.MOB_lookControl, var5) ? var5 : (LookController)var2.get());
      } else {
         return (LookController)var2.get();
      }
   }

   public void wrapNavigation(Entity entity) {
      net.minecraft.world.entity.Entity var2 = EntityUtils.nms(var1);
      if (var2 instanceof EntityInsentient) {
         EntityInsentient var3 = (EntityInsentient)var2;

         try {
            Field var4 = ReflectionUtils.getField(NMSFields.MOB_navigation);
            NavigationAbstract var5 = var3.N();
            Object var6;
            if (var5 instanceof NavigationSpider) {
               NavigationSpider var7 = (NavigationSpider)var5;
               var6 = new WallClimberNavigationWrapper(var3, var7);
            } else if (var5 instanceof Navigation) {
               Navigation var9 = (Navigation)var5;
               var6 = new GroundNavigationWrapper(var3, var9);
            } else if (var5 instanceof NavigationFlying) {
               NavigationFlying var10 = (NavigationFlying)var5;
               var6 = new FlyingNavigationWrapper(var3, var10);
            } else if (var5 instanceof NavigationGuardian) {
               var6 = new WaterBoundNavigationWrapper(var3);
            } else {
               if (!(var5 instanceof AmphibiousPathNavigation)) {
                  String var13 = String.valueOf(var3.am());
                  LogUtil.warn("Failed to create custom navigation for " + var13 + ": " + String.valueOf(var3.cz()));
                  LogUtil.warn("Reason: Navigation class type is " + var5.getClass().getSimpleName() + ".");
                  return;
               }

               AmphibiousPathNavigation var11 = (AmphibiousPathNavigation)var5;
               var6 = new AmphibiousNavigationWrapper(var3, var11);
            }

            var4.set(var3, var6);
            PathfinderGoalSelector var12 = (PathfinderGoalSelector)ReflectionUtils.getField(NMSFields.MOB_goalSelector).get(var3);
            RaceConditionUtil.wrapConmod(() -> {
               this.replaceNavigation(var12, var6);
            });
         } catch (IllegalAccessException var8) {
            var8.printStackTrace();
         }
      }

   }

   private void replaceNavigation(PathfinderGoalSelector goalSelector, NavigationAbstract newNav) {
      try {
         Iterator var3 = var1.b().iterator();

         while(var3.hasNext()) {
            PathfinderGoalWrapped var4 = (PathfinderGoalWrapped)var3.next();
            PathfinderGoal var5 = var4.k();
            Field[] var6 = var5.getClass().getDeclaredFields();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               Field var9 = var6[var8];
               var9 = ReflectionUtils.getField(var5.getClass(), var9.getName());
               Object var10 = var9.get(var5);
               if (var10 instanceof NavigationAbstract) {
                  var9.set(var5, var2);
               }
            }
         }
      } catch (IllegalAccessException var11) {
         var11.printStackTrace();
      }

   }

   public HitboxEntity createHitbox(Location location, IJoint joint, SubHitbox subHitbox) {
      WorldServer var4 = ((CraftWorld)var1.getWorld()).getHandle();
      HitboxEntityImpl var5 = new HitboxEntityImpl(var4, var2, var3);
      var5.queueLocation((new Vector3f()).set(var1.getX(), var1.getY(), var1.getZ()));
      var5.a_(var1.getX(), var1.getY(), var1.getZ());
      ModelAPI.setRenderCanceled(var5.an(), true);
      Future.start((Entity)var5.getBukkitEntity()).thenRunSync(() -> {
         var4.b(var5);
         ModelAPI.getInteractionTracker().addHitbox(var5);
      });
      return var5;
   }

   @Nullable
   public HitboxEntity castHitbox(Entity entity) {
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

   public boolean hurt(Entity entity, Object source, float amount) {
      if (var2 instanceof DamageSource) {
         DamageSource var4 = (DamageSource)var2;
         return EntityUtils.nms(var1).a(var4, var3);
      } else {
         throw new RuntimeException("Passed in source is not an NMS DamageSource.");
      }
   }

   public EntityHandler.InteractionResult interact(Entity entity, HumanEntity player, EquipmentSlot hand) {
      net.minecraft.world.entity.Entity var4 = EntityUtils.nms(var1);
      if (var4 instanceof EntityLiving) {
         EntityLiving var5 = (EntityLiving)var4;
         EnumInteractionResult var6 = var5.a(((CraftPlayer)var2).getHandle(), var3 == EquipmentSlot.HAND ? EnumHand.a : EnumHand.b);
         EntityHandler.InteractionResult var7;
         switch(var6) {
         case a:
            var7 = EntityHandler.InteractionResult.SUCCESS;
            break;
         case b:
            var7 = EntityHandler.InteractionResult.SUCCESS_NO_ITEM_USED;
            break;
         case c:
            var7 = EntityHandler.InteractionResult.CONSUME;
            break;
         case d:
            var7 = EntityHandler.InteractionResult.CONSUME_PARTIAL;
            break;
         case e:
            var7 = EntityHandler.InteractionResult.PASS;
            break;
         case f:
            var7 = EntityHandler.InteractionResult.FAIL;
            break;
         default:
            throw new RuntimeException((String)null, (Throwable)null);
         }

         return var7;
      } else {
         return EntityHandler.InteractionResult.FAIL;
      }
   }

   public void spawnDynamicHitbox(DynamicHitbox hitbox) {
      Vector var2 = (Vector)var1.getPositionTracker().get();
      final Packets.PacketSupplier var3 = NetworkUtils.createPivotSpawn(DynamicHitbox.getPivotId(), DynamicHitbox.getPivotUUID(), var2.toVector3f().add(0.0F, -0.5202F, 0.0F));
      final PacketPlayOutEntityMetadata var4 = new PacketPlayOutEntityMetadata(DynamicHitbox.getPivotId(), EntityUtils.DEFAULT_AREA_EFFECT_CLOUD_DATA);
      final PacketPlayOutSpawnEntity var5 = new PacketPlayOutSpawnEntity(DynamicHitbox.getHitboxId(), DynamicHitbox.getHitboxUUID(), var2.getX(), var2.getY() - 0.5202D, var2.getZ(), 0.0F, 0.0F, EntityTypes.aP, 0, Vec3D.b, 0.0D);
      final PacketPlayOutEntityMetadata var6 = new PacketPlayOutEntityMetadata(DynamicHitbox.getHitboxId(), EntityUtils.DEFAULT_SLIME_DATA);
      final PacketPlayOutMount var7 = new PacketPlayOutMount(EntityContainer.of(DynamicHitbox.getPivotId(), DynamicHitbox.getHitboxId()));
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

   public void updateDynamicHitbox(DynamicHitbox hitbox) {
      Vector3f var2 = ((Vector)var1.getPositionTracker().get()).toVector3f().add(0.0F, -0.5202F, 0.0F);
      NetworkUtils.send(var1.getPlayer().getUniqueId(), NetworkUtils.createPivotTeleport(DynamicHitbox.getPivotId(), var2).supply(var1.getPlayer().getUniqueId()));
   }

   public void destroyDynamicHitbox(DynamicHitbox hitbox) {
      PacketPlayOutEntityDestroy var2 = new PacketPlayOutEntityDestroy(new int[]{DynamicHitbox.getHitboxId(), DynamicHitbox.getPivotId()});
      NetworkUtils.send((UUID)var1.getPlayer().getUniqueId(), var2);
   }

   public void forceUseItem(Player player, EquipmentSlot hand) {
      ItemStack var3 = var1.getEquipment().getItem(var2);
      net.minecraft.world.item.ItemStack var4 = ((CraftItemStack)var3).handle;
      EntityPlayer var5 = (EntityPlayer)EntityUtils.nms(var1);
      PlayerConnection var6 = var5.c;
      PacketPlayInBlockPlace var7 = new PacketPlayInBlockPlace(var2 == EquipmentSlot.HAND ? EnumHand.a : EnumHand.b, 0, 0.0F, 0.0F);
      var7.timestamp = System.currentTimeMillis();
      PatchedServerGamePacketListener.handleUseItem(var7, var6, (var4x) -> {
         if (var4.u() != EnumAnimation.a && var4x == EnumInteractionResult.c) {
            PacketPlayOutEntityMetadata var5x = new PacketPlayOutEntityMetadata(var1.getEntityId(), List.of(new c(8, DataWatcherRegistry.a, (byte)(var2 == EquipmentSlot.HAND ? 1 : 3))));
            NetworkUtils.send((UUID)var1.getUniqueId(), var5x);
            Item var6 = var4.g();
            if (var6 instanceof InstrumentItem) {
               InstrumentItem var7 = (InstrumentItem)var6;
               Optional var8 = (Optional)ReflectionUtils.call(var7, NMSMethods.INSTRUMENT_ITEM_getInstrument, var4);
               var8.ifPresent((var2x) -> {
                  Instrument var3 = (Instrument)var2x.a();
                  Holder var4 = var3.a();
                  float var5x = var3.c() / 16.0F;
                  RandomSource var6 = (RandomSource)ReflectionUtils.get(var5.dO(), NMSFields.LEVEL_threadSafeRandom);
                  PacketPlayOutEntitySound var7 = new PacketPlayOutEntitySound(var4, SoundCategory.c, var5, var5x, 1.0F, var6.g());
                  NetworkUtils.send((UUID)var1.getUniqueId(), var7);
               });
            }
         }

      });
   }

   public float getYRot(Entity entity) {
      return EntityUtils.nms(var1).dE();
   }

   public float getYHeadRot(Entity entity) {
      net.minecraft.world.entity.Entity var2 = EntityUtils.nms(var1);
      if (var2 instanceof EntityLiving) {
         EntityLiving var3 = (EntityLiving)var2;
         return var3.ct();
      } else {
         return var2.dE();
      }
   }

   public float getXHeadRot(Entity entity) {
      return EntityUtils.nms(var1).dG();
   }

   public float getYBodyRot(Entity entity) {
      net.minecraft.world.entity.Entity var2 = EntityUtils.nms(var1);
      if (var2 instanceof EntityLiving) {
         EntityLiving var3 = (EntityLiving)var2;
         return var3.aY;
      } else {
         return var2.dE();
      }
   }

   public void setYRot(Entity entity, float angle) {
      EntityUtils.nms(var1).t(var2);
   }

   public void setYHeadRot(Entity entity, float angle) {
      net.minecraft.world.entity.Entity var3 = EntityUtils.nms(var1);
      if (var3 instanceof EntityLiving) {
         EntityLiving var4 = (EntityLiving)var3;
         var4.o(var2);
      } else {
         var3.t(var2);
      }

   }

   public void setXHeadRot(Entity entity, float angle) {
      EntityUtils.nms(var1).u(var2);
   }

   public void setYBodyRot(Entity entity, float angle) {
      net.minecraft.world.entity.Entity var3 = EntityUtils.nms(var1);
      if (var3 instanceof EntityLiving) {
         EntityLiving var4 = (EntityLiving)var3;
         var4.p(var2);
      } else {
         var3.t(var2);
      }

   }

   public void move(Entity entity, double x, double y, double z) {
      net.minecraft.world.entity.Entity var8 = EntityUtils.nms(var1);
      var8.a(EnumMoveType.a, new Vec3D(var2, var4, var6));
   }

   public boolean isWalking(Entity entity) {
      net.minecraft.world.entity.Entity var2 = EntityUtils.nms(var1);
      if (var2.ai < 1) {
         return false;
      } else {
         double var3 = var2.dt() - var2.ad;
         double var5 = var2.dz() - var2.af;
         return var3 * var3 + var5 * var5 > 2.500000277905201E-7D;
      }
   }

   public boolean isStrafing(Entity entity) {
      return false;
   }

   public boolean isJumping(Entity entity) {
      net.minecraft.world.entity.Entity var2 = EntityUtils.nms(var1);
      if (!(var2 instanceof EntityLiving)) {
         return false;
      } else {
         EntityLiving var3 = (EntityLiving)var2;
         Boolean var4 = (Boolean)ReflectionUtils.get(var3, NMSFields.LIVING_ENTITY_jumping, false);
         return var4 != null && var4;
      }
   }

   public boolean isFlying(Entity entity) {
      return false;
   }

   public float getHealth(Entity entity) {
      net.minecraft.world.entity.Entity var2 = EntityUtils.nms(var1);
      if (var2 instanceof EntityLiving) {
         EntityLiving var3 = (EntityLiving)var2;
         return var3.ew();
      } else {
         return 20.0F;
      }
   }

   public float getMaxHealth(Entity entity) {
      net.minecraft.world.entity.Entity var2 = EntityUtils.nms(var1);
      if (var2 instanceof EntityLiving) {
         EntityLiving var3 = (EntityLiving)var2;
         return var3.eN();
      } else {
         return 20.0F;
      }
   }

   public boolean isRemoved(Entity entity) {
      return EntityUtils.nms(var1).dJ();
   }

   public int getGlowColor(Entity entity) {
      net.minecraft.world.entity.Entity var2 = EntityUtils.nms(var1);
      return var2.q_();
   }

   public void setDeathTick(Entity entity, int tick) {
      net.minecraft.world.entity.Entity var3 = EntityUtils.nms(var1);
      if (var3 instanceof EntityLiving) {
         EntityLiving var4 = (EntityLiving)var3;
         var4.aQ = var2;
      }

   }

   public TrackedEntity wrapTrackedEntity(Entity entity) {
      WorldServer var2 = ((CraftWorld)var1.getWorld()).getHandle();
      Int2ObjectMap var3 = var2.l().a.K;
      EntityTracker var4 = (EntityTracker)var3.get(var1.getEntityId());
      return (TrackedEntity)(var4 == null ? new TempTrackedEntity(var1) : new TrackedEntityImpl(var1, () -> {
         return (EntityTracker)var3.get(var1.getEntityId());
      }, var4));
   }

   public boolean shouldCull(Player player, Location eyePosition, Entity entity, BoundingBox box) {
      CraftWorld var5 = (CraftWorld)var1.getWorld();
      Vec3D var6 = CraftLocation.toVec3D(var2);
      if (!(var4.getWidthX() >= this.forceRenderWidth) && !(var4.getWidthZ() >= this.forceRenderWidth) && !(var4.getHeight() >= this.forceRenderHeight)) {
         int var7 = MathHelper.a(var4.getMinX());
         int var8 = MathHelper.a(var4.getMinY());
         int var9 = MathHelper.a(var4.getMinZ());
         int var10 = MathHelper.c(var4.getMaxX()) - 1;
         int var11 = MathHelper.c(var4.getMaxY()) - 1;
         int var12 = MathHelper.c(var4.getMaxZ()) - 1;
         EntityHandler.BoxRelToCam var13 = EntityHandler.BoxRelToCam.from(var7, var10, MathHelper.a(var6.c));
         EntityHandler.BoxRelToCam var14 = EntityHandler.BoxRelToCam.from(var8, var11, MathHelper.a(var6.d));
         EntityHandler.BoxRelToCam var15 = EntityHandler.BoxRelToCam.from(var9, var12, MathHelper.a(var6.e));
         if (var13 == EntityHandler.BoxRelToCam.INSIDE && var14 == EntityHandler.BoxRelToCam.INSIDE && var15 == EntityHandler.BoxRelToCam.INSIDE) {
            return false;
         } else {
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
                           var16.add(new Vec3D((double)((float)var17 + var24.x), (double)((float)var19 + var24.y), (double)((float)var21 + var24.z)));
                        }
                     }
                  }
               }
            }

            Iterator var25;
            Vec3D var26;
            if (DebugToggle.isDebugging(DebugToggle.SHOW_CULL_POINTS)) {
               var25 = var16.iterator();

               while(var25.hasNext()) {
                  var26 = (Vec3D)var25.next();
                  var5.spawnParticle(Particle.DUST, var26.c, var26.d, var26.e, 1, new DustOptions(Color.RED, 0.2F));
               }
            }

            var25 = var16.iterator();

            while(var25.hasNext()) {
               var26 = (Vec3D)var25.next();
               if (isVisible(var5, var6, var26)) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   static {
      ENTITY_COUNTER = (AtomicInteger)ReflectionUtils.get(NMSFields.ENTITY_ENTITY_COUNTER);
   }
}
