package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerAcknowledgeBlockChanges extends PacketWrapper<WrapperPlayServerAcknowledgeBlockChanges> {
   private int sequence;

   public WrapperPlayServerAcknowledgeBlockChanges(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerAcknowledgeBlockChanges(int sequence) {
      super((PacketTypeCommon)PacketType.Play.Server.ACKNOWLEDGE_BLOCK_CHANGES);
      this.sequence = sequence;
   }

   public void read() {
      this.sequence = this.readVarInt();
   }

   public void write() {
      this.writeVarInt(this.sequence);
   }

   public void copy(WrapperPlayServerAcknowledgeBlockChanges packet) {
      this.sequence = packet.sequence;
   }

   public int getSequence() {
      return this.sequence;
   }

   public void setSequence(int sequence) {
      this.sequence = sequence;
   }
}
