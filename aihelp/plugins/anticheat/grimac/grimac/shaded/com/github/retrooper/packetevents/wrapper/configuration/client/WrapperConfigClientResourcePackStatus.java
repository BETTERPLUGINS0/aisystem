package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.UUID;

public class WrapperConfigClientResourcePackStatus extends PacketWrapper<WrapperConfigClientResourcePackStatus> {
   private UUID packId;
   private WrapperConfigClientResourcePackStatus.Result result;

   public WrapperConfigClientResourcePackStatus(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperConfigClientResourcePackStatus(WrapperConfigClientResourcePackStatus.Result result) {
      this(UUID.randomUUID(), result);
   }

   public WrapperConfigClientResourcePackStatus(UUID packId, WrapperConfigClientResourcePackStatus.Result result) {
      super((PacketTypeCommon)PacketType.Configuration.Client.RESOURCE_PACK_STATUS);
      this.packId = packId;
      this.result = result;
   }

   public void read() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
         this.packId = this.readUUID();
      }

      this.result = WrapperConfigClientResourcePackStatus.Result.VALUES[this.readVarInt()];
   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
         this.writeUUID(this.packId);
      }

      this.writeVarInt(this.result.ordinal());
   }

   public void copy(WrapperConfigClientResourcePackStatus wrapper) {
      this.packId = wrapper.packId;
      this.result = wrapper.result;
   }

   public UUID getPackId() {
      return this.packId;
   }

   public void setPackId(UUID packId) {
      this.packId = packId;
   }

   public WrapperConfigClientResourcePackStatus.Result getResult() {
      return this.result;
   }

   public void setResult(WrapperConfigClientResourcePackStatus.Result result) {
      this.result = result;
   }

   public static enum Result {
      SUCCESSFULLY_LOADED,
      DECLINED,
      FAILED_DOWNLOAD,
      ACCEPTED,
      DOWNLOADED,
      INVALID_URL,
      FAILED_RELOAD,
      DISCARDED;

      public static final WrapperConfigClientResourcePackStatus.Result[] VALUES = values();

      // $FF: synthetic method
      private static WrapperConfigClientResourcePackStatus.Result[] $values() {
         return new WrapperConfigClientResourcePackStatus.Result[]{SUCCESSFULLY_LOADED, DECLINED, FAILED_DOWNLOAD, ACCEPTED, DOWNLOADED, INVALID_URL, FAILED_RELOAD, DISCARDED};
      }
   }
}
