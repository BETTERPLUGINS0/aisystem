package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;

@CheckData(
   name = "BadPacketsG",
   description = "Sent duplicate sneaking status"
)
public class BadPacketsG extends Check implements PacketCheck {
   private boolean lastSneaking;
   private boolean respawn;

   public BadPacketsG(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION) {
         WrapperPlayClientEntityAction packet = new WrapperPlayClientEntityAction(event);
         if (packet.getAction() == WrapperPlayClientEntityAction.Action.START_SNEAKING) {
            if (this.lastSneaking && !this.respawn) {
               if (this.flagAndAlert("state=true") && this.shouldModifyPackets()) {
                  event.setCancelled(true);
                  this.player.onPacketCancel();
               }
            } else {
               this.lastSneaking = true;
            }

            this.respawn = false;
         } else if (packet.getAction() == WrapperPlayClientEntityAction.Action.STOP_SNEAKING) {
            if (!this.lastSneaking && !this.respawn) {
               if (this.flagAndAlert("state=false") && this.shouldModifyPackets()) {
                  event.setCancelled(true);
                  this.player.onPacketCancel();
               }
            } else {
               this.lastSneaking = false;
            }

            this.respawn = false;
         }
      }

   }

   public void handleRespawn() {
      this.respawn = true;
   }
}
