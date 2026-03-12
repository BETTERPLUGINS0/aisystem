package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.UUID;

public class WrapperPlayServerResourcePackRemove extends PacketWrapper<WrapperPlayServerResourcePackRemove> {
   @Nullable
   private UUID packId;

   public WrapperPlayServerResourcePackRemove(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerResourcePackRemove(@Nullable UUID packId) {
      super((PacketTypeCommon)PacketType.Play.Server.RESOURCE_PACK_REMOVE);
      this.packId = packId;
   }

   public void read() {
      this.packId = (UUID)this.readOptional(PacketWrapper::readUUID);
   }

   public void write() {
      this.writeOptional(this.packId, PacketWrapper::writeUUID);
   }

   public void copy(WrapperPlayServerResourcePackRemove wrapper) {
      this.packId = wrapper.packId;
   }

   @Nullable
   public UUID getPackId() {
      return this.packId;
   }

   public void setPackId(@Nullable UUID packId) {
      this.packId = packId;
   }
}
