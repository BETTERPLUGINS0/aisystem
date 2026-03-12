package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.UUID;

public class WrapperConfigServerResourcePackRemove extends PacketWrapper<WrapperConfigServerResourcePackRemove> {
   @Nullable
   private UUID packId;

   public WrapperConfigServerResourcePackRemove(PacketSendEvent event) {
      super(event);
   }

   public WrapperConfigServerResourcePackRemove(@Nullable UUID packId) {
      super((PacketTypeCommon)PacketType.Configuration.Server.RESOURCE_PACK_REMOVE);
      this.packId = packId;
   }

   public void read() {
      this.packId = (UUID)this.readOptional(PacketWrapper::readUUID);
   }

   public void write() {
      this.writeOptional(this.packId, PacketWrapper::writeUUID);
   }

   public void copy(WrapperConfigServerResourcePackRemove wrapper) {
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
