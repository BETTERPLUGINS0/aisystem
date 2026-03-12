package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.UUID;

public class WrapperPlayClientResourcePackStatus extends PacketWrapper<WrapperPlayClientResourcePackStatus> {
   private UUID packId;
   private String hash;
   private WrapperPlayClientResourcePackStatus.Result result;

   public WrapperPlayClientResourcePackStatus(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientResourcePackStatus(WrapperPlayClientResourcePackStatus.Result result) {
      this(UUID.randomUUID(), result);
   }

   public WrapperPlayClientResourcePackStatus(UUID packId, WrapperPlayClientResourcePackStatus.Result result) {
      super((PacketTypeCommon)PacketType.Play.Client.RESOURCE_PACK_STATUS);
      this.packId = packId;
      this.result = result;
   }

   /** @deprecated */
   @Deprecated
   public WrapperPlayClientResourcePackStatus(String hash, WrapperPlayClientResourcePackStatus.Result result) {
      super((PacketTypeCommon)PacketType.Play.Client.RESOURCE_PACK_STATUS);
      this.hash = hash;
      this.result = result;
   }

   public void read() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
         this.packId = this.readUUID();
      }

      if (this.serverVersion.isOlderThan(ServerVersion.V_1_10)) {
         this.hash = this.readString(40);
      } else {
         this.hash = "";
      }

      int resultIndex = this.readVarInt();
      this.result = WrapperPlayClientResourcePackStatus.Result.VALUES[resultIndex];
   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
         this.writeUUID(this.packId);
      }

      if (this.serverVersion.isOlderThan(ServerVersion.V_1_10)) {
         this.writeString(this.hash, 40);
      }

      this.writeVarInt(this.result.ordinal());
   }

   public void copy(WrapperPlayClientResourcePackStatus wrapper) {
      this.packId = wrapper.packId;
      this.hash = wrapper.hash;
      this.result = wrapper.result;
   }

   public UUID getPackId() {
      return this.packId;
   }

   public void setPackId(UUID packId) {
      this.packId = packId;
   }

   public WrapperPlayClientResourcePackStatus.Result getResult() {
      return this.result;
   }

   public void setResult(WrapperPlayClientResourcePackStatus.Result result) {
      this.result = result;
   }

   public String getHash() {
      return this.hash;
   }

   public void setHash(String hash) {
      this.hash = hash;
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

      public static final WrapperPlayClientResourcePackStatus.Result[] VALUES = values();

      // $FF: synthetic method
      private static WrapperPlayClientResourcePackStatus.Result[] $values() {
         return new WrapperPlayClientResourcePackStatus.Result[]{SUCCESSFULLY_LOADED, DECLINED, FAILED_DOWNLOAD, ACCEPTED, DOWNLOADED, INVALID_URL, FAILED_RELOAD, DISCARDED};
      }
   }
}
