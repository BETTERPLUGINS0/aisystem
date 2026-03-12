package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerChunkBatchBegin extends PacketWrapper<WrapperPlayServerChunkBatchBegin> {
   public WrapperPlayServerChunkBatchBegin(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerChunkBatchBegin() {
      super((PacketTypeCommon)PacketType.Play.Server.CHUNK_BATCH_BEGIN);
   }
}
