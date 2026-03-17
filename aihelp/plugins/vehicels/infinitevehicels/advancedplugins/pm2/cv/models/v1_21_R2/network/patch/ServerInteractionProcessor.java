package advancedplugins.pm2.cv.models.v1_21_R2.network.patch;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.ServerInfo;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.events.BaseEntityInteractEvent;
import advancedplugins.pm2.cv.models.api.nms.entity.EntityHandler;
import advancedplugins.pm2.cv.models.api.nms.entity.HitboxEntity;
import advancedplugins.pm2.cv.models.api.utils.reflections.ReflectionUtils;
import advancedplugins.pm2.cv.models.v1_21_R2.ReflectionMethodCatalog;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriterionTriggers;
import net.minecraft.network.protocol.PlayerConnectionUtils;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.PacketListenerPlayIn;
import net.minecraft.network.protocol.game.PacketPlayInBlockPlace;
import net.minecraft.network.protocol.game.PacketPlayOutAttachEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity.c;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.EntityTrackerEntry;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.level.PlayerChunkMap.EntityTracker;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumHand;
import net.minecraft.world.EnumInteractionResult;
import net.minecraft.world.EnumInteractionResult.d;
import net.minecraft.world.EnumInteractionResult.e;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityExperienceOrb;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.entity.projectile.EntityArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.EnumGamemode;
import net.minecraft.world.level.RayTrace;
import net.minecraft.world.level.RayTrace.BlockCollisionOption;
import net.minecraft.world.level.RayTrace.FluidCollisionOption;
import net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.world.phys.MovingObjectPosition;
import net.minecraft.world.phys.MovingObjectPositionBlock;
import net.minecraft.world.phys.Vec3D;
import net.minecraft.world.phys.MovingObjectPosition.EnumMovingObjectType;
import org.bukkit.craftbukkit.v1_21_R2.CraftServer;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R2.event.CraftEventFactory;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class ServerInteractionProcessor {
   private static final Logger LOGGER = LogUtils.getLogger();

   public static void handleInteract(InteractionPacketWrapper wrapper, PacketListenerPlayIn listener) {
      if (var1 instanceof PlayerConnection) {
         PlayerConnection var2 = (PlayerConnection)var1;
         ServerInteractionProcessor.InteractionHandler var3 = new ServerInteractionProcessor.InteractionHandler(var0, var2);
         var3.process();
      }
   }

   public static void handleUseItem(PacketPlayInBlockPlace packet, PacketListenerPlayIn listener, Consumer<EnumInteractionResult> afterUse) {
      if (var1 instanceof PlayerConnection) {
         PlayerConnection var3 = (PlayerConnection)var1;
         ServerInteractionProcessor.ItemUseHandler var4 = new ServerInteractionProcessor.ItemUseHandler(var0, var3, var2);
         var4.process();
      }
   }

   private static class InteractionHandler {
      private final InteractionPacketWrapper wrapper;
      private final PlayerConnection connection;
      private final EntityPlayer player;
      private final CraftPlayer craftPlayer;
      private final WorldServer level;
      private final CraftServer server;

      InteractionHandler(InteractionPacketWrapper wrapper, PlayerConnection connection) {
         this.wrapper = var1;
         this.connection = var2;
         this.player = var2.o();
         this.craftPlayer = this.player.getBukkitEntity();
         this.level = this.player.y();
         this.server = this.level.p().server;
      }

      void process() {
         PlayerConnectionUtils.a(this.wrapper, this.connection, this.level);
         if (!this.player.fi()) {
            this.handleFakeInteraction();
            Entity var1 = this.wrapper.getTarget(this.level);
            if (this.shouldProcessInteraction(var1)) {
               this.processEntityInteraction(var1);
            }

         }
      }

      private void handleFakeInteraction() {
         this.wrapper.dispatch(new c() {
            // $FF: synthetic field
            final ServerInteractionProcessor.InteractionHandler this$0;

            {
               this.this$0 = var1;
            }

            public void a(EnumHand hand) {
               if (this.this$0.wrapper.isFakeInteraction()) {
                  EntityHandler var2 = ModelAPI.getNMSHandler().getEntityHandler();
                  var2.forceUseItem(this.this$0.craftPlayer, EquipmentSlot.HAND);
                  var2.forceUseItem(this.this$0.craftPlayer, EquipmentSlot.OFF_HAND);
               }

            }

            public void a(EnumHand hand, Vec3D pos) {
            }

            public void a() {
            }
         });
      }

      private boolean shouldProcessInteraction(Entity entity) {
         return var1 != this.player || this.player.aa_();
      }

      private void processEntityInteraction(Entity targetEntity) {
         this.player.H();
         this.player.g(this.wrapper.isUsingSecondaryAction());
         IVisualModel var2 = this.findAssociatedModel();
         if (var2 != null) {
            this.handleModelInteraction(var2);
         }

         if (var1 == null) {
            this.handleUnknownEntity();
         } else if (this.level.F_().a(var1.dw())) {
            this.handleKnownEntity(var1);
         }

      }

      private IVisualModel findAssociatedModel() {
         IVisualModel var1 = ModelAPI.getInteractionTracker().getModelRelay(this.wrapper.getOriginalId());
         if (var1 == null) {
            HitboxEntity var2 = ModelAPI.getInteractionTracker().getHitbox(this.wrapper.getRelayedId());
            if (var2 != null) {
               var1 = var2.getJoint().getVisualModel();
            }
         }

         return var1;
      }

      private void handleModelInteraction(IVisualModel model) {
         BaseEntity var2 = var1.getModeledEntity().getBase();
         this.wrapper.dispatch(new c(var2, var1) {
            // $FF: synthetic field
            final BaseEntity val$baseEntity;
            // $FF: synthetic field
            final IVisualModel val$model;
            // $FF: synthetic field
            final ServerInteractionProcessor.InteractionHandler this$0;

            {
               this.this$0 = var1;
               this.val$baseEntity = var2;
               this.val$model = var3;
            }

            public void a(EnumHand hand) {
               this.fireInteractionEvent(this.val$baseEntity, this.val$model, BaseEntityInteractEvent.Action.INTERACT, (Vector)null);
            }

            public void a(EnumHand hand, Vec3D pos) {
               Vector var3 = new Vector(var2.d, var2.e, var2.f);
               this.fireInteractionEvent(this.val$baseEntity, this.val$model, BaseEntityInteractEvent.Action.INTERACT_ON, var3);
            }

            public void a() {
               this.this$0.server.getPluginManager().callEvent(new BaseEntityInteractEvent(this.this$0.craftPlayer, this.val$baseEntity, this.val$model, BaseEntityInteractEvent.Action.ATTACK, EquipmentSlot.HAND, this.this$0.wrapper.isUsingSecondaryAction(), this.this$0.craftPlayer.getInventory().getItem(EquipmentSlot.HAND), (Vector)null));
            }

            private void fireInteractionEvent(BaseEntity<?> entity, IVisualModel model, BaseEntityInteractEvent.Action action, Vector pos) {
               EquipmentSlot[] var5 = new EquipmentSlot[]{EquipmentSlot.HAND, EquipmentSlot.OFF_HAND};
               int var6 = var5.length;

               for(int var7 = 0; var7 < var6; ++var7) {
                  EquipmentSlot var8 = var5[var7];
                  this.this$0.server.getPluginManager().callEvent(new BaseEntityInteractEvent(this.this$0.craftPlayer, var1, var2, var3, var8, this.this$0.wrapper.isUsingSecondaryAction(), this.this$0.craftPlayer.getInventory().getItem(var8), var4));
               }

            }
         });
      }

      private void handleUnknownEntity() {
         if (ServerInfo.IS_PAPER) {
            this.wrapper.dispatch(new c(this) {
               public void a(EnumHand hand) {
                  this.invokeUnknownEntityEvent(var1, (Vec3D)null);
               }

               public void a(EnumHand hand, Vec3D pos) {
                  this.invokeUnknownEntityEvent(var1, var2);
               }

               public void a() {
                  this.invokeUnknownEntityEvent(EnumHand.a, (Vec3D)null);
               }

               private void invokeUnknownEntityEvent(EnumHand hand, @Nullable Vec3D vector) {
               }
            });
         }
      }

      private void handleKnownEntity(Entity entity) {
         AxisAlignedBB var2 = var1.cR();
         double var3 = var2.e(this.player.bF());
         double var5 = this.player.gK();
         if (var3 < var5 * var5) {
            (new ServerInteractionProcessor.InteractionHandler.EntityInteractionProcessor(var1)).process();
         }

      }

      private class EntityInteractionProcessor {
         private final Entity target;

         EntityInteractionProcessor(Entity target) {
            this.target = var2;
         }

         void process() {
            InteractionHandler.this.wrapper.dispatch(new c() {
               // $FF: synthetic field
               final ServerInteractionProcessor.InteractionHandler.EntityInteractionProcessor this$1;

               {
                  this.this$1 = var1;
               }

               public void a(@NotNull EnumHand hand) {
                  this.executeInteraction(var1, EntityHuman::a, new PlayerInteractEntityEvent(this.this$1.this$0.craftPlayer, this.this$1.target.getBukkitEntity(), var1 == EnumHand.b ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND));
               }

               public void a(@NotNull EnumHand hand, @NotNull Vec3D pos) {
                  this.executeInteraction(var1, (var1, var2, var3) -> {
                     return var2.a(var1, var0, var3);
                  }, new PlayerInteractAtEntityEvent(this.this$1.this$0.craftPlayer, this.this$1.target.getBukkitEntity(), new Vector(var2.d, var2.e, var2.f), var1 == EnumHand.b ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND));
               }

               public void a() {
                  if (this.canAttack()) {
                     ItemStack var1 = this.this$1.this$0.player.b(EnumHand.a);
                     if (var1.a(this.this$1.this$0.level.J())) {
                        this.this$1.this$0.player.e(this.this$1.target);
                        this.validateItemCount(var1);
                     }
                  }

               }

               private boolean canAttack() {
                  return !(this.this$1.target instanceof EntityItem) && !(this.this$1.target instanceof EntityExperienceOrb) && !(this.this$1.target instanceof EntityArrow) && (this.this$1.target != this.this$1.this$0.player || this.this$1.this$0.player.aa_());
               }

               private void executeInteraction(EnumHand hand, ServerInteractionProcessor.EntityInteractor interactor, PlayerInteractEntityEvent event) {
                  ItemStack var4 = this.this$1.this$0.player.b(var1);
                  if (var4.a(this.this$1.this$0.level.J())) {
                     ItemStack var5 = var4.v();
                     Item var6 = this.this$1.this$0.player.gi().f().h();
                     boolean var7 = var4.h() == Items.vA && this.this$1.target instanceof EntityInsentient;
                     this.this$1.this$0.server.getPluginManager().callEvent(var3);
                     this.handleSpecialCases(var3, var6, var7);
                     if (var3.isCancelled()) {
                        this.this$1.this$0.player.cd.b();
                     } else {
                        EnumInteractionResult var8 = var2.interact(this.this$1.this$0.player, this.this$1.target, var1);
                        this.validateItemCount(var4);
                        if (var8.a()) {
                           CriterionTriggers.T.a(this.this$1.this$0.player, var5, this.this$1.target);
                           this.handleSuccessfulInteraction(var8, var1);
                        }

                     }
                  }
               }

               private void handleSpecialCases(PlayerInteractEntityEvent event, Item beforeItem, boolean needsLeashUpdate) {
                  if (this.shouldUpdateBucketable(var1, var2)) {
                     this.updateBucketableEntity();
                  }

                  if (var3 && this.shouldUpdateLeash(var1, var2)) {
                     this.updateLeash();
                  }

                  if (this.shouldUpdateEntityData(var1, var2)) {
                     this.updateEntityData();
                  }

                  if (this.this$1.target instanceof Allay && this.shouldUpdateAllay(var1)) {
                     this.updateAllayEquipment();
                  }

               }

               private boolean shouldUpdateBucketable(PlayerInteractEntityEvent event, Item beforeItem) {
                  return this.this$1.target instanceof Bucketable && this.this$1.target instanceof EntityLiving && var2.j() == Items.qX && (var1.isCancelled() || this.this$1.this$0.player.gi().f().h() != var2);
               }

               private boolean shouldUpdateLeash(PlayerInteractEntityEvent event, Item beforeItem) {
                  return var1.isCancelled() || this.this$1.this$0.player.gi().f().h() != var2;
               }

               private boolean shouldUpdateEntityData(PlayerInteractEntityEvent event, Item beforeItem) {
                  return var1.isCancelled() || this.this$1.this$0.player.gi().f().h() != var2;
               }

               private boolean shouldUpdateAllay(PlayerInteractEntityEvent event) {
                  return var1.isCancelled();
               }

               private void updateBucketableEntity() {
                  if (this.this$1.this$0.player.getBukkitEntity().canSee(this.this$1.target.getBukkitEntity())) {
                     EntityTrackerEntry var1 = ((EntityTracker)this.this$1.this$0.level.m().a.K.get(this.this$1.target.ar())).b;
                     ArrayList var2 = new ArrayList();
                     EntityPlayer var10001 = this.this$1.this$0.player;
                     Objects.requireNonNull(var2);
                     var1.a(var10001, var2::add);
                     this.this$1.this$0.player.f.b(new ClientboundBundlePacket(var2));
                  }

                  this.this$1.this$0.player.cd.b();
               }

               private void updateLeash() {
                  this.this$1.this$0.connection.b(new PacketPlayOutAttachEntity(this.this$1.target, ((EntityInsentient)this.this$1.target).A()));
               }

               private void updateEntityData() {
                  List var1 = this.this$1.target.au().b();
                  if (var1 != null && this.this$1.this$0.player.getBukkitEntity().canSee(this.this$1.target.getBukkitEntity())) {
                     this.this$1.this$0.player.f.b(new PacketPlayOutEntityMetadata(this.this$1.target.ar(), var1));
                  }

               }

               private void updateAllayEquipment() {
                  Allay var1 = (Allay)this.this$1.target;
                  List var2 = (List)Arrays.stream(EnumItemSlot.values()).map((var1) -> {
                     return Pair.of(var1, var0.a(var1));
                  }).collect(Collectors.toList());
                  this.this$1.this$0.connection.b(new PacketPlayOutEntityEquipment(this.this$1.target.ar(), var2));
                  this.this$1.this$0.player.cd.b();
               }

               private void validateItemCount(ItemStack item) {
                  if (!var1.f() && var1.L() <= -1) {
                     this.this$1.this$0.player.cd.b();
                  }

               }

               private void handleSuccessfulInteraction(EnumInteractionResult result, EnumHand hand) {
                  if (var1 instanceof d) {
                     d var3 = (d)var1;
                     if (var3.e() != e.a) {
                        this.this$1.this$0.player.a(var2, var3.e() == e.c);
                     }
                  }

               }

               // $FF: synthetic method
               private static Pair lambda$updateAllayEquipment$1(Allay allay, EnumItemSlot slot) {
                  return Pair.of(var1, var0.a(var1));
               }

               // $FF: synthetic method
               private static EnumInteractionResult lambda$onInteraction$0(Vec3D pos, EntityPlayer p, Entity e, EnumHand h) {
                  return var2.a(var1, var0, var3);
               }
            });
         }
      }
   }

   private static class ItemUseHandler {
      private final PacketPlayInBlockPlace packet;
      private final PlayerConnection connection;
      private final Consumer<EnumInteractionResult> afterUse;
      private final EntityPlayer player;

      ItemUseHandler(PacketPlayInBlockPlace packet, PlayerConnection connection, Consumer<EnumInteractionResult> afterUse) {
         this.packet = var1;
         this.connection = var2;
         this.afterUse = var3;
         this.player = var2.o();
      }

      void process() {
         PlayerConnectionUtils.a(this.packet, this.connection, this.player.y());
         if (!this.player.fi() && this.checkRateLimit()) {
            this.connection.a(this.packet.e());
            EnumHand var1 = this.packet.b();
            ItemStack var2 = this.player.b(var1);
            this.player.H();
            if (!var2.f() && var2.a(this.player.dW().J())) {
               this.processItemUse(var1, var2);
            }
         }
      }

      private boolean checkRateLimit() {
         return Boolean.TRUE.equals(ReflectionUtils.call(this.connection, ReflectionMethodCatalog.VALIDATE_PACKET_LIMIT, this.packet.timestamp));
      }

      private void processItemUse(EnumHand hand, ItemStack item) {
         MovingObjectPosition var3 = this.calculateHitResult();
         boolean var4 = this.checkCancellation(var3, var1, var2);
         if (var4) {
            this.player.getBukkitEntity().updateInventory();
         } else {
            var2 = this.player.b(var1);
            if (!var2.f()) {
               EnumInteractionResult var5 = this.player.h.a(this.player, this.player.dW(), var2, var1);
               this.afterUse.accept(var5);
               if (var5 instanceof d) {
                  d var6 = (d)var5;
                  if (var6.e() != e.a) {
                     this.player.a(var1, var6.e() == e.c);
                  }
               }

            }
         }
      }

      private MovingObjectPosition calculateHitResult() {
         ServerInteractionProcessor.ItemUseHandler.PlayerViewInfo var1 = new ServerInteractionProcessor.ItemUseHandler.PlayerViewInfo(this.player);
         double var2 = this.player.h.b() == EnumGamemode.b ? 5.0D : 4.5D;
         Vec3D var4 = var1.getTargetPosition(var2);
         return this.player.dW().a(new RayTrace(var1.eyePosition, var4, BlockCollisionOption.b, FluidCollisionOption.a, this.player));
      }

      private boolean checkCancellation(MovingObjectPosition hitResult, EnumHand hand, ItemStack item) {
         if (var1 != null && var1.d() == EnumMovingObjectType.b) {
            MovingObjectPositionBlock var6 = (MovingObjectPositionBlock)var1;
            if (this.isRepeatedInteraction(var6, var2, var3)) {
               return this.player.h.interactResult;
            } else {
               PlayerInteractEvent var5 = CraftEventFactory.callPlayerInteractEvent(this.player, Action.RIGHT_CLICK_BLOCK, var6.b(), var6.c(), var3, true, var2, var6.g());
               this.player.h.firedInteract = false;
               return var5.useItemInHand() == Result.DENY;
            }
         } else {
            PlayerInteractEvent var4 = CraftEventFactory.callPlayerInteractEvent(this.player, Action.RIGHT_CLICK_AIR, var3, var2);
            return var4.useItemInHand() == Result.DENY;
         }
      }

      private boolean isRepeatedInteraction(MovingObjectPositionBlock blockHit, EnumHand hand, ItemStack item) {
         return this.player.h.firedInteract && this.player.h.interactPosition.equals(var1.b()) && this.player.h.interactHand == var2 && ItemStack.c(this.player.h.interactItemStack, var3);
      }

      private static class PlayerViewInfo {
         final Vec3D eyePosition;
         final float pitch;
         final float yaw;

         PlayerViewInfo(EntityPlayer player) {
            this.eyePosition = new Vec3D(var1.dB(), var1.dD() + (double)var1.cS(), var1.dH());
            this.pitch = var1.dO();
            this.yaw = var1.dM();
         }

         Vec3D getTargetPosition(double distance) {
            float var3 = -this.yaw * 0.017453292F - 3.1415927F;
            float var4 = -this.pitch * 0.017453292F;
            float var5 = MathHelper.b(var3);
            float var6 = MathHelper.a(var3);
            float var7 = -MathHelper.b(var4);
            float var8 = MathHelper.a(var4);
            float var9 = var6 * var7;
            float var11 = var5 * var7;
            return this.eyePosition.b((double)var9 * var1, (double)var8 * var1, (double)var11 * var1);
         }
      }
   }

   @FunctionalInterface
   private interface EntityInteractor {
      EnumInteractionResult interact(EntityPlayer var1, Entity var2, EnumHand var3);
   }
}
