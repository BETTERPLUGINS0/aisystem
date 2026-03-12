package ac.grim.grimac.checks.impl.packetorder;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;

@CheckData(
   name = "PacketOrderO",
   experimental = true
)
public class PacketOrderO extends Check implements PacketCheck {
   private boolean flying;

   public PacketOrderO(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.CLIENT_TICK_END) {
         this.flying = false;
      }

      if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType()) && this.player.supportsEndTick() && !this.player.packetStateData.lastPacketWasTeleport) {
         this.flying = true;
      } else {
         if (this.flying && !isAsync(event.getPacketType()) && event.getPacketType() != PacketType.Play.Client.VEHICLE_MOVE) {
            if (this.player.inVehicle() && event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION) {
               WrapperPlayClientEntityAction.Action action = (new WrapperPlayClientEntityAction(event)).getAction();
               if (action == WrapperPlayClientEntityAction.Action.START_SPRINTING || action == WrapperPlayClientEntityAction.Action.STOP_SPRINTING) {
                  return;
               }
            }

            this.flagAndAlert("type=" + String.valueOf(event.getPacketType()));
         }

      }
   }
}
