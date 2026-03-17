package advancedplugins.pm2.cv.models.v1_21_R5.network.patch;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.ServerInfo;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.events.BaseEntityInteractEvent;
import advancedplugins.pm2.cv.models.api.nms.entity.EntityHandler;
import advancedplugins.pm2.cv.models.api.nms.entity.HitboxEntity;
import advancedplugins.pm2.cv.models.api.utils.reflections.ReflectionUtils;
import advancedplugins.pm2.cv.models.v1_21_R5.NMSMethods;
import com.destroystokyo.paper.event.player.PlayerUseUnknownEntityEvent;
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
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class PatchedServerGamePacketListener {
   private static final Logger LOGGER = LogUtils.getLogger();

   public static void handleInteract(final ServerboundInteractPacketWrapper var0, ServerGamePacketListener var1) {
      if (var1 instanceof ServerGamePacketListenerImpl) {
         final ServerGamePacketListenerImpl var2 = (ServerGamePacketListenerImpl)var1;
         PacketUtils.ensureRunningOnSameThread(var0, var2, var2.getPlayer().level());
         final ServerPlayer var3 = var2.getPlayer();
         if (!var3.isImmobile()) {
            final CraftPlayer var4 = var3.getBukkitEntity();
            final ServerLevel var5 = var3.level();
            final CraftServer var6 = var5.getServer().server;
            final Entity var7 = var0.getTarget(var5);
            var0.dispatch(new Handler() {
               public void onInteraction(InteractionHand var1) {
                  if (var0.isFakeInteraction()) {
                     EntityHandler var2 = ModelAPI.getNMSHandler().getEntityHandler();
                     var2.forceUseItem(var4, EquipmentSlot.HAND);
                     var2.forceUseItem(var4, EquipmentSlot.OFF_HAND);
                  }

               }

               public void onInteraction(InteractionHand var1, Vec3 var2) {
               }

               public void onAttack() {
               }
            });
            if (var7 != var3 || var3.isSpectator()) {
               var3.resetLastActionTime();
               var3.setShiftKeyDown(var0.isUsingSecondaryAction());
               final IVisualModel var8 = ModelAPI.getInteractionTracker().getModelRelay(var0.getOriginalId());
               if (var8 == null) {
                  HitboxEntity var9 = ModelAPI.getInteractionTracker().getHitbox(var0.getRelayedId());
                  if (var9 != null) {
                     var8 = var9.getJoint().getVisualModel();
                  }
               }

               if (var8 != null) {
                  final BaseEntity var11 = var8.getModeledEntity().getBase();
                  var0.dispatch(new Handler() {
                     public void onInteraction(InteractionHand var1) {
                        var6.getPluginManager().callEvent(new BaseEntityInteractEvent(var4, var11, var8, BaseEntityInteractEvent.Action.INTERACT, EquipmentSlot.HAND, var0.isUsingSecondaryAction(), var4.getInventory().getItem(EquipmentSlot.HAND), (Vector)null));
                        var6.getPluginManager().callEvent(new BaseEntityInteractEvent(var4, var11, var8, BaseEntityInteractEvent.Action.INTERACT, EquipmentSlot.OFF_HAND, var0.isUsingSecondaryAction(), var4.getInventory().getItem(EquipmentSlot.OFF_HAND), (Vector)null));
                     }

                     public void onInteraction(InteractionHand var1, Vec3 var2) {
                        Vector var3 = new Vector(var2.x, var2.y, var2.z);
                        var6.getPluginManager().callEvent(new BaseEntityInteractEvent(var4, var11, var8, BaseEntityInteractEvent.Action.INTERACT_ON, EquipmentSlot.HAND, var0.isUsingSecondaryAction(), var4.getInventory().getItem(EquipmentSlot.HAND), var3));
                        var6.getPluginManager().callEvent(new BaseEntityInteractEvent(var4, var11, var8, BaseEntityInteractEvent.Action.INTERACT_ON, EquipmentSlot.OFF_HAND, var0.isUsingSecondaryAction(), var4.getInventory().getItem(EquipmentSlot.OFF_HAND), var3));
                     }

                     public void onAttack() {
                        var6.getPluginManager().callEvent(new BaseEntityInteractEvent(var4, var11, var8, BaseEntityInteractEvent.Action.ATTACK, EquipmentSlot.HAND, var0.isUsingSecondaryAction(), var4.getInventory().getItem(EquipmentSlot.HAND), (Vector)null));
                     }
                  });
               }

               if (var7 == null) {
                  if (ServerInfo.IS_PAPER) {
                     var0.dispatch(new Handler() {
                        public void onInteraction(InteractionHand var1) {
                           PatchedServerGamePacketListener.callPlayerUseUnknownEntityEvent(var6, var4, var0, var1, (Vec3)null);
                        }

                        public void onInteraction(InteractionHand var1, Vec3 var2) {
                           PatchedServerGamePacketListener.callPlayerUseUnknownEntityEvent(var6, var4, var0, var1, var2);
                        }

                        public void onAttack() {
                           PatchedServerGamePacketListener.callPlayerUseUnknownEntityEvent(var6, var4, var0, InteractionHand.MAIN_HAND, (Vec3)null);
                        }
                     });
                  }
               } else if (var5.getWorldBorder().isWithinBounds(var7.blockPosition())) {
                  AABB var12 = var7.getBoundingBox();
                  if (!(var12.distanceToSqr(var3.getEyePosition()) >= var3.entityInteractionRange() * var3.entityInteractionRange())) {
                     var0.dispatch(new Handler() {
                        private void performInteraction(InteractionHand var1, PatchedServerGamePacketListener.EntityInteraction var2x, PlayerInteractEntityEvent var3x) {
                           ItemStack var4 = var3.getItemInHand(var1);
                           if (var4.isItemEnabled(var5.enabledFeatures())) {
                              ItemStack var5x = var4.copy();
                              ItemStack var6x = var3.getItemInHand(var1);
                              boolean var7x = var6x.getItem() == Items.LEAD && var7 instanceof Mob;
                              Item var8 = var3.getInventory().getSelectedItem().getItem();
                              var6.getPluginManager().callEvent(var3x);
                              if (var7 instanceof Bucketable && var7 instanceof LivingEntity && var8.asItem() == Items.WATER_BUCKET && (var3x.isCancelled() || var3.getInventory().getSelectedItem().getItem() != var8)) {
                                 if (var3.getBukkitEntity().canSee(var7.getBukkitEntity())) {
                                    ServerEntity var9 = var7.moonrise$getTrackedEntity().serverEntity;
                                    ArrayList var10 = new ArrayList();
                                    ServerPlayer var11 = var3;
                                    Objects.requireNonNull(var10);
                                    Objects.requireNonNull(var10);
                                    var9.sendPairingData(var11, var10::add);
                                    var3.connection.send(new ClientboundBundlePacket(var10));
                                 }

                                 var3.containerMenu.sendAllDataToRemote();
                              }

                              if (var7x && (var3x.isCancelled() || var3.getInventory().getSelectedItem().getItem() != var8)) {
                                 var2.send(new ClientboundSetEntityLinkPacket(var7, ((Mob)var7).getLeashHolder()));
                              }

                              if (var3x.isCancelled() || var3.getInventory().getSelectedItem().getItem() != var8) {
                                 List var12 = var7.getEntityData().packAll();
                                 if (var12 != null && var3.getBukkitEntity().canSee(var7.getBukkitEntity())) {
                                    var3.connection.send(new ClientboundSetEntityDataPacket(var7.getId(), var12));
                                 }

                                 if (var7 instanceof Allay) {
                                    Allay var14 = (Allay)var7;
                                    List var16 = (List)Arrays.stream(net.minecraft.world.entity.EquipmentSlot.values()).map((var1x) -> {
                                       return Pair.of(var1x, var14.getItemBySlot(var1x));
                                    }).collect(Collectors.toList());
                                    var2.send(new ClientboundSetEquipmentPacket(var7.getId(), var16));
                                    var3.containerMenu.sendAllDataToRemote();
                                 }
                              }

                              if (var3x.isCancelled()) {
                                 var3.containerMenu.sendAllDataToRemote();
                                 return;
                              }

                              InteractionResult var13 = var2x.run(var3, var7, var1);
                              if (!var6x.isEmpty() && var6x.getCount() <= -1) {
                                 var3.containerMenu.sendAllDataToRemote();
                              }

                              if (var13.consumesAction()) {
                                 CriteriaTriggers.PLAYER_INTERACTED_WITH_ENTITY.trigger(var3, var5x, var7);
                                 if (var13 instanceof Success) {
                                    Success var15 = (Success)var13;
                                    if (var15.swingSource() != SwingSource.NONE) {
                                       var3.swing(var1, var15.swingSource() == SwingSource.SERVER);
                                    }
                                 }
                              }
                           }

                        }

                        public void onInteraction(@NotNull InteractionHand var1) {
                           this.performInteraction(var1, Player::interactOn, new PlayerInteractEntityEvent(var2.getCraftPlayer(), var7.getBukkitEntity(), var1 == InteractionHand.OFF_HAND ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND));
                        }

                        public void onInteraction(@NotNull InteractionHand var1, @NotNull Vec3 var2x) {
                           this.performInteraction(var1, (var1x, var2xx, var3x) -> {
                              return var2xx.interactAt(var1x, var2x, var3x);
                           }, new PlayerInteractAtEntityEvent(var2.getCraftPlayer(), var7.getBukkitEntity(), new Vector(var2x.x, var2x.y, var2x.z), var1 == InteractionHand.OFF_HAND ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND));
                        }

                        public void onAttack() {
                           if (!(var7 instanceof ItemEntity) && !(var7 instanceof ExperienceOrb) && !(var7 instanceof AbstractArrow) && (var7 != var3 || var3.isSpectator())) {
                              ItemStack var1 = var3.getItemInHand(InteractionHand.MAIN_HAND);
                              if (var1.isItemEnabled(var5.enabledFeatures())) {
                                 var3.attack(var7);
                                 if (!var1.isEmpty() && var1.getCount() <= -1) {
                                    var3.containerMenu.sendAllDataToRemote();
                                 }
                              }
                           }

                        }
                     });
                  }
               }
            }
         }
      }

   }

   public static void handleUseItem(ServerboundUseItemPacket var0, ServerGamePacketListener var1, Consumer<InteractionResult> var2) {
      if (var1 instanceof ServerGamePacketListenerImpl) {
         ServerGamePacketListenerImpl var3 = (ServerGamePacketListenerImpl)var1;
         ServerPlayer var4 = var3.getPlayer();
         PacketUtils.ensureRunningOnSameThread(var0, var3, var4.level());
         if (!var4.isImmobile()) {
            boolean var5 = Boolean.TRUE.equals(ReflectionUtils.call(var3, NMSMethods.SERVER_GAME_PACKET_LISTENER_IMPL_checkLimit, var0.timestamp));
            if (var5) {
               var3.ackBlockChangesUpTo(var0.getSequence());
               ServerLevel var6 = var4.level();
               InteractionHand var7 = var0.getHand();
               ItemStack var8 = var4.getItemInHand(var7);
               var4.resetLastActionTime();
               if (!var8.isEmpty() && var8.isItemEnabled(var6.enabledFeatures())) {
                  float var9 = var4.getXRot();
                  float var10 = var4.getYRot();
                  double var11 = var4.getX();
                  double var13 = var4.getY() + (double)var4.getEyeHeight();
                  double var15 = var4.getZ();
                  Vec3 var17 = new Vec3(var11, var13, var15);
                  float var18 = Mth.cos(-var10 * 0.017453292F - 3.1415927F);
                  float var19 = Mth.sin(-var10 * 0.017453292F - 3.1415927F);
                  float var20 = -Mth.cos(-var9 * 0.017453292F);
                  float var21 = Mth.sin(-var9 * 0.017453292F);
                  float var22 = var19 * var20;
                  float var23 = var18 * var20;
                  double var24 = var4.gameMode.getGameModeForPlayer() == GameType.CREATIVE ? 5.0D : 4.5D;
                  Vec3 var26 = var17.add((double)var22 * var24, (double)var21 * var24, (double)var23 * var24);
                  BlockHitResult var27 = var4.level().clip(new ClipContext(var17, var26, Block.OUTLINE, Fluid.NONE, var4));
                  boolean var28;
                  if (var27 != null && var27.getType() == Type.BLOCK) {
                     BlockHitResult var31 = (BlockHitResult)var27;
                     if (var4.gameMode.firedInteract && var4.gameMode.interactPosition.equals(var31.getBlockPos()) && var4.gameMode.interactHand == var7 && ItemStack.isSameItemSameComponents(var4.gameMode.interactItemStack, var8)) {
                        var28 = var4.gameMode.interactResult;
                     } else {
                        PlayerInteractEvent var30 = CraftEventFactory.callPlayerInteractEvent(var4, Action.RIGHT_CLICK_BLOCK, var31.getBlockPos(), var31.getDirection(), var8, true, var7, var31.getLocation());
                        var28 = var30.useItemInHand() == Result.DENY;
                     }

                     var4.gameMode.firedInteract = false;
                  } else {
                     PlayerInteractEvent var29 = CraftEventFactory.callPlayerInteractEvent(var4, Action.RIGHT_CLICK_AIR, var8, var7);
                     var28 = var29.useItemInHand() == Result.DENY;
                  }

                  if (var28) {
                     var4.resyncUsingItem(var4);
                     var4.getBukkitEntity().updateInventory();
                     return;
                  }

                  var8 = var4.getItemInHand(var7);
                  if (var8.isEmpty()) {
                     return;
                  }

                  InteractionResult var32 = var4.gameMode.useItem(var4, var6, var8, var7);
                  var2.accept(var32);
                  if (var32 instanceof Success) {
                     Success var33 = (Success)var32;
                     if (var33.swingSource() != SwingSource.NONE) {
                        var4.swing(var7, var33.swingSource() == SwingSource.SERVER);
                     }
                  }
               }
            }
         }
      }

   }

   private static void callPlayerUseUnknownEntityEvent(CraftServer var0, CraftPlayer var1, ServerboundInteractPacketWrapper var2, InteractionHand var3, @Nullable Vec3 var4) {
      var0.getPluginManager().callEvent(new PlayerUseUnknownEntityEvent(var1, var2.getEntityId(), var2.isAttack(), var3 == InteractionHand.MAIN_HAND ? EquipmentSlot.HAND : EquipmentSlot.OFF_HAND, var4 != null ? new Vector(var4.x, var4.y, var4.z) : null));
   }

   @FunctionalInterface
   private interface EntityInteraction {
      InteractionResult run(ServerPlayer var1, Entity var2, InteractionHand var3);
   }
}
