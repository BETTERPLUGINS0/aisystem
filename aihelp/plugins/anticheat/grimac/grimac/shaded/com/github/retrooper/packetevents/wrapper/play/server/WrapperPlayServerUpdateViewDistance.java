package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerUpdateViewDistance extends PacketWrapper<WrapperPlayServerUpdateViewDistance> {
   private int viewDistance;

   public WrapperPlayServerUpdateViewDistance(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerUpdateViewDistance(int viewDistance) {
      super((PacketTypeCommon)PacketType.Play.Server.UPDATE_VIEW_DISTANCE);
      this.viewDistance = viewDistance;
   }

   public void read() {
      this.viewDistance = this.readVarInt();
   }

   public void write() {
      this.writeVarInt(this.viewDistance);
   }

   public void copy(WrapperPlayServerUpdateViewDistance wrapper) {
      this.viewDistance = wrapper.viewDistance;
   }

   public int getViewDistance() {
      return this.viewDistance;
   }

   public void setViewDistance(int viewDistance) {
      this.viewDistance = viewDistance;
   }
}
