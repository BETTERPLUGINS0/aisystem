package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;

@CheckData(
   name = "BadPacketsQ"
)
public class BadPacketsQ extends Check implements PacketCheck {
   public BadPacketsQ(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION) {
         WrapperPlayClientEntityAction wrapper = new WrapperPlayClientEntityAction(event);
         if (Math.abs(wrapper.getJumpBoost()) > 100 || wrapper.getEntityId() != this.player.entityID || wrapper.getAction() != WrapperPlayClientEntityAction.Action.START_JUMPING_WITH_HORSE && wrapper.getJumpBoost() != 0) {
            int var10001 = wrapper.getJumpBoost();
            if (this.flagAndAlert("boost=" + var10001 + ", action=" + String.valueOf(wrapper.getAction()) + ", entity=" + wrapper.getEntityId()) && this.shouldModifyPackets()) {
               event.setCancelled(true);
               this.player.onPacketCancel();
            }
         }
      }

   }
}
