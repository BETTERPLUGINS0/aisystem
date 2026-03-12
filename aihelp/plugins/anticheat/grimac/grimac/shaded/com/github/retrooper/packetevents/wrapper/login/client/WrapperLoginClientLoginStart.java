package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.login.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.crypto.SignatureData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class WrapperLoginClientLoginStart extends PacketWrapper<WrapperLoginClientLoginStart> {
   private String username;
   @Nullable
   private SignatureData signatureData;
   @Nullable
   private UUID playerUUID;

   public WrapperLoginClientLoginStart(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperLoginClientLoginStart(ClientVersion clientVersion, String username) {
      this(clientVersion, username, (SignatureData)null, (UUID)null);
   }

   public WrapperLoginClientLoginStart(ClientVersion clientVersion, String username, @Nullable SignatureData signatureData) {
      this(clientVersion, username, signatureData, (UUID)null);
   }

   public WrapperLoginClientLoginStart(ClientVersion clientVersion, String username, @Nullable SignatureData signatureData, @Nullable UUID playerUUID) {
      super(PacketType.Login.Client.LOGIN_START.getId(), clientVersion);
      this.username = username;
      this.signatureData = signatureData;
      this.playerUUID = playerUUID;
   }

   public void read() {
      this.username = this.readString(16);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
         if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_19_3)) {
            this.signatureData = (SignatureData)this.readOptional(PacketWrapper::readSignatureData);
         }

         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_2)) {
            this.playerUUID = this.readUUID();
         } else if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_1)) {
            this.playerUUID = (UUID)this.readOptional(PacketWrapper::readUUID);
         }
      }

   }

   public void write() {
      this.writeString(this.username, 16);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
         if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_19_3)) {
            this.writeOptional(this.signatureData, PacketWrapper::writeSignatureData);
         }

         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_2)) {
            this.writeUUID((UUID)Objects.requireNonNull(this.playerUUID, "playerUUID is required for >= 1.20.2"));
         } else if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_1)) {
            this.writeOptional(this.playerUUID, PacketWrapper::writeUUID);
         }
      }

   }

   public void copy(WrapperLoginClientLoginStart wrapper) {
      this.username = wrapper.username;
      this.signatureData = wrapper.signatureData;
      this.playerUUID = wrapper.playerUUID;
   }

   public String getUsername() {
      return this.username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public Optional<SignatureData> getSignatureData() {
      return Optional.ofNullable(this.signatureData);
   }

   public void setSignatureData(@Nullable SignatureData signatureData) {
      this.signatureData = signatureData;
   }

   public Optional<UUID> getPlayerUUID() {
      return Optional.ofNullable(this.playerUUID);
   }

   public void setPlayerUUID(@Nullable UUID playerUUID) {
      this.playerUUID = playerUUID;
   }
}
