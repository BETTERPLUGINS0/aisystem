package ac.grim.grimac.checks.impl.packetorder;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;

@CheckData(
   name = "PacketOrderB",
   description = "Did not swing for attack"
)
public class PacketOrderB extends Check implements PacketCheck {
   private final boolean is1_9;
   private final boolean exempt;
   private boolean sentAnimationSinceLastAttack;
   private boolean sentAttack;
   private boolean sentAnimation;
   private boolean sentSlotSwitch;

   public PacketOrderB(GrimPlayer player) {
      super(player);
      this.is1_9 = this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9);
      this.exempt = this.player.getClientVersion().isOlderThan(ClientVersion.V_1_9) && PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9);
      this.sentAnimationSinceLastAttack = this.player.getClientVersion().isNewerThan(ClientVersion.V_1_8);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (!this.exempt) {
         if (event.getPacketType() == PacketType.Play.Client.ANIMATION) {
            this.sentAnimationSinceLastAttack = this.sentAnimation = true;
            this.sentAttack = this.sentSlotSwitch = false;
         } else {
            if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
               WrapperPlayClientInteractEntity packet = new WrapperPlayClientInteractEntity(event);
               if (packet.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                  label44: {
                     this.sentAttack = true;
                     if (this.is1_9) {
                        if (this.sentAnimationSinceLastAttack) {
                           break label44;
                        }
                     } else if (this.sentAnimation) {
                        break label44;
                     }

                     this.sentAttack = false;
                     if (this.flagAndAlert("pre-attack") && this.shouldModifyPackets()) {
                        event.setCancelled(true);
                        this.player.onPacketCancel();
                     }
                  }

                  this.sentAnimationSinceLastAttack = this.sentAnimation = this.sentSlotSwitch = false;
                  return;
               }
            }

            if (event.getPacketType() == PacketType.Play.Client.HELD_ITEM_CHANGE && !this.is1_9 && !this.sentSlotSwitch) {
               this.sentSlotSwitch = true;
            } else {
               if (!isAsync(event.getPacketType())) {
                  if (this.sentAttack && this.is1_9) {
                     this.flagAndAlert("post-attack");
                  }

                  this.sentAttack = this.sentAnimation = this.sentSlotSwitch = false;
               }

            }
         }
      }
   }
}
