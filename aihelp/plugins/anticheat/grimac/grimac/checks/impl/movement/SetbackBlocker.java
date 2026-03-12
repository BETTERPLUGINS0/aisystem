package ac.grim.grimac.checks.impl.movement;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;

public class SetbackBlocker extends Check implements PacketCheck {
   public SetbackBlocker(GrimPlayer playerData) {
      super(playerData);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (!this.player.disableGrim) {
         if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY && this.player.getSetbackTeleportUtil().cheatVehicleInterpolationDelay > 0) {
            event.setCancelled(true);
         }

         if (!this.player.packetStateData.lastPacketWasTeleport) {
            if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
               if (this.player.getSetbackTeleportUtil().shouldBlockMovement()) {
                  event.setCancelled(true);
               }

               if (this.player.inVehicle() && event.getPacketType() != PacketType.Play.Client.PLAYER_ROTATION && !this.player.packetStateData.lastPacketWasTeleport) {
                  event.setCancelled(true);
               }

               if (this.player.isInBed && (new Vector3d(this.player.x, this.player.y, this.player.z)).distanceSquared(this.player.bedPosition) > 1.0D) {
                  event.setCancelled(true);
               }

               if (this.player.compensatedEntities.self.isDead) {
                  event.setCancelled(true);
               }
            }

            if (event.getPacketType() == PacketType.Play.Client.VEHICLE_MOVE) {
               if (this.player.getSetbackTeleportUtil().shouldBlockMovement()) {
                  event.setCancelled(true);
               }

               if (!this.player.inVehicle()) {
                  event.setCancelled(true);
               }

               if (this.player.isInBed) {
                  event.setCancelled(true);
               }

               if (this.player.compensatedEntities.self.isDead) {
                  event.setCancelled(true);
               }
            }

         }
      }
   }
}
