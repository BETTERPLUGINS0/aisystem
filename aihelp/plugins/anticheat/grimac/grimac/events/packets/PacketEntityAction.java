package ac.grim.grimac.events.packets;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.checks.impl.elytra.ElytraA;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerAbstract;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerPriority;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import ac.grim.grimac.utils.data.Pair;
import ac.grim.grimac.utils.data.packetentity.JumpableEntity;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;

public class PacketEntityAction extends PacketListenerAbstract {
   public PacketEntityAction() {
      super(PacketListenerPriority.LOW);
   }

   public boolean isPreVia() {
      return true;
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION) {
         WrapperPlayClientEntityAction action = new WrapperPlayClientEntityAction(event);
         GrimPlayer player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
         if (player == null) {
            return;
         }

         switch(action.getAction()) {
         case START_SPRINTING:
            player.isSprinting = true;
            break;
         case STOP_SPRINTING:
            player.isSprinting = false;
            break;
         case START_SNEAKING:
            player.isSneaking = true;
            break;
         case STOP_SNEAKING:
            player.isSneaking = false;
            break;
         case START_FLYING_WITH_ELYTRA:
            if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_9)) {
               return;
            }

            if (!player.onGround && !player.lastOnGround) {
               if (player.getClientVersion().isOlderThan(ClientVersion.V_1_15)) {
                  return;
               }

               ((ElytraA)player.checkManager.getPostPredictionCheck(ElytraA.class)).onStartGliding(event);
               if (player.canGlide()) {
                  player.isGliding = true;
                  player.pointThreeEstimator.updatePlayerGliding();
               } else {
                  player.getSetbackTeleportUtil().executeNonSimulatingForceResync();
                  if (player.platformPlayer != null) {
                     player.platformPlayer.setSneaking(!player.platformPlayer.isSneaking());
                  }

                  event.setCancelled(true);
                  player.onPacketCancel();
               }
            } else {
               player.getSetbackTeleportUtil().executeNonSimulatingForceResync();
               if (player.platformPlayer != null) {
                  player.platformPlayer.setSneaking(!player.platformPlayer.isSneaking());
               }

               event.setCancelled(true);
               player.onPacketCancel();
            }
            break;
         case START_JUMPING_WITH_HORSE:
            PacketEntity riding = player.compensatedEntities.self.getRiding();
            if (riding instanceof JumpableEntity) {
               JumpableEntity jumpable = (JumpableEntity)riding;
               if (player.vehicleData.pendingJumps.size() >= 20) {
                  return;
               }

               player.vehicleData.pendingJumps.add(new Pair(action.getJumpBoost(), jumpable));
            }
         }
      }

   }
}
