package advancedplugins.pm2.cv.models.v1_21_R10.network.patch;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.ServerInfo;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.events.BaseEntityInteractEvent;
import advancedplugins.pm2.cv.models.api.nms.entity.EntityHandler;
import advancedplugins.pm2.cv.models.api.nms.entity.HitboxEntity;
import advancedplugins.pm2.cv.models.api.utils.reflections.ReflectionUtils;
import advancedplugins.pm2.cv.models.v1_21_R10.ReflectionMethodCatalog;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.network.protocol.game.ServerboundInteractPacket.Handler;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ChunkMap.TrackedEntity;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResult.Success;
import net.minecraft.world.InteractionResult.SwingSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import org.bukkit.craftbukkit.v1_21_R7.CraftServer;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R7.event.CraftEventFactory;
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

   public static void handleInteract(InteractionPacketWrapper var0, ServerGamePacketListener var1) {
      if (var1 instanceof ServerGamePacketListenerImpl) {
         ServerGamePacketListenerImpl var2 = (ServerGamePacketListenerImpl)var1;
         ServerInteractionProcessor.InteractionHandler var3 = new ServerInteractionProcessor.InteractionHandler(var0, var2);
         var3.process();
      }
   }

   public static void handleUseItem(ServerboundUseItemPacket var0, ServerGamePacketListener var1, Consumer<InteractionResult> var2) {
      if (var1 instanceof ServerGamePacketListenerImpl) {
         ServerGamePacketListenerImpl var3 = (ServerGamePacketListenerImpl)var1;
         ServerInteractionProcessor.ItemUseHandler var4 = new ServerInteractionProcessor.ItemUseHandler(var0, var3, var2);
         var4.process();
      }
   }

   private static class InteractionHandler {
      private final InteractionPacketWrapper wrapper;
      private final ServerGamePacketListenerImpl connection;
      private final ServerPlayer player;
      private final CraftPlayer craftPlayer;
      private final ServerLevel level;
      private final CraftServer server;

      InteractionHandler(InteractionPacketWrapper var1, ServerGamePacketListenerImpl var2) {
         this.wrapper = var1;
         this.connection = var2;
         this.player = var2.getPlayer();
         this.craftPlayer = this.player.getBukkitEntity();
         this.level = this.player.level();
         this.server = this.level.getServer().server;
      }

      void process() {
         PacketUtils.ensureRunningOnSameThread(this.wrapper, this.connection, this.level);
         if (!this.player.isImmobile()) {
            this.handleFakeInteraction();
            Entity var1 = this.wrapper.getTarget(this.level);
            if (this.shouldProcessInteraction(var1)) {
               this.processEntityInteraction(var1);
            }

         }
      }

      private void handleFakeInteraction() {
         this.wrapper.dispatch(new Handler() {
            // $FF: synthetic field
            final ServerInteractionProcessor.InteractionHandler this$0;

            {
               this.this$0 = var1;
            }

            public void onInteraction(InteractionHand var1) {
               if (this.this$0.wrapper.isFakeInteraction()) {
                  EntityHandler var2 = ModelAPI.getNMSHandler().getEntityHandler();
                  var2.forceUseItem(this.this$0.craftPlayer, EquipmentSlot.HAND);
                  var2.forceUseItem(this.this$0.craftPlayer, EquipmentSlot.OFF_HAND);
               }

            }

            public void onInteraction(InteractionHand var1, Vec3 var2) {
            }

            public void onAttack() {
            }
         });
      }

      private boolean shouldProcessInteraction(Entity var1) {
         return var1 != this.player || this.player.isSpectator();
      }

      private void processEntityInteraction(Entity var1) {
         this.player.resetLastActionTime();
         this.player.setShiftKeyDown(this.wrapper.isUsingSecondaryAction());
         IVisualModel var2 = this.findAssociatedModel();
         if (var2 != null) {
            this.handleModelInteraction(var2);
         }

         if (var1 == null) {
            this.handleUnknownEntity();
         } else if (this.level.getWorldBorder().isWithinBounds(var1.blockPosition())) {
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

      private void handleModelInteraction(IVisualModel var1) {
         BaseEntity var2 = var1.getModeledEntity().getBase();
         this.wrapper.dispatch(new Handler(var2, var1) {
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

            public void onInteraction(InteractionHand var1) {
               this.fireInteractionEvent(this.val$baseEntity, this.val$model, BaseEntityInteractEvent.Action.INTERACT, (Vector)null);
            }

            public void onInteraction(InteractionHand var1, Vec3 var2) {
               Vector var3 = new Vector(var2.x, var2.y, var2.z);
               this.fireInteractionEvent(this.val$baseEntity, this.val$model, BaseEntityInteractEvent.Action.INTERACT_ON, var3);
            }

            public void onAttack() {
               this.this$0.server.getPluginManager().callEvent(new BaseEntityInteractEvent(this.this$0.craftPlayer, this.val$baseEntity, this.val$model, BaseEntityInteractEvent.Action.ATTACK, EquipmentSlot.HAND, this.this$0.wrapper.isUsingSecondaryAction(), this.this$0.craftPlayer.getInventory().getItem(EquipmentSlot.HAND), (Vector)null));
            }

            private void fireInteractionEvent(BaseEntity<?> var1, IVisualModel var2, BaseEntityInteractEvent.Action var3, Vector var4) {
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
            this.wrapper.dispatch(new Handler(this) {
               public void onInteraction(InteractionHand var1) {
                  this.invokeUnknownEntityEvent(var1, (Vec3)null);
               }

               public void onInteraction(InteractionHand var1, Vec3 var2) {
                  this.invokeUnknownEntityEvent(var1, var2);
               }

               public void onAttack() {
                  this.invokeUnknownEntityEvent(InteractionHand.MAIN_HAND, (Vec3)null);
               }

               private void invokeUnknownEntityEvent(InteractionHand var1, @Nullable Vec3 var2) {
               }
            });
         }
      }

      private void handleKnownEntity(Entity var1) {
         AABB var2 = var1.getBoundingBox();
         double var3 = var2.distanceToSqr(this.player.getEyePosition());
         double var5 = this.player.entityInteractionRange();
         if (var3 < var5 * var5) {
            (new ServerInteractionProcessor.InteractionHandler.EntityInteractionProcessor(var1)).process();
         }

      }

      private class EntityInteractionProcessor {
         private final Entity target;

         EntityInteractionProcessor(Entity param2) {
            this.target = var2;
         }

         void process() {
            InteractionHandler.this.wrapper.dispatch(new Handler() {
               // $FF: synthetic field
               final ServerInteractionProcessor.InteractionHandler.EntityInteractionProcessor this$1;

               {
                  this.this$1 = var1;
               }

               public void onInteraction(@NotNull InteractionHand var1) {
                  this.executeInteraction(var1, Player::interactOn, new PlayerInteractEntityEvent(this.this$1.this$0.craftPlayer, this.this$1.target.getBukkitEntity(), var1 == InteractionHand.OFF_HAND ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND));
               }

               public void onInteraction(@NotNull InteractionHand var1, @NotNull Vec3 var2) {
                  this.executeInteraction(var1, (var1, var2, var3) -> {
                     return var2.interactAt(var1, var0, var3);
                  }, new PlayerInteractAtEntityEvent(this.this$1.this$0.craftPlayer, this.this$1.target.getBukkitEntity(), new Vector(var2.x, var2.y, var2.z), var1 == InteractionHand.OFF_HAND ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND));
               }

               public void onAttack() {
                  if (this.canAttack()) {
                     ItemStack var1 = this.this$1.this$0.player.getItemInHand(InteractionHand.MAIN_HAND);
                     if (var1.isItemEnabled(this.this$1.this$0.level.enabledFeatures())) {
                        this.this$1.this$0.player.attack(this.this$1.target);
                        this.validateItemCount(var1);
                     }
                  }

               }

               private boolean canAttack() {
                  return !(this.this$1.target instanceof ItemEntity) && !(this.this$1.target instanceof ExperienceOrb) && !(this.this$1.target instanceof AbstractArrow) && (this.this$1.target != this.this$1.this$0.player || this.this$1.this$0.player.isSpectator());
               }

               private void executeInteraction(InteractionHand var1, ServerInteractionProcessor.EntityInteractor var2, PlayerInteractEntityEvent var3) {
                  ItemStack var4 = this.this$1.this$0.player.getItemInHand(var1);
                  if (var4.isItemEnabled(this.this$1.this$0.level.enabledFeatures())) {
                     ItemStack var5 = var4.copy();
                     Item var6 = this.this$1.this$0.player.getInventory().getSelectedItem().getItem();
                     boolean var7 = var4.getItem() == Items.LEAD && this.this$1.target instanceof Mob;
                     this.this$1.this$0.server.getPluginManager().callEvent(var3);
                     this.handleSpecialCases(var3, var6, var7);
                     if (var3.isCancelled()) {
                        this.this$1.this$0.player.containerMenu.sendAllDataToRemote();
                     } else {
                        InteractionResult var8 = var2.interact(this.this$1.this$0.player, this.this$1.target, var1);
                        this.validateItemCount(var4);
                        if (var8.consumesAction()) {
                           CriteriaTriggers.PLAYER_INTERACTED_WITH_ENTITY.trigger(this.this$1.this$0.player, var5, this.this$1.target);
                           this.handleSuccessfulInteraction(var8, var1);
                        }

                     }
                  }
               }

               private void handleSpecialCases(PlayerInteractEntityEvent var1, Item var2, boolean var3) {
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

               private boolean shouldUpdateBucketable(PlayerInteractEntityEvent var1, Item var2) {
                  return this.this$1.target instanceof Bucketable && this.this$1.target instanceof LivingEntity && var2.asItem() == Items.WATER_BUCKET && (var1.isCancelled() || this.this$1.this$0.player.getInventory().getSelectedItem().getItem() != var2);
               }

               private boolean shouldUpdateLeash(PlayerInteractEntityEvent var1, Item var2) {
                  return var1.isCancelled() || this.this$1.this$0.player.getInventory().getSelectedItem().getItem() != var2;
               }

               private boolean shouldUpdateEntityData(PlayerInteractEntityEvent var1, Item var2) {
                  return var1.isCancelled() || this.this$1.this$0.player.getInventory().getSelectedItem().getItem() != var2;
               }

               private boolean shouldUpdateAllay(PlayerInteractEntityEvent var1) {
                  return var1.isCancelled();
               }

               private void updateBucketableEntity() {
                  if (this.this$1.this$0.player.getBukkitEntity().canSee(this.this$1.target.getBukkitEntity())) {
                     ServerEntity var1 = ((TrackedEntity)this.this$1.this$0.level.getChunkSource().chunkMap.entityMap.get(this.this$1.target.getId())).serverEntity;
                     ArrayList var2 = new ArrayList();
                     ServerPlayer var10001 = this.this$1.this$0.player;
                     Objects.requireNonNull(var2);
                     var1.sendPairingData(var10001, var2::add);
                     this.this$1.this$0.player.connection.send(new ClientboundBundlePacket(var2));
                  }

                  this.this$1.this$0.player.containerMenu.sendAllDataToRemote();
               }

               private void updateLeash() {
                  this.this$1.this$0.connection.send(new ClientboundSetEntityLinkPacket(this.this$1.target, ((Mob)this.this$1.target).getLeashHolder()));
               }

               private void updateEntityData() {
                  List var1 = this.this$1.target.getEntityData().packDirty();
                  if (var1 != null && this.this$1.this$0.player.getBukkitEntity().canSee(this.this$1.target.getBukkitEntity())) {
                     this.this$1.this$0.player.connection.send(new ClientboundSetEntityDataPacket(this.this$1.target.getId(), var1));
                  }

               }

               private void updateAllayEquipment() {
                  Allay var1 = (Allay)this.this$1.target;
                  List var2 = (List)Arrays.stream(net.minecraft.world.entity.EquipmentSlot.values()).map((var1) -> {
                     return Pair.of(var1, var0.getItemBySlot(var1));
                  }).collect(Collectors.toList());
                  this.this$1.this$0.connection.send(new ClientboundSetEquipmentPacket(this.this$1.target.getId(), var2));
                  this.this$1.this$0.player.containerMenu.sendAllDataToRemote();
               }

               private void validateItemCount(ItemStack var1) {
                  if (!var1.isEmpty() && var1.getCount() <= -1) {
                     this.this$1.this$0.player.containerMenu.sendAllDataToRemote();
                  }

               }

               private void handleSuccessfulInteraction(InteractionResult var1, InteractionHand var2) {
                  if (var1 instanceof Success) {
                     Success var3 = (Success)var1;
                     if (var3.swingSource() != SwingSource.NONE) {
                        this.this$1.this$0.player.swing(var2, var3.swingSource() == SwingSource.SERVER);
                     }
                  }

               }

               // $FF: synthetic method
               private static Pair lambda$updateAllayEquipment$1(Allay var0, net.minecraft.world.entity.EquipmentSlot var1) {
                  return Pair.of(var1, var0.getItemBySlot(var1));
               }

               // $FF: synthetic method
               private static InteractionResult lambda$onInteraction$0(Vec3 var0, ServerPlayer var1, Entity var2, InteractionHand var3) {
                  return var2.interactAt(var1, var0, var3);
               }
            });
         }
      }
   }

   private static class ItemUseHandler {
      private final ServerboundUseItemPacket packet;
      private final ServerGamePacketListenerImpl connection;
      private final Consumer<InteractionResult> afterUse;
      private final ServerPlayer player;

      ItemUseHandler(ServerboundUseItemPacket var1, ServerGamePacketListenerImpl var2, Consumer<InteractionResult> var3) {
         this.packet = var1;
         this.connection = var2;
         this.afterUse = var3;
         this.player = var2.getPlayer();
      }

      void process() {
         PacketUtils.ensureRunningOnSameThread(this.packet, this.connection, this.player.level());
         if (!this.player.isImmobile() && this.checkRateLimit()) {
            this.connection.ackBlockChangesUpTo(this.packet.getSequence());
            InteractionHand var1 = this.packet.getHand();
            ItemStack var2 = this.player.getItemInHand(var1);
            this.player.resetLastActionTime();
            if (!var2.isEmpty() && var2.isItemEnabled(this.player.level().enabledFeatures())) {
               this.processItemUse(var1, var2);
            }
         }
      }

      private boolean checkRateLimit() {
         return Boolean.TRUE.equals(ReflectionUtils.call(this.connection, ReflectionMethodCatalog.VALIDATE_PACKET_LIMIT, this.packet.timestamp));
      }

      private void processItemUse(InteractionHand var1, ItemStack var2) {
         HitResult var3 = this.calculateHitResult();
         boolean var4 = this.checkCancellation(var3, var1, var2);
         if (var4) {
            this.player.getBukkitEntity().updateInventory();
         } else {
            var2 = this.player.getItemInHand(var1);
            if (!var2.isEmpty()) {
               InteractionResult var5 = this.player.gameMode.useItem(this.player, this.player.level(), var2, var1);
               this.afterUse.accept(var5);
               if (var5 instanceof Success) {
                  Success var6 = (Success)var5;
                  if (var6.swingSource() != SwingSource.NONE) {
                     this.player.swing(var1, var6.swingSource() == SwingSource.SERVER);
                  }
               }

            }
         }
      }

      private HitResult calculateHitResult() {
         ServerInteractionProcessor.ItemUseHandler.PlayerViewInfo var1 = new ServerInteractionProcessor.ItemUseHandler.PlayerViewInfo(this.player);
         double var2 = this.player.gameMode.getGameModeForPlayer() == GameType.CREATIVE ? 5.0D : 4.5D;
         Vec3 var4 = var1.getTargetPosition(var2);
         return this.player.level().clip(new ClipContext(var1.eyePosition, var4, Block.OUTLINE, Fluid.NONE, this.player));
      }

      private boolean checkCancellation(HitResult var1, InteractionHand var2, ItemStack var3) {
         if (var1 != null && var1.getType() == Type.BLOCK) {
            BlockHitResult var6 = (BlockHitResult)var1;
            if (this.isRepeatedInteraction(var6, var2, var3)) {
               return this.player.gameMode.interactResult;
            } else {
               PlayerInteractEvent var5 = CraftEventFactory.callPlayerInteractEvent(this.player, Action.RIGHT_CLICK_BLOCK, var6.getBlockPos(), var6.getDirection(), var3, true, var2, var6.getLocation());
               this.player.gameMode.firedInteract = false;
               return var5.useItemInHand() == Result.DENY;
            }
         } else {
            PlayerInteractEvent var4 = CraftEventFactory.callPlayerInteractEvent(this.player, Action.RIGHT_CLICK_AIR, var3, var2);
            return var4.useItemInHand() == Result.DENY;
         }
      }

      private boolean isRepeatedInteraction(BlockHitResult var1, InteractionHand var2, ItemStack var3) {
         return this.player.gameMode.firedInteract && this.player.gameMode.interactPosition.equals(var1.getBlockPos()) && this.player.gameMode.interactHand == var2 && ItemStack.isSameItemSameComponents(this.player.gameMode.interactItemStack, var3);
      }

      private static class PlayerViewInfo {
         final Vec3 eyePosition;
         final float pitch;
         final float yaw;

         PlayerViewInfo(ServerPlayer var1) {
            this.eyePosition = new Vec3(var1.getX(), var1.getY() + (double)var1.getEyeHeight(), var1.getZ());
            this.pitch = var1.getXRot();
            this.yaw = var1.getYRot();
         }

         Vec3 getTargetPosition(double var1) {
            float var3 = -this.yaw * 0.017453292F - 3.1415927F;
            float var4 = -this.pitch * 0.017453292F;
            float var5 = Mth.cos((double)var3);
            float var6 = Mth.sin((double)var3);
            float var7 = -Mth.cos((double)var4);
            float var8 = Mth.sin((double)var4);
            float var9 = var6 * var7;
            float var11 = var5 * var7;
            return this.eyePosition.add((double)var9 * var1, (double)var8 * var1, (double)var11 * var1);
         }
      }
   }

   @FunctionalInterface
   private interface EntityInteractor {
      InteractionResult interact(ServerPlayer var1, Entity var2, InteractionHand var3);
   }
}
