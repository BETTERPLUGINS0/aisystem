package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;

@CheckData(
   name = "BadPacketsF",
   description = "Sent duplicate sprinting status"
)
public class BadPacketsF extends Check implements PacketCheck {
   public boolean lastSprinting;
   public boolean exemptNext = true;

   public BadPacketsF(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION) {
         WrapperPlayClientEntityAction packet = new WrapperPlayClientEntityAction(event);
         if (packet.getAction() == WrapperPlayClientEntityAction.Action.START_SPRINTING) {
            if (this.lastSprinting) {
               if (this.exemptNext) {
                  this.exemptNext = false;
                  return;
               }

               if (this.flagAndAlert("state=true") && this.shouldModifyPackets()) {
                  event.setCancelled(true);
                  this.player.onPacketCancel();
               }
            }

            this.lastSprinting = true;
         } else if (packet.getAction() == WrapperPlayClientEntityAction.Action.STOP_SPRINTING) {
            if (!this.lastSprinting) {
               if (this.exemptNext) {
                  this.exemptNext = false;
                  return;
               }

               if (this.flagAndAlert("state=false") && this.shouldModifyPackets()) {
                  event.setCancelled(true);
                  this.player.onPacketCancel();
               }
            }

            this.lastSprinting = false;
         }
      }

   }
}
