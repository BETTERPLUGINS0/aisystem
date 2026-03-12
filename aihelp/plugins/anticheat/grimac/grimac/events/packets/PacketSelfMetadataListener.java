package ac.grim.grimac.events.packets;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerAbstract;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerPriority;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.InteractionHand;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUseBed;
import ac.grim.grimac.utils.nmsutil.WatchableIndexUtil;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PacketSelfMetadataListener extends PacketListenerAbstract {
   public PacketSelfMetadataListener() {
      super(PacketListenerPriority.HIGH);
   }

   public void onPacketSend(PacketSendEvent event) {
      GrimPlayer player;
      List var10000;
      if (event.getPacketType() == PacketType.Play.Server.ENTITY_METADATA) {
         WrapperPlayServerEntityMetadata entityMetadata = new WrapperPlayServerEntityMetadata(event);
         player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
         if (player == null) {
            return;
         }

         if (entityMetadata.getEntityId() == player.entityID) {
            boolean hasSendTransaction = false;
            if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_14)) {
               List<EntityData<?>> metadataStuff = entityMetadata.getEntityMetadata();
               metadataStuff.removeIf((element) -> {
                  return element.getIndex() == 6;
               });
               entityMetadata.setEntityMetadata(metadataStuff);
               event.markForReEncode(true);
            }

            EntityData<?> watchable = WatchableIndexUtil.getIndex(entityMetadata.getEntityMetadata(), 0);
            boolean isActive;
            boolean isOffhand;
            if (watchable != null) {
               Object zeroBitField = watchable.getValue();
               if (zeroBitField instanceof Byte) {
                  byte field = (Byte)zeroBitField;
                  isActive = (field & 128) == 128 && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9);
                  isOffhand = (field & 16) == 16;
                  boolean isSprinting = (field & 8) == 8;
                  if (!hasSendTransaction) {
                     player.sendTransaction();
                  }

                  hasSendTransaction = true;
                  player.latencyUtils.addRealTimeTask(player.lastTransactionSent.get(), () -> {
                     player.isSwimming = isOffhand;
                     player.lastSprinting = isSprinting;
                     if (player.isGliding != isActive) {
                        player.pointThreeEstimator.updatePlayerGliding();
                     }

                     player.isGliding = isActive;
                  });
               }
            }

            EntityData riptide;
            if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
               riptide = WatchableIndexUtil.getIndex(entityMetadata.getEntityMetadata(), 5);
               if (riptide != null) {
                  Object gravityObject = riptide.getValue();
                  if (gravityObject instanceof Boolean) {
                     if (!hasSendTransaction) {
                        player.sendTransaction();
                     }

                     hasSendTransaction = true;
                     player.latencyUtils.addRealTimeTask(player.lastTransactionSent.get(), () -> {
                        player.playerEntityHasGravity = !(Boolean)gravityObject;
                     });
                  }
               }
            }

            if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_17)) {
               riptide = WatchableIndexUtil.getIndex(entityMetadata.getEntityMetadata(), 7);
               if (riptide != null) {
                  if (!hasSendTransaction) {
                     player.sendTransaction();
                  }

                  hasSendTransaction = true;
                  player.latencyUtils.addRealTimeTask(player.lastTransactionSent.get(), () -> {
                     player.powderSnowFrozenTicks = (Integer)riptide.getValue();
                  });
               }
            }

            if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_14)) {
               byte id;
               if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_14_4)) {
                  id = 12;
               } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_16_5)) {
                  id = 13;
               } else {
                  id = 14;
               }

               EntityData<?> bedObject = WatchableIndexUtil.getIndex(entityMetadata.getEntityMetadata(), id);
               if (bedObject != null) {
                  if (!hasSendTransaction) {
                     player.sendTransaction();
                  }

                  hasSendTransaction = true;
                  player.latencyUtils.addRealTimeTask(player.lastTransactionSent.get(), () -> {
                     Optional<Vector3i> bed = (Optional)bedObject.getValue();
                     if (bed.isPresent()) {
                        player.isInBed = true;
                        Vector3i bedPos = (Vector3i)bed.get();
                        player.bedPosition = new Vector3d((double)bedPos.getX() + 0.5D, (double)bedPos.getY(), (double)bedPos.getZ() + 0.5D);
                     } else {
                        player.isInBed = false;
                     }

                  });
               }
            }

            if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13) && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9)) {
               riptide = WatchableIndexUtil.getIndex(entityMetadata.getEntityMetadata(), PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_17) ? 8 : 7);
               if (riptide != null && riptide.getValue() instanceof Byte) {
                  boolean isRiptiding = ((Byte)riptide.getValue() & 4) == 4;
                  if (!hasSendTransaction) {
                     player.sendTransaction();
                  }

                  hasSendTransaction = true;
                  player.latencyUtils.addRealTimeTask(player.lastTransactionSent.get(), () -> {
                     player.isRiptidePose = isRiptiding;
                  });
                  if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) && PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
                     isActive = ((Byte)riptide.getValue() & 1) > 0;
                     isOffhand = ((Byte)riptide.getValue() & 2) > 0;
                     player.latencyUtils.addRealTimeTask(player.lastTransactionSent.get(), () -> {
                        player.packetStateData.setSlowedByUsingItem(false);
                     });
                     int markedTransaction = player.lastTransactionSent.get();
                     player.latencyUtils.addRealTimeTask(player.lastTransactionSent.get() + 1, () -> {
                        if (player.packetStateData.slowedByUsingItemTransaction < markedTransaction) {
                           PacketPlayerDigging.handleUseItem(player, isOffhand ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
                           player.packetStateData.setSlowedByUsingItem(isActive);
                           if (isActive) {
                              player.packetStateData.itemInUseHand = isOffhand ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
                           }
                        }

                     });
                     var10000 = event.getTasksAfterSend();
                     Objects.requireNonNull(player);
                     var10000.add(player::sendTransaction);
                  }
               }
            }
         }
      }

      if (event.getPacketType() == PacketType.Play.Server.USE_BED) {
         WrapperPlayServerUseBed bed = new WrapperPlayServerUseBed(event);
         player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
         if (player != null && player.entityID == bed.getEntityId()) {
            player.latencyUtils.addRealTimeTask(player.lastTransactionSent.get(), () -> {
               player.isInBed = true;
               player.bedPosition = new Vector3d((double)bed.getPosition().getX() + 0.5D, (double)bed.getPosition().getY(), (double)bed.getPosition().getZ() + 0.5D);
            });
         }
      }

      if (event.getPacketType() == PacketType.Play.Server.ENTITY_ANIMATION) {
         WrapperPlayServerEntityAnimation animation = new WrapperPlayServerEntityAnimation(event);
         player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
         if (player != null && player.entityID == animation.getEntityId() && animation.getType() == WrapperPlayServerEntityAnimation.EntityAnimationType.WAKE_UP) {
            player.latencyUtils.addRealTimeTask(player.lastTransactionSent.get() + 1, () -> {
               player.isInBed = false;
            });
            var10000 = event.getTasksAfterSend();
            Objects.requireNonNull(player);
            var10000.add(player::sendTransaction);
         }
      }

   }
}
